import interpreter.*;
import graphing.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

//UI
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jfree.chart.JFreeChart;
import ui.*;


class Main {

	public static JFrameWindow appWindow;
	public static JFreeChart currentChart;

	public static Runnable UIThread;
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

		//Creating graph on start, needs to change to graph button eventually
		currentChart = CreateGraph.setData(chartData, 0, 1);

	}


	private static ArrayList<ArrayList<String>> interpretData(){
		String variables = getContentsOfFile("variables");
		String rules = getContentsOfFile("rules");

		//Check UI for iterations, 10 is default for now
		int iterations = 100;

		return Interpreter.interpret(variables, rules, iterations);
	}

	private static void setUpUI(){

		// following two lines are not important
		String s = UIManager.getSystemLookAndFeelClassName();
		System.out.println(s);

		// boilerplate code needed for setting up the window, don't delete
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				appWindow = new JFrameWindow();
				updateGraph();
			}

			public void updateGraph(){
				appWindow.updateGraph(currentChart);
			}
		});
	}
}