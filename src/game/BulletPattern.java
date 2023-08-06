package game;

import game.gameObjects.PathedAnimatedSpritedObject;
import game.gameObjects.Rectangle;

/**
 * BulletPattern
 * @author Kobe Goodwin
 * @version 8/6/2023
 * 
 * A pattern of bullets for the player to dodge.
 */
public class BulletPattern {
    
    private BattleHandler b = new BattleHandler();
    private Bullet[] bullets;
    private long began, length;
    private boolean moving;
    
    public BulletPattern( ) {
        
        bullets = new Bullet[0];
        length = 10000;
        
    }
    
    private BulletPattern( Bullet[] bullets ) {
        
        this.bullets = bullets;
        
    }
    
    public boolean isMoving( ) { return moving; }
    
    public boolean isComplete( ) {
        if (System.currentTimeMillis() - began > length) { return true; }
        return false;
    }
    
    public void newAttack( Rectangle battleBox ) {
        Bullet bullet = new Bullet(b.BULLET.getSprite(0, 0), 
                new Path("2 * t", "t", battleBox.getX(), battleBox.getY(), 0, 100, .1, true), -100, -100, 2);
        bullets = new Bullet[] {bullet};
        length = 4000;
    }
    
    public void newWhimsunAttack( Rectangle battleBox ) {
        Bullet bullet = new Bullet(b.BULLET.getSprite(0, 0), 
                new Path("t", "t", battleBox.getX(), battleBox.getY(), 0, 100, .1, true), -100, -100, 5);
        bullets = new Bullet[] {bullet};
        length = 4000;
    }
    
    public void newTestAttack( Rectangle battleBox ) {
        Bullet bullet = new Bullet(b.BULLET.getSprite(0, 0),
                new Path("t", "t", battleBox.getX(), battleBox.getY(), 0, 100, .1, true), -100, -100, 5);
        bullets = new Bullet[] {bullet};
        length = 1000000;
    }
    
    public void start( ) {
        
        began = System.currentTimeMillis();
        for (PathedAnimatedSpritedObject o : bullets) {
            o.startMoving();
        }
        moving = true;
        
    }
    
    public PathedAnimatedSpritedObject[] getObjects( ) { return bullets; }
    public int getDamage( int index ) { return bullets[index].getDamage(); }
    
    public void remove( int index ) {
        
        Bullet[] newBullets = new Bullet[bullets.length - 1];
        for (int i = 0; i < index; i++) {
            newBullets[i] = bullets[i];
        }
        for (int i = index; i < bullets.length - 1; i++) {
            newBullets[i] = bullets[i + 1];
        }
        bullets = newBullets;
        
    }
    
    public Rectangle[] getRects( ) {
        
        Rectangle[] rects = new Rectangle[bullets.length];
        for (int i = 0; i < bullets.length; i++) {
            
            rects[i] = new Rectangle(bullets[i].getX(), bullets[i].getY(), 
                    bullets[i].getSprite().getWidth(), 
                    bullets[i].getSprite().getHeight());
            
        }
        return rects;
        
    }
    
    public class Bullet extends PathedAnimatedSpritedObject {
        
        private int damage;
        
        public Bullet( Sprite[] sprites, MovingPath path, 
                int timePerSwitchMillis, boolean hideWhenFinished,
                boolean loopAnimation, int damage ) {
            super(sprites, path, timePerSwitchMillis, hideWhenFinished, loopAnimation);
            this.damage = damage;
        }
        
        public Bullet( Sprite sprite, Path path, int x, int y, int damage ) {
            super(sprite, path, x, y);
            this.damage = damage;
        }
        
        public int getDamage( ) { return damage; }
        
    }
    
}
