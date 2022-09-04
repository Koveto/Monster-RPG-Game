package game.gameObjects;

import game.Game;
import game.RenderHandler;
import game.TileSet;
import game.gameObjects.GameObject;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Map
 * @author Kobe Goodwin
 * @version 3/13/2022
 * 
 * Plots a TileSet to the screen from a file specifying x and y positions.
 */
public class Map implements GameObject {
    
    private TileSet tileSet;
    private int fillTileID = -1;
    private ArrayList<MappedTile> mappedTiles = new ArrayList();
    
    /**
     * Constructor
     * @param mapFile   File with map data of the form...
     * <p> Fill: [id]
     * <p> [id], [x], [y]
     * @param tileSet   TileSet of Tiles to plot.
     */
    public Map( File mapFile, TileSet tileSet ) {
        
        this.tileSet = tileSet;
        try {
            Scanner scan = new Scanner(mapFile);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (!line.startsWith("//")) {
                    if (line.contains(":")) {
                        String[] splitString = line.split(":");
                        if (splitString[0].equalsIgnoreCase("Fill")) {
                            fillTileID = Integer.parseInt(splitString[1]);
                            continue;
                        }
                    }
                    String[] splitString = line.split(",");
                    if (splitString.length >= 3) {
                        MappedTile mappedTile = new MappedTile(Integer.parseInt(splitString[0]),
                                Integer.parseInt(splitString[1]), Integer.parseInt(splitString[2]));
                        mappedTiles.add(mappedTile);
                    }
                }
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        
    }

    /**
     * Adds a new tile at an x and y position.
     * @param tileID    ID of tile to add
     * @param x         X Position
     * @param y         Y Position
     */
    public void addTile(int tileID, int x, int y) {

        Rectangle camera = RenderHandler.getCamera();
        System.out.println("clicked at " + x + ", " + y + ". Placing tile at " + Math.round((double) (x + camera.getX())/(16)) + ", " + Math.round( (double)(y + camera.getY())/(16)) + ", Camera at " + camera.getX() + ", " + camera.getY());
        mappedTiles.add(new MappedTile(tileID, (int) Math.round((double) (x + camera.getX())/(16)), (int) Math.round( (double)(y + camera.getY())/(16))));

    }
    
    // No implementation
    public int getX( ) {return 0;}
    public int getY( ) {return 0;}
    public double getTransparency( ) {return 0.0;}
    public void setX( int x ) {}
    public void setY( int y ) {}
    public void setTransparency( double transparency ) {}
    public void render( Graphics graphics ) {}
    
    /**
     * Renders each tile on the map.
     */
    public void render( ) {
        int tileWidth = 20;
        int tileHeight = 20;
        
        if (fillTileID >= 0) {
            Rectangle camera = RenderHandler.getCamera();
            for (int y = camera.getY(); y < camera.getY() + camera.getHeight(); y += tileHeight) {
                for (int x = camera.getX(); x < camera.getX() + camera.getWidth(); x += tileWidth) {
                    tileSet.renderTile(fillTileID, x, y);
                }
            }
        }
        
        for (int tileIndex = 0; tileIndex < mappedTiles.size(); tileIndex++) {
            MappedTile mappedTile = mappedTiles.get(tileIndex);
            int x = mappedTile.x * tileWidth;
            int y = mappedTile.y * tileHeight;
            //System.out.println("Placed at " + x + ", " + y);
            tileSet.renderTile(mappedTile.id, mappedTile.x * tileWidth, mappedTile.y * tileHeight);
        }
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
            
        public int id, x, y;

        /**
         * Constructor
         * @param id    ID of Tile in TileSet
         * @param x     X Position
         * @param y     Y Position
         */
        public MappedTile( int id, int x, int y ) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

    }
}
