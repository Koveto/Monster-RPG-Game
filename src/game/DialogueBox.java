package game;

import game.gameObjects.AnimatedSpritedObject;
import game.gameObjects.GameObject;
import game.gameObjects.Rectangle;
import java.awt.Color;
import java.util.Random;

/**
 * DialogueBox
 * @author Kobe Goodwin
 * @version 7/9/2023
 */
public class DialogueBox {
    
    private AnimatedSpritedObject face;
    private Rectangle inner, outer;
    private Text[] texts;
    private String[] dialogue, faces;
    private String blinkChar;
    private boolean show, usingFace, blinking;
    private int textIndex, blinkCount, blinkTime;
    
    public DialogueBox( ) {
        
        outer = new Rectangle(33, 320, 577, 153, TextHandler.WHITE.getRGB(), 6);
        inner = new Rectangle(33 + 6, 320 + 6, 577 - 12, 153 - 12, TextHandler.BLACK.getRGB());
        face = new AnimatedSpritedObject(DialogueHandler.getFaceGraphic(DialogueHandler.TORIEL_SMILE),
                59, 355, 175, false, true);
        texts = new Text[] {
            new Text("Default flavor text", 200, 370, true, TextHandler.WHITE, TextHandler.DIALOGUE_FONT, "Text", TextHandler.DEFAULT_WRAP, true, 0),
            new Text("Default flavor text", 200, 407, true, TextHandler.WHITE, TextHandler.DIALOGUE_FONT, "Text", TextHandler.DEFAULT_WRAP, true, 0),
            new Text("Default flavor text", 200, 446, true, TextHandler.WHITE, TextHandler.DIALOGUE_FONT, "Text", TextHandler.DEFAULT_WRAP, true, 0)};
        dialogue = new String[0];
        faces = new String[0];
        show = false;
        
    }
    
    public boolean isShowing( ) { return show; }
    
    public boolean finishedScrolling( ) { 
        
        if (texts[2].getMessage().equals(""))
            if (texts[1].getMessage().equals("")) {
                return texts[0].isFinishedScrolling();
            } else {
                return texts[1].isFinishedScrolling();
            }
        return texts[2].isFinishedScrolling(); 
        
    }
    
    public void hide( ) { show = false; }
    
    public boolean progress( ) {
        
        if (dialogue.length <= textIndex + 1) return false;
        textIndex++;
        displayMessage( dialogue[textIndex], faces[textIndex] );
        return true;
        
    }
    
    public void newMessage( String[] dialogue, String[] faces ) {
        
        this.dialogue = dialogue;
        this.faces = faces;
        textIndex = 0;
        blinking = false;
        blinkCount = 0;
        displayMessage( dialogue[0], faces[0] );
        
    }
    
    public void displayMessage( String message, String newFace ) {
        
       if (newFace.equals("None")) {
            for (Text t : texts) {
                t.setX(DialogueHandler.X_DEFAULT);
                t.setWrap(TextHandler.DEFAULT_WRAP);
            }
            face.hide();
            face.pause();
        } else {
            blinkChar = newFace;
            if (blinkChar.equals("Toriel_Neutral") || blinkChar.equals("Toriel_Smile") 
                    || blinkChar.equals("Toriel_Thought")) {
                blinking = true;
                Random r = new Random();
                blinkTime = r.nextInt(DialogueHandler.DELAY_RANDOMBLINK) + DialogueHandler.DELAY_BLINK;
            }
            for (Text t : texts) {
                t.setX(DialogueHandler.X_FACE);
                t.setWrap(TextHandler.INDENT_WRAP);
            }
            face.show();
            face.animate();
            face.setSprites(DialogueHandler.getFaceGraphic(newFace));
        }
        String[] lines = message.split("@");
        if (lines.length == 1) displayMessages( message, "", "");
        else if (lines.length == 2) displayMessages( lines[0], lines[1], "");
        else displayMessages(lines[0], lines[1], lines[2]);
        
    }
    
    private void displayMessages( String message1, String message2, String message3) {
        
        int temp = 0;
        for (String s : new String[] {message1, message2, message3}) {
            if (s.contains(">")) {
                texts[temp].newMessage(s.substring(1));
                texts[temp].setX(DialogueHandler.X_INDENT);
            } else {
                texts[temp].newMessage(s);
            }
            temp++;
        }
        texts[1].setScroll(false);
        texts[2].setScroll(false);
        show = true;
        
    }
    
    public void blink( ) {
        
        blinkCount++;
        if (blinkCount == blinkTime) {
            face.setSprites(DialogueHandler.getFaceGraphic(blinkChar.concat("_Blink")));
        } else if (blinkCount >= blinkTime + DialogueHandler.DELAY_BLINKINCREMENT) {
            if (face.getSpriteIndex() == 2) {
                Random r = new Random();
                blinkTime = r.nextInt(DialogueHandler.DELAY_RANDOMBLINK) + DialogueHandler.DELAY_BLINK;
                blinkCount = 0;
                face.setSprites(DialogueHandler.getFaceGraphic(blinkChar));
            }
        }
        
    }
    
    public GameObject[] getObjects( ) {
        
        if (blinking) blink();
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
