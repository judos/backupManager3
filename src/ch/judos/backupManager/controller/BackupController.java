package ch.judos.backupManager.controller;

import javax.swing.Timer;

import ch.judos.backupManager.controller.backup.CheckFilesThread;
import ch.judos.backupManager.model.BackupData;
import ch.judos.backupManager.model.BackupOptions;
import ch.judos.backupManager.model.PathStorage;
import ch.judos.backupManager.view.BackupProgressFrame;
import ch.judos.backupManager.view.MainFrame;

public class BackupController {

	private PathStorage storage;
	private MainFrame frame;
	private BackupProgressFrame backupFrame;
	private BackupOptions options;
	private CheckFilesThread checkThread;
	private BackupData backupData;
	private Timer updateUiThread;

	public BackupController(MainFrame frame, PathStorage storage) {
		this.frame = frame;
		this.storage = storage;
		this.backupData = new BackupData();
		this.checkThread = new CheckFilesThread(this.storage, this.backupData);
	}

	public void startBackup(BackupOptions options) {
		this.options = options;
		this.backupFrame = new BackupProgressFrame(this.frame, options);
		this.backupFrame.setVisible(true);

		this.checkThread.start();

		this.updateUiThread = new Timer(500, event -> updateUI());
		this.updateUiThread.start();
	}

	private void updateUI() {
		this.backupFrame.progressBar.setMaximum(1000);
		this.backupFrame.progressBar.setValue((int) (this.checkThread.getProgress() * 1000));
		this.backupFrame.checkingFoldersLabel.setText(this.checkThread.getProgressText());
	}

}
