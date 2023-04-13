package game;

import game.gameObjects.Player;
import game.gameObjects.Rectangle;

/**
 * DialogueTrigger
 * @author Kobe Goodwin
 * @version 4/13/2023
 */
public class DialogueTrigger {
    
    private Rectangle interactBox;
    private String text;
    private int direction;
    
    public DialogueTrigger( Rectangle interactBox, String text, int direction ) {
        
        this.interactBox = interactBox;
        this.text = text;
        this.direction = direction;
        
    }
    
    public boolean isColliding( Player player ) {
        
        return player.getRect().isColliding(interactBox);
        
    }
    
    public String getText( ) { return text; }
    
    public int getDirection( ) { return direction; }
    
}
