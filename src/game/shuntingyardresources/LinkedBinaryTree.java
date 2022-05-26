package game.shuntingyardresources;

/**
 * LinkedBinaryTree
 * @author  Michael T. Goodrich
 * @author  Roberto Tamassia
 * @author  Michael H. Goldwasser
 * @author  Transcribed by Kobe Goodwin
 * @param <E>   generic data type
 * 
 * Concrete implementation of a binary tree using a node-based, linked structure.
 * Transcribed from Data Structures and Algorithms in Java by Michael 
 * T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser, Code Fragment 8.8,
 * 8.9, 8.10, and 8.11.
 */
public class LinkedBinaryTree<E> extends AbstractBinaryTree<E> {
    
    /**
     * Nested Node Class. Contains an element with a parent and children, left
     * and right.
     * @param <E>    generic data type
     */
    protected static class Node<E> implements Position<E> {
        
        private E element;
        private Node<E> parent, left, right;
        
        /**
         * Constructor
         * @param e             Element to contain
         * @param above         Parent node
         * @param leftChild     Left child node
         * @param rightChild    Right child node
         */
        public Node( E e, Node<E> above, Node<E> leftChild, Node<E> rightChild ) {
            
            element = e;
            parent = above;
            left = leftChild;
            right = rightChild;
            
        }
        
        /**
         * Accessor for element
         * @return  element stored
         */
        public E getElement( ) { return element; }
        
        /**
         * Accessor for parent node
         * @return  parent node
         */
        public Node<E> getParent( ) { return parent; }
        
        /**
         * Accessor for left child node
         * @return  left child node
         */
        public Node<E> getLeft( ) { return left; }
        
        /**
         * Accessor for right child node
         * @return  right child node
         */
        public Node<E> getRight( ) { return right; }
        
        /**
         * Mutator for element
         * @param element   new element to store
         */
        public void setElement( E element ) { this.element = element; }
        
        /**
         * Mutator for parent node
         * @param parent    new parent node
         */
        public void setParent( Node<E> parent ) { this.parent = parent; }
        
        /**
         * Mutator for left child node
         * @param left      new left child node
         */
        public void setLeft( Node<E> left ) { this.left = left; }
        
        /**
         * Mutator for right child node
         * @param right     new right child node
         */
        public void setRight( Node<E> right ) { this.right = right; }
        
    }
    
    /**
     * Factory function to create a new node storing element e.
     * @param e         Element to store
     * @param parent    Parent node
     * @param left      Left child node
     * @param right     Right child node
     * @return          New node
     */
    protected Node<E> createNode( E e, Node<E> parent, Node<E> left, 
            Node<E> right ) {
        
        return new Node( e, parent, left, right );
        
    }
    
    protected Node<E> root = null;      // root of the tree
    private int size = 0;               // number of nodes in the tree
    
    /**
     * Constructor
     */
    public LinkedBinaryTree( ) {}
    
    /**
     * Validates the position and returns it as a node
     * @param p     Position p to interpret as a node
     * @return      Position as a node reference
     * @throws IllegalArgumentException     if no longer in tree or never on tree
     */
    protected Node<E> validate( Position<E> p ) throws IllegalArgumentException {
        
        if (!(p instanceof Node))
            throw new IllegalArgumentException("Not valid position type");
        Node<E> node = (Node<E>) p;
        if (node.getParent() == node)
            throw new IllegalArgumentException("p is no longer in the tree");
        return node;
        
    }
    
    /**
     * Returns the number of nodes in the tree
     * @return  number of nodes in the tree
     */
    public int size( ) { return size; }
    
    /**
     * Returns root Position of the tree (or null if the tree is empty)
     * @return  Position of the root of the tree
     */
    public Position<E> root( ) { return root; }
    
    /**
     * Returns the Position of p's parent (or null if p is the root)
     * @param p     Position to find parent of
     * @return      parent Position of p
     * @throws IllegalArgumentException 
     */
    public Position<E> parent( Position<E> p ) throws IllegalArgumentException {
        
        Node<E> node = validate(p);
        return node.getParent();
        
    }
    
    /**
     * Returns the Position of p's left child (or null if no child exists)
     * @param p     Position to find left child of
     * @return      left child of p
     * @throws IllegalArgumentException 
     */
    public Position<E> left( Position<E> p ) throws IllegalArgumentException {
        
        Node<E> node = validate(p);
        return node.getLeft();
        
    }
    
    /**
     * Returns the Position of p's right child (or null if no child exists)
     * @param p     Position to find right child of
     * @return      right child of p
     * @throws IllegalArgumentException 
     */
    public Position<E> right( Position<E> p ) throws IllegalArgumentException {
        
        Node<E> node = validate(p);
        return node.getRight();
        
    }
    
    /**
     * Places element e at the root of an empty tree and returns its new 
     * Position.
     * @param e     Element to put at root
     * @return      Position of root
     * @throws IllegalStateException 
     */
    public Position<E> addRoot( E e ) throws IllegalStateException {
        
        if (!isEmpty( )) throw new IllegalStateException("tree is not empty.");
        root = createNode( e, null, null, null );
        size = 1;
        return root;
        
    }
    
    /**
     * Create a new left child of Position p storing element e; returns its
     * Position
     * @param p     Position to create left child of
     * @param e     Element to store in left child
     * @return      Position of left child
     * @throws IllegalArgumentException 
     */
    public Position<E> addLeft( Position<E> p, E e ) throws IllegalArgumentException {
        
        Node<E> parent = validate(p);
        if (parent.getLeft() != null)
            throw new IllegalArgumentException("p already has a left child");
        Node<E> child = createNode(e, parent, null, null);
        parent.setLeft(child);
        size++;
        return child;
        
    }
    
    /**
     * Create a new right child of Position p storing element e; returns its
     * Position
     * @param p     Position to create right child of
     * @param e     Element to store in right child
     * @return      Position of right child
     * @throws IllegalArgumentException 
     */
    public Position<E> addRight( Position<E> p, E e ) throws IllegalArgumentException {
        
        Node<E> parent = validate(p);
        if (parent.getRight() != null)
            throw new IllegalArgumentException("p already has a right child");
        Node<E> child = createNode(e, parent, null, null);
        parent.setRight(child);
        size++;
        return child;
        
    }
    
    /**
     * Replaces the element at Position p with e and returns the replaced
     * element.
     * @param p     Position to replace element with
     * @param e     Element to replace existing element
     * @return      Replaced element
     * @throws IllegalArgumentException 
     */
    public E set( Position<E> p, E e ) throws IllegalArgumentException {
        
        Node<E> node = validate(p);
        E temp = node.getElement();
        node.setElement(e);
        return temp;
        
    }
    
    public void attach( Position<E> p, LinkedBinaryTree<E> t1, 
            LinkedBinaryTree<E> t2 ) throws IllegalArgumentException {
        
        Node<E> node = validate(p);
        if (isInternal(p)) throw new IllegalArgumentException("p must be a leaf");
        size += t1.size() + t2.size();
        if (!t1.isEmpty( )) {
            t1.root.setParent(node);
            node.setLeft(t1.root);
            t1.root = null;
            t1.size = 0;
        }
        if (!t2.isEmpty( )) {
            t2.root.setParent(node);
            node.setRight(t2.root);
            t2.root = null;
            t2.size = 0;
        }
        
    }
    
    /**
     * Removes the node at Position p and replaces it with its child, if any.
     * @param p     Position to remove
     * @return      Element stored in Position p
     * @throws IllegalArgumentException 
     */
    public E remove( Position<E> p ) throws IllegalArgumentException {
        
        Node<E> node = validate(p);
        if (numChildren(p) == 2)
            throw new IllegalArgumentException("p has two children");
        Node<E> child = (node.getLeft( ) != null ? node.getLeft() : node.getRight());
        if (child != null)
            child.setParent(node.getParent( ));
        if (node == root)
            root = child;
        else {
            Node<E> parent = node.getParent();
            if (node == parent.getLeft())
                parent.setLeft(child);
            else
                parent.setRight(child);
        }
        size--;
        E temp = node.getElement();
        node.setElement(null);
        node.setLeft(null);
        node.setRight(null);
        node.setParent(node);
        return temp;
        
    }
    
}
