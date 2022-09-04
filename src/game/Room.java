package game;

import game.gameObjects.GameObject;
import game.gameObjects.Map;
import game.gameObjects.Player;
import java.io.File;

/**
 * Room
 * @author Kobe Goodwin
 * @version 9/2/2022
 */
public class Room {
    
    private Player player;
    private TileSet tiles;
    private Map map1, map2;
    
    public Room( Player player, TileSet tiles, String map1Path, String map2Path ) {
        
        this.player = player;
        this.tiles = tiles;
        map1 = new Map(new File(map1Path), tiles);
        map2 = new Map(new File(map2Path), tiles);
    }
    
    public GameObject[] getObjects( ) {
        
        return new GameObject[] {map1, map2, player};
        
    }
    
    
}
