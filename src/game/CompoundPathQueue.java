package game;

/**
 * CompoundPathQueue
 * @author Kobe Goodwin
 * @version 5/25/2022
 * 
 * An array of CompoundPaths. When the path finishes, the next path is started, 
 * until there are none left in the queue.
 */
public class CompoundPathQueue {
    
    private CompoundPath[] paths;
    private int current;
    private boolean moving;
    
    /**
     * Constructor.
     * @param paths     Array of CompoundPaths to progress through.
     */
    public CompoundPathQueue( CompoundPath[] paths ) {
        
        this.paths = paths;
        
    }
    
    /**
     * Retrieves the currently moving path.
     * @return  currently moving CompoundPath
     */
    public CompoundPath get( ) { return paths[current]; }
    
    /**
     * Evaluates if a CompoundPath in the array is moving.
     * @return  True if a CompoundPath is moving.
     */
    public boolean isMoving( ) { return moving; }
    
    /**
     * Starts the queue from the first CompoundPath.
     */
    public void start( ) {
        current = 0;
        paths[current].start();
        moving = true;
    }
    
    /**
     * Starts the queue as it was previously.
     */
    public void resume( ) {
        moving = true;
    }
    
    /**
     * Pauses the queue.
     */
    public void stop( ) {
        moving = false;
    }
    
    /**
     * Moves on to the next CompoundPath if the current one is finished.
     * @return  True if moved on to new CompoundPath, false if resuming current.
     */
    public boolean update( ) {
        
        if (moving && paths[current].isFinished()) {
            paths[current].stop();
            if (current + 1 < paths.length) {
                current++;
                paths[current].start();
                return true;
            } else moving = false;
        }
        return false;
        
    }
    
    
}
