package game;

import game.shuntingyardresources.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Path
 * @author Kobe Goodwin
 * @version 5/26/2022
 * 
 * A two-dimensional path parameterized in the form r(t) with respect to an
 * original x and y position that travels from a start to an end t value. For
 * example, consider the vector r(t) = (5, t). At t = 0, r(0) = (5, 0). With
 * respect to the original point P = (10, 10), the path at t = 0 is (15, 10).
 */
public class Path implements MovingPath {
    
    private double t, startT, endT, increment;
    private boolean loop, moving;
    private int originX, originY;
    private LinkedQueue<String> x, y;  // postfix equations
    
    /**
     * Constructor. Path travels with respect to the origin, (0, 0). In this
     * case, the return values of xComponent() and getX() are equal. The return 
     * values of yComponent() and getY() are also equal.
     * @param xComponent    function of x as a String. In the path r(t) =
     *      (-t, 5), xComponent is "-1 * t". Operators and operators must be
     *      separated by spaces. Grouping symbols are supported. Trigonometric
     *      functions, logarithms, and exponents are not supported.
     * @param yComponent    function of y as a String. In the path r(t) =
     *      (5, -t), yComponent is "-1 * t". Operators and operators must be
     *      separated by spaces. Grouping symbols are supported. Trigonometric
     *      functions, logarithms, and exponents are not supported.
     * @param startT        t value for which to begin traveling.
     * @param endT          t value for which to end traveling.
     * @param increment     number to increment t every frame.
     * @param loop          true to loop Path from the start when finished.
     */
    public Path( String xComponent, String yComponent, double startT, 
            double endT, double increment, boolean loop ) {
        
        this(xComponent, yComponent, 0, 0, startT, endT, increment, loop);
    
    }
    
    /**
     * Constructor.
     * @param xComponent    function of x as a String. In the path r(t) =
     *      (-t, 5), xComponent is "-1 * t". Operators and operators must be
     *      separated by spaces. Grouping symbols are supported. Trigonometric
     *      functions, logarithms, and exponents are not supported.
     * @param yComponent    function of y as a String. In the path r(t) =
     *      (5, -t), yComponent is "-1 * t". Operators and operators must be
     *      separated by spaces. Grouping symbols are supported. Trigonometric
     *      functions, logarithms, and exponents are not supported.
     * @param respectToX    x value to travel with respect to.
     * @param respectToY    y value to travel with respect to.
     * @param startT        t value for which to begin traveling.
     * @param endT          t value for which to end traveling.
     * @param increment     number to increment t every frame.
     * @param loop          true to loop Path from the start when finished.
     */
    public Path( String xComponent, String yComponent, int respectToX, 
            int respectToY, double startT, double endT, double increment, 
            boolean loop ) {
        
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
     * Stops the movement of the path.
     */
    @Override
    public void stop( ) { moving = false; }
    
    /**
     * Starts the movement of the path at the point (x, y).
     * @param x     X Position
     * @param y     Y Position
     */
    @Override
    public void startAt( int x, int y ) {
        setOriginX(x);
        setOriginY(y);
        start();
    }
    
    /**
     * Starts the movement of the path.
     */
    @Override
    public void start( ) { moving = true; }
    
    /**
     * Accessor for t value.
     * @return  current t value
     */
    public double getT( ) { return t; }
    
    /**
     * Accessor for start time, the t value at which the Path starts moving.
     * @return  start time
     */
    public double getStartT( ) { return startT; }
    
    /**
     * Accessor for end time, the t value for which the Path stops moving.
     * @return  end time
     */
    public double getEndT( ) { return endT; }
    
    /**
     * Accessor for original x position. For example, a sprite at (5, 10)
     * along the path r(t) = (t, 0) would be have an original x position of 5.
     * @return  original x position
     */
    public int getOriginX( ) { return originX; }
    
    /**
     * Accessor for original y position. For example, a sprite at (5, 10)
     * along the path r(t) = (t, 0) would be have an original y position of 10.
     * @return  original y position
     */
    public int getOriginY( ) { return originY; }
    
    /**
     * Mutator for original x position.
     * @param originX   new x position to move with respect to.
     */
    public void setOriginX( int originX ) { this.originX = originX; }
    
    /**
     * Mutator for original y position.
     * @param originY   new y position to move with respect to.
     */
    public void setOriginY( int originY ) { this.originY = originY; }
    
    /**
     * Evaluates if the path is moving.
     * @return  true if moving, false if not
     */
    @Override
    public boolean isMoving( ) { return moving; }
    
    /**
     * Evaluates if the path is finished traveling. This method will never
     * return true for a path that loops.
     * @return  true if finished traveling, false if not.
     */
    @Override
    public boolean isFinished( ) { 
        
        return t >= endT && !loop && endT != Double.NaN;
        
    }
    
    /**
     * Sets the t value to the starting t value. 
     */
    public void restart( ) { t = startT; }
    
    /**
     * Evaluates the x component of the path with the current t value. For
     * example, the path r(t) = (8 * t, 5) where t = 1 will return 8.
     * @return  x component of r(t) at current t.
     */
    @Override
    public double xComponent( ) {
        
        return PostfixCalculator.evaluatePostfix(x, "t", getT());
        
    }
    
    /**
     * Evaluates the y component of the path with the current t value. For
     * example, the path r(t) = (5, 8 * t) where t = 1 will return 8.
     * @return  y component of r(t) at current t.
     */
    public double yComponent( ) {
        
        return PostfixCalculator.evaluatePostfix(y, "t", getT());
        
    }
    
    /**
     * Accessor for the x position of the Path. Equal to the sum of xComponent()
     * and getOriginX().
     * @return  x position
     */
    @Override
    public int getX( ) {
        
        return originX + (int) xComponent();
        
    }
    
    /**
     * Accessor for the y position of the Path. Equal to the sum of yComponent()
     * and getOriginY().
     * @return  y position
     */
    @Override
    public int getY( ) {
        
        return originY + (int) yComponent();
        
    }
    
    /**
     * Increments the t value by the increment value.
     */
    @Override
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
