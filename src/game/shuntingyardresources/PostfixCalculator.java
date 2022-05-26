package game.shuntingyardresources;

/**
 * PostfixCalculator
 * @author Kobe Goodwin
 * @version 4/9/2022
 * 
 * Utilities class that calculates the value of a postfix expression.
 */
public class PostfixCalculator {
    
    private static TokenUtilities u = new TokenUtilities();
    
    /**
     * Evaluates a postfix expression, stored in a LinkedQueue.
     * @param postfix   Postfix expression in a queue
     * @return          Double value of expression
     */
    public static double evaluatePostfix( LinkedQueue<String> postfix, String var, Double varValue ) {
        
        LinkedStack<String> stack = new LinkedStack();
        LinkedQueue<String> copy = new LinkedQueue();
        
        while (!postfix.isEmpty()) {
            
            while (postfix.first() != null && !u.isOperator(postfix.first())) {
                
                String dequeued = postfix.dequeue();
                stack.push(dequeued);
                copy.enqueue(dequeued);
                
            }
            
            if (postfix.isEmpty())
                break;
            
            double rightOperand = 0, leftOperand = 0;
            try {
                if (var != null && stack.top() != null && stack.top().equals(var)) {
                    stack.pop();
                    rightOperand = varValue;
                }
                else
                    rightOperand = Double.parseDouble(stack.pop());
                if (var != null && stack.top() != null && stack.top().equals(var)) {
                    stack.pop();
                    leftOperand = varValue;
                }
                else
                    leftOperand = Double.parseDouble(stack.pop());
            } catch (NullPointerException npe) {
                return Double.NaN;
            }
            String operator = postfix.dequeue();
            copy.enqueue(operator);
            
            double result = 0;
            
            if (operator.equals("+")) {
                result = leftOperand + rightOperand;
            } else if (operator.equals("-")) {
                result = leftOperand - rightOperand;
            } else if (operator.equals("*")) {
                result = leftOperand * rightOperand;
            } else if (operator.equals("/")) {
                result = leftOperand / rightOperand;
            } else if (operator.equals("^")) {
                result = Math.pow(leftOperand, rightOperand);
            } else {
                return Double.NaN;
            }
            
            stack.push(String.valueOf(result));
            
        }
        
        if (stack.size() != 1)
            return Double.NaN;
        
        while (!copy.isEmpty()) {
            postfix.enqueue(copy.dequeue());
        }
        
        if (stack.top().equals(var)) {
            return varValue;
        }
        
        return Double.valueOf(stack.pop());
        
    }
    
}
