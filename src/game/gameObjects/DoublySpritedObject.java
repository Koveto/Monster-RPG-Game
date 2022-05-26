package game.gameObjects;

import game.Game;
import game.RenderHandler;
import game.Sprite;
import java.awt.Graphics;

/**
 * DoublySpritedObject
 * @author Kobe Goodwin
 * @version 3/12/2022
 * 
 * A GameObject rendered at a given x and y position. It can displayed with one
 * of two sprites at a time.
 */
public class DoublySpritedObject extends SpritedObject implements GameObject {
    
    private Sprite spriteA;
    private Sprite spriteB;
    private boolean isSpriteA;
    
    /**
     * Constructor
     * @param spriteA       First sprite
     * @param spriteB       Second sprite
     * @param xPosition     X Position
     * @param yPosition     Y Position
     */
    public DoublySpritedObject( Sprite spriteA, Sprite spriteB, int xPosition, int yPosition ) {
        
        super(spriteA, xPosition, yPosition);
        this.spriteA = spriteA;
        this.spriteB = spriteB;
        this.isSpriteA = true;
        
    }
    
    /**
     * Constructor
     * @param spriteA       First sprite
     * @param spriteB       Second sprite
     * @param xPosition     X Position
     * @param yPosition     Y Position
     * @param isSpriteA     First sprite will be displayed if true, second if false
     */
    public DoublySpritedObject( Sprite spriteA, Sprite spriteB, int xPosition, int yPosition, boolean isSpriteA ) {
        
        super(isSpriteA ? spriteA : spriteB, xPosition, yPosition);
        this.spriteA = spriteA;
        this.spriteB = spriteB;
        this.isSpriteA = isSpriteA;
        
    }
    
    /**
     * @return  First of two sprites stored
     */
    public Sprite getSpriteA( ) {return spriteA;}
    
    /**
     * @return  Second of two sprites stored
     */
    public Sprite getSpriteB( ) {return spriteB;}
    
    /**
     * @return  True if first sprite is displaying, false if second
     */
    public boolean isSpriteA( ) {return isSpriteA;}
    
    /**
     * Mutates the first sprite.
     * @param spriteA   Sprite to set as first sprite
     */
    public void setSpriteA( Sprite spriteA ) {this.spriteA = spriteA;}
    
    /**
     * Mutates the second sprite.
     * @param spriteB   Sprite to set as second sprite
     */
    public void setSpriteB( Sprite spriteB ) {this.spriteB = spriteB;}
    
    /**
     * Changes the sprite being displayed.
     * @param isSpriteA True to display first sprite, False to display second sprite
     */
    public void switchSprite( boolean isSpriteA ) {this.isSpriteA = isSpriteA;}
    
    /**
     * Changes the sprite being displayed to the opposite sprite.
     */
    public void switchSprite( ) {
        if (isSpriteA) {setSprite(spriteB);}
        else {setSprite(spriteA);}
        isSpriteA = !isSpriteA;
    }

    /**
     * Renders the current sprite to the screen at its x and y position.
     */
    @Override
    public void render( Graphics g ) {
        
        RenderHandler.renderSprite(getSprite(), getX(), getY());
    
    }

    /**
     * Updates the sprite being displayed.
     * @param game 
     */
    @Override
    public void update(Game game) {
    
        if (isSpriteA) {setSprite(spriteA);}
        else {setSprite(spriteB);}
        
    }
    
    /**
     * Compares Object to DoublySpritedObject. Must have the same sprites in
     * the same order and showing the same at the same x and y positions to be 
     * equal.
     * @param o Object to compare to.
     * @return  Boolean evaluating if the objects are the same.
     */
    public boolean equals( Object o ) {
        
        if (!(o instanceof DoublySpritedObject)) return false;
        DoublySpritedObject dob = (DoublySpritedObject) o;
        return dob.getSprite().equals(this.getSprite())
                && dob.getSpriteA().equals(this.spriteA)
                && dob.getSpriteB().equals(this.spriteB)
                && dob.getX() == this.getX()
                && dob.getY() == this.getY();
        
    }
    
    /**
     * Describes DoublySpritedObject. In the form...
     * <p> super:DoublySpritedObject:isSpriteA:spriteA:spriteB
     * @return  String describing DoublySpritedObject.
     */
    public String toString( ) {
        
        return super.toString() + ":" + getClass().getName() + ":" + isSpriteA + 
                ":" + spriteA.toString() + ":" + spriteB.toString();
        
    }
    
    
}
