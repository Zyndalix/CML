package interpreter;

import java.util.ArrayList;

public class Interpreter {

	// this becomes true if an error is encountered; functions of the interpreter will then not execute
	static boolean error;
	public static String errorBuffer;
	// for additional possibly interesting information for the user, such as the Abstract Syntax Trees
	// of each variable and rule
	public static String diagnosticBuffer;
	static ArrayList<Node> variableList;
	static ArrayList<Node> ruleList;
	static ArrayList<ArrayList<String>> data;
	
	
	public static ArrayList<ArrayList<String>> main(String variables, String rules, int iterations) {
		// pre-interpreting tasks are performed here
		// make sure global variables are properly initialized
		error = false;
		errorBuffer = new String();
		diagnosticBuffer = new String();
		data = new ArrayList<ArrayList<String>>();
		variableList = new ArrayList<Node>();
		ruleList = new ArrayList<Node>();
		
		// fix the raw strings		
		variables = Parser.appendNewLine(variables);
		rules = Parser.appendNewLine(rules);
		Parser.check(variables, Parser.VARIABLES);
		Parser.check(rules, Parser.RULES);
		if (! error) rules = Parser.replacePowers(rules);

		// place variables and rules in variableList and ruleList
		if (! error) Parser.extract(variables, Parser.VARIABLES);
		if (! error) Parser.extract(rules, Parser.RULES);
		
		// store the Abstract Syntax Trees of all variables and rules
		diagnosticBuffer += "Variables:\n";
		for (Node n : variableList) n.print();
		diagnosticBuffer += "\n\nRules:\n";	
		for (Node n : ruleList) n.print();
		
		// replace calls to functions with their respective results, so for example sin(pi) becomes 0
		Parser.computeInitialValues();
		
		// start the actual interpretation
		// prepare the data array, and create an initial snapshot of all variables
		for (int i = 0; i < variableList.size(); i++) {
			data.add(new ArrayList<String>());
			data.get(i).add(variableList.get(i).left.data);
			data.get(i).add(variableList.get(i).right.data);
		}

		// calculate variables according to rules		
		// first iteration has been run already, that's why it starts at 1
		for (int iter = 1; iter < iterations && ! error; iter++) {
			for (int i = 0; i < ruleList.size() && ! error; i++) {
				Executer.executeRule(ruleList.get(i));
			}
			
			// create a snapshot of all variables during every iteration
			for (int i = 0; i < variableList.size() && ! error; i++) {
				data.get(i).add(variableList.get(i).right.data);
			}
		}
		
		// remove this before final release; comment this to make the interpreter quiet
		System.out.println(errorBuffer);
		System.out.println(diagnosticBuffer);
		System.out.println(data);
		
		return data;
	}
}