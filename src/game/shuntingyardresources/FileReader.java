package game.shuntingyardresources;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Kobe Goodwin
 * @version 4/9/2022
 * 
 * Reads the lines of a file and parses each line delimited by spaces.
 */
public class FileReader {
    
    /**
     * Divides a text file into an array of Strings for each of its line
     * @param f     File to read from
     * @return      String array of lines
     * @throws FileNotFoundException 
     */
    public static String[] readLinesFromFile( File f ) throws FileNotFoundException {
        
        String[] toReturn = new String[0];
        Scanner fileScan = new Scanner(f);
        while (fileScan.hasNextLine()) {
            String[] temp = new String[toReturn.length + 1];
            for (int i = 0; i < toReturn.length; i++) {
                temp[i] = toReturn[i];
            }
            temp[toReturn.length] = fileScan.nextLine();
            toReturn = temp;
        }
        return toReturn;
        
    }
    
    /**
     * Creates a LinkedQueue from each space delimited token in a line.
     * @param line      A String to parse into tokens
     * @return  LinkedQueue from each space delimited token in a line
     */
    public static LinkedQueue createQueueFromLine( String line ) {
        
        LinkedQueue<String> queue = new LinkedQueue();
        Scanner lineScan = new Scanner(line);
        while (lineScan.hasNext()) {
            queue.enqueue(lineScan.next());
        }
        return queue;
        
    }

}
