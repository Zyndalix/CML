package interpreter;

class Parser {

	// modes for the parse() function
	static final int RULES = 0;
	static final int VARIABLES = 1;
	
	static String prepare(String input) {
		// append a newline to the input string if there isn't one already,
		// this simplifies extracting stuff from it later on
		if (input.charAt(input.length() - 1) != '\n') {
			input = input.concat("\n");
		}
		
		return input;
	}

	static void extract(String input, int mode) {
	
		StringBuilder sb = new StringBuilder();
		String before = new String(); // before the = sign
		String after = new String(); // after the = sign
		boolean ignore = false; // for comments

		for (int i = 0; i < input.length(); i++) {
		
			if (input.charAt(i) == '\n') {
				// parse this line and reset everything for the next line
				after = sb.toString();
				sb.setLength(0);
				ignore = false;
				
				if (before.length() != 0 && after.length() != 0) {
					if (mode == VARIABLES) {
						Variable.list.add(new Variable(before, Double.parseDouble(after)));
					} else {
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