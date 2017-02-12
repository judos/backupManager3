package ch.judos.backupManager.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import ch.judos.backupManager.model.operations.FileOperation;
import ch.judos.generic.data.DynamicList;
import ch.judos.generic.data.date.DateTime;
import ch.judos.generic.data.date.Time;
import ch.judos.generic.files.FileSize;
import ch.judos.generic.files.FileUtils;

public class BackupData {

	public Queue<FileOperation> taskList;
	private int elementsToProcess;
	private long bytesToProcess;
	public DynamicList<String> backupLog;
	private final int backupLogInitialSize = 1000;
	private DateTime backupStarted;

	public BackupData() {
		this.taskList = new LinkedBlockingQueue<>();
		this.backupLog = new DynamicList<String>(backupLogInitialSize);
		this.backupStarted = new DateTime();
		this.elementsToProcess = 0;
		this.bytesToProcess = 0;
	}

	public void add(FileOperation operation) {
		this.taskList.add(operation);
		this.elementsToProcess += operation.getElementsToProcess();
		this.bytesToProcess += operation.getBytesToProcess();
		this.backupLog.add(operation.getLogLine());
	}

	public int getElementsToProcess() {
		return this.elementsToProcess;
	}

	public long getBytesToProcess() {
		return this.bytesToProcess;
	}

	public void writeLogTo(File file, BackupOptions options) throws IOException {
		DateTime backupFinished = new DateTime();
		try (BufferedWriter writer = FileUtils.getWriterForFile(file)) {
			if (options.onlyCreateLog) {
				writer.write(Text.get("log_only"));
				writer.newLine();
				writer.newLine();
			}
			writer.write(Text.get("log_start") + ": " + this.backupStarted.toString());
			writer.newLine();
			writer.write(Text.get("log_end") + ": " + backupFinished.toString());
			writer.newLine();
			Time duration = new Time((int) this.backupStarted.getDiffTo(backupFinished,
				TimeUnit.SECONDS));
			writer.write(Text.get("log_duration") + ": " + duration.toString());
			writer.newLine();
			writer.newLine();
			writer.write(Text.get("log_synchronized") + ":");
			writer.newLine();

			writer.write(" " + Text.get("log_synchronized_files") + ": "
				+ this.elementsToProcess);
			writer.newLine();
			writer.write(" " + Text.get("log_synchronized_data") + ": " + FileSize
				.getSizeNiceFromBytes(this.bytesToProcess));
			writer.newLine();
			writer.newLine();
			writer.write(Text.get("log_legend1", Text.get("log_add"), Text.get("log_remove"),
				Text.get("log_change")));
			writer.newLine();
			writer.write(Text.get("log_legend2", Text.get("log_file"), Text.get(
				"log_folder")));
			writer.newLine();
			writer.newLine();

			for (String s : this.backupLog) {
				writer.write(s);
				writer.newLine();
			}
		}
	}
}