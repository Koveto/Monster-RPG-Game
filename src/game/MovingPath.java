package game;

/**
 * MovingPath
 * @author Kobe Goodwin
 * @version 5/25/2022
 * 
 * A path that moves along two dimensions over a period of time t. It can be
 * started and stopped, and finishes when an end t value is reached. The path
 * moves with respect to some original point.
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
    
    /**
     * Evaluates if the path is finished traveling.
     * @return  true if finished, false if not
     */
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
    
    /**
     * Accessor for absolute x position.
     * @return  absolute x position
     */
    int getX( );
    
    /**
     * Accessor for absolute y position.
     * @return  absolute y position
     */
    int getY( );
    
    /**
     * Increment the t value.
     */
    void increment( );
    
}
