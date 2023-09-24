package game.gameObjects;

import game.Game;
import static game.Game.loadImage;
import game.MovingPath;
import game.RenderHandler;
import game.Sprite;
import game.SpriteSheet;
import game.listeners.KeyboardListener;

/**
 * Player
 * @author Kobe Goodwin
 * @version 8/16/2023
 * 
 * A Battler with that represents the player character. It responds to keyboard
 * inputs. The camera follows its rectangle collision box. Makes selections on 
 * the battle screen.
 */
public class Player extends Battler {
    
    private final int STEP_SWITCH1 = 10, STEP_SWITCH2 = 20, STEP_SWITCH3 = 30, STEP_SWITCH4 = 40;
    
    private Rectangle rect;
    private String name;
    private int lastDirection, textCooldown, facing, stepCount;
    private long invincTime;
    private boolean invulnerable, isCameraShake;
    
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
        rect = new Rectangle(xPosition, yPosition, 19 * 2, 29 * 2);
        invulnerable = false;
        facing = 1;
        stepCount = 0;
        
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
    
    public int facing( ) { return facing; }
    
    public boolean isVulnerable( ) {return !invulnerable;}
    
    public boolean isFacingUp( ) { return facing == Game.UP; }
    public boolean isFacingDown( ) { return facing == Game.DOWN; }
    public boolean isFacingLeft( ) { return facing == Game.LEFT; }
    public boolean isFacingRight( ) { return facing == Game.RIGHT; }

    public boolean isCameraShake( ) { return isCameraShake; }

    public void setIsCameraShake( boolean shake ) { isCameraShake = shake; }
     
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
    
    /**
     * Mutator for direction
     * @param direction     New direction to face
     */
    public void setDirection( int direction ) {this.facing = direction;}
    
    public void switchToSoul( ) {
        
        SpriteSheet soulSheet = new SpriteSheet(loadImage("ss\\soul.png"), 18, 18);
        getSpritedObject().setSprites(soulSheet.getSprites());
        getSpritedObject().setSprite(soulSheet.getSprites()[0]);
        rect.setWidth(18);
        rect.setHeight(18);
        
    }
    
    public void switchToOverworld( ) {
        
        SpriteSheet friskSheet = new SpriteSheet(Game.loadImage("ss//frisk.png"), 19, 29);
        getSpritedObject().setSprites(friskSheet.getSprites());
        getSpritedObject().setSprite(friskSheet.getSprites()[0]);
        rect.setWidth(38);
        rect.setHeight(58);
        
        
    }
    
    public void setX( int x ) {
        super.setX(x);
        rect.setX(x);
    }
    
    public void setY( int y ) {
        super.setY(y);
        rect.setY(y);
    }
    
    public void startStepping( ) {
        if (stepCount == 0) stepCount++;
    }
    
    public void stopStepping( ) {
        stepCount = 0;
        if (facing == Game.UP)
            getSpritedObject().setSprite(getSpritedObject().getSprites()[7]);
        else if (facing == Game.DOWN)
            getSpritedObject().setSprite(getSpritedObject().getSprites()[0]);
        else if (facing == Game.LEFT)
            getSpritedObject().setSprite(getSpritedObject().getSprites()[3]);
        else if (facing == Game.RIGHT)
            getSpritedObject().setSprite(getSpritedObject().getSprites()[5]);
    }
    
