package game.gameObjects;

import game.Game;
import game.RenderHandler;
import game.Sprite;
import game.SpriteSheet;
import java.awt.Color;
import java.awt.Graphics;

/**
 * SpritedObject
 * @author Kobe Goodwin
 * @version 8/31/2022
 * 
 * A GameObject with a sprite rendered at a given x and y position.
 */
public class SpritedObject implements GameObject {

    private Sprite sprite;
    private int x, y, flashCount, brightnessToggleCount, timesToToggleBrightness,
            fadeInCount, fadeInSpeed, fadeOutCount, fadeOutSpeed, colorToggleCount,
            timesToToggleColor, tempCount, darkenToBlackCount, brightnessIndex,
            brightestIndex;
    private float brightnessToggleFactor, toFullBrightFactor, colorToggleFactor,
            darkenToBlackFactor;
    private boolean show, isFlashing, isTogglingBrightness, isFadingIn, isFadingOut, 
            isBrighteningToFull, isChangingColor, isDarkeningToBlack;
    private double transparency;
    
    private final int COUNT_UNTIL_BRIGHTEN = 15;
    private final int COUNT_UNTIL_DARKEN = 38;
    
    private int[] defaultPixels;
    
    /**
     * Constructor
     * @param sprite    Sprite to display
     * @param x         X Position
     * @param y         Y Position
     */
    public SpritedObject( Sprite sprite, int x, int y ) {
        
        this.sprite = sprite;
        //System.out.println("init:" + (this instanceof RatioBar) + sprite.getPixels().length);
        this.x = x;
        this.y = y;
        this.show = true;
        this.isFlashing = false;
        this.flashCount = 0;
        this.timesToToggleBrightness = 0;
        this.brightnessToggleFactor = 0;
        this.brightnessToggleCount = 0;
        this.transparency = 1;
        this.defaultPixels = sprite.getPixels();
        this.tempCount = 0;
        this.brightnessIndex = -1;
        this.brightestIndex = findBrightestIndex(sprite.getPixels());
        
        
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
     * @return  Transparency from 0.0 to 1.0
     */
    public double getTransparency( ) {return transparency;}
    
    /**
     * Accessor for Sprite
     * @return  Current sprite being displayed
     */
    public Sprite getSprite( ) {return sprite;}
    
    /**
     * Determines if Sprite is showing
     * @return  True if Sprite is showing, False if not
     */
    public boolean isShowing( ) {return show;}
    
    /**
     * Determines if Sprite is fading in
     * @return  True if Sprite is fading in, false if not
     */
    public boolean isFadingIn( ) {return isFadingIn;}
    
    /**
     * Determines if Sprite is fading out
     * @return  True if Sprite is fading out, false if not
     */
    public boolean isFadingOut( ) {return isFadingOut;}
    
    /**
     * Determines if Sprite is darkening to black
     * @return  True if Sprite is darkening, false if not
     */
    public boolean isDarkeningToBlack( ) {return isDarkeningToBlack;}
    
    /**
     * Mutator for X Position
     * @param x New x position
     */
    public void setX( int x ) { this.x = x; }
    
    /**
     * Mutator for Y Position
     * @param y New y position
     */
    public void setY( int y ) { this.y = y; }
    
    /**
     * Mutator for default pixels
     * @param pixels    New default pixels
     */
    public void setDefaultPixels( int[] pixels ) {this.defaultPixels = pixels;}
    
    /**
     * Mutator for Transparency
     * @param transparency  New transparency from 0.0 to 1.0
     */
    public void setTransparency( double transparency ) {this.transparency = transparency;}
    
    /**
     * Mutator for Sprite
     * @param sprite    New Sprite
     */
    public void setSprite( Sprite sprite ) {
        this.sprite = sprite;
        this.brightestIndex = findBrightestIndex(sprite.getPixels());
    }
    
    /**
     * Shows the Sprite on screen.
     */
    public void show( ) {this.show = true;}
    
    /**
     * Hides the Sprite.
     */
    public void hide( ) {this.show = false;}
    
    /**
     * Accessor for Flashing
     * @return  Evaluates whether Sprite is flashing
     */
    public boolean isFlashing( ) {return this.isFlashing;}
    
    public boolean isChangingColor( ) {return this.isChangingColor;}
    
    /**
     * Darkens and brightens the Sprite.
     */
    public void flash( ) {this.isFlashing = true;}
    
    /**
     * Sets the Sprite transparent and gradually fades in
     * @param speed     Speed to fade in
     */
    public void fadeIn( int speed ) {
        
        setTransparency(0.05);
        isFadingIn = true;
        isFadingOut = false;
        fadeInCount = 0;
        fadeOutCount = 0;
        fadeInSpeed = speed;
        
    }
    
    /**
     * Fades the Sprite to fully transparent
     * @param speed     Speed to fade out
     */
    public void fadeOut( int speed ) {
        
        isFadingOut = true;
        isFadingIn = false;
        fadeOutCount = 0;
        fadeInCount = 0;
        fadeOutSpeed = speed;
        
    }
    
    /**
     * Darkens or brightens a sprite by a factor a provided number of times.
     * @param factor            greater than 1 to brighten, less than 1 to darken
     * @param timesToScale      number of frames the factor is used
     */
    public void toggleBrightness( float factor, int timesToScale ) {
        
        this.brightnessToggleFactor = factor;
        this.brightnessToggleCount = 0;
        this.timesToToggleBrightness = timesToScale;
        this.isTogglingBrightness = true;
        
    }
    
    public void toggleRed( float factor, int timesToScale ) {
    
        this.colorToggleFactor = factor;
        this.colorToggleCount = 0;
        this.timesToToggleColor = timesToScale;
        this.isChangingColor = true;
    }
    
    /**
     * Brightens the Sprite to its default brightness
     * @param factor    Multiplier to brighten by each frame
     */
    public void brightenToDefault( float factor ) {
        
        this.toFullBrightFactor = factor;
        this.isBrighteningToFull = true;
        
    }
    
    public void darkenToBlack( float factor ) {
        
        setDefaultPixels(sprite.getPixels());
        this.darkenToBlackCount = 1;
        this.darkenToBlackFactor = factor;
        this.isDarkeningToBlack = true;
        
    }
    
    /**
     * Resets brightness to default
     */
    public void resetBrightness( ) {
        
        sprite = new Sprite(defaultPixels, sprite.getWidth(), sprite.getHeight());
        //flashCount = 0;
        //isFlashing = false;
        isTogglingBrightness = false;
        brightnessToggleCount = 0;
        brightnessToggleFactor = 0;
        
        
    }
    
    /**
     * Stops flashing.
     */
    public void stopFlashing( ) {
        
        flashCount = 0;
        isFlashing = false;
        
    }
    
    /**
     * Brightens or darkens the sprite by a factor.
     * @param factor    Greater than 1 brightens, less than 1 darkens. 
     * 0 sets the sprite to black.
     */
    private void toggleColor( float factor, boolean red, boolean green, boolean blue ) {
        
        int[] pixelArray = sprite.getPixels();
        int[] newPixelArray = new int[pixelArray.length];
        for (int i = 0; i < newPixelArray.length; i++) {
            Color c = Color.decode(String.valueOf(pixelArray[i]));
            newPixelArray[i] = scaleColorByFactor(c, factor, red, green, blue).getRGB();
        }
        sprite = new Sprite(newPixelArray, sprite.getWidth(), sprite.getHeight());
    }
    
    public void toggleRed( float factor ) {
        
        toggleColor(factor, true, false, false);
        
    }
    
    public void toggleGreen( float factor ) {
        
        toggleColor(factor, false, true, false);
        
    }
    
    public void toggleBlue( float factor ) {
        
        toggleColor(factor, false, false, true);
        
    }
    
    public void toggleCyan( float factor ) {
        
        toggleColor(factor, false, true, true);
        
    }
    
    public void toggleMagenta( float factor ) {
        
        toggleColor(factor, true, false, true);
        
    }
    
    public void toggleYellow( float factor ) {
        
        toggleColor(factor, true, true, false);
        
    }
    
    public void toggleBrightness( float factor ) {
        
        toggleColor(factor, true, true, true);
        
    }
    
    /**
     * Scales the RGB values of a color by a factor. If the color is equal to
     * the alpha color, no change is made.
     * @param c         Color to scale
     * @param factor    Factor to scale by. >1 for brighter, less than 1 for darker.
     * @return  Scaled color
     */
    private Color scaleColorByFactor(Color c, float factor, boolean scaleRed,
            boolean scaleGreen, boolean scaleBlue){
        
        if (factor < 0) factor = 0;
        
        if (tempCount < 8113)
            tempCount++;
        else
            tempCount = 0;
        
        if (c.equals(Color.decode(String.valueOf(Game.ALPHA)))) return Color.decode(String.valueOf(Game.ALPHA));
        
        if(factor < 0)
            throw new IllegalArgumentException("Factor must be positive");

        int[] components = new int[] {c.getRed(), c.getGreen(), c.getBlue()};
        //float[] components = new float[] {c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()};

        float alpha = 0.5F;
        if (c.getAlpha() == 255) alpha = 1.0F;
        
        
        int red = (scaleRed) ? (int) (components[0] * factor) : components[0];
        int green = (scaleGreen) ? (int) (components[1] * factor) : components[1];
        int blue = (scaleBlue) ? (int) (components[2] * factor) : components[2];
        
        return new Color(Math.min(red == 0 ? 1 : red , 255) , 
                         Math.min(green == 0 ? 1 : green, 255) ,
                         Math.min(blue == 0 ? 1 : blue, 255) , 
                         255);
    }
    
    public boolean isMaxBright() {
        
        if (brightnessIndex == -1) setBrightnessIndex();
        Color c = Color.decode(String.valueOf(getSprite().getPixels()[brightnessIndex]));
        if (c.getRed() == 255 || c.getGreen() == 255 || c.getBlue() == 255) {
            return true;
        }
        return false;
        
    }
    
    public boolean isMaxDark() {
        
        //if (brightnessIndex == -1) setBrightnessIndex();
        Color c = Color.decode(String.valueOf(getSprite().getPixels()[506]));
        if (c.getRed() == 1 || c.getGreen() == 1 || c.getBlue() == 1) {
            return true;
        }
        return false;
        
    }
    
    /**
     * Sets brightnessIndex to the first pixel which is not alpha or black.
     */
    private void setBrightnessIndex( ) {
        
        for (int i = 0; i < getSprite().getPixels().length; i++) {
            if (getSprite().getPixels()[i] != Game.ALPHA) {
                Color c = Color.decode(String.valueOf(getSprite().getPixels()[i]));
                if (!(c.getRed() == 1 && c.getGreen() == 1 && c.getBlue() == 1)) {
                    brightnessIndex = i;
                    return;
                }
            }
        }
        
    }
    
    private int findFirstNonBlackIndex( int[] pixels ) {
        
        int indexOfColor = 0;
        for (int i = 0; i < pixels.length; i++) {
            Color c = Color.decode(String.valueOf(pixels[i]));
            if (c.getRed() == 0 && c.getGreen() == 0 & c.getBlue() == 0) {
                indexOfColor++;
            }
        }
        return indexOfColor;
        
    }
    
    private int findFirstOpaqueIndex( int[] pixels ) {
        
        int indexOfColor = 0;
        boolean indexFound = false;
        while (!indexFound) {
            Color c = Color.decode(String.valueOf(pixels[indexOfColor]));
            if (!(c.getRed() == 27 && c.getGreen() == 36 & c.getBlue() == 30)) {
                indexFound = true;
            }
            else
                indexOfColor++;
        }
        return indexOfColor;
        
    }
    
    private int findBrightestIndex( int[] pixels ) {
        
        int indexOfColor = 0;
        for (int i = 0; i < pixels.length; i++) {
            Color c = Color.decode(String.valueOf(pixels[i]));
            Color brightest = Color.decode(String.valueOf(pixels[indexOfColor]));
            if (((c.getRed() + c.getGreen() + c.getBlue()) > 
                    (brightest.getRed() + brightest.getGreen() + brightest.getBlue())
                    && c.getRGB() != Game.ALPHA) || (brightest.getRGB() == Game.ALPHA)) {
                indexOfColor = i;
            }
        }
        return indexOfColor;
        
    }
    
    /**
     * Renders the current sprite to the screen at its x and y position.
     */
    @Override
    public void render( ) {
        if (show) RenderHandler.renderSprite(sprite, x, y, (Game.isBattle()) ? 1 : 2, (Game.isBattle()) ? 1 : 2);
    }
    
    /**
     * Renders the current sprite to the screen at its x and y position. Used
     * when object is partially transparent.
     * @param graphics 
     */
    @Override
    public void render( Graphics graphics ) {
        if (show) RenderHandler.renderSprite(graphics, sprite, x, y, transparency);
    }
    
    protected void updateBrightness( ) {
        
        if (isDarkeningToBlack) {
            
            resetBrightness();
            toggleBrightness(Float.parseFloat(String.valueOf(
                    (Math.pow(Double.parseDouble(String.valueOf(darkenToBlackFactor)),
                            darkenToBlackCount)))));
            Color c = Color.decode(String.valueOf(sprite.getPixels()[brightestIndex]));
            if (c.getRed() < 10 && c.getGreen() < 10 && c.getBlue() < 10) {
                isDarkeningToBlack = false;
                hide();
            }
            darkenToBlackCount++;
            
        }
        
    }

    /**
     * Toggles the brightness and transparency each frame.
     * @param game 
     */
    @Override
    public void update(Game game) {
        
        if (isChangingColor && colorToggleCount < timesToToggleColor) {
            
            toggleMagenta(colorToggleFactor);
            colorToggleCount++;
            
        } else if (isChangingColor && colorToggleCount == timesToToggleColor) {
            
            isChangingColor = false;
            
        }
        
        if (isFlashing) {
            
            flashCount++;
            float a = 0.03F;
            if (flashCount == 51) flashCount = 0;
            resetBrightness();
            if (flashCount < 20) {
                toggleBrightness(1 - (a * flashCount));
            } else if (flashCount >= 20 && flashCount < 25) {
                toggleBrightness(1 - (a * 20));
            } else {
                toggleBrightness(a * (flashCount - 5) - (a * 5));
            }
            
            /*if (flashCount < COUNT_UNTIL_BRIGHTEN) {
                toggleBrightness(0.9F);
                flashCount++;
            }
            else if (!isMaxBright()) {
                toggleBrightness(1.1F);
                flashCount++;
            }
            else {
                flashCount = 0;
            }*/
           
            
        }
        /*
        if (isFlashing) {
            
            if (!isMaxDark() && flashCount != 1) { //flashCount < COUNT_UNTIL_BRIGHTEN
                toggleBrightness(0.9F);
                flashCount = 0;
                //flashCount++;
            }
            else if (!isMaxBright()) {//flashCount < COUNT_UNTIL_DARKEN || isMaxBright()) {
                //System.out.println(flashCount + ": " + COUNT_UNTIL_DARKEN);
                toggleBrightness(1.1F);
                flashCount = 1;
                //flashCount++;
            }
            else {
                System.out.println(isMaxDark());
                flashCount = 0;
            }
           
            
        }*/
        
        
        
        if (isBrighteningToFull) {
            
            toggleBrightness(toFullBrightFactor);
            int firstOpaqueIndex = findFirstNonBlackIndex(sprite.getPixels());
            Color currentColor = Color.decode(String.valueOf(sprite.getPixels()[firstOpaqueIndex]));
            Color defaultColor = Color.decode(String.valueOf(defaultPixels[firstOpaqueIndex]));
            if (currentColor.getRed() + currentColor.getGreen() + currentColor.getBlue() >=
                    defaultColor.getRed() + defaultColor.getGreen() + defaultColor.getBlue()) {
                setSprite(new Sprite(defaultPixels, sprite.getWidth(), sprite.getHeight()));
                isBrighteningToFull = false;
            }
            
        }
        
        if (isTogglingBrightness && brightnessToggleCount < timesToToggleBrightness) {
            
            toggleBrightness(brightnessToggleFactor);
            brightnessToggleCount++;
            
        } else if (isTogglingBrightness && brightnessToggleCount == timesToToggleBrightness) {
            
            isTogglingBrightness = false;
            
        }
        
        
        
        if (isFadingIn && getTransparency() < 1) {
            
            //System.out.println(getTransparency() + (0.001 * 500));
            setTransparency(getTransparency() + (0.001 * fadeInSpeed));
            if (getTransparency() > 1.0) setTransparency(1.0);
            
        } else if (isFadingIn && getTransparency() >= 1) {
            
            isFadingIn = false;
            
        }
        
        if (isFadingOut && getTransparency() > 0) {
            
            //System.out.println("Instance of: " + (this instanceof PathedAnimatedSpritedObject));
            //System.out.println(name + getTransparency());
            setTransparency(getTransparency() - (0.001 * fadeOutSpeed));
            if (getTransparency() < 0.0) setTransparency(0.0);
            
        } else if (isFadingOut && getTransparency() == 0) {
            
            //System.out.println("hidden");
            isFadingOut = false;
            hide();
            setTransparency(1.0);
            
        }
    }
    
    /**
     * Compares Object to SpritedObject. Must have the same sprite at the same 
     * x and y positions to be equal. Must both be showing or not showing.
     * @param o Object to compare to.
     * @return  Boolean evaluating if the objects are the same.
     */
    public boolean equals( Object o ) {
        
        if (!(o instanceof SpritedObject)) return false;
        SpritedObject so = (SpritedObject) o;
        return so.getSprite().equals(this.getSprite())
                && so.getX() == this.getX()
                && so.getY() == this.getY()
                && so.isShowing() == this.isShowing();
        
    }
    
    /**
     * Describes SpritedObject. In the form...
     * <p> SpritedObject:sprite:x:y:showing
     * @return  String describing SpritedObject.
     */
    public String toString( ) {
        
        return getClass().getName() + ":" + getSprite().toString() + ":" +
                getX() + ":" + getY() + ":" + isShowing();
        
    }
    
    
}
