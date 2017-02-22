package ch.judos.backupManager.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import ch.judos.backupManager.model.BackupOptions;
import ch.judos.backupManager.model.Text;
import ch.judos.generic.exception.RunnableThrowsException;

public class BackupProgressFrame extends JDialog {

	private BackupOptions options;
	public JProgressBar logProgressBar;
	private JButton cancelButton;
	public JProgressBar backupProgressBar;
	public JLabel logProgressLabel;
	public JLabel backupProgressLabel;
	public Runnable onCancel;
	public JLabel currentOperationLabel;
	public Runnable onFinish;
	private GridBagConstraints buttonGridConstraints;
	private JButton closeButton;
	private JButton openLogButton;
	private static final long serialVersionUID = -163978212692777202L;

	public BackupProgressFrame(MainFrame parent, BackupOptions options) {
		super(parent);
		this.options = options;
		if (this.options.onlyCreateLog)
			setTitle(Text.get("log_running"));
		else
			setTitle(Text.get("backup_running"));
		setupComponents();
		pack();
		if (!this.options.onlyCreateLog) {
			this.backupProgressLabel.setText(Text.get("synchronizing_files", ""));
		}
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addActionListeners();
	}

	private void addActionListeners() {
		this.cancelButton.addActionListener(event -> {
			if (this.onCancel != null)
				this.onCancel.run();
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (onCancel != null)
					onCancel.run();
			}
		});
	}

	private void setupComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridwidth = 2;
		c.weightx = 1;
		c.gridy = 0;
		this.logProgressLabel = new JLabel(Text.get("checking_folders", 0, 0));
		this.add(this.logProgressLabel, c);

		c.gridy++;
		this.logProgressBar = createProgressBar();
		this.add(this.logProgressBar, c);

		if (!this.options.onlyCreateLog) {
			c.gridy++;
			String elements = "12345 / 67890 " + Text.get("synchronizing_files_elements");
			String data = "100 Bytes / 100 Bytes";
			String all = Text.get("synchronizing_files", elements + ", " + data);
			this.backupProgressLabel = new JLabel(all);
			this.add(this.backupProgressLabel, c);

			c.gridy++;
			this.backupProgressBar = createProgressBar();
			this.add(this.backupProgressBar, c);

			c.gridy++;
			this.currentOperationLabel = new JLabel("");
			this.add(this.currentOperationLabel, c);
		}

		c.gridy++;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JPanel(), c);

		c.gridy++;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.buttonGridConstraints = c;
		this.cancelButton = new JButton(Text.get("cancel"), Images.cancel());
		this.add(this.cancelButton, c);
	}

	private JProgressBar createProgressBar() {
		JProgressBar result = new JProgressBar(SwingConstants.HORIZONTAL);
		result.setMinimumSize(new Dimension(300, 35));
		result.setPreferredSize(new Dimension(300, 35));
		result.setStringPainted(true);
		return result;
	}

	public void setFinishedUI(Runnable completion, RunnableThrowsException openLogFile) {
		if (this.options.onlyCreateLog)
			setTitle(Text.get("log_finished"));
		else
			setTitle(Text.get("backup_finished"));
		this.remove(this.cancelButton);
		GridBagConstraints c = this.buttonGridConstraints;
		c.gridwidth = 1;
		this.closeButton = new JButton(Text.get("close_finished_dialog"), Images.done());
		this.add(this.closeButton, c);
		this.closeButton.addActionListener(event -> {
			completion.run();
		});

		c.gridx = 1;
		this.openLogButton = new JButton(Text.get("open_log"), Images.read());
		this.add(this.openLogButton, c);
		this.openLogButton.addActionListener(event -> openLogFile
			.runWithoutRuntimeException());
		this.rootPane.updateUI();
	}

}
