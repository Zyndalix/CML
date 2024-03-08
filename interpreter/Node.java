package interpreter;

import java.util.ArrayList;

// this is for the abstract syntax tree; each rule has its own binary tree
public class Node {

	static ArrayList<Node> list;

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

    
    boolean isSymbol() {    	
    	if (this.data.length() == 1 && isOperator(this.data.charAt(0))) {
    		return true;
    	}
    	
    	if (isOneArgFunction(this.data) || isTwoArgsFunction(this.data)) {
    		return true;
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
			this.splitAtOperator(new char[]{'+', '-'});
			this.splitAtOperator(new char[]{'*', '/'});
			this.splitAtFunction(new String[]{"sqrt", "root", "pow", "ln", "log", "logbase", "sin", "cos", "tan"});
		}
	}

    
    private boolean isSplit() {
		// keep track of how many parentheses of each type we have encountered, so we always know
		// if we're in a parenthesized expression or not
		int openPar = 0;
		int closePar = 0;
		
		// first check if this node contains an unsplit string with an exterior operator 
		// (i.e. not inside parentheses)
		if (this.data.length() > 1) {
			for (int i = 0; i < this.data.length(); i++) {
				if (this.data.charAt(i) == '(') {
					openPar++;
				} else if (this.data.charAt(i) == ')') {
					closePar++;
				} else if (openPar == closePar && isOperator(this.data.charAt(i))) {
					return false;
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
	
	
	private void splitAtOperator(char[] operators) {
		// note: due to the way that binary trees work, operators are added from last to first
		char nextOperator = 0;
		int indexOfNextOperator = -1;
		boolean stop = false;
		// keep track of how many parentheses of each type we have encountered, so we always know
		// if we're in a parenthesized expression or not
		int openPar = 0;
		int closePar = 0;
	
		// now, iterate from back to forth till we detect any of the desired operators
		// (supplied in the symbols[] array)
		if (this.data.length() > 1) {
			for (int i = this.data.length() - 1; i >= 0 && stop == false; i--) {		
				if (this.data.charAt(i) == '(') {
					openPar++;
				} else if (this.data.charAt(i) == ')') {
					closePar++;
				} else if (openPar == closePar) {
					for (char op : operators) {
						// if we have encountered just as many '(' as ')', then the current position
						// is very surely not inside a parenthesized expression
						if ( this.data.charAt(i) == op && (i == 0 || ! isOperator(this.data.charAt(i - 1))) ) {
							// ignore minus signs that denote a negative variable or value instead of subtraction
							// operator s has been detected at position i
							nextOperator = op;
							indexOfNextOperator = i;
							stop = true;
							break;
						}
					}
				}
			}
		}
		
		if (indexOfNextOperator != -1) {
			// only continue if an operator was found
			String left = this.data.substring(0, indexOfNextOperator);
			String right = this.data.substring(indexOfNextOperator + 1);
			
			if (nextOperator == '-' && left.length() == 0) {
				// the value in the string 'right' needs to be made negative; subtract it from 0 to do so
				left = "0";
			}
			
			if (left.length() != 0 && right.length() != 0) {
				// only continue if splitting was done
				left = removeOuterParentheses(left);
				right = removeOuterParentheses(right);
				this.left = new Node(left);
				this.right = new Node(right);
				this.data = Character.toString(nextOperator);
			}
		}
		
		// also split the subtrees, if they exist
		if (this.left != null && ! Interpreter.error) {
			this.left.splitAtOperator(operators);
		}

		if (this.right != null && ! Interpreter.error) {
			this.right.splitAtOperator(operators);
		}
	}
	
	
	private void splitAtFunction(String[] functions) {
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
			for (String f : functions) {
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
					if (isOneArgFunction(functionName)) {					
						// functions that take only one argument
						if (arg2.length() != 0) {
							Error.tooManyArguments(functionName);
						}
					} else if (isTwoArgsFunction(functionName)) {
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
		
		// also split the subtrees, if they exist
		if (this.left != null && ! Interpreter.error) {
			this.left.splitAtFunction(functions);
		}

		if (this.right != null && ! Interpreter.error) {
			this.right.splitAtFunction(functions);
		}
	}
	
	
	
	
	static boolean isOperator(char operator) {
		for (char c : new char[]{'+', '-', '*', '/'}) {
			if (c == operator) {
				return true;
			}
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