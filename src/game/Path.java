package game;

import game.shuntingyardresources.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
 

/**
 * Path
 * @author Kobe Goodwin
 * @version 5/20/2022
 * 
 * A two-dimensional path parameterized in the form r(t) that travels
 * from a start t value to an end t value.
 */
public class Path implements MovingPath {
    
    private double t, startT, endT, increment;
    private boolean loop, moving;
    private int originX, originY;
    private LinkedQueue<String> x, y;  // postfix equations
    
    public Path( String xComponent, String yComponent, int respectToX, int respectToY, double startT, 
            double endT, double increment, boolean loop ) {
        
        this.t = startT;
        this.startT = startT;
        this.endT = endT;
        this.increment = increment;
        this.loop = loop;
        this.originX = respectToX;
        this.originY = respectToY;
        LinkedQueue<String> xInfix = FileReader.createQueueFromLine(xComponent);
        LinkedQueue<String> yInfix = FileReader.createQueueFromLine(yComponent);
        x = InfixTranslator.infixToPostfix(xInfix);
        y = InfixTranslator.infixToPostfix(yInfix);
    
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
    
    public int getOriginX( ) { return originX; }
    
    public int getOriginY( ) { return originY; }
    
    public void setOriginX( int originX ) { this.originX = originX; }
    
    public void setOriginY( int originY ) { this.originY = originY; }
    
    /**
     * Evaluates if the path is moving
     * @return  true if moving, false if not
     */
    public boolean isMoving( ) { return moving; }
    
    public boolean isFinished( ) { return t >= endT && !loop && endT != Double.NaN; }
    
    public void restart( ) { t = 0; }
    
    /**
     * Evaluates the x component of the path with the current t value.
     * @return  x component of r(t) at current t.
     */
    public double xComponent( ) {
        
        return PostfixCalculator.evaluatePostfix(x, "t", getT());
        //return t;
        //return 200 + (50 * Math.cos(t));
        //return 200 + ((100 / (Math.pow(t, 2) + 1)) - 1);
        
    }
    
    /**
     * Evaluates the y component of the path with the current t value.
     * @return  y component of r(t) at current t.
     */
    public double yComponent( ) {
        
        return PostfixCalculator.evaluatePostfix(y, "t", getT());
        //return 200 + (50 * Math.sin(t));
        //return 200 + (100 * t) / (Math.pow(t, 2) + 1);
        
    }
    
    public int xWithRespectToOrigin( ) {
        
        return originX + (int) xComponent();
        
    }
    
    public int yWithRespectToOrigin( ) {
        
        return originY + (int) yComponent();
        
    }
    
    /**
     * Increment the t value.
     */
    public void increment( ) {
        
        if (isFinished())
            return;
        
        BigDecimal a = new BigDecimal(String.valueOf(t + increment));
        a = a.setScale(1, RoundingMode.HALF_UP);
        t = a.doubleValue();
        
        if (t >= endT && endT != Double.NaN && loop) {
            t = startT;
        }
        
    }
    
}
