package ch.judos.backupManager;
import java.io.File;
import java.io.IOException;

import ch.judos.backupManager.controller.MainFrameController;
import ch.judos.backupManager.model.PathStorage;
import ch.judos.backupManager.view.MainFrame;
import ch.judos.generic.exception.GlobalExceptionHandler;

public class Launcher {
	public static final String VERSION = "3.0";
	private PathStorage storage;
	private static final File BACKUP_PATH_FILE = new File("backupPaths.csv");

	public static void main(String[] args) throws Exception {
		setupGlobalExceptionHandler();
		Launcher launcher = new Launcher();
		launcher.startApp();
	}

	private static void setupGlobalExceptionHandler() {
		Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
	}

	private void startApp() throws Exception {
		this.storage = new PathStorage();
		storage.loadFromFile(BACKUP_PATH_FILE);
		MainFrame frame = new MainFrame(storage);
		frame.addShutdownListener(() -> this.requestShutdown());
		new MainFrameController(frame, this.storage);
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
