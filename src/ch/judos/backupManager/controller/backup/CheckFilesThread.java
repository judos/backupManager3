package ch.judos.backupManager.controller.backup;

import java.io.File;
import java.util.ArrayList;
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
		addWorkFromStorage(storage);
		this.checkedFolders = 0;
		this.totalFolders = 0;
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
		File folderChange = new File(basePaths.getChangePath(), currentRelativePath);
		File folderBackup = new File(basePaths.getBackupPath(), currentRelativePath);
		ArrayList<String> newRelativePathsToCheck = compareFolders(folderChange, folderBackup,
			currentRelativePath);
		for (String newRelativePathToCheck : newRelativePathsToCheck) {
			addPathCheck(basePaths, newRelativePathToCheck);
		}
		this.checkedFolders++;
		if (!this.shouldRun)
			return;
	}

	private ArrayList<String> compareFolders(File folderChange, File folderBackup,
		String basePath) {
		ArrayList<String> newPathsToCheck = new ArrayList<String>();
		String[] filesChange = folderChange.list();
		if (filesChange == null) {
			this.backupData.addError(new ExceptionWithKey("READ_ERROR",
				"Path is not a folder: " + folderChange.getAbsolutePath()));
			return newPathsToCheck;
		}
		String[] filesBackup = folderBackup.list();
		if (filesBackup == null) {
			this.backupData.addError(new ExceptionWithKey("READ_ERROR",
				"Path is not a folder: " + folderBackup.getAbsolutePath()));
			return newPathsToCheck;
		}
		HashSet<String> filesBackupSet = new HashSet<String>(Arrays.asList(filesBackup));
		for (String file : filesChange) {
			File changedFile = new File(folderChange, file);
			File backupFile = new File(folderBackup, file);
			if (filesBackupSet.contains(file)) {
				filesBackupSet.remove(file);
				if (changedFile.isFile() != backupFile.isFile()) {
					RemoveOperation remove = new RemoveOperation(backupFile);
					CopyOperation copy = new CopyOperation(changedFile, backupFile, Tag.NEW);
					copy.dependsOn = remove;
					this.backupData.add(remove);
					this.backupData.add(copy);
				}
				else if (changedFile.isDirectory()) {
					File subpath = new File(basePath, file);
					newPathsToCheck.add(subpath.getPath());
				}
				else if (changedFile.isFile()) {
					compareFiles(changedFile, backupFile, basePath + "/" + file);
				}
			}
			else {
				this.backupData.add(new CopyOperation(changedFile, backupFile, Tag.NEW));
			}
		}
		for (String file : filesBackupSet) {
			this.backupData.add(new RemoveOperation(new File(folderBackup, file)));
		}
		return newPathsToCheck;
	}

	private void compareFiles(File changedFile, File backupFile, String relativePath) {
		if (!fileWasModified(changedFile, backupFile))
			return;
		this.backupData.add(new CopyOperation(changedFile, backupFile, Tag.CHANGED));
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
