package interpreter;

import java.util.LinkedList;

// this is for the abstract syntax tree; each rule has its own binary tree
class Node {

	static LinkedList<Node> list = new LinkedList<Node>();

	String data;
	Node left = null;
	Node right = null;

	Node(String data) {
		this.data = data;
	}

	void print() {
        print("", this, false);
    }

    void print(String prefix, Node n, boolean isLeft) {
        if (n != null) {
            System.out.println(prefix + (isLeft ? "├── " : "└── ") + n.data);
            print(prefix + (isLeft ? "│   " : "    "), n.left, true);
            print(prefix + (isLeft ? "│   " : "    "), n.right, false);
        }
    }
    
    boolean isOperator() {
    	String[] operators = {"+", "-", "*", "/", "sin", "cos", "tan"};
    	for (String operator : operators) {
    		if (this.data.equals(operator)) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    boolean isNumber() {
    	boolean success = true;
    	
    	try {
			Double.parseDouble(this.data);
		} catch(NumberFormatException e) {
			success = false;
		}
		
		return success;
    }
}