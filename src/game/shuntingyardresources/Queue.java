package game.shuntingyardresources;

/**
 * Queue
 * @author  Michael T. Goodrich
 * @author  Roberto Tamassia
 * @author  Michael H. Goldwasser
 * @author  Transcribed by Kobe Goodwin
 * @version March 24th, 2022
 * 
 * A collection of objects following first-in-first-out. Transcribed from
 * Data Structures and Algorithms in Java by Michael T. Goodrich, Roberto
 * Tamassia, and Michael H. Goldwasser, Code Fragment 6.9.
 */
public interface Queue<E> {
    
    /**
     * Returns the number of elements in the queue.
     * @return  number of elements in the queue.
     */
    int size( );
    
    /**
     * Tests whether the queue is empty.
     * @return  true if the queue is empty, false otherwise.
     */
    boolean isEmpty( );
    
    /**
     * Inserts an element at the rear of the queue.
     * @param e     Element to insert
     * @throws  QueueFullException if queue is full.
     */
    void enqueue( E e );
    
    /**
     * Returns, but does not remove, the first element of the queue (null if
     * empty).
     * @return  First element in the queue. 
     */
    E first( );
    
    /**
     * Removes and returns the first element of the queue (null if empty).
     * @return  First element in the queue that was removed.
     */
    E dequeue( );
    
}
