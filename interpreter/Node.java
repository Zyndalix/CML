package interpreter;

// this is for the abstract syntax tree; each variable and rule has its own binary tree
class Node {

	String data;
	Node left = null;
	Node right = null;
	char type; // can be either 'v' for variable or 'r' for rule
	int lineNumber; // line where the symbol in this.data was called; used for error messages

	Node(String data, char type, int lineNumber) {
		this.data = data;
		this.type = type;
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
    	if (this.data.startsWith("if")) {
    		splitIfStatement();
    	} else if (this.data.contains("=")) {
    		splitAssignment();
    	} else if (! this.data.equals("stop")) {
    		Error.whatIsThis(this.type, this.lineNumber, this.data);
    	}
    }
    
    
	private void splitIfStatement() {
	
		StringBuilder sb = new StringBuilder();
		int lineNumber = this.lineNumber;
	
		for (int i = 2; i < this.data.length(); i++) {
			if (i + 4 < this.data.length() && this.data.substring(i, i + 4).contains("then")) {
				// create condition
				this.left = new Node(sb.toString(), this.type, lineNumber);
				sb.setLength(0);
				i += 4;
				
			} else if (i + 5 < this.data.length() && this.data.substring(i, i + 5).contains("endif")) {
				this.right = new Node(sb.toString(), this.type, lineNumber);
				
			} else if (this.data.charAt(i) == '\n') {
				lineNumber++;
				
			} else {
				sb.append(this.data.charAt(i));
			}
		}
		
		this.data = "if";
		
		this.left.splitCondition();
		if (! this.right.data.equals("stop")) this.right.splitAssignment();		
	}
	
	
	private void splitCondition() {
		// get outer parentheses out of the way
		this.data = Util.removeOuterParentheses(this.data);
	
		// splits a condition, so what comes after if in an if statement
		splitAtOperator(new String[]{"||"});
		splitAtOperator(new String[]{"&&"});
		splitAtOperator(new String[]{"==", "!=", ">=", "<=", "> ", "< "});
		
		if (this.left != null) this.left.splitCondition();
		if (this.right != null) this.right.splitCondition();
	}

    
	private void splitAssignment() {
		// splits the assignment, so for example x = x + 1
		// this function is run only once for each assignment, in contrast with splitCalculation,
		// which is recursive
		int equalsSignPos = -1;
		for (int i = 0; i < this.data.length(); i++) {
			if (this.data.charAt(i) == '=' && equalsSignPos == -1) {
				equalsSignPos = i;
			} else if (this.data.charAt(i) == '=' && equalsSignPos != -1) {
				// another equals sign has been found, no good
				Error.onlyOneAssignment(this.type, this.lineNumber);
			}
		}
		
		// split assignment
		this.left = new Node(this.data.substring(0, equalsSignPos), this.type, this.lineNumber);
		this.right = new Node(this.data.substring(equalsSignPos + 1), this.type, this.lineNumber);
		this.data = "=";
		
		// check that the left part is not a calculation
		for (int i = 0; i < this.left.data.length(); i++) {
			if (Util.isArithmeticOperator(this.left.data.charAt(i))) {
				Error.noCalculationBeforeEqualsSign(this.type, this.lineNumber, this.left.data);
				break;
			}
		}
		
		// split the right part
		this.right.splitCalculation();
	}


	private void splitCalculation() {
		// get outer parentheses out of the way
		this.data = Util.removeOuterParentheses(this.data);
	
		// splits a calculation, so what comes after the equals sign in an assignment
		splitAtOperator(new String[]{"+", "-"});
		splitAtOperator(new String[]{"*", "/"});
		this.splitAtFunction(new String[]{"sqrt", "root", "pow", "ln", "log", "logbase", "sin", "cos", "tan"});
		
		if (this.left != null) this.left.splitCalculation();
		if (this.right != null) this.right.splitCalculation();
	}
	
	
	private void splitAtOperator(String[] operators) {
		int opLen = operators[0].length(); // operator length, assuming all operators have the same length
		String currStr = new String(); // current string
		int netParCount = 0;
		boolean stop = false;
	
		for (int i = this.data.length() - 1; i >= 0 && stop == false; i--) {
			if (i + opLen < this.data.length()) {
				currStr = this.data.substring(i, i + opLen);
			}
			
			if (this.data.charAt(i) == '(') {
				netParCount++;
			} else if (this.data.charAt(i) == ')') {
				netParCount--;
			} else if (netParCount == 0) {
				for (String op : operators) {
					if (currStr.equals(op)) {
						splitAtPosition(i, opLen);
						stop = true;
						break;
					}
				}
			}
		}	
	}
	
	
	private void splitAtPosition(int position, int length) {
		// splits this.data at a certain string
		if (position + length < this.data.length()) {
			this.left = new Node(this.data.substring(0, position), this.type, this.lineNumber);
			this.right = new Node(this.data.substring(position + length), this.type, this.lineNumber);
			this.data = this.data.substring(position, position + length);
		}
	}
	
	
	private void splitAtFunction(String[] functions) {	
		// find out if the current node contains a function
		// note: the requirements of a string being a function are: must contain a '(', must not start with '(',
		// must end with ')', and must not contain an operator outside the parentheses
		int indexOfParenthesis = -1;
		
		for (int i = 0; i < this.data.length(); i++) {
			if (this.data.charAt(i) == '(') {
				indexOfParenthesis = i;
				break;
			} else if (Util.isArithmeticOperator(this.data.charAt(i))) {
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
				Error.noSuchFunction(this.type, this.lineNumber, functionName);
			} else {
				// get arguments
				// note: functions can have at most 2 arguments in CML
				int commasEncountered = 0;
				StringBuilder sb = new StringBuilder();
				
				for (int i = indexOfParenthesis + 1; i < this.data.length(); i++) {
					if (this.data.charAt(i) == ',') {
						commasEncountered++;
						if (commasEncountered > 1) {
							Error.tooManyArguments(this.type, this.lineNumber, functionName);
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
					Error.notEnoughArguments(this.type, this.lineNumber, functionName);
					
				} else if (Util.isOneArgFunction(functionName) && arg2.length() != 0) {					
					// functions that take only one argument
					Error.tooManyArguments(this.type, this.lineNumber, functionName);
					
				} else if (Util.isTwoArgsFunction(functionName) && arg2.length() == 0) {
					// functions that take only two arguments
					Error.notEnoughArguments(this.type, this.lineNumber, functionName);
				}
			}
				
			// finish arranging the function into the current node
			if (! Interpreter.error) {
				this.left = new Node(arg1, this.type, this.lineNumber);
				
				// not all functions have a second argument
				if (arg2.length() != 0) {
					this.right = new Node(arg2, this.type, this.lineNumber);
				}
				
				this.data = functionName;
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