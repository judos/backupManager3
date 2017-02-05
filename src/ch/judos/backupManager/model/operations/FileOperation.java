package ch.judos.backupManager.model.operations;

public abstract class FileOperation {
	public FileOperation dependsOn;
	public State currentState;
	protected long dataToProcess;
	protected int elementsToProcess;
	private Tag tag;
	private String relativePath;

	public enum State {
		TODO, IN_PROGRESS, DONE;
	}
	public enum Tag {
		NEW, CHANGED, REMOVED;
	}

	public FileOperation(Tag operationTag, String relativePath) {
		this.currentState = State.TODO;
		this.tag = operationTag;
		this.relativePath = relativePath;
	}

	public FileOperation getDependsOn() {
		return this.dependsOn;
	}

	public abstract void execute();
	public String getLogLine() {
		return this.tag.name() + ": " + this.relativePath;
	}
	public long getDataToProcess() {
		return this.dataToProcess;
	}
	public int getElementsToProcess() {
		return this.elementsToProcess;
	}

}
