package game;

import game.gameObjects.AnimatedSpritedObject;
import game.gameObjects.DoublySpritedObject;
import game.gameObjects.Enemy;
import game.gameObjects.GameObject;
import game.gameObjects.ImageObject;
import game.gameObjects.PathedAnimatedSpritedObject;
import game.gameObjects.Player;
import game.gameObjects.Rectangle;
import game.gameObjects.SpritedObject;
import java.awt.Color;

/**
 * Battle
 * @author Kobe Goodwin
 * @version 5/26/2022
 * 
 * Handles the properties of a battle.
 */
public class Battle {

    private final BattleHandler b;
    private final BattleText bt;
    
    private final ImageObject backgroundImage;
    private final Rectangle battleRect;
    private final Rectangle[] backgroundRects;
    private final DoublySpritedObject fightButton, actButton, itemButton, mercyButton;
    
    private AnimatedSpritedObject attackAnimation;
    private SpritedObject attackField;
    private PathedAnimatedSpritedObject attackCursor, damageNumber;
    
    private String[] initialOptions, extraOptions;
    private Player player;
    private Enemy[] enemies;
    
    private int state, optionSelected, enemySelected, lastOptionSelected,
            delayStateSwitchMS;
    private long lastStateSwitch;
    
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
        
        this.fightButton = b.newButton(b.FIGHT_BUTTON);
        this.actButton = b.newButton(b.ACT_BUTTON);
        this.itemButton = b.newButton(b.ITEM_BUTTON);
        this.mercyButton = b.newButton(b.MERCY_BUTTON);
        
        this.attackAnimation = new AnimatedSpritedObject(
                b.KNIFE_ATTACK.getSprites(), 
                0, 0, b.DELAY_ATTACKANIMATION);
        this.attackAnimation.hide();
        
        this.attackField = new SpritedObject(
                b.ATTACK_FIELD.getSprite(0, 0), 
                b.X_ATTACKFIELD, b.Y_ATTACKFIELD);
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
        
        this.initialOptions = new String[0];
        this.extraOptions = new String[0];
        
