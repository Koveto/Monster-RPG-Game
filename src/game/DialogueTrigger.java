package game;

import game.gameObjects.Player;
import game.gameObjects.Rectangle;

/**
 * DialogueTrigger
 * @author Kobe Goodwin
 * @version 8/26/2024
 */
public class DialogueTrigger {
    
    private Rectangle interactBox;
    private String[] texts, faces, sounds;
    private int direction;
    
    public DialogueTrigger( Rectangle interactBox, String[] texts, String[] faces,
        String[] sounds, int direction ) {
        
        this.interactBox = interactBox;
        this.texts = texts;
        this.faces = faces;
        this.sounds = sounds;
        this.direction = direction;
        
    }
    
    public boolean isColliding( Player player ) {
        
        return player.getRect().isColliding(interactBox);
        
    }
    
    public void setInteractBox( int x, int y ) {
        interactBox.setX(x + interactBox.getX());
        interactBox.setY(y + interactBox.getY());
    }
    
    public String[] getTexts( ) { return texts; }
    
    public String[] getFaces( ) { return faces; }

    public String[] getSounds( ) { return sounds; }
    
    public int getDirection( ) { return direction; }
    
    public Rectangle getInteractBox( ) { return interactBox; }
    
}
