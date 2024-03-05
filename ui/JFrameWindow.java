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
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.util.ArrayList;
import javax.swing.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

public class JFrameWindow {
	private JFrame frame;
	private JPanel setLines;
	//UI Buttons / combo boxes under graph
	private JLabel yAxisLabel;
	private JPanel yAxisPanel;
	private JLabel xAxisLabel;
	private JComboBox xAxisComboBox;
	private JButton runButton;

	private JLabel iterationsLabel;
	private JTextField iterationsInput;
	private GridBagConstraints setLinesConstraints;





	public JFrameWindow() {
		initialize();
	}

	public Color greenColor;
	public Color blueColor;
	public Color redColor;

	public Color backgroundColor;

	//Interpreter should log the tree to this variable, so it can be added to the UI Textarea
	//Default is empty
	public static String treeText = "";

	//Interpreter should log the error to this variable, so it can be added to the UI Textarea
	//if no errors this is default:
	public static String errorText = "\n No errors :)";
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

		//Set background to green for now
		backgroundColor = greenColor;

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
		c.gridy = 4;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 0.2;
		c.weighty = 0.2;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		JLabel consoleLabel = new JLabel("CONSOLE:");
		pane.add(consoleLabel, c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridheight = 2;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
		JTextArea consoleArea = new JTextArea(0,0);
		consoleArea.setEditable(false);
		consoleArea.setText(errorText);
		JScrollPane consoleScrollPane = new JScrollPane(consoleArea);
		pane.add(consoleScrollPane, c);

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
        pane.setBackground(backgroundColor);
	}
	public void setGraph(JFreeChart c, ArrayList<String> setLines, int setXAxisIndex){
		chart = c;
		chart.setBackgroundPaint(backgroundColor);
		chart.getLegend().setBackgroundPaint(backgroundColor);
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
		c.gridwidth = 2;
		c.weightx = 0.2;
		c.weighty = 0.2;
		c.ipady = 0;
		c.ipadx = 0;
        c.fill = GridBagConstraints.BOTH;

		setLines = new JPanel();
		setLines.setLayout(new FlowLayout(FlowLayout.LEFT));

		yAxisLabel = new JLabel("Show on Y-axis: ");

		yAxisPanel =new CheckCombo().setComboBox(lines, xAxisIndex, backgroundColor, (int)frame.getWidth()/10,(int)frame.getHeight()/15);

		xAxisLabel = new JLabel("X-axis: ");

		//MAKE COMBO BOX WITH ALL LINES AS OPTIONS FOR XAXIS
		String[] lineNames = new String[lines.size()];
		for(int i = 0; i<lines.size(); i++)
			lineNames[i] = lines.get(i);
		xAxisComboBox = new JComboBox(lineNames);
		xAxisComboBox.setPreferredSize(new Dimension((int)frame.getWidth()/10,(int)frame.getHeight()/15));
		xAxisComboBox.addPopupMenuListener(new ComboBoxPopupMenuListener((int)frame.getWidth()/10, (int)frame.getHeight()/15, xAxisComboBox.getHeight(), xAxisComboBox.getWidth(), xAxisComboBox));

		//ADD ITERATIONS TEXT INPUT

		runButton = new JButton("RUN");
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CreateGraph.generateChart();
				setGraph(CreateGraph.currentChart, CreateGraph.lineNames, 1);
			}
		});

		iterationsLabel = new JLabel("Iterations: ");

		iterationsInput = new JTextField(5);

        PlainDocument doc = (PlainDocument) iterationsInput.getDocument();
        doc.setDocumentFilter(new IntFilter());

		setLines.addComponentListener(new ComponentAdapter(){
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
                c.fill = GridBagConstraints.BOTH;
				setLinesConstraints = c;
				SetResize();
			}
		});

		//Find better color for improved oversight in app
		setLines.setBackground(backgroundColor);
		setLinesConstraints = c;
		SetResize();
	}
	void SetResize(){
		setLines.add(yAxisLabel);
		setLines.add(yAxisPanel);
		setLines.add(xAxisLabel);
		setLines.add(xAxisComboBox);
		setLines.add(iterationsLabel);
		setLines.add(iterationsInput);
		setLines.add(runButton);
        setLines.setPreferredSize(new Dimension(frame.getWidth()/5, frame.getHeight()/5));

		frame.getContentPane().add(setLines, setLinesConstraints);
		frame.setVisible(true);
	}
}
