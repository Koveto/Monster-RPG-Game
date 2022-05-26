package game;

/**
 * CompoundPath
 * @author Kobe Goodwin
 * @version 5/26/2022
 * 
 * A Path composed of one or more subpaths. When one subpath finishes its
 * motion, the next begins its motion with respect to the end point of the
 * previous subpath.
 * 
 * For example, consider the Paths...
 *      r1(s) = (s, 0) where s varies from 0 to 2 rooted at P = (0, 0)
 *      r2(t) = (0, t) where t varies from 0 to 1 rooted at Q = (5, 5)
 * 
 * The compound path R(u) first travels along r1(s) until s = 2. At s = 2,
 * r1(s) = (2, 0) and the path is at the point (2, 0). Then, the path proceeds
 * with r2(t) rooted at the point (2, 0) and not Q. At t = 1, r2(1) = (0, 1)
 * and the path is at the point (2, 1). Because the last subpath has completed
 * its motion, the CompoundPath is now finished. If it loops, it would return
 * to the point P and start again at r1(s).
 */
public class CompoundPath implements MovingPath {
    
    private final Path[] PATHS;
    private final boolean LOOP;
    
    private int current, originX, originY, respectToX, respectToY;
    private boolean moving, finished;
    
    /**
     * Constructor. Path begins at the point (0, 0).
     * @param loop      True if path loops when completed, false if not.
     * @param paths     Subpaths in the order to traverse them.
     */
    public CompoundPath(boolean loop, Path... paths ) {
        
        this(0, 0, loop, paths);
        
    }
    
    /**
     * Constructor.
     * @param originX   X Position to start at.
     * @param originY   Y Position to start at.
     * @param loop      True if path loops when completed, false if not.
     * @param paths     Subpaths in the order to traverse them.
     */
    public CompoundPath(int originX, int originY, boolean loop, Path... paths ) {
        this.originX = originX;
        this.originY = originY;
        this.respectToX = originX;
        this.respectToY = originY;
        this.LOOP = loop;
        this.PATHS = paths;
    }
    
    /**
     * Starts the movement of the Path at the point (x, y), starting from
     * the first subpath.
     * @param x     X Position
     * @param y     Y Position
     */
    @Override
    public void startAt( int x, int y ) {
        
        PATHS[current].setOriginX(x);
        PATHS[current].setOriginY(y);
        originX = x;
        originY = y;
        respectToX = x;
        respectToY = y;
        start();
        
    }
    
    /**
     * Starts the movement of the Path from the first subpath.
     */
    @Override
    public void start( ) {
        moving = true;
        for (int i = 0; i < PATHS.length; i++) {
            if (!PATHS[i].isFinished()) {
                PATHS[i].start();
                current = i;
                break;
            }
        }
    }

    /**
     * Evaluates the current subpath's x component at its t value.
     * @return  current subpath x component.
     */
    @Override
    public double xComponent() {
        return PATHS[current].xComponent();
    }

    /**
     * Evaluates the current subpath's y component at its t value. 
     * @return  current subpath y component.
     */
    @Override
    public double yComponent() {
        return PATHS[current].yComponent();
    }
    
    /**
     * Accessor for the absolute x position. This is equal to the sum of
     * xComponent() and the x component of the previous subpath at its ending
     * t value.
     * @return  absolute x position.
     */
    @Override
    public int getX( ) {
        return respectToX + (int) xComponent();
    }
    
    /**
     * Accessor for the absolute y position. This is equal to the sum of
     * yComponent() and the y component of the previous subpath at its ending
     * t value.
     * @return  absolute y position.
     */
    @Override
    public int getY() {
        return respectToY + (int) yComponent();
    }

    /**
     * Stops the movement of the path.
     */
    @Override
    public void stop() {
        moving = false;
    }

    /**
     * Evaluates if the path is moving.
     * @return  true if moving, false if not.
     */
    @Override
    public boolean isMoving() { return moving; }
    
    /**
     * Evaluates if the path is finished traveling.
     * @return  true if path is finished, false if not.
     */
    @Override
    public boolean isFinished( ) { return finished; }
    
    /**
     * Increments the current subpath's t value. If the end t value of that
     * subpath is reached, the next subpath becomes the current subpath and
     * begins at its start t value.
     */
    @Override
    public void increment() {
        
        if (!PATHS[current].isFinished())
            PATHS[current].increment();
        else {
            for (int i = 0; i < PATHS.length; i++) {
                if (!PATHS[i].isFinished()) {
                    respectToX = getX();
                    respectToY = getY();
                    PATHS[i].start();
                    current = i;
                    break;
                }
                
            }
            if (PATHS[current].isFinished()) {
                if (LOOP) {
                    current = 0;
                    respectToX = originX;
                    respectToY = originY;
                    for (Path p : PATHS) p.restart();
                }
                else finished = true;
            }
        }
    }
    
}
