import interpreter.*;
import graphing.*;


import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

//UI
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jfree.chart.JFreeChart;
import org.xml.sax.ErrorHandler;
import ui.*;


public class Main {

	public static void main(String[] args) {
		extractFiles();
		CreateGraph.setUpUI();
	}
	private static void extractFiles() {
		String[] filesToExtract	= {"rules", "variables", "iterations"};

		for (String fileName: filesToExtract) {
			File file = new File(fileName);
			if (!file.exists()) {
				try (InputStream inputStream = Main.class.getResourceAsStream(fileName);
					 OutputStream outputStream = new FileOutputStream(file)) {
					byte[] buffer = new byte[1024];
					int length;
					while (true) {
                        assert inputStream != null;
                        if (!((length = inputStream.read(buffer)) > 0)) break;
                        outputStream.write(buffer, 0, length);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}