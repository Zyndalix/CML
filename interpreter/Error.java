package interpreter;

class Error {

	public static void parenthesesCount(char type, int lineNumber) {
		errorHandler(type, lineNumber, "",
			"opening parentheses count does not match closing parentheses count");
	}
	
	public static void onlyOneAssignment(char type, int lineNumber) {
		errorHandler(type, lineNumber, "", "only one assignment is allowed per line");
	}

	public static void invalidVariableInitializer(int lineNumber, String value) {
		errorHandler('v', lineNumber, value, "variable initializer needs to be a number");
	}
	
	public static void noSuchVariable(int lineNumber, String variable) {
		errorHandler('r', lineNumber, variable, "no such variable has been declared");
	}
	
	public static void noIfStatementsInVariables(int lineNumber) {
		errorHandler('v', lineNumber, "", "if statements are not allowed in the variables section");
	}
	
	public static void noCalculationBeforeEqualsSign(char type, int lineNumber, String calculation) {
		errorHandler(type, lineNumber, calculation, "calculations are not allowed before an equals sign");
	}
	
	public static void whatIsThis(char type, int lineNumber, String line) {
		errorHandler(type, lineNumber, line, "line is neither an assignment nor an if statement");
	}
	
	public static void noSuchFunction(char type, int lineNumber, String function) {
		errorHandler(type, lineNumber, function, "no such function exists");
	}
	
	public static void notEnoughArguments(char type, int lineNumber, String function) {
		errorHandler(type, lineNumber, function, "not enough arguments were provided to this function");
	}
	
	public static void tooManyArguments(char type, int lineNumber, String function) {
		errorHandler(type, lineNumber, function, "too many arguments were provided to this function");
	}
	
	static void errorHandler(char type, int lineNumber, String symbol, String errorMsg) {
		StringBuffer sb = new StringBuffer();
		if (type == 'v') {
			sb.append("variables:");
		} else {
			sb.append("rules:");
		}
		sb.append(Integer.toString(lineNumber) + ": ");
		sb.append("error: ");
		if (symbol.length() != 0) {
			sb.append(symbol + ": ");
		}
		sb.append(errorMsg + "\n");
		Interpreter.errorBuffer += sb.toString();
		Interpreter.error = true;
	}	
}