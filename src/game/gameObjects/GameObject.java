package game.gameObjects;

import game.Game;
import java.awt.Graphics;

/**
 * GameObject
 * @author Kobe Goodwin
 * @version 3/12/2022
 * 
 * An object to be rendered to the screen and updated every frame.
 */
public interface GameObject {
    
    int getX( );
    
    int getY( );
    
    double getTransparency( );
    
    void setX( int x );
    
    void setY( int y );
    
    void setTransparency( double transparency );
    
    void render( );
    
    void render( Graphics graphics );
    
    void update( Game game );
    
}
