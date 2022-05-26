package game;

import game.gameObjects.Rectangle;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;

/**
 * RenderHandler
 * @author Kobe Goodwin
 * @version 3/13/2022
 * 
 * Handles the rendering of various types of data to the screen.
 */
public class RenderHandler 
{
    private static BufferedImage view = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_RGB);
    private static Rectangle camera = new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT);
    private static int[] pixels = ((DataBufferInt) view.getRaster().getDataBuffer()).getData();
    
    public static final int HP_BAR_HEIGHT = 21;
    public static final int ENEMY_HP_BAR_HEIGHT = 18;
    public static final int ENEMY_HP_BAR_DISTANCE_FROM_Y = 25;
    
    public static BufferedImage getImageFromArray(int[] pixels, int width, int height) {
        
        BufferedImage pixelImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixelImage.setRGB(0, 0, width, height, pixels, 0, width);
        return pixelImage;
        
    }
    
    /**
     * Renders the tiles and objects in view.
     * @param graphics  Graphics to draw to.
     */
    public static void render( Graphics graphics )
    {   
        graphics.drawImage(view, 0, 0, view.getWidth(), view.getHeight(), null);
        
    }
    
    /**
     * Renders a Text object to the screen.
     * @param graphics  Graphics to draw to.
     * @param text      Text object to draw.
     */
    public static void renderText( Graphics graphics, Text text ) {
        
        graphics.setFont(text.getFont());
        graphics.setColor(text.getColor());
        String[] lines = TextHandler.formatMessage(graphics, text).split("\n");
        boolean isYellowText = false;
        boolean isBlueText = false;
        for (int i = 0; i < lines.length; i++) {
            String[] yellowDivisions = lines[i].split(TextHandler.YELLOW_CODE);
            String[] blueDivisions = lines[i].split(TextHandler.BLUE_CODE);
            if (yellowDivisions.length > 1 || lines[i].endsWith(TextHandler.YELLOW_CODE))
                isYellowText = renderUniqueTextColor(graphics, text, yellowDivisions, TextHandler.YELLOW, isYellowText, lines[i], i);
            else if (blueDivisions.length > 1 || lines[i].endsWith(TextHandler.BLUE_CODE))
                isBlueText = renderUniqueTextColor(graphics, text, blueDivisions, TextHandler.BLUE, isBlueText, lines[i], i);
            else {
                if (isYellowText) {
                    graphics.setColor(TextHandler.YELLOW);
                } else if (isBlueText) {
                    graphics.setColor(TextHandler.BLUE);
                } else {
                    graphics.setColor(text.getColor());
                }
                graphics.drawString(lines[i], text.getX(), 
                    text.getY() + (i * graphics.getFontMetrics().getHeight()));
            }
        }
    }
    
    /**
     * Renders a BufferedImage to the screen.
     * @param image     BufferedImage to draw.
     * @param xPosition X Position
     * @param yPosition Y Position
     */
    public static void renderImage( BufferedImage image, int xPosition, int yPosition )
    {
        int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        renderArray(imagePixels, image.getWidth(), image.getHeight(), xPosition, yPosition);
    }
    
    /**
     * Renders a BufferedImage with transparency to the screen.
     * @param graphics      Graphics
     * @param image         BufferedImage to draw
     * @param xPosition     X Position
     * @param yPosition     Y Position
     * @param transparency  Transparency between 0.0 and 1.0
     */
    public static void renderImage( Graphics graphics, BufferedImage image, int xPosition, int yPosition, double transparency ) {
        
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) transparency));
        g2d.drawImage(image, xPosition, yPosition, 
                image.getWidth(), image.getHeight(), null);
        
    }
    
    /**
     * Renders an array of pixels to the screen.
     * @param renderPixels  Array of colors to be rendered
     * @param renderWidth   Width of array
     * @param renderHeight  Height of array
     * @param xPosition     X Position
     * @param yPosition     Y Position
     */
    public static void renderArray( int[] renderPixels, int renderWidth, int renderHeight, int xPosition, int yPosition )
    {
        for (int y = 0; y < renderHeight; y++)
            for (int x = 0; x < renderWidth; x++) {
                setPixel(renderPixels[x + y * renderWidth], x + xPosition, y + yPosition);
            }
                //for (int yZoomPosition = 0; yZoomPosition < yZoom; yZoomPosition++)
                //    for (int xZoomPosition = 0; xZoomPosition < xZoom; xZoomPosition++)

    }
    
    /**
     * Renders a Rectangle to the screen.
     * @param rectangle Rectangle to draw to the screen.
     */
    public static void renderRectangle( Rectangle rectangle )
    {
        int[] rectanglePixels = rectangle.getPixels();
        if (rectanglePixels != null)
            renderArray(rectanglePixels, rectangle.getWidth(), rectangle.getHeight(), 
                    rectangle.getX(), rectangle.getY());
    }
    
    /**
     * Renders a Rectangle with transparency.
     * @param graphics      Graphics
     * @param rectangle     Rectangle to draw
     * @param transparency  Transparency between 0.0 and 1.0
     */
    public static void renderRectangle( Graphics graphics, Rectangle rectangle, double transparency ) {
        
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) transparency));
        BufferedImage bi = RenderHandler.getImageFromArray(rectangle.getPixels(), 
                rectangle.getWidth(), rectangle.getHeight());
        g2d.drawImage(bi, rectangle.getX(), rectangle.getY(), 
                bi.getWidth(), bi.getHeight(), null);
        
    }
    
    /**
     * Renders a Sprite to the screen.
     * @param sprite        Sprite to draw to the screen.
     * @param xPosition     X Position
     * @param yPosition     Y Position
     */
    public static void renderSprite( Sprite sprite, int xPosition, int yPosition)
    {
        renderArray(sprite.getPixels(), sprite.getWidth(), sprite.getHeight(), xPosition, yPosition);
    }
    
    /**
     * Renders a Sprite to the screen.
     * @param graphics      Graphics to make transparent
     * @param sprite        Sprite to draw to the screen.
     * @param xPosition     X Position
     * @param yPosition     Y Position
     * @param transparency  Transparency from 0.0 to 1.0
     */
    public static void renderSprite( Graphics graphics, Sprite sprite, 
            int xPosition, int yPosition, double transparency )
    {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) transparency));
        //System.out.println(sprite.getPixels().length);
        BufferedImage bi = RenderHandler.getImageFromArray(sprite.getPixels(), 
                sprite.getWidth(), sprite.getHeight());
        g2d.drawImage(bi, xPosition, yPosition, 
                bi.getWidth(), bi.getHeight(), null);
    }
    
    /**
     * Changes the pixel position to the camera and view.
     * @param pixel     Pixel to interpret
     * @param x         X Position
     * @param y         Y Position
     */
    private static void setPixel( int pixel, int x, int y )
    {
        // Are x and y on the camera?
        //System.out.println(camera.x);
        //System.out.println(camera.y);
        if (x >= camera.getX() && y > camera.getY() && x <= camera.getX() + 
                camera.getWidth() && y <= camera.getY() + camera.getHeight())
        {
            int pixelIndex = (x - camera.getX()) + (y - camera.getY()) * view.getWidth();
            if (pixels.length > pixelIndex && pixel != Game.ALPHA)
                pixels[pixelIndex] = pixel;
        }
    }
    
    /**
     * Accessor for camera
     * @return  Rectangle representing the camera's view.
     */
    public static Rectangle getCamera( ) {
        
        return camera;
        
    }
    
    /**
     * Replaces text enclosed by a divider such as "/Y" with colored text.
     * @param graphics          Graphics
     * @param text              Original text
     * @param colorDivisions    Current line split by divider
     * @param color             Color to change to
     * @param isColoredText     True if text is already colored
     * @param currentLine       Current "\n" line (lines[i])
     * @param lineNum           i
     * @return 
     */
    private static boolean renderUniqueTextColor( Graphics graphics, Text text, String[] colorDivisions, Color color, boolean isColoredText, String currentLine, int lineNum ) {
        for (int j = 0; j < colorDivisions.length; j++) {
            
            if (isColoredText) graphics.setColor(color);
            else graphics.setColor(text.getColor());
            String messageBeforeSplit = "";
            int tempJ = 1;
            while (tempJ < j + 1) {
                messageBeforeSplit += colorDivisions[tempJ - 1];
                tempJ++;
            }
            if (j == 0) {
                graphics.drawString(colorDivisions[j], text.getX(), 
                text.getY() + (lineNum * graphics.getFontMetrics().getHeight()));
            } else {
                graphics.drawString(colorDivisions[j], text.getX() + graphics.getFontMetrics().stringWidth(messageBeforeSplit), 
                text.getY() + (lineNum * graphics.getFontMetrics().getHeight()));
            }
            String linesSub = currentLine.substring(currentLine.length() - colorDivisions[j].length(), currentLine.length());
            String yellowSub = colorDivisions[j].substring(colorDivisions[j].length() - colorDivisions[j].length(), colorDivisions[j].length());
            if (!linesSub.equals(yellowSub))
                isColoredText = !isColoredText;
        }
        return isColoredText;
    }
    
}
