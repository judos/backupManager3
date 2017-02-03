package ch.judos.backupManager.model.operations;

public abstract class FileOperation {
	public FileOperation dependsOn;
	public State currentState;
	protected long dataToProcess;
	protected int elementsToProcess;

	public enum State {
		TODO, IN_PROGRESS, DONE;
	}

	public FileOperation() {
		this.currentState = State.TODO;
	}

	public FileOperation getDependsOn() {
		return this.dependsOn;
	}

	public abstract void execute();
	public abstract String getLogLine();
	public long getDataToProcess() {
		return this.dataToProcess;
	}
	public int getElementsToProcess() {
		return this.elementsToProcess;
	}

}
