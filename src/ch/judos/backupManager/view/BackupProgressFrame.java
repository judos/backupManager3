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
	private JProgressBar progressBar;
	private JButton cancelButton;
	private JProgressBar progressBar2;
	private static final long serialVersionUID = -163978212692777202L;

	public BackupProgressFrame(MainFrame parent, BackupOptions options) {
		super(parent);
		this.options = options;
		setTitle(Text.get("backup_running"));
		setupComponents();
		pack();
		setLocationRelativeTo(parent);
	}

	private void setupComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 1;
		c.gridy = 0;
		this.add(new JLabel(Text.get("checking_files")), c);

		c.gridy++;
		this.progressBar = createProgressBar();
		this.add(this.progressBar, c);

		if (!this.options.onlyCreateLog) {
			c.gridy++;
			this.add(new JLabel(Text.get("synchronizing_files")), c);

			c.gridy++;
			this.progressBar2 = createProgressBar();
			this.add(this.progressBar2, c);
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
		result.setMinimumSize(new Dimension(300, 40));
		result.setPreferredSize(new Dimension(300, 40));
		return result;
	}

}
