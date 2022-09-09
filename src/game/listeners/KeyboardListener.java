package game.listeners;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 *
 * @author bluey
 */
public class KeyboardListener implements KeyListener, FocusListener {

    public boolean[] keys = new boolean[120];
    
    @Override
    public void keyTyped(KeyEvent e) {
        //
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode < keys.length) {
            keys[keyCode] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode < keys.length) {
            keys[keyCode] = false;
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        //System.out.println("gained");
    }

    @Override
    public void focusLost(FocusEvent e) {
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
    }
    
    public boolean z() {return keys[KeyEvent.VK_Z] || keys[KeyEvent.VK_ENTER];}
    public boolean x() {return keys[KeyEvent.VK_X] || keys[KeyEvent.VK_SHIFT];}
    
    public boolean up() {return keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];}
    public boolean down() {return keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];}
    public boolean left() {return keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];}
    public boolean right() {return keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];}
    
    public boolean upAndLeft() {return up() && left();}
    public boolean upAndRight() {return up() && right();}
    public boolean downAndLeft() {return down() && left();}
    public boolean downAndRight() {return down() && right();}
    
    public boolean onlyUp() {return up() && !down() && !left() && !right();}
    public boolean onlyDown() {return !up() && down() && !left() && !right();}
    public boolean onlyLeft() {return !up() && !down() && left() && !right();}
    public boolean onlyRight() {return !up() && !down() && !left() && right();}
    
    public boolean noArrowKeys() {return !(up() || down() || left() || right());}
    public boolean noKeys() {return !(up() || down() || left() || right() || z() || x());}
    
}
