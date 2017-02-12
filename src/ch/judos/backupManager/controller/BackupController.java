package ch.judos.backupManager.controller;

import java.io.File;

import javax.swing.Timer;

import ch.judos.backupManager.controller.backup.CheckFilesThread;
import ch.judos.backupManager.model.BackupData;
import ch.judos.backupManager.model.BackupOptions;
import ch.judos.backupManager.model.PathStorage;
import ch.judos.backupManager.view.BackupProgressFrame;
import ch.judos.backupManager.view.MainFrame;
import ch.judos.generic.data.date.Date;
import ch.judos.generic.files.FileUtils;

public class BackupController {

	private PathStorage storage;
	private MainFrame frame;
	private BackupProgressFrame backupFrame;
	private BackupOptions options;
	private CheckFilesThread checkThread;
	private BackupData data;
	private Timer updateUiThread;

	public BackupController(MainFrame frame, PathStorage storage) {
		this.frame = frame;
		this.storage = storage;
		this.data = new BackupData();
		this.checkThread = new CheckFilesThread(this.storage, this.data);
	}

	public void startBackup(BackupOptions options) {
		this.options = options;
		this.backupFrame = new BackupProgressFrame(this.frame, options);
		this.backupFrame.setVisible(true);

		this.checkThread.start();
		this.checkThread.onFinished = this::finishBackup;

		this.updateUiThread = new Timer(300, event -> updateUI());
		this.updateUiThread.start();
	}

	private void finishBackup() {
		createAndOpenLogFile();

		updateUI();
		this.updateUiThread.stop();
	}

	private void createAndOpenLogFile() {
		try {
			File logFile = null;
			if (this.options.saveLog) {
				logFile = new File(new Date().toString("Y-m-d") + " Backup Log.txt");
			}
			else if (this.options.openLog) {
				logFile = File.createTempFile(new Date().toString() + " Backup Log - ",
					".txt");
			}
			if (logFile != null) {
				this.data.writeLogTo(logFile, this.options);
				if (this.options.openLog) {
					FileUtils.openFileWithDefaultApplication(logFile);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateUI() {
		this.backupFrame.progressBar.setMaximum(1000);
		this.backupFrame.progressBar.setValue((int) (this.checkThread.getProgress() * 1000));
		this.backupFrame.checkingFoldersLabel.setText(this.checkThread.getProgressText());
	}

}
