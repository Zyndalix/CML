package ui;

import com.sun.tools.javac.Main;
import graphing.CreateGraph;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.*;

public class JFrameWindow {
	private JFrame frame;
	public JFrameWindow() {
		initialize();
	}

	public Color greenColor;
	public Color blueColor;
	public Color redColor;

	//Interpreter should log the tree to this variable, so it can be added to the UI Textarea
	//Default is empty
	public static String treeText = "";

	//Interpreter should log the error to this variable, so it can be added to the UI Textarea
	//if no errors this is default:
	public static String errorText = "No errors :)";

	JFreeChart chart;
	ArrayList<String> lines;
	int xAxisIndex;

	public void initialize() {
		//Set standard colors for UI
		float[] greenHSB = new float[3];
		Color.RGBtoHSB(116, 255, 190, greenHSB);
		greenColor = Color.getHSBColor(greenHSB[0], greenHSB[1], greenHSB[2]);
		float[] blueHSB = new float[3];
		Color.RGBtoHSB(116, 153, 255, blueHSB);
		blueColor = Color.getHSBColor(blueHSB[0], blueHSB[1], blueHSB[2]);
		float[] redHSB = new float[3];
		Color.RGBtoHSB(255, 134, 116, redHSB);
		redColor = Color.getHSBColor(redHSB[0], redHSB[1], redHSB[2]);


		frame = new JFrame();
		frame.setTitle("CML");
		frame.setLocationRelativeTo(null);
		frame.setPreferredSize(new Dimension(800, 500));

		Dimension DimMax = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setMaximumSize(DimMax);
		frame.setPreferredSize(new Dimension(800, 500));
		frame.setMinimumSize(new Dimension(800, 500));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		ResizeListener resizeListener = new ResizeListener(this);
		frame.addComponentListener(resizeListener);

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
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.025;
		JLabel rulesLabel = new JLabel("RULES:");
		pane.add(rulesLabel, c);

		c.gridx = 4;
		c.gridy = 0;
		JLabel variablesLabel = new JLabel("VARIABLES:");
		pane.add(variablesLabel, c);

		c.gridx = 3;
		c.gridy = 1;
		c.weightx = 0.5;
		c.weighty = 0.5;
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

	public void setGraph(JFreeChart c, ArrayList<String> setLines, int setXAxisIndex){
		chart = c;
		lines = setLines;
		xAxisIndex = setXAxisIndex;
		updateGraph();
	}

	public void updateGraph(){
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
		addPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
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
				frame.setVisible(true);
			}
		});
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

		//MAKE COMBO BOX WITH ALL LINES AS OPTIONS FOR XAXIS
		JComboBox comboBox = new JComboBox();

		enabledLines.add(comboBox);

		//ADD ITERATIONS TEXT INPUT

		JButton runButton = new JButton("RUN");
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CreateGraph.generateChart();
				setGraph(CreateGraph.currentChart, CreateGraph.lineNames, 0);
			}
		});
		enabledLines.add(runButton, c);

		enabledLines.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent e) {
				c.gridx = 0;
				c.gridy = 3;
				c.gridheight = 1;
				c.gridwidth = 1;
				c.weightx = 0.2;
				c.weighty = 0.2;
				c.ipady = 0;
				c.ipadx = 0;
				c.anchor = GridBagConstraints.FIRST_LINE_START;
				frame.getContentPane().add(enabledLines, c);
				frame.setVisible(true);
			}
		});

		//Find better color for improved oversight in app
		enabledLines.setBackground(greenColor);

		frame.getContentPane().add(enabledLines, c);
		frame.pack();
		frame.setVisible(true);
	}
}
