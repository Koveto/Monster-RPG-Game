package game.gameObjects;

import game.Game;
import game.MovingPath;
import game.RenderHandler;
import game.Sprite;
import game.listeners.KeyboardListener;

/**
 * Player
 * @author Kobe Goodwin
 * @version 8/31/2022
 * 
 * A Battler with that represents the player character. It responds to keyboard
 * inputs. The camera follows its rectangle collision box. Makes selections on 
 * the battle screen.
 */
public class Player extends Battler {
    
    private Rectangle rect;
    private String name;
    private int lastDirection, textCooldown;
    private long invincTime;
    private boolean invulnerable;
    
    /**
     * Constructor
     * @param name      Name
     * @param sprite    Sprite to display
     * @param xPosition X Position
     * @param yPosition Y Position
     * @param maxHP     Maximum HP
     * @param level     Level
     */
    public Player( String name, Sprite[] sprites, MovingPath path, 
            int xPosition, int yPosition, 
            int timePerSwitchMillis,
            int maxHP, int level,
            boolean hideWhenFinished, boolean loopAnimation ) {
        
        super(sprites, path, xPosition, yPosition, timePerSwitchMillis, maxHP, level, hideWhenFinished, loopAnimation);
        this.name = name;
        this.lastDirection = 0;
        this.textCooldown = 0;
        rect = new Rectangle(32, 16, 16, 32);
        invulnerable = false;
        
    }
    
    /**
     * Accessor for name
     * @return  Name of player
     */
    public String getName( ) {return name;}
    
    /**
     * Accessor for rectangle
     * @return  Rectangle bounding box for the player
     */
    public Rectangle getRect( ) {return rect;}
    
    /**
     * Accessor for last direction
     * @return  Integer representing last direction where...
     * <p> 0 = None, 1 = Up, 2 = Down, 3 = Left, 4 = Right
     */
    public int getLastDirection( ) {return lastDirection;}
    
    public boolean isVulnerable( ) {return !invulnerable;}
    
    /**
     * Mutator for name
     * @param name  New name
     */
    public void setName( String name ) {this.name = name;}
    
    /**
     * Mutator for rectangle
     * @param rect  New bounding rectangle
     */
    public void setRect( Rectangle rect ) {this.rect = rect;}
    
    public void setX( int x ) {
        super.setX(x);
        rect.setX(x);
    }
    
    public void setY( int y ) {
        super.setY(y);
        rect.setY(y);
    }
    
    /**
     * Mutator for last direction
     * @param lastDirection     New direction
     */
    public void setLastDirection( int lastDirection ) {this.lastDirection = lastDirection;}
    
    /**
     * Update camera to player's rectangle.
     * @param camera    Game camera to follow player.
     */
    public void updateCamera(Rectangle camera) {
        camera.setX(rect.getX() - (camera.getWidth() / 2));
        camera.setY(rect.getY() - (camera.getHeight() / 2));
    }
    
    /**
     * Compares Object to Player. Must have the same sprite at the same 
     * x and y positions to be equal. Must both be showing or not showing.
     * Must have the same HP, maxHP, and level. Must have the same name and
     * bounding rectangle.
     * @param o Object to compare to.
     * @return  Boolean evaluating if the objects are the same.
     */
    public boolean equals( Object o ) {
        
        if (!(o instanceof Player)) return false;
        Player p = (Player) o;
        return ((Battler) p).equals((Battler) this)
                && p.getName().equals(this.name)
                && p.getRect().equals(this.getRect());
        
    }
    
    /**
     * Describes Player. In the form...
     * <p> super:Player:name:rectangle:lastDirection:textCooldown:speed
     * @return  String describing Player.
     */
    public String toString( ) {
        
        return super.toString() + ":" + getClass().getName() + ":" + this.name
                + ":" + this.getRect() + ":" + this.lastDirection + ":" + 
                this.textCooldown;
        
    }
    
    @Override
    public boolean takeDamage( int damage ) {
        
        boolean alive = super.takeDamage(damage);
        if (alive) {
            invulnerable = true;
            invincTime = System.currentTimeMillis();
            getSpritedObject().animate();
        }
        return alive;
        
    }
    
    @Override
    public void update(Game game) {
        super.update(game);
        if (invulnerable) {
            if (System.currentTimeMillis() - invincTime >= 600) {
                invulnerable = false;
                getSpritedObject().setSprite(getSpritedObject().getSprites()[0]);
                getSpritedObject().pause();
            }
        }
    }
    
}
