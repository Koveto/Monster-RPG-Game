package game.gameObjects;

import game.Game;
import game.RenderHandler;
import game.Sprite;
import game.SpriteSheet;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Map
 * @author Kobe Goodwin
 * @version 8/26/2024
 * 
 * Plots a TileSet to the screen from a file specifying x and y positions.
 */
public class Map implements GameObject {
    
    private Sprite fillSprite;
    private ArrayList<MappedTile> mappedTiles = new ArrayList();
    
    /**
     * Map
     * @param mapFile       File with tile mapping information
     * @param spriteSheet   SpriteSheet to interpret tiles
     */
    public Map( File mapFile, SpriteSheet spriteSheet ) {
        
        try {
            Scanner scan = new Scanner(mapFile);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.startsWith("//")) continue;
                if (line.contains(":")) {
                    String[] splitString = line.split(":");
                    if (splitString[0].equalsIgnoreCase("Fill")) {
                        fillSprite = spriteSheet.getSprite(Integer.parseInt(splitString[1]), Integer.parseInt(splitString[2]));
                        continue;
                    }
                }
                String[] splitString = line.split(",");
                if (splitString.length >= 3) {
                    Sprite s = spriteSheet.getSprite(Integer.parseInt(splitString[0]), Integer.parseInt(splitString[1]));
                    MappedTile mappedTile = new MappedTile(s,
                            Integer.parseInt(splitString[2]), Integer.parseInt(splitString[3]));
                    mappedTiles.add(mappedTile);
                }
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        
    }
    
    // No implementation
    public int getX( ) {return 0;}
    public int getY( ) {return 0;}
    public double getTransparency( ) {return 0.0;}
    public void setX( int x ) {}
    public void setY( int y ) {}
    public void setTransparency( double transparency ) {}
    public void render( Graphics graphics ) {}
    public void show( ) {}
    public void hide( ) {}
    
    /**
     * Renders each tile on the map.
     */
    public void render( ) {
        int tileWidth = 20;
        int tileHeight = 20;
        
        Rectangle camera = RenderHandler.getCamera();
        for (int y = camera.getY(); y < camera.getY() + camera.getHeight(); y += tileHeight) {
            for (int x = camera.getX(); x < camera.getX() + camera.getWidth(); x += tileWidth) {
                RenderHandler.renderSprite(fillSprite, x, y);
            }
        }
        
        for (int i = 0; i < mappedTiles.size(); i++) {
            RenderHandler.renderSprite(mappedTiles.get(i).sprite, 
                    mappedTiles.get(i).x * tileWidth * 2, 
                    mappedTiles.get(i).y * tileHeight * 2, 2, 2);
        }
        /*for (int tileIndex = 0; tileIndex < mappedTiles.size(); tileIndex++) {
            MappedTile mappedTile = mappedTiles.get(tileIndex);
            int x = mappedTile.x * tileWidth;
            int y = mappedTile.y * tileHeight;
            //System.out.println("Placed at " + x + ", " + y);
            //RenderHandler.renderSprite(mappedTile.sprite, x, y);
            tileSet.renderTile(mappedTile.id, mappedTile.x * tileWidth, mappedTile.y * tileHeight);
        }*/
    }
    
    /**
     * No implementation.
     * @param game 
     */
    public void update( Game game ) {}
    
    
    /**
     * Nested MappedTile class.
     * Plots a tile (identified by its ID) at an x and y position.
     */
    private class MappedTile {
            
        public Sprite sprite;
        public int x, y;

        /**
         * Constructor
         * @param id    ID of Tile in TileSet
         * @param x     X Position
         * @param y     Y Position
         */
        public MappedTile( Sprite s, int x, int y ) {
            this.sprite = s;
            this.x = x;
            this.y = y;
        }

    }
}
