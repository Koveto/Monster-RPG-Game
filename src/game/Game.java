// Tutorial: https://www.youtube.com/watch?v=lDzKX3djE-M

package game;

import game.gameObjects.SpritedObject;
import game.gameObjects.Player;
import game.gameObjects.Entity;
import game.gameObjects.GameObject;
import game.gameObjects.Rectangle;
import game.listeners.MouseEventListener;
import game.listeners.KeyboardListener;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.io.IOException;

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
    
    private final SpritedObject ftb;
    
    private Overworld overworld;
    private Battle battle;
    private Room room;
    
    private static Sound music1, music2, sound;
    
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
        
        music1 = new Sound();
        music2 = new Sound();
        sound = new Sound();
        
        SpriteSheet friskSheet = new SpriteSheet(Game.loadImage("ss//frisk.png"), 19, 29);
        Player player = new Player("CHARA", friskSheet.getSprites(), new Path("1", "1", 200, 200, 0, 0, 0.1, true), 200, 200, 100, 20, 1, false, true);
        
        ftb = new SpritedObject(new Sprite(Game.loadImage("sprites\\bgd1.png")), 0, 0);
        ftb.hide();
        
        battle = null;
        room = new Room(player, new DialogueBox(),
                        new SpriteSheet(Game.loadImage("ss\\ruinsTiles.png"), 20, 20),
                        "maps\\room0mapA.txt\\",
                        "maps\\room0mapB.txt\\",
                        "maps\\room0walls.txt\\",
                        "maps\\room0entities.txt\\",
                        "maps\\room0script.txt\\",
                        "text\\dialogue.txt\\");
        overworld = new Overworld(player, room);
        Game.getMusic1().play("Ruins", true);
        
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
     * Appends String to a String array.
     * @param array Array or Strings to append to
     * @param e     New String to append
     * @return  String array with e appended
     */
    public static String[] addToStringArray( String[] array, String e ) {
        
        String[] temp = new String[array.length + 1];
        for (int i = 0; i < array.length; i++) {
            temp[i] = array[i];
        }
        temp[array.length] = e;
        return temp;
        
    }
    
    /**
     * Appends Entity to a Entity array.
     * @param array Array or Strings to append to
     * @param e     New String to append
     * @return  String array with e appended
     */
    public static Entity[] addToEntityArray( Entity[] array, Entity e ) {
        
        Entity[] temp = new Entity[array.length + 1];
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
     * Appends Rectangle to Rectangle array
     * @param array     Rectangle array to append to
     * @param e         Rectangle to be appended
     * @return      Rectangle array with e appended
     */
    public static Rectangle[] addToRectangleArray( Rectangle[] array, Rectangle e ) {
        
        Rectangle[] temp = new Rectangle[array.length + 1];
        for (int i = 0; i < array.length; i++) {
            temp[i] = array[i];
        }
        temp[array.length] = e;
        return temp;
        
    }
    
    /**
     * Appends GameObject to GameObject array
     * @param array     GameObject array to append to
     * @param e         GameObject to be appended
     * @return      GameObject array with e appended
     */
    public static GameObject[] addToGOArray( GameObject[] array, GameObject e ) {
        
        GameObject[] temp = new GameObject[array.length + 1];
        for (int i = 0; i < array.length; i++) {
            temp[i] = array[i];
        }
        temp[array.length] = e;
        return temp;
        
    }
    
    /**
     * Appends Sprite to Sprite array
     * @param array     Sprite array to append to
     * @param s        Sprite to be appended
     * @return      Sprite array with e appended
     */
    public static Sprite[] addToSpriteArray( Sprite[] array, Sprite s ) {
        
        Sprite[] temp = new Sprite[array.length + 1];
        for (int i = 0; i < array.length; i++) {
            temp[i] = array[i];
        }
        temp[array.length] = s;
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
     * Calls the update method of each GameObject. Updates the
     * fade to black SpritedObject.
     */
    public void updateGameObjects( ) {

        GameObject[] objects = new GameObject[] {};
        if (mode == BATTLE) objects = battle.getObjects();
        if (mode == NO_BATTLE) objects = overworld.getObjects();
        for (GameObject object : objects) {
            object.update(this);
        }
        ftb.update(this);

    }

    /**
     * Determines which keys are being pressed, and calls the correct
     * interpretButtonPress method depending on whether a battle is
     * in progress or not.
     */
    public void interpretButtonPress( ) {

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

    }

    /**
     * Handles the game fading in and out between rooms and battles.
     */
    public void fadeInOut( ) {

        // Fade in
        if (overworld != null && overworld.transitioningRooms() && !ftb.isShowing()) {
            ftb.show();
            ftb.fadeIn(160);
        }
        if (battle != null) { 
            if (battle.update() && !ftb.isShowing()) {
                ftb.show();
                ftb.fadeIn(25);
            } 
        }        

        // Fade out
        if (ftb.isShowing() && !ftb.isFadingIn() && !ftb.isFadingOut() && overworld != null) {
            ftb.fadeOut(160);
            overworld.transitionRooms();        }
        if (ftb.isShowing() && !ftb.isFadingIn()
                && !ftb.isFadingOut() && overworld == null) {
            mode = NO_BATTLE;
            Game.getMusic1().resume();
            battle.getPlayer().getSpritedObject().show();
            battle.getPlayer().switchToOverworld();
            battle.getPlayer().setX(200);
            battle.getPlayer().setY(200);
            overworld = new Overworld(battle.getPlayer(), room);
            battle = null;
            ftb.fadeOut(25);
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
        updateGameObjects();
        
        // Is dialogue being triggered?
        if (mode == NO_BATTLE) {
            overworld.checkDialogueTrigger();
        }
        
        // Is a battle activating?
        if (overworld != null && overworld.activatingBattle()) {
            mode = BATTLE;
            battle = new Battle(overworld.getPlayer(), Encounter.getWhimsun());
            overworld = null;
        }
        
        interpretButtonPress();
        fadeInOut();

    }

    /**
     * Given a Graphics object, renders Game Objects to screen.
     * @param graphics  Graphics object
     */
    public void renderGameObjects( Graphics graphics ) {

        if (mode == BATTLE) {
            for (GameObject o : battle.getOpaqueObjects()) {
                o.render();
            }
        }
        if (mode == NO_BATTLE) {
            for (GameObject o : overworld.getObjects()) {
                o.render();
            }
        }

    }

    /**
     * Given a Graphics object, renders Text to the screen.
     */
    public void renderText( Graphics graphics ) {

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

    }

    /**
     * Renders semi-transparent objects to the screen
     * @param graphics  Graphics object
     */
    public void renderTransparency( Graphics graphics ) {

        if (mode == BATTLE) {
            for (GameObject o : battle.getTransparentObjects()) {
                o.render(graphics);
            }
        }
        ftb.render(graphics); // fade-to-black

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
        
        graphics.setColor(Color.black); // Fill screen black
        graphics.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        
        renderGameObjects(graphics); // Player, npcs, etc
        RenderHandler.render(graphics); // Tiles in view
        renderText(graphics); // Text
        renderTransparency(graphics); // Semi-transparent
        
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
     * Accessor for game music controller.
     * @return  Sound controlling game music.
     */
    public static Sound getMusic1() {
        return music1;}
    
    /**
     * Accessor for game music controller.
     * @return  Sound controlling game music.
     */
    public static Sound getMusic2() {
        return music2;}
    
    /**
     * Accessor for game sound controller.
     * @return  Sound controlling game sound.
     */
    public static Sound getSound() {return sound;}

    
    /**
     * Implemented Runnable method. Updates game time and calls update() then
     * render().
     */
    @Override
    public void run( )
    {
        BufferStrategy bufferStrategy;
        
        long lastTime = System.nanoTime();
        final double NANO_SECOND_CONVERSION = 1000000000 / 30.0; // FPS
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