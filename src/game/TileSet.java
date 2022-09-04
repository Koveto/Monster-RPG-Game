package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * TileSet
 * @author Kobe Goodwin
 * @version 9/2/2022
 * 
 * A group of tiles interpreted from a file. Each has a name and a sprite,
 * identified by an ID number.
 */
public class TileSet {
    
    private SpriteSheet spriteSheet;
    private ArrayList<Tile> tiles;
    private File tilesFile;
    
    /**
     * Constructor
     * @param tilesFile     File with tile data of the form...
     * <p> [name],[x on spriteSheet],[y on spriteSheet]
     * @param spriteSheet   SpriteSheet corresponding to tile data.
     */
    public TileSet( File tilesFile, SpriteSheet spriteSheet ) {
        
        this.spriteSheet = spriteSheet;
        tiles = new ArrayList();
        this.tilesFile = tilesFile;
        
        try {
            Scanner scan = new Scanner(tilesFile);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.contains("//")) continue;
                String[] array = line.split("-");
                int x = Integer.parseInt(array[1]);
                int y = Integer.parseInt(array[2]);
                Tile tile = new Tile(array[0], spriteSheet.getSprite(x, y));
                tiles.add(tile);
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        
    }
    
    /**
     * Renders a tile at an x and y position
     * @param tileID    Identification number of tile
     * @param x         X Position
     * @param y         Y Position
     */
    public void renderTile( int tileID, int x, int y ) {
        
        if (tileID >= 0 && tiles.size() > tileID) {
            RenderHandler.renderSprite(tiles.get(tileID).sprite, x * 2, y * 2, 2, 2);
        }
        else {
            System.out.println("Tile ID " + tileID + " out of range " + tiles.size());
        }
        
    }
    
    /**
     * Nested Tile class.
     * Creates a tile with a name and sprite.
     */
    private class Tile {
        
        public String tileName;
        public Sprite sprite;
        
        /**
         * Constructor
         * @param tileName  Name of tile
         * @param sprite    Sprite
         */
        public Tile( String tileName, Sprite sprite ) {
            
            this.tileName = tileName;
            this.sprite = sprite;
            
        }
        
    }
}
