package game;

import game.gameObjects.*;

/**
 * Overworld
 * @author Kobe Goodwin
 * @version 6/23/2023
 */
public class Overworld {
    
    private Player player;
    private Room room;
    private DialogueBox dialogueBox;
    private int confirmDelay;
    private boolean holdingUpOrDown, holdingRightOrLeft, isActivatingBattle;
    
    public Overworld( Player player, Room room ) {
        
        this.player = player;
        this.room = room;
        this.dialogueBox = new DialogueBox();
        holdingUpOrDown = false;
        isActivatingBattle = false;
        
    }
    
    public Player getPlayer( ) { return player; }
    
    public void interpretButtonPress( int button ) {
        
        /*if (player.getLastDirection() == button && state != b.ENEMY_TURN) {
            return;
        }*/
        if (confirmDelay > 0) confirmDelay--;
        if (confirmDelay == 0 && button == Game.CONFIRM && dialogueBox.finishedScrolling()) {
            dialogueBox.hide();
        }
        if (button == Game.CONFIRM && confirmDelay == 0) confirmDelay = 10;
        if (dialogueBox.isShowing()) return;
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
            if (Game.getKeyListener().down() && !(Game.getKeyListener().left() || Game.getKeyListener().right())) {
                player.stopStepping();
                return;
            }
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
            if (Game.getKeyListener().up() && !(Game.getKeyListener().left() || Game.getKeyListener().right())) {
                player.stopStepping();
                return;
            }
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
        if (player.isFacingUp() && !Game.getKeyListener().up() && button != Game.NO_KEYS) {
            if (button == Game.LEFT) player.turnDirection(Game.LEFT);
            else if (button == Game.RIGHT) player.turnDirection(Game.RIGHT);
        }
        if (player.isFacingDown() && !Game.getKeyListener().down() && button != Game.NO_KEYS) {
            if (button == Game.LEFT) player.turnDirection(Game.LEFT);
            else if (button == Game.RIGHT) player.turnDirection(Game.RIGHT);
        }
        if (player.isFacingRight() && !Game.getKeyListener().right() && button != Game.NO_KEYS) {
            if (button == Game.UP) player.turnDirection(Game.UP);
            else if (button == Game.DOWN) player.turnDirection(Game.DOWN);
        }
        if (player.isFacingLeft() && !Game.getKeyListener().left() && button != Game.NO_KEYS) {
            if (button == Game.UP) player.turnDirection(Game.UP);
            else if (button == Game.DOWN) player.turnDirection(Game.DOWN);
        }
        
        
    }
    
    public boolean activatingBattle( ) { return isActivatingBattle; }
    
    public void checkDialogueTrigger( ) {
        
        /*if (room.getDialogueTrigger().isColliding(player) && Game.getKeyListener().z()) {
            isActivatingBattle = true;
            player.setX(39);
            player.setY(444);
            player.switchToSoul();
        }*/
        
        for (DialogueTrigger dt : room.getDialogueTriggers()) {
            if (confirmDelay == 0 && dt.isColliding(player) && !dialogueBox.isShowing() && Game.getKeyListener().z() && player.facing() == dt.getDirection()) {
                dialogueBox.displayMessage(dt.getText(), dt.getFace());
                player.stopStepping();
                break;
            }
        }
        
    }
    
    public GameObject[] getObjects( ) {
        
        GameObject[] temp = new GameObject[room.getObjects().length + dialogueBox.getObjects().length + 1];
        for (int i = 0; i < room.getObjects().length; i++) {
            temp[i] = room.getObjects()[i];
        }
        for (int i = 0; i < dialogueBox.getObjects().length; i++) {
            temp[i + room.getObjects().length] = dialogueBox.getObjects()[i];
        }
        temp[temp.length - 1] = player;
        return temp;
        
    }
    
    public Text[] getText( ) { return dialogueBox.getText(); }
    
}
