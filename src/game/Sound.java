package game;

import java.io.File;
import java.util.Scanner;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;

/**
 * Sound
 * @author Kobe Goodwin
 * @version 6/26/2023
 */
public class Sound {
    
    private Clip clip;
    
    public Sound( ) {}
    
    private void setFile( String path ) {
        
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(System.getProperty("user.dir") + "\\src\\game\\sound\\" + path));
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private String getSongPath( String name ) {
        
        try {
            File music = new File("C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\sound\\music.txt");
            Scanner scan = new Scanner(music);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] divided = line.split(",");
                if (divided[1].toLowerCase().equals(name.toLowerCase())) return divided[2];
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        
    }
    
    private String getSongPath( int id ) {
        
        try {
            File music = new File("C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\sound\\music.txt");
            Scanner scan = new Scanner(music);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] divided = line.split(",");
                if (Integer.parseInt(divided[0]) == id) return divided[2];
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        
    }
    
    public void play( String name, boolean loop ) { 
        
        setFile(getSongPath(name));
        clip.start();
        if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
        
    }
    
    public void play ( int id, boolean loop ) {
        
        setFile(getSongPath(id));
        clip.start();
        if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
        
    }
    
    public void stop( ) { clip.stop(); }
    
}
