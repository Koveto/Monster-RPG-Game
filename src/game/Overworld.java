package game;

import game.gameObjects.*;

/**
 * Overworld
 * @author Kobe Goodwin
 * @version 8/17/2023
 */
public class Overworld {
    
    private Player player;
    private Room room;
    private DialogueBox dialogueBox;
    private final int TRANSITION_LENGTH = 10;
    private int confirmDelay, idTransitioningTo, xTransitioningTo, yTransitioningTo,
            roomTransitionCount;
    private boolean holdingUpOrDown, holdingRightOrLeft, isActivatingBattle,
            isTransitioningRooms;
    
    public Overworld( Player player, Room room ) {
        
        this.player = player;
        this.room = room;
        this.dialogueBox = room.getDialogueBox();
        holdingUpOrDown = false;
        isActivatingBattle = false;
        
    }
    
    public Player getPlayer( ) { return player; }
    
    public void interpretButtonPress( int button ) {
        
        if (confirmDelay > 0) confirmDelay--;   
        if (confirmDelay == 0 && button == Game.CONFIRM && dialogueBox.finishedScrolling()) {
            if (!dialogueBox.progress()) {
                dialogueBox.hide();
            }
            
        }
        if (button == Game.CONFIRM && confirmDelay == 0) confirmDelay = 10;
        if (dialogueBox.isShowing()) return;
        int PLAYER_SPEED = 3;
        
        if (isTransitioningRooms) {
            if (player.isFacingDown()) player.setY(player.getY() + PLAYER_SPEED);
            if (player.isFacingUp()) player.setY(player.getY() - PLAYER_SPEED);
            if (player.isFacingLeft()) player.setX(player.getX() - PLAYER_SPEED);
            if (player.isFacingRight()) player.setX(player.getX() + PLAYER_SPEED);
            return;
        } else if (roomTransitionCount > 0) {
            if (player.isFacingDown()) player.setY(player.getY() + PLAYER_SPEED);
            if (player.isFacingUp()) player.setY(player.getY() - PLAYER_SPEED);
            if (player.isFacingLeft()) player.setX(player.getX() - PLAYER_SPEED);
            if (player.isFacingRight()) player.setX(player.getX() + PLAYER_SPEED);
            roomTransitionCount--;
            return;
        }
        
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
    public boolean transitioningRooms( ) { return isTransitioningRooms; }
    
    public void transitionRooms( ) {
        
        room = new Room(player, dialogueBox, room.getTiles(), 
            "maps\\room" + String.valueOf(idTransitioningTo) + "mapA.txt",
            "maps\\room" + String.valueOf(idTransitioningTo) + "mapB.txt",
            "maps\\room" + String.valueOf(idTransitioningTo) + "walls.txt",
            "maps\\room" + String.valueOf(idTransitioningTo) + "entities.txt",
            "maps\\room" + String.valueOf(idTransitioningTo) + "script.txt",
                "text\\dialogue.txt");
        player.setX(xTransitioningTo);
        player.setY(yTransitioningTo);
        roomTransitionCount = TRANSITION_LENGTH;
        isTransitioningRooms = false;
        
    }
    
    public void checkDialogueTrigger( ) {
        for (int i = 0; i < room.getDialogueTriggers().size(); i++) {
            if (confirmDelay == 0 && room.getDialogueTriggers().get(i).isColliding(player) && 
                    !dialogueBox.isShowing() && Game.getKeyListener().z() && 
                    player.facing() == room.getDialogueTriggers().get(i).getDirection()) {
                dialogueBox.newMessage(room.getDialogueTriggers().get(i).getTexts(), 
                        room.getDialogueTriggers().get(i).getFaces());
                player.stopStepping();
                break;
            }
        }
        
        for (int i = 0; i < room.getEntityTriggers().size(); i++) {
            if (confirmDelay == 0 && room.getEntityTriggers().get(i).isColliding(player) && 
                    !dialogueBox.isShowing() && Game.getKeyListener().z() && 
                    player.facing() == room.getEntityTriggers().get(i).getDirection()) {
                //room.getEntities()[i].
                dialogueBox.newMessage(room.getEntityTriggers().get(i).getTexts(), 
                        room.getEntityTriggers().get(i).getFaces());
                room.setScriptFlag(true, i);
                player.stopStepping();
                break;
            }
        }
            /*if (room.getTriggers().get(i).isColliding(player) && Game.getKeyListener().z()) {
                isActivatingBattle = true;
                Game.getMusic1().stop();
                Game.getMusic2().play("Battle", true);
                player.setX(39);
                player.setY(444);
                player.switchToSoul();
            }*/
        
        
        /*for (DialogueTrigger dt : room.getDialogueTriggers()) {
            if (confirmDelay == 0 && dt.isColliding(player) && !dialogueBox.isShowing() && Game.getKeyListener().z() && player.facing() == dt.getDirection()) {
                dialogueBox.newMessage(dt.getTexts(), dt.getFaces());
                player.stopStepping();
                break;
            }
        }*/
        
    }
    
    public void checkEntityCollision( ) {
        
        for (Entity e : room.getEntities())
            if (player.getRect().isColliding(e.getCollision())) {
                player.setX(player.getX() - e.getDeltaX());
                player.setY(player.getY() + e.getDeltaY());
            }
        if (!(isTransitioningRooms || roomTransitionCount > 0))
            for (int i = 0; i < room.getRoomTransitions().length; i++) {
                if (player.getRect().isColliding(room.getRoomTransitions()[i])) {
                    isTransitioningRooms = true;
                    idTransitioningTo = room.getTransitionIDs()[i];
                    xTransitioningTo = room.getTransitionXs()[i];
                    yTransitioningTo = room.getTransitionYs()[i];
                    player.setDirection(room.getTransitionDirections()[i]);
                }
            }
        
    }
    
    public GameObject[] getObjects( ) {
        
        checkEntityCollision();
        player.updateCamera(RenderHandler.getCamera(), room.getCameraWalls(), true, true);
        
        GameObject[] temp = new GameObject[room.getObjects().length + dialogueBox.getObjects().length + 1];
        for (int i = 0; i < room.getObjects().length; i++) {
            temp[i] = room.getObjects()[i];
        }
        temp[room.getObjects().length] = player;
        for (int i = 0; i < dialogueBox.getObjects().length; i++) {
            if (i + room.getObjects().length + 1 < temp.length)
                temp[i + room.getObjects().length + 1] = dialogueBox.getObjects()[i];
        }
        return temp;
        
    }
    
    public Text[] getText( ) { return dialogueBox.getText(); }
    
}
