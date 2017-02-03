package ch.judos.backupManager.controller;

import java.util.concurrent.ConcurrentLinkedQueue;

import ch.judos.backupManager.model.operations.FileOperation;
import ch.judos.generic.files.FileSize;

public class TaskManager {

	private ConcurrentLinkedQueue<FileOperation> taskList;
	private int elementsToProcess;
	private long dataToProcess;

	public TaskManager() {
		this.taskList = new ConcurrentLinkedQueue<>();
		this.elementsToProcess = 0;
		this.dataToProcess = 0;
	}

	public void add(FileOperation operation) {
		this.taskList.add(operation);
		this.elementsToProcess += operation.getElementsToProcess();
		this.dataToProcess += operation.getDataToProcess();
		System.out.println(operation.getLogLine());
	}

	public void print() {
		System.out.println("Elements: " + this.elementsToProcess);
		System.out.println("Data: " + FileSize.getSizeNiceFromBytes(this.dataToProcess));
	}

}
