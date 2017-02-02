package ch.judos.backupManager.controller;
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
	}

	private void startBackup() {
		BackupOptionsFrame backupOptionsFrame = new BackupOptionsFrame(this.frame);
		backupOptionsFrame.setCompletion(options -> {
			BackupController controller = new BackupController(this.frame, this.storage);
			controller.startBackup(options);
			backupOptionsFrame.dispose();
		});
		backupOptionsFrame.setVisible(true);
	}

	private void removeDirectories() {
		int[] selected = this.frame.table.getSelectedRows();
		if (selected.length == 0 || this.frame.table.getRowCount() == 0)
			return;
		this.frame.table.removeRowSelectionInterval(0, this.frame.table.getRowCount() - 1);
		this.storage.removePathIndexes(selected);
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
