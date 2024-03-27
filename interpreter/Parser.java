package interpreter;

class Parser {
	
	static String appendNewLine(String input) {
		// append a newline to the input string if there isn't one already,
		// this simplifies extracting stuff from it later on
		if (input.charAt(input.length() - 1) != '\n') {
			input = input.concat("\n");
		}
		
		return input;
	}
	
	
	static String stripUnneededChars(String input) {
		// removes comments and spaces
		StringBuilder sb = new StringBuilder();
		boolean ignore = false;
		
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '\n') {
				ignore = false;
				sb.append('\n');
			} else if (input.charAt(i) == '\'') {
				ignore = true;
			} else if (! ignore && input.charAt(i) != ' ' && input.charAt(i) != '\t') {
				sb.append(input.charAt(i));
			}
		}
		
		return sb.toString();
	}
	
	
	static void checkParenthesesCount(String input, char mode) {
		// checks for every line if there are just as many closing parentheses as opening parentheses
		int lineNumber = 0;
		int netParCount = 0;
		
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '\n') {
				lineNumber++;
				if (netParCount != 0) {
					Error.parenthesesCount(mode, lineNumber);
					netParCount = 0;
				}
			} else if (input.charAt(i) == '(') {
				netParCount++;
			} else if (input.charAt(i) == ')') {
				netParCount--;
			}
		}
	}
	
	
	static String replaceNegativeSigns(String input) {
		// replaces, for example, x*-x with x*(0-x) to avoid operator conflicts
		// we start at 1 so we can go back one char without getting an out of bounds exception
		int currentPosition = 1;
		char currCh = 0, prevCh = 0; // current and previous char
		
		while (currentPosition != input.length() - 1) {
			int signLocation = -1;
			
			for (; currentPosition < input.length() - 1 && signLocation == -1; currentPosition++) {
				currCh = input.charAt(currentPosition);
				prevCh = input.charAt(currentPosition - 1);
				
				if (currCh == '-' && (Util.isArithmeticOperator(prevCh) || prevCh == '=' || prevCh == '(') ) {
					signLocation = currentPosition;
				}
			}
			
			if (signLocation != -1) {
				// find out to which variable or value the negative sign applies
				int end = 0;
				int netParCount = 0;
				String appliesTo;
				StringBuilder sb = new StringBuilder();
				
				for (int i = signLocation + 1; i < input.length(); i++) {
					currCh = input.charAt(i);
					if (currCh == '(') {
						netParCount++;
					} else if (currCh == ')') {
						netParCount--;
					} else if (currCh == '\n' || Util.isArithmeticOperator(currCh) && netParCount == 0) {
						end = i;
						break;
					}
				}
				
				appliesTo = input.substring(signLocation + 1, end);
				
				// replace -x with (0-x)
				sb.append(input.substring(0, signLocation));
				sb.append("(0-" + appliesTo + ")");
				sb.append(input.substring(end));
				input = sb.toString();
			}	
		}
		
		return input;
	}
	
	
	static String replacePowers(String input) {
		// replaces all instances of a^b with pow(a, b)
		int currentPosition = 0;
		char currCh = 0; // current char
		
		while (currentPosition != input.length() - 1) {
			int caretLocation = -1;
			
			for (; currentPosition < input.length() - 1 && caretLocation == -1; currentPosition++) {
				currCh = input.charAt(currentPosition);
				
				if (currCh == '^') {
					caretLocation = currentPosition;
				}
			}
			
			if (caretLocation != -1) {
				// find out where boundaries are
				int start = 0, end = 0;
				int netParCount = 0;
				String base, exponent;
				StringBuilder sb = new StringBuilder();
				
				for (int i = caretLocation - 1; i > 0; i--) {
					currCh = input.charAt(i);
					if (currCh == '(') {
						netParCount++;
						
						if (netParCount > 0) {
							// reset so it doesn't mess up the counting for the exponent
							netParCount = 0;
							start = i + 1;
							break;
						}
					} else if (currCh == ')') {
						netParCount--;
					} else if (currCh == '=' || Util.isArithmeticOperator(currCh) && netParCount == 0) {
						start = i + 1;
						break;
					}
				}
				
				for (int i = caretLocation + 1; i < input.length(); i++) {
					currCh = input.charAt(i);
					if (currCh == '(') {
						netParCount++;
					} else if (currCh == ')') {
						netParCount--;
						
						if (netParCount < 0) {
							// we don't need to reset netParCount, since it's never used after this
							end = i;
							break;
						}
					} else if (currCh == '\n' || Util.isArithmeticOperator(currCh) && netParCount == 0) {
						end = i;
						break;
					}
				}
				
				// get base and exponent
				base = input.substring(start, caretLocation);
				exponent = input.substring(caretLocation + 1, end);
				
				// replace a^b with pow(a, b)
				sb.append(input.substring(0, start));
				sb.append("pow(" + base + "," + exponent + ")");
				sb.append(input.substring(end));
				input = sb.toString();
			}
		}
		
		return input;
	}
	
	
	static String replaceGtAndLtSigns(String input) {
		// replaces ">" and "<" with "> " and "< " to simplify the parsing process (notice the spaces)
		StringBuilder sb = new StringBuilder();
						
		for (int i = 0; i < input.length(); i++) {
			if (i < input.length() - 1 && input.charAt(i) == '>' && input.charAt(i + 1) != '=') {
				sb.append("> ");
			} else if (i < input.length() - 1 && input.charAt(i) == '<' && input.charAt(i + 1) != '=') {
				sb.append("< ");
			} else {
				sb.append(input.charAt(i));
			}
		}
		
		return sb.toString();
	}
	

	static void extract(String input, char mode) {
		
		StringBuilder sb = new StringBuilder();
		int lineNumber = 1; // for error messages

		for (int i = 0; i < input.length(); i++) {
		
			if (input.charAt(i) == '\n') {
				// parse this line and reset everything for the next line
				if (sb.length() != 0 && mode == 'v') {
					Node root = new Node(sb.toString(), mode, lineNumber);
					Interpreter.variableList.add(root);
					sb.setLength(0);
					
				} else if (sb.length() != 0 && mode == 'r') {
					Node root = new Node(sb.toString(), mode, lineNumber);
					Interpreter.ruleList.add(root);
					sb.setLength(0);
				}
				
				lineNumber++;
				
			} else if (mode == 'v' && i + 2 < input.length() && input.substring(i, i + 2).contains("if")) {
				Error.noIfStatementsInVariables(lineNumber);
				
			} else if (mode == 'r' && i + 2 < input.length() && input.substring(i, i + 2).contains("if")) {
				// first condition of above if statement is put there to prevent index out of bounds errors
				// if statement encountered; search for "endif"
				for (int j = i + 2; j < input.length(); j++) {
					if (j + 5 < input.length() && input.substring(j, j + 5).contains("endif")) {
						Node root = new Node(input.substring(i, j + 5) + '\n', mode, lineNumber);
						Interpreter.ruleList.add(root);
						i = j + 5;
						break;
					}
				}
				
			} else {
				// all other cases
				sb.append(input.charAt(i));
			}
		}
	}
	
	
	static void computeInitialValues() {
		// compute initial values of variables before the main loop is run
		for (Node n : Interpreter.variableList) {
			if (! Util.isNumber(n.right.data)) {
				n.right = new Node(Double.toString(Executer.getValue(n.right)), n.right.type, n.right.lineNumber);
			}
		}
	}
}