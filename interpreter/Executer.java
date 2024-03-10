package interpreter;

class Executer {

	static void executeRule(Node rule) {
		if (rule.data.equals("=")) {
			// calculate the new value of the variable
			Variable varToTest = Variable.get(rule.left.data);
			if (varToTest != null) {
				varToTest.value = Double.toString(getValue(rule.right));
			} else {
				Error.noSuchVariable(rule.left.lineNumber, rule.left.data);
			}
		}
	}
	
	static double getValue(Node n) {
		double value = 0;
		if (! Util.isSymbol(n.data)) {
			if (Util.isNumber(n.data)) {
				value = Double.parseDouble(n.data);
			} else if (Variable.get(n.data) != null) {
				// n is probably a variable name
				value = Double.parseDouble(Variable.get(n.data).value);
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