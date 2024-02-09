package interpreter;

import java.util.LinkedList;

class Variable {

	static LinkedList<Variable> list = new LinkedList<Variable>();
	
	// returns the variable that has the given name
	static Variable get(String searchName) {
		for (Variable v : list) {
			if (v.name.equals(searchName)) {
				return v;
			}
		}
		return null;
	}
	

	String name;
	double value;
	
	Variable(String name, double value) {
		this.name = name;
		this.value = value;
	}
	
	void print() {
		System.out.println(this.name + "    " + this.value);
	}
}
