package interpreter;

import java.util.LinkedList;
import java.lang.NumberFormatException;

public class Interpreter {

	// this becomes true if an error is encountered; functions of the interpreter will then not execute
	static boolean error = false;
	
	static int numberOfTimesSplit = 0;

	static void debugPrint() {
		for (Variable v : Variable.list) {
			v.print();
		}
		
		System.out.print("\n\n");
		
		for (Node n : Node.list) {
			n.print();
		}
		
		System.out.println(numberOfTimesSplit);
	}
	
	
	public static void start(String variables, String rules, int iterations) {
		// append a newline to both strings if there isn't one already,
		// this makes life easier for us and the code more readable
		if (variables.charAt(variables.length() - 1) != '\n') {
			variables = variables.concat("\n");
		}
		
		if (rules.charAt(rules.length() - 1) != '\n') {
			rules = rules.concat("\n");
		}

		// parse variables and rules
		Parser.parse(variables, Parser.VARIABLES);
		Parser.parse(rules, Parser.RULES);
		
		// remove this before final release
		debugPrint();

		// calculate variables according to rules
		/*for (int i = 1; i <= iterations; i++) {
			System.out.println("Iteration " + i);
		}*/
		
		/*for (Node n : Node.list) {
			Executer.executeRule(n);
		}*/
		
		//double a = Math.sin(3.1415 * 0.5);
		//System.out.println(a);
	}
}
