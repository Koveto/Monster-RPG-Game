package game.gameObjects;

import game.Game;
import game.Sprite;
import java.awt.Color;

/**
 * AnimatedSpritedObject
 * @author Kobe Goodwin
 * @version 8/24/2022
 * 
 * A SpritedObject with the capability to store an array of Sprites to be
 * displayed and animated at a specified speed. All Sprites will be animated at
 * the same speed and the animation may be paused or resumed. The animation
 * may loop when finished.
 */
public class AnimatedSpritedObject extends SpritedObject {
    
    private Sprite[] sprites;
    private final int MILLIS_PER_SWITCH;
    private final boolean HIDE_WHEN_FINISHED, LOOP;
    
    private long timeLastSwitch;
    private int index;
    private boolean animating, finished;
    
    /**
     * Constructor
     * 
     * @param sprites               Array of Sprites in order to be animated
     * @param x                     X Position
     * @param y                     Y Position
     * @param timePerSwitchMillis   Milliseconds to display each Sprite
     */
    public AnimatedSpritedObject( Sprite[] sprites, int x, int y, int timePerSwitchMillis ) {
        
        super(sprites[0], x, y);
        this.sprites = sprites;
        this.index = 0;
        this.timeLastSwitch = System.currentTimeMillis();
        this.MILLIS_PER_SWITCH = timePerSwitchMillis;
        this.HIDE_WHEN_FINISHED = true;
        this.LOOP = false;
        
    }
    
    /**
     * Constructor
     * 
     * @param sprites               Array of Sprites to be animated
     * @param x                     X Position
     * @param y                     Y Position
     * @param timePerSwitchMillis   Milliseconds to display each Sprite
     * @param hideWhenFinished      True to hide the SpritedObject when the final
     *                              Sprite in [sprites] is reached
     * @param loop                  True to begin from the beginning of [sprites]
     *                              when the final Sprite is reached.
     */
    public AnimatedSpritedObject( Sprite[] sprites, int x, int y, int timePerSwitchMillis, 
            boolean hideWhenFinished, boolean loop ) {
        
        super(sprites[0], x, y);
        this.sprites = sprites;
        this.index = 0;
        this.timeLastSwitch = System.currentTimeMillis();
        this.MILLIS_PER_SWITCH = timePerSwitchMillis;
        this.HIDE_WHEN_FINISHED = hideWhenFinished;
        this.LOOP = loop;
        
    }
    
    /**
     * Accessor for Sprite array to be animated.
     * @return  Sprite array to be animated.
     */
    public Sprite[] getSprites( ) { return sprites; }
    
    /**
     * Accessor for time for each Sprite to be displayed during animation.
     * @return  Milliseconds between switch between Sprites.
     */
    public int getMillisPerSwitch( ) { return MILLIS_PER_SWITCH; }
    
    /**
     * Begins animating the SpritedObject from the first Sprite. If the SpritedObject
     * is hiding, it begins showing.
     */
    public void animate( ) {
        index = 0;
        show();
        animating = true; 
        finished = false;
    }
    
    /**
     * Pauses the animation of an AnimatedSpritedObject. May resume animation
     * using the resume() method.
     */
    public void pause( ) { animating = false; }
    
    /**
     * Resumes the animation of an AnimatedSpritedObject. May pause animation
     * using the pause() method.
     */
    public void resume( ) { animating = true; }
    
    /**
     * Determines if the animation is in progress. Returns false if paused or
     * the animate() method was never called.
     * @return  True if animating, false if not.
     */
    public boolean isAnimating( ) { return animating; }
    
    /**
     * Determines if the animation has been completed and reached the last sprite.
     * @return 
     */
    public boolean finishedAnimating( ) { return finished; }
    
    /**
     *  Changes the sprites used for animation.
     */
    public void setSprites( Sprite[] sprites ) { 
        
        this.sprites = sprites; 
        index = 0;
        
    }
    
    /**
     * Determines the next valid index of a Sprite in SPRITES.
     * @return  next available index in SPRITES
     */
    private int nextSpriteIndex( ) {
        
        if (index + 1 >= sprites.length) {
            if (!LOOP) finished = true;
            else {
                index = 0;
                return 0;
            }
            if (HIDE_WHEN_FINISHED) hide();
            return index;
        }
        else {
            index++;
            return index;
        }
        
    }
    
    /**
     * Updates the AnimatedSpritedObject's current Sprite.
     * @param g 
     */
    @Override
    public void update(Game g) {
        
        super.update(g);
        if (animating && !finished && 
                System.currentTimeMillis() - timeLastSwitch > MILLIS_PER_SWITCH) {
            int nsi = nextSpriteIndex();
            setSprite(sprites[nsi]);
            setDefaultPixels(getSprite().getPixels());
            timeLastSwitch = System.currentTimeMillis();
        }
        updateBrightness();
        
    }
    
}
