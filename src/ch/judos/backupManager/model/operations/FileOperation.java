package ch.judos.backupManager.model.operations;

import ch.judos.backupManager.model.Text;

public abstract class FileOperation {
	public FileOperation dependsOn;
	public State currentState;
	protected long bytesToProcess;
	protected int elementsToProcess;
	private Tag tag;
	private String relativePath;

	public enum State {
		TODO, DONE;
	}
	public enum Tag {
		NEW(Text.get("log_add")), CHANGED(Text.get("log_change")), REMOVED(Text.get(
			"log_remove"));

		private String name;

		private Tag(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
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

	/**
	 * @return false for Operations on folders
	 */
	public abstract boolean isFile();

	public String getLogLine() {
		String prefix = isFile() ? Text.get("log_file") : Text.get("log_folder");
		return prefix + this.tag + " " + this.relativePath;
	}
	public long getBytesToProcess() {
		return this.bytesToProcess;
	}
	public int getElementsToProcess() {
		return this.elementsToProcess;
	}

}
