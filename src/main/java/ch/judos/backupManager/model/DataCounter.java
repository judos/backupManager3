package ch.judos.backupManager.model;

public interface DataCounter {
	public void addData(long bytes);
	public void addElements(int files);
}
