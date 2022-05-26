package game;

import game.gameObjects.Player;

/**
 * BattleText
 * @author Kobe Goodwin
 * @version 4/19/2022
 * 
 * A helper class that stores and alters the Texts used in a Battle.
 */
public class BattleText {
    
    private final Text nameAndLevel, hpLabel, hpFraction, asterisk, flavorText, 
            longSelection1, longSelection2, longSelection3, shortSelection1,
            shortSelection2, shortSelection3, shortSelection4, shortSelection5,
            shortSelection6, pageNumber;
    
    private Text[] textsDisplayed;
    
    /**
     * Constructor
     * @param b         BattleHandler
     * @param player    Player
     */
    public BattleText( BattleHandler b, Player player ) {
        
        this.nameAndLevel = new Text(
                player.getName() + "  LV " + String.valueOf(player.getLevel()), 
                b.X_NAMEANDLEVEL, 
                b.Y_NAMEANDLEVEL, 
                false, TextHandler.WHITE, TextHandler.MARS);
        this.hpLabel = new Text("HP", 
                b.X_HPLABEL, 
                b.Y_HPLABEL, 
                false, TextHandler.WHITE, TextHandler.MARS_SMALL);
        this.hpFraction = new Text(String.valueOf(player.getHP()) + " / " + 
                String.valueOf(player.getMaxHP()), 
                b.X_HPFRACTION, 
                b.Y_HPFRACTION, 
                false, TextHandler.WHITE, TextHandler.MARS);
        this.asterisk = new Text("*", 
                b.X_ASTERISK, b.Y_OPTIONROW1, 
                false, TextHandler.WHITE, TextHandler.DIALOGUE_FONT);
        this.flavorText = new Text(b.TEXT_DEFAULTFLAVOR, 
                b.X_FLAVORTEXT, b.Y_OPTIONROW1, 
                true, TextHandler.WHITE, TextHandler.DIALOGUE_FONT, 
                TextHandler.DEFAULT_WRAP, true); // scrolls, doubles spaces
        this.longSelection1 = b.newLongSelection(1);
        this.longSelection2 = b.newLongSelection(2);
        this.longSelection3 = b.newLongSelection(3);
        this.shortSelection1 = b.newShortSelection(1);
        this.shortSelection2 = b.newShortSelection(2);
        this.shortSelection3 = b.newShortSelection(3);
        this.shortSelection4 = b.newShortSelection(4);
        this.shortSelection5 = b.newShortSelection(5);
        this.shortSelection6 = b.newShortSelection(6);
        this.pageNumber = new Text("", 
                b.X_PAGENUMBER, b.Y_OPTIONROW3, 
                false, TextHandler.WHITE, TextHandler.DIALOGUE_FONT, 
                TextHandler.SHORT_WRAP, true);
        
        displayFlavorText(b.TEXT_DEFAULTFLAVOR, true);
        
    }
    
    /**
     * Lists short selections 1-6. Selection 1 is row 1, column 1. Selection
     * 2 is row 1, column 2.
     * @return  Array of short selections
     */
    public Text[] getShortSelections( ) {
        
        return new Text[] {shortSelection1, shortSelection2, shortSelection3,
            shortSelection4, shortSelection5, shortSelection6};
        
    }
    
    /**
     * Lists long selections 1-3, which are listed by column.
     * @return  Array of long selections
     */
    public Text[] getLongSelections( ) {
        
        return new Text[] {longSelection1, longSelection2, longSelection3};
        
    }
    
    /**
     * Retrieves all Texts to render.
     * @return  Array of Texts
     */
    public Text[] getText( ) {
        
        Text[] toReturn = new Text[textsDisplayed.length + 3];
        for (int i = 0; i < textsDisplayed.length; i++) {
            toReturn[i] = textsDisplayed[i];
        }
        toReturn[textsDisplayed.length] = nameAndLevel;
        toReturn[textsDisplayed.length + 1] = hpLabel;
        toReturn[textsDisplayed.length + 2] = hpFraction;
        return toReturn;
        
    }
    
    /**
     * Lists n number of Text selections. If n is greater than 3, short selections
     * will be used. Otherwise, long selections will be used. If n is greater
     * than 6, 
     * @param n Number of selections
     * @return  Array of n selections
     */
    public Text[] getNSelections( int n ) {
        
        return getNSelections(n, n > 3);
        
    }
    
    /**
     * Lists n number of Text selections.
     * @param n                     Number of selections
     * @param useShortSelections    True to use short, false to use long
     * @return  Array of n selections
     */
    public Text[] getNSelections( int n, boolean useShortSelections ) {
        
        Text[] temp = new Text[n];
        if (!useShortSelections) {
            Text[] longs = getLongSelections();
            for (int i = 0; i < n; i++) {
                temp[i] = longs[i];
            }
        } else if (useShortSelections) {
            
            Text[] shorts = getShortSelections();
            for (int i = 0; i < n; i++) {
                temp[i] = shorts[i];
            }
            
        }
        return temp;
        
    }
    
    /**
     * Retrieves options being displayed as Texts
     * @return  Text array of options
     */
    public Text[] getOptions( ) {
        
        Text[] options = new Text[0];
        for (int i = 0; i < textsDisplayed.length; i++) {
            
            if (!textsDisplayed[i].getMessage().equals("")
                    && !textsDisplayed[i].getMessage().contains("PAGE")) {
                
                Text[] newTemp = new Text[options.length + 1];
                for (int j = 0; j < options.length; j++) {
                    newTemp[j] = options[j];
                }
                newTemp[options.length] = textsDisplayed[i];
                options = newTemp;
                
            }
                
        }
        return options;
        
    }
    
    /**
     * Clears all Texts from the screen, except for ones used in the UI such
     * as player name and health label.
     */
    public void clear( ) { 
        textsDisplayed = new Text[0];
    }
    
    /**
     * Displays an array of Texts to the screen.
     * @param texts     Texts to display
     */
    public void displayTexts( Text[] texts ) { 
        textsDisplayed = texts;
    }
    
    /**
     * Displays flavor text.
     * @param newFlavorText     New message to display, null to keep current msg.
     * @param scrollText        True to scroll text, false to not
     */
    public void displayFlavorText( String newFlavorText, boolean scrollText ) {
        
        String message = flavorText.getMessage();
        if (newFlavorText != null)
            flavorText.newMessage(newFlavorText);
        else
            flavorText.newMessage(message);
        asterisk.newMessage("*");
        textsDisplayed = new Text[] {asterisk, flavorText};
        
    }
    
    /**
     * Given a Text array, adds the Page Number Text object to the end of it.
     * @param pageNumber    Number of page
     * @param array         Array to append to
     * @return              New array with page number appended
     */
    public Text[] appendPageNumberToTextArray( Integer pageNumber, Text[] array ) {
        
        setPageNumber(pageNumber);
        return Game.addToTextArray(array, this.pageNumber);
        
    }
    
    /**
     * Retrieves the page number.
     * @return  Page number, 0 if there is no second page.
     */
    public int getPageNumber( ) {
        
        if (pageNumber.getMessage().length() == 0)
            return 0;
        return Integer.parseInt(pageNumber.getMessage().substring(6, 7));
        
    }
    
    public void setPageNumber( Integer pageNumber ) {
        
        if (pageNumber != null)
            this.pageNumber.newMessage("PAGE " + pageNumber);
        
    }
    
}
