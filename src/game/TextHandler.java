package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * TextHandler
 * @author Kobe Goodwin
 * @version 6/26/2023
 * 
 * Handles the formatting, wrapping, and parsing of Texts.
 */
public class TextHandler {
    
    public static Font  DEFAULT_FONT = new Font("Default", Font.PLAIN, 30),
                        DIALOGUE_FONT,
                        DOTUMCHE_PIXEL,
                        DIALOGUE_FONT_SMALL,
                        DETERMINATION_MONO,
                        MARS,
                        MARS_SMALL;
    
    public static Color WHITE = Color.decode("#FFFFFF"),
                        BLACK = Color.decode("000000"),
                        YELLOW = Color.decode("#ffff00"),
                        ORANGE = Color.decode("#fca600"),
                        BLUE = Color.decode("#0000ff"),
                        LIGHT_BLUE = Color.decode("#42fcff"),
                        DARK_BLUE = Color.decode("#003cff"),
                        GREEN = Color.decode("#00ff00"),
                        DARK_GREEN = Color.decode("#00c000"),
                        RED = Color.decode("#ff0000"),
                        DARK_RED = Color.decode("#c00000"),
                        PURPLE = Color.decode("#d535d9"),
                        GRAY = Color.decode("#404040");
    
    public static String    YELLOW_CODE = "/Y",
                            BLUE_CODE = "/B",
                            RED_CODE = "/R",
                            ORANGE_CODE = "/O",
                            GREEN_CODE = "/G",
                            PURPLE_CODE = "/P",
                            LIGHT_BLUE_CODE = "/A",
                            DARK_BLUE_CODE = "/C",
                            NEWLINE = "/N";
    
    public static final int DEFAULT_WRAP = 500,
                            INDENT_WRAP = 400,
                            SHORT_WRAP = 200,
                            BUBBLE_WRAP = 100;
    
    public static final int DEFAULT_SCROLL_SPEED = 2,
                            FAST_SCROLL_SPEED = 1,
                            SKIP_SCROLL_SPEED = 0,
                            SLOW_SCROLL_SPEED = 5,
                            SLOWEST_SCROLL_SPEED = 1;
    
    
    /**
     * Lists the colors which have a String code to automatically color Texts.
     * @return  Array of coded colors
     */
    public static Color[] listCodedColors( ) {
        
        return new Color[] {YELLOW, BLUE, RED, ORANGE, GREEN, PURPLE,
            LIGHT_BLUE, DARK_BLUE}; 
        
    }
    
    /**
     * Lists the String codes used to color Texts, such as "/Y" for yellow.
     * @return  Array of color codes
     */
    public static String[] listColorCodes( ) {
        
        return new String[] {YELLOW_CODE, BLUE_CODE, RED_CODE, ORANGE_CODE,
            GREEN_CODE, PURPLE_CODE, LIGHT_BLUE_CODE, DARK_BLUE_CODE};
        
    }
    
    /**
     * Attempts to load fonts from a file. If one or more cannot be loaded, all
     * fonts are set to the default and an error message is given.
     */
    public static void loadFonts( ) {
        
        try {
            DIALOGUE_FONT = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\listeners\\8bitoperator_JVE\\8bitoperator_jve.ttf")).deriveFont(33f);
            DIALOGUE_FONT_SMALL = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\listeners\\8bitoperator_JVE\\8bitoperator_jve.ttf")).deriveFont(24f);
            DETERMINATION_MONO = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\listeners\\determinationmonoweb-webfont.ttf")).deriveFont(30f);
            MARS = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\listeners\\Mars_Needs_Cunnilingus\\Mars_Needs_Cunnilingus.ttf")).deriveFont(26f);
            MARS_SMALL = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\listeners\\Mars_Needs_Cunnilingus\\Mars_Needs_Cunnilingus.ttf")).deriveFont(19f);
            DOTUMCHE_PIXEL = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\listeners\\dotumche-pixel\\dotumche-pixel.ttf")).deriveFont(13f);
        } catch (Exception e) {
            System.out.println("One or more fonts could not be loaded.");
            e.printStackTrace();
            DIALOGUE_FONT = DEFAULT_FONT;
            DIALOGUE_FONT_SMALL = DEFAULT_FONT;
            DETERMINATION_MONO = DEFAULT_FONT;
            MARS = DEFAULT_FONT;
            MARS_SMALL = DEFAULT_FONT;
        }
        
    }
    
    /**
     * Retrieves all Text objects from a file. 
     * @param path  Path of file to interpret.
     * @return      Array of Texts interpreted from file.
     * @throws FileNotFoundException    if file cannot be opened.
     */
    public static Text[] parseText( String path ) throws FileNotFoundException {
        
        ArrayList<Text> text = new ArrayList<>();
        Scanner scan = new Scanner(new File(path));
        while (scan.hasNextLine()) {
            text.add(new Text(scan.nextLine(), 90, 298, true, Color.white, DIALOGUE_FONT, TextHandler.DEFAULT_WRAP, true, 0));
        }
        Text[] arr = new Text[text.size()];
        arr = text.toArray(arr);
        
        return arr;
        
    }
    
    /**
     * Formats a Text's message by its scroll and wrap size.
     * @param graphics  Graphics
     * @param text      Text to format
     * @return          Formatted message
     */
    public static String formatMessage( Graphics graphics, Text text ) {
        
        String message = text.applyScroll(wrapMessage(graphics, text.getMessage(), text.getWrapSize()));
        return message;
    }
    
    /**
     * Wraps a String by the wrap size and pixel width of the Graphics Font.
     * @param graphics  Graphics
     * @param message   Message to format
     * @param wrapSize  Maximum Pixel Width
     * @return          Message with newlines added appropriately.
     */
    public static String wrapMessage( Graphics graphics, String message, int wrapSize ) {
        
        Scanner scan = new Scanner(message);
        String toReturn = "";
        String currentLine = "";
        int currentIndex = 0;
        int afterIndex = 0;
        while (scan.hasNext()) {
            String token = scan.next();
            
            currentIndex = message.indexOf(token, currentIndex) + token.length();
            String restOfMessage = message.substring(currentIndex, message.length());
            Scanner tempScan = new Scanner(restOfMessage);
            String nextToken = "";
            if (tempScan.hasNext())
                nextToken = tempScan.next();
            if (nextToken.equals("")) {
                afterIndex = message.length();
            } else {
                afterIndex = message.indexOf(nextToken, currentIndex);
            }
            String between = message.substring(currentIndex, afterIndex);
            if (graphics.getFontMetrics().stringWidth(currentLine) + 
                    graphics.getFontMetrics().stringWidth(token) > wrapSize) {
                toReturn += "\n" + token + between;
                currentLine = token + between;
            } else if (token.equals(NEWLINE)) {
                toReturn += "\n";
                currentLine = "";
            } else {
                toReturn += token + between;
                currentLine += token + between;
            }
        }
        return toReturn;
        
    }
    
    /**
     * Multiplies a character in a String [factor] number of times.
     * @param message       String to change
     * @param toMultiply    Character to multiply
     * @param factor        Times character will be multiplied
     * @return              String with character multiplied
     */
    public static String multiplyCharacter( String message, char toMultiply, int factor ) {
        
        String toReturn = "";
        for (int i = 0; i < message.length(); i++) {
            toReturn += message.charAt(i);
            if (message.charAt(i) == toMultiply) {
                for (int j = 0; j < factor - 1; j++)
                    toReturn += message.charAt(i);
            }
        }
        return toReturn;
        
    }
    
}