    private void step( ) {
        
        if (facing == Game.UP && (stepCount == STEP_SWITCH1 || stepCount == STEP_SWITCH3))
            getSpritedObject().setSprite(getSpritedObject().getSprites()[7]);
        if (facing == Game.UP && stepCount == STEP_SWITCH2)
            getSpritedObject().setSprite(getSpritedObject().getSprites()[9]);
        if (facing == Game.UP && stepCount == STEP_SWITCH4)
            getSpritedObject().setSprite(getSpritedObject().getSprites()[8]);
        if (facing == Game.DOWN && (stepCount == STEP_SWITCH1 || stepCount == STEP_SWITCH3))
            getSpritedObject().setSprite(getSpritedObject().getSprites()[0]);
        if (facing == Game.DOWN && stepCount == STEP_SWITCH2)
            getSpritedObject().setSprite(getSpritedObject().getSprites()[2]);
        if (facing == Game.DOWN && stepCount == STEP_SWITCH4)
            getSpritedObject().setSprite(getSpritedObject().getSprites()[1]);
        if (facing == Game.LEFT && (stepCount == STEP_SWITCH1 || stepCount == STEP_SWITCH3))
            getSpritedObject().setSprite(getSpritedObject().getSprites()[3]);
        if (facing == Game.LEFT && (stepCount == STEP_SWITCH2 || stepCount == STEP_SWITCH4))
            getSpritedObject().setSprite(getSpritedObject().getSprites()[4]);
        if (facing == Game.RIGHT && (stepCount == STEP_SWITCH1 || stepCount == STEP_SWITCH3))
            getSpritedObject().setSprite(getSpritedObject().getSprites()[5]);
        if (facing == Game.RIGHT && (stepCount == STEP_SWITCH2 || stepCount == STEP_SWITCH4))
            getSpritedObject().setSprite(getSpritedObject().getSprites()[6]);
        if (stepCount == STEP_SWITCH4) stepCount = 1;
    }
    
    /**
     * Mutator for last direction
     * @param lastDirection     New direction
     */
    public void setLastDirection( int lastDirection ) {this.lastDirection = lastDirection;}
    
    /**
     * Update camera to player's rectangle.
     * @param camera    Game camera to follow player.
     * @param cameraWalls   Walls for camera not to go past.
     * @param x         Move the camera by the x direction
     * @param y         Move the camera by the y direction
     */
    public void updateCamera(Rectangle camera, Rectangle[] cameraWalls, boolean x, boolean y) {
        Rectangle testCamera = new Rectangle(camera.getX(),
            camera.getY(), camera.getWidth(), camera.getHeight());
        boolean testFlag = false;
        if (x) {
            testCamera.setX(rect.getX() - (camera.getWidth() / 2));
            for (Rectangle r : cameraWalls) {
                if (r.getWidth() < r.getHeight()) continue;
                if (r.isColliding(testCamera)) testFlag = true;
            }
            if (!testFlag) camera.setX(rect.getX() - (camera.getWidth() / 2));
        }
        testCamera = new Rectangle(camera.getX(),
            camera.getY(), camera.getWidth(), camera.getHeight());
        if (y) {
            testCamera.setY(rect.getY() - (camera.getHeight() / 2));
            testFlag = false;
            for (Rectangle r : cameraWalls) {
                if (r.getWidth() > r.getHeight()) continue;
                if (r.isColliding(testCamera)) testFlag = true;
            }
            if (!testFlag) camera.setY(rect.getY() - (camera.getHeight() / 2));
        }
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
    
    public void turnDirection( int direction ) {
        
        if (facing == direction) return;
        facing = direction;
        if (direction == Game.UP)
            getSpritedObject().setSprite(getSpritedObject().getSprites()[8]);
        else if (direction == Game.DOWN)
            getSpritedObject().setSprite(getSpritedObject().getSprites()[1]);
        else if (direction == Game.LEFT)
            getSpritedObject().setSprite(getSpritedObject().getSprites()[4]);
        else if (direction == Game.RIGHT)
            getSpritedObject().setSprite(getSpritedObject().getSprites()[6]);
        
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
        if (!Game.isBattle()) {
            if (stepCount != 0) stepCount++;
            if (stepCount == STEP_SWITCH1 || stepCount == STEP_SWITCH2
                    || stepCount == STEP_SWITCH3
                    || stepCount == STEP_SWITCH4) step();
        }
    }
    
}
