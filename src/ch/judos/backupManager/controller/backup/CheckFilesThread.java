package ch.judos.backupManager.controller.backup;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import ch.judos.backupManager.model.BackupData;
import ch.judos.backupManager.model.PathEntry;
import ch.judos.backupManager.model.PathStorage;
import ch.judos.backupManager.model.Text;
import ch.judos.backupManager.model.operations.CopyOperation;
import ch.judos.backupManager.model.operations.FileOperation.Tag;
import ch.judos.backupManager.model.operations.RemoveOperation;
import ch.judos.generic.data.DynamicList;
import ch.judos.generic.data.TupleR;
import ch.judos.generic.exception.ExceptionWithKey;

public class CheckFilesThread extends Thread implements ProgressTrackable {

	private BackupData backupData;
	private DynamicList<TupleR<PathEntry, String>> pathsToCheck;
	private int checkedFolders;
	public Runnable onFinished;
	public boolean shouldRun;

	public CheckFilesThread(PathStorage storage, BackupData backupData) {
		this.backupData = backupData;
		this.pathsToCheck = new DynamicList<TupleR<PathEntry, String>>();
		addWorkFromStorage(storage);
		this.checkedFolders = 0;
		this.shouldRun = true;
		setName("CheckFilesThread");
	}

	private void addWorkFromStorage(PathStorage storage) {
		for (int i = 0; i < storage.getSize(); i++) {
			PathEntry entry = storage.getPathEntry(i);
			if (entry.isSelected()) {
				pathsToCheck.add(new TupleR<>(entry, ""));
			}
		}
	}

	public double getProgress() {
		int total = this.checkedFolders + this.pathsToCheck.size();
		return (double) this.checkedFolders / total;
	}

	public String getProgressText() {
		int total = this.checkedFolders + this.pathsToCheck.size();
		return Text.get("checking_folders", this.checkedFolders, total);
	}

	public void run() {
		while (this.pathsToCheck.size() > 0) {
			TupleR<PathEntry, String> checkEntry = this.pathsToCheck.remove(0);
			PathEntry basePaths = checkEntry.e0;
			String currentRelativePath = checkEntry.e1;
			File folderChange = new File(basePaths.getChangePath(), currentRelativePath);
			File folderBackup = new File(basePaths.getBackupPath(), currentRelativePath);
			ArrayList<String> newRelativePathsToCheck = compareFolders(folderChange,
				folderBackup, currentRelativePath);
			for (String newRelativePathToCheck : newRelativePathsToCheck) {
				this.pathsToCheck.add(new TupleR<>(basePaths, newRelativePathToCheck));
			}
			this.checkedFolders++;
			if (!this.shouldRun)
				return;
		}
		if (this.onFinished != null) {
			this.onFinished.run();
		}
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
}
