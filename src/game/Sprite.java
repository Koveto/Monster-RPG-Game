package game;

import java.awt.image.BufferedImage;

/**
 * Sprite
 * @author Kobe Goodwin
 * @version 3/12/2022
 * 
 * An image represented by an array of colored pixels. It has a width and height.
 */
public class Sprite {
    
    private int width, height;
    private int[] pixels;
    
    /**
     * Constructor
     * @param spriteSheet   SpriteSheet Sprite is from
     * @param startX        Starting x coordinate on the spritesheet
     * @param startY        Starting y coordinate on the spritesheet
     * @param width         Width
     * @param height        Height
     */
    public Sprite( SpriteSheet spriteSheet, int startX, int startY, int width, int height )
    {
        this.width = width;
        this.height = height;
        
        pixels = new int[width * height];
        
        spriteSheet.getImage().getRGB(startX, startY, width, height, pixels, 0, width);
    }
    
    /**
     * Constructor
     * @param image     BufferedImage to interpret as a Sprite 
     */
    public Sprite( BufferedImage image )
    {
        this.width = image.getWidth();
        this.height = image.getHeight();
        
        pixels = new int[width * height];
        
        image.getRGB(0, 0, width, height, pixels, 0, width);
    }
    
    /**
     * Constructor
     * @param pixels    Array of Integer RGB values
     * @param width     Width of image
     * @param height    Height of image
     */
    public Sprite( int[] pixels, int width, int height ) {
        
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        
    }
    
    /**
     * Accessor for Width
     * @return  Width
     */
    public int getWidth( )
    {
        return width;
    }
    
    /**
     * Accessor for Height
     * @return  Height
     */
    public int getHeight( )
    {
        return height;
    }
    
    /**
     * Accessor for pixels
     * @return  Array of pixels representing the color at each pixel
     */
    public int[] getPixels( )
    {
        return pixels;
    }
    
    /**
     * Compares Object to Sprite. Must have the same pixels, height, and width.
     * @param o Object to compare to.
     * @return  Boolean evaluating if the objects are the same.
     */
    public boolean equals( Object o ) {
        
        if (!(o instanceof Sprite)) return false;
        Sprite s = (Sprite) o;
        return s.getPixels().equals(this.getPixels())
                && s.getWidth() == this.getWidth()
                && s.getHeight() == this.getHeight();
        
    } 
    
    /**
     * Describes Sprite. In the form...
     * <p> Sprite:width:height:pixels
     * @return  String describing Sprite.
     */
    public String toString( ) {
        
        return getClass().getName() + ":" + width + ":" + height + ":" + 
                getPixels();
        
    }
    
}
