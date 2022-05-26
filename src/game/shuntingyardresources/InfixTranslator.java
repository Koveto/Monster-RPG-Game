package game.shuntingyardresources;

/**
 * InfixTranslator
 * @author Kobe Goodwin
 * @version 4/9/2022
 * 
 * A utilities class that translates an infix queue to a postfix queue.
 */
public class InfixTranslator {
    
    private static TokenUtilities u = new TokenUtilities();
    
    /**
     * Creates a postfix queue from an infix queue.
     * @param infix     Infix queue to interpret
     * @return          Postfix queue
     */
    public static LinkedQueue<String> infixToPostfix( LinkedQueue<String> infix ) {
        
        LinkedStack<String> stack = new LinkedStack();
        LinkedQueue<String> postfix = new LinkedQueue();
        
        while (!infix.isEmpty()) {
            
            String token = infix.dequeue();
            if (u.isOperator(token) || u.isOpenGroupingSymbol(token)) {
                if (token.equals("^")) {
                    while (stack.top() != null && stack.top().equals("^")) {
                        postfix.enqueue(stack.pop());
                    }
                } else if (u.isMultiplyOrDivide(token)) {
                    while (stack.top() != null && 
                            (stack.top().equals("^") || 
                            u.isMultiplyOrDivide(stack.top()))) {
                        postfix.enqueue(stack.pop());
                    }
                } else if (u.isAddOrSubtract(token)) {
                    while (stack.top() != null && 
                            (u.isMultiplyOrDivide(stack.top()) || 
                            u.isAddOrSubtract(stack.top()))) {
                        postfix.enqueue(stack.pop());
                    }
                }
                stack.push(token);
            } else if (u.isClosingGroupingSymbol(token)) {
                while (!u.isOpenGroupingSymbol(stack.top())) {
                    String removed = stack.pop();
                    postfix.enqueue(removed);
                }
                if (!u.isMatchingGroupingSymbol(stack.pop(), token))
                    return null;
                
            } else { // must be number
                postfix.enqueue(token);
            }
            
        }
        
        while (!stack.isEmpty()) {
            
            String toAppendToPostfix = stack.pop();
            if (u.isOpenGroupingSymbol(toAppendToPostfix))
                return null;
            postfix.enqueue(toAppendToPostfix);
            
        }
        
        return postfix;
        
    }
    
}
