package game;

import game.gameObjects.DoublySpritedObject;

/**
 * BattleHandler
 * @author Kobe Goodwin
 * @version 8/31/2022
 * 
 * Helper class containing constants related to battles.
 */
public class BattleHandler {
    
    public final int            SELECTING_BATTLE_BUTTON = 0, 
                                SELECTING_TARGET = 1,
                                SELECTING_SKILL = 2,
                                SELECTING_ACT = 3,
                                SELECTING_ITEM = 4,
                                SELECTING_MERCY = 5,
                                ATTACKING = 6,
                                ATTACK_ANIMATION_PLAYING = 7,
                                ENEMY_TAKING_DAMAGE = 8,
                                BETWEEN_TURNS = 9,
                                ENEMY_TURN = 10,
                                ENEMY_TURN_FINISHED = 11,
                                BATTLE_END = 12,

                                FIGHT_BUTTON = 0,
                                ACT_BUTTON = 1,
                                ITEM_BUTTON = 2,
                                MERCY_BUTTON = 3,
            
                                PLAYER_SPEED = 3,

                                X_PLAYERFIGHTBUTTON = 39,
                                X_PLAYERACTBUTTON = 196,
                                X_PLAYERITEMBUTTON = 350,
                                X_PLAYERMERCYBUTTON = 505,
                                X_PLAYEROPTIONCOLUMN1 = 60,
                                X_PLAYEROPTIONCOLUMN2 = 320,
                                X_PLAYERBATTLEBOX = 310,
                                X_BATTLERECT = 32,
                                X_FIGHTBUTTON = X_BATTLERECT,
                                X_ACTBUTTON = 188,
                                X_ITEMBUTTON = 343,
                                X_MERCYBUTTON = 499,
                                X_ATTACKFIELD = X_BATTLERECT + 16,
                                X_ATTACKCURSOR = -150,
                                X_NAMEANDLEVEL = X_BATTLERECT,
                                X_HPLABEL = 246,
                                X_HPFRACTION = 316,
                                X_ASTERISK = 59,
                                X_FLAVORTEXT = 90,
                                X_OPTIONCOLUMN1 = 98,
                                X_OPTIONCOLUMN2 = 350,
                                X_PAGENUMBER = 379,
                                X_BUBBLEOFFSET = 22,

                                Y_PLAYERBUTTONS = 444,
                                Y_PLAYEROPTIONROW1 = 281,
                                Y_PLAYEROPTIONROW2 = 312,
                                Y_PLAYEROPTIONROW3 = 340,
                                Y_PLAYERBATTLEBOX = 310,
                                Y_BATTLERECT = 249,
                                Y_BUTTONS = 431,
                                Y_ATTACKFIELD = Y_BATTLERECT + 14,
                                Y_ATTACKCURSOR = 640,
                                Y_NAMEANDLEVEL = 419,
                                Y_HPLABEL = 416,
                                Y_HPFRACTION = 418,
                                Y_OPTIONROW1 = 298,
                                Y_OPTIONROW2 = 329,
                                Y_OPTIONROW3 = 360,
                                Y_BUBBLEOFFSET = 78,
            
                                W_BATTLERECT = 577,
                                W_BATTLERECTBORDER = 6,
                                W_SHEET_BUTTONS = 110,
                                W_SHEET_KNIFEATTACK = 16,
                                W_SHEET_ATTACKFIELD = 546,
                                W_SHEET_ATTACKCURSORS = 14,
                                W_SHEET_DAMAGENUMBERS = 31,
                                W_SHEET_CHATBOXES = 237,
                                W_SHEET_BULLET = 4,
                                W_BATTLERECTINCREMENT = 5,
            
                                H_BATTLERECT = 141,
                                H_SHEET_BUTTONS = 42,
                                H_SHEET_KNIFEATTACK = 66,
                                H_SHEET_ATTACKFIELD = 115,
                                H_SHEET_ATTACKCURSORS = 128,
                                H_SHEET_DAMAGENUMBERS = 30,
                                H_SHEET_CHATBOXES = 216,
                                H_SHEET_BULLET = 4,

                                TIMES_TO_DARKEN = 6,
                                TIMES_TO_BRIGHTEN = 6,
                                
                                DELAY_ATTACKANIMATION = 100,
                                DELAY_ATTACKCURSORPATH = 7,
                                DELAY_ATTACKCURSORANIM = 100,
                                DELAY_ATTACKTOTAKINGDAMAGE = 500,
                                DELAY_TURNENDTOHPFADE = 520,
                                DELAY_TURNENDTODEATH = 500,
                                DELAY_DAMAGEDTOCHATBOX = 500,

