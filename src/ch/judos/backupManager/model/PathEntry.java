package ch.judos.backupManager.model;

public class PathEntry {

	private String changePath;
	private String backupPath;
	private String status;
	private boolean selected;

	public PathEntry(String changePath, String backupPath, String status,
		boolean selected) {
		this.changePath = changePath;
		this.backupPath = backupPath;
		this.status = status;
		this.selected = selected;
	}

	public String getChangePath() {
		return this.changePath;
	}

	public String getBackupPath() {
		return this.backupPath;
	}

	public String getStatus() {
		return this.status;
	}

	@Override
	public String toString() {
		return "DirectoryEntry (" + this.changePath + " / " + this.backupPath + " / "
			+ this.status + ")";
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return this.selected;
	}
}
