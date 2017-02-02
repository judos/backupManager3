package ch.judos.backupManager.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import ch.judos.backupManager.model.BackupOptions;
import ch.judos.backupManager.model.PathEntry;
import ch.judos.backupManager.model.PathStorage;
import ch.judos.backupManager.view.BackupProgressFrame;
import ch.judos.backupManager.view.MainFrame;
import ch.judos.generic.data.DynamicList;

public class BackupController {

	private PathStorage storage;
	private MainFrame frame;
	private BackupProgressFrame backupFrame;
	private BackupOptions options;
	private TaskManager taskManager;

	public BackupController(MainFrame frame, PathStorage storage) {
		this.frame = frame;
		this.storage = storage;
		this.taskManager = new TaskManager();
	}

	public void startBackup(BackupOptions options) {
		this.options = options;
		this.backupFrame = new BackupProgressFrame(this.frame);
		this.backupFrame.setVisible(true);
		checkAllEntries();

	}

	private void checkAllEntries() {
		for (int i = 0; i < this.storage.getSize(); i++) {
			PathEntry entry = this.storage.getPathEntry(i);
			if (entry.isSelected()) {
				checkEntry(entry);
			}
		}
	}

	private void checkEntry(PathEntry entry) {
		DynamicList<String> pathsToCheck = new DynamicList<String>();
		pathsToCheck.add("");
		while (pathsToCheck.size() > 0) {
			String currentPath = pathsToCheck.remove(0);
			System.out.println("Checking path: " + currentPath);
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
		String[] filesBackup = folderBackup.list();
		HashSet<String> filesChangeSet = new HashSet<String>(Arrays.asList(filesChange));
		HashSet<String> filesBackupSet = new HashSet<String>(Arrays.asList(filesBackup));
		for (String file : filesChange) {
			if (filesBackupSet.contains(file)) {
				filesBackupSet.remove(file);
				File changedFile = new File(folderChange, file);
				File backupFile = new File(folderBackup, file);
				if (changedFile.isFile() != backupFile.isFile()) {
					System.out.println("old file: " + file);
					System.out.println("new file: " + file);
				}
				else if (changedFile.isDirectory()) {
					System.out.println("check folder later: " + file);
					newPathsToCheck.add(basePath + file);
				}
				else if (changedFile.isFile()) {
					compareFiles(changedFile, backupFile);
				}
			}
			else {
				System.out.println("new file: " + file);
			}
		}
		for (String file : filesBackupSet) {
			System.out.println("old file: " + file);
		}
		return newPathsToCheck;
	}

	private void compareFiles(File changedFile, File backupFile) {
		if (!fileWasModified(changedFile, backupFile))
			return;
		System.out.println("modified file: " + changedFile);
	}

	private boolean fileWasModified(File changedFile, File backupFile) {
		if (changedFile.length() != backupFile.length())
			return true;
		// if (changedFile.lastModified() != backupFile.lastModified())
		// return true;
		try {
			BasicFileAttributes attr1 = Files.readAttributes(changedFile.toPath(),
				BasicFileAttributes.class);
			BasicFileAttributes attr2 = Files.readAttributes(backupFile.toPath(),
				BasicFileAttributes.class);
			if (!attr1.creationTime().equals(attr2.creationTime()))
				return true;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
