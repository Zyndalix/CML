import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
	public static void main(String[] args) {

		// following two lines are not important
		String s = UIManager.getSystemLookAndFeelClassName();
		System.out.println(s);

		// boilerplate code needed for setting up the window, don't delete
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrameWindow window = new JFrameWindow();
			}
		});

	}
}
