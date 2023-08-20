package game;

import game.gameObjects.Rectangle;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * DialogueHandler
 * @author  Kobe Goodwin
 * @version 8/19/2023
 */
public class DialogueHandler {
    
    public static   int X_DEFAULT = 59,
                        X_FACE = 200,
                        X_INDENT = 236,
                        DELAY_BLINK = 5000,
                        DELAY_RANDOMBLINK = 1000,
                        DELAY_BLINKINCREMENT = 525,
                        DELAY_DIALOGUESOUND = 50;
    
    public static   String TORIEL_NEUTRAL = "1_2";
    public static   String TORIEL_NEUTRAL_BLINK = "3_4_5";
    public static   String TORIEL_SMILE = "6_7";
    public static   String TORIEL_SMILE_BLINK = "8_9_10";
    public static   String TORIEL_THOUGHT = "11_12";
    public static   String TORIEL_THOUGHT_BLINK = "13_14_15";
    public static   String TORIEL_UPSET = "16_17";
    public static   String TORIEL_ANGRY = "18_19";
    public static   String TORIEL_DOTEYES = "21";
    public static   String TORIEL_CONFUSED = "22";
    public static   String TORIEL_FLUSTERED = "23";
    public static   String TORIEL_SQUINT = "24";
    public static   String TORIEL_SAD = "25";
    
    private static Sprite[] decodeFaceGraphic( String character ) {
        
        SpriteSheet TORIEL_TALKING = new SpriteSheet(Game.loadImage("ss\\torieltalking.png"), 50, 36);
        String[] nums = character.split("_");
        Sprite[] toReturn = new Sprite[0];
        for (int i = 0; i < nums.length; i++) {
            Sprite[] newToReturn = new Sprite[toReturn.length + 1];
            for (int j = 0; j < toReturn.length; j++) {
                newToReturn[j] = toReturn[j];
            }
            newToReturn[toReturn.length] = TORIEL_TALKING.getSprites()[Integer.parseInt(nums[i]) - 1];
            toReturn = newToReturn;
        }
        return toReturn;
        
    }
    
    public static Sprite[] getFaceGraphic( String character ) {
        
        switch (character) {
            case "Toriel_Neutral":
                return decodeFaceGraphic(TORIEL_NEUTRAL);
            case "Toriel_Neutral_Blink":
                return decodeFaceGraphic(TORIEL_NEUTRAL_BLINK);
            case "Toriel_Smile":
                return decodeFaceGraphic(TORIEL_SMILE);
            case "Toriel_Smile_Blink":
                return decodeFaceGraphic(TORIEL_SMILE_BLINK);
            case "Toriel_Thought":
                return decodeFaceGraphic(TORIEL_THOUGHT);
            case "Toriel_Thought_Blink":
                return decodeFaceGraphic(TORIEL_THOUGHT_BLINK);
            case "Toriel_Upset":
                return decodeFaceGraphic(TORIEL_UPSET);
            case "Toriel_Angry":
                return decodeFaceGraphic(TORIEL_ANGRY);
            case "Toriel_Doteyes":
                return decodeFaceGraphic(TORIEL_DOTEYES);
            case "Toriel_Confused":
                return decodeFaceGraphic(TORIEL_CONFUSED);
            case "Toriel_Flustered":
                return decodeFaceGraphic(TORIEL_FLUSTERED);
            case "Toriel_Squint":
                return decodeFaceGraphic(TORIEL_SQUINT);
            case "Toriel_Sad":
                return decodeFaceGraphic(TORIEL_SAD);
            default:
                return decodeFaceGraphic(TORIEL_NEUTRAL);
                
            
        }
        
    }
    
    public static ArrayList<DialogueTrigger> parseDialogueFile( String dialoguePath ) {
        
        ArrayList<DialogueTrigger> temp = new ArrayList();
        try {
            Scanner scan = new Scanner(new File(System.getProperty("user.dir") + "\\src\\game\\"+ dialoguePath));
            int[] xywhd = new int[5];
            String[] texts = new String[0];
            String[] faces = new String[0];
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.contains("//")) continue;
                if (line.length() > 0 && line.charAt(0) == '#') {
                    if (texts.length != 0) {
                        temp.add(new DialogueTrigger(new Rectangle(
                            xywhd[0], xywhd[1], xywhd[2], xywhd[3]),
                            texts, faces, xywhd[4]));
                    }
                    String[] splitString = line.substring(2).split(",");
                    for (int i = 0; i < splitString.length; i++) {
                        xywhd[i] = Integer.parseInt(splitString[i]);
                    }
                    texts = new String[0];
                    faces = new String[0];
                } else {
                    if (line.charAt(0) == '>') {
                        faces = Game.addToStringArray(faces, line.substring(2));
                    } else {
                        texts = Game.addToStringArray(texts, line);
                    }
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("Dialogue path not found.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Dialogue path incorrectly composed.");
        }
        return temp;
        
    }
    
}
