package game;

import game.gameObjects.Player;
import game.gameObjects.Rectangle;

/**
 * DialogueTrigger
 * @author Kobe Goodwin
 * @version 5/28/2023
 */
public class DialogueTrigger {
    
    private Rectangle interactBox;
    private String[] texts;
    private int direction;
    
    public DialogueTrigger( Rectangle interactBox, String[] texts, int direction ) {
        
        this.interactBox = interactBox;
        this.texts = texts;
        this.direction = direction;
        
    }
    
    public boolean isColliding( Player player ) {
        
        return player.getRect().isColliding(interactBox);
        
    }
    
    public String getText( ) { return texts[0]; }
    
    public int getDirection( ) { return direction; }
    
}
