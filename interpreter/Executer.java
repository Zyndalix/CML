package interpreter;

class Executer {

	static void executeRule(Node rule) {
		if (rule.data == "=") {
			// get the variable that the rule applies to
			Variable varToModify = Variable.get(rule.left.data);
			
			// calculate the new value of the variable
			if (varToModify != null) {
				varToModify.value = getValue(rule.right);
			} else {
				Error.noSuchVariable(rule.left.data);
			}
			
		}
	}
	
	static double getValue(Node n) {
		double value = 0;
		if (! n.isSymbol()) {
			if (n.isNumber()) {
				value = Double.parseDouble(n.data);
			} else {
				// n is probably a variable, test to see if variable exists
				Variable varToTest = Variable.get(n.data);
				if (varToTest != null) {
					value = varToTest.value;
				} else {
					Error.noSuchVariable(n.data);
				}
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
			result = Math.log(right) / Math.log(10);
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