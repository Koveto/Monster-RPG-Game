package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferStrategy;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

/**
 * Text
 * @author Kobe Goodwin
 * @version 6/23/2023
 * 
 * A message to be drawn to the screen. 
 */
public class Text {

    private String message;
    private boolean scrollingText, doubleSpaces;
    private int x, y, scrollIndex, wrap, newLineSpace;
    private long timeLastScroll;
    private Color color;
    private Font font;
    
    /**
     * Constructor
     * @param message           Message to display
     * @param x                 X Position
     * @param y                 Y Position
     * @param scrollingText     True if text should scroll, false if not
     * @param color             Color of message
     * @param font              Message font
     */
    public Text( String message, int x, int y, boolean scrollingText, Color color, Font font ) {
        
        this.message = message;
        this.x = x;
        this.y = y;
        this.scrollingText = scrollingText;
        scrollIndex = 0;
        this.color = color;
        this.font = font;
        wrap = TextHandler.DEFAULT_WRAP;
        this.newLineSpace = 0;
        
    }
    
    /**
     * Constructor
     * @param message           Message to display
     * @param x                 X Position
     * @param y                 Y Position
     * @param scrollingText     True if text should scroll, false if not
     * @param color             Color of message
     * @param font              Message font
     * @param wrapSize          Width of wrap
     * @param doubleSpaces      True to double the spaces
     */
    public Text( String message, int x, int y, boolean scrollingText, 
            Color color, Font font, int wrapSize, boolean doubleSpaces,
            int newLineSpace) {
        
        if (doubleSpaces) this.message = TextHandler.multiplyCharacter(message, ' ', 2);
        else this.message = message;
        this.x = x;
        this.y = y;
        this.scrollingText = scrollingText;
        scrollIndex = 0;
        this.color = color;
        this.font = font;
        this.wrap = wrapSize;
        this.doubleSpaces = doubleSpaces;
        this.newLineSpace = newLineSpace;
        
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
     * Accessor for Color
     * @return  Color
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Accessor for Font
     * @return  Font
     */
    public Font getFont() {
        return font;
    }
    
    /**
     * Accessor for Wrap Size
     * @return  Wrap size
     */
    public int getWrapSize( ) {
        return wrap;
    }
    
    /**
     * Accessor for New Line Space
     * @return  New Line Space
     */
    public int getNewLineSpace( ) {
        return newLineSpace;
    }
    
    /**
     * Accessor for Message
     * @return  Message
     */
    public String getMessage( ) { return message;}
    
    /**
     * Determines if the Text has finished scrolling.
     * @return  True if finished scrolling.
     */
    public boolean isFinishedScrolling( ) {
        
        return scrollIndex >= message.length();
        
    }
    
    /**
     * Retrieves the message at its current scroll position.
     * @return  String containing message at its current scroll position.
     */
    public String applyScroll( String message ) {
        
        if (scrollIndex == message.length() || !scrollingText) return message;
        if (System.nanoTime() - timeLastScroll > ((long) Game.scrollSpeed) * 10000000) {
            scrollIndex++;
            timeLastScroll = System.nanoTime();
        }
        if (message.substring(0, scrollIndex).endsWith("/") && message.charAt(scrollIndex) == 'Y'
                || message.substring(0, scrollIndex).endsWith("/") && message.charAt(scrollIndex) == 'B') {
            return message.substring(0, scrollIndex - 1);
        } 
        return message.substring(0, scrollIndex);
        
    }
    
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
     * Mutator for Color
     * @param color New color
     */
    public void setColor( Color color ) {this.color = color;}
    
    /**
     * Mutator for Font
     * @param font  New font
     */
    public void setFont( Font font ) {this.font = font;}
    
    /**
     * Mutator for Wrap Size
     * @param wrapSize  New wrap size
     */
    public void setWrap( int wrapSize ) {this.wrap = wrapSize;}
    
    /**
     * Mutator for NewLine Space
     * @param newLineSpace  NewLine Space
     */
    public void setNewLineSpace( int newLineSpace ) {this.newLineSpace = newLineSpace;}
    
    /**
     * Mutator for Text Scroll
     * @param scrollingText     Boolean determining whether text should scroll.
     */
    public void setScroll( boolean scrollingText ) {this.scrollingText = scrollingText;}
    
    /**
     * Changes the message being displayed and resets the scroll.
     * @param message   New message to display
     */
    public void newMessage( String message ) {
        
        if (this.doubleSpaces) this.message = TextHandler.multiplyCharacter(message, ' ', 3);
        else this.message = message;
        scrollIndex = 0;
        timeLastScroll = System.nanoTime();
        
    }
    
}
