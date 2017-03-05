package ch.judos.backupManager.controller.backup;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

import ch.judos.backupManager.model.BackupData;
import ch.judos.backupManager.model.PathEntry;
import ch.judos.backupManager.model.PathStorage;
import ch.judos.backupManager.model.operations.CopyOperation;
import ch.judos.backupManager.model.operations.FileOperation.Tag;
import ch.judos.backupManager.model.operations.RemoveOperation;
import ch.judos.generic.control.Text;
import ch.judos.generic.control.concurrency.ProgressThread;
import ch.judos.generic.exception.ExceptionWithKey;

public class CheckFilesThread extends ProgressThread {

	private BackupData backupData;
	private int checkedFolders;
	public Runnable onFinished;
	private int totalFolders;

	public CheckFilesThread(PathStorage storage, BackupData backupData) {
		super("CheckFilesThread");
		this.backupData = backupData;
		this.checkedFolders = 0;
		this.totalFolders = 0;
		addWorkFromStorage(storage);
	}

	private void addWorkFromStorage(PathStorage storage) {
		for (int i = 0; i < storage.getSize(); i++) {
			PathEntry entry = storage.getPathEntry(i);
			if (entry.isSelected()) {
				addPathCheck(entry, "");
			}
		}
	}

	private void addPathCheck(PathEntry entry, String relativePath) {
		this.totalFolders++;
		this.tasks.add(() -> {
			this.checkPath(entry, relativePath);
		});
	}

	public String getProgressText() {
		return Text.get("checking_folders", this.checkedFolders, this.totalFolders);
	}

	public void run() {
		super.run();
		if (this.onFinished != null) {
			this.onFinished.run();
		}
	}

	private void checkPath(PathEntry basePaths, String currentRelativePath) {
		compareFolders(basePaths, currentRelativePath);
		this.checkedFolders++;
	}

	private void compareFolders(PathEntry basePaths, String relativePath) {
		File folderChange = new File(basePaths.getChangePath(), relativePath);
		File folderBackup = new File(basePaths.getBackupPath(), relativePath);
		String[] filesChange = folderChange.list();
		String[] filesBackup = folderBackup.list();
		if (!foldersValid(folderChange, folderBackup))
			return;

		HashSet<String> filesBackupSet = new HashSet<String>(Arrays.asList(filesBackup));
		for (String file : filesChange) {
			File changedFile = new File(folderChange, file);
			File backupFile = new File(folderBackup, file);
			if (filesBackupSet.contains(file)) {
				filesBackupSet.remove(file);
				if (changedFile.isFile() != backupFile.isFile()) {
					RemoveOperation remove = new RemoveOperation(backupFile);
					remove.calculateWork(this.backupData, this.tasks::add);
					CopyOperation copy = new CopyOperation(changedFile, backupFile, Tag.NEW);
					copy.calculateWork(this.backupData, this.tasks::add);
					copy.dependsOn = remove;
					this.backupData.add(remove);
					this.backupData.add(copy);
				}
				else if (changedFile.isDirectory()) {
					File subpath = new File(relativePath, file);
					addPathCheck(basePaths, subpath.getPath());
				}
				else if (changedFile.isFile()) {
					compareFiles(changedFile, backupFile, relativePath + "/" + file);
				}
			}
			else {
				CopyOperation copy = new CopyOperation(changedFile, backupFile, Tag.NEW);
				copy.calculateWork(this.backupData, this.tasks::add);
				this.backupData.add(copy);
			}
		}
		for (String file : filesBackupSet) {
			RemoveOperation remove = new RemoveOperation(new File(folderBackup, file));
			remove.calculateWork(this.backupData, this.tasks::add);
			this.backupData.add(remove);

		}
	}

	private boolean foldersValid(File folderChange, File folderBackup) {
		if (folderChange.list() == null) {
			this.backupData.addError(new ExceptionWithKey("READ_ERROR",
				"Path is not a folder: " + folderChange.getAbsolutePath()));
			return false;
		}
		if (folderBackup.list() == null) {
			this.backupData.addError(new ExceptionWithKey("READ_ERROR",
				"Path is not a folder: " + folderBackup.getAbsolutePath()));
			return false;
		}
		return true;
	}

	private void compareFiles(File changedFile, File backupFile, String relativePath) {
		if (!fileWasModified(changedFile, backupFile))
			return;
		CopyOperation copy = new CopyOperation(changedFile, backupFile, Tag.CHANGED);
		copy.calculateWork(backupData, this.tasks::add);
		this.backupData.add(copy);
	}

	private boolean fileWasModified(File changedFile, File backupFile) {
		if (changedFile.length() != backupFile.length())
			return true;
		if (changedFile.lastModified() / 1000 != backupFile.lastModified() / 1000)
			return true;
		return false;
	}

	public void setShouldRun(boolean shouldRun) {
		this.shouldRun = shouldRun;
	}
}
