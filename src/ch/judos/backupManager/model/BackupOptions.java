package ch.judos.backupManager.model;

public class BackupOptions {

	public boolean onlyCreateLog;
	public boolean openLog;
	public boolean saveLog;
	public PathStorage pathsForBackup;

	public BackupOptions(boolean onlyCreateLog, boolean openLog, boolean saveLog) {
		this.onlyCreateLog = onlyCreateLog;
		this.openLog = openLog;
		this.saveLog = saveLog;
	}

}
