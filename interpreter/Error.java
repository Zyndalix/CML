package interpreter;

class Error {

	static String place = "location";

	public static void invalidVariableInitializer(int line, String var) {
		errorHandler("variables", line, var, "variable initializer needs to be a number");
	}
	
	public static void noSuchVariable(String var) {
		errorHandler(place, 0, var, "no such variable has been declared");
	}
	
	public static void noSuchFunction(String func) {
		errorHandler(place, 0, func, "no such function exists");
	}
	
	public static void notEnoughArguments(String func) {
		errorHandler(place, 0, func, "not enough arguments were provided to this function");
	}
	
	public static void tooManyArguments(String func) {
		errorHandler(place, 0, func, "too many arguments were provided to this function");
	}
	
	static void errorHandler(String place, int line, String symbol, String errorMsg) {
		Interpreter.errorBuffer = Interpreter.errorBuffer +
			place + ":" + Integer.toString(line) + ": error: " + symbol + ": " + errorMsg + "\n";
		Interpreter.error = true;
	}
	
}