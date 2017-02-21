package ch.judos.backupManager.controller.backup;

import java.util.List;

import ch.judos.backupManager.model.BackupData;
import ch.judos.backupManager.model.Text;
import ch.judos.backupManager.model.operations.FileOperation;
import ch.judos.generic.exception.ExceptionWithKey;
import ch.judos.generic.files.FileSize;

public class BackupFilesThread extends Thread implements ProgressTrackable {

	private BackupData data;
	public Runnable onFinished;
	private int elementsProcessed;
	private long bytesProcessed;
	public boolean shouldRun;
	private FileOperation currentOperation;

	public BackupFilesThread(BackupData data) {
		this.data = data;
		this.elementsProcessed = 0;
		this.bytesProcessed = 0;
		this.shouldRun = true;
		this.currentOperation = null;
		setName("BackupThread");
	}

	@Override
	public double getProgress() {
		double progressFiles = (double) this.elementsProcessed / this.data
			.getElementsToProcess();
		double progressData = (double) this.bytesProcessed / this.data.getBytesToProcess();
		return (progressFiles + progressData) / 2;
	}

	@Override
	public String getProgressText() {
		String elements = this.elementsProcessed + " / " + this.data.getElementsToProcess()
			+ " " + Text.get("synchronizing_files_elements");
		String size = FileSize.getSizeNiceFromBytes(this.bytesProcessed) + " / " + FileSize
			.getSizeNiceFromBytes(this.data.getBytesToProcess());
		return Text.get("synchronizing_files", elements + ", " + size);
	}

	@Override
	public void run() {
		while (this.data.taskList.size() > 0) {
			FileOperation operation = this.data.taskList.poll();
			workOn(operation);
			if (!this.shouldRun)
				return;
		}

		if (this.onFinished != null)
			this.onFinished.run();
	}

	private void workOn(FileOperation operation) {
		this.currentOperation = operation;
		try {
			List<ExceptionWithKey> exceptionList = operation.execute();
			this.data.addError(exceptionList);
		}
		catch (Exception e) {
			System.err.println("Should not happen: " + e.getMessage());
			e.printStackTrace();
		}
		this.elementsProcessed += operation.getElementsToProcess();
		this.bytesProcessed += operation.getBytesToProcess();
		operation.currentState = FileOperation.State.DONE;
	}

	public String getCurrentOperationText() {
		if (this.currentOperation == null)
			return "";
		return this.currentOperation.getLogLine();
	}

}
