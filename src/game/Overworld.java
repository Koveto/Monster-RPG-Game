package game;

import game.gameObjects.*;

/**
 * Overworld
 * @author Kobe Goodwin
 * @version 9/6/2022
 */
public class Overworld {
    
    private Player player;
    private Room room;
    
    private boolean holdingUpOrDown, holdingRightOrLeft;
    
    public Overworld( Player player, Room room ) {
        
        this.player = player;
        this.room = room;
        
        holdingUpOrDown = false;
        
    }
    
    public Player getPlayer( ) { return player; }
    
    public void interpretButtonPress( int button ) {
        
        /*if (player.getLastDirection() == button && state != b.ENEMY_TURN) {
            return;
        }*/
        int PLAYER_SPEED = 3;
        if (button == Game.LEFT) {
            if (!holdingUpOrDown || (Game.getKeyListener().onlyLeft())) {
                player.turnDirection(Game.LEFT);
                holdingRightOrLeft = true;
            }
            player.setX(player.getX() - PLAYER_SPEED);
            for (int i = 0; i < room.getWalls().length; i++) {
                if (player.getRect().isColliding(room.getWalls()[i])) {
                    player.setX(player.getX() + PLAYER_SPEED);
                    if (Game.getKeyListener().onlyLeft()) player.stopStepping();
                    break;
                }
            }
            player.startStepping();
        } else if (button == Game.RIGHT) {
            if (!holdingUpOrDown || (Game.getKeyListener().onlyRight())) {
                player.turnDirection(Game.RIGHT);
                holdingRightOrLeft = true;
            }
            player.setX(player.getX() + PLAYER_SPEED);
            for (int i = 0; i < room.getWalls().length; i++) {
                if (player.getRect().isColliding(room.getWalls()[i])) {
                    player.setX(player.getX() - PLAYER_SPEED);
                    if (Game.getKeyListener().onlyRight()) player.stopStepping();
                    break;
                }
            }
            player.startStepping();
        } else if (button == Game.UP) {
            if (!holdingRightOrLeft || (Game.getKeyListener().onlyUp())) {
                player.turnDirection(Game.UP);
                holdingUpOrDown = true;
            }
            player.setY(player.getY() - PLAYER_SPEED);
            for (int i = 0; i < room.getWalls().length; i++) {
                if (player.getRect().isColliding(room.getWalls()[i])) {
                    player.setY(player.getY() + PLAYER_SPEED);
                    if (Game.getKeyListener().onlyUp()) player.stopStepping();
                    break;
                }
            }
            player.startStepping();
        } else if (button == Game.DOWN) {
            if (!holdingRightOrLeft || Game.getKeyListener().onlyDown()) {
                player.turnDirection(Game.DOWN);
                holdingUpOrDown = true;
            }
            player.setY(player.getY() + PLAYER_SPEED);
            for (int i = 0; i < room.getWalls().length; i++) {
                if (player.getRect().isColliding(room.getWalls()[i])) {
                    player.setY(player.getY() - PLAYER_SPEED);
                    if (Game.getKeyListener().onlyDown()) player.stopStepping();
                    break;
                }
            }
            player.startStepping();
        } else if (button == Game.NO_KEYS) {
            holdingUpOrDown = false;
            holdingRightOrLeft = false;
            player.stopStepping();
        }
        
    }
    
    public GameObject[] getObjects( ) {
        
        GameObject[] temp = new GameObject[room.getObjects().length + 1];
        for (int i = 0; i < room.getObjects().length; i++) {
            temp[i] = room.getObjects()[i];
        }
        temp[temp.length - 1] = player;
        return temp;
        
    }
    
}
