package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

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
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBackground(Color.GREEN);

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		panel.setPreferredSize(new Dimension(250, 250));
		frame.add(panel, BorderLayout.EAST);

		JButton button1 = new JButton("Button 1");
		panel.add(button1);

		JTextField text1 = new JTextField(10);
		panel.add(text1);

		JTextArea area1 = new JTextArea(10, 10);
		JScrollPane scroll = new JScrollPane(area1);
		panel.add(scroll);

		frame.pack();
		frame.setVisible(true);
	}
}