        this.state = b.SELECTING_BATTLE_BUTTON;
        this.lastStateSwitch = System.currentTimeMillis();
        this.delayStateSwitchMS = 500;
        
    }
    
    /**
     * Evaluates if attack animation is playing
     * @return  True if attack animation is playing
     */
    public boolean isAttackAnimationPlaying( ) { return state == b.ATTACK_ANIMATION_PLAYING; }
    
    /**
     * Evaluates if enemy hp bar is sliding down
     * @return  True if enemy is taking damage
     */
    public boolean isEnemyTakingDamage( ) { return state == b.ENEMY_TAKING_DAMAGE; }
    
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
    
    public void interpretButtonPress( int button ) {
        
        if (player.getLastDirection() == button) {
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
                } else {
                    bt.clear();
                    attackField.show();
                    attackCursor.startMoving();
                    resetSelectedEnemy();
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
                attackCursor.stopMoving();
                attackCursor.animate();
                attackAnimation.setX(enemies[enemySelected].getX() + 35);
                attackAnimation.setY(enemies[enemySelected].getY() + 30);
                attackAnimation.animate();
                
            }
            
        } else if (state == b.ATTACK_ANIMATION_PLAYING) {
            
        } else if (state == b.ENEMY_TAKING_DAMAGE) {}
        
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
            backgroundImage, fightButton, actButton, itemButton, mercyButton,
            player, player.getHPBar(), attackField, battleRect, attackCursor};
        GameObject[] objects = new GameObject[temp.length + (2 * enemies.length) + backgroundRects.length + 2];
        for (int i = 0; i < temp.length; i++) {
            objects[i] = temp[i];
        }
        for (int i = 0; i < backgroundRects.length; i++) {
            objects[i + temp.length] = backgroundRects[i];
        }
        for (int i = 0; i < enemies.length; i++) {
            objects[i + temp.length + backgroundRects.length] = enemies[i];
        }
        objects[objects.length - 2 - enemies.length] = damageNumber;
        for (int i = 0; i < enemies.length; i++) {
            objects[
                i + temp.length + backgroundRects.length + enemies.length + 1] 
                    = enemies[i].getHPBar();
        }
        objects[objects.length - 1] = attackAnimation;
        
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
                }
                tempArray[transparentObjects.length] = go;
                transparentObjects = tempArray;
            }
        }
        
        return transparentObjects;
        
    }
    
    public void checkIfAttackAnimationIsFinished( ) {
        
        if (attackAnimation.finished() && System.currentTimeMillis() - 
                lastStateSwitch >= delayStateSwitchMS) {
            state = b.ENEMY_TAKING_DAMAGE;
            int damage = 151;
            enemies[enemySelected].getHPBar().slideToNumerator(
                    enemies[enemySelected].getHP() - damage);
            enemies[enemySelected].hurt();
            
            int x = enemies[enemySelected].getHPBar().getX();
            int y = enemies[enemySelected].getHPBar().getY() - 3 -
                    b.H_SHEET_DAMAGENUMBERS;
            damageNumber.getPath().startAt(x, y);
            damageNumber.setX(x);
            damageNumber.setY(y);
            
            damageNumber.setSprite(getDamageNumberSprite(damage));
            
            damageNumber.show();
            lastStateSwitch = System.currentTimeMillis();
        } else if (!attackAnimation.finished())
            lastStateSwitch = System.currentTimeMillis();
        
    }
    
    /**
     * Retrieves options being displayed as Texts
     * @return  Text array of options
     */
    private Text[] getOptions( ) { return bt.getOptions(); }
    
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
    
    private Sprite getDamageNumberSprite( int damage ) {
        
        if (damage < 10)
            return b.DAMAGE_NUMBERS.getSprite(damage, 0);
        
        String damageString = String.valueOf(damage);
        Sprite[] numbers = new Sprite[damageString.length()];
        for (int i = 0; i < damageString.length(); i++) {
            int x = Integer.parseInt(
                    new String(new char[] {damageString.charAt(i)}));
            numbers[i] = b.DAMAGE_NUMBERS.getSprite(x, 0);
        }
        
        //for (int pixel : numbers[0].getPixels()) System.out.println(pixel);
        int[] pixels = new int[30 * 30 * numbers.length];
        for (int i = 0; i < 30; i++) { // for each row
            int[] row = new int[30 * numbers.length];
            for (int j = 0; j < numbers.length; j++) { // for each number, get row
                int[] subrow = new int[30];
                for (int k = 0; k < subrow.length; k++) {
                    subrow[k] = numbers[j].getPixels()[k + (30 * i)];
                }
                for (int k = 0; k < subrow.length; k++) {
                    row[(j * 30) + k] = subrow[k];
                }
            }
            //for (int r : row) System.out.println("row " + i + ": " + r);
            for (int k = 0; k < row.length; k++) {
                pixels[(i * row.length) + k] = row[k];
            }
        }
        return new Sprite(pixels, 30 * numbers.length, 30);
        
    }
    
    private void selectTarget( boolean returning ) {
        
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
                    enemies[i].getSpritedObject().toggleBrightness(b.DARKENED);
                }
            }
        if (!returning)
            optionSelected = 0;
        else {
            optionSelected = enemySelected;
        }
        
    }
    
    private void highlightSelectedEnemy( ) {
        
        enemies[enemySelected].getSpritedObject().brightenToDefault(b.BRIGHTEN_TO_DEFAULT_FACTOR);
        enemies[enemySelected].getSpritedObject().stopFlashing();
        
    }
    
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
     * When pressing Z on one of the Battle Buttons
     */
    private void pressBattleButton( ) {
        
        player.getSpritedObject().show();
        
        resetEnemyEffects();
        int buttonSelected = getButtonSelected();
        
        if (buttonSelected == b.FIGHT_BUTTON) {
            
            state = b.SELECTING_SKILL;
            listOptions(new String[] {"/AIce Age", "/RInferno", "/YThunder Reign", "/GZandyne"}, true);
            
        } else if (buttonSelected == b.ACT_BUTTON) {
            
            selectTarget(false);
            
        } else if (buttonSelected == b.ITEM_BUTTON) {
            
            state = b.SELECTING_ITEM;
            listOptions(new String[] {"Bandage", "Spider Donut", "Spider Cider", "Bscotch Pie", "Snail Pie", "Snow Piece", "Nice Cream", "Glamburger"}, false);
            
        } else if (buttonSelected == b.MERCY_BUTTON) {
            
            state = b.SELECTING_MERCY;
            listOptions(new String[] {"Spare", "Flee"}, false);
            
        }
        
    }
    
    /**
     * When pressing X while choosing a target to fight
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
    
    private void resetEnemyEffects( ) {
        
        resetEnemyEffects( -1 );
        
    }
    
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
    
    private void selectEnemy( int newEnemyIndex ) {
        
        enemies[enemySelected].getSpritedObject().resetBrightness();
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
