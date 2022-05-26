package game;

import game.shuntingyardresources.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
 

/**
 * AbstractPath
 * @author Kobe Goodwin
 * @version 5/20/2022
 * 
 * A two-dimensional path parameterized in the form r(t) that travels
 * from a start t value to an end t value.
 */
public abstract class AbstractPath {
    
    private double t, startT, endT, increment;
    private boolean loop, moving;
    
    public AbstractPath( double startT, double endT, double increment, boolean loop ) {
        
        this.t = startT;
        this.startT = startT;
        this.endT = endT;
        this.increment = increment;
        this.loop = loop;
        
    }
    
    /**
     * Stops the movement of the path
     */
    public void stop( ) { moving = false; }
    
    /**
     * Starts the movement of the path
     */
    public void start( ) { moving = true; }
    
    /**
     * Accessor for time
     * @return  time
     */
    public double getT( ) { return t; }
    
    /**
     * Accessor for start time
     * @return  start time
     */
    public double getStartT( ) { return startT; }
    
    /**
     * Accessor for end time
     * @return  end time
     */
    public double getEndT( ) { return endT; }
    
    /**
     * Evaluates if the path is moving
     * @return  true if moving, false if not
     */
    public boolean isMoving( ) { return moving; }
    
    /**
     * Evaluates the x component of the path with the current t value.
     * @return  x component of r(t) at current t.
     */
    public abstract double xComponent( );
    
    /**
     * Evaluates the y component of the path with the current t value.
     * @return  y component of r(t) at current t.
     */
    public abstract double yComponent( );
    
    /**
     * Increment the t value.
     */
    public void increment( ) {
        
        if (t >= endT && !loop && endT != Double.NaN)
            return;
        
        BigDecimal a = new BigDecimal(String.valueOf(t + increment));
        a = a.setScale(1, RoundingMode.HALF_UP);
        t = a.doubleValue();
        
        if (t >= endT && endT != Double.NaN && loop) {
            t = startT;
        }
        
    }
    
}
