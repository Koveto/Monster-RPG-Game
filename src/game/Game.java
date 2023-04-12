// Tutorial: https://www.youtube.com/watch?v=lDzKX3djE-M

package game;

import game.gameObjects.SpritedObject;
import game.gameObjects.Player;
import game.gameObjects.DoublySpritedObject;
import game.gameObjects.GameObject;
import game.gameObjects.Rectangle;
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
    
    private Overworld overworld;
    private Battle battle;
    private Room room;
    
    public static int scrollSpeed, mode;
    
    private static KeyboardListener keyListener = new KeyboardListener();
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
        SpriteSheet friskSheet = new SpriteSheet(Game.loadImage("ss//frisk.png"), 19, 29);
        Player player = new Player("CHARA", friskSheet.getSprites(), new Path("1", "1", 200, 200, 0, 0, 0.1, true), 200, 200, 100, 20, 1, false, true);
        
        ftb = new SpritedObject(new Sprite(Game.loadImage("sprites\\bgd1.png")), 0, 0);
        ftb.hide();
        
        battle = null;
        room = new Room(new TileSet(new File("C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\maps\\tiles.txt\\"), 
                                        new SpriteSheet(Game.loadImage("ss\\ruins.png"), 20, 20)),
                        "C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\maps\\map.txt\\",
                        "C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\maps\\map1.txt\\",
                        "C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\maps\\walls.txt\\");
        overworld = new Overworld(player, room);
        
        
        iconImage = loadImage("ss\\Icon.png");
        
        canvas.addKeyListener(keyListener);
        canvas.addFocusListener(keyListener);
        canvas.addMouseListener(mouseListener);
        canvas.addMouseMotionListener(mouseListener);
        canvas.requestFocus();
        
        setIconImage(iconImage);
        
    }
    
    /**
     * Mutator for scrollSpeed. Speeds documented in TextHandler.
     * @param newScrollSpeed    scrollSpeed to change to.
     */
    public static void setScrollSpeed( int newScrollSpeed ) { scrollSpeed = newScrollSpeed; }
    
    /**
     * Determines if a battle is initiated.
     * @return  True if battling, false if not.
     */
    public static boolean isBattle( ) { return mode == BATTLE; }
    
    /**
     * Appends  integer to an integer array.
     * @param array Array or integers to append to
     * @param e     New integer to append
     * @return  Integer array with e appended
     */
    public static int[] addToIntArray( int[] array, int e ) {
        
        int[] temp = new int[array.length + 1];
        for (int i = 0; i < array.length; i++) {
            temp[i] = array[i];
        }
        temp[array.length] = e;
        return temp;
        
    }
    
    /**
     * Appends Text to Text array
     * @param array     Text array to append to
     * @param e         Text to be appended
     * @return      Text array with e appended
     */
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
    
    /**
     * Operations performed each deltaSecond in run(). First calls the update()
     * method on each GameObject in the BATTLE or NO_BATTLE mode. Then the
     * update() method on Game objects such as the fade to black. Determines
     * if a battle is beginning and handles the mode change. Handles keyboard
     * inputs for each respective mode. Calls method in battle appropriate for
     * its state. Handles fade to black between modes.
     */
    public void update( )
    {
        GameObject[] objects = new GameObject[] {};
        if (mode == BATTLE) objects = battle.getObjects();
        if (mode == NO_BATTLE) objects = overworld.getObjects();
        for (GameObject object : objects) {
            object.update(this);
        }
        
        ftb.update(this);
        
        if (mode == NO_BATTLE) {
            overworld.checkDialogueTrigger();
        }
        
        if (overworld != null && overworld.activatingBattle()) {
            mode = BATTLE;
            battle = new Battle(overworld.getPlayer(), Encounter.getWhimsun());
            overworld = null;
        }
        
        boolean direction = false;
        
        if (keyListener.noKeys()) {
            if (mode == BATTLE) battle.interpretButtonPress(Game.NO_KEYS);
            else overworld.interpretButtonPress(Game.NO_KEYS);
        }
        if (keyListener.up()) {
            if (mode == BATTLE) battle.interpretButtonPress(Game.UP);
            else overworld.interpretButtonPress(Game.UP);
            direction = true;
        }
        if (keyListener.down()) {
            if (mode == BATTLE) {
                if (!direction || battle.isEnemyTurn()) battle.interpretButtonPress(Game.DOWN);
            }
            else overworld.interpretButtonPress(Game.DOWN);
            direction = true;
            /*battle = new Battle(overworld.getPlayer(), Encounter.getWhimsun());
            overworld.getPlayer().switchToSoul();
            overworld.getPlayer().setX(39);
            overworld.getPlayer().setY(444);
            mode = BATTLE;*/
        }
        if (keyListener.left()) {
            if (mode == BATTLE) {
                if (!direction || battle.isEnemyTurn()) battle.interpretButtonPress(Game.LEFT);
            }
            else overworld.interpretButtonPress(Game.LEFT);
            direction = true;
        }
        if (keyListener.right()) {
            if (mode == BATTLE) {
                if (!direction || battle.isEnemyTurn()) battle.interpretButtonPress(Game.RIGHT);
            }
            else overworld.interpretButtonPress(Game.RIGHT);
            direction = true;
        }
        if (keyListener.z()) {
            if (mode == BATTLE) battle.interpretButtonPress(Game.CONFIRM);
            else overworld.interpretButtonPress(Game.CONFIRM);
        }
        if (keyListener.x()) {
            Game.setScrollSpeed(TextHandler.SKIP_SCROLL_SPEED);
            if (mode == BATTLE) battle.interpretButtonPress(Game.CANCEL);
            else overworld.interpretButtonPress(Game.CANCEL);
        } else {
            Game.setScrollSpeed(TextHandler.DEFAULT_SCROLL_SPEED);
        }
        
        
        
        if (battle != null) { 
            if (battle.update() && !ftb.isShowing()) {
                ftb.show();
                ftb.fadeIn(25);
            } 
        }
        
        if (ftb.isShowing() && !ftb.isFadingIn()
                && !ftb.isFadingOut()) {
            mode = NO_BATTLE;
            battle.getPlayer().getSpritedObject().show();
            battle.getPlayer().switchToOverworld();
            //battle.getPlayer().getSpritedObject().setSprites(new SpriteSheet(Game.loadImage("ss//frisk.png"), 19, 29).getSprites());
            battle.getPlayer().setX(200);
            battle.getPlayer().setY(200);
            overworld = new Overworld(battle.getPlayer(), room);
            battle = null;
            ftb.fadeOut(25);
        }
    }

    /**
     * Operations performed every deltaSeccond in run() after update(). Creates
     * a BufferStrategy to handle lag. Fills the display with black then calls
     * the render() method in each mode's GameObjects. Renders objects visible
     * in RenderHandler's view. Renders Text objects in each mode. Calls render()
     * method on semitransparent objects. Switches graphics buffers.
     */
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
            for (GameObject o : overworld.getObjects()) {
                o.render();
            }
        }
        
        RenderHandler.render(graphics);
        
        if (mode == BATTLE) {
        for (Text t : battle.getText()) {
            RenderHandler.renderText(graphics, t);
        }
        }
        if (mode == NO_BATTLE) {
            for (Text t : overworld.getText()) {
                RenderHandler.renderText(graphics, t);
            }
        }
        
        if (mode == BATTLE) {
        for (GameObject o : battle.getTransparentObjects()) {
            o.render(graphics);
        }}
        ftb.render(graphics);
        
        graphics.dispose();
        bufferStrategy.show(); // Switches buffers
    }
    
    /**
     * Accessor for keyListener. Listens to keyboard inputs. 
     * @return  KeyboardListener used in Game.
     */
    public static KeyboardListener getKeyListener() {return keyListener;}
    
    /**
     * Accessor for mouseListener. Listens to mouse inputs.
     * @return  MouseEventListener used in Game.
     */
    public MouseEventListener getMouseEventListener() {return mouseListener;}

    
    /**
     * Implemented Runnable method. Updates game time and calls update() then
     * render().
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
    
    /**
     * Creates a new Game and Thread.
     * @param args 
     */
    public static void main( String[] args ) 
    {
        Game game = new Game();
        Thread gameThread = new Thread(game);
        gameThread.start();
    }
}