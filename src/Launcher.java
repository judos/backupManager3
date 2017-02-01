import java.io.File;
import java.io.IOException;

import controller.MainFrameController;
import model.PathStorage;
import view.MainFrame;

public class Launcher {
	private PathStorage storage;
	private MainFrameController controller;
	private static final File BACKUP_PATH_FILE = new File("backupPaths.csv");

	public static void main(String[] args) throws Exception {
		Launcher launcher = new Launcher();
		launcher.startApp();
	}

	private void startApp() throws Exception {
		this.storage = new PathStorage();
		storage.loadFromFile(BACKUP_PATH_FILE);
		MainFrame frame = new MainFrame(storage);
		frame.addShutdownListener(() -> this.requestShutdown());
		this.controller = new MainFrameController(frame, this.storage);

		frame.setVisible(true);
	}

	private void requestShutdown() {
		try {
			this.storage.saveToFile(BACKUP_PATH_FILE);
			System.exit(0);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
}
