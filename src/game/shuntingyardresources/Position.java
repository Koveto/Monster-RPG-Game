package game.shuntingyardresources;

/**
 * Position
 * @author Kobe Goodwin
 * @version 4/1/2022
 * 
 * An encapsulated element to be stored.
 */
public interface Position<E> {
    
    /**
     * Returns the element stored at this position.
     * 
     * @return  the stored element
     * @throws  IllegalStateException 
     */
    E getElement( ) throws IllegalStateException;
    
}
