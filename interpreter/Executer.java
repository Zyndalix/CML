package interpreter;

class Executer {

	static void executeRule(Node rule) {
		if (rule.data == "=") {
			// get the variable that the rule applies to
			Variable varToModify = Variable.get(rule.left.data);
			
			// calculate the new value of the variable
			if (varToModify != null) {
				varToModify.value = calculate(rule.right);
				System.out.println(varToModify.value);
			} else {
				Error.noSuchVariable(rule.left.data);
			}
			
		}
	}
	
	static double calculate(Node equation) {
		double result = 0;
		double left = 0, right = 0;
		
		// make sure that the equation is not just a mere number or variable
		if (equation.left == null && equation.right == null) {
		
			if (equation.isNumber()) {
				result = Double.parseDouble(equation.data);
			} else {
				// equation is probably a variable, test to see if variable exists
				Variable varToTest = Variable.get(equation.data);
				if (varToTest != null) {
					result = varToTest.value;
				} else {
					Error.noSuchVariable(equation.data);
				}
			}
			
		} else {
		
			// get value for the left operand
			if (equation.left.isOperator()) {
				left = calculate(equation.left);
			} else if (equation.left.isNumber()) {
				left = Double.parseDouble(equation.left.data);
			} else {
				// left node is probably a variable, test to see if variable exists
				Variable varToTest = Variable.get(equation.left.data);
				if (varToTest != null) {
					left = varToTest.value;
				} else {
					Error.noSuchVariable(equation.left.data);
				}
			}
			
			// get value for the right operand
			if (equation.right.isOperator()) {
				right = calculate(equation.right);
			} else if (equation.right.isNumber()) {
				right = Double.parseDouble(equation.right.data);
			} else {
				// right node is probably a variable, test to see if variable exists
				Variable varToTest = Variable.get(equation.right.data);
				if (varToTest != null) {
					right = varToTest.value;
				} else {
					Error.noSuchVariable(equation.right.data);
				}
			}
			
			if (equation.data.equals("+")) {
				result = left + right;
			} else if (equation.data.equals("-")) {
				result = left - right;
			} else if (equation.data.equals("*")) {
				result = left * right;
			} else if (equation.data.equals("/")) {
				result = left / right;
			}
			
		}

		return result;
	}
}