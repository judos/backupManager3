package ch.judos.backupManager.controller;
import javax.swing.Timer;

import ch.judos.backupManager.model.PathEntry;
import ch.judos.backupManager.model.PathStorage;
import ch.judos.backupManager.view.AddPathFrame;
import ch.judos.backupManager.view.BackupOptionsFrame;
import ch.judos.backupManager.view.MainFrame;

public class MainFrameController {

	private PathStorage storage;
	private MainFrame frame;

	public MainFrameController(MainFrame frame, PathStorage storage) {
		this.storage = storage;
		this.frame = frame;
		setupListeners();
	}

	private void setupListeners() {
		this.frame.addButton.addActionListener(event -> this.addDirectoryClicked());
		this.frame.removeButton.addActionListener(event -> this.removeDirectories());
		this.frame.startBackupButton.addActionListener(event -> this.startBackup());
		// refresh table if directory becomes available or unavailable
		new Timer(1000, event -> {
			this.frame.table.updateUI();
		}).start();
	}

	private void startBackup() {
		BackupOptionsFrame backupOptionsFrame = new BackupOptionsFrame(this.frame);
		backupOptionsFrame.setCompletion(options -> {
			backupOptionsFrame.dispose();
			options.pathsForBackup = this.storage.cloneSelected();
			BackupController controller = new BackupController(this.frame, options);
			controller.setCompletion(() -> {
				this.frame.table.updateUI();
			});
			controller.startBackup();
		});
		backupOptionsFrame.setVisible(true);
	}

	private void removeDirectories() {
		this.storage.removeSelectedPaths();
		this.frame.table.updateUI();
	}

	private void addDirectoryClicked() {
		AddPathFrame addPathFrame = new AddPathFrame(this.frame);
		addPathFrame.setAddPathListener((changePath, backupPath) -> {
			addPathFrame.dispose();
			PathEntry entry = new PathEntry(changePath, backupPath, "-", false);
			this.storage.addPath(entry);
			this.frame.table.updateUI();
		});
		addPathFrame.setVisible(true);
	}

}
