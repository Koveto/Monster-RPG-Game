package game;

import game.gameObjects.AnimatedSpritedObject;
import game.gameObjects.GameObject;
import game.gameObjects.Rectangle;
import java.awt.Color;

/**
 * DialogueBox
 * @author Kobe Goodwin
 * @version 6/23/2023
 */
public class DialogueBox {
    
    private AnimatedSpritedObject face;
    private Rectangle inner, outer;
    private Text[] texts;
    private boolean show;
    
    public DialogueBox( ) {
        
        SpriteSheet TORIEL_TALKING = new SpriteSheet(Game.loadImage("ss\\torieltalking.png"), 50, 36);
        outer = new Rectangle(33, 320, 577, 153, TextHandler.WHITE.getRGB(), 6);
        inner = new Rectangle(33 + 6, 320 + 6, 577 - 12, 153 - 12, TextHandler.BLACK.getRGB());
        face = new AnimatedSpritedObject(TORIEL_TALKING.getSprites(), 59, 370, 500, false, true);
        face.animate();
        texts = new Text[] {
            new Text("Default flavor text", 59, 370, true, TextHandler.WHITE, TextHandler.DIALOGUE_FONT, TextHandler.DEFAULT_WRAP, true, 0),
            new Text("Default flavor text", 59, 407, true, TextHandler.WHITE, TextHandler.DIALOGUE_FONT, TextHandler.DEFAULT_WRAP, true, 0),
            new Text("Default flavor text", 59, 446, true, TextHandler.WHITE, TextHandler.DIALOGUE_FONT, TextHandler.DEFAULT_WRAP, true, 0)};
        show = false;
        
    }
    
    public boolean isShowing( ) { return show; }
    
    public boolean finishedScrolling( ) { return texts[2].isFinishedScrolling(); }
    
    public void hide( ) { show = false; }
    
    public void displayMessage( String message ) {
        
        String[] lines = message.split("@");
        if (lines.length == 1) displayMessages( message, "", "");
        else if (lines.length == 2) displayMessages( lines[0], lines[1], "");
        else displayMessages(lines[0], lines[1], lines[2]);
    }
    
    public void displayMessages( String message1, String message2, String message3) {
        
        texts[0].newMessage(message1);
        texts[1].newMessage(message2);
        texts[2].newMessage(message3);
        texts[1].setScroll(false);
        texts[2].setScroll(false);
        show = true;
        
    }
    
    public GameObject[] getObjects( ) {
        
        if (show) return new GameObject[] {inner, outer, face};
        else return new GameObject[] {};
        
    }
    
    public Text[] getText( ) {
        
        if (show) {
            if (texts[1].isFinishedScrolling()) {
                texts[2].setScroll(true);
                return texts;
            }
            if (texts[0].isFinishedScrolling()) {
                texts[1].setScroll(true);
                return new Text[] {texts[0], texts[1]};
            } 
            return new Text[] {texts[0]};
            
        }
        else return new Text[] {};
        
    }
    
}
