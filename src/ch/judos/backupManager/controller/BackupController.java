package ch.judos.backupManager.controller;

import java.io.File;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import ch.judos.backupManager.controller.backup.BackupFilesThread;
import ch.judos.backupManager.controller.backup.CheckFilesThread;
import ch.judos.backupManager.model.BackupData;
import ch.judos.backupManager.model.BackupOptions;
import ch.judos.backupManager.model.PathEntry;
import ch.judos.backupManager.view.BackupProgressFrame;
import ch.judos.backupManager.view.MainFrame;
import ch.judos.generic.control.Text;
import ch.judos.generic.control.concurrency.ProgressTrackable;
import ch.judos.generic.data.date.Date;
import ch.judos.generic.data.date.Time;
import ch.judos.generic.exception.RunnableThrowsException;
import ch.judos.generic.files.FileUtils;
import ch.judos.generic.gui.Notification;
import ch.judos.generic.os.TaskBarProgressWrapper;
import ch.judos.generic.os.TaskBarProgressWrapper.State;

public class BackupController {

	private MainFrame frame;
	private BackupProgressFrame backupFrame;
	private BackupOptions options;
	private CheckFilesThread checkThread;
	private BackupData data;
	private Timer updateUiThread;
	private BackupFilesThread backupThread;
	private boolean finished;
	private Runnable completion;
	private TaskBarProgressWrapper progressTaskBar;
	private RunnableThrowsException openLogFile;

	public BackupController(MainFrame frame, BackupOptions options) {
		this.frame = frame;
		this.options = options;
		this.data = new BackupData();
		this.checkThread = new CheckFilesThread(this.options.pathsForBackup, this.data);
		this.finished = false;
	}

	public void startBackup() {
		this.backupFrame = new BackupProgressFrame(this.frame, options);
		this.backupFrame.onCancel = this::promptCancelBackup;
		this.backupFrame.setVisible(true);

		this.checkThread.onFinished = this::startActualBackup;

		this.progressTaskBar = new TaskBarProgressWrapper(this.frame);
		this.progressTaskBar.setState(State.NORMAL);

		this.updateUiThread = new Timer(300, event -> updateUI());
		this.updateUiThread.start();

		this.checkThread.start();
	}

	private void promptCancelBackup() {
		String[] options = {Text.get("yes"), Text.get("no")};
		int optionIndexYes = 0;
		int selected = Notification.proceed(Text.get("cancel"), Text.get(
			"question_cancel_backup"), options, optionIndexYes);
		if (selected == optionIndexYes)
			cancelBackup();
	}

	private void startActualBackup() {
		if (this.options.onlyCreateLog) {
			finishBackup();
			return;
		}

		this.backupThread = new BackupFilesThread(this.data);
		this.backupThread.onFinished = this::finishBackup;
		this.backupThread.start();
	}

	private synchronized void cancelBackup() {
		if (this.finished)
			return;

		this.updateUiThread.stop();
		this.checkThread.shouldRun = false;
		if (this.backupThread != null)
			this.backupThread.shouldRun = false;
		this.backupFrame.dispose();

		this.progressTaskBar.setState(State.NO_PROGRESS);
		this.finished = true;
	}

	private synchronized void finishBackup() {
		if (this.finished)
			return;
		if (!this.options.onlyCreateLog)
			updatePathEntryDates();
		File logFile = createLogFile();
		this.openLogFile = () -> {
			FileUtils.openFileWithDefaultApplication(logFile);
		};

		this.updateUiThread.stop();
		this.finished = true;
		updateUI();
		Runnable close = () -> {
			this.backupFrame.dispose();
			if (this.completion != null)
				this.completion.run();
		};
		this.backupFrame.setFinishedUI(close, this.openLogFile);
		this.backupFrame.onCancel = close;
		this.progressTaskBar.setState(State.NO_PROGRESS);
	}

	private void updatePathEntryDates() {
		for (PathEntry entry : this.options.pathsForBackup) {
			entry.setLastBackup(new Date().toString("d.m.Y"));
		}
	}

	private File createLogFile() {
		try {
			File logFile = null;
			File logFolder = new File("Logs/");
			if (!logFolder.exists()) {
				logFolder.mkdirs();
			}
			if (this.options.saveLog) {
				logFile = new File(logFolder, new Date().toString("Y-m-d") + " " + new Time()
					.toString(" H/hi") + " Backup Log.txt");
			}
			else {
				logFile = new File(logFolder, "temp.txt");
			}
			if (logFile != null) {
				this.data.writeLogTo(logFile, this.options);
			}
			return logFile;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void updateUI() {
		setProgress(this.backupFrame.logProgressBar, this.backupFrame.logProgressLabel,
			this.checkThread);
		setProgress(this.backupFrame.backupProgressBar, this.backupFrame.backupProgressLabel,
			this.backupThread);
		setTaskbarProgress();
		if (this.backupThread != null) {
			this.backupFrame.currentOperationLabel.setText(this.backupThread
				.getCurrentOperationText());
		}
	}

	private void setTaskbarProgress() {
		double progress = this.checkThread.getProgress();
		if (!this.options.onlyCreateLog) {
			if (this.backupThread != null) {
				progress = (progress + 3 * this.backupThread.getProgress()) / 4;
			}
			else {
				progress /= 4;
			}
		}
		this.progressTaskBar.setState(State.NORMAL);
		this.progressTaskBar.setProgress(progress);

	}

	private void setProgress(JProgressBar progressBar, JLabel progressLabel,
		ProgressTrackable progress) {
		if (progress == null)
			return;
		progressBar.setMaximum(1000);
		progressBar.setValue((int) (progress.getProgress() * 1000));
		progressLabel.setText(progress.getProgressText());
	}

	public void setCompletion(Runnable completion) {
		this.completion = completion;
	}

}
