package interpreter;

import graphing.CreateGraph;

import java.util.ArrayList;

public class Interpreter {

	// this becomes true if an error is encountered; functions of the interpreter will then not execute
	static boolean			error;
	public static String	errorBuffer;
	// this becomes true if the "stop" keyword is used; interpreter will run one last time and quit
	static boolean			stop;
	// for additional possibly interesting information for the user, such as the Abstract Syntax Trees
	// of each variable and rule
	public static String	diagnosticBuffer;
	static ArrayList<Node>	variableList;
	static ArrayList<Node>	ruleList;
	static ArrayList<ArrayList<String>> data;
	
	
	public static ArrayList<ArrayList<String>> interpret(String variables, String rules, int iterations) {
		// pre-interpreting tasks are performed here
		// make sure global variables are properly initialized
		error				= false;
		stop				= false;
		errorBuffer			= new String();
		diagnosticBuffer	= new String();
		variableList		= new ArrayList<Node>();
		ruleList			= new ArrayList<Node>();
		data				= new ArrayList<ArrayList<String>>();
		
		// fix the raw strings		
		variables	= Parser.appendNewLine(variables);
		rules		= Parser.appendNewLine(rules);
		variables	= Parser.stripUnneededChars(variables);
		rules		= Parser.stripUnneededChars(rules);
		Parser.checkParenthesesCount(variables, 'v');
		Parser.checkParenthesesCount(rules, 'r');		
		if (! error)	variables	= Parser.replaceNegativeSigns(variables);
		if (! error)	rules		= Parser.replaceNegativeSigns(rules);
		if (! error)	variables	= Parser.replacePowers(variables);
		if (! error)	rules		= Parser.replacePowers(rules);
		if (! error)	rules		= Parser.replaceGtAndLtSigns(rules);

		// place variables and rules in variableList and ruleList
		if (! error) Parser.extract(variables, 'v');
		if (! error) Parser.extract(rules, 'r');
		
		// organize each variable and rule into Abstract Syntax Trees
		if (! error) for (Node n : variableList) n.split();
		if (! error) for (Node n : ruleList) n.split();
		
		if (! error) {		
			// store all initial values and the Abstract Syntax Trees of all variables and rules
			diagnosticBuffer += "Variables:\n";
			for (Node n : variableList) n.print();
			
			// replace calls to functions with their respective results, so for example sin(pi) becomes 0
			Parser.computeInitialValues();
			
			diagnosticBuffer += "\n\nPrecomputed values of variables:\n";
			for (Node n : variableList) diagnosticBuffer += n.left.data + " = " + n.right.data + "\n";
			
			diagnosticBuffer += "\n\nRules:\n";
			for (Node n : ruleList) n.print();
		}
		
		// start the actual interpretation
		// prepare the data array, and create an initial snapshot of all variables
		if (! error) {
			for (int i = 0; i < variableList.size(); i++) {
				data.add(new ArrayList<String>());
				data.get(i).add(variableList.get(i).left.data);
				data.get(i).add(variableList.get(i).right.data);
			}
		}

		// calculate variables according to rules		
		// first iteration has been run already, that's why it starts at 1
		for (int iter = 1; iter < iterations && ! error; iter++) {
            for (Node node : ruleList) {
                Executer.executeRule(node);
            }
			
			// create a snapshot of all variables during every iteration
			for (int i = 0; i < variableList.size(); i++) {
				data.get(i).add(variableList.get(i).right.data);
			}
			
			// stop program execution if needed
			if (stop) {
				break;
			}
		}
		
		// send information to the UI
		CreateGraph.appWindow.consoleLog(errorBuffer, 0);
		CreateGraph.appWindow.diagnosticsLog(diagnosticBuffer);
		
		return data;
	}
}