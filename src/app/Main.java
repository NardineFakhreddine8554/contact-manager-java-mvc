package app;

import controller.MainController;
import view.MainWindow;

public class Main {
	public static void main(String[] args) {
		// Ensure GUI is created on the Event Dispatch Thread (EDT)
		javax.swing.SwingUtilities.invokeLater(() -> {
			System.out.println("Launching MainWindow...");
			MainController controller = new MainController();
			MainWindow mainWindow = new MainWindow(controller);
		});
	}
}