package ch.judos.backupManager.controller;

import java.io.File;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import ch.judos.backupManager.controller.backup.BackupFilesThread;
import ch.judos.backupManager.controller.backup.CheckFilesThread;
import ch.judos.backupManager.controller.backup.ProgressTrackable;
import ch.judos.backupManager.model.BackupData;
import ch.judos.backupManager.model.BackupOptions;
import ch.judos.backupManager.model.PathEntry;
import ch.judos.backupManager.model.Text;
import ch.judos.backupManager.view.BackupProgressFrame;
import ch.judos.backupManager.view.MainFrame;
import ch.judos.generic.data.date.Date;
import ch.judos.generic.data.date.Time;
import ch.judos.generic.files.FileUtils;
import ch.judos.generic.gui.Notification;

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
		this.checkThread.start();

		this.updateUiThread = new Timer(300, event -> updateUI());
		this.updateUiThread.start();
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

		this.finished = true;
	}

	private synchronized void finishBackup() {
		if (this.finished)
			return;
		if (!this.options.onlyCreateLog)
			updatePathEntryDates();
		createAndOpenLogFile();

		this.updateUiThread.stop();
		this.finished = true;
		updateUI();
	}

	private void updatePathEntryDates() {
		for (PathEntry entry : this.options.pathsForBackup) {
			entry.setLastBackup(new Date().toString("d.m.Y"));
		}
	}

	private void createAndOpenLogFile() {
		try {
			File logFile = null;
			if (this.options.saveLog) {
				logFile = new File(new Date().toString("Y-m-d") + " " + new Time().toString(
					" H/hi") + " Backup Log.txt");
			}
			else if (this.options.openLog) {
				logFile = File.createTempFile(new Date().toString() + " Backup Log - ",
					".txt");
			}
			if (logFile != null) {
				this.data.writeLogTo(logFile, this.options);
				if (this.options.openLog) {
					FileUtils.openFileWithDefaultApplication(logFile);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateUI() {
		setProgress(this.backupFrame.logProgressBar, this.backupFrame.logProgressLabel,
			this.checkThread);
		setProgress(this.backupFrame.backupProgressBar, this.backupFrame.backupProgressLabel,
			this.backupThread);
		if (this.backupThread != null) {
			this.backupFrame.currentOperationLabel.setText(this.backupThread
				.getCurrentOperationText());
		}
		if (this.finished) {
			if (this.options.onlyCreateLog)
				this.backupFrame.setTitle(Text.get("log_finished"));
			else
				this.backupFrame.setTitle(Text.get("backup_finished"));
			this.backupFrame.setButtonToFinished(this.completion);
		}
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
