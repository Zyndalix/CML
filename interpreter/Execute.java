package interpreter;

class Execute {

	static void executeRule(Node rule) {
		if (rule.data == "=") {
			// get the variable that the rule applies to
			Variable varToModify = Variable.get(rule.left.data);
			
			// calculate the new value of the variable
			if (varToModify != null) {
				varToModify.value = calculate(rule.right);
				System.out.println(varToModify.value);
			} else {
				System.out.println("Error: " + rule.left.data + ": no such variable has been declared");
			}
			
		}
	}
	
	static double calculate(Node equation) {
		double result = 0;
		double left = 0, right = 0;
		
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
				System.out.println("Error: " + equation.left.data + ": no such variable has been declared");
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
				System.out.println("Error: " + equation.right.data + ": no such variable has been declared");
			}
		}
		
		if (equation.data == "+") {
			result = left + right;
		} else if (equation.data == "-") {
			result = left - right;
		} else if (equation.data == "*") {
			result = left * right;
		} else if (equation.data == "/") {
			result = left / right;
		}

		return result;
	}
}