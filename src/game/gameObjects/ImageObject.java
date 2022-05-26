package game.gameObjects;

import game.Game;
import game.RenderHandler;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * ImageObject
 * @author Kobe Goodwin
 * @version 3/12/2022
 * 
 * An image to be rendered and updated.
 */
public class ImageObject implements GameObject {

    private BufferedImage image;
    private int x, y;
    private double transparency;
    
    /**
     * Constructor
     * @param image         Image
     * @param xPosition     X Position
     * @param yPosition     Y Position
     */
    public ImageObject( BufferedImage image, int xPosition, int yPosition ) {
        
        this.image = image;
        this.x = xPosition;
        this.y = yPosition;
        this.transparency = 1;
        
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
     * Accessor for Transparency
     * @return  Transparency between 0.0 and 1.0
     */
    public double getTransparency( ) {return transparency;}
    
    /**
     * Mutator for X Position
     * @param x     X Position
     */
    public void setX( int x ) {this.x = x;}
    
    /**
     * Mutator for Y Position
     * @param y     Y Position
     */
    public void setY( int y ) {this.y = y;}
    
    /**
     * Mutator for Transparency
     * @param transparency  New transparency between 0.0 and 1.0
     */
    public void setTransparency( double transparency ) {
        this.transparency = transparency;
    }
    
    /**
     * Renders image to screen
     */
    @Override
    public void render( ) {
        RenderHandler.renderImage(image, x, y);
    }
    
    /**
     * Renders image with transparency
     * @param graphics  Graphics
     */
    public void render( Graphics graphics ) {
        RenderHandler.renderImage(graphics, image, x, y, transparency);
    }

    /**
     * No implementation.
     * @param game 
     */
    @Override
    public void update(Game game) {}
    
}
