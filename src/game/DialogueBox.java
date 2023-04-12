package game;

import game.gameObjects.GameObject;
import game.gameObjects.Rectangle;
import java.awt.Color;

/**
 * DialogueBox
 * @author Kobe Goodwin
 * @version 9/12/2022
 */
public class DialogueBox {
    
    private Rectangle inner, outer;
    private Text asterisk, text;
    private boolean show;
    
    public DialogueBox( ) {
        
        outer = new Rectangle(33, 320, 577, 153, TextHandler.WHITE.getRGB(), 6);
        inner = new Rectangle(33 + 6, 320 + 6, 577 - 12, 153 - 12, TextHandler.BLACK.getRGB());
        asterisk = new Text("*", 59, 375, false, TextHandler.WHITE, TextHandler.DIALOGUE_FONT, TextHandler.SHORT_WRAP, false, 0);
        text = new Text("Default flavor text", 90, 375, true, TextHandler.WHITE, TextHandler.DIALOGUE_FONT, TextHandler.DEFAULT_WRAP, true, 0);
        show = false;
        
    }
    
    public boolean isShowing( ) { return show; }
    
    public boolean finishedScrolling( ) { return text.isFinishedScrolling(); }
    
    public void hide( ) { show = false; }
    
    public void displayMessage( String message ) {
        
        text.newMessage(message);
        show = true;
        
    }
    
    public GameObject[] getObjects( ) {
        
        if (show) return new GameObject[] {inner, outer};
        else return new GameObject[] {};
        
    }
    
    public Text[] getText( ) {
        
        if (show) return new Text[] {asterisk, text};
        else return new Text[] {};
        
    }
    
}
