package game;

/**
 * DialogueHandler
 * @author  Kobe Goodwin
 * @version 6/23/2023
 */
public class DialogueHandler {
    
    public static   int X_DEFAULT = 59,
                        X_FACE = 200,
                        X_INDENT = 236;
    
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
    
}
