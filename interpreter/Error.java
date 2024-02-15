package interpreter;

class Error {
	
	public static void noSuchVariable(String var) {
		System.out.println("Error: " + var + ": no such variable has been declared");
		Interpreter.error = true;
	}
	
	public static void noSuchFunction(String func) {
		System.out.println("Error: " + func + ": no such function exists");
		Interpreter.error = true;
	}
	
	public static void notEnoughArguments(String func) {
		System.out.println("Error: " + func + ": not enough arguments were provided to this function");
		Interpreter.error = true;
	}
	
	public static void tooManyArguments(String func) {
		System.out.println("Error: " + func + ": too many arguments were provided to this function");
		Interpreter.error = true;
	}
	
}