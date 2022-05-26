package game.shuntingyardresources;

/**
 * TreeMaker
 * @author Kobe Goodwin
 * @version 4/9/2022
 * 
 * Utilities class that creates a binary tree from a postfix queue.
 */
public class TreeMaker {
    
    private static TokenUtilities u = new TokenUtilities();
    
    /**
     * Creates a binary tree from a postfix queue.
     * @param postfix       Postfix queue  
     * @return              LinkedBinaryTree representation of postfix
     */
    public static LinkedBinaryTree<String> postfixToBinaryTree( LinkedQueue<String> postfix ) {
        
        if (postfix == null || postfix.isEmpty())
            return null;
        
        LinkedStack<LinkedBinaryTree<String>> stack = new LinkedStack();
        
        while (!postfix.isEmpty()) {
            
            String token = postfix.dequeue();
            if (u.isOperator(token)) {
                
                LinkedBinaryTree<String> right = stack.pop();
                LinkedBinaryTree<String> left = stack.pop();
                
                if (left == null || right == null)
                    return null;
                
                LinkedBinaryTree<String> newTree = new LinkedBinaryTree();
                newTree.addRoot(token);
                newTree.attach(newTree.root, left, right);
                
                stack.push(newTree);
                
            } else {
                
                LinkedBinaryTree<String> newTree = new LinkedBinaryTree();
                newTree.addRoot(token);
                stack.push(newTree);
                
            }
            
        }
        
        if (stack.size() != 1)
            return null;
        
        return stack.top();
        
    }
    
}
