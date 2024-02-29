package interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

// this is for the abstract syntax tree; each rule has its own binary tree
class Node {

	static ArrayList<Node> list = new ArrayList<Node>();
	
	// modes for the splitAtSymbol() function
	static final int OPERATOR = 0;
	static final int FUNCTION = 1;
	
	// these are declared here to avoid redundant code
	static final char[] ALL_OPERATORS_LIST = {'+', '-', '*', '/'};
	static final String[] ALL_FUNCTIONS_LIST = {"sqrt", "root", "pow", "log", "sin", "cos", "tan"};
	static final String[] ALL_SYMBOLS_LIST = {"+", "-", "*", "/", "sqrt", "root", "pow", "log", "sin", "cos", "tan"};
	
	static final List<String> REQUIRES_ONE_ARG = Arrays.asList(new String[]{"sqrt", "sin", "cos", "tan"});
	static final List<String> REQUIRES_TWO_ARGS = Arrays.asList(new String[]{"root", "pow"});
	// all functions that do not occur in these arrays can accept either one or two arguments

	String data;
	Node left = null;
	Node right = null;

	Node(String data) {
		this.data = data;
	}


	void print() {
        print("", this, false, true);
    }


    void print(String prefix, Node n, boolean isLeft, boolean isOnlyChild) {
        if (n != null) {
            System.out.println(prefix + (isLeft && ! isOnlyChild ? "├── " : "└── ") + n.data);
            print(prefix + (isLeft && ! isOnlyChild ? "│   " : "    "), n.left, true, (n.right == null ? true : false));
            print(prefix + (isLeft && ! isOnlyChild ? "│   " : "    "), n.right, false, (n.left == null ? true : false));
        }
    }

    
    boolean isOperator() {
    	for (String operator : ALL_SYMBOLS_LIST) {
    		if (this.data.equals(operator)) {
    			return true;
    		}
    	}
    	
    	return false;
    }

    
    boolean isNumber() {
    	boolean success = true;
    	
    	try {
			Double.parseDouble(this.data);
		} catch(NumberFormatException e) {
			success = false;
		}
		
		return success;
    }


	void split() {		
		while (! this.isSplit() && ! Interpreter.error) {
			this.splitAtSymbol(new String[]{"+", "-"}, OPERATOR);
			this.splitAtSymbol(new String[]{"*", "/"}, OPERATOR);
			this.splitAtSymbol(ALL_FUNCTIONS_LIST, FUNCTION);
			
		}
	}

    
    private boolean isSplit() {
		// keep track of how many parentheses of each type we have encountered, so we always know
		// if we're in a parenthesized expression or not
		int openPar = 0;
		int closePar = 0;
		
		// first check if this node contains an unsplit string with an exterior operator 
		// (i.e. not inside parentheses)
		for (int i = 0; i < this.data.length(); i++) {
			if (this.data.charAt(i) == '(') {
				openPar++;
			} else if (this.data.charAt(i) == ')') {
				closePar++;
			} else {
				for (char c : ALL_OPERATORS_LIST) {
					if (openPar == closePar && this.data.length() > 1 && this.data.charAt(i) == c) {
						return false;
					}
				}
			}
		}
		
		// if we have come this far, then there are no exterior operators, and therefore also no
		// interior operators (why would an operator be enclosed in parentheses if there's no
		// operator outside them?); thus, the only parentheses that could be encountered, are those
		// of a function
		if (this.data.contains("(")) {
			return false;
		}
		
		if (this.left != null) {
			if (! this.left.isSplit()) {
				return false;
			}
		}
		
		if (this.right != null) {
			if (! this.right.isSplit()) {
				return false;
			}
		}
		
		return true;
	}
	
	
	private void splitAtSymbol(String[] symbols, int mode) {
		Interpreter.numberOfTimesSplit++;
		
		if (mode == OPERATOR) {
			// note: due to the way that binary trees work, operators are added from last to first
			String nextOperator = new String();
			int indexOfNextOperator = -1;
			boolean stop = false;
			// keep track of how many parentheses of each type we have encountered, so we always know
			// if we're in a parenthesized expression or not
			int openPar = 0;
			int closePar = 0;
		
			// now, iterate from back to forth till we detect any of the desired operators
			// (supplied in the symbols[] array)
			for (int i = this.data.length() - 1; i >= 0 && stop == false; i--) {		
				if (this.data.charAt(i) == '(') {
					openPar++;
				} else if (this.data.charAt(i) == ')') {
					closePar++;
				} else {
					for (String s : symbols) {
						// if we have encountered just as many '(' as ')', then the current position
						// is very surely not inside a parenthesized expression
						if (Character.toString(this.data.charAt(i)).equals(s) && openPar == closePar) {
							// operator op has been detected at position i
							nextOperator = s;
							indexOfNextOperator = i;
							stop = true;
							break;
						}
					}
				}
			}
			
			if (indexOfNextOperator != -1) {
				// only continue if an operator was found
				String left = this.data.substring(0, indexOfNextOperator);
				String right = this.data.substring(indexOfNextOperator + 1);
				
				if (left.length() != 0 && right.length() != 0) {
					// only continue if splitting was done
					left = removeOuterParentheses(left);
					right = removeOuterParentheses(right);
					this.left = new Node(left);
					this.right = new Node(right);
					this.data = nextOperator;
				}
			}
			
		} else if (mode == FUNCTION) {
			// first, find out if the current node contains a function
			// note: the requirements of a string being a function are: must contain a '(', must not start with '(' and must
			// end with ')'			
			int indexOfParenthesis = -1;
			
			for (int i = 0; i < this.data.length(); i++) {
				if (this.data.charAt(i) == '(') {
					indexOfParenthesis = i;
					break;
				}
			}
			
			if (indexOfParenthesis != -1 && this.data.charAt(0) != '(' && this.data.charAt(this.data.length() - 1) == ')') {
				String functionName = this.data.substring(0, indexOfParenthesis);
				String arg1 = new String();
				String arg2 = new String();
				boolean isValidFunctionName = false;
				
				// check if function name is valid
				for (String f : ALL_FUNCTIONS_LIST) {
					if (functionName.equals(f)) {
						isValidFunctionName = true;
						break;
					}
				}
				
				if (! isValidFunctionName) {
					Error.noSuchFunction(functionName);
				} else {
					// get arguments
					// note: functions can have at most 2 arguments in CML
					int commasEncountered = 0;
					StringBuilder sb = new StringBuilder();
					
					for (int i = indexOfParenthesis + 1; i < this.data.length(); i++) {
						if (this.data.charAt(i) == ',') {
							commasEncountered++;
							if (commasEncountered > 1) {
								Error.tooManyArguments(functionName);
								break;
							} else {
								arg1 = sb.toString();
								sb.setLength(0); // reset for the second argument
							}
								
						} else if (i == this.data.length() - 1) {
							// this is the closing parenthesis of the function
							if (commasEncountered == 0) {
								arg1 = sb.toString();
							} else {
								arg2 = sb.toString();
							}
							
						} else {
							sb.append(this.data.charAt(i));
						}					
					}
				}
				
				if (! Interpreter.error) {
					// check that the function got the right amount of arguments
					if (arg1.length() == 0) {
						Error.notEnoughArguments(functionName);
					} else {		
						if (REQUIRES_ONE_ARG.contains(functionName)) {					
							// functions that take only one argument
							if (arg2.length() != 0) {
								Error.tooManyArguments(functionName);
							}
						} else if (REQUIRES_TWO_ARGS.contains(functionName)) {
							// functions that take only two arguments
							if (arg2.length() == 0) {
								Error.notEnoughArguments(functionName);
							}
						}
					}
					
					// finish arranging the function into the current node
					if (! Interpreter.error) {
						arg1 = removeOuterParentheses(arg1);
						this.left = new Node(arg1);
						
						// not all functions have a second argument
						if (arg2.length() != 0) {
							arg2 = removeOuterParentheses(arg2);
							this.right = new Node(arg2);
						}
						
						this.data = functionName;
					}
				}
			}
		}
		
		// also split the subtrees, if they exist
		if (this.left != null && ! Interpreter.error) {
			this.left.splitAtSymbol(symbols, mode);
		}

		if (this.right != null && ! Interpreter.error) {
			this.right.splitAtSymbol(symbols, mode);
		}
	}
	
	
	private String removeOuterParentheses(String statement) {
		// this function removes the outer parentheses from the string in statement, if and only if
		// the statement is a single whole parenthesized expression
		
		// keep track of how many parentheses of each type we have encountered, so we always know
		// if we're in a parenthesized expression or not
		int openPar = 0;
		int closePar = 0;
		
		if ( statement.charAt(0) == '(' && statement.charAt(statement.length() - 1) == ')' ) {
			// we first need to make sure that the outer parentheses are not part of two separate
			// parallel parenthesized expressions; the way that we check this, is to look whether
			// there are any operators *outside* any parentheses
			boolean parallelOuterParentheses = false;
			
			for (int i = 0; i < statement.length(); i++) {
				if (statement.charAt(i) == '(') {
					openPar++;
				} else if (statement.charAt(i) == ')') {
					closePar++;
				} else {
					if (openPar == closePar) {
						// remember: if we have encountered just as many opening as closing parentheses,
						// then we are surely outside any parenthesized expression
						parallelOuterParentheses = true;
						break;
					}
				}			
			}

			// only remove the outer parentheses if they are not part of parallel parenthesized expressions
			if (parallelOuterParentheses == false) {
				return statement.substring(1, statement.length() - 1);
			} else {
				// outer parentheses are part of multiple parallel parenthesized expressions, return as-is
				return statement;
			}
		} else {
			// no outer parentheses, return as-is
			return statement;
		}
	}
}