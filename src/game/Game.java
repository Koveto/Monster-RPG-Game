// Tutorial: https://www.youtube.com/watch?v=lDzKX3djE-M

package game;

import game.gameObjects.SpritedObject;
import game.gameObjects.Player;
import game.gameObjects.DoublySpritedObject;
import game.gameObjects.GameObject;
import game.listeners.MouseEventListener;
import game.listeners.KeyboardListener;
import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Game extends JFrame implements Runnable {
    
    public final static int ALPHA = 0xFF1b241e,
                            WIDTH = 656,
                            HEIGHT = 519,
                            NO_KEYS = 0,
                            UP = 1,
                            DOWN = 2,
                            LEFT = 3,
                            RIGHT = 4,
                            CONFIRM = 5,
                            CANCEL = 6;
    
    private final Canvas canvas = new Canvas();
    
    private final BufferedImage iconImage;
    
    private final SpriteSheet soulSheet;
    
    private Battle battle;
    
    public static int scrollSpeed;
    
    private KeyboardListener keyListener = new KeyboardListener();
    private MouseEventListener mouseListener = new MouseEventListener(this);
    
    public Game( )
    {
        // Shuts down program when window is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("My Game");
        
        // Set size and position of frame
        setBounds(0, 0, WIDTH, HEIGHT);
        
        // Puts frame in center of screen
        setLocationRelativeTo(null);
        
        add(canvas);
        //add(new Surface());
                
        // Makes frame visible
        setVisible(true);
        
        TextHandler.loadFonts();
        scrollSpeed = TextHandler.DEFAULT_SCROLL_SPEED;
        
        // Create our object for our buffer strategy
        canvas.createBufferStrategy(3);
        
        soulSheet = new SpriteSheet(loadImage("ss\\soul.png"), 18, 18);
        Player player = new Player("CHARA", soulSheet.getSprite(0, 0), 39, 444, 20, 1);
        
        battle = new Battle(player, Encounter.getWhimsun());
        
        iconImage = loadImage("ss\\Icon.png");
        
        canvas.addKeyListener(keyListener);
        canvas.addFocusListener(keyListener);
        canvas.addMouseListener(mouseListener);
        canvas.addMouseMotionListener(mouseListener);
        canvas.requestFocus();
        
        setIconImage(iconImage);
        
    }
    
    public static int[] addToIntArray( int[] array, int e ) {
        
        int[] temp = new int[array.length + 1];
        for (int i = 0; i < array.length; i++) {
            temp[i] = array[i];
        }
        temp[array.length] = e;
        return temp;
        
    }
    
    public static Text[] addToTextArray( Text[] array, Text e ) {
        
        Text[] temp = new Text[array.length + 1];
        for (int i = 0; i < array.length; i++) {
            temp[i] = array[i];
        }
        temp[array.length] = e;
        return temp;
        
    }
    
    /**
    * Reads image from the specified path. Image is formatted and returned 
    * as a new object of the BufferedImage class.
    */
    public static BufferedImage loadImage( String path )
    {
        try {
            BufferedImage loadedImage = ImageIO.read(Game.class.getResource(path));
            BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(), 
                    loadedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);

            return formattedImage;
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
            return null;
        }
    }
    
    public void update( )
    {
        GameObject[] objects = battle.getObjects();
        for (GameObject object : objects) {
            object.update(this);
        }
        
        if (keyListener.noKeys()) {
            battle.interpretButtonPress(Game.NO_KEYS);
        }
        if (keyListener.up()) {
            battle.interpretButtonPress(Game.UP);
        }
        else if (keyListener.down()) {
            battle.interpretButtonPress(Game.DOWN);
        }
        else if (keyListener.left()) {
            battle.interpretButtonPress(Game.LEFT);
        }
        else if (keyListener.right()) {
            battle.interpretButtonPress(Game.RIGHT);
        }
        else if (keyListener.z()) {
            battle.interpretButtonPress(Game.CONFIRM);
        }
        else if (keyListener.x()) {
            battle.interpretButtonPress(Game.CANCEL);
        }
        
        if (battle.isAttackAnimationPlaying() ) {
            battle.checkIfAttackAnimationIsFinished();
        }
        
    }

    public void render( )
    {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();
        
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        
        for (GameObject o : battle.getOpaqueObjects()) {
            o.render();
        }
        
        RenderHandler.render(graphics);
        
        for (Text t : battle.getText()) {
            RenderHandler.renderText(graphics, t);
        }
        
        for (GameObject o : battle.getTransparentObjects()) {
            o.render(graphics);
        }
        
        graphics.dispose();
        bufferStrategy.show(); // Switches buffers
    }
    
    public KeyboardListener getKeyListener() {return keyListener;}
    public MouseEventListener getMouseEventListener() {return mouseListener;}

    
    /*
    Implemented Runnable Method
    */
    @Override
    public void run( )
    {
        BufferStrategy bufferStrategy;
        
        long lastTime = System.nanoTime();
        final double NANO_SECOND_CONVERSION = 1000000000 / 60.0; // FPS
        double deltaSeconds = 0.0;
        
        while (true) 
        {
            long now = System.nanoTime();
            deltaSeconds += (now - lastTime) / NANO_SECOND_CONVERSION;
            
            while (deltaSeconds >= 1) 
            {
                
                deltaSeconds = 0.0;
                update();
                
            }
            
            render();
            
            lastTime = now;
        }
    }
    
    public static void main( String[] args ) 
    {
        Game game = new Game();
        Thread gameThread = new Thread(game);
        gameThread.start();
    }
}