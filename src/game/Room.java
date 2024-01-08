package game;

import game.gameObjects.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Room
 * @author Kobe Goodwin
 * @version 9/24/2023
 */
public class Room {
    
    private Player player;
    private DialogueBox dialogueBox;
    private SpriteSheet tiles;
    private Map map1, map2;
    private Script[] scripts;
    private Rectangle[] walls, cameraWalls, transitions;
    private int[] transIDs, xs, ys, transDirections;
    private Entity[] entities;
    private ArrayList<DialogueTrigger> dt;
    private boolean eventFlag;
    
    public Room( Player player, DialogueBox dialogueBox,
            SpriteSheet tiles, String map1Path, String map2Path, String wallPath,
            String entityPath, String scriptPath, String dialoguePath ) {
        
        this.player = player;
        this.dialogueBox = dialogueBox;
        this.tiles = tiles;
        map1 = new Map(new File(System.getProperty("user.dir") + "\\src\\game\\" + map1Path), tiles);
        map2 = new Map(new File(System.getProperty("user.dir") + "\\src\\game\\" + map2Path), tiles);
        dt = new ArrayList<>();
        transIDs = new int[0];
        xs = new int[0];
        ys = new int[0];
        transDirections = new int[0];
        walls = new Rectangle[0];
        transitions = new Rectangle[0];
        cameraWalls = new Rectangle[0];
        entities = new Entity[0];
        Rectangle camera = RenderHandler.getCamera();
        camera.setX(0);
        camera.setY(0);
        
        try {
            Scanner scan = new Scanner(new File(System.getProperty("user.dir") + "\\src\\game\\" + wallPath));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.contains("//") || line.equals("")) continue;
                String[] splitString = line.split(",");
                if (line.startsWith("Transition: ")) {
                    transitions = Game.addToRectangleArray(transitions, 
                            new Rectangle(Integer.parseInt(splitString[0].substring(12)),
                                            Integer.parseInt(splitString[1]),
                                            Integer.parseInt(splitString[2]),
                                            Integer.parseInt(splitString[3])));
                    transIDs = Game.addToIntArray(transIDs, Integer.parseInt(splitString[4]));
                    xs = Game.addToIntArray(xs, Integer.parseInt(splitString[5]));
                    ys = Game.addToIntArray(ys, Integer.parseInt(splitString[6]));
                    transDirections = Game.addToIntArray(transDirections, Integer.parseInt(splitString[7]));
                } else if (line.startsWith("Camera: ")) {
                    cameraWalls = Game.addToRectangleArray(cameraWalls, 
                            new Rectangle(Integer.parseInt(splitString[0].substring(8)),
                                            Integer.parseInt(splitString[1]),
                                            Integer.parseInt(splitString[2]),
                                            Integer.parseInt(splitString[3])));
                } else {
                    walls = Game.addToRectangleArray(walls, 
                            new Rectangle(Integer.parseInt(splitString[0]),
                                            Integer.parseInt(splitString[1]),
                                            Integer.parseInt(splitString[2]),
                                            Integer.parseInt(splitString[3])));
                }
                
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("Wall path not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            Scanner scan = new Scanner(new File(System.getProperty("user.dir") + "\\src\\game\\" + entityPath));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.contains("//") || line.equals("")) continue;
                if (line.equals("Single Sprite")) {
                    line = scan.nextLine();
                    String[] sub = line.split(",");
                    Sprite s = new SpriteSheet(Game.loadImage(sub[0]), Integer.parseInt(sub[1]), 
                            Integer.parseInt(sub[2])).getSprite(Integer.parseInt(sub[3]),
                                    Integer.parseInt(sub[4]));
                    line = scan.nextLine();
                    sub = line.split(",");
                    Path p = new Path(sub[0], sub[1], Double.parseDouble(sub[2]),
                        Double.parseDouble(sub[3]), Double.parseDouble(sub[4]),
                        Boolean.parseBoolean(sub[5]));
                    line = scan.nextLine();
                    sub = line.split(",");
                    Entity e = null;
                    if (sub.length == 6)
                        e = new Entity(s, p, Integer.parseInt(sub[0]),
                                Integer.parseInt(sub[1]), Integer.parseInt(sub[2]),
                                Integer.parseInt(sub[3]), Boolean.parseBoolean(sub[4]),
                                sub[5]);
                    else {
                        e = new Entity(s, p, Integer.parseInt(sub[0]),
                                Integer.parseInt(sub[1]), Integer.parseInt(sub[2]),
                                Integer.parseInt(sub[3]), Integer.parseInt(sub[4]),
                                Integer.parseInt(sub[5]), Integer.parseInt(sub[6]),
                                Boolean.parseBoolean(sub[7]), sub[8]);
                    }
                    entities = Game.addToEntityArray(entities, e);
                }
                
                if (line.equals("Multiple Sprites")) {
                    line = scan.nextLine();
                    String[] sub = line.split(",");
                    Sprite[] s1 = Arrays.copyOfRange(new SpriteSheet(Game.loadImage(sub[0]), Integer.parseInt(sub[1]), 
                            Integer.parseInt(sub[2])).getSprites(), Integer.parseInt(sub[3]),
                                    Integer.parseInt(sub[4]));
                    line = scan.nextLine();
                    sub = line.split(",");
                    Sprite[] s2 = Arrays.copyOfRange(new SpriteSheet(Game.loadImage(sub[0]), Integer.parseInt(sub[1]), 
                            Integer.parseInt(sub[2])).getSprites(), Integer.parseInt(sub[3]),
                                    Integer.parseInt(sub[4]));
                    line = scan.nextLine();
                    sub = line.split(",");
                    Sprite[] s3 = Arrays.copyOfRange(new SpriteSheet(Game.loadImage(sub[0]), Integer.parseInt(sub[1]), 
                            Integer.parseInt(sub[2])).getSprites(), Integer.parseInt(sub[3]),
                                    Integer.parseInt(sub[4]));
                    line = scan.nextLine();
                    sub = line.split(",");
                    Sprite[] s4 = Arrays.copyOfRange(new SpriteSheet(Game.loadImage(sub[0]), Integer.parseInt(sub[1]), 
                            Integer.parseInt(sub[2])).getSprites(), Integer.parseInt(sub[3]),
                                    Integer.parseInt(sub[4]));
                    line = scan.nextLine();
                    sub = line.split(",");
                    Path p = new Path(sub[0], sub[1], Integer.parseInt(sub[2]),
                        Integer.parseInt(sub[3]), Double.parseDouble(sub[4]),
                        Double.parseDouble(sub[5]), Double.parseDouble(sub[6]),
                        Boolean.parseBoolean(sub[7]));
                    line = scan.nextLine();
                    sub = line.split(",");
                    Entity e = new Entity(s1, s2, s3, s4, p, Integer.parseInt(sub[0]),
                            Boolean.parseBoolean(sub[1]), Boolean.parseBoolean(sub[2]),
                            Integer.parseInt(sub[3]), Integer.parseInt(sub[4]),
                            Integer.parseInt(sub[5]), Integer.parseInt(sub[6]), sub[7]);
                    entities = Game.addToEntityArray(entities, e);
                }
                if (line.equals("Animated Sprite")) {
                    line = scan.nextLine();
                    Sprite[] s = new Sprite[0];
                    String[] sub = line.split(",");
                    while (sub.length == 5) {
                        s = Game.addToSpriteArray(s, new Sprite(new SpriteSheet(Game.loadImage(sub[0]), 
                            Integer.parseInt(sub[1]), Integer.parseInt(sub[2])).getSprite(
                                Integer.parseInt(sub[3]), Integer.parseInt(sub[4]))));
                        line = scan.nextLine();
                        sub = line.split(",");
                    }
                    if (sub.length == 3) {
                        s = new SpriteSheet(Game.loadImage(sub[0]), Integer.parseInt(sub[1]), 
                            Integer.parseInt(sub[2])).getSprites();
                        line = scan.nextLine();
                        sub = line.split(",");
                    }
                    Path p = new Path(sub[0], sub[1], Double.parseDouble(sub[2]),
                        Double.parseDouble(sub[3]), Double.parseDouble(sub[4]),
                        Boolean.parseBoolean(sub[5]));
                    line = scan.nextLine();
                    sub = line.split(",");
                    Entity e = new Entity(s, p, Integer.parseInt(sub[0]),
                            Boolean.parseBoolean(sub[1]), Boolean.parseBoolean(sub[2]), 
                            Integer.parseInt(sub[3]), Integer.parseInt(sub[4]),
                            Integer.parseInt(sub[5]), Integer.parseInt(sub[6]),
                            sub[7]);
                    entities = Game.addToEntityArray(entities, e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        dt = DialogueHandler.parseDialogueFile(dialoguePath);
        
        GameObject[] scriptObjects = new GameObject[0];
        scriptObjects = Game.addToGOArray(scriptObjects, player);
        for (int i = 0; i < entities.length; i++) {
            scriptObjects = Game.addToGOArray(scriptObjects, entities[i]);
        }
        /*script = new Script(scriptPath, this, null,
                scriptObjects, dialogueBox);*/
        scripts = new Script[1];
        scripts[0] = new Script(scriptPath, this, null,
                scriptObjects, dialogueBox);
    
    }
    
    public Rectangle[] getWalls( ) { 
        
        Rectangle[] toReturn = walls;
        toReturn = Game.addToRectangleArray(walls, entities[0].getCollision());
        for (Entity e : entities) {
            toReturn = Game.addToRectangleArray(toReturn, e.getCollision());
        }
        return toReturn;
        
    }
    
    public Rectangle[] getCameraWalls( ) { return cameraWalls; }
    
    public DialogueBox getDialogueBox( ) { return dialogueBox; }
    
    public ArrayList<DialogueTrigger> getDialogueTriggers( ) { return dt; }
    
    public ArrayList<DialogueTrigger> getEntityTriggers( ) {
        ArrayList<DialogueTrigger> triggers = new ArrayList();
        for (Entity e : entities) {
            triggers.add(e.getDialogueTrigger());
        }
        return triggers;
    }
    
    public SpriteSheet getTiles( ) { return tiles; }
    public Entity[] getEntities( ) { return entities; }
    public Rectangle[] getTransitions( ) { return transitions; }
    public int[] getTransitionIDs( ) { return transIDs; }
    public int[] getTransitionXs( ) { return xs; }
    public int[] getTransitionYs( ) { return ys; }
    public int[] getTransitionDirections( ) { return transDirections; }
    public Rectangle[] getRoomTransitions( ) { return transitions; }
    public boolean isEventFlag( ) { return eventFlag; }
    
    public void flipEventFlag( ) { 
        if (eventFlag) eventFlag = false;
        else eventFlag = true;
    }
    
    public GameObject[] getObjects( ) {
        
        for (Script s : scripts)
            s.update();
        //script.update();
        
        GameObject[] obj = new GameObject[] {map1, map2};
        for (Entity e : entities) {
            obj = Game.addToGOArray(obj, e);
        }
        //if (entities.length >= 8) System.out.println(entities[8].getX());
        //obj = Game.addToGOArray(obj, new Rectangle(1440,280,19,50));
        /*for (DialogueTrigger d : dt) {
            obj = Game.addToGOArray(obj, d.getInteractBox());
        }*/
        /*for (Entity e : entities) {
            obj = Game.addToGOArray(obj, e.getDialogueTrigger().getInteractBox());
        }*/
        /*for (int i = 0; i < dt.size(); i++) {
            obj = Game.addToGOArray(obj, dt.get(i).getInteractBox());
        }*/
        return obj;
        
    }

    public void setScriptFlag( boolean flag, int index ) {
        if (index > scripts.length - 1) index = scripts.length - 1;
        scripts[index].setRoomFlag(flag);
    }

    public void addScript( String scriptPath, int entityIndex ) {
        
        Script[] temp = new Script[scripts.length + 1];
        for (int i = 0; i < scripts.length; i++) {
            temp[i] = scripts[i];
        }
        temp[scripts.length] = new Script(scriptPath, this, null, new GameObject[] {player, entities[entityIndex]}, dialogueBox);
        scripts = temp;

    }

    public void removeScript( int entityIndex ) {

        Script[] temp = new Script[scripts.length - 1];
        for (int i = 0, k = 0; i < temp.length; i++) {
            if (i != entityIndex) {
                temp[k] = scripts[i];
                k++;
            }
        }
        scripts = temp;

    }

    public void updateDialogueBoxVertical( ) {
        if (player.getY() > Game.HEIGHT / 2) dialogueBox.switchVertical(true);
        else dialogueBox.switchVertical(false);
    }

    
}
