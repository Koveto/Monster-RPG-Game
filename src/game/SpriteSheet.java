package game;

import java.awt.image.BufferedImage;

/**
 * SpriteSheet
 * @author Kobe Goodwin
 * @version 3/28/2022
 * 
 * A container for several Sprites loaded from a sheet image.
 */
public class SpriteSheet {
    
    private boolean spritesLoaded = false;
    private int[] pixels;
    private Sprite[] loadedSprites = null;
    
    private final int SPRITE_SIZE_X, SPRITE_SIZE_Y;
    private final BufferedImage IMAGE;
    private final int SIZE_X, SIZE_Y;
    
    /**
     * Constructor
     * @param sheetImage    BufferedImage representing SpriteSheet
     * @param spriteWidth   Width of each Sprite
     * @param spriteHeight  Height of each Sprite
     */
    public SpriteSheet( BufferedImage sheetImage, int spriteWidth, int spriteHeight )
    {
        IMAGE = sheetImage;
        SIZE_X = sheetImage.getWidth();
        SIZE_Y = sheetImage.getHeight();
        
        this.SPRITE_SIZE_X = spriteWidth;
        this.SPRITE_SIZE_Y = spriteHeight;
        
        pixels = new int[SIZE_X * SIZE_Y];
        pixels = sheetImage.getRGB(0, 0, SIZE_X, SIZE_Y, pixels, 0, SIZE_X);
        
        loadSprites(spriteWidth, spriteHeight);
    }
    
    /**
     * Accessor for pixels
     * @return  Array of colored pixels represented by integers
     */
    public int[] getPixels( )
    {
        return pixels;
    }
    
    /**
     * Accessor for image
     * @return  BufferedImage representing sheet image
     */
    public BufferedImage getImage( )
    {
        return IMAGE;
    }
    
    /**
     * Accessor for Sprite Width
     * @return  Sprite width
     */
    public int getSpriteWidth( ) {return SPRITE_SIZE_X;}
    
    /**
     * Accessor for Sprite Height
     * @return  Sprite height
     */
    public int getSpriteHeight( ) {return SPRITE_SIZE_Y;}
    
    /**
     * Loads sprites in the image for the X and Y sizes.
     * @param spriteSizeX   Width of each Sprite
     * @param spriteSizeY   Height of each Sprite
     */
    private void loadSprites( int spriteSizeX, int spriteSizeY )
    {
        loadedSprites = new Sprite[(SIZE_X / spriteSizeX) * (SIZE_Y / spriteSizeY)];
        int spriteID = 0;
        for (int y = 0; y < SIZE_Y; y += spriteSizeY)
            for (int x = 0; x < SIZE_X; x += spriteSizeX)
            {
                loadedSprites[spriteID] = new Sprite(this, x, y, spriteSizeX, spriteSizeY);
                spriteID++;
            }
        
        spritesLoaded = true;
    }
    
    /**
     * Retrieves Sprite from SpriteSheet at an index
     * @param x Number of sprites from x = 0
     * @param y Number of sprites from y = 0
     * @return  Sprite at index
     */
    public Sprite getSprite( int x, int y )
    {
        if (spritesLoaded)
        {
            int spriteID = x + y * (SIZE_X / SPRITE_SIZE_X); // width is sizex/spritesizex
            
            if (spriteID < loadedSprites.length)
                return loadedSprites[spriteID];
            else
                System.out.println("Sprite ID of " + spriteID + " is out of range with a maximum of " + loadedSprites.length + ".");
        }
        else
            System.out.println("SpriteSheet could not get a sprite without loaded sprites.");
        
        return null;
    }
    
    /**
     * Retrieves all Sprites stored in SpriteSheet
     * @return  Array of Sprites in sheet
     */
    public Sprite[] getSprites( ) {
        
        int numberOfSprites = (SIZE_X / SPRITE_SIZE_X) * (SIZE_Y / SPRITE_SIZE_Y);
        Sprite[] sprites = new Sprite[numberOfSprites];
        for (int i = 0; i < (SIZE_X / SPRITE_SIZE_X); i++) {
            for (int j = 0; j < (SIZE_Y / SPRITE_SIZE_Y); j++) {
                sprites[i + j] = getSprite(i, j);
            }
        }
        return sprites;
        
    }
    
    /**
     * Compares Object to SpriteSheet. Must have the same pixels, image, sprite
     * width, and sprite height.
     * @param o Object to compare to.
     * @return  Boolean evaluating if the objects are the same.
     */
    public boolean equals( Object o ) {
        
        if (!(o instanceof SpriteSheet)) return false;
        SpriteSheet ss = (SpriteSheet) o;
        return ss.getPixels().equals(this.getPixels())
                && ss.getImage().equals(this.getImage())
                && ss.getSpriteWidth() == this.getSpriteWidth()
                && ss.getSpriteHeight() == this.getSpriteHeight();
        
    } 
    
    /**
     * Describes SpriteSheet. In the form...
     * <p> SpriteSheet:pixels:spritesLoaded:loadedSprites:spriteWidth:spriteHeight:
     * width:height:image
     * @return  String describing SpriteSheet.
     */
    public String toString( ) {
        
        return getClass().getName() + ":" + this.pixels + ":" + this.spritesLoaded
                + ":" + this.loadedSprites + ":" + this.SPRITE_SIZE_X + ":" +
                this.SPRITE_SIZE_Y + ":" + this.SIZE_X + ":" + this.SIZE_Y +
                ":" + this.IMAGE;
        
    }
}
