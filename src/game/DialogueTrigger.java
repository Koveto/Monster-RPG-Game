package game;

import game.gameObjects.Player;
import game.gameObjects.Rectangle;

/**
 * DialogueTrigger
 * @author Kobe Goodwin
 * @version 9/7/2022
 */
public class DialogueTrigger {
    
    private Rectangle interactBox;
    
    public DialogueTrigger( Rectangle interactBox ) {
        
        this.interactBox = interactBox;
        
    }
    
    public boolean isColliding( Player player ) {
        
        return player.getRect().isColliding(interactBox);
        
    }
    
}
