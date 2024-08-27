package game;

import java.io.File;
import java.util.Scanner;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;

/**
 * Sound
 * A single audio track to play music or sound effects to.
 * @author Kobe Goodwin
 * @version 8/27/2024
 */
public class Sound {

    private final String MUSIC_PATH = "C:\\Users\\bluey\\OneDrive\\Documents\\NetBeansProjects\\smt\\src\\game\\sounds\\a_music.txt";
    private Clip clip;
    private String name;
    private int id;
    
    /**
     * Constructor. Creates a audio track to play music or sound effects to.
     */
    public Sound( ) {
        name = null;
        id = -1;
    }
    
    /**
     * Changes the file the clip is using
     * @param path  Path of new file
     */
    private void setFile( String path ) {
        
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(System.getProperty("user.dir") + "\\src\\game\\sounds\\" + path));
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * Retrieves the path of a song from its name specified in MUSIC_PATH
     * @param name  The name of a song in MUSIC_PATH, such as "Ruins"
     * @return      The path of the song
     */
    private String getSongPath( String name ) {
        
        this.name = name;
        this.id = -1;
        try {
            File music = new File(MUSIC_PATH);
            Scanner scan = new Scanner(music);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] divided = line.split(",");
                if (divided[1].toLowerCase().equals(name.toLowerCase())) {
                    scan.close();
                    return divided[2];
                }
            }
            scan.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        
    }
    
    /**
     * Retrieves the path of a song from an ID, specified in MUSIC_PATH.
     * @param id    ID as specified in MUSIC_PATH
     * @return      path of music file
     */
    private String getSongPath( int id ) {
        
        this.name = null;
        this.id = id;
        try {
            File music = new File(MUSIC_PATH);
            Scanner scan = new Scanner(music);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] divided = line.split(",");
                if (Integer.parseInt(divided[0]) == id) {
                    scan.close();
                    return divided[2];
                }
            }
            scan.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Using its name as specified in MUSIC_PATH, plays a song
     * from the very start. Ability to loop the music.
     * @param name      Name of song, as specified in MUSIC_PATH
     * @param loop      True if music loops
     */
    public void play( String name, boolean loop ) { 
        
        if (name == null) return;
        setFile(getSongPath(name));
        clip.start();
        if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
        
    }
    
    /**
     * Using its id as specified in MUSIC_PATH, plays a song
     * from the very start. Ability to loop the music.
     * @param id        Id of song, as specified in MUSIC_PATH
     * @param loop      True if music loops
     */
    public void play ( int id, boolean loop ) {
        
        setFile(getSongPath(id));
        clip.start();
        if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
        
    }

    /**
     * Accessor for the name of the sound, as specified
     * in MUSIC_PATH. If the sound was retrieved using
     * its ID, returns null.
     * @return  Name of sound
     */
    public String getName( ) { return name; }

    /**
     * Accessor for the id of the sound, as specified
     * in MUSIC_PATH. If the sound was retrieved using
     * its name, returns -1.
     * @return  ID of sound
     */
    public int getID( ) { return id; }
    
    /**
     * Stops the music.
     */
    public void stop( ) { clip.stop(); }
    
    /**
     * Resumes the music.
     */
    public void resume( ) { clip.start(); }

    /**
     * Compares Object to Sound. Returns True if they are playing
     * the song with the same path.
     * @param o Object to compare to.
     * @return  Boolean evaluating if the message is the same.
     */
    public boolean equals( Object o ) {
        
        if (!(o instanceof Sound)) return false;
        Sound s = (Sound) o;
        String path1 = "";
        String path2 = "";
        if (s.getID() == -1)
            path1 = s.getSongPath(s.getName());
        else
            path1 = s.getSongPath(s.getID());
        if (this.getID() == -1)
            path2 = this.getSongPath(this.getName());
        else
            path2 = this.getSongPath(this.getID());
        return path1.equals(path2);

    /**
     * Describes Sound.
     * @return  String describing Sound.
     */
    public String toString( ) {
        
        if (name != null)
            return "Sound object playing song with path:  " + getSongPath(name) + 
            ".\n Is it running? " + String.valueOf(clip.isRunning());
        else
            return "Sound object playing song with path: " + getSongPath(id) +
            ".\n Is it running? " + String.valueOf(clip.isRunning());
        
    }
    
}
