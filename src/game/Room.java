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
 * @version 8/9/2023
 */
public class Room {
    
    private Player player;
    private DialogueBox dialogueBox;
    private SpriteSheet tiles;
    private Map map1, map2;
    private Script script;
    private Rectangle[] walls, transitions;
    private int[] transIDs, xs, ys;
    private Entity[] entities;
    private ArrayList<DialogueTrigger> dt;
    
    public Room( Player player, DialogueBox dialogueBox,
            SpriteSheet tiles, String map1Path, String map2Path, String wallPath,
            String dialoguePath ) {
        
        this.player = player;
        this.dialogueBox = dialogueBox;
        this.tiles = tiles;
        map1 = new Map(new File(System.getProperty("user.dir") + "\\src\\game\\" + map1Path), tiles);
        map2 = new Map(new File(System.getProperty("user.dir") + "\\src\\game\\" + map2Path), tiles);
        dt = new ArrayList<>();
        transIDs = new int[0];
        xs = new int[0];
        ys = new int[0];
        walls = new Rectangle[0];
        transitions = new Rectangle[0];
        entities = new Entity[] {
            new Entity(new SpriteSheet(Game.loadImage("ss\\toriel.png"), 25, 52).getSprite(0, 0), 
                    new Path("1", "1", 0, 1, 1, false), 100, 100, 50, 104, 
                    "text\\testEntity.txt\\"),
            new Entity(Arrays.copyOfRange(new SpriteSheet(Game.loadImage("ss\\toriel.png"), 25, 52).getSprites(), 0, 4), 
                    Arrays.copyOfRange(new SpriteSheet(Game.loadImage("ss\\toriel.png"), 25, 52).getSprites(), 4, 8), 
                    Arrays.copyOfRange(new SpriteSheet(Game.loadImage("ss\\toriel.png"), 25, 52).getSprites(), 8, 12), 
                    Arrays.copyOfRange(new SpriteSheet(Game.loadImage("ss\\toriel.png"), 25, 52).getSprites(), 12, 16), 
                    new Path("1", "t", 300, 200, 0, 50, 0.5, false), 
                    300, false, true, 300, 200, 50, 70,
                    "text\\testEntity.txt\\")
        };
        
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
        
        dt = DialogueHandler.parseDialogueFile(dialoguePath);
        
        script = new Script("text\\script.txt\\", this, null,
                new GameObject[] {player, entities[0], entities[1]}, 
                dialogueBox);
    
    }
    
    public Rectangle[] getWalls( ) { 
        
        Rectangle[] toReturn = walls;
        toReturn = Game.addToRectangleArray(walls, entities[0].getCollision());
        for (Entity e : entities) {
            toReturn = Game.addToRectangleArray(toReturn, e.getCollision());
        }
        return toReturn;
        
    }
    
    public DialogueBox getDialogueBox( ) { return dialogueBox; }
    
    public ArrayList<DialogueTrigger> getDialogueTriggers( ) {
        ArrayList<DialogueTrigger> triggers = new ArrayList();
        for (Entity e : entities) {
            for (DialogueTrigger d : e.getDialogueTriggers()) {
                triggers.add(d);
            }
        }
        return triggers;
    }
    
    public SpriteSheet getTiles( ) { return tiles; }
    public Entity[] getEntities( ) { return entities; }
    public int[] getTransitionIDs( ) { return transIDs; }
    public int[] getTransitionXs( ) { return xs; }
    public int[] getTransitionYs( ) { return ys; }
    public Rectangle[] getRoomTransitions( ) { return transitions; }
    
    public GameObject[] getObjects( ) {
        
        script.update();
        
        GameObject[] obj = new GameObject[] {map1, map2};
        for (Entity e : entities) {
            obj = Game.addToGOArray(obj, e);
        }
        /*for (Rectangle r : walls) {
            obj = Game.addToGOArray(obj, r);
        }*/
        return obj;
        
    }
    
    
}
