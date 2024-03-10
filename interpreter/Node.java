package interpreter;

// this is for the abstract syntax tree; each variable and rule has its own binary tree
class Node {

	String data;
	Node left = null;
	Node right = null;
	int lineNumber; // line where the symbol in this.data was called; used for error messages

	Node(String data, int lineNumber) {
		this.data = data;
		this.lineNumber = lineNumber;
	}


	void print() {
        print("", this, false, true);
    }


    void print(String prefix, Node n, boolean isLeft, boolean isOnlyChild) {
        if (n != null) {
            Interpreter.diagnosticBuffer += prefix + (isLeft && ! isOnlyChild ? "├── " : "└── ") + n.data + "\n";
            print(prefix + (isLeft && ! isOnlyChild ? "│   " : "    "), n.left, true, (n.right == null ? true : false));
            print(prefix + (isLeft && ! isOnlyChild ? "│   " : "    "), n.right, false, (n.left == null ? true : false));
        }
    }


	void split() {		
		while (! this.isSplit() && ! Interpreter.error) {
			this.splitAtOperator(new char[]{'+', '-'});
			this.splitAtOperator(new char[]{'*', '/'});
			this.splitAtFunction(new String[]{"sqrt", "root", "pow", "ln", "log", "logbase", "sin", "cos", "tan"});
		}
	}

    
    private boolean isSplit() {
		// keep track of the net parentheses count: if it's positive, then we have encountered more
		// opening than closing parentheses; the opposite is true for a negative value
		int netParCount = 0;
		
		// first check if this node contains an unsplit string with an exterior operator 
		// (i.e. not inside parentheses)
		if (this.data.length() > 1) {
			for (int i = 0; i < this.data.length(); i++) {
				if (this.data.charAt(i) == '(') {
					netParCount++;
				} else if (this.data.charAt(i) == ')') {
					netParCount--;
				} else if (netParCount == 0 && Util.isOperator(this.data.charAt(i))) {
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
		// keep track of the net parentheses count: if it's positive, then we have encountered more
		// opening than closing parentheses; the opposite is true for a negative value
		int netParCount = 0;
	
		// now, iterate from back to forth till we detect any of the desired operators
		// (supplied in the symbols[] array)
		if (this.data.length() > 1) {
			for (int i = this.data.length() - 1; i >= 0 && stop == false; i--) {		
				if (this.data.charAt(i) == '(') {
					netParCount++;
				} else if (this.data.charAt(i) == ')') {
					netParCount--;
				} else if (netParCount == 0) {
					for (char op : operators) {
						// if we have encountered just as many '(' as ')', then the current position
						// is very surely not inside a parenthesized expression
						if ( this.data.charAt(i) == op && (i == 0 || ! Util.isOperator(this.data.charAt(i - 1))) ) {
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
				left = Util.removeOuterParentheses(left);
				right = Util.removeOuterParentheses(right);
				this.left = new Node(left, this.lineNumber);
				this.right = new Node(right, this.lineNumber);
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
		// note: the requirements of a string being a function are: must contain a '(', must not start with '(',
		// must end with ')', and must not contain an operator outside the parentheses
		int indexOfParenthesis = -1;
		
		for (int i = 0; i < this.data.length(); i++) {
			if (this.data.charAt(i) == '(') {
				indexOfParenthesis = i;
				break;
			} else if (Util.isOperator(this.data.charAt(i))) {
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
				Error.noSuchFunction("rules", this.lineNumber, functionName);
			} else {
				// get arguments
				// note: functions can have at most 2 arguments in CML
				int commasEncountered = 0;
				StringBuilder sb = new StringBuilder();
				
				for (int i = indexOfParenthesis + 1; i < this.data.length(); i++) {
					if (this.data.charAt(i) == ',') {
						commasEncountered++;
						if (commasEncountered > 1) {
							Error.tooManyArguments("rules", this.lineNumber, functionName);
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
					Error.notEnoughArguments("rules", this.lineNumber, functionName);
				} else {		
					if (Util.isOneArgFunction(functionName)) {					
						// functions that take only one argument
						if (arg2.length() != 0) {
							Error.tooManyArguments("rules", this.lineNumber, functionName);
						}
					} else if (Util.isTwoArgsFunction(functionName)) {
						// functions that take only two arguments
						if (arg2.length() == 0) {
							Error.notEnoughArguments("rules", this.lineNumber, functionName);
						}
					}
				}
				
				// finish arranging the function into the current node
				if (! Interpreter.error) {
					arg1 = Util.removeOuterParentheses(arg1);
					this.left = new Node(arg1, this.lineNumber);
					
					// not all functions have a second argument
					if (arg2.length() != 0) {
						arg2 = Util.removeOuterParentheses(arg2);
						this.right = new Node(arg2, this.lineNumber);
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
}