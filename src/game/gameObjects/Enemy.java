package game.gameObjects;

import game.AbstractPath;
import game.CompoundPath;
import game.CompoundPathQueue;
import game.Game;
import static game.Game.loadImage;
import game.MovingPath;
import game.Path;
import game.RenderHandler;
import game.Sprite;
import game.SpriteSheet;
import game.TextHandler;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Enemy
 * @author Kobe Goodwin
 * @version 5/25/2022
 * 
 * An enemy in a battle.
 */
public class Enemy extends Battler {
    
    private PathedAnimatedSpritedObject pathedSprite;
    private CompoundPathQueue hurtPaths;
    private MovingPath idlePath;
    private MovingPath hurtPath;
    private Sprite hurtSprite;
    
    private String unknownName, name, shortUnknownName, shortName;
    private boolean isKnown, takingDamage;
    
    /**
     * Constructor
     * @param name              Complete, full name
     * @param unknownName       Name when encountering
     * @param shortUnknownName  Name when encountering, shortened
     * @param shortName         Shortened name
     * @param sprite            Sprite to represent it
     * @param xPosition         X Position
     * @param yPosition         Y Position
     * @param maxHP             Maximum Health
     * @param level             Level
     */
    public Enemy( String name, String unknownName, String shortUnknownName,
            String shortName, Sprite[] sprites, MovingPath path, int xPosition, 
            int yPosition, int maxHP, int level, int timePerSwitchMillis,
            boolean hideWhenFinished, boolean loopAnimation) {
        
        super(sprites[0], xPosition, yPosition, maxHP, level);
        Sprite[] idleSprites = new Sprite[sprites.length - 1];
        System.arraycopy(sprites, 0, idleSprites, 0, sprites.length - 1);
        this.pathedSprite = new PathedAnimatedSpritedObject(idleSprites, path, timePerSwitchMillis, hideWhenFinished, loopAnimation);
        this.idlePath = path;
        this.hurtPath = getHurtPath(xPosition, yPosition, 10);
        this.hurtSprite = sprites[sprites.length - 1];
        this.unknownName = unknownName;
        this.shortUnknownName = shortUnknownName;
        this.shortName = shortName;
        this.isKnown = false;
        setHPBar(new RatioBar(getHP(), getMaxHP(), TextHandler.GREEN, TextHandler.GRAY, 
                xPosition, yPosition - RenderHandler.ENEMY_HP_BAR_DISTANCE_FROM_Y, 
                getSpritedObject().getSprite().getWidth(), RenderHandler.ENEMY_HP_BAR_HEIGHT));
        getHPBar().hide();
        
    }
    
    private void toIdlePath( ) {
        pathedSprite.setPath(idlePath);
    }
    
    public void hurt( ) {
        
        takingDamage = true;
        hurtPaths = getHurtPathQueue(pathedSprite.getX(), pathedSprite.getY());
        hurtPaths.start();
        pathedSprite.setPath(hurtPaths.get());
        pathedSprite.pause();
        pathedSprite.setSprite(hurtSprite);
        
    }
    
    /**
     * Accessor for SpritedObject
     * @return  SpritedObject
     */
    @Override
    public PathedAnimatedSpritedObject getSpritedObject( ) {return pathedSprite;}
    
    @Override
    public void render( ) {
        pathedSprite.render();
    }
    
    @Override
    public void render( Graphics graphics ) {
        this.render();
    }
    
    @Override
    public int getX() {
        return pathedSprite.getX();
    }

    @Override
    public int getY() {
        return pathedSprite.getY();
    }

    @Override
    public double getTransparency() {
        return pathedSprite.getTransparency();
    }

    @Override
    public void setX(int x) {
        pathedSprite.setX(x);
    }

    @Override
    public void setY(int y) {
        pathedSprite.setY(y);
    }

    @Override
    public void setTransparency(double transparency) {
        pathedSprite.setTransparency(transparency);
    }

    @Override
    public void update(Game game) {
        pathedSprite.update(game);
        getHPBar().update(game);
        if (hurtPaths != null && hurtPaths.update()) {
            pathedSprite.setPath(hurtPaths.get());
        }
    }
    
    
    
    /**
     * Retrieves the name to display as, unshortened.
     * @return  Unshorted name.
     */
    public String getName( ) {
        return isKnown ? name : unknownName;
    }
    
    /**
     * Retrieves the name to display as, shortened.
     * @return  Shortened name.
     */
    public String getShortName( ) {
        return isKnown ? shortName : shortUnknownName;
    }
    
    /**
     * Appends a letter to the end of the enemy's name
     * @param letter    Letter to distinguish enemy
     */
    public void addLetterToName( char letter ) {
        
        name += " " + String.valueOf(letter);
        unknownName += " " + String.valueOf(letter);
        shortUnknownName += " " + String.valueOf(letter);
        shortName += " " + String.valueOf(letter);
        
    }
    
    /**
     * Factory method for Dummy.
     * @param xPosition     X Position to render at
     * @return  Dummy enemy
     */
    public static Enemy getDummy( int xPosition ) {
        
        SpriteSheet dummySheet = new SpriteSheet(Game.loadImage("ss\\dummy.png"), 78, 104);
        int y = 238 - dummySheet.getSprite(0, 0).getHeight();
        Enemy e = new Enemy( "Dummy", "Cotton-stuffed Doll", 
                "Cotton Doll", "Dummy", 
                dummySheet.getSprites(),
                new Path(String.valueOf(xPosition), String.valueOf(y), xPosition, y, 0, 0, 0, false),
                xPosition, 
                y, 10, 1, 0, false, false);
        return e;
        
    }
    
    /**
     * Factory method for Whimsun.
     * @param xPosition     X Position to render at
     * @return  Whimsun enemy
     */
    public static Enemy getWhimsun( int xPosition ) {
        
        SpriteSheet whimsunSheet = new SpriteSheet(Game.loadImage("ss\\whimsun.png"), 100, 94);
        int y = 15;
        Enemy e = new Enemy( "Whimsun", "Whimsun", 
                "Whimsun", "Whimsun", 
                whimsunSheet.getSprites(),
                new CompoundPath( xPosition, y, true,
                    new Path("0", "t", xPosition, y, 0, 10, 0.1, false),
                    new Path("0", "-1 * t", xPosition, y, 0, 10, 0.1, false)),
                xPosition, y, 370, 370, 370, false, true);
        e.getSpritedObject().animate();
        e.getSpritedObject().startMoving();
        return e;
        
    }
    
    private CompoundPath getHurtPath( int xPosition, int yPosition, int distance ) {
        
        CompoundPath hurtPath = new CompoundPath( xPosition, yPosition, false, 
                new Path("t", "0", xPosition, yPosition, 0, distance, distance, false),
                new Path("-1 * t", "0", xPosition, yPosition, 0, distance * 2, distance, false),
                new Path("t", "0", xPosition, yPosition, 0, distance, distance, false));
        return hurtPath;
        
    }
    
    private CompoundPathQueue getHurtPathQueue( int xPosition, int yPosition ) {
        
        int DISTANCE = 8;
        CompoundPath[] paths = new CompoundPath[DISTANCE];
        for (int i = DISTANCE; i >= 1; i--) {
            CompoundPath path = getHurtPath(xPosition, yPosition, i);
            paths[DISTANCE - i] = path;
        }
        return new CompoundPathQueue(paths);
        
    }
    
}
