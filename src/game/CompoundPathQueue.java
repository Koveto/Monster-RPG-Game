/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author bluey
 */
public class CompoundPathQueue {
    
    private CompoundPath[] paths;
    private int current;
    private boolean moving;
    
    public CompoundPathQueue( CompoundPath[] paths ) {
        
        this.paths = paths;
        paths[current].start();
        moving = true;
        
    }
    
    public CompoundPath get( ) { return paths[current]; }
    
    public boolean isMoving( ) { return moving; }
    
    public void start( ) {
        current = 0;
        paths[current].start();
        moving = true;
    }
    
    public void stop( ) {
        moving = false;
    }
    
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
