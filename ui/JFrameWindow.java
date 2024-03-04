package ui;

import com.sun.tools.javac.Main;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class JFrameWindow {
	private JFrame frame;
	public JFrameWindow() {
		initialize();
	}

	//Interpreter should log the tree to this variable so it can be added to the UI Textarea
	//Default is empty
	public static String treeText = "";

	//Interpreter should log the error to this variable so it can be added to the UI Textarea
	// if no errors this is default:
	public static String errorText = "No errors :)";

	public void initialize() {
		frame = new JFrame();
		frame.setTitle("Main Window");
		frame.setLocationRelativeTo(null);
		frame.setPreferredSize(new Dimension(800, 500));

		Dimension DimMax = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setMaximumSize(DimMax);
		frame.setPreferredSize(new Dimension(800, 500));
		frame.setMinimumSize(new Dimension(800, 500));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		/*JButton button1 = new JButton("Button 1");

		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1Pressed(e);
			}
		});
		panel.add(button1);*/
		AddComponentsToPane(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
	}

	public void AddComponentsToPane(Container pane){
		//Make grid bag layout and order different components into with set heights and widths
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 3;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.1;
		JLabel rulesLabel = new JLabel("RULES:");
		pane.add(rulesLabel, c);

		c.gridx = 4;
		c.gridy = 0;
		JLabel variablesLabel = new JLabel("VARIABLES:");
		pane.add(variablesLabel, c);

		c.gridx = 3;
		c.gridy = 1;
		c.weightx = 0.5;
		c.weighty = 0.4;
		c.fill = GridBagConstraints.BOTH;
		JTextArea rulesText = new JTextArea(0,0);
		JScrollPane rulesScrollPane = new JScrollPane(rulesText);
		pane.add(rulesScrollPane, c);



		c.gridx = 4;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		JTextArea variablesText = new JTextArea(0,0);
		JScrollPane variablesScrollPane = new JScrollPane(variablesText);
		pane.add(variablesScrollPane, c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridheight = 2;
		c.gridwidth = 2;
		JLabel consoleLabel = new JLabel("CONSOLE:");
		pane.add(consoleLabel, c);

		c.gridx = 3;
		c.gridy = 3;
		c.gridheight = 2;
		c.gridwidth = 2;
		c.ipady = frame.getHeight()/2;
		c.ipadx = frame.getWidth()/2;
		c.fill = GridBagConstraints.BOTH;
		JTextArea treeTextArea = new JTextArea(0,0);
		treeTextArea.setEditable(false);
		treeTextArea.setText(treeText);
		JScrollPane treeScrollPane = new JScrollPane(treeTextArea);
		pane.add(treeScrollPane, c);
	}

	public void updateGraph(JFreeChart chart, ArrayList<String> lines, int xAxisIndex){

		ChartPanel addPanel = new ChartPanel(chart);
		addPanel.setBackground(Color.GREEN);
		addPanel.setSize(frame.getWidth()/2, frame.getHeight()/2);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.gridwidth = 2;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.ipady = frame.getHeight()/2;
		c.ipadx = frame.getWidth()/2;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		frame.getContentPane().add(addPanel, c);


		c.gridx = 0;
		c.gridy = 3;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 0.2;
		c.weighty = 0.2;
		c.ipady = 0;
		c.ipadx = 0;

		JPanel enabledLines = new JPanel();
		enabledLines.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel yAxisLabel = new JLabel("Show on Y-axis: ");
		enabledLines.add(yAxisLabel);

		//Remove x-axis from y-axis lines
		enabledLines.add(new CheckCombo().setComboBox(lines, xAxisIndex));

		JLabel xAxisLabel = new JLabel("X-axis: ");
		enabledLines.add(xAxisLabel);

		frame.getContentPane().add(enabledLines, c);
		frame.pack();
	}

	public void button1Pressed(ActionEvent actionEvent){
		System.out.println("Button1 pressed :)");
	}
}
