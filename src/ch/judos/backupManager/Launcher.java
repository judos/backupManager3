package ch.judos.backupManager;
import java.io.File;

import ch.judos.backupManager.controller.MainFrameController;
import ch.judos.backupManager.model.Constants;
import ch.judos.backupManager.model.PathStorage;
import ch.judos.backupManager.view.MainFrame;
import ch.judos.generic.exception.GlobalExceptionHandler;

public class Launcher {
	public static final String VERSION = "3.3";
	private PathStorage storage;
	private static final File BACKUP_PATH_FILE = new File(Constants.APP_DATA,
		"backupPaths.csv");

	public static void main(String[] args) throws Exception {
		setupGlobalExceptionHandler();
		Launcher launcher = new Launcher();
		launcher.startApp();
	}

	private static void setupGlobalExceptionHandler() {
		Thread.setDefaultUncaughtExceptionHandler(GlobalExceptionHandler.getInstance());
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
		GlobalExceptionHandler.runAndHandle(() -> {
			this.storage.saveToFile(BACKUP_PATH_FILE);
		});
		System.exit(0);
	}
}
