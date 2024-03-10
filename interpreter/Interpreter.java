package interpreter;

import java.util.ArrayList;

public class Interpreter {

	// this becomes true if an error is encountered; functions of the interpreter will then not execute
	static boolean error;
	public static String errorBuffer;
	static ArrayList<ArrayList<String>> data;

	static void debugPrint() {
		System.out.println(errorBuffer);		
		
		for (Variable v : Variable.list) {
			v.print();
		}
		
		System.out.print("\n\n");
		
		for (Node n : Node.list) {
			n.print();
		}
		
		System.out.println(data);
	}
	
	
	public static ArrayList<ArrayList<String>> main(String variables, String rules, int iterations) {
		// pre-interpreting tasks are performed here
		// make sure global variables are properly initialized
		error = false;
		errorBuffer = new String();
		data = new ArrayList<ArrayList<String>>();
		Variable.list = new ArrayList<Variable>();
		Node.list = new ArrayList<Node>();
		
		// fix the raw strings		
		variables = Parser.appendNewLine(variables);
		rules = Parser.appendNewLine(rules);
		Parser.check(variables, Parser.VARIABLES);
		Parser.check(rules, Parser.RULES);
		if (! error) rules = Parser.replacePowers(rules);

		// place variables and rules in Variable.list and Node.list
		if (! error) Parser.extract(variables, Parser.VARIABLES);
		if (! error) Parser.extract(rules, Parser.RULES);
		
		// replace calls to functions with their respective results, so for example sin(pi) becomes 0
		//Parser.computeInitialValues();
		
		// start the actual interpretation
		// prepare the data array, and create an initial snapshot of all variables
		for (int i = 0; i < Variable.list.size(); i++) {
			data.add(new ArrayList<String>());
			data.get(i).add(Variable.list.get(i).name);
			data.get(i).add(Variable.list.get(i).value);
		}

		// calculate variables according to rules		
		// first iteration has been run already, that's why it starts at 1
		for (int iter = 1; iter < iterations && ! error; iter++) {
			for (int i = 0; i < Node.list.size() && ! error; i++) {
				Executer.executeRule(Node.list.get(i));
			}
			
			// create a snapshot of all variables during every iteration
			for (int i = 0; i < Variable.list.size() && ! error; i++) {
				data.get(i).add(Variable.list.get(i).value);
			}
		}
		
		// remove this before final release; comment this to make the interpreter quiet
		debugPrint();
		
		return data;
	}
}