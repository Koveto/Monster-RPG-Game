package game;

import game.gameObjects.AnimatedSpritedObject;
import game.gameObjects.Battler;
import game.gameObjects.DoublySpritedObject;
import game.gameObjects.Enemy;
import game.gameObjects.Entity;
import game.gameObjects.GameObject;
import game.gameObjects.PathedAnimatedSpritedObject;
import game.gameObjects.Player;
import game.gameObjects.RatioBar;
import game.gameObjects.Rectangle;
import game.gameObjects.SpritedObject;
import game.listeners.KeyboardListener;
import game.shuntingyardresources.FileReader;
import game.shuntingyardresources.InfixTranslator;
import game.shuntingyardresources.LinkedQueue;
import game.shuntingyardresources.PostfixCalculator;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Script
 * @author Kobe Goodwin
 * @version 8/20/2023
 */
public class Script {
    
    private Battle battle;
    private Room room;
    private DialogueBox dialogueBox;
    private GameObject[] gameObjects;
    private GameObject current;
    private String scriptPath;
    private int index, delay;
    private int[] storedNums;
    private long last = 0;
    private boolean storedBoolean;
    
    public Script( String scriptPath, Room room, Battle battle, 
            GameObject[] gameObjects, DialogueBox dialogueBox ) {
        
        this.scriptPath = scriptPath;
        this.room = room;
        this.battle = battle;
        this.gameObjects = gameObjects;
        this.dialogueBox = dialogueBox;
        index = parseScriptFile(scriptPath, 0);
        storedNums = new int[0];
        
    }
    
    public void update( ) {
        
        if (index == -1) return;
        if (System.currentTimeMillis() - last < delay) return;
        index = parseScriptFile(scriptPath, index);
        
    }
    
