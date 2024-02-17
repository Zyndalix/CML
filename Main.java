import interpreter.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;


class Main {

	static String getContentsOfFile(String file) {
		String data = new String();
		try {
			Scanner scanner = new Scanner( new File(file) );
			while (scanner.hasNextLine()) {
				data = data.concat(scanner.nextLine());
				if (scanner.hasNextLine()) {
					data = data.concat("\n");
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Could not open file");
			e.printStackTrace();
		}
		return data;
	}

	public static void main(String[] args) {

		String variables = getContentsOfFile("variables");
		String rules = getContentsOfFile("rules");
		int iterations = 10;

		ArrayList<ArrayList<String>> chartData = Interpreter.main(variables, rules, iterations);
	}
}
