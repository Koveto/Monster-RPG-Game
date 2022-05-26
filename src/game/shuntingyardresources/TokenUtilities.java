package game.shuntingyardresources;

/**
 * TokenUtilities
 * @author Kobe Goodwin
 * @version 4/9/2022
 * 
 * A utilities class to determine if a String token is a certain symbol.
 */
public class TokenUtilities {
    
    /**
     * Determines if token is an operator.
     * @param token String
     * @return      True if token is "+", "-", "*", "/"
     */
    public boolean isOperator( String token ) {
        
        return token.equals("+") || token.equals("-") || token.equals("*")
                    || token.equals("/") || token.equals("^");
        
    }
    
    /**
     * Determines if token is an open grouping symbol.
     * @param token String
     * @return      True if token is "(", "{", "["
     */
    public boolean isOpenGroupingSymbol( String token ) {
        
        return token.equals("(") || token.equals("[") || token.equals("{");
        
    }
    
    /**
     * Determines if token is a closing grouping symbol.
     * @param token String
     * @return      True if token is ")", "}", "]"
     */
    public boolean isClosingGroupingSymbol( String token ) {
        
        return token.equals(")") || token.equals("]") || token.equals("}");
        
    }
    
    /**
     * Determines if leftSymbol matches rightSymbol.
     * @param leftSymbol        Left grouping symbol such as "(", "{", "["
     * @param rightSymbol       Right grouping symbol such as ")", "}", "]"
     * @return  True if left symbol and right symbol are both parenthesis,
     * curly brackets, or square brackets.
     */
    public boolean isMatchingGroupingSymbol( String leftSymbol, String rightSymbol ) {
        
        return (leftSymbol.equals("(") && rightSymbol.equals(")")) ||
                (leftSymbol.equals("[") && rightSymbol.equals("]")) ||
                (leftSymbol.equals("{") && rightSymbol.equals("}"));
        
    }
    
    public boolean isMultiplyOrDivide( String token ) {
        
        return token.equals("*") || token.equals("/");
        
    }
    
    public boolean isAddOrSubtract( String token ) {
        
        return token.equals("+") || token.equals("-");
        
    }
    
}
