package game;

import game.gameObjects.Enemy;

/**
 * Encounter
 * @author Kobe Goodwin
 * @version 3/6/2022
 * 
 * An encounter of Enemies.
 */
public class Encounter {
    
    private Enemy[] enemies;
    
    /**
     * Constructor
     * @param enemies   Enemies encountered 
     */
    public Encounter( Enemy[] enemies ) {
        
        this.enemies = enemies;
        
    }
    
    /**
     * Accessor for enemies encountered
     * @return  Array of Enemies encountered
     */
    public Enemy[] getEnemies( ) {return enemies;}
    
    /**
     * Factory method for one Dummy.
     * @return  Encounter with one Dummy.
     */
    public static Encounter getDummy( ) {
        
        return new Encounter( new Enemy[] {Enemy.getDummy(222)} );
        
    }
    
    public static Encounter getWhimsun( ) {
        
        return new Encounter( new Enemy[] {Enemy.getWhimsun(222)} );
        
    }
    
    /**
     * Factory method for two Dummies.
     * @return  Encounter with two Dummies.
     */
    public static Encounter getTwoDummies( ) {
        
        Enemy dummy1 = Enemy.getDummy(150);
        Enemy dummy2 = Enemy.getDummy(300);
        dummy1.addLetterToName('A');
        dummy2.addLetterToName('B');
        return new Encounter( new Enemy[] {dummy1, dummy2});
        
    }
    
    /**
     * Factory method for four Dummies.
     * @return  Encounter with four Dummies.
     */
    public static Encounter getFourDummies( ) {
        
        Enemy dummy1 = Enemy.getDummy(100);
        Enemy dummy2 = Enemy.getDummy(200);
        Enemy dummy3 = Enemy.getDummy(300);
        Enemy dummy4 = Enemy.getDummy(400);
        dummy1.addLetterToName('A');
        dummy2.addLetterToName('B');
        dummy3.addLetterToName('C');
        dummy4.addLetterToName('D');
        return new Encounter( new Enemy[] {dummy1, dummy2, dummy3, dummy4});
        
    }
    
}
