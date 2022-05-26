package game.shuntingyardresources;

/**
 * LinkedQueue
 * @author  Michael T. Goodrich
 * @author  Roberto Tamassia
 * @author  Michael H. Goldwasser
 * @author  Transcribed by Kobe Goodwin
 * @version March 24th, 2022
 * 
 * A SinglyLinkedList of objects following first-in-first-out. Transcribed from
 * Data Structures and Algorithms in Java by Michael T. Goodrich, Roberto
 * Tamassia, and Michael H. Goldwasser, Code Fragment 6.11.
 */
public class LinkedQueue<E> implements Queue<E> {

    private SinglyLinkedList<E> list = new SinglyLinkedList( );
    
    /**
     * Default constructor
     */
    public LinkedQueue( ) {}
    
    /**
     * Returns the number of elements in the queue.
     * @return  number of elements in the queue.
     */
    @Override
    public int size() { return list.size(); }

    /**
     * Tests whether the queue is empty.
     * @return  true if the queue is empty, false otherwise.
     */
    @Override
    public boolean isEmpty() { return list.isEmpty(); }

    /**
     * Inserts an element at the rear of the queue.
     * @param e     Element to insert
     * @throws  QueueFullException if queue is full.
     */
    @Override
    public void enqueue(E element) {
        list.addLast(element);
    }

    /**
     * Returns, but does not remove, the first element of the queue (null if
     * empty).
     * @return  First element in the queue. 
     */
    @Override
    public E first( ) { return list.first(); }

    /**
     * Removes and returns the first element of the queue (null if empty).
     * @return  First element in the queue that was removed.
     */
    @Override
    public E dequeue( ) {
        return list.removeFirst();
    }
    
}
