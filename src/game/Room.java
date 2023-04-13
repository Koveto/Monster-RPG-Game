package game;

import game.gameObjects.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Room
 * @author Kobe Goodwin
 * @version 4/13/2023
 */
public class Room {
    
    private TileSet tiles;
    private Map map1, map2;
    private Rectangle[] walls;
    private ArrayList<DialogueTrigger> dt;
    
    public Room( TileSet tiles, String map1Path, String map2Path, String wallPath,
            String dialoguePath ) {
        
        this.tiles = tiles;
        this.walls = walls;
        map1 = new Map(new File(map1Path), tiles);
        map2 = new Map(new File(map2Path), tiles);
        dt = new ArrayList<DialogueTrigger>();
        
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
        
        try {
            Scanner scan = new Scanner(new File(dialoguePath));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.contains("//")) continue;
                String[] splitString = line.split(",");
                dt.add(new DialogueTrigger(new Rectangle(
                                    Integer.parseInt(splitString[0]),
                                    Integer.parseInt(splitString[1]),
                                    Integer.parseInt(splitString[2]),
                                    Integer.parseInt(splitString[3])),
                                    splitString[4], Game.UP)
                );
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("Dialogue path not found.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Dialogue path incorrectly composed.");
        }
    
    }
    
    public Rectangle[] getWalls( ) { return walls; }
    
    public ArrayList<DialogueTrigger> getDialogueTriggers( ) {return dt;}
    
    public GameObject[] getObjects( ) {
        
        return new GameObject[] {map1, map2};
        
    }
    
    
}
