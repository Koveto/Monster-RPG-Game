package game.gameObjects;

import game.Game;
import game.MovingPath;
import game.RenderHandler;
import game.Sprite;
import game.TextHandler;
import java.awt.Graphics;

/**
 * Battler
 * @author Kobe Goodwin
 * @version 8/31/2022
 * 
 * A SpritedObject that has battle statistics, such as health and level.
 */
public class Battler implements GameObject {
    
    private PathedAnimatedSpritedObject sprite;
    
    private int maxHP, hp, level;
    private boolean isAlive;
    
    private RatioBar hpBar;
    
    /**
     * Constructor
     * @param sprites
     * @param path
     * @param xPosition         X Position
     * @param timePerSwitchMillis
     * @param yPosition         Y Position
     * @param maximumHitPoints  Maximum HP
     * @param hideWhenFinished
     * @param loopAnimation
     * @param level             Level
     */
    public Battler( Sprite[] sprites, MovingPath path, 
            int xPosition, int yPosition,
            int timePerSwitchMillis,
            int maximumHitPoints, int level,
            boolean hideWhenFinished, boolean loopAnimation ) {
        
        this.sprite = new PathedAnimatedSpritedObject(sprites, path, 
                timePerSwitchMillis, hideWhenFinished, loopAnimation);
        this.maxHP = maximumHitPoints;
        this.hp = maximumHitPoints;
        this.level = level;
        this.hpBar = new RatioBar(hp, maxHP, TextHandler.YELLOW, 
                TextHandler.DARK_RED, 277, 400, 26, RenderHandler.HP_BAR_HEIGHT);
        this.isAlive = true;
        
    
    }
    
    /**
     * Accessor for SpritedObject
     * @return  SpritedObject
     */
    public PathedAnimatedSpritedObject getSpritedObject( ) {return sprite;}
    
    /**
     * Accessor for Max HP
     * @return  Maximum HP
     */
    public int getMaxHP( ) {return maxHP;}
    
    /**
     * Accessor for HP
     * @return  Current HP
     */
    public int getHP( ) {return hp;}
    
    /**
     * Accessor for Level
     * @return  Level
     */
    public int getLevel( ) {return level;}
    
    /**
     * Determines if HP is 0 or lower
     * @return  True if HP greater than 0
     */
    public boolean isAlive( ) {return isAlive;}
    
    /**
     * Mutator for Max HP
     * @param maxHP     New Max HP
     */
    public void setMaxHP( int maxHP ) {
        this.maxHP = maxHP;
        hpBar.setDenominator(maxHP);
    }
    
    /**
     * Mutator for HP
     * @param hp    New HP
     */
    public void setHP( int hp ) {
        this.hp = hp;
        hpBar.setNumerator(hp < 0 ? 0 : hp);
    }
    
    /**
     * Mutator for Level
     * @param level New Level
     */
    public void setLevel( int level ) {this.level = level;}
    
    /**
     * Accessor for HP Bar
     * @return  RatioBar representing HP.
     */
    public RatioBar getHPBar( ) {
        return hpBar;
    } 
    
    /**
     * Mutator for HP Bar
     * @param hpBar     New HP Bar
     */
    public void setHPBar( RatioBar hpBar ) {
        this.hpBar = hpBar;
    }
    
    /**
     * Removes damage from HP
     * @param damage    Damage to remove from HP
     * @return  If HP after taking damage is greater than 0
     */
    public boolean takeDamage( int damage ) {
        
        setHP(this.hp - damage);
        isAlive = hp > 0; 
        return isAlive;
        
    }
    
    /**
     * Increases the level by one
     * @return  New level
     */
    public int levelUp( ) {
        
        this.level++;
        return level;
        
    }
    
    /**
     * Renders the Sprite.
     * @param graphics 
     */
    public void render( Graphics graphics ) {
        render();
    }
    
    /**
     * Renders the Sprite.
     */
    public void render( ) {
        
        sprite.render();
        
    }
    
    /**
     * Compares Object to Battler. Must have the same sprite at the same 
     * x and y positions to be equal. Must both be showing or not showing.
     * Must have the same HP, maxHP, and level.
     * @param o Object to compare to.
     * @return  Boolean evaluating if the objects are the same.
     */
    public boolean equals( Object o ) {
        
        if (!(o instanceof Battler)) return false;
        Battler b = (Battler) o;
        return (b.getSpritedObject().equals(this.getSpritedObject()))
                && b.getHP() == this.getHP()
                && b.getMaxHP() == this.getMaxHP()
                && b.getLevel() == this.getLevel();
        
    }
    
    /**
     * Describes Battler. In the form...
     * <p> super:Battler:hp:maxHP:isAlive:level
     * @return  String describing Battler.
     */
    public String toString( ) {
        
        return super.toString() + ":" + getClass().getName() + ":" + this.hp + 
                ":" + this.maxHP + ":" + this.isAlive + ":" + this.level;
        
    }

    @Override
    public int getX() {
        return sprite.getX();
    }

    @Override
    public int getY() {
        return sprite.getY();
    }

    @Override
    public double getTransparency() {
        return sprite.getTransparency();
    }

    @Override
    public void setX(int x) {
        sprite.setX(x);
    }

    @Override
    public void setY(int y) {
        sprite.setY(y);
    }

    @Override
    public void setTransparency(double transparency) {
        sprite.setTransparency(transparency);
    }

    @Override
    public void update(Game game) {
        sprite.update(game);
        hpBar.update(game);
    }
    
}
