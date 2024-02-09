package interpreter;

class Parse {

	// modes for the parse() function
	static final int RULES = 0;
	static final int VARIABLES = 1;

	static void parse(String input, int mode) {
	
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
						
						splitTree(root.right, new String[]{"+", "-"});
						splitTree(root.right, new String[]{"*", "/"});
						
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
	

	static void splitTree(Node assignment, String[] operators) {
		// order does matter; therefore, find out which operator must be processed first
		// note: due to the way that binary trees work, operators are added from last to first
		
		// this array stores the indices of the *last* operators of each type that occur in 
		// the assignment string; the array maps 1:1 to the operators[] array, which is passed as
		// a parameter to this function
		int[] locations = new int[operators.length];

		for (int i = 0; i < operators.length; i++) {
			locations[i] = assignment.data.lastIndexOf(operators[i]);
		}
		
		// find out which index is the largest (since the last operator to process lies at that index)
		int largestIndex = -1;
		for (int i = 0; i < locations.length; i++) {
			if (locations[i] > largestIndex) {
				largestIndex = locations[i];
			}
		}
		
		// only continue if any operator was found
		if (largestIndex != -1) {
			// also find out which operator lies at the largest index
			String lastOperator = new String();
			for (int i = 0; i < locations.length; i++) {
				if (locations[i] == largestIndex) {
					lastOperator = operators[i];
					break;
				}
			}

			// perform the actual splitting
			String left = assignment.data.substring(0, largestIndex);
			String right = assignment.data.substring(largestIndex + lastOperator.length());
			
			assignment.left = new Node(left);
			assignment.right = new Node(right);
			assignment.data = lastOperator;
		}
		
		// also do this for the subtrees, if they exist
		if (assignment.left != null) {
			splitTree(assignment.left, operators);
		}

		if (assignment.right != null) {
			splitTree(assignment.right, operators);
		}
	}
	
}