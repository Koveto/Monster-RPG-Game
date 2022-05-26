package game.shuntingyardresources;

/**
 * LinkedStack
 * @author  Michael T. Goodrich
 * @author  Roberto Tamassia
 * @author  Michael H. Goldwasser
 * @author  Transcribed by Kobe Goodwin
 * @version March 24th, 2022
 * 
 * A SinglyLinkedList of elements following last-in-first-out. Transcribed from
 * Data Structures and Algorithms in Java by Michael T. Goodrich, Roberto
 * Tamassia, and Michael H. Goldwasser, Code Fragment 6.4.
 */
public class LinkedStack<E> implements Stack<E> {
    
    private SinglyLinkedList<E> list = new SinglyLinkedList( );
    
    /**
     * Default Constructor
     */
    public LinkedStack( ) {}
    
    /**
     * Returns the number of elements in the stack.
     * @return  number of elements in the stack.
     */
    public int size( ) {return list.size();}
    
    /**
     * Tests whether the stack is empty.
     * @return  true if the stack is empty, false otherwise.
     */
    public boolean isEmpty( ) {return list.isEmpty();}
    
    /**
     * Inserts an element at the top of the stack.
     * @param e     element to be inserted
     * @throws StackFullException 
     */
    public void push( E element ) {
        list.addFirst(element); 
    }
    
    /**
     * Returns, but does not remove, the element at the top of the stack.
     * @return  top element in the stack, null if empty
     */
    public E top( ) { return list.first(); }
    
    /**
     * Removes and returns the top element from the stack.
     * @return  element removed, null if empty
     */
    public E pop( ) { return list.removeFirst(); }
    
}
