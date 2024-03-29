package ch.judos.backupManager.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.Consumer;

import javax.swing.*;

import ch.judos.backupManager.model.BackupOptions;
import ch.judos.generic.Text;

public class BackupOptionsFrame extends JDialog {

	private JRadioButton logOnlyButton;
	private JRadioButton fullBackupButton;
	private JCheckBox saveLogCheckbox;
	private JButton startButton;
	private JButton cancelButton;
	private Consumer<BackupOptions> completion;
	private static final long serialVersionUID = 6071547131279844824L;

	public BackupOptionsFrame(MainFrame parent) {
		super(parent);
		setTitle(Text.get("options"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setupContent();
		setupActionListeners();
		pack();
		setLocationRelativeTo(parent);
	}

	private void setupActionListeners() {
		this.cancelButton.addActionListener(event -> this.dispose());
		this.startButton.addActionListener(event -> this.startBackup());
	}

	private void startBackup() {
		boolean onlyCreateLog = this.logOnlyButton.isSelected();
		boolean saveLog = this.saveLogCheckbox.isSelected();
		BackupOptions options = new BackupOptions(onlyCreateLog, saveLog);
		this.completion.accept(options);
	}

	private void setupContent() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 5, 5);
		add(new JLabel(Text.get("process") + ":"), c);

		c.gridy = 1;
		c.insets.left = 20;
		ButtonGroup processMode = new ButtonGroup();
		this.logOnlyButton = new JRadioButton(Text.get("only_log"));
		this.logOnlyButton.setSelected(true);
		processMode.add(this.logOnlyButton);
		add(this.logOnlyButton, c);

		c.gridy++;
		this.fullBackupButton = new JRadioButton(Text.get("full_backup"));
		processMode.add(this.fullBackupButton);
		add(this.fullBackupButton, c);

		c.gridy++;
		c.insets.left = 5;
		add(new JLabel(Text.get("afterwards") + ":"), c);

		c.gridy++;
		this.saveLogCheckbox = new JCheckBox(Text.get("store_log"));
		this.saveLogCheckbox.setSelected(true);
		add(this.saveLogCheckbox, c);

		c.gridy++;
		c.insets.left = 5;
		c.gridwidth = 1;
		this.startButton = new JButton(Text.get("start_backup"), Images.backup());
		add(this.startButton, c);

		c.gridx = 1;
		this.cancelButton = new JButton(Text.get("cancel"), Images.cancel());
		add(this.cancelButton, c);
	}

	public void setCompletion(Consumer<BackupOptions> completion) {
		this.completion = completion;
	}

}
