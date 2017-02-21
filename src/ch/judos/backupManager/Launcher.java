package ch.judos.backupManager;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import ch.judos.backupManager.controller.MainFrameController;
import ch.judos.backupManager.model.PathStorage;
import ch.judos.backupManager.model.Text;
import ch.judos.backupManager.view.MainFrame;
import ch.judos.generic.gui.Notification;

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
		Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
			StringWriter exceptionStackTrace = new StringWriter();
			PrintWriter printWriter = new PrintWriter(exceptionStackTrace);
			exception.printStackTrace(printWriter);
			System.err.println(exceptionStackTrace);
			Notification.notifyErr("Error", Text.get("error_occured_in_thread", thread
				.getName()) + ":\n\n" + exceptionStackTrace);
		});
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
