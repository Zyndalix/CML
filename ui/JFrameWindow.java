package ui;

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

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;
import java.io.*;
import java.util.Objects;

public class JFrameWindow {
	private JFrame frame;
	private JPanel setLines;
	//UI Buttons / combo boxes under graph
	private JLabel yAxisLabel;
	private JPanel yAxisPanel;
	private JLabel xAxisLabel;
	private JComboBox xAxisComboBox;
	private JButton runButton;

    private JTextArea rulesText;
    private String rulesTextString;
    private JTextArea variablesText;
    private String variablesTextString;


    private JPanel iterationsPanel;
    public JTextField iterationsText;
	public String iterationsTextString;
	private JLabel iterationsLabel;
	private GridBagConstraints setLinesConstraints;
    public int xAxisIndex;


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

    //Always start with a new line to not overlap text.
	public static String errorText = "\n No errors :)";
	private JTextArea consoleArea;
	JFreeChart chart;
	ArrayList<String> lines;
    public boolean[] enabledLines;

	private ChartPanel addPanel;
	public void initialize() {
		//Set standard colors for UI
		float[] greenHSB = new float[3];
		Color.RGBtoHSB(116, 255, 190, greenHSB);
		greenColor = Color.getHSBColor(greenHSB[0], greenHSB[1], greenHSB[2]);
		float[] blueHSB = new float[3];
		Color.RGBtoHSB(227, 255, 255, blueHSB);
		blueColor = Color.getHSBColor(blueHSB[0], blueHSB[1], blueHSB[2]);
		float[] redHSB = new float[3];
		Color.RGBtoHSB(255, 134, 116, redHSB);
		redColor = Color.getHSBColor(redHSB[0], redHSB[1], redHSB[2]);

		//Set background to green for now
		backgroundColor = blueColor;

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
		c.weightx = 1;
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
        c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
        c.ipadx = frame.getWidth()/3;
        c.ipady = 0;
		rulesText = new JTextArea(0,0);
        rulesText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		JScrollPane rulesScrollPane = new JScrollPane(rulesText);
        TextLineNumber rulesTLN = new TextLineNumber(rulesText);
        rulesScrollPane.setRowHeaderView(rulesTLN);
		pane.add(rulesScrollPane, c);

		c.gridx = 4;
		c.gridy = 1;
		variablesText = new JTextArea(0,0);

        variablesText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		JScrollPane variablesScrollPane = new JScrollPane(variablesText);
        TextLineNumber variablesTLN = new TextLineNumber(variablesText);
        variablesScrollPane.setRowHeaderView(variablesTLN);
		pane.add(variablesScrollPane, c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 0.2;
		c.weighty = 0.2;
        c.ipadx = 0;
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
		consoleArea = new JTextArea(0,0);
		consoleArea.setEditable(false);
		consoleArea.setText(errorText);
		JScrollPane consoleScrollPane = new JScrollPane(consoleArea);
		pane.add(consoleScrollPane, c);

		c.gridx = 3;
		c.gridy = 3;
		c.gridheight = 2;
		c.gridwidth = 2;
        c.weightx = 0.5;
        c.weighty = 0.5;
		c.fill = GridBagConstraints.BOTH;
		JTextArea treeTextArea = new JTextArea(0,0);
		treeTextArea.setEditable(false);
		treeTextArea.setText(treeText);
		JScrollPane treeScrollPane = new JScrollPane(treeTextArea);
		pane.add(treeScrollPane, c);
        pane.setBackground(backgroundColor);
	}
	public void setGraph(JFreeChart c, ArrayList<String> setLines){
		chart = c;
		chart.setBackgroundPaint(backgroundColor);
		chart.getLegend().setBackgroundPaint(backgroundColor);
		lines = setLines;

		updateGraph();
	}

	public void logError(String err){
		if(Objects.equals(errorText, "\n No errors :)")){
			consoleArea.setText(err);
		}
		consoleArea.setForeground(redColor);
		errorText = errorText.concat(err);
		consoleArea.setText(errorText);

		consoleArea.repaint();
		consoleArea.revalidate();
	}

	public void getIterationsText(){
		try{
			iterationsTextString = CreateGraph.getContentsOfFile("iterations");
		}catch (Exception e)
		{
			System.err.print("Not an integer in iterations file");
			iterationsTextString = "100";
		}
	}
	public void updateGraph(){
		GridBagConstraints c = new GridBagConstraints();
		if(addPanel == null){
			addPanel = new ChartPanel(chart);
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 2;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.ipadx = frame.getWidth()/2;
			c.ipady = frame.getHeight()/2;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.fill = GridBagConstraints.VERTICAL;
			frame.getContentPane().add(addPanel, c);
			System.out.print("Created first chart");
		}
		else{
			System.out.print("Created new chart");
			addPanel.setChart(chart);
			addPanel.repaint();
		}

		if(setLines != null && setLines.getComponents().length != 0)
			return;

		setLines = new JPanel();

		setLines.setLayout(new FlowLayout(FlowLayout.LEFT, 5,5));

		yAxisLabel = new JLabel("Show on Y-axis:");
		updateYComboBox();
		xAxisLabel = new JLabel("X-axis:");

		setXComboBox();

        ActionListener xAxisActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectXAxis(xAxisComboBox.getSelectedIndex());
			}
		};
        xAxisComboBox.addActionListener(xAxisActionListener);

		runButton = new JButton("RUN");
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                getTextFromArea(rulesText, "rules");
                getTextFromArea(variablesText, "variables");
				getTextFromField(iterationsText, "iterations");

                loadSavedText();
                CreateGraph.generateChart();
                setGraph(CreateGraph.currentChart, CreateGraph.lineNames);
			}
		});

		iterationsPanel = new JPanel();
		iterationsLabel = new JLabel("Iterations: ");
		System.out.print("Test");
		iterationsText = new JTextField(iterationsTextString, 5);
		iterationsText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
		PlainDocument doc = (PlainDocument) iterationsText.getDocument();
		doc.setDocumentFilter(new IntFilter());
		iterationsPanel.add(iterationsLabel);
		iterationsPanel.add(iterationsText);
		iterationsPanel.setBackground(backgroundColor);


		//Find better color for improved oversight in app
		setLines.setBackground(backgroundColor);

        c.gridx = 0;
        c.gridy = 3;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.weightx = 0.8;
        c.weighty = 0.8;
        c.ipady = 0;
        c.ipadx = 0;
        c.anchor = GridBagConstraints.SOUTHWEST;
        c.fill = GridBagConstraints.BOTH;

        setLinesConstraints = c;
		SetLines();
	}
	void SetLines(){

		setLines.add(yAxisLabel);
		setLines.add(yAxisPanel);
		setLines.add(xAxisLabel);
		setLines.add(xAxisComboBox);
		setLines.add(iterationsPanel);
		setLines.add(runButton);

		frame.getContentPane().add(setLines, setLinesConstraints);
		frame.setVisible(true);
	}
    void getTextFromArea(JTextArea textArea, String saveName){
        // Save text to files for interpreter to find and automatically saves between closing and opening apps
        File fileName = new File(saveName);
        BufferedWriter outFile = null;
        try {
            outFile = new BufferedWriter(new FileWriter(fileName));
            textArea.write(outFile);
            System.out.print("Saving file: " + saveName);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error saving file: " + ex.getMessage());
        } finally {
            if (outFile != null) {
                try {
                    outFile.close();
                } catch (IOException e) {
                    System.out.println("Couldn't save text to file " + saveName);
                }
            }
        }
    }
	void getTextFromField(JTextField textField, String saveName){
		// Save text to files for interpreter to find and automatically saves between closing and opening apps
		File fileName = new File(saveName);
		BufferedWriter outFile = null;
		try {
			outFile = new BufferedWriter(new FileWriter(fileName));
			textField.write(outFile);
			System.out.print("Saving file: " + saveName);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("Error saving file: " + ex.getMessage());
		} finally {
			if (outFile != null) {
				try {
					outFile.close();
				} catch (IOException e) {
					System.out.println("Couldn't save text to file " + saveName);
				}
			}
		}
	}

    public void loadSavedText(){
        rulesTextString = CreateGraph.getContentsOfFile("rules");
        rulesText.setText(rulesTextString);
        variablesTextString = CreateGraph.getContentsOfFile("variables");
        variablesText.setText(variablesTextString);
		getIterationsText();
    }

    void selectXAxis(int index){
        System.out.print("Selected x-axis: " + lines.get(index));
        xAxisIndex = index;
        updateYComboBox();
    }

	void setXComboBox(){
		String[] lineNames = new String[lines.size()];
		for(int i = 0; i<lines.size(); i++)
			lineNames[i] = lines.get(i);
		if(xAxisComboBox == null){
			xAxisComboBox = new JComboBox(lineNames);
			xAxisComboBox.setPreferredSize(new Dimension(frame.getWidth()/10,frame.getHeight()/15));
			return;
		}
		xAxisComboBox.removeAllItems();
        xAxisComboBox.addItem(lineNames);
		updateYComboBox();
	}
    void updateYComboBox(){
        yAxisPanel = CheckCombo.setComboBox(lines, xAxisIndex, backgroundColor, frame.getWidth()/10,frame.getHeight()/15);
		yAxisPanel.repaint();
		yAxisPanel.revalidate();
    }

	public void setLineVisibility(){
		chart = CreateGraph.setData();
		chart.setBackgroundPaint(backgroundColor);
		chart.getLegend().setBackgroundPaint(backgroundColor);
		System.out.print("Enabled/disabled line");
		addPanel.setChart(chart);
		addPanel.repaint();
	}
}
