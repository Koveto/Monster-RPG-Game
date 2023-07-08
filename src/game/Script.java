package game;

import game.gameObjects.Entity;
import game.gameObjects.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Script
 * @author Kobe Goodwin
 * @version 7/8/2023
 */
public class Script {
    
    private Entity[] entities;
    private Entity current;
    private String scriptPath;
    private int index = 0, delay = 0;
    private long last = 0;
    
    public Script( Entity[] entities, String scriptPath ) {
        
        this.scriptPath = scriptPath;
        this.entities = entities;
        index = parseScriptFile(scriptPath, 0);
        
    }
    
    public void update( ) {
        
        if (index == -1) return;
        if (System.currentTimeMillis() - last < delay) return;
        index = parseScriptFile(scriptPath, index);
        
    }
    
    private int parseScriptFile( String scriptPath, int lineNum ) {
        
        try {
            File f = new File(scriptPath);
            Scanner scan = new Scanner(f);
            
            int i = 0;
            
            while (scan.hasNextLine()) {
                
                if (i++ < lineNum) {
                    scan.nextLine();
                    continue;
                }
                
                delay = 0;
                
                String line = scan.nextLine();
                
                if (line.startsWith("//")) continue;
                if (line.startsWith("Wait")) {
                    last = System.currentTimeMillis();
                    delay = Integer.parseInt(line.substring(5));
                    return i;
                }
                if (line.startsWith("Entity")) {
                    current = entities[Integer.parseInt(String.valueOf(line.charAt(7)))];
                    continue;
                }
                if (line.startsWith("Turn")) {
                    if (current != null) current.turn(Integer.parseInt(String.valueOf(line.charAt(5))));
                    continue;
                }
            }
            
            scan.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Script path not found.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Script path incorrectly composed.");
        }
     
    return -1;
    
    }
    
}
