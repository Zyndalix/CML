package interpreter;

class Executer {

	static void executeRule(Node rule) {
		if (rule.data.equals("=")) {
			// calculate the new value of the variable
			Node varToTest = getVariable(rule.left.data);
			if (varToTest != null) {
				varToTest.right.data = Double.toString(getValue(rule.right));
			} else {
				Error.noSuchVariable(rule.left.lineNumber, rule.left.data);
			}
		}
	}
	
	
	static Node getVariable(String searchName) {
		for (Node n : Interpreter.variableList) {
			if (n.left.data.equals(searchName)) {
				return n;
			}
		}
		return null;
	}
	
	
	static double getValue(Node n) {
		// return the value of Node n, for example the value of a variable that is referenced in Node n
		double value = 0;
		if (! Util.isSymbol(n.data)) {
			if (Util.isNumber(n.data)) {
				value = Double.parseDouble(n.data);
			} else if (getVariable(n.data) != null) {
				// n is probably a variable name
				value = Double.parseDouble(getVariable(n.data).right.data);
			} else if (n.data.equals("e")) {
				value = Math.exp(1);
			} else if (n.data.equals("pi") || n.data.equals("π")) {
				value = Math.PI;
			} else {
				Error.noSuchVariable(n.lineNumber, n.data);
			}
		} else {
			value = calculate(n);
		}
		return value;
	}
	
	
	static double calculate(Node equation) {
		// calculate the result of a mathematical operation stored in Node equation
		double result = 0, left = 0, right = 0;
		
		// get value for the left operand
		if (equation.left != null) {
			left = getValue(equation.left);
		}
		
		// get value for the right operand
		if (equation.right != null) {
			right = getValue(equation.right);
		}
		
		if (equation.data.equals("+")) {
			result = left + right;
		} else if (equation.data.equals("-")) {
			result = left - right;
		} else if (equation.data.equals("*")) {
			result = left * right;
		} else if (equation.data.equals("/")) {
			result = left / right;
		} else if (equation.data.equals("sqrt")) {
			result = Math.sqrt(left);
		} else if (equation.data.equals("root")) {
			result = Math.pow(right, 1 / left);
		} else if (equation.data.equals("pow")) {
			result = Math.pow(left, right);
		} else if (equation.data.equals("log")) {
			result = Math.log(left) / Math.log(10);
		} else if (equation.data.equals("logbase")) {
			result = Math.log(right) / Math.log(left);
		} else if (equation.data.equals("ln")) {
			result = Math.log(left);
		} else if (equation.data.equals("sin")) {
			result = Math.sin(left);
		} else if (equation.data.equals("cos")) {
			result = Math.cos(left);
		} else if (equation.data.equals("tan")) {
			result = Math.tan(left);
		}

		return result;
	}
}