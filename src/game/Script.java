package game;

import game.gameObjects.AnimatedSpritedObject;
import game.gameObjects.Entity;
import game.gameObjects.GameObject;
import game.gameObjects.PathedAnimatedSpritedObject;
import game.gameObjects.Player;
import game.gameObjects.Rectangle;
import game.gameObjects.SpritedObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Script
 * @author Kobe Goodwin
 * @version 7/9/2023
 */
public class Script {
    
    private DialogueBox dialogueBox;
    private GameObject[] gameObjects;
    private GameObject current;
    private String scriptPath;
    private int index = 0, delay = 0;
    private long last = 0;
    
    public Script( String scriptPath, GameObject[] gameObjects, DialogueBox dialogueBox ) {
        
        this.scriptPath = scriptPath;
        this.gameObjects = gameObjects;
        this.dialogueBox = dialogueBox;
        index = parseScriptFile(scriptPath, 0);
        
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
                
                if (line.startsWith("//")) continue;
                if (line.startsWith("Wait")) {
                    last = System.currentTimeMillis();
                    delay = Integer.parseInt(line.substring(5));
                    return i;
                }
                if (line.startsWith(">  Object")) {
                    current = gameObjects[Integer.parseInt(line.substring(10, 12))];
                    continue;
                }
                if (line.startsWith("Switch to overworld")) {
                    try {
                        Player p = (Player) current;
                        p.switchToOverworld();
                    } catch (Exception e) {
                        System.out.println("Cannot cast to Player.");
                    }
                }
                if (line.startsWith("Switch to soul")) {
                    try {
                        Player p = (Player) current;
                        p.switchToSoul();
                    } catch (Exception e) {
                        System.out.println("Cannot cast to Player.");
                    }
                }
                if (line.startsWith("Take damage")) {
                    try {
                        Player p = (Player) current;
                        p.takeDamage(Integer.parseInt(line.substring(12)));
                    } catch (Exception e) {
                        System.out.println("Cannot cast to Player.");
                    }
                }
                if (line.startsWith("Turn")) {
                    try {
                        Entity e = (Entity) current;
                        e.turn(Integer.parseInt(String.valueOf(line.charAt(5))));
                    } catch (Exception e) {
                        try {
                            Player p = (Player) current;
                            p.turnDirection(Integer.parseInt(String.valueOf(line.charAt(5))));
                        } catch (Exception ex) {
                            System.out.println("Cannot cast to Entity or Player.");
                        }
                    }
                }
                if (line.startsWith("Path")) {
                    try {
                        PathedAnimatedSpritedObject paso = (PathedAnimatedSpritedObject) current;
                        String[] substrings = line.split(",");
                        Path p = new Path(substrings[0].substring(4), substrings[1], Integer.parseInt(substrings[2]),
                                Integer.parseInt(substrings[3]), Double.parseDouble(substrings[4]),
                                Double.parseDouble(substrings[5]), Double.parseDouble(substrings[6]),
                                Boolean.parseBoolean(substrings[7]));
                        paso.setPath(p);
                        paso.startMoving();
                    } catch (Exception e) {
                        System.out.println("Cannot cast to PathedAnimatedSpritedObject.");
                        continue;
                    }
                        
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
                if (line.startsWith("Sprite ")) {
                    if (line.charAt(7) == '#') {
                        try {
                            AnimatedSpritedObject aso = (AnimatedSpritedObject) current;
                            aso.setSprite(aso.getSprites()[Integer.parseInt(String.valueOf(line.charAt(8)))]);
                            
                        } catch (Exception e) {
                            System.out.println("Cannot cast to AnimatedSpritedObject.");
                        }
                    }
                    else {
                        try {
                            SpritedObject so = (SpritedObject) current;
                            String[] substrings = line.split(",");
                            SpriteSheet s = new SpriteSheet(Game.loadImage(substrings[0].substring(7)), 
                                Integer.parseInt(substrings[1]), Integer.parseInt(substrings[2]));
                            so.setSprite(s.getSprite(Integer.parseInt(substrings[3]), Integer.parseInt(substrings[4])));
                        } catch (Exception e) {
                            System.out.println("Cannot cast to SpritedObject.");
                        }
                    }
                }
                if (line.startsWith("Sprites")) {
                    try {
                        AnimatedSpritedObject aso = (AnimatedSpritedObject) current;
                        String[] substrings = line.split(",");
                        SpriteSheet s = new SpriteSheet(Game.loadImage(substrings[0].substring(8)), 
                                Integer.parseInt(substrings[1]), Integer.parseInt(substrings[2]));
                        aso.setSprites(s.getSprites());
                    } catch (Exception e) {
                        System.out.println("Cannot cast to AnimatedSpritedObject.");
                    }
                    
                }
                if (line.startsWith("Move")) {
                    String[] substrings = line.split(",");
                    current.setX(Integer.parseInt(substrings[0].substring(5)));
                    current.setY(Integer.parseInt(substrings[1]));
                }
                if (line.equals("Stop Moving")) {
                    try {
                        PathedAnimatedSpritedObject paso = (PathedAnimatedSpritedObject) current;
                        paso.stopMoving();
                    } catch (Exception e) {
                        System.out.println("Cannot cast to PathedAnimatedSpritedObject.");
                    }
                }
                if (line.equals("Animate")) {
                    try {
                        AnimatedSpritedObject aso = (AnimatedSpritedObject) current;
                        aso.animate();
                    } catch (Exception e) {
                        System.out.println("Cannot cast to AnimatedSpritedObject.");
                    }
                }
                if (line.equals("Show")) {
                    try {
                        SpritedObject so = (SpritedObject) current;
                        so.show();
                    } catch (Exception e) {
                        System.out.println("Cannot cast to SpritedObject.");
                    }
                }
                if (line.equals("Hide")) {
                    try {
                        SpritedObject so = (SpritedObject) current;
                        so.hide();
                    } catch (Exception e) {
                        System.out.println("Cannot cast to SpritedObject.");
                    }
                }
                if (line.equals("Pause")) {
                    try {
                        AnimatedSpritedObject aso = (AnimatedSpritedObject) current;
                        aso.pause();
                    } catch (Exception e) {
                        System.out.println("Cannot cast to AnimatedSpritedObject.");
                    }
                }
                if (line.equals("Resume")) {
                    try {
                        AnimatedSpritedObject aso = (AnimatedSpritedObject) current;
                        aso.resume();
                    } catch (Exception e) {
                        System.out.println("Cannot cast to AnimatedSpritedObject.");
                    }
                }
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
    
}
