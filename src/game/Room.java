package game;

import game.gameObjects.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Room
 * @author Kobe Goodwin
 * @version 5/28/2023
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
            int[] xywhd = new int[5];
            String[] texts = new String[0];
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.contains("//")) continue;
                
                if (line.charAt(0) == '#') {
                    if (texts.length != 0) {
                        dt.add(new DialogueTrigger(new Rectangle(
                            xywhd[0], xywhd[1], xywhd[2], xywhd[3]),
                            texts, xywhd[4]));
                    }
                    String[] splitString = line.substring(2).split(",");
                    for (int i = 0; i < splitString.length; i++) {
                        xywhd[i] = Integer.parseInt(splitString[i]);
                    }
                    texts = new String[0];
                } else {
                    texts = Game.addToStringArray(texts, line);
                }
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
