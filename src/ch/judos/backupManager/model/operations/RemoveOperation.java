package ch.judos.backupManager.model.operations;

import java.io.File;

import ch.judos.generic.files.FileUtils;

public class RemoveOperation extends FileOperation {

	private File remove;

	public RemoveOperation(File remove, String relativePath) {
		super(Tag.REMOVED, relativePath);
		this.remove = remove;
		calculateWork();
	}

	private void calculateWork() {
		this.bytesToProcess = 0;
		this.elementsToProcess = FileUtils.getFilesCount(this.remove, true);
	}

	@Override
	public void execute() {
		FileUtils.deleteDirectory(this.remove);
	}

	@Override
	public boolean isFile() {
		return this.remove.isFile();
	}

}