                                FADE_OUT_SPEED_NORMAL = 50,
                                FADE_OUT_SPEED_FAST = 150;
    
    public final float              DARKENED = 0.1F,
                                    DARKEN_FACTOR = 0.7F,
                                    BRIGHTEN_FACTOR = 1.5F,
                                    BRIGHTEN_TO_DEFAULT_FACTOR = 1.15F,
                                    DEATH_FADE_OUT_FACTOR = 0.9F,
                                    ATTACK_FIELD_DARKEN_FACTOR = 0.85F;
    
    public final String         PATH_BACKGROUNDIMAGE = "sprites\\bgd1.png",
                                PATH_BATTLEBUTTONS = "ss\\buttons.png",
                                PATH_KNIFEATTACK = "ss\\knifeAttack.png",
                                PATH_ATTACKFIELD = "ss\\attackFields.png",
                                PATH_ATTACKCURSORS = "ss\\attackCursors.png",
                                PATH_DAMAGENUMBERS = "ss\\damageNums.png",
                                PATH_CHATBOXES = "ss\\chatboxes.png",
                                PATH_BULLET = "ss\\bullet.png",
            
                                TEXT_DEFAULTFLAVOR = "Negative b plus or minus the /Bsquare root of b squared/B minus four a c all over two a.";
    
    public final SpriteSheet            BATTLE_BUTTONS = new SpriteSheet(
                                Game.loadImage(PATH_BATTLEBUTTONS), 
                                W_SHEET_BUTTONS, H_SHEET_BUTTONS),
            
                                        KNIFE_ATTACK = new SpriteSheet(
                                Game.loadImage(PATH_KNIFEATTACK), 
                                W_SHEET_KNIFEATTACK, H_SHEET_KNIFEATTACK),
    
                                        ATTACK_FIELD = new SpriteSheet(
                                Game.loadImage(PATH_ATTACKFIELD), 
                                W_SHEET_ATTACKFIELD, H_SHEET_ATTACKFIELD),
    
                                        ATTACK_CURSORS = new SpriteSheet(
                                Game.loadImage(PATH_ATTACKCURSORS), 
                                W_SHEET_ATTACKCURSORS, H_SHEET_ATTACKCURSORS),
                                
                                        DAMAGE_NUMBERS = new SpriteSheet(
                                Game.loadImage(PATH_DAMAGENUMBERS),
                                W_SHEET_DAMAGENUMBERS, H_SHEET_DAMAGENUMBERS),
            
                                        TEXT_BUBBLES = new SpriteSheet(
                                Game.loadImage(PATH_CHATBOXES),
                                W_SHEET_CHATBOXES, H_SHEET_CHATBOXES),
            
                                        BULLET = new SpriteSheet(
                                Game.loadImage(PATH_BULLET),
                                W_SHEET_BULLET, H_SHEET_BULLET);
    
    public DoublySpritedObject newButton( int button ) {
        
        int[] xPositions = new int[] {
                    X_FIGHTBUTTON, X_ACTBUTTON, X_ITEMBUTTON, X_MERCYBUTTON };
        
        return new DoublySpritedObject(
                BATTLE_BUTTONS.getSprite(button, 0), 
                BATTLE_BUTTONS.getSprite(button, 1), 
                xPositions[button], Y_BUTTONS, button != FIGHT_BUTTON);
        
    }
    
    public Text newLongSelection( int number ) {
        
        int[] rows = new int[] {Y_OPTIONROW1, Y_OPTIONROW2, Y_OPTIONROW3};
        return new Text("", X_OPTIONCOLUMN1, rows[number - 1], 
                false, TextHandler.WHITE, TextHandler.DIALOGUE_FONT, 
                TextHandler.DEFAULT_WRAP, true, 0);
        
    }
    
    public Text newShortSelection( int number ) {
        
        int[] rows = new int[] {Y_OPTIONROW1, Y_OPTIONROW2, Y_OPTIONROW3};
        int[] columns = new int[] {X_OPTIONCOLUMN1, X_OPTIONCOLUMN2};
        
        int column = 0, row = 0;
        if (number % 2 == 0) {
            column = 1;
        }
        if (number > 4)
            row = 2;
        else if (number > 2)
            row = 1;
            
        return new Text("", columns[column], rows[row], 
                false, TextHandler.WHITE, TextHandler.DIALOGUE_FONT, 
                TextHandler.SHORT_WRAP, true, 0);
    }
    
}
