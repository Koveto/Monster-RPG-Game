package game.shuntingyardresources;


import java.util.ArrayList;
import java.util.List;

/**
 * AbstractBinaryTree
 * @author  Michael T. Goodrich
 * @author  Roberto Tamassia
 * @author  Michael H. Goldwasser
 * @author  Transcribed by Kobe Goodwin
 * @version 4/2/2022
 * 
 * An abstract base class providing some functionality of the BinaryTree
 * interface. Transcribed from Data Structures and Algorithms in Java by Michael 
 * T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser, Code Fragments 8.7,
 * and 8.22.
 */
public abstract class AbstractBinaryTree<E> extends AbstractTree<E>
                                        implements BinaryTree<E> {
    
    /**
     * Returns the Position of p's sibling (or null if no sibling exists).
     * @param p     position to find the sibling of
     * @return      position of sibling
     * @throws IllegalArgumentException 
     */
    public Position<E> sibling( Position<E> p ) {
        
        Position<E> parent = parent(p);
        if (parent == null) return null;
        if (p == left(parent))
            return right(parent);
        else
            return left(parent);
        
    }
    
    /**
     * Returns the number of children of Position p
     * @param p     Position to find number of children of
     * @return      number of children of position p
     */
    public int numChildren( Position<E> p ) {
        
        int count = 0;
        if (left(p) != null)
            count++;
        if (right(p) != null)
            count++;
        return count;
        
    }
    
    /**
     * Returns an iterable collection of the Positions representing p's
     * children
     * @param p     Position to list children of
     * @return      Iterable collection of p's children
     */
    public Iterable<Position<E>> children( Position<E> p ) {
        
        List<Position<E>> snapshot = new ArrayList( 2 );
        if (left(p) != null)
            snapshot.add(left(p));
        if (right(p) != null)
            snapshot.add(right(p));
        return snapshot;
        
    }
    
    /**
     * Adds positions of the subtree rooted at Position p to the given snapshot.
     * @param p                 Root of subtree
     * @param snapshot          List of positions
     */
    private void inorderSubtree( Position<E> p, List<Position<E>> snapshot ) {
        
        if (left(p) != null)
            inorderSubtree(left(p), snapshot);
        snapshot.add(p);
        if (right(p) != null)
            inorderSubtree(right(p), snapshot);
    
    }

    /**
     * Returns an iterable collection of positions of the tree, reported in
     * inorder.
     * @return  Iterable collection of positions in inorder
     */
    public Iterable<Position<E>> inorder( ) {
        
        List<Position<E>> snapshot = new ArrayList();
        if (!isEmpty())
            inorderSubtree(root(), snapshot);   // fill snapshot recursively
        return snapshot;
        
    }
    
    /**
     * Overrides positions to make inorder the default order for binary trees
     * @return  Inorder traversal as iterable collection
     */
    public Iterable<Position<E>> positions( ) { return inorder( ); }
    
}
