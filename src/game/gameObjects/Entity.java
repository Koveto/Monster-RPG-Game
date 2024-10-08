package game.gameObjects;

import game.DialogueHandler;
import game.DialogueTrigger;
import game.Game;
import game.MovingPath;
import game.Path;
import game.Sprite;

/**
 * Entity
 * @author Kobe Goodwin
 * @version 1/23/2024
 */
public class Entity extends PathedAnimatedSpritedObject {
    
    private Rectangle collision;
    private DialogueTrigger dt;
    private Sprite[] up, down, left, right;
    private String currentPath;
    private int deltaX, deltaY, dialogueIndex, renderOverPlayer;
    
    public Entity( Sprite sprite, Path path, int x, int y, int w, int h,
            boolean doubleSize, String dialoguePath ) {
        
        super(sprite, path, x, y, doubleSize);
        collision = new Rectangle( x, y, w, h);
        dt = DialogueHandler.parseDialogueFile(dialoguePath).get(0);
        currentPath = dialoguePath;
        up = new Sprite[] {sprite};
        down = new Sprite[] {sprite};
        left = new Sprite[] {sprite};
        right = new Sprite[] {sprite};
        renderOverPlayer = -1;
        
    }

    public Entity( Sprite sprite, Path path, int x, int y, 
            int collisionX, int collisionY, int w, int h,
            int renderOverPlayer, boolean doubleSize, String dialoguePath ) {
        
        super(sprite, path, x, y, doubleSize);
        collision = new Rectangle( collisionX, collisionY, w, h);
        dt = DialogueHandler.parseDialogueFile(dialoguePath).get(0);
        currentPath = dialoguePath;
        up = new Sprite[] {sprite};
        down = new Sprite[] {sprite};
        left = new Sprite[] {sprite};
        right = new Sprite[] {sprite};
        this.renderOverPlayer = renderOverPlayer;
        
    }
    
    public Entity( Sprite[] sprites, MovingPath path, int timePerSwitchMillis,
            boolean hideWhenFinished, boolean loopAnimation, int x, int y,
            int w, int h, String dialoguePath ) {
        
        super(sprites, path, timePerSwitchMillis, hideWhenFinished, loopAnimation);
        collision = new Rectangle( x, y, w, h);
        dt = DialogueHandler.parseDialogueFile(dialoguePath).get(0);
        currentPath = dialoguePath;
        up = sprites;
        down = sprites;
        left = sprites;
        right = sprites;
        renderOverPlayer = -1;
        
    }
    
    public Entity( Sprite[] up, Sprite[] down, Sprite[] left, Sprite[] right, 
            MovingPath path, int timePerSwitchMillis,
            boolean hideWhenFinished, boolean loopAnimation, int x, int y,
            int w, int h, String dialoguePath ) {
        
        super(up, path, timePerSwitchMillis, hideWhenFinished, loopAnimation);
        collision = new Rectangle( x, y, w, h);
        dt = DialogueHandler.parseDialogueFile(dialoguePath).get(0);
        currentPath = dialoguePath;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        renderOverPlayer = -1;
        
    }

    public Entity( Sprite[] up, Sprite[] down, Sprite[] left, Sprite[] right, 
            MovingPath path, int timePerSwitchMillis,
            boolean hideWhenFinished, boolean loopAnimation, int x, int y,
            int collisionX, int collisionY, int w, int h, int renderOverPlayer, String dialoguePath ) {
        
        super(up, path, timePerSwitchMillis, hideWhenFinished, loopAnimation);
        collision = new Rectangle( collisionX, collisionY, w, h);
        dt = DialogueHandler.parseDialogueFile(dialoguePath).get(0);
        currentPath = dialoguePath;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.renderOverPlayer = renderOverPlayer;
        
    }
    
    public int getDeltaX( ) { return deltaX; }
    public int getDeltaY( ) { return deltaY; }
    
    public void turn( int direction ) {
        setSprites(new Sprite[][] {up, down, left, right}[direction]);
        setSprite(getSprites()[0]);
    }
    
    public DialogueTrigger getDialogueTrigger( ) { return dt; }
    
    public Rectangle getCollision( ) { return collision; }

    public int getRenderOverPlayer( ) { return renderOverPlayer; }

    public void setCollision( Rectangle r ) { collision = r; }
    
    public void setDialogue( String dialoguePath, int index ) {
        dialogueIndex = index;
        currentPath = dialoguePath;
        dt = DialogueHandler.parseEntityDialogue(dialoguePath, index);
    }
    
    public void updateDialogue( ) {
        setDialogue(currentPath, dialogueIndex + 1);
    }

    @Override
    public void update( Game g ) {
        
        super.update(g);
        if (getPath().isMoving()) {
            int beforeX = collision.getX();
            int beforeY = collision.getY();
            deltaX = getPath().getX() - beforeX;
            deltaY = getPath().getY() - beforeY;
            collision.setX(collision.getX() + deltaX);
            collision.setY(collision.getY() + deltaY);
            dt.setInteractBox(deltaX, deltaY);
        }
        
    }

    /*@Override
    public void render() {
        super.render(); //To change body of generated methods, choose Tools | Templates.
        collision.render();
    }*/
    
    
    
}
