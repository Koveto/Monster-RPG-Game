package game.gameObjects;

import game.Game;
import game.MovingPath;
import game.Path;
import game.Sprite;

/**
 * PathedAnimatedSpritedObject
 * @author Kobe Goodwin
 * @version 5/27/2022
 * 
 * An AnimatedSpritedObject that moves along a two dimensional path 
 * parameterized in the form r(t).
 */
public class PathedAnimatedSpritedObject extends AnimatedSpritedObject {
    
    private MovingPath path;
    
    public PathedAnimatedSpritedObject( Sprite[] sprites, MovingPath path, 
            int timePerSwitchMillis, boolean hideWhenFinished, boolean loopAnimation) {
        
        super(sprites, 0, 0, timePerSwitchMillis, hideWhenFinished, loopAnimation);
        this.path = path;
        setX(path.getX());
        setY(path.getY());
        path.increment();
        path.stop();
        
    }
    
    public PathedAnimatedSpritedObject( Sprite sprite, Path path, int x, int y ) {
        
        super(new Sprite[] {sprite}, x, y, 1);
        this.path = path;
        setX(x + (int) path.xComponent());
        setY(y + (int) path.yComponent());
        path.increment();
        
    }
    
    public void stopMoving( ) { path.stop(); }
    
    public void startMoving( ) { path.start(); }
    
    public MovingPath getPath( ) { return path; }
    
    public void setPath( MovingPath path ) {this.path = path;} 
    
    @Override
    public void update( Game g ) {
        
        super.update(g);
        if (path.isMoving()) {
            setX(path.getX());
            setY(path.getY());
            path.increment();
        }
        
    }
    
}
