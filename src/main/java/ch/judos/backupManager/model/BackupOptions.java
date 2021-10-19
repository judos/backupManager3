package ch.judos.backupManager.model;

public class BackupOptions {

	public boolean onlyCreateLog;
	public boolean saveLog;
	public PathStorage pathsForBackup;

	public BackupOptions(boolean onlyCreateLog, boolean saveLog) {
		this.onlyCreateLog = onlyCreateLog;
		this.saveLog = saveLog;
	}

}
