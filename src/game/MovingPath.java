package game;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * MovingPath
 * @author Kobe Goodwin
 */
public interface MovingPath {
    
    /**
     * Stops the movement of the path
     */
    void stop( );
    
    /**
     * Starts the movement of the path
     */
    void start( );
    
    /**
     * Evaluates if the path is moving
     * @return  true if moving, false if not
     */
    boolean isMoving( );
    
    boolean isFinished( );
    
    /**
     * Evaluates the x component of the path with the current t value.
     * @return  x component of r(t) at current t.
     */
    double xComponent( );
    
    /**
     * Evaluates the y component of the path with the current t value.
     * @return  y component of r(t) at current t.
     */
    double yComponent( );
    
    int xWithRespectToOrigin( );
    
    int yWithRespectToOrigin( );
    
    /**
     * Increment the t value.
     */
    void increment( );
    
}
