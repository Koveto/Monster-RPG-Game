package game.gameObjects;

import game.Game;
import game.RenderHandler;
import game.gameObjects.GameObject;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Rectangle
 * @author Kobe Goodwin
 * @version 4/1/2022
 * 
 * A rectangle with a width, height, x position, and y position to be rendered
 * and updated.
 */
public class Rectangle implements GameObject {
    
    private int x, y, w, h, color, borderWidth;
    private double transparency;
    private int[] pixels;

    /**
     * Constructor
     * @param x             X Position
     * @param y             Y Position
     * @param w             Width
     * @param h             Height
     * @param color         Color
     * @param borderWidth   Width of border
     */
    public Rectangle( int x, int y, int w, int h, int color, int borderWidth ) {
        
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = color;
        this.borderWidth = borderWidth;
        generateGraphics(borderWidth, color);
        transparency = 1;
        
    }
    
    /**
     * Constructor
     * @param x     X Position
     * @param y     Y Position
     * @param w     Width
     * @param h     Height
     * @param color Color
     */
    public Rectangle( int x, int y, int w, int h, int color ) {
        
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = color;
        generateGraphics(color);
        transparency = 1;
        
    }
    
    /**
     * Constructor
     * @param x X Position
     * @param y Y Position
     * @param w Width
     * @param h Height
     */
    public Rectangle( int x, int y, int w, int h )
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = 0xFFFFFFFF;
        generateGraphics(0xFFFFFFFF);
        transparency = 1;
    }
    
    /**
     * Default constructor. 0 width and 0 height at (0,0). Solid white.
     */
    public Rectangle( )
    {
        this(1,1,1,1);
        this.color = 0xFFFFFFFF;
        generateGraphics(0xFFFFFFFF);
        transparency = 1;
    }
    
    /**
     * Generate a solid color rectangle.
     * @param color     Color
     */
    public void generateGraphics( int color )
    {
        this.color = color;
        pixels = new int[w*h];
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
            {
                pixels[x + y * w] = color;
            }
    }
    
    /**
     * Generate a colored border.
     * @param borderWidth   Size of border
     * @param color         Color
     */
    public void generateGraphics( int borderWidth, int color )
    {
        pixels = new int[w*h];
        this.borderWidth = borderWidth;
        this.color = color;
        
        for (int i = 0; i < pixels.length; i++)
        {
            pixels[i] = Game.ALPHA;
        }
        
        for (int y = 0; y < h; y++)
        {
            if (y < borderWidth || y >= h - borderWidth)
                for (int x = 0; x < w; x++)
                    pixels[x + y * w] = color;
            else
            {
                for (int x = 0; x < w; x++) {
                    if (x < borderWidth || x >= w - borderWidth) {
                        pixels[x + y * w] = color;
                    }
                }
            }
        }
    }
    
    /**
     * Accessor for X Position
     * @return  X Position
     */
    public int getX( ) {return x;}
    
    /**
     * Accessor for Y Position
     * @return  Y Position
     */
    public int getY( ) {return y;}
    
    /**
     * Accessor for Width
     * @return  Width
     */
    public int getWidth( ) {return w;}
    
    /**
     * Accessor for Height
     * @return  Height
     */
    public int getHeight( ) {return h;}
    
    /**
     * Accessor for Color
     * @return  Color RGB
     */
    public int getColor( ) {return color;}
    
    /**
     * Accessor for Border Width
     * @return  Border Width
     */
    public int getBorderWidth( ) {return borderWidth;}
    
    /**
     * Mutator for X Position
     * @param x     New X Position
     */
    public void setX( int x ) {this.x = x;}
    
    /**
     * Mutator for Y Position
     * @param y     New Y Position
     */
    public void setY( int y ) {this.y = y;}
    
    /**
     * Mutator for Width
     * @param w     New width
     */
    public void setWidth( int w ) {this.w = w;}
    
    /**
     * Mutator for Height
     * @param h     New height
     */
    public void setHeight( int h ) {this.h = h;}
    
    /**
     * Accessor for pixel array
     * @return  Array of pixels representing the color at each position. Returns
     * null if graphics have not been generated.
     */
    public int[] getPixels( )
    {
        if (pixels != null)
            return pixels;
        else
            System.out.println("Attempted to receive pixels without generated graphics.");
        return null;
    }
    
    /**
     * Accessor for Transparency
     * @return  Transparency between 0.0 and 1.0
     */
    public double getTransparency( ) {return transparency;}
    
    /**
     * Mutator for Transparency
     * @param transparency  Transparency between 0.0 and 1.0
     */
    public void setTransparency( double transparency ) {this.transparency = transparency;}
    
    /**
     * Render the rectangle to the screen.
     */
    public void render( ) {
        RenderHandler.renderRectangle(this);
    }
    
    /**
     * Render the rectangle to the screen.
     * @param graphics 
     */
    public void render( Graphics graphics ) {
        RenderHandler.renderRectangle(graphics, this, transparency);
    }
    
    /**
     * No implementation.
     * @param game  Game
     */
    public void update(Game game) {}
    
    /**
     * Compares Object to Rectangle. Must have the same x and y position and
     * width and height. Must have the same colored pixels.
     * @param o Object to compare to.
     * @return  Boolean evaluating if the objects are the same.
     */
    public boolean equals( Object o ) {
        
        if (!(o instanceof Rectangle)) return false;
        Rectangle r = (Rectangle) o;
        return r.x == this.x
                && r.y == this.y
                && r.w == this.w
                && r.h == this.h
                && r.getPixels().equals(this.getPixels());
        
    }
    
    /**
     * Describes Rectangle. In the form...
     * <p> Rectangle:x:y:w:h:pixels
     * @return  String describing Rectangle
     */
    public String toString( ) {
        
        return getClass().getName() + ":" + x + ":" + y + ":" + w + ":" + h +
                ":" + getPixels().toString();
        
    }
}
