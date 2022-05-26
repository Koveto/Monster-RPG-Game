package game.shuntingyardresources;

import java.util.Iterator;

/**
 * Tree
 * @author  Michael T. Goodrich
 * @author  Roberto Tamassia
 * @author  Michael H. Goldwasser
 * @author  Transcribed by Kobe Goodwin
 * @version 4/1/2022
 * 
 * A collection of elements in a parent-child hierachy. Each position has one
 * parent, except for the root, which has no parent. Each position has zero or
 * more children. Transcribed from Data Structures and Algorithms in Java by 
 * Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser, Code 
 * Fragment 8.1.
 */
public interface Tree<E> extends Iterable<E> {
    
    /**
     * Returns the position of the root of the tree (or null if empty)
     * @return  position of the root
     */
    Position<E> root( );
    
    /**
     * Returns the position of the parent of position p (or null if p is the root)
     * @param p     position to find parent of
     * @return      parent of position
     * @throws IllegalArgumentException 
     */
    Position<E> parent(Position<E> p) throws IllegalArgumentException;
    
    /**
     * Returns an iterable collection containing the children of position p
     * @param p     position to find children of
     * @return      children of p
     * @throws IllegalArgumentException 
     */
    Iterable<Position<E>> children(Position<E> p) throws IllegalArgumentException;
    
    /**
     * Returns the number of children of position p
     * @param p     position to find number of children of
     * @return      number of children of p
     * @throws IllegalArgumentException 
     */
    int numChildren(Position<E> p) throws IllegalArgumentException;
    
    /**
     * Returns true if position p has at least one child
     * @param p     position to determine whether it has a child
     * @return      true if p has at least one child
     * @throws IllegalArgumentException 
     */
    boolean isInternal(Position<E> p) throws IllegalArgumentException;
    
    /**
     * Returns true if position p does not have any children
     * @param p     position to determine whether it does not have any children
     * @return      true if p has no children
     * @throws IllegalArgumentException 
     */
    boolean isExternal(Position<E> p) throws IllegalArgumentException;
    
    /**
     * Returns true if position p is the root of the tree
     * @param p     position to determine if it has the root
     * @return      true if p is the root of a tree
     * @throws IllegalArgumentException 
     */
    boolean isRoot(Position<E> p) throws IllegalArgumentException;
    
    /**
     * Returns the number of positions (and hence elements) that are contained
     * in the tree
     * @return  number of positions in the tree
     */
    int size( );
    
    /**
     * Returns true if the tree does not contain any positions (and thus no
     * elements)
     * @return  true if the tree does not contain any positions
     */
    boolean isEmpty( );
    
    /**
     * Returns an iterator for all elements in the tree (so that the tree itself
     * is Iterable)
     * @return  an iterator for the elements in the tree
     */
    Iterator<E> iterator( );
    
    /**
     * Returns an iterable collection of all positions of the tree
     * @return  an iterable collection of positions in the tree
     */
    Iterable<Position<E>> positions( );
    
    
}
