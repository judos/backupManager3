package controller;
import model.PathEntry;
import model.PathStorage;
import view.AddPathFrame;
import view.MainFrame;

public class MainFrameController {

	private PathStorage storage;
	private MainFrame frame;
	private AddPathFrame addPathFrame;

	public MainFrameController(MainFrame frame, PathStorage storage) {
		this.storage = storage;
		this.frame = frame;
		setupListeners();
	}

	private void setupListeners() {
		this.frame.addButton.addActionListener(event -> this.addDirectoryClicked());
		this.frame.removeButton.addActionListener(event -> this.removeDirectories());
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
		this.addPathFrame = new AddPathFrame(this.frame);
		this.addPathFrame.setAddPathListener((changePath, backupPath) -> {
			PathEntry entry = new PathEntry(changePath, backupPath, "-", false);
			this.storage.addPath(entry);
			this.frame.table.updateUI();
		});
		this.addPathFrame.setVisible(true);
	}

}
