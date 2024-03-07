package interpreter;

import java.util.ArrayList;

public class Interpreter {

	// this becomes true if an error is encountered; functions of the interpreter will then not execute
	static boolean error = false;
	public static ArrayList<ArrayList<String>> data = new ArrayList<>();
	static int numberOfTimesSplit = 0;

	static void debugPrint() {
		for (Variable v : Variable.list) {
			v.print();
		}
		
		System.out.print("\n\n");
		
		for (Node n : Node.list) {
			n.print();
		}
		
		// uncomment this for an interesting statistic
		//System.out.println("Number of times the splitAtSymbol() function has been called: " + numberOfTimesSplit);
		
		System.out.println(data);
	}
	
	
	public static ArrayList<ArrayList<String>> interpret(String variables, String rules, int iterations) {
		// pre-interpreting tasks are performed here
		variables = Parser.prepare(variables);
		rules = Parser.prepare(rules);

		// parse variables and rules
		Parser.extract(variables, Parser.VARIABLES);
		Parser.extract(rules, Parser.RULES);
		
		// prepare the data array
		for (int i = 0; i < Variable.list.size(); i++) {
			data.add(new ArrayList<>());
			data.get(i).add(Variable.list.get(i).name);
		}

		// calculate variables according to rules		
		for (int iter = 0; iter < iterations && ! error; iter++) {
			for (int i = 0; i < Node.list.size() && ! error; i++) {
				Executer.executeRule(Node.list.get(i));
			}
			
			// create a snapshot of all variables
			for (int i = 0; i < Variable.list.size() && ! error; i++) {
				data.get(i).add(Double.toString(Variable.list.get(i).value));
			}
		}
		
		// remove this before final release; comment this to make the interpreter quiet
		debugPrint();
		
		return data;
	}
}