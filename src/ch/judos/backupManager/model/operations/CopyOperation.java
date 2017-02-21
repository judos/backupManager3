package ch.judos.backupManager.model.operations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.judos.generic.exception.ExceptionWithKey;
import ch.judos.generic.files.FileUtils;

public class CopyOperation extends FileOperation {
	private File source;
	private File target;

	public CopyOperation(File source, File target, Tag operationTag, String relativePath) {
		super(operationTag, relativePath);
		this.source = source;
		this.target = target;
		calculateWork();
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
			System.err.println("ERROR: " + file);
			return;
		}
		for (File child : file.listFiles()) {
			calculateWorkFor(child);
		}
	}

	public List<ExceptionWithKey> execute() {
		ArrayList<ExceptionWithKey> list = new ArrayList<>();
		FileUtils.copyDirectory(this.source, this.target, list);
		return list;
	}

	@Override
	public boolean isFile() {
		return this.source.isFile();
	}
}
