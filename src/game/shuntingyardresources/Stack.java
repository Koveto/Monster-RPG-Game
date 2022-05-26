package game.shuntingyardresources;

/**
 * Stack
 * @author  Michael T. Goodrich
 * @author  Roberto Tamassia
 * @author  Michael H. Goldwasser
 * @author  Transcribed by Kobe Goodwin
 * @version March 24th, 2022
 * 
 * A collection of objects following last-in-first-out. Transcribed from
 * Data Structures and Algorithms in Java by Michael T. Goodrich, Roberto
 * Tamassia, and Michael H. Goldwasser, Code Fragment 6.1.
 */
public interface Stack<E> {
    
    /**
     * Returns the number of elements in the stack.
     * @return  number of elements in the stack.
     */
    int size( );
    
    /**
     * Tests whether the stack is empty.
     * @return  true if the stack is empty, false otherwise.
     */
    boolean isEmpty( );
    
    /**
     * Inserts an element at the top of the stack.
     * @param e     element to be inserted
     * @throws StackFullException 
     */
    void push(E e);
    
    /**
     * Returns, but does not remove, the element at the top of the stack.
     * @return  top element in the stack, null if empty
     */
    E top( );
    
    /**
     * Removes and returns the top element from the stack.
     * @return  element removed, null if empty
     */
    E pop( );
    
}
