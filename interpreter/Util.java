package interpreter;

// various utility functions
class Util {

	static boolean isNumber(String s) {
    	boolean success = true;
    	
    	try {
			Double.parseDouble(s);
		} catch(NumberFormatException e) {
			success = false;
		}
		
		if (s.equals("NaN")) {
			success = false;
		}
		
		return success;
    }
    
    static boolean isOperator(char operator) {
		for (char c : new char[]{'+', '-', '*', '/'}) {
			if (c == operator) {
				return true;
			}
		}
		return false;
	}
	
	static boolean isSymbol(String s) {    	
    	if (s.length() == 1 && isOperator(s.charAt(0))) {
    		return true;
    	}
    	
    	if (isOneArgFunction(s) || isTwoArgsFunction(s)) {
    		return true;
    	}
    	
    	return false;
    }
	
	// NOTICE: all functions must either accept one or two arguments, meaning that the same function must
	// always accept the same number of arguments, no matter what the context is
	static boolean isOneArgFunction(String function) {
		for (String f : new String[]{"sqrt", "ln", "log", "sin", "cos", "tan"}) {
			if (f.equals(function)) {
				return true;
			}
		}
		return false;
	}

	
	static boolean isTwoArgsFunction(String function) {
		for (String f : new String[]{"root", "pow", "logbase"}) {
			if (f.equals(function)) {
				return true;
			}
		}
		return false;
	}
	
	static String removeOuterParentheses(String statement) {
		// this function recursively removes the outer parentheses from the string in statement,
		// if and only if the statement is a single whole parenthesized expression
		
		while (hasOuterParentheses(statement)) {
			statement = statement.substring(1, statement.length() - 1);
		}
		
		return statement;
	}
	
	
	static boolean hasOuterParentheses(String statement) {
		// returns true if the statement contains outer parentheses, and if those parentheses are not part
		// of parallel outer parentheses
		
		// keep track of the net parentheses count: if it's positive, then we have encountered more
		// opening than closing parentheses; the opposite is true for a negative value
		int netParCount = 0;
		
		if ( statement.charAt(0) == '(' && statement.charAt(statement.length() - 1) == ')' ) {		
			// we first need to make sure that the outer parentheses are not part of two separate
			// parallel parenthesized expressions; the way that we check this, is to look whether
			// there are any operators *outside* any parentheses
			boolean parallelOuterParentheses = false;
			
			for (int i = 0; i < statement.length(); i++) {
				if (statement.charAt(i) == '(') {
					netParCount++;
				} else if (statement.charAt(i) == ')') {
					netParCount--;
				} else {
					if (netParCount == 0) {
						// remember: if we have encountered just as many opening as closing parentheses,
						// then we are surely outside any parenthesized expression
						parallelOuterParentheses = true;
						break;
					}
				}			
			}
			
			if (! parallelOuterParentheses) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}