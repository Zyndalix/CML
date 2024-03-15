package interpreter;

class Executer {

	static void executeRule(Node rule) {
		if (rule.data.equals("=")) {
			// assignment: calculate the new value of the variable
			Node varToTest = getVariable(rule.left.data);
			if (varToTest != null) {
				varToTest.right.data = Double.toString(getValue(rule.right));
			} else {
				Error.noSuchVariable(rule.left.lineNumber, rule.left.data);
			}
		} else if (rule.data.equals("if")) {
			// if statement
			if (evaluateCondition(rule.left)) {
				executeRule(rule.right);
			}
		} else if (rule.data.equals("stop")) {
			// stop program execution
			Interpreter.stop = true;
		}
	}
	
	
	static boolean evaluateCondition(Node condition) {
		if (Util.isComparisonOperator(condition.data)) {
			return evaluateComparison(condition);
		}
		
		boolean left = false, right = false;
		
		if (Util.isLogicalOperator(condition.left.data)) {
			left = evaluateCondition(condition.left);
		} else {			
			left = evaluateComparison(condition.left);
		}
		
		if (Util.isLogicalOperator(condition.right.data)) {
			right = evaluateCondition(condition.right);
		} else {			
			right = evaluateComparison(condition.right);
		}
		
		if (condition.data.equals("||")) {
			if (left || right) {
				return true;
			}
		} else if (condition.data.equals("&&")) {
			if (left && right) {
				return true;
			}
		}
		
		return false;
	}
	
	
	static boolean evaluateComparison(Node comparison) {	
		double left = 0, right = 0;
		
		left = getValue(comparison.left);
		right = getValue(comparison.right);
		
		if (comparison.data.equals("==")) {
			if (left == right) return true;
		} else if (comparison.data.equals("!=")) {
			if (left != right) return true;
		} else if (comparison.data.equals(">=")) {
			if (left >= right) return true;
		} else if (comparison.data.equals("<=")) {
			if (left <= right) return true;
		} else if (comparison.data.equals("> ")) {
			if (left > right) return true;
		} else if (comparison.data.equals("< ")) {
			if (left < right) return true;
		}
		
		return false;
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
			} else if (n.data.equals("true")) {
				value = 1;
			} else if (n.data.equals("false")) {
				value = 0;
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
		
		left = getValue(equation.left);
		
		// not all operators have two operands
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