package game;

import game.gameObjects.AnimatedSpritedObject;
import game.gameObjects.DoublySpritedObject;
import game.gameObjects.Enemy;
import game.gameObjects.GameObject;
import game.gameObjects.ImageObject;
import game.gameObjects.PathedAnimatedSpritedObject;
import game.gameObjects.Player;
import game.gameObjects.RatioBar;
import game.gameObjects.Rectangle;
import game.gameObjects.SpritedObject;
import java.awt.Color;

/**
 * Battle
 * @author Kobe Goodwin
 * @version 7/13/2023
 * 
 * Handles the properties of a battle.
 */
public class Battle {

    private final BattleHandler b;
    private final BattleText bt;
    private Script s;
    
    private final ImageObject backgroundImage;
    private final Rectangle battleRect;
    private final Rectangle[] backgroundRects;
    private final DoublySpritedObject fightButton, actButton, itemButton, mercyButton;
    
    private SpritedObject textBubble;
    private AnimatedSpritedObject attackAnimation, attackField;
    private PathedAnimatedSpritedObject attackCursor, damageNumber;
    
    private BulletPattern[] patterns;
    
    private String[] initialOptions, extraOptions;
    private Player player;
    private Enemy[] enemies;
    
    private int state, optionSelected, enemySelected, lastOptionSelected;
    private long lastStateSwitch;
    private String key;
    
    /**
     * Constructor
     * @param player    Player to control the battle
     * @param encounter Encounter player is encountering
     */
    public Battle( Player player, Encounter encounter ) {
        
        this.player = player;
        this.enemies = encounter.getEnemies();
        
        b = new BattleHandler();
        bt = new BattleText(b, player);
        
        this.backgroundImage = new ImageObject(
                Game.loadImage(b.PATH_BACKGROUNDIMAGE), 0, 0);
        
        this.battleRect = new Rectangle(b.X_BATTLERECT, 
                                        b.Y_BATTLERECT, 
                                        b.W_BATTLERECT, 
                                        b.H_BATTLERECT,
                                        TextHandler.WHITE.getRGB(),
                                        b.W_BATTLERECTBORDER);
        
        backgroundRects = new Rectangle[12];
        for (int i = 0; i < 6; i++) {
            backgroundRects[i] = new Rectangle( 16 + (i * 100), 9, 102, 119, 0x16532b, 2);
        }
        for (int i = 0; i < 6; i++) {
            backgroundRects[i + 6] = new Rectangle( 16 + (i * 100), 126, 102, 119, 0x16532b, 2);
        }
        
        this.textBubble = new SpritedObject(b.TEXT_BUBBLES.getSprite(0, 1), enemies[0].getX() + enemies[0].getSpritedObject().getSprite().getWidth(), enemies[0].getY() - (enemies[0].getSpritedObject().getSprite().getHeight() / 2));
        this.textBubble.hide();
        
        this.fightButton = b.newButton(b.FIGHT_BUTTON);
        this.actButton = b.newButton(b.ACT_BUTTON);
        this.itemButton = b.newButton(b.ITEM_BUTTON);
        this.mercyButton = b.newButton(b.MERCY_BUTTON);
        
        this.attackAnimation = new AnimatedSpritedObject(
                b.KNIFE_ATTACK.getSprites(), 
                0, 0, b.DELAY_ATTACKANIMATION);
        this.attackAnimation.hide();
        
        this.attackField = new AnimatedSpritedObject(
                b.ATTACK_FIELD.getSprites(), 
                b.X_ATTACKFIELD, b.Y_ATTACKFIELD, 30,
                false, false);
        this.attackField.hide();
        
        this.attackCursor = new PathedAnimatedSpritedObject(
            new Sprite[] {  b.ATTACK_CURSORS.getSprite(0, 0), 
                            b.ATTACK_CURSORS.getSprite(1, 0)},
            new Path(   "t",
                        "0",
                        0,
                        255,
                        b.X_ATTACKCURSOR, 
                        b.Y_ATTACKCURSOR, 
                        b.DELAY_ATTACKCURSORPATH, true),
            b.DELAY_ATTACKCURSORANIM, false, true);
        
        this.damageNumber = new PathedAnimatedSpritedObject(
            new Sprite[] {  b.DAMAGE_NUMBERS.getSprite(0, 0) },
            new CompoundPath(false, 
                new Path("0", "-2 * t", 0, 6, 0.5, false),
                new Path("0", "2 * t", 0, 8, 0.5, false),
                new Path("0", "-2 * t", 0, 2, 0.5, false)),
            1, false, true);
        this.damageNumber.hide();
        
        this.patterns = new BulletPattern[] {   new BulletPattern(),
                                                new BulletPattern(),
                                                new BulletPattern()};
        
        this.initialOptions = new String[0];
        this.extraOptions = new String[0];
        
        this.state = b.SELECTING_BATTLE_BUTTON;
        this.lastStateSwitch = System.currentTimeMillis();
        this.key = "";
        
    }
    
    /**
     * Evaluates if attack animation is playing
     * @return  True if attack animation is playing
     */
    private boolean isAttackAnimationPlaying( ) { return state == b.ATTACK_ANIMATION_PLAYING; }
    
    /**
     * Evaluates if enemy hp bar is sliding down
     * @return  True if enemy is taking damage
     */
    private boolean isEnemyTakingDamage( ) { return state == b.ENEMY_TAKING_DAMAGE; }
    
    /**
     * Evaluates if battle is between player's turn and enemy's turn
     * @return  True if battle is between turns
     */
    private boolean isBetweenTurns( ) { return state == b.BETWEEN_TURNS; }
    
    /**
     * Evaluates if battle is between enemy's turn and player's turn
     * @return  True if battle is between turns
     */
    private boolean isTransitioningToPlayerTurn( ) { return state == b.ENEMY_TURN_FINISHED; }
    
    /**
     * Determines if the battle is waiting to progress on the scroll of a Text.
     * @return  True if waiting on a Text
     */
    private boolean isWaitingOnText( ) { return bt.getTextWaitingOn() != null; }
    
    /**
     * Determines if the enemies' turn is in progress.
     * @return  True if enemy turn in progress
     */
    public boolean isEnemyTurn( ) { return state == b.ENEMY_TURN; }
    
    /**
     * Accessor for Player
     * @return  Player character
     */
    public Player getPlayer( ) {return player;}
    
    /**
     * Accessor for State
     * @return  State of Battle
     */
    public int getState( ) {return state;}
    
    /**
     * Retrieves all Texts to render.
     * @return  Array of Texts
     */
    public Text[] getText( ) { return bt.getText(); }
    
    /**
     * Updates processes and Objects in Battle depending on its state.
     * @return  True if the Battle is ended, False if not.
     */
    public boolean update( ) {
        
        if (s != null) s.update();
        
        if (key.equals("resize") || key.equals("text")) resizeBattleRect();
        if (key.equals("end")) endBattle();
        if (key.equals("text")) enemyTurn();
        
        if (isAttackAnimationPlaying() ) {
            checkIfAttackAnimationIsFinished();
        } else if (isEnemyTakingDamage()) {
            checkIfDamageNumberFinished();
        } else if (isEnemyTurn()) {
            checkBulletCollision();
        } else if (isTransitioningToPlayerTurn()) {
            transitionToPlayerTurn();
        } else if (isEnded()) { return true; }
        
        return false;
    }
    
    /**
     * Handles keyboard input during a Battle. Called in Game.update(). Depends
     * on state of battle and button being pressed.
     * @param button    Button being pressed. Clarified in Game.
     */
    public void interpretButtonPress( int button ) {
        
        if (player.getLastDirection() == button && state != b.ENEMY_TURN) {
            return;
        }
        
        if (state == b.SELECTING_BATTLE_BUTTON) {
            
            if (button == Game.LEFT)
                selectBattleButton(true);
            else if (button == Game.RIGHT)
                selectBattleButton(false);
            else if (button == Game.CONFIRM)
                pressBattleButton();
            
        } else if (state == b.SELECTING_TARGET) {
            
            if (button == Game.UP || button == Game.DOWN || 
                    button == Game.LEFT || button == Game.RIGHT) {
                selectOption(button);
            } else if (button == Game.CONFIRM) {
                if (getButtonSelected() == b.ACT_BUTTON) {
                    state = b.SELECTING_ACT;
                    highlightSelectedEnemy();
                    listOptions(new String[] {"Check", "Encourage",
                        "Talk", "Hug"}, false);
                } else if (state == b.SELECTING_TARGET) {
                    bt.clear();
                    Game.getSound().play("Select", false);
                    fightButton.switchSprite();
                    attackField.show();
                    attackCursor.show();
                    attackCursor.startMoving();  //getPath().startAt(b.X_ATTACKCURSOR, b.Y_ATTACKCURSOR);
                    //resetSelectedEnemy();
                    for (Enemy e : enemies) {
                        e.getSpritedObject().toggleBrightness(b.BRIGHTEN_FACTOR, b.TIMES_TO_BRIGHTEN);
                        e.getSpritedObject().stopFlashing();
                    }
                    for (int i = 0; i < enemies.length; i++) {
                        if (i != enemySelected) {
                            enemies[i].getHPBar().fadeOut(b.FADE_OUT_SPEED_NORMAL);
                        }
                    }
                    state = b.ATTACKING;
                }
            } else if (button == Game.CANCEL) {
                if (getButtonSelected() == b.FIGHT_BUTTON) {
                    pressBattleButton();
                }
                else
                    backToButtonSelect();
            }
            
        } else if (state == b.SELECTING_SKILL) {
            
            if (button == Game.UP || button == Game.DOWN || 
                    button == Game.LEFT || button == Game.RIGHT) {
                selectOption(button);
            } else if (button == Game.CONFIRM) {
                selectTarget(false);
            } else if (button == Game.CANCEL) {
                backToButtonSelect();
            }
            
        } else if (state == b.SELECTING_ACT) {
            
            if (button == Game.UP || button == Game.DOWN || 
                    button == Game.LEFT || button == Game.RIGHT) {
                selectOption(button);
            } else if (button == Game.CONFIRM) {
                //
            } else if (button == Game.CANCEL) {
                selectTarget(true);
            }
            
        } else if (state == b.SELECTING_MERCY) {
        
            if (button == Game.UP || button == Game.DOWN || 
                    button == Game.LEFT || button == Game.RIGHT) {
                selectOption(button);
            } else if (button == Game.CONFIRM) {
                //
            } else if (button == Game.CANCEL) {
                backToButtonSelect();
            }
        } else if (state == b.SELECTING_ITEM) {
            
            if (button == Game.UP || button == Game.DOWN || 
                    button == Game.LEFT || button == Game.RIGHT) {
                selectOption(button);
            } else if (button == Game.CONFIRM) {
                //
            } else if (button == Game.CANCEL) {
                backToButtonSelect();
            }
            
        } else if (state == b.ATTACKING) {
            
            if (button == Game.CONFIRM && 
                    attackCursor.getX() > battleRect.getX() && 
                    attackCursor.getX() < (battleRect.getX() + battleRect.getWidth())) {
                state = b.ATTACK_ANIMATION_PLAYING;
                Game.getSound().play("Attack", false);
                attackCursor.stopMoving();
                attackCursor.animate();
                attackAnimation.setX(enemies[enemySelected].getX() + 35);
                attackAnimation.setY(enemies[enemySelected].getY() + 30);
                attackAnimation.animate();
                s = new Script("text\\battleScript.txt\\", null, this, 
                    new GameObject[] {attackAnimation, enemies[enemySelected], enemies[enemySelected].getHPBar(),
                    damageNumber, enemies[enemySelected].getSpritedObject(), attackField, attackCursor, battleRect,
                    textBubble, player, player.getSpritedObject(), player.getRect()}, 
                        new DialogueBox());
                
            }
            
        } else if (isWaitingOnText() && checkOnWaitingText() && button == Game.CONFIRM) {
            
            bt.waitOnText(null);
        
        } else if (state == b.ENEMY_TURN) {
            
            if (button == Game.LEFT) {
                player.setX(player.getX() - b.PLAYER_SPEED);
                if (!player.getRect().isInside(battleRect.getInnerRect())) {
                    player.setX(battleRect.getX() + battleRect.getBorderWidth());
                }
                    
            } else if (button == Game.RIGHT) {
                player.setX(player.getX() + b.PLAYER_SPEED);
                if (!player.getRect().isInside(battleRect.getInnerRect()))
                    player.setX(battleRect.getX() + battleRect.getWidth() - battleRect.getBorderWidth() - player.getSpritedObject().getSprite().getWidth());
            } else if (button == Game.UP) {
                player.setY(player.getY() - b.PLAYER_SPEED);
                if (!player.getRect().isInside(battleRect.getInnerRect()))
                    player.setY(battleRect.getY() + battleRect.getBorderWidth());
            } else if (button == Game.DOWN) {
                player.setY(player.getY() + b.PLAYER_SPEED);
                if (!player.getRect().isInside(battleRect.getInnerRect())) {
                    player.setY(battleRect.getY() + battleRect.getHeight() - battleRect.getBorderWidth() - player.getSpritedObject().getSprite().getHeight());
                }
            }
            
        }
        
        if (!(state == b.SELECTING_BATTLE_BUTTON && button == Game.CANCEL)) 
            player.setLastDirection(button);
        
    }
    
    /**
     * Determines which option is being selected based on the options being
     * displayed to the screen and the player's position.
     * @return  Option selected
     */
    private int getSelectedOption( ) {
        
        Text[] options = getOptions();
        
        int optionSelected = 0;
        
        if (options.length > 3) {
            if (player.getY() == b.Y_PLAYEROPTIONROW1) {
                if (player.getX() == b.X_PLAYEROPTIONCOLUMN1)
                    optionSelected = 0;
                else
                    optionSelected = 1;
            }
            else if (player.getY() == b.Y_PLAYEROPTIONROW2) {
                if (player.getX() == b.X_PLAYEROPTIONCOLUMN1)
                    optionSelected = 2;
                else
                    optionSelected = 3;
            }
            else {
                if (player.getX() == b.X_PLAYEROPTIONCOLUMN1)
                    optionSelected = 4;
                else
                    optionSelected = 5;
            }
        }
        else {
            if (player.getY() == b.Y_PLAYEROPTIONROW1)
                optionSelected = 0;
            else if (player.getY() == b.Y_PLAYEROPTIONROW2)
                optionSelected = 1;
            else
                optionSelected = 2;
        }
        
        return optionSelected;
        
    }
    
    /**
     * Lists GameObjects in the Battle
     * @return  Array of GameObjects to be rendered
     */
    public GameObject[] getObjects( ) {
        
        GameObject[] temp = new GameObject[] {
            fightButton, actButton, itemButton, mercyButton,
            player, player.getHPBar(), attackField, battleRect, attackCursor,
            textBubble};
        GameObject[] objects = new GameObject[temp.length + 
                patterns[0].getObjects().length + 
                patterns[1].getObjects().length + 
                patterns[2].getObjects().length + 
                (2 * enemies.length) + backgroundRects.length + 3];
        objects[0] = backgroundImage;
        for (int i = 0; i < backgroundRects.length; i++) {
            objects[i + 1] = backgroundRects[i];
        }
        for (int i = 0; i < temp.length; i++) {
            objects[i + backgroundRects.length + 1] = temp[i];
        }
        for (int i = 0; i < enemies.length; i++) {
            objects[i + temp.length + backgroundRects.length + 1] = enemies[i];
        }
        objects[1 + backgroundRects.length + temp.length + enemies.length] = damageNumber;
        for (int i = 0; i < enemies.length; i++) {
            objects[
                i + temp.length + backgroundRects.length + enemies.length + 2] 
                    = enemies[i].getHPBar();
        }
        for (int i = 0; i < patterns[0].getObjects().length; i++) {
            objects[
                i + temp.length + backgroundRects.length + (2 * enemies.length) + 2] =
                    patterns[0].getObjects()[i];
        }
        for (int i = 0; i < patterns[1].getObjects().length; i++) {
            objects[
                i + temp.length + backgroundRects.length + (2 * enemies.length) 
                    + 2 + patterns[0].getObjects().length] =
                    patterns[1].getObjects()[i];
        }
        for (int i = 0; i < patterns[2].getObjects().length; i++) {
            objects[
                i + temp.length + backgroundRects.length + (2 * enemies.length) 
                    + 2 + patterns[0].getObjects().length + patterns[1].getObjects().length] =
                    patterns[2].getObjects()[i];
        }
        objects[objects.length - 1] = attackAnimation;
        //for (GameObject go : objects) System.out.println(go);
        return objects;
        
    }
    
    /**
     * Lists objects with full transparency
     * @return  Array of GameObjects with full transparency
     */
    public GameObject[] getOpaqueObjects( ) {
        
        GameObject[] objects = getObjects();
        
        GameObject[] opaqueObjects = new GameObject[0];
        for (GameObject go : objects) {
            if (go.getTransparency() == 1) {
                GameObject[] tempArray = new GameObject[opaqueObjects.length + 1];
                for (int j = 0; j < opaqueObjects.length; j++) {
                    tempArray[j] = opaqueObjects[j];
                }
                tempArray[opaqueObjects.length] = go;
                opaqueObjects = tempArray;
            }
        }
        
        return opaqueObjects;
        
    }
    
    /**
     * Lists Game Objects with partial transparency
     * @return  Array of GameObjects with partial transparency
     */
    public GameObject[] getTransparentObjects( ) {
        
        GameObject[] objects = getObjects();
        
        GameObject[] transparentObjects = new GameObject[0];
        for (GameObject go : objects) {
            if (go.getTransparency() != 1) {
                GameObject[] tempArray = new GameObject[transparentObjects.length + 1];
                for (int j = 0; j < transparentObjects.length; j++) {
                    tempArray[j] = transparentObjects[j];
                    //System.out.println(transparentObjects[j].getTransparency() + String.valueOf(transparentObjects[j] instanceof Enemy));
                }
                tempArray[transparentObjects.length] = go;
                transparentObjects = tempArray;
            }
        }
        
        //System.out.println(transparentObjects.length);
        
        return transparentObjects;
        
    }
    
    /**
     * Progresses Battle after the attack animation plays. Changes state to
     * ENEMY_TAKING_DAMAGE. Slides HP bar by damage, calls hurt animation,
     * updates enemy health, begins damageNumber Path.
     */
    private void checkIfAttackAnimationIsFinished( ) {
        
        if (attackAnimation.finishedAnimating() && System.currentTimeMillis() - 
                lastStateSwitch >= b.DELAY_ATTACKTOTAKINGDAMAGE) {
            state = b.ENEMY_TAKING_DAMAGE;
            /*int damage = 5;
            enemies[enemySelected].getHPBar().slideToNumerator(
                    enemies[enemySelected].getHP() - damage);
            enemies[enemySelected].hurt();
            enemies[enemySelected].takeDamage(damage);
            
            Game.getSound().play("Damage", false);
            setDamageNumberSprite(damage);
            
            int x = enemies[enemySelected].getHPBar().getX() + 
                    ((enemies[enemySelected].getHPBar().getWidth() 
                    - damageNumber.getSprite().getWidth()) / 2);
            int y = enemies[enemySelected].getHPBar().getY() - 3 -
                    b.H_SHEET_DAMAGENUMBERS;
            damageNumber.getPath().startAt(x, y);
            damageNumber.setX(x);
            damageNumber.setY(y);
            
            damageNumber.show();*/
            lastStateSwitch = System.currentTimeMillis();
        } else if (!attackAnimation.finishedAnimating()) {
            lastStateSwitch = System.currentTimeMillis();
        }
        
        
    }
    
    /**
     * Updates state to BETWEEN_TURNS if damageNumber completed Path.
     */
    private void checkIfDamageNumberFinished( ) {
        
        if (damageNumber.getPath().isFinished()) {
            state = b.BETWEEN_TURNS;
            lastStateSwitch = System.currentTimeMillis();
        }
        
    }
    
    /**
     * Determines if Battle ended.
     * @return  True if ended, False if not.
     */
    private boolean isEnded( ) { return state == b.BATTLE_END; }
    
    public void startResizing( ) {key = "resize";}
    public void startBattleEnd( ) {key = "end";}
    
    private void enemyTurn( ) {
        if (!isWaitingOnText()) {
            key = "";
            textBubble.hide();
            bt.clear();
            state = b.ENEMY_TURN;
            patterns[0].newWhimsunAttack(battleRect);
            patterns[0].start();
            patterns[1].newAttack(battleRect);
            patterns[1].start();
        }
    }
    
    public void waitOnTextBubble( ) {
        
        Text bubble = bt.getTextBubbles()[0];
        bubble.setX(textBubble.getX() + b.X_BUBBLEOFFSET);
        bubble.setY(textBubble.getY() + b.Y_BUBBLEOFFSET);
        bubble.newMessage("Ribbit, ribbit, ribbit, ribbit");
        bt.displayTexts(new Text[] {bubble});
        bt.waitOnText(bubble);
        key = "text";
        
    }
    
    private void resizeBattleRect( ) {
        if (!attackCursor.isShowing() &&
                battleRect.getWidth() > 220
                && enemies[enemySelected].isAlive()) {
            battleRect.setX(battleRect.getX() + b.W_BATTLERECTINCREMENT);
            battleRect.setWidth(battleRect.getWidth() - (2 * b.W_BATTLERECTINCREMENT));
            battleRect.generateGraphics(b.W_BATTLERECTBORDER, TextHandler.WHITE.getRGB());
        }
    }
    
    private void endBattle( ) {
        s = null;
        if (!attackField.isShowing() && !enemies[enemySelected].isAlive()
                && bt.getTextWaitingOn() == null) {
            if (bt.getFlavorText().getMessage().equals("You   won!\nYou   earned   5   EXP   and   1   gold.")) {
                state = b.BATTLE_END;
                return;
            }
            Game.setScrollSpeed(TextHandler.SLOW_SCROLL_SPEED);
            Game.getMusic2().stop();
            Game.getSound().play("LevelUp", false);
            bt.displayFlavorText("You won!\nYou earned 5 EXP and 1 gold.", true);
            bt.waitOnText(bt.getFlavorText());
        }
    }
    
    /**
     * Widens battleRect and returns to SELECTING_BATTLE_BUTTON after enemy turn.
     */
    private void transitionToPlayerTurn( ) {
        
        if (battleRect.getWidth() < b.W_BATTLERECT) {
            battleRect.setX(battleRect.getX() - b.W_BATTLERECTINCREMENT);
            battleRect.setWidth(battleRect.getWidth() + (2 * b.W_BATTLERECTINCREMENT));
            battleRect.generateGraphics(b.W_BATTLERECTBORDER, TextHandler.WHITE.getRGB());
        } else {
            state = b.SELECTING_BATTLE_BUTTON;
            player.getSpritedObject().show();
            player.setX(b.X_PLAYERFIGHTBUTTON);
            player.setY(b.Y_PLAYERBUTTONS);
            attackField.setSprite(b.ATTACK_FIELD.getSprite(0, 0));
            attackCursor.setSprite(b.ATTACK_CURSORS.getSprite(0, 0));
            attackCursor.setX(b.X_ATTACKCURSOR);
            attackCursor.getPath().restart();
            Game.setScrollSpeed(TextHandler.DEFAULT_SCROLL_SPEED);
            bt.displayFlavorText(b.TEXT_DEFAULTFLAVOR, true);
            fightButton.switchSprite();
        }
        
    }
    
    /**
     * Handles bullet collision during enemy turn. Hides player and bullets
     * when attack is complete. Removes collided bullet and damages player.
     */
    private void checkBulletCollision( ) {
        
        /*Rectangle[] bullets = new Rectangle[patterns[0].getRects().length + 
                patterns[1].getRects().length + patterns[2].getRects().length];
        for (int i = 0; i < patterns[0].getRects().length; i++) {
            bullets[i] = patterns[0].getRects()[i];
        }*/
        for (BulletPattern bp : patterns) {
            if (bp.isMoving() && bp.isComplete()) {
                patterns[0] = new BulletPattern();
                patterns[1] = new BulletPattern();
                patterns[2] = new BulletPattern();
                player.getSpritedObject().hide();
                state = b.ENEMY_TURN_FINISHED;
                return;
            }
        }
        if (!player.isVulnerable()) return;
        int damage = 0;
        for (int i = 0; i < patterns.length; i++) {
            for (int j = 0; j < patterns[i].getRects().length; j++) {
                if (player.getRect().isColliding(patterns[i].getRects()[j])) {
                    Game.getSound().play("Hurt", false);
                    damage = patterns[i].getDamage(j);
                    patterns[i].remove(j);
                    break;
                }
            }
            if (damage != 0) break;
        }
        if (damage != 0) {
            player.takeDamage(damage);
            bt.updateHP(player);
        }
        
    }
    
    /**
     * Determines if the Text the battle is waiting on is finished scrolling.
     * @return  True if finished scrolling, false if not.
     */
    private boolean checkOnWaitingText( ) {
        
        if (bt.getTextWaitingOn() == null) return false;
        if (bt.getTextWaitingOn().isFinishedScrolling()) return true;
        return false;
        
    }
    
    /**
     * Retrieves options being displayed as Texts
     * @return  Text array of options
     */
    private Text[] getOptions( ) { return bt.getOptions(); }
    
    /**
     * Resets the flashing of an Enemy and its brightness.
     */
    private void resetSelectedEnemy( ) {
        
        enemies[enemySelected].getSpritedObject().stopFlashing();
        enemies[enemySelected].getSpritedObject().brightenToDefault(b.BRIGHTEN_TO_DEFAULT_FACTOR);
        
    }
    
    /**
     * Determines which Battle Button is selected
     * @return  Index of battle button 0-3
     */
    private int getButtonSelected( ) {
        
        DoublySpritedObject[] battleButtons = 
            {fightButton, actButton, itemButton, mercyButton};
        int buttonSelected = b.FIGHT_BUTTON;
        
        if (!actButton.isSpriteA()) buttonSelected = b.ACT_BUTTON;
        else if (!itemButton.isSpriteA()) buttonSelected = b.ITEM_BUTTON;
        else if (!mercyButton.isSpriteA()) buttonSelected = b.MERCY_BUTTON;
        
        return buttonSelected;
        
    }
    
    /**
     * Creates a Sprite of a damage number which includes "1". Handled specially
     * because of spacing ("1" is less wide). Called in getDamageNumberSprite.
     * @return  Sprite representing "1".
     */
    private Sprite getNumberOneSprite( ) {
        int[] pixels = b.DAMAGE_NUMBERS.getSprite(1, 0).getPixels();
        int[] newPixels = new int[570];
        int counter1 = 0, counter2 = 0;
        for (int i = 0; i < pixels.length; i++) {
            counter1++;
            if (counter1 == 31) counter1 = 0;
            if (counter1 > 11) {
                newPixels[counter2] = pixels[i + 1];
                counter2++;
            }
        }
        return new Sprite(newPixels, 19, 30);
    }
    
    /**
     * Changes Damage Number Sprite representing the number of damage dealt. 
     * @param damage    Damage dealt.
     */
    public void setDamageNumberSprite( int damage ) {
        
        Sprite newSprite = null;
        
        if (damage < 10) {
            if (damage == 1) {
                newSprite = getNumberOneSprite();
            }
            else newSprite = b.DAMAGE_NUMBERS.getSprite(damage, 0);
        }
        else {
            String damageString = String.valueOf(damage);
            Sprite[] numbers = new Sprite[damageString.length()];
            for (int i = 0; i < damageString.length(); i++) {
                int x = Integer.parseInt(
                        new String(new char[] {damageString.charAt(i)}));
                numbers[i] = b.DAMAGE_NUMBERS.getSprite(x, 0);
                if (damageString.charAt(i) == '1') numbers[i] = getNumberOneSprite();
            }
            newSprite = new Sprite(numbers); 
        }
        damageNumber.setSprite(newSprite);
        
    }
    
    /**
     * Handles Battle during SELECTING_TARGET state. Selected Enemy flashes and
     * nonselected enemies darken.
     * @param returning     True if canceled an ACT, False if from FIGHT/ACT.
     */
    private void selectTarget( boolean returning ) {
        
        if (!returning) Game.getSound().play("Select", false);
        
        player.getSpritedObject().hide();
        state = b.SELECTING_TARGET;
        if (!returning)
            enemySelected = 0;
        lastOptionSelected = optionSelected;
        
        bt.displayFlavorText("Cotton-Stuffed Doll  LV 1 /N A cotton heart and a button eye, you are the apple of my eye.", 
                true);
        
        player.setX((new int[] {b.X_PLAYERFIGHTBUTTON, b.X_PLAYERACTBUTTON, 
            b.X_PLAYERITEMBUTTON, b.X_PLAYERMERCYBUTTON})[getButtonSelected()]);
        player.setY(b.Y_PLAYERBUTTONS);
        
        enemies[enemySelected].getSpritedObject().flash();
        for (int i = 0; i < enemies.length; i++) {
            enemies[i].getHPBar().show();
            if (!returning) {
                enemies[i].getHPBar().setTransparency(1.0);
                enemies[i].getHPBar().fadeIn(b.FADE_OUT_SPEED_NORMAL);
            }
            if (i != enemySelected) {
                enemies[i].getHPBar().resetBrightness();
                enemies[i].getHPBar().toggleBrightness(b.DARKENED);
            }
        }
        enemies[enemySelected].getHPBar().brightenToDefault(b.BRIGHTEN_TO_DEFAULT_FACTOR);
        if (getButtonSelected() != 1)
            for (int i = 1; i < enemies.length; i++) {
                enemies[i].getSpritedObject().toggleBrightness(b.DARKEN_FACTOR, b.TIMES_TO_DARKEN);
            }
        else
            for (int i = 0; i < enemies.length; i++) {
                if (i != enemySelected) {
                    enemies[i].getSpritedObject().resetBrightness();
                    enemies[i].getSpritedObject().stopFlashing();
                    enemies[i].getSpritedObject().toggleBrightness(b.DARKENED);
                }
            }
        if (!returning)
            optionSelected = 0;
        else {
            optionSelected = enemySelected;
        }
        
    }
    
    /**
     * Brightens selected Enemy and stops flashing.
     */
    private void highlightSelectedEnemy( ) {
        
        Game.getSound().play("Select", false);
        enemies[enemySelected].getSpritedObject().brightenToDefault(b.BRIGHTEN_TO_DEFAULT_FACTOR);
        enemies[enemySelected].getSpritedObject().stopFlashing();
        
    }
    
    /**
     * Lists Text selections to display and alters them by color code. Handles
     * multiple pages. Moves player to appropriate selection.
     * @param options       String array of formatted Text options.
     * @param returning     True if returning from selecting target.
     */
    private void listOptions( String[] options, boolean returning ) {
        
        player.getSpritedObject().show();
        
        // List Text selections to display
        Text[] temp = new Text[0];
        if (options.length < 7) {
            if (options.length < 4 && !options.equals(extraOptions))
                temp = bt.getNSelections(options.length);
            else
                temp = bt.getNSelections(options.length, true);
        } else {  // 2 pages
            temp = bt.getNSelections(5, true);
            extraOptions = new String[options.length - 5];
            for (int i = 5; i < options.length; i++) {
                extraOptions[i - 5] = options[i];
            }
            initialOptions = new String[5];
            for (int i = 0; i < 5; i++) {
                initialOptions[i] = options[i];
            }
            
        }
        
        // Color texts and change their messages
        for (int i = 0; i < temp.length; i++) {
            String message = options[i];
            String[] colorCodes = TextHandler.listColorCodes();
            Color[] codedColors = TextHandler.listCodedColors();
            boolean containsCodedColor = false;
            for (int j = 0; j < colorCodes.length; j++) {
                if (options[i].startsWith(colorCodes[j])) {
                    message = options[i].substring(2);
                    temp[i].setColor(codedColors[j]);
                    containsCodedColor = true;
                    break;
                }
            }
            if (!containsCodedColor)
                temp[i].setColor(TextHandler.WHITE);
            
            temp[i].newMessage("* " + message);
        }
        
        // Add page number
        if (options.length > 6 || bt.getPageNumber() == 2) {
            temp = bt.appendPageNumberToTextArray(1, temp);
        } else if (options.equals(initialOptions) && bt.getPageNumber() == 1) {
            temp = bt.appendPageNumberToTextArray(null, temp);
        } else if (options.equals(extraOptions) && bt.getPageNumber() == 1) {
            temp = bt.appendPageNumberToTextArray(2, temp);
        }
        
        bt.displayTexts(temp);
        
        // Select option
        if (options.equals(initialOptions)) {
            optionSelected++;
        } else if (options.equals(extraOptions) && options.length > 2) {
            optionSelected--;
        } else if (returning) {
            optionSelected = lastOptionSelected;
        } else {
            optionSelected = 0;
        }
        
        movePlayerToSelection(optionSelected);
        
    }
    
    /**
     * Handles a CONFIRM input while selecting a battle button. Changes depending
     * on button pressed: FIGHT changes state to SELECTING_SKILL, ACT to
     * SELECTING_TARGET, ITEM to SELECTING_ITEM, MERCY to SELECTING_MERCY.
     */
    private void pressBattleButton( ) {
        
        player.getSpritedObject().show();
        
        resetEnemyEffects();
        int buttonSelected = getButtonSelected();
        
        if (buttonSelected == b.FIGHT_BUTTON) {
            
            if (state == b.SELECTING_BATTLE_BUTTON) Game.getSound().play("Select", false);
            state = b.SELECTING_SKILL;
            listOptions(new String[] {"/AIce Age", "/RInferno", "/YThunder Reign", "/GZandyne"}, true);
            
        } else if (buttonSelected == b.ACT_BUTTON) {
            
            selectTarget(false);
            
        } else if (buttonSelected == b.ITEM_BUTTON) {
            
            if (state == b.SELECTING_BATTLE_BUTTON) Game.getSound().play("Select", false);
            state = b.SELECTING_ITEM;
            listOptions(new String[] {"Bandage", "Spider Donut", "Spider Cider", "Bscotch Pie", "Snail Pie", "Snow Piece", "Nice Cream", "Glamburger"}, false);
            
        } else if (buttonSelected == b.MERCY_BUTTON) {
            
            if (state == b.SELECTING_BATTLE_BUTTON) Game.getSound().play("Select", false);
            state = b.SELECTING_MERCY;
            listOptions(new String[] {"Spare", "Flee"}, false);
            
        }
        
    }
    
    /**
     * Handles a CANCEL input while choosing a skill. Clears battle text.
     */
    private void backToButtonSelect( ) {
        
        player.getSpritedObject().show();
        
        state = b.SELECTING_BATTLE_BUTTON;
        int button = getButtonSelected();
        int[] x = new int[] {b.X_PLAYERFIGHTBUTTON, b.X_PLAYERACTBUTTON, 
            b.X_PLAYERITEMBUTTON, b.X_PLAYERMERCYBUTTON};
        player.setX(x[button]);
        player.setY(b.Y_PLAYERBUTTONS);
        //pageNumber.newMessage("");
        lastOptionSelected = b.FIGHT_BUTTON;
        
        bt.clear();
        bt.setPageNumber(0);
        bt.displayFlavorText(b.TEXT_DEFAULTFLAVOR, true);
        resetEnemyEffects();
        
    }
    
    /**
     * Resets effects of all enemies.
     */
    private void resetEnemyEffects( ) {
        
        resetEnemyEffects( -1 );
        
    }
    
    /**
     * Resets effects of all enemies except for the one occupying the ignoreIndex.
     * @param ignoreIndex   Index of Enemy to ignore.
     */
    private void resetEnemyEffects( int ignoreIndex ) {
        
        for (int i = 0; i < enemies.length; i++) {
            if (i != ignoreIndex) {
                enemies[i].getSpritedObject().toggleBrightness(b.BRIGHTEN_FACTOR, b.TIMES_TO_BRIGHTEN);
                enemies[i].getSpritedObject().stopFlashing();
                if (i != optionSelected)
                    enemies[i].getHPBar().fadeOut(b.FADE_OUT_SPEED_NORMAL);
                else
                    enemies[i].getHPBar().fadeOut(b.FADE_OUT_SPEED_FAST);
            }
        }
        
    }
    
    /**
     * Determines if the current getOptions() return array is the same as options.
     * @param options   Options to compare to.
     * @return      True if getOptions and options are the same.
     */
    private boolean optionsEqual( String[] options ) {
        
        if (options.length == 0) return false;
        boolean failed = false;
        for (int i = 0; i < getOptions().length; i++) {
            if (!getOptions()[i].getMessage().equals("*  " + TextHandler.multiplyCharacter(options[i], ' ', 2))) {
                failed = true;
                break;
            }
        }
        return !failed;
        
    }
    
    /**
     * Sets player's position to the appropriate option.
     * @param optionSelected    Index of option to move Player to.
     */
    private void movePlayerToSelection( int optionSelected ) {
        
        int[] columns = new int[] {b.X_PLAYEROPTIONCOLUMN1, b.X_PLAYEROPTIONCOLUMN2};
        int[] rows = new int[] {b.Y_PLAYEROPTIONROW1, b.Y_PLAYEROPTIONROW2, b.Y_PLAYEROPTIONROW3};
        
        boolean useShortOptions = getOptions().length > 3;
        if (optionsEqual(extraOptions)) useShortOptions = true;
        
        if (useShortOptions) {
            player.setX(columns[optionSelected % 2]);
            if (optionSelected > 3)
                player.setY(rows[2]);
            else if (optionSelected > 1)
                player.setY(rows[1]);
            else
                player.setY(rows[0]);
        } else {
            player.setX(b.X_PLAYEROPTIONCOLUMN1);
            if (optionSelected == 0) {
                player.setY(b.Y_PLAYEROPTIONROW1);
            } else if (optionSelected == 1) {
                player.setY(b.Y_PLAYEROPTIONROW2);
            } else if (optionSelected == 2) {
                player.setY(b.Y_PLAYEROPTIONROW3);
            }
        }
        
    }
    
    /**
     * Selects new enemy and deselects previous enemy. Previous stops flashing
     * and darkens. New enemy flashes and brightens.
     * @param newEnemyIndex     Index of Enemy to select.
     */
    private void selectEnemy( int newEnemyIndex ) {
        
        enemies[enemySelected].getSpritedObject().resetBrightness();
        enemies[enemySelected].getSpritedObject().stopFlashing();
        enemies[enemySelected].getSpritedObject().toggleBrightness(b.DARKEN_FACTOR, b.TIMES_TO_DARKEN);
        enemies[enemySelected].getHPBar().resetBrightness();
        enemies[enemySelected].getHPBar().toggleBrightness(b.DARKEN_FACTOR, b.TIMES_TO_DARKEN);
        enemies[newEnemyIndex].getSpritedObject().resetBrightness();
        enemies[newEnemyIndex].getSpritedObject().flash();
        enemies[newEnemyIndex].getSpritedObject().brightenToDefault(b.BRIGHTEN_TO_DEFAULT_FACTOR);
        enemies[newEnemyIndex].getHPBar().resetBrightness();
        enemies[newEnemyIndex].getHPBar().toggleBrightness(b.DARKENED);
        enemies[newEnemyIndex].getHPBar().brightenToDefault(b.BRIGHTEN_TO_DEFAULT_FACTOR);
        enemySelected = newEnemyIndex;
        
    }
    
    
    /**
     * Retrieves options and moves player to highlight the one indicated by
     * the direction pressed.
     * @param direction     Direction pressed
     */
    private void selectOption( int direction ) {
        
        Text[] options = getOptions();
        int numberOfOptions = options.length;
        boolean useShortOptions = numberOfOptions > 3;
        if (optionsEqual(extraOptions)) useShortOptions = true;
        
        int previousOption = optionSelected;
        
        if (state == b.SELECTING_TARGET) {
            numberOfOptions = enemies.length;
        }
        
        if (direction == Game.UP) {
            
            if (state != b.SELECTING_TARGET) {
                if (useShortOptions)
                    optionSelected -= 2;
                else
                    optionSelected--;

                if (optionSelected < 0) optionSelected = 0;
            }
            
        } else if (direction == Game.DOWN) {
            
            if (state != b.SELECTING_TARGET) {
                if (useShortOptions) {
                    if (optionSelected + 2 < numberOfOptions) {
                        optionSelected += 2;
                    }
                } else {
                    optionSelected++;
                }

                if (optionSelected > numberOfOptions - 1)
                    optionSelected = numberOfOptions - 1;
            }
            
        } else if (direction == Game.LEFT) {
            
            if (state == b.SELECTING_TARGET) {
                if (optionSelected - 1 > -1) {
                    optionSelected--;
                }
            } else {
                if (useShortOptions) {
                    if (optionSelected % 2 == 1) {
                        optionSelected--;
                    } else {
                        if (optionsEqual(extraOptions)) {
                            listOptions(initialOptions, false);
                        }
                    }
                } else {
                    optionSelected--;
                    if (optionSelected < 0) optionSelected++;
                }
            }
            
        } else {
            
            if (state == b.SELECTING_TARGET) {
                if (optionSelected + 1 < numberOfOptions) {
                    optionSelected++;
                }
            } else {
                if (useShortOptions) {
                    if (optionSelected == 0 || optionSelected == 2) {
                        optionSelected++;
                    } else {
                        if (optionsEqual(initialOptions)) {
                            listOptions(extraOptions, false);
                        }
                    }
                    while (extraOptions.length != 0 && 
                            optionSelected > extraOptions.length - 1) {
                        optionSelected--;
                    }
                } else {
                    optionSelected++;
                    if (optionSelected > numberOfOptions) {
                        optionSelected--;
                    }
                }
            }
            
        }
        
        if (previousOption != optionSelected) {
            if (state == b.SELECTING_SKILL || state == b.SELECTING_ACT || 
                    state == b.SELECTING_MERCY || state == b.SELECTING_ITEM)
                movePlayerToSelection(optionSelected);

            if (state == b.SELECTING_TARGET) {
                selectEnemy(optionSelected);
                bt.displayFlavorText("Cotton-Stuffed Doll  LV 1 /N A cotton heart and a button eye, you are the apple of my eye.", 
                        true);
            }
        }
        
    }
    
    /**
     * When pressing left/right while choosing an action
     * @param isMovingLeft  True if left was pressed, false if right
     */
    private void selectBattleButton( boolean isMovingLeft ) {
        
        Game.getSound().play("Squeak", false);
        
        DoublySpritedObject[] battleButtons = 
            {fightButton, actButton, itemButton, mercyButton};
        
        int iconSelected = 0;
        
        for (int i = 0; i < battleButtons.length; i++) {
            if (!battleButtons[i].isSpriteA()) {
                iconSelected = i;
                break;
            }
        }
        
        if (isMovingLeft) {
            battleButtons[iconSelected].switchSprite();
            if (iconSelected == 0) {
                player.setX(b.X_PLAYERMERCYBUTTON);
            } else if (iconSelected == 1) {
                player.setX(b.X_PLAYERFIGHTBUTTON);
            } else if (iconSelected == 2) {
                player.setX(b.X_PLAYERACTBUTTON);
            } else {
                player.setX(b.X_PLAYERITEMBUTTON);
            }
            if (iconSelected == 0) {
                battleButtons[3].switchSprite();
            } else {
                battleButtons[iconSelected - 1].switchSprite();
            }
        } else {
            battleButtons[iconSelected].switchSprite();
            if (iconSelected == 0) {
                player.setX(b.X_PLAYERACTBUTTON);
            } else if (iconSelected == 1) {
                player.setX(b.X_PLAYERITEMBUTTON);
            } else if (iconSelected == 2) {
                player.setX(b.X_PLAYERMERCYBUTTON);
            } else {
                player.setX(b.X_PLAYERFIGHTBUTTON);
            }
            if (iconSelected == 3) {
                battleButtons[0].switchSprite();
            } else {
                battleButtons[iconSelected + 1].switchSprite();
            }
        }
        
    }
    
}
