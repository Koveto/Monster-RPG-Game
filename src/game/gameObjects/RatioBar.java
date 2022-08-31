package game.gameObjects;

import game.Game;
import game.Sprite;
import game.TextHandler;
import java.awt.Color;

/**
 * RatioBar
 * @author Kobe Goodwin
 * @version 8/31/2022
 * 
 * A rectangular health or mana bar to be rendered to the screen.
 */
public class RatioBar extends SpritedObject {
    
    private int numerator, denominator, slidingRatioWidthMax, 
            slidingRatioWidthDecrement, slidingRatioWidthTarget, 
            decreaseRatioWidthBy;
    
    private final int width, height;
    
    private Color numeratorColor, denominatorColor;
    
    private boolean isSliding;
    
    /**
     * Constructor
     * @param numerator             The numerator of a fraction
     * @param denominator           The denominator of a fraction
     * @param numeratorColor        The color to fill the area represented by the numerator
     * @param denominatorColor      The color to fill the area represented by the denominator
     * @param xPosition             X Position
     * @param yPosition             Y Position
     * @param width                 Width
     * @param height                Height
     */
    public RatioBar( int numerator, int denominator, Color numeratorColor, 
            Color denominatorColor, int xPosition, int yPosition, int width, int height ) {
        
        super(new Sprite(new Rectangle(xPosition, yPosition, width, height, TextHandler.GREEN.getRGB()).getPixels(), width, height), xPosition, yPosition);
        this.numerator = numerator;
        this.denominator = denominator;
        this.width = width;
        this.height = height;
        this.numeratorColor = numeratorColor;
        this.denominatorColor = denominatorColor;
        updateSprite();
        
    }
    
    /**
     * Determines if Ratio Bar is sliding between numerators
     * @return  true if sliding, false if not
     */
    public boolean isSliding( ) { return isSliding; }
    
    /**
     * Slides the bar from the current numerator to the new numerator
     * @param numerator     new numerator
     */
    public void slideToNumerator( int numerator ) {
        
        slidingRatioWidthMax = getRatioWidth();
        double ratio = (double) numerator / denominator;
        slidingRatioWidthTarget = (int) ((double) width * ratio);
        decreaseRatioWidthBy = 1;
        if (slidingRatioWidthMax - slidingRatioWidthTarget > 50)
            decreaseRatioWidthBy = 2;
        isSliding = true;
        this.numerator = numerator;
        slidingRatioWidthDecrement = 0;
        
    }
    
    /**
     * Creates a new Sprite from the numerator and denominator values.
     */
    private void updateSprite( ) {
        
        createNewSprite(getRatioWidth());
        
    }
    
    /**
     * Creates a new Sprite depending on how far the bar has slid.
     */
    private void updateSpriteToSlide( ) {
        
        if (slidingRatioWidthMax - slidingRatioWidthDecrement <= slidingRatioWidthTarget) {
            isSliding = false;
            updateSprite();
            return;
        }
        int newRatioWidth = slidingRatioWidthMax - slidingRatioWidthDecrement;
        createNewSprite(newRatioWidth);
        slidingRatioWidthDecrement += decreaseRatioWidthBy;
        
    }
    
    /**
     * Finds width of ratio (numerator / denominator) being rendered.
     * @return  width of ratio
     */
    private int getRatioWidth( ) {
        
        double ratio = (double) numerator / denominator;
        int ratioWidth = (int) ((double) width * ratio);
        return ratioWidth;
        
    }
    
    /**
     * Creates a new Sprite from a ratio width
     * @param newRatioWidth     width of ratio (numerator/denominator) to render
     */
    private void createNewSprite( int newRatioWidth ) {
        
        int[] pixels = new int[width * height];
        for (int i = 0; i < width; i++) {
            int color = numeratorColor.getRGB();
            if (i > newRatioWidth) {
                color = denominatorColor.getRGB();
            }
            for (int j = 0; j < height; j++) {
                pixels[i + (j * width)] = color;
            }
        }
        setSprite(new Sprite(pixels, width, height));
        setDefaultPixels(pixels);
        
    }
    
    /**
     * Accessor for numerator
     * @return  The numerator of a fraction
     */
    public int getNumerator( ) {return numerator;}
    
    /**
     * Accessor for denominator
     * @return  The denominator of a fraction
     */
    public int getDenominator( ) {return denominator;}
    
    /**
     * Accessor for width
     * @return  The width of the bar
     */
    public int getWidth( ) {return width;}
    
    /**
     * Accessor for height
     * @return  The height of the bar
     */
    public int getHeight( ) {return height;}
    
    /**
     * Accessor for numerator color
     * @return  The color filled by the numerator
     */
    public Color getNumeratorColor( ) {return numeratorColor;}
    
    /**
     * Accessor for denominator color
     * @return  The color filled by the denominator
     */
    public Color getDenominatorColor( ) {return denominatorColor;}
    
    /**
     * Mutator for numerator
     * @param numerator     New numerator
     */
    public void setNumerator( int numerator ) {
        this.numerator = numerator;
        updateSprite();
    }
    
    /**
     * Mutator for denominator
     * @param denominator   New denominator
     */
    public void setDenominator( int denominator ) {
        this.denominator = denominator;
        updateSprite();
    }
    
    /**
     * Mutator for numerator color
     * @param numeratorColor    New color filled by numerator
     */
    public void setNumeratorColor( Color numeratorColor ) {
        this.numeratorColor = numeratorColor;
        updateSprite();
    }
    
    /**
     * Mutator for denominator color
     * @param denominatorColor  New color filled by denominator
     */
    public void setDenominatorColor( Color denominatorColor ) {
        this.denominatorColor = denominatorColor;
        updateSprite();
    }
    
    /**
     * Calls superclass update method and slides sprite if it is sliding.
     * @param game 
     */
    @Override
    public void update( Game game ) {
        super.update(game);
        if (isSliding) updateSpriteToSlide();
    }
    
}
