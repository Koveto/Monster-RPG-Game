package game;

import game.gameObjects.Player;
import game.gameObjects.Rectangle;

/**
 * DialogueTrigger
 * @author Kobe Goodwin
 * @version 6/23/2023
 */
public class DialogueTrigger {
    
    private Rectangle interactBox;
    private String[] texts, faces;
    private int direction;
    
    public DialogueTrigger( Rectangle interactBox, String[] texts, String[] faces, int direction ) {
        
        this.interactBox = interactBox;
        this.texts = texts;
        this.faces = faces;
        this.direction = direction;
        
    }
    
    public boolean isColliding( Player player ) {
        
        return player.getRect().isColliding(interactBox);
        
    }
    
    public String[] getTexts( ) { return texts; }
    
    public String[] getFaces( ) { return faces; }
    
    public int getDirection( ) { return direction; }
    
}
