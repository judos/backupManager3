package ch.judos.backupManager.model;

public class PathEntry {

	private String changePath;
	private String backupPath;
	private String lastBackup;
	private boolean selected;

	public PathEntry(String changePath, String backupPath, String lastBackup, boolean selected) {
		this.changePath = changePath;
		this.backupPath = backupPath;
		this.lastBackup = lastBackup;
		this.selected = selected;
	}

	public String getChangePath() {
		return this.changePath;
	}

	public String getBackupPath() {
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
