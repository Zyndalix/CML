import interpreter.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

//UI
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.*;


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
		setUpUI();

		//Interpreting data in main right now, needs to change to button in UI
		ArrayList<ArrayList<String>> chartData = interpretData();

	}

	private static ArrayList<ArrayList<String>> interpretData(){
		String variables = getContentsOfFile("variables");
		String rules = getContentsOfFile("rules");

		//Check UI for iterations, 10 is default for now
		int iterations = 10;

		return Interpreter.main(variables, rules, iterations);
	}

	private static void setUpUI(){
		//Still need to set up UI in this method

		// following two lines are not important
		String s = UIManager.getSystemLookAndFeelClassName();
		System.out.println(s);

		// boilerplate code needed for setting up the window, don't delete
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrameWindow window = new JFrameWindow();
			}
		});
	}
}
