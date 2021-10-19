package ch.judos.backupManager.model;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;

public class PathEntry {

	private File changePath;
	private File backupPath;
	private String lastBackup;
	private boolean selected;
	private AtomicBoolean checking;
	private boolean backupPathAvailable;
	private boolean changePathAvailable;
	private long lastAvailabilityCheck;

	public PathEntry(File changePath, File backupPath, String lastBackup, boolean selected) {
		this.changePath = changePath;
		this.backupPath = backupPath;
		this.lastBackup = lastBackup;
		this.selected = selected;
		this.checking = new AtomicBoolean(false);
		SwingUtilities.invokeLater(this::checkFoldersAvailable);
	}

	private void checkFoldersAvailable() {
		if (System.currentTimeMillis() - this.lastAvailabilityCheck < 1000)
			return;
		if (!this.checking.compareAndSet(false, true))
			return;
		this.changePathAvailable = this.changePath.exists();
		this.backupPathAvailable = this.backupPath.exists();
		this.lastAvailabilityCheck = System.currentTimeMillis();
		this.checking.set(false);
	}

	public File getChangePath() {
		return this.changePath;
	}

	public File getBackupPath() {
		return this.backupPath;
	}

	public String getLastBackup() {
		return this.lastBackup;
	}

	@Override
	public String toString() {
		return "DirectoryEntry (" + this.changePath + " / " + this.backupPath + " / "
			+ this.lastBackup + ")";
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setLastBackup(String string) {
		this.lastBackup = string;
	}

	public boolean isChangePathMissing() {
		SwingUtilities.invokeLater(this::checkFoldersAvailable);
		return !this.changePathAvailable;
	}

	public boolean isBackupPathMissing() {
		SwingUtilities.invokeLater(this::checkFoldersAvailable);
		return !this.backupPathAvailable;
	}

	public boolean isAvailabilityChecked() {
		return this.lastAvailabilityCheck > 0;
	}
}
