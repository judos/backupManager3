package ch.judos.backupManager.controller;

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
	private Thread checkThread;
	private BackupData backupData;

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
	}

}
