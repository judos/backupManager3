package ch.judos.backupManager.model.operations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.judos.generic.exception.ExceptionWithKey;
import ch.judos.generic.files.FileUtils;

public class CopyOperation extends FileOperation {
	private File source;
	private File target;
	private ArrayList<ExceptionWithKey> exceptionList;

	public CopyOperation(File source, File target, Tag operationTag) {
		super(operationTag, source);
		this.source = source;
		this.target = target;
		calculateWork();
		this.exceptionList = new ArrayList<>();
	}

	private void calculateWork() {
		calculateWorkFor(this.source);
	}

	private void calculateWorkFor(File file) {
		this.elementsToProcess++;
		if (file.isFile()) {
			this.bytesToProcess += file.length();
			return;
		}
		if (file.listFiles() == null) {
			this.exceptionList.add(new ExceptionWithKey("NOT_A_FILE_OR_DIRECTORY",
				"Path is not a file or directory: " + file.getAbsolutePath()));
			return;
		}
		for (File child : file.listFiles()) {
			calculateWorkFor(child);
		}
	}

	public List<ExceptionWithKey> execute() {
		FileUtils.copyDirectory(this.source, this.target, this.exceptionList);
		return this.exceptionList;
	}

	@Override
	public boolean isFile() {
		return this.source.isFile();
	}
}
