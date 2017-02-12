package ch.judos.backupManager.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

import ch.judos.backupManager.model.BackupOptions;
import ch.judos.backupManager.model.Text;

public class BackupProgressFrame extends JDialog {

	private BackupOptions options;
	public JProgressBar logProgressBar;
	public JButton cancelButton;
	public JProgressBar backupProgressBar;
	public JLabel logProgressLabel;
	public JLabel backupProgressLabel;
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
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	private void setupComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 1;
		c.gridy = 0;
		this.logProgressLabel = new JLabel(Text.get("checking_folders", 0, 0));
		this.add(this.logProgressLabel, c);

		c.gridy++;
		this.logProgressBar = createProgressBar();
		this.add(this.logProgressBar, c);

		if (!this.options.onlyCreateLog) {
			c.gridy++;
			this.backupProgressLabel = new JLabel(Text.get("synchronizing_files"));
			this.add(this.backupProgressLabel, c);

			c.gridy++;
			this.backupProgressBar = createProgressBar();
			this.add(this.backupProgressBar, c);
		}

		c.gridy++;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JPanel(), c);

		c.gridy++;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
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

}
