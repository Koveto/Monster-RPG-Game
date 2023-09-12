package game.gameObjects;

import game.DialogueHandler;
import game.DialogueTrigger;
import game.Game;
import game.MovingPath;
import game.Path;
import game.Sprite;
import java.util.ArrayList;

/**
 * Entity
 * @author Kobe Goodwin
 * @version 9/10/2023
 */
public class Entity extends PathedAnimatedSpritedObject {
    
    private Rectangle collision;
    private ArrayList<DialogueTrigger> dt;
    private Sprite[] up, down, left, right;
    private int deltaX, deltaY;
    
    public Entity( Sprite sprite, Path path, int x, int y, int w, int h,
            boolean doubleSize, String dialoguePath ) {
        
        super(sprite, path, x, y, doubleSize);
        collision = new Rectangle( x, y, w, h);
        dt = DialogueHandler.parseDialogueFile(dialoguePath);
        up = new Sprite[] {sprite};
        down = new Sprite[] {sprite};
        left = new Sprite[] {sprite};
        right = new Sprite[] {sprite};
        
    }
    
    public Entity( Sprite[] sprites, MovingPath path, int timePerSwitchMillis,
            boolean hideWhenFinished, boolean loopAnimation, int x, int y,
            int w, int h, String dialoguePath ) {
        
        super(sprites, path, timePerSwitchMillis, hideWhenFinished, loopAnimation);
        collision = new Rectangle( x, y, w, h);
        dt = DialogueHandler.parseDialogueFile(dialoguePath);
        up = sprites;
        down = sprites;
        left = sprites;
        right = sprites;
        
    }
    
    public Entity( Sprite[] up, Sprite[] down, Sprite[] left, Sprite[] right, 
            MovingPath path, int timePerSwitchMillis,
            boolean hideWhenFinished, boolean loopAnimation, int x, int y,
            int w, int h, String dialoguePath ) {
        
        super(up, path, timePerSwitchMillis, hideWhenFinished, loopAnimation);
        collision = new Rectangle( x, y, w, h);
        dt = DialogueHandler.parseDialogueFile(dialoguePath);
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        
    }
    
    public int getDeltaX( ) { return deltaX; }
    public int getDeltaY( ) { return deltaY; }
    
    public void turn( int direction ) {
        setSprites(new Sprite[][] {up, down, left, right}[direction]);
        setSprite(getSprites()[0]);
    }
    
    public ArrayList<DialogueTrigger> getDialogueTriggers( ) { return dt; }
    
    public Rectangle getCollision( ) { return collision; }

    public void setCollision( Rectangle r ) { collision = r; }
    
    public void setDialogue( String dialoguePath, int index ) {
        dt = DialogueHandler.parseEntityDialogue(dialoguePath, index);
    }
    
    @Override
    public void update( Game g ) {
        
        super.update(g);
        if (getPath().isMoving()) {
            int beforeX = collision.getX();
            int beforeY = collision.getY();
            collision.setX(getPath().getX());
            collision.setY(getPath().getY());
            deltaX = collision.getX() - beforeX;
            deltaY = collision.getY() - beforeY;
            for (int i = 0; i < dt.size(); i++) {
                dt.get(i).setInteractBox(deltaX, deltaY);
            }
        }
        
    }

    /*@Override
    public void render() {
        super.render(); //To change body of generated methods, choose Tools | Templates.
        collision.render();
    }*/
    
    
    
}
