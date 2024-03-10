package interpreter;

class Error {

	static String place = "location";

	public static void noRulesInVariables(int lineNumber) {
		errorHandler("variables", lineNumber, "", "rules may not be declared in the variable initialization list");
	}

	public static void parenthesesCount(int lineNumber) {
		errorHandler("rules", lineNumber, "",
			"opening parentheses count does not match closing parentheses count");
	}
	
	public static void onlyOneAssignment(String place, int lineNumber) {
		errorHandler(place, lineNumber, "", "only one assignment is allowed per line");
	}

	public static void invalidVariableInitializer(int lineNumber, String value) {
		errorHandler("variables", lineNumber, value, "variable initializer needs to be a number");
	}
	
	public static void noSuchVariable(int lineNumber, String variable) {
		errorHandler("rules", lineNumber, variable, "no such variable has been declared");
	}
	
	public static void noSuchFunction(String place, int lineNumber, String function) {
		errorHandler(place, lineNumber, function, "no such function exists");
	}
	
	public static void notEnoughArguments(String place, int lineNumber, String function) {
		errorHandler(place, lineNumber, function, "not enough arguments were provided to this function");
	}
	
	public static void tooManyArguments(String place, int lineNumber, String function) {
		errorHandler(place, lineNumber, function, "too many arguments were provided to this function");
	}
	
	static void errorHandler(String place, int lineNumber, String symbol, String errorMsg) {
		StringBuffer sb = new StringBuffer();
		sb.append(place + ":");
		sb.append(Integer.toString(lineNumber) + ": ");
		sb.append("error: ");
		if (symbol.length() != 0) sb.append(symbol + ": ");
		sb.append(errorMsg + "\n");
		Interpreter.errorBuffer += sb.toString();
		Interpreter.error = true;
	}	
}