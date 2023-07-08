package game.gameObjects;

import game.DialogueHandler;
import game.DialogueTrigger;
import game.Game;
import game.Path;
import game.Sprite;
import java.util.ArrayList;

/**
 * Entity
 * @author Kobe Goodwin
 * @version 7/7/2023
 */
public class Entity extends PathedAnimatedSpritedObject {
    
    private Rectangle collision;
    private ArrayList<DialogueTrigger> dt;
    
    public Entity( Sprite sprite, Path path, int x, int y, int w, int h,
            String dialoguePath ) {
        
        super(sprite, path, x, y);
        collision = new Rectangle( x, y, w, h);
        dt = DialogueHandler.parseDialogueFile(dialoguePath);
        
    }
    
    public ArrayList<DialogueTrigger> getDialogueTriggers( ) { return dt; }
    
    public Rectangle getCollision( ) { return collision; }
    
    @Override
    public void update( Game g ) {
        
        super.update(g);
        if (getPath().isMoving()) {
            collision.setX(getPath().getX());
            collision.setY(getPath().getY());
        }
        
    }

    /*@Override
    public void render() {
        super.render(); //To change body of generated methods, choose Tools | Templates.
        collision.render();
    }*/
    
    
    
}
