package game;

import game.gameObjects.AnimatedSpritedObject;
import game.gameObjects.GameObject;
import game.gameObjects.Rectangle;
import java.awt.Color;
import java.util.Random;

/**
 * DialogueBox
 * @author Kobe Goodwin
 * @version 8/25/2024
 */
public class DialogueBox {
    
    private AnimatedSpritedObject face;
    private Rectangle inner, outer;
    private Text[] texts;
    private String[] dialogue, faces, sounds;
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
        sounds = new String[0];
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
        int temp = textIndex;
        while (temp >= faces.length) temp--;
        displayMessage( dialogue[textIndex], faces[temp], sounds[temp] );
        
        return true;
        
    }
    
    public void newMessage( String[] dialogue, String[] faces, String[] sounds ) {
        
        this.dialogue = dialogue;
        this.faces = faces;
        this.sounds = sounds;
        textIndex = 0;
        blinking = false;
        blinkCount = 0;
        displayMessage( dialogue[0], faces[0], sounds[0] );
        
    }
    
    public void displayMessage( String message, String newFace, String sound ) {
        
       if (newFace.equals("None")) {
            for (Text t : texts) {
                t.setX(DialogueHandler.X_DEFAULT);
                t.setWrap(TextHandler.DEFAULT_WRAP);
                t.setSound(sound);
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
                t.setWrap(TextHandler.FACE_WRAP);
                t.setSound(sound);
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
            Random r = new Random();
            blinkTime = r.nextInt(DialogueHandler.DELAY_RANDOMBLINK) + DialogueHandler.DELAY_BLINK;
            blinkCount = 0;
            face.setSprites(DialogueHandler.getFaceGraphic(blinkChar));
        }
        
    }

    public void switchVertical( boolean toTopOfScreen ) {
        if (toTopOfScreen) {
            int FROM_TOP = 9;
            outer.setY(FROM_TOP);
            inner.setY(FROM_TOP + 6);
            face.setY(FROM_TOP + 35);
            texts[0].setY(FROM_TOP + 50);
            texts[1].setY(FROM_TOP + 50 + 37);
            texts[2].setY(FROM_TOP + 50 + 37 + 39);
        } else {
            outer.setY(320);
            inner.setY(320 + 6);
            face.setY(320 + 35);
            texts[0].setY(320 + 50);
            texts[1].setY(320 + 50 + 37);
            texts[2].setY(320 + 50 + 37 + 39);
        }
    }

    public Rectangle getOuterRect( ) { return outer; }
    
    public GameObject[] getObjects( ) {
        
        inner.setX(RenderHandler.getCamera().getX() + 33 + 6);
        outer.setX(RenderHandler.getCamera().getX() + 33);
        face.setX(RenderHandler.getCamera().getX() + 59);
        if (blinking) blink();
        if (show) return new GameObject[] {inner, outer, face};
        else return new GameObject[] {};
        
    }
    
    public Text[] getText( ) {
        
        if (show) {
            if (texts[0].isFinishedScrolling() && !texts[1].isFinishedScrolling()) {
                texts[1].setScroll(true);
                return new Text[] {texts[0], texts[1]};
            } 
            if (texts[0].isFinishedScrolling() && texts[1].isFinishedScrolling()) {
                texts[2].setScroll(true);
                return texts;
            }
            return new Text[] {texts[0]};
            
        }
        else return new Text[] {};
        
    }
    
}