    private int parseScriptFile( String scriptPath, int lineNum ) {
        
        try {
            File f = new File(System.getProperty("user.dir") + "\\src\\game\\" + scriptPath);
            Scanner scan = new Scanner(f);
            
            int i = 0;
            
            while (scan.hasNextLine()) {
                
                if (i++ < lineNum) {
                    scan.nextLine();
                    continue;
                }
                
                delay = 0;
                
                String line = scan.nextLine();
                
                if (storedNums == null) storedNums = new int[0];
                
                if (line.startsWith("//")) continue;
                
                if (line.startsWith("Until")) {
                    if (line.substring(6).startsWith("!")) {
                        if (checkIf(line.substring(7)) != -1) return i - 1;
                    } else {
                        if (checkIf(line.substring(6)) == -1) return i - 1;
                    }
                }
                
                if (line.startsWith("Repeat Until")) {
                    if (checkIf(line.substring(13)) == -1) return i - 2;
                }
                
                if (line.startsWith("If")) {
                    if (line.charAt(3) == '!') {
                        int len = checkIf(line.substring(4));
                        if (len == -1) {
                            String next = line.split(" ")[2];
                            line = line.substring(line.indexOf(next));
                        } else continue;
                    } else {
                        int len = checkIf(line.substring(3));
                        if (len != -1) {
                            line = line.substring(4 + len);
                        } else continue;
                    }
                }
                
                if (line.startsWith(">  Object")) {
                    current = gameObjects[Integer.parseInt(line.substring(10, 12))];
                    continue;
                }
                
                if (line.startsWith("Wait")) {
                    last = System.currentTimeMillis();
                    delay = Integer.parseInt(line.substring(5));
                    return i;
                }
                
                if (line.startsWith("Music")) {
                    String[] substrings = line.split(" ");
                    Game.getSound().play(substrings[1], Boolean.valueOf(substrings[2]));
                }
                
                if (line.startsWith("Test")) {System.out.println(line);}
                
                if (line.startsWith("Scroll Speed")) {
                    Game.setScrollSpeed(Integer.parseInt(line.substring(13)));
                }
                
                if (line.startsWith("Dialogue")) {
                    String[] substrings = line.split(",");
                    ArrayList<DialogueTrigger> dt = DialogueHandler.parseDialogueFile(substrings[0].substring(9));
                    ArrayList<DialogueTrigger> newDt = new ArrayList();
                    for (DialogueTrigger d : dt) {
                        if (d.getDirection() == 0) {
                            newDt.add(d);
                        }
                    }
                    int num = Integer.parseInt(substrings[1]);
                    dialogueBox.newMessage(newDt.get(num).getTexts(), newDt.get(num).getFaces());
                }
                
                if (line.startsWith("Move")) {
                    String[] substrings = line.split(",");
                    if (substrings[0].substring(5).startsWith("s")) {
                        current.setX(storedNums[Integer.parseInt(substrings[0].substring(6))]);
                    } else {
                        current.setX(Integer.parseInt(substrings[0].substring(5)));
                    }
                    if (substrings[1].startsWith("s")) {
                        current.setY(storedNums[Integer.parseInt(substrings[1].substring(1))]);
                    } else current.setY(Integer.parseInt(substrings[1]));
                }
                
                if (line.startsWith("X ")) {
                    if (line.substring(2).startsWith("s")) {
                        current.setX(storedNums[Integer.parseInt(line.substring(3))]);
                    } else {
                        current.setX(Integer.parseInt(line.substring(2)));
                    }
                }
                if (line.startsWith("Y ")) {
                    if (line.substring(2).startsWith("s")) {
                        current.setY(storedNums[Integer.parseInt(line.substring(3))]);
                    } else {
                        current.setY(Integer.parseInt(line.substring(2)));
                    }
                }
                
                if (line.startsWith("Store")) {
                    String l = line.substring(6);
                    if (l.equals("Clear")) {
                        storedNums = new int[0];
                    } else if (l.equals("True")) {
                        storedBoolean = true;
                    } else if (l.equals("False")) {
                        storedBoolean = false;
                    } else if (l.equals("HP")) {
                        try {
                            Battler b = (Battler) current;
                            storedNums = Game.addToIntArray(storedNums, b.getHP());
                        } catch (ClassCastException e) {
                            System.out.println("Cannot cast to Battler");
                        }
                    } else if (l.equals("X")) {
                        storedNums = Game.addToIntArray(storedNums, current.getX());
                    } else if (l.equals("Y")) {
                        storedNums = Game.addToIntArray(storedNums, current.getY());
                    } else if (l.equals("Width")) {
                        try {
                            RatioBar rb = (RatioBar) current;
                            storedNums = Game.addToIntArray(storedNums, rb.getWidth());
                        } catch (ClassCastException e) {}
                        try {
                            PathedAnimatedSpritedObject paso = (PathedAnimatedSpritedObject) current;
                            storedNums = Game.addToIntArray(storedNums, paso.getSprite().getWidth());
                        } catch (ClassCastException e) {}
                    } else {
                        while (l.contains("s")) {
                            int indexOfS = l.indexOf("s");
                            l = l.replace("s" + String.valueOf(l.charAt(indexOfS + 1)), String.valueOf(storedNums[Integer.parseInt(l.substring(indexOfS + 1, indexOfS + 2))]));
                        }
                        LinkedQueue<String> infix = FileReader.createQueueFromLine(l);
                        LinkedQueue<String> postfix = InfixTranslator.infixToPostfix(infix);
                        if (storedNums == null) storedNums = new int[0];
                        storedNums = Game.addToIntArray(storedNums, (int) PostfixCalculator.evaluatePostfix(postfix, "x", 5.0));
                    }
                }
                
                
                
                
                
                
                
                
                
                // BATTLE METHODS
                if (line.startsWith("B  Set damage number sprite")) {
                    battle.setDamageNumberSprite(Integer.parseInt(line.substring(28)));
                } if (line.startsWith("B  Resize battle rect")) {
                    battle.startResizing();
                } if (line.startsWith("B  End battle")) {
                    battle.startBattleEnd();
                } if (line.startsWith("B  Wait on text bubble")) {
                    battle.waitOnTextBubble();
                } if (line.startsWith("B  Start battle box growing")) {
                    battle.startBattleBoxGrowing();
                }
                
                
                
                
                // RATIOBAR METHODS
                try {
                    RatioBar rb = (RatioBar) current;
                    if (line.startsWith("Slide to numerator")) {
                        String sub = line.substring(19);
                        int num = 0;
                        if (sub.contains("-")) {
                            String num0 = sub.split(" - ")[0];
                            String num1 = sub.split(" - ")[1];
                            if (num0.equals("s")) num0 = String.valueOf(storedNums[0]);
                            if (num1.equals("s")) num1 = String.valueOf(storedNums[0]);
                            num = Integer.parseInt(num0) - Integer.parseInt(num1);
                        } else {
                            num = Integer.parseInt(sub);
                        }
                        rb.slideToNumerator(num);
                    }
                } catch (ClassCastException e) {}
                
                
                
                
                // BATTLER METHODS
                try {
                    Battler b = (Battler) current;
                    if (line.startsWith("Take damage")) b.takeDamage(Integer.valueOf(line.substring(12)));
                } catch (ClassCastException e) {}
                
                
                
                
                // PLAYER METHODS
                try {
                    Player p = (Player) current;
                    if (line.startsWith("Switch to overworld")) p.switchToOverworld();
                    if (line.startsWith("Switch to soul")) p.switchToSoul();
                    if (line.startsWith("Turn")) p.turnDirection(Integer.parseInt(String.valueOf(line.charAt(5))));
                } catch (ClassCastException e) {}
                
                
                
                
                
                // ENTITY METHODS
                try {
                    Entity e = (Entity) current;
                    if (line.startsWith("Turn")) {
                        e.turn(Integer.parseInt(String.valueOf(line.charAt(5))));
                    }
                    if (line.startsWith("Set Dialogue")) {
                        e.setDialogue(line.substring(13));
                    }
                } catch (ClassCastException e) {}
                    
                
                
                
                
                // PATHEDANIMATEDSPRITEDOBJECT METHODS
                try {
                    PathedAnimatedSpritedObject paso = (PathedAnimatedSpritedObject) current;
                    if (line.startsWith("Path")) {
                        String[] substrings = line.split(",");
                        Path p = new Path(substrings[0].substring(4), substrings[1], Integer.parseInt(substrings[2]),
                                Integer.parseInt(substrings[3]), Double.parseDouble(substrings[4]),
                                Double.parseDouble(substrings[5]), Double.parseDouble(substrings[6]),
                                Boolean.parseBoolean(substrings[7]));
                        paso.setPath(p);
                        paso.startMoving();
                    }
                    if (line.startsWith("Start At")) {
                        String[] substrings = line.split(",");
                        MovingPath p = paso.getPath();
                        String a = substrings[0].substring(9);
                        String b = substrings[1];
                        if (a.startsWith("s")) a = String.valueOf(storedNums[Integer.parseInt(a.substring(1))]);
                        if (b.startsWith("s")) b = String.valueOf(storedNums[Integer.parseInt(b.substring(1))]);
                        p.startAt(Integer.parseInt(a), Integer.parseInt(b));
                    }
                    if (line.equals("Restart")) paso.getPath().restart();
                    if (line.equals("Start Moving")) paso.startMoving();
                    if (line.equals("Stop Moving")) paso.stopMoving();
                } catch (ClassCastException e) {}
                
                
                
                // DOUBLYSPRITEDOBJECT METHODS
                try {
                    DoublySpritedObject dso = (DoublySpritedObject) current;
                    if (line.equals("Switch Sprite")) dso.switchSprite();
                } catch (ClassCastException cce) {}
                
                
                
                
                // SPRITEDOBJECT METHODS
                try {
                    SpritedObject so = (SpritedObject) current;
                    if (line.startsWith("Sprite ") && line.charAt(7) != '#') {
                        String[] substrings = line.split(",");
                        SpriteSheet s = new SpriteSheet(Game.loadImage(substrings[0].substring(7)), 
                            Integer.parseInt(substrings[1]), Integer.parseInt(substrings[2]));
                        so.setSprite(s.getSprite(Integer.parseInt(substrings[3]), Integer.parseInt(substrings[4])));
                    }
                    if (line.startsWith("Darken to black")) so.darkenToBlack(Float.parseFloat(line.substring(16)));
                    if (line.equals("Show")) {so.show();}
                    if (line.contains("Hide")) so.hide();
                    
                } catch (ClassCastException e) {}
                
                
                
                
                // ANIMATEDSPRITEDOBJECT METHODS
                try {
                    AnimatedSpritedObject aso = (AnimatedSpritedObject) current;
                    if (line.startsWith("Sprite ") && line.charAt(7) == '#') {
                        aso.setSprite(aso.getSprites()[Integer.parseInt(String.valueOf(line.charAt(8)))]);
                    }
                    if (line.startsWith("Sprites")) {
                        String[] substrings = line.split(",");
                        SpriteSheet s = new SpriteSheet(Game.loadImage(substrings[0].substring(8)), 
                                Integer.parseInt(substrings[1]), Integer.parseInt(substrings[2]));
                        aso.setSprites(s.getSprites());
                    }
                    if (line.equals("Animate")) {aso.animate();}
                    if (line.equals("Pause")) {aso.pause();}
                    if (line.equals("Resume")) {aso.resume();}
                } catch (ClassCastException e) {}
                
                
                
                
                // ENEMY METHODS
                try {
                    Enemy e = (Enemy) current;
                    if (line.startsWith("Hurt")) e.hurt();
                    if (line.startsWith("No longer hurt")) e.noLongerHurt();
                } catch (ClassCastException e) {}
            }
            
            scan.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Script path not found.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Script path incorrectly composed.");
        }
     
    return -1;
    
    }
    
    
    private int checkIf( String line ) {
        
        if (line.startsWith("True")) return 4;
        if (line.startsWith("False")) return -1;
        if (line.contains("<")) {
            int temp = line.indexOf("<");
            int first = storedNums[Integer.parseInt(line.substring(temp - 3, temp - 1))];
            int second = storedNums[Integer.parseInt(line.substring(temp + 2,temp + 4))];
            if (first < second) return 7;
        }
        if (line.contains("Finished-Moving") && 
                line.startsWith("Finished-Moving")) {
            try {
                PathedAnimatedSpritedObject paso = (PathedAnimatedSpritedObject) current;
                if (paso.finishedMoving()) {
                    return 15;
                }
            } catch (ClassCastException e) {}
        }
        if (line.contains("Finished-Animating") && 
                line.startsWith("Finished-Animating")) {
            try {
                AnimatedSpritedObject aso = (AnimatedSpritedObject) current;
                if (aso.finishedAnimating()) {return 18;}
            } catch (ClassCastException e) {}
        }
        if (line.contains("Is-Animating") && 
                line.startsWith("Is-Animating")) {
            try {
                AnimatedSpritedObject aso = (AnimatedSpritedObject) current;
                if (aso.isAnimating()) {return 12;}
            } catch (ClassCastException e) {}
        }
        if (line.contains("Path-Finished") && 
                line.startsWith("Path-Finished")) {
            try {
                PathedAnimatedSpritedObject paso = (PathedAnimatedSpritedObject) current;
                if (paso.getPath().isFinished()) {return 13;}
            } catch (ClassCastException e) {}
        }
        if (line.contains("Alive") && line.startsWith("Alive")) {
            try {
                Battler b = (Battler) current;
                if (b.isAlive()) {return 5;}
            } catch (ClassCastException e) {}
        }
        if (line.startsWith("Stored") && storedBoolean) return 6;
        if (line.startsWith("Event Flag")) {
            if (room.isEventFlag()) return 10;
        }
        if (line.startsWith("Dialogue Finished")) {
            if (!dialogueBox.isShowing()) return 17;
        }
        if (line.startsWith("Colliding")) {
            String[] substrings = line.split(",");
            Rectangle r = new Rectangle(Integer.parseInt(substrings[1]),
                Integer.parseInt(substrings[2]), Integer.parseInt(substrings[3]),
                Integer.parseInt(substrings[4]));
            int length = line.split(" ")[0].length();
            Player p = (Player) current;
            if (p != null && p.getRect().isColliding(r)) return length;
        }
        if (line.startsWith("Interact")) {
            String[] substrings = line.split(",");
            Rectangle r = new Rectangle(Integer.parseInt(substrings[1]),
                Integer.parseInt(substrings[2]), Integer.parseInt(substrings[3]),
                Integer.parseInt(substrings[4]));
            int length = line.split(" ")[0].length();
            Player p = (Player) current;
            if (p != null && p.getRect().isColliding(r)
                    && p.facing() == Integer.parseInt(substrings[5])
                    && Game.getKeyListener().z()) return length;
        }
        return -1;
    }
    
}

