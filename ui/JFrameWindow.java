package ui;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class JFrameWindow {
	private JFrame frame;
	public JFrameWindow() {
		initialize();
	}

	public void initialize() {
		frame = new JFrame();
		frame.setTitle("Main Window");
		frame.setLocationRelativeTo(null);
		frame.setPreferredSize(new Dimension(800, 500));
		frame.setMinimumSize(new Dimension(800, 500));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2, 5, 5));
		panel.setPreferredSize(new Dimension(400, 250));
		//panel.setBackground(Color.GREEN);
		//Find nice color for right panel

		frame.add(panel, BorderLayout.EAST);

		/*JButton button1 = new JButton("Button 1");

		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1Pressed(e);
			}
		});
		panel.add(button1);*/

		JLabel rulesLabel = new JLabel("Rules:");
		panel.add(rulesLabel);

		JLabel variablesLabel = new JLabel("Start variables:");
		panel.add(variablesLabel, BorderLayout.PAGE_END);


		JTextArea rulesTextArea = new JTextArea(10,10);
		JScrollPane rulesScroll = new JScrollPane(rulesTextArea);
		panel.add(rulesScroll, BorderLayout.WEST);

		JTextArea variablesTextArea = new JTextArea(10, 10);
		JScrollPane variablesScroll = new JScrollPane(variablesTextArea);
		panel.add(variablesScroll, BorderLayout.EAST);

		frame.pack();
		frame.setVisible(true);
	}

	public void updateGraph(JFreeChart chart){
		JPanel chartPanel = new JPanel();
		chartPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		chartPanel.setPreferredSize(new Dimension(400, 312));

		ChartPanel addPanel = new ChartPanel(chart);
		addPanel.setBackground(Color.GREEN);
		addPanel.setPreferredSize(new Dimension(400, 312));

		chartPanel.add(addPanel);
		frame.add(chartPanel, BorderLayout.WEST);
		frame.pack();
	}

	public void button1Pressed(ActionEvent actionEvent){
		System.out.println("Button1 pressed :)");
	}
}
