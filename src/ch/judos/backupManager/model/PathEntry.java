package ch.judos.backupManager.model;

import java.io.File;

public class PathEntry {

	private File changePath;
	private File backupPath;
	private String lastBackup;
	private boolean selected;

	public PathEntry(File changePath, File backupPath, String lastBackup, boolean selected) {
		this.changePath = changePath;
		this.backupPath = backupPath;
		this.lastBackup = lastBackup;
		this.selected = selected;
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
}
