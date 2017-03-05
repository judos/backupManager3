package ch.judos.backupManager.model.operations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ch.judos.backupManager.model.DataCounter;
import ch.judos.generic.exception.ExceptionWithKey;
import ch.judos.generic.files.FileUtils;

public class RemoveOperation extends FileOperation {

	private File remove;
	private ArrayList<ExceptionWithKey> exceptionList;

	public RemoveOperation(File remove) {
		super(Tag.REMOVED, remove);
		this.remove = remove;
		this.exceptionList = new ArrayList<>();

	}

	public void calculateWork(DataCounter counter, Consumer<Runnable> newSubActions) {
		calculateWork(counter, this.remove, newSubActions);
	}

	private void calculateWork(DataCounter counter, File toCheck,
		Consumer<Runnable> subActions) {
		this.elementsToProcess++;
		counter.addElements(1);
		if (toCheck.isFile()) {
			return;
		}
		if (toCheck.isDirectory()) {
			for (File f : toCheck.listFiles()) {
				if (f.isDirectory()) {
					subActions.accept(() -> {
						this.calculateWork(counter, f, subActions);
					});
				}
				else {
					this.elementsToProcess++;
					counter.addElements(1);
				}
			}
			return;
		}
		this.exceptionList.add(new ExceptionWithKey("NOT_A_FILE_OR_DIRECTORY",
			"Path is not a file or directory: " + toCheck.getAbsolutePath()));
	}

	@Override
	public List<ExceptionWithKey> execute() {
		boolean ok = FileUtils.deleteDirectory(this.remove);
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
