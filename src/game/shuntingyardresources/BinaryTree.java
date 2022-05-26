package game.shuntingyardresources;

/**
 * BinaryTree
 * @author  Michael T. Goodrich
 * @author  Roberto Tamassia
 * @author  Michael H. Goldwasser
 * @author  Transcribed by Kobe Goodwin
 * @version 4/2/2022
 * 
 * An interface for a binary tree, in which each node has at most two children.
 * Transcribed from Data Structures and Algorithms in Java by Michael 
 * T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser, Code Fragment 8.6.
 */
public interface BinaryTree<E> extends Tree<E> {
    
    /**
     * Returns the Position of p's left child (or null if no child exists).
     * @param p     position to find the left child of
     * @return      position of left child
     * @throws IllegalArgumentException 
     */
    Position<E> left( Position<E> p ) throws IllegalArgumentException;
    
    /**
     * Returns the Position of p's right child (or null if no child exists).
     * @param p     position to find the right child of
     * @return      position of right child
     * @throws IllegalArgumentException 
     */
    Position<E> right( Position<E> p ) throws IllegalArgumentException;
    
    /**
     * Returns the Position of p's sibling (or null if no sibling exists).
     * @param p     position to find the sibling of
     * @return      position of sibling
     * @throws IllegalArgumentException 
     */
    Position<E> sibling( Position<E> p ) throws IllegalArgumentException;
    
}
