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
                            CANCEL = 6,
                            BATTLE = 0,
                            NO_BATTLE = 1;
    
    private final Canvas canvas = new Canvas();
    
    private final BufferedImage iconImage;
    
    private final SpriteSheet soulSheet;
    private final SpritedObject ftb;
    
    private Battle battle;
    private Room room;
    
    public static int scrollSpeed, mode;
    
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
        mode = NO_BATTLE;
        
        // Create our object for our buffer strategy
        canvas.createBufferStrategy(3);
        
        soulSheet = new SpriteSheet(loadImage("ss\\soul.png"), 18, 18);
        Player player = new Player("CHARA", soulSheet.getSprites(), new Path("1", "1", 39, 444, 0, 0, 0.1, true), 39, 444, 100, 20, 1, false, true);
        
        ftb = new SpritedObject(new Sprite(Game.loadImage("sprites\\bgd1.png")), 0, 0);
        ftb.hide();
        
        battle = new Battle(player, Encounter.getWhimsun());
        room = new Room(player,
                        new TileSet(new File("C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\maps\\tiles.txt\\"), 
                                        new SpriteSheet(Game.loadImage("ss\\ruins.png"), 20, 20)),
                        "C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\maps\\map.txt\\",
                        "C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\maps\\map1.txt\\");
        
        iconImage = loadImage("ss\\Icon.png");
        
        canvas.addKeyListener(keyListener);
        canvas.addFocusListener(keyListener);
        canvas.addMouseListener(mouseListener);
        canvas.addMouseMotionListener(mouseListener);
        canvas.requestFocus();
        
        setIconImage(iconImage);
        
    }
    
    public static void setScrollSpeed( int newScrollSpeed ) { scrollSpeed = newScrollSpeed; }
    
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
        GameObject[] objects = new GameObject[] {};
        if (mode == BATTLE) objects = battle.getObjects();
        if (mode == NO_BATTLE) objects = room.getObjects();
        for (GameObject object : objects) {
            object.update(this);
        }
        
        ftb.update(this);
        
        boolean direction = false;
        
        if (mode == BATTLE) {
        if (keyListener.noKeys()) {
            if (mode == BATTLE) battle.interpretButtonPress(Game.NO_KEYS);
        }
        if (keyListener.up()) {
            if (mode == BATTLE) battle.interpretButtonPress(Game.UP);
            direction = true;
        }
        if (keyListener.down() && (!direction  || battle.isEnemyTurn())) {
            if (mode == BATTLE) battle.interpretButtonPress(Game.DOWN);
            direction = true;
        }
        if (keyListener.left() && (!direction  || battle.isEnemyTurn())) {
            if (mode == BATTLE) battle.interpretButtonPress(Game.LEFT);
            direction = true;
        }
        if (keyListener.right() && (!direction  || battle.isEnemyTurn())) {
            if (mode == BATTLE) battle.interpretButtonPress(Game.RIGHT);
            direction = true;
        }
        if (keyListener.z()) {
            battle.interpretButtonPress(Game.CONFIRM);
        }
        if (keyListener.x()) {
            battle.interpretButtonPress(Game.CANCEL);
        }
        
        if (battle.isAttackAnimationPlaying() ) {
            battle.checkIfAttackAnimationIsFinished();
        } else if (battle.isEnemyTakingDamage()) {
            battle.checkIfDamageNumberFinished();
        } else if (battle.isBetweenTurns()) {
            battle.checkTimeBetweenTurns();
        } else if (battle.isEnemyTurn()) {
            battle.checkBulletCollision();
        } else if (battle.isTransitioningToPlayerTurn()) {
            battle.transitionToPlayerTurn();
        } else if (battle.isEnded() && !ftb.isShowing()) {
            ftb.show();
            ftb.fadeIn(25);
            //ftb.setTransparency(0.5);
            //ftb.show();
        }
        }
        if (ftb.isShowing() && !ftb.isFadingIn()
                && !ftb.isFadingOut()) {
            mode = NO_BATTLE;
            ftb.fadeOut(25);
        }
    }

    public void render( )
    {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();
        
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        
        if (mode == BATTLE){
        for (GameObject o : battle.getOpaqueObjects()) {
            o.render();
        }}
        if (mode == NO_BATTLE) {
            for (GameObject o : room.getObjects()) {
                o.render();
            }
        }
        
        RenderHandler.render(graphics);
        
        if (mode == BATTLE) {
        for (Text t : battle.getText()) {
            RenderHandler.renderText(graphics, t);
        }
        
        for (GameObject o : battle.getTransparentObjects()) {
            o.render(graphics);
        }}
        ftb.render(graphics);
        
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