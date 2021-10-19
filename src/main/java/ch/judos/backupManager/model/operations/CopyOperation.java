package ch.judos.backupManager.model.operations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ch.judos.backupManager.model.DataCounter;
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
		this.exceptionList = new ArrayList<>();
	}

	@Override
	public void calculateWork(DataCounter counter, Consumer<Runnable> newSubActions) {
		calculateWork(counter, newSubActions, this.source);
	}

	private void calculateWork(DataCounter counter, Consumer<Runnable> newSubActions,
		File toCheck) {
		this.elementsToProcess++;
		counter.addElements(1);
		if (toCheck.isFile()) {
			this.bytesToProcess += toCheck.length();
			counter.addData(toCheck.length());
			return;
		}
		if (toCheck.listFiles() == null) {
			this.exceptionList.add(new ExceptionWithKey("NOT_A_FILE_OR_DIRECTORY",
				"Path is not a file or directory: " + toCheck.getAbsolutePath()));
			return;
		}
		for (File child : toCheck.listFiles()) {
			newSubActions.accept(() -> {
				this.calculateWork(counter, newSubActions, child);
			});
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
