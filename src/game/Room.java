package game;

import game.gameObjects.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Room
 * @author Kobe Goodwin
 * @version 7/8/2023
 */
public class Room {
    
    private TileSet tiles;
    private Map map1, map2;
    private Rectangle[] walls;
    private Entity[] entities;
    private ArrayList<DialogueTrigger> dt;
    
    public Room( TileSet tiles, String map1Path, String map2Path, String wallPath,
            String dialoguePath ) {
        
        this.tiles = tiles;
        this.walls = walls;
        map1 = new Map(new File(map1Path), tiles);
        map2 = new Map(new File(map2Path), tiles);
        dt = new ArrayList<DialogueTrigger>();
        entities = new Entity[] {
            new Entity(new SpriteSheet(Game.loadImage("ss\\toriel.png"), 25, 52).getSprite(0, 0), 
                    new Path("300", "t + 200", 0, 50, 0.5, false), 300, 200, 50, 104, 
                    "C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\text\\testEntity.txt\\"),
            new Entity(new SpriteSheet(Game.loadImage("ss\\toriel.png"), 25, 52).getSprite(0, 0), 
                    new Path("1", "1", 0, 1, 1, false), 100, 100, 50, 104, 
                    "C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\text\\testEntity.txt\\")
        };
        
        try {
            ArrayList<Rectangle> rects = new ArrayList();
            Scanner scan = new Scanner(new File(wallPath));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.contains("//")) continue;
                String[] splitString = line.split(",");
                rects.add(new Rectangle(Integer.parseInt(splitString[0]),
                                        Integer.parseInt(splitString[1]),
                                        Integer.parseInt(splitString[2]),
                                        Integer.parseInt(splitString[3])));
            }
            walls = (Rectangle[]) rects.toArray(new Rectangle[] {});
        } catch (FileNotFoundException fnfe) {
            System.out.println("Wall path not found.");
            walls = new Rectangle[] {};
        } catch (Exception e) {
            e.printStackTrace();
            walls = new Rectangle[] {};
        }
        
        dt = DialogueHandler.parseDialogueFile(dialoguePath);
        entities[0].startMoving();
    
    }
    
    public Rectangle[] getWalls( ) { 
        
        Rectangle[] toReturn = walls;
        toReturn = Game.addToRectangleArray(walls, entities[0].getCollision());
        for (Entity e : entities) {
            toReturn = Game.addToRectangleArray(toReturn, e.getCollision());
        }
        return toReturn;
        
    }
    
    public ArrayList<DialogueTrigger> getDialogueTriggers( ) {
        ArrayList<DialogueTrigger> triggers = new ArrayList();
        for (Entity e : entities) {
            for (DialogueTrigger d : e.getDialogueTriggers()) {
                triggers.add(d);
            }
        }
        return triggers;
    }
    
    public GameObject[] getObjects( ) {
        
        GameObject[] obj = new GameObject[] {map1, map2};
        for (Entity e : entities) {
            obj = Game.addToGOArray(obj, e);
        }
        return obj;
        
    }
    
    
}
