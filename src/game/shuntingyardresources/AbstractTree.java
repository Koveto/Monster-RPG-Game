package game.shuntingyardresources;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * AbstractTree
 * @author  Michael T. Goodrich
 * @author  Roberto Tamassia
 * @author  Michael H. Goldwasser
 * @author  Transcribed by Kobe Goodwin
 * @version 4/2/2022
 * 
 * An abstract base class providing some functionality of the Tree interface.
 * Transcribed from Data Structures and Algorithms in Java by Michael 
 * T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser, Code Fragments 8.2,
 * 8.4, 8.5, 8.16, 8.17, 8.18, 8.19, 8.20, and 8.21.
 */
public abstract class AbstractTree<E> implements Tree<E> {
    
    /**
     * Nested ElementIterator class. Adapts the iteration produced by
     * positions() to return elements.
     */
    private class ElementIterator implements Iterator<E> {
        
        Iterator<Position<E>> posIterator = positions().iterator();
        
        /**
         * True if iterator has a next token
         * @return  True if next element exists, false if not
         */
        public boolean hasNext( ) { return posIterator.hasNext(); }
        
        /**
         * Returns next element in iterator
         * @return  next element in iterator
         */
        public E next( ) { return posIterator.next().getElement(); }
        
        /**
         * Removes last element in iterator
         */
        public void remove( ) { posIterator.remove(); }
        
    }
    
    /**
     * Returns true if position p has at least one child
     * @param p     position to determine whether it has a child
     * @return      true if p has at least one child
     * @throws IllegalArgumentException 
     */
    public boolean isInternal( Position<E> p ) {return numChildren(p) > 0;}
    
    /**
     * Returns true if position p does not have any children
     * @param p     position to determine whether it does not have any children
     * @return      true if p has no children
     * @throws IllegalArgumentException 
     */
    public boolean isExternal( Position<E> p ) {return numChildren(p) == 0;}
    
    /**
     * Returns true if position p is the root of the tree
     * @param p     position to determine if it has the root
     * @return      true if p is the root of a tree
     * @throws IllegalArgumentException 
     */
    public boolean isRoot( Position<E> p ) {return p == root( );}
    
    /**
     * Returns true if the tree does not contain any positions (and thus no
     * elements)
     * @return  true if the tree does not contain any positions
     */
    public boolean isEmpty( ) { return size( ) == 0; }
    
    /**
     * Returns the number of levels separating Position p from the root
     * @param p     position to find the depth of
     * @return      number of levels seperating p from the root
     */
    public int depth( Position<E> p ) {
        
        if (isRoot(p))
            return 0;
        else
            return 1 + depth(parent(p));
        
    }
    
    /**
     * Returns the height of the subtree rooted at Position p.
     * @param p     position to find height of
     * @return      height of the subtree rooted at p
     */
    public int height( Position<E> p ) {
        
        int h = 0;
        for (Position<E> c : children(p))
            h = Math.max(h, 1 + height(c));
        return h;
        
    }
    
    /**
     * Returns an iterator of the elements stored in the tree
     * @return      an iterator of the elements stored in the tree
     */
    public Iterator<E> iterator( ) { return new ElementIterator( ); }
    
    /**
     * Returns an iterable list of positions in the tree
     * @return      an iterable list of positions in the tree
     */
    public Iterable<Position<E>> positions( ) { return preorder( ); }
    
    /**
     * Adds positions of the subtree rooted at Position p to the given snapshot.
     * @param p             Position to add children to
     * @param snapshot      List of positions
     */
    private void preorderSubtree( Position<E> p, List<Position<E>> snapshot ) {
        
        snapshot.add(p);    // for preorder, we add position p before exploring subtrees
        for (Position<E> c : children(p))
            preorderSubtree(c, snapshot);
        
    }
    
    /**
     * Adds positions of the subtree rooted at Position p to the given snapshot
     * @param p             Position to add children to
     * @param snapshot      List of positions
     */
    private void postorderSubtree( Position<E> p, List<Position<E>> snapshot ) {
        
        for (Position<E> c : children(p))
            postorderSubtree(c, snapshot);
        snapshot.add(p);    // for postorder, we add position p after exploring subtrees
        
    }
    
    /**
     * Returns an iterable collection of positions of the tree, reported in
     * preorder.
     * @return  iterable collection of positions in the tree, in preorder
     */
    public Iterable<Position<E>> preorder( ) {
        
        List<Position<E>> snapshot = new ArrayList();
        if (!isEmpty())
            preorderSubtree(root( ), snapshot); // snapshot recursively
        return snapshot;
        
    }
    
    /**
     * Returns an iterable collection of positions of the tree, reported in
     * postorder.
     * @return  iterable collection of positions in the tree, in postorder
     */
    public Iterable<Position<E>> postorder( ) {
        
        List<Position<E>> snapshot = new ArrayList();
        if (!isEmpty())
            postorderSubtree(root( ), snapshot);
        return snapshot;
        
    }
    
    public Iterable<Position<E>> breadthfirst( ) {
        
        List<Position<E>> snapshot = new ArrayList();
        if (!isEmpty()) {
            Queue<Position<E>> fringe = new LinkedQueue();
            fringe.enqueue(root());
            while (!fringe.isEmpty()) {
                Position<E> p = fringe.dequeue();
                snapshot.add(p);
                for (Position<E> c : children(p))
                    fringe.enqueue(c);
            }
        }
        return snapshot;
        
    }
    
}

