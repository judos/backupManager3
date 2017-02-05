package ch.judos.backupManager.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import ch.judos.backupManager.model.BackupOptions;
import ch.judos.backupManager.model.PathEntry;
import ch.judos.backupManager.model.PathStorage;
import ch.judos.backupManager.model.operations.CopyOperation;
import ch.judos.backupManager.model.operations.FileOperation.Tag;
import ch.judos.backupManager.model.operations.RemoveOperation;
import ch.judos.backupManager.view.BackupProgressFrame;
import ch.judos.backupManager.view.MainFrame;
import ch.judos.generic.data.DynamicList;

public class BackupController {

	private PathStorage storage;
	private MainFrame frame;
	private BackupProgressFrame backupFrame;
	private BackupOptions options;
	private TaskManager taskManager;
	private Thread checkThread;

	public BackupController(MainFrame frame, PathStorage storage) {
		this.frame = frame;
		this.storage = storage;
		this.taskManager = new TaskManager();
	}

	public void startBackup(BackupOptions options) {
		this.options = options;
		this.backupFrame = new BackupProgressFrame(this.frame, options);
		this.backupFrame.setVisible(true);
		this.checkThread = new Thread(this::checkAllEntries, "Check Thread");
		this.checkThread.start();
	}

	private void checkAllEntries() {
		for (int i = 0; i < this.storage.getSize(); i++) {
			PathEntry entry = this.storage.getPathEntry(i);
			if (entry.isSelected()) {
				checkEntry(entry);
			}
		}
		this.taskManager.print();
	}

	private void checkEntry(PathEntry entry) {
		DynamicList<String> pathsToCheck = new DynamicList<String>();
		pathsToCheck.add("");
		while (pathsToCheck.size() > 0) {
			String currentPath = pathsToCheck.remove(0);
			File folderChange = new File(entry.getChangePath(), currentPath);
			File folderBackup = new File(entry.getBackupPath(), currentPath);
			ArrayList<String> newFolderToCheck = compareFolders(folderChange, folderBackup,
				currentPath);
			pathsToCheck.addAll(newFolderToCheck);
		}
	}

	private ArrayList<String> compareFolders(File folderChange, File folderBackup,
		String basePath) {
		ArrayList<String> newPathsToCheck = new ArrayList<String>();
		String[] filesChange = folderChange.list();
		if (filesChange == null) {
			System.err.println("ERROR: " + folderChange);
			return newPathsToCheck;
		}
		String[] filesBackup = folderBackup.list();
		if (filesBackup == null) {
			System.err.println("ERROR: " + folderBackup);
			return newPathsToCheck;
		}
		assert (filesChange != null);
		assert (filesBackup != null);
		HashSet<String> filesBackupSet = new HashSet<String>(Arrays.asList(filesBackup));
		for (String file : filesChange) {
			File changedFile = new File(folderChange, file);
			File backupFile = new File(folderBackup, file);
			if (filesBackupSet.contains(file)) {
				filesBackupSet.remove(file);
				if (changedFile.isFile() != backupFile.isFile()) {
					RemoveOperation remove = new RemoveOperation(backupFile, basePath + "/"
						+ file);
					CopyOperation copy = new CopyOperation(changedFile, backupFile,
						Tag.CHANGED, basePath + "/" + file);
					copy.dependsOn = remove;
					this.taskManager.add(remove);
					this.taskManager.add(copy);
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
				this.taskManager.add(new CopyOperation(changedFile, backupFile, Tag.NEW,
					basePath + "/" + file));
			}
		}
		for (String file : filesBackupSet) {
			this.taskManager.add(new RemoveOperation(new File(folderBackup, file), basePath
				+ "/" + file));
		}
		return newPathsToCheck;
	}

	private void compareFiles(File changedFile, File backupFile, String relativePath) {
		if (!fileWasModified(changedFile, backupFile))
			return;
		this.taskManager.add(new CopyOperation(changedFile, backupFile, Tag.CHANGED,
			relativePath));
	}

	private boolean fileWasModified(File changedFile, File backupFile) {
		if (changedFile.length() != backupFile.length())
			return true;
		if (changedFile.lastModified() / 1000 != backupFile.lastModified() / 1000)
			return true;
		return false;
	}

}
