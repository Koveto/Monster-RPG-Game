package game;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * CompoundPath
 * @author Kobe Goodwin
 * @version 5/20/2022
 * 
 */
public class CompoundPath implements MovingPath {
    
    private Path[] paths;
    private int current, originX, originY, respectToX, respectToY;
    private boolean moving, loop, finished;
    
    public CompoundPath(int originX, int originY, boolean loop, Path... paths ) {
        this.originX = originX;
        this.originY = originY;
        this.respectToX = originX;
        this.respectToY = originY;
        this.loop = loop;
        this.paths = paths;
    }
    
    @Override
    public void start( ) {
        moving = true;
        for (int i = 0; i < paths.length; i++) {
            if (!paths[i].isFinished()) {
                paths[i].start();
                current = i;
                break;
            }
        }
    }

    @Override
    public double xComponent() {
        return paths[current].xComponent();
    }

    @Override
    public double yComponent() {
        return paths[current].yComponent();
    }
    
    @Override
    public int xWithRespectToOrigin( ) {
        return respectToX + (int) xComponent();
    }
    
    @Override
    public int yWithRespectToOrigin() {
        return respectToY + (int) yComponent();
    }

    @Override
    public void stop() {
        moving = false;
    }

    @Override
    public boolean isMoving() { return moving; }
    
    @Override
    public boolean isFinished() { return finished; }

    @Override
    public void increment() {
        
        if (!paths[current].isFinished())
            paths[current].increment();
        else {
            for (int i = 0; i < paths.length; i++) {
                if (!paths[i].isFinished()) {
                    respectToX = xWithRespectToOrigin();
                    respectToY = yWithRespectToOrigin();
                    paths[i].start();
                    current = i;
                    break;
                }
                
            }
            if (paths[current].isFinished()) {
                if (loop) {
                    current = 0;
                    respectToX = originX;
                    respectToY = originY;
                    for (Path p : paths) p.restart();
                }
                else finished = true;
            }
        }
    }
    
}
