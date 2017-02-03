package ch.judos.backupManager.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import ch.judos.backupManager.model.BackupOptions;
import ch.judos.backupManager.model.Text;

public class BackupProgressFrame extends JDialog {

	private BackupOptions options;
	private JProgressBar progressBar;
	private JButton cancelButton;
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
		this.progressBar = new JProgressBar(SwingConstants.HORIZONTAL);
		this.progressBar.setMinimumSize(new Dimension(300, 40));
		this.add(this.progressBar, c);

		c.gridy = 1;
		this.cancelButton = new JButton(Text.get("cancel"), Images.cancel());
		this.add(this.cancelButton, c);
	}

}
