package game;

import game.gameObjects.Rectangle;

/**
 * DialogueTrigger
 * @author Kobe Goodwin
 * @version 9/7/2022
 */
public class DialogueTrigger {
    
    private Rectangle interactBox;
    private boolean inputRequired;
    private int interactedNTimes;
    
    public DialogueTrigger( Rectangle interactBox, boolean inputRequired ) {
        
        this.interactBox = interactBox;
        this.inputRequired = inputRequired;
        interactedNTimes = 0;
        
    }
    
}
