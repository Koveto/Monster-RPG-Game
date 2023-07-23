package game.gameObjects;

import game.Game;
import java.awt.Graphics;

/**
 * GameObject
 * @author Kobe Goodwin
 * @version 7/18/2023
 * 
 * An object to be rendered to the screen and updated every frame.
 */
public interface GameObject {
    
    int getX( );
    
    int getY( );
    
    double getTransparency( );
    
    void setX( int x );
    
    void setY( int y );
    
    void show( );
    
    void hide( );
    
    void setTransparency( double transparency );
    
    void render( );
    
    void render( Graphics graphics );
    
    void update( Game game );
    
}
