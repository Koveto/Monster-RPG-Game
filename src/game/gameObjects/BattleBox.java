package game.gameObjects;

import game.Game;
import game.RenderHandler;
import java.awt.Graphics;

/**
 * BattleBox
 * @author Kobe Goodwin
 * @version 4/1/2022
 */
public class BattleBox extends Rectangle {
    
    private int toX, toY, toW, toH, xScaleFactor, yScaleFactor;
    
    public BattleBox( Rectangle r ) {
        
        super(r.getX(), r.getY(), r.getWidth(), r.getHeight(), r.getColor());
        toX = r.getX();
        toY = r.getY();
        toW = r.getWidth();
        toH = r.getHeight();
        
    }
    
    public void shiftTo( int x, int y, int w, int h, int xScale, int yScale ) {
        
        toX = x;
        toY = y;
        toW = w;
        toH = h;
        xScaleFactor = xScale;
        yScaleFactor = yScale;
        
    }
    
    public void shiftTo( int x, int y, int w, int h ) {
        
        shiftTo(x, y, w, h, 1, 1);
        
    }
    
    @Override
    public void update( Game game ) {
        
        if (getX() != toX) {
            if (toX > getX()) {
                setX(getX() + xScaleFactor);
                if (getX() > toX) setX(toX);
            } else {
                setX(getX() - xScaleFactor);
                if (getX() < toX) setX(toX);
            }
        }
        if (getY() != toY) {
            if (toY > getY()) {
                setY(getY() + yScaleFactor);
                if (getY() > toY) setY(toY);
            } else {
                setY(getY() - yScaleFactor);
                if (getY() < toY) setY(toY);
            }
        }
        if (getWidth() != toW) {
            if (toW > getWidth()) {
                setWidth(getWidth() + xScaleFactor);
                if (getWidth() > toW) setWidth(toW);
            } else {
                setWidth(getWidth() - xScaleFactor);
                if (getWidth() < toW) setWidth(toW);
            }
        }
        if (getHeight() != toH) {
            if (toH > getHeight()) {
                setHeight(getHeight() + yScaleFactor);
                if (getHeight() > toH) setHeight(toH);
            } else {
                setHeight(getHeight() - yScaleFactor);
                if (getHeight() < toH) setHeight(toH);
            }
        }
        generateGraphics(getBorderWidth(), getColor());
        
    }
    
    @Override
    public void render( Graphics graphics ) {
        
        RenderHandler.renderRectangle(this);
        
    }
    
}
