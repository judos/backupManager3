package ch.judos.backupManager.model.operations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.judos.generic.exception.ExceptionWithKey;
import ch.judos.generic.files.FileUtils;

public class RemoveOperation extends FileOperation {

	private File remove;

	public RemoveOperation(File remove) {
		super(Tag.REMOVED, remove);
		this.remove = remove;
		calculateWork();
	}

	private void calculateWork() {
		this.bytesToProcess = 0;
		this.elementsToProcess = FileUtils.getFilesCount(this.remove, true);
	}

	@Override
	public List<ExceptionWithKey> execute() {
		boolean ok = FileUtils.deleteDirectory(this.remove);
		ArrayList<ExceptionWithKey> exceptionList = new ArrayList<>();
		if (!ok) {
			exceptionList.add(new ExceptionWithKey("DELETE_FAILED", "Could not remove path: "
				+ this.remove.getAbsolutePath()));
		}
		return exceptionList;
	}

	@Override
	public boolean isFile() {
		return this.remove.isFile();
	}

}
