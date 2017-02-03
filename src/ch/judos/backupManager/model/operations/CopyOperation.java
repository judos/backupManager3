package ch.judos.backupManager.model.operations;

import java.io.File;

public class CopyOperation extends FileOperation {
	private File source;
	private File target;

	public CopyOperation(File source, File target) {
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
			this.dataToProcess += file.length();
			return;
		}
		for (File child : file.listFiles()) {
			calculateWorkFor(child);
		}
	}

	public void execute() {

	}

	public String getLogLine() {
		return "COPY: " + this.source + " -> " + this.target;
	}

}
