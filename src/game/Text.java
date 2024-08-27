package game;

import java.awt.Color;
import java.awt.Font;

/**
 * Text
 * @author Kobe Goodwin
 * @version 8/27/2024
 * 
 * A message to be drawn to the screen. 
 */
public class Text {

    private String message, sound;
    private boolean scrollingText, doubleSpaces;
    private int x, y, scrollIndex, wrap, newLineSpace;
    private long timeLastScroll, timeLastSound;
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
     * @param sound             Sound for text scroll
     */
    public Text( String message, int x, int y, boolean scrollingText,
            Color color, Font font, String sound ) {
        
        this.message = message;
        this.x = x;
        this.y = y;
        this.scrollingText = scrollingText;
        scrollIndex = 0;
        this.color = color;
        this.font = font;
        wrap = TextHandler.DEFAULT_WRAP;
        this.newLineSpace = 0;
        this.sound = sound;
        
    }
    
    /**
     * Constructor
     * @param message           Message to display
     * @param x                 X Position
     * @param y                 Y Position
     * @param scrollingText     True if text should scroll, false if not
     * @param color             Color of message
     * @param font              Message font
     * @param sound             Sound for text scroll
     * @param wrapSize          Width of wrap
     * @param doubleSpaces      True to double the spaces
     * @param newLineSpace      Distance to add between new lines
     */
    public Text( String message, int x, int y, boolean scrollingText, 
            Color color, Font font, String sound,
            int wrapSize, boolean doubleSpaces,
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
        this.sound = sound;
        
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
     * Mutator for dialogue sound
     * @param sound See a_music for identifiers
     */
    public void setSound( String sound ) {

        this.sound = sound;

    }
    
    /**
     * Shortens the message at its current scroll position.
     * @return  String containing message at its current scroll position.
     */
    public String applyScroll( String message ) {
        
        // Example message:
        // "*   To   make   progress   here,
        //        you   will   need   to   trigger
        //        several   switches."

        // Trivial case, return full message
        if (scrollIndex == message.length() || !scrollingText) return message;

        // If enough time has passed...
        if (System.nanoTime() - timeLastScroll > ((long) Game.scrollSpeed) * 10000000) {
            
            // Increase the scrollIndex until it's not a space
            scrollIndex++; 
            while (scrollIndex > 0 && message.charAt(scrollIndex - 1) == ' ' && scrollIndex < message.length())
                scrollIndex++;

            // Play dialogue sound effect
            if (message.charAt(scrollIndex - 1) != ' ' && message.charAt(scrollIndex - 1) != '*'
                    && System.currentTimeMillis() - timeLastSound > DialogueHandler.DELAY_DIALOGUESOUND) {
                if (sound != null) {
                    Game.getSound().play(sound, false);
                }
                timeLastSound = System.currentTimeMillis();
            }
            timeLastScroll = System.nanoTime();
        }

        // Don't separate color codes ("* To make /Y progress here," highlghts "progress" yellow)
        if (message.substring(0, scrollIndex).endsWith("/") && message.charAt(scrollIndex) == 'Y'
                || message.substring(0, scrollIndex).endsWith("/") && message.charAt(scrollIndex) == 'B') {
            return message.substring(0, scrollIndex - 1);
        } 

        // Example output 1
        // *   To
        // Example output 2
        // *   To   m
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

    /**
     * Compares Object to Text. Returns True if they are both Texts
     * with the same Message. Their other fields can be differ.
     * @param o Object to compare to.
     * @return  Boolean evaluating if the message is the same.
     */
    public boolean equals( Object o ) {
        
        if (!(o instanceof Text)) return false;
        Text t = (Text) o;
        return t.getMessage().equals(this.getMessage());
    }

    /**
     * Describes Text.
     * @return  String describing Text.
     */
    public String toString( ) {
        
        return "Text object {Message=\n[" + message + "]\nSound=[" + sound + "]\nScrolling Text? " + 
        String.valueOf(scrollingText) + "\nDouble Spaces? " + String.valueOf(doubleSpaces) + 
        "\nX, Y: " + String.valueOf(x) + ", " + String.valueOf(y) + "\nScrollIndex: " +
        String.valueOf(scrollIndex) + "\nWrap: " + String.valueOf(wrap) + "\nnewLineSpace: " +
        String.valueOf(newLineSpace) + "\ntimeLastScroll: " + String.valueOf(timeLastScroll) +
        "\ntimeLastSound: " + String.valueOf(timeLastSound) + "\nColor: " + String.valueOf(color) +
        "\nFont: " + String.valueOf(font) + "}";
        
    }
    
}
