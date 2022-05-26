package game.gameObjects;

import game.Game;
import game.Sprite;

/**
 * AnimatedSpritedObject
 * @author Kobe Goodwin
 * @version 5/20/2022
 */
public class AnimatedSpritedObject extends SpritedObject {
    
    private final Sprite[] SPRITES;
    private final int MILLIS_PER_SWITCH;
    private final boolean HIDE_WHEN_FINISHED, LOOP;
    
    private long timeLastSwitch;
    private int index;
    private boolean animating, finished;
    
    public AnimatedSpritedObject( Sprite[] sprites, int x, int y, int timePerSwitchMillis ) {
        
        super(sprites[0], x, y);
        this.SPRITES = sprites;
        this.index = 0;
        this.timeLastSwitch = System.currentTimeMillis();
        this.MILLIS_PER_SWITCH = timePerSwitchMillis;
        this.HIDE_WHEN_FINISHED = true;
        this.LOOP = false;
        
    }
    
    public AnimatedSpritedObject( Sprite[] sprites, int x, int y, int timePerSwitchMillis, 
            boolean hideWhenFinished, boolean loop ) {
        
        super(sprites[0], x, y);
        this.SPRITES = sprites;
        this.index = 0;
        this.timeLastSwitch = System.currentTimeMillis();
        this.MILLIS_PER_SWITCH = timePerSwitchMillis;
        this.HIDE_WHEN_FINISHED = hideWhenFinished;
        this.LOOP = loop;
        
    }
    
    public Sprite[] getSprites( ) { return SPRITES; }
    public int getMillisPerSwitch( ) { return MILLIS_PER_SWITCH; }
    
    public void animate( ) {
        index = 0;
        show();
        animating = true; 
        finished = false;
    }
    
    public void pause( ) { animating = false; }
    public void resume( ) { animating = true; }
    
    public boolean finished( ) { return finished; }
    
    private int nextSpriteIndex( ) {
        
        if (index + 1 >= SPRITES.length) {
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
    
    @Override
    public void update(Game g) {
        
        super.update(g);
        if (animating && !finished && 
                System.currentTimeMillis() - timeLastSwitch > MILLIS_PER_SWITCH) {
            setSprite(SPRITES[nextSpriteIndex()]);
            timeLastSwitch = System.currentTimeMillis();
        }
        
    }
    
}
