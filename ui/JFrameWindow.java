package ui;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class JFrameWindow {
	private JFrame frame;
	public JPanel gridPanel;
	public JFrameWindow() {
		initialize();
	}

	public void initialize() {
		frame = new JFrame();
		frame.setTitle("Main Window");
		frame.setLocationRelativeTo(null);
		frame.setPreferredSize(new Dimension(800, 500));

		Dimension DimMax = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setMaximumSize(DimMax);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setPreferredSize(new Dimension(800, 500));
		frame.setMinimumSize(new Dimension(800, 500));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		gridPanel.setLayout(new GridLayout(2,2));
		gridPanel.setPreferredSize(frame.getSize());

		/*JButton button1 = new JButton("Button 1");

		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1Pressed(e);
			}
		});
		panel.add(button1);*/

		//Make gridbag layout and order different components into with set heights and widths

		frame.pack();
		frame.setVisible(true);
	}

	public void updateGraph(JFreeChart chart){
		JPanel chartPanel = new JPanel();
		chartPanel.setLayout(new GridLayout(1,0));
		chartPanel.setPreferredSize(new Dimension(frame.getSize().width/2, frame.getSize().height/2));

		ChartPanel addPanel = new ChartPanel(chart);
		addPanel.setBackground(Color.GREEN);

		addPanel.setPreferredSize(chartPanel.getSize());

		chartPanel.add(addPanel);
		frame.add(chartPanel, BorderLayout.WEST);
		frame.pack();
	}

	public void button1Pressed(ActionEvent actionEvent){
		System.out.println("Button1 pressed :)");
	}
}
