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
		panel.setLayout(new FlowLayout());
		panel.setPreferredSize(new Dimension(400, 125));
		//panel.setBackground(Color.GREEN);
		//Find nice color for right panel


		/*JButton button1 = new JButton("Button 1");

		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1Pressed(e);
			}
		});
		panel.add(button1);*/


        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 10));

		JLabel rulesLabel = new JLabel("Rules:");
		labelPanel.add(rulesLabel, BorderLayout.NORTH);
		JLabel variablesLabel = new JLabel("Start variables:");
		labelPanel.add(variablesLabel, BorderLayout.NORTH);
        frame.add(labelPanel, BorderLayout.EAST);

        JTextArea variablesTextArea = new JTextArea(1, 1);
        JScrollPane variablesScroll = new JScrollPane(variablesTextArea);
        panel.add(variablesScroll, BorderLayout.EAST);

		JTextArea rulesTextArea = new JTextArea(1,1);
		JScrollPane rulesScroll = new JScrollPane(rulesTextArea);
		panel.add(rulesScroll, BorderLayout.EAST);

        frame.add(panel, BorderLayout.EAST);

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
