package interpreter;

class Parser {

	// modes for the parse() function
	static final int VARIABLES = 0;
	static final int RULES = 1;
	
	
	static String appendNewLine(String input) {
		// append a newline to the input string if there isn't one already,
		// this simplifies extracting stuff from it later on
		if (input.charAt(input.length() - 1) != '\n') {
			input = input.concat("\n");
		}
		
		return input;
	}
	
	
	static String replacePowers(String input) {
		// replaces all instances of a^b with pow(a, b)
		int currentPosition = 0;
		while (currentPosition != input.length() - 1) {
			int caretLocation = -1;
			for (; currentPosition < input.length() - 1 && caretLocation == -1; currentPosition++) {
				if (input.charAt(currentPosition) == '^') {
					caretLocation = currentPosition;
				}
			}
			
			if (caretLocation != -1) {			
				// find out where boundaries are
				int start = 0, end = 0;
				int netParCount = 0;
				String base, exponent;
				StringBuilder sb = new StringBuilder();
				
				for (int i = caretLocation; i > 0; i--) {
					if (input.charAt(i) == '(') {
						netParCount++;
					} else if (input.charAt(i) == ')') {
						netParCount--;
					} else if (input.charAt(i) == '=' || (Util.isOperator(input.charAt(i)) && netParCount == 0 )) {
						start = i + 1;
						break;
					}
				}
				
				for (int i = caretLocation; i < input.length(); i++) {
					if (input.charAt(i) == '(') {
						netParCount++;
					} else if (input.charAt(i) == ')') {
						netParCount--;
					} else if (input.charAt(i) == '\n' || (Util.isOperator(input.charAt(i)) && netParCount == 0 )) {
						end = i;
						break;
					}
				}
				
				// get base and exponent
				base = input.substring(start, caretLocation);
				exponent = input.substring(caretLocation + 1, end);
				
				// uncomment when debugging
				/*System.out.println("caretLocation: " + caretLocation);
				System.out.println("Start: " + start);
				System.out.println("End: " + end);
				System.out.println("Base: " + base);
				System.out.println("Exponent: " + exponent);*/
				
				// replace a^b with pow(a, b)
				sb.append(input.substring(0, start));
				sb.append("pow(" + base + ", " + exponent + ")");
				sb.append(input.substring(end));
				input = sb.toString();
			}
		}
		
		return input;
	}
	

	static void extract(String input, int mode) {
	
		StringBuilder sb = new StringBuilder();
		String before = new String(); // before the = sign
		String after = new String(); // after the = sign
		boolean ignore = false; // for comments
		int lineNumber = 0;

		for (int i = 0; i < input.length(); i++) {
		
			if (input.charAt(i) == '\n') {
				// parse this line and reset everything for the next line
				after = sb.toString();
				sb.setLength(0);
				ignore = false;
				lineNumber++;
				
				if (before.length() != 0 && after.length() != 0) {
					if (mode == VARIABLES) {
						// make sure that variable initializer is a number
						if (Util.isNumber(after)) {
							Variable.list.add(new Variable(before, Double.parseDouble(after)));
						} else {
							Error.invalidVariableInitializer(lineNumber, before);
						}
					} else if (mode == RULES) {
					
						// this line is a rule; organize it into the abstract syntax tree
						Node root = new Node("=");
						root.left = new Node(before);
						root.right = new Node(after);
						
						root.right.split();
						Node.list.add(root);
					}
				}
		
			} else if (ignore == true || input.charAt(i) == ' ') {
				; // we tell the interpreter to skip this char, we're either dealing with a space or a comment
			
			} else if (input.charAt(i) == '=') {
				before = sb.toString();
				sb.setLength(0);
			
			} else if (input.charAt(i) == '\'') {
				ignore = true;
			
			} else {
				// all other cases: we're either dealing with what's before or after the = sign
				sb.append(input.charAt(i));
			}

		}
	}
}