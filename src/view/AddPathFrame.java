package view;

import java.awt.*;
import java.io.File;
import java.util.function.BiConsumer;

import javax.swing.*;

import ch.judos.generic.files.FileUtils;
import model.Text;

public class AddPathFrame extends JDialog {

	private static final Dimension preferedTextFieldSize = new Dimension(400, 25);

	private JTextField changePathField;
	private JTextField backupPathField;

	private JButton browseChangePathButton;

	private JButton browseBackupPathButton;

	private JButton addButton;

	private JButton cancelButton;

	private BiConsumer<String, String> addPathCompletion;
	private static final long serialVersionUID = -8915344722242312624L;

	public AddPathFrame(JFrame parent) {
		super(parent);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle(Text.get("add_path"));
		addComponents();
		pack();
		setLocationRelativeTo(parent);
		setupActions();
	}

	private void setupActions() {
		this.cancelButton.addActionListener(event -> this.dispose());
		this.browseChangePathButton.addActionListener(event -> {
			File f = FileUtils.requestDir(this, Text.get("browse"));
			this.changePathField.setText(f.getAbsolutePath());
		});
		this.browseBackupPathButton.addActionListener(event -> {
			File f = FileUtils.requestDir(this, Text.get("browse"));
			this.backupPathField.setText(f.getAbsolutePath());
		});
		this.addButton.addActionListener(event -> {
			File changePath = new File(this.changePathField.getText());
			File backupPath = new File(this.backupPathField.getText());
			if (changePath.isDirectory() && changePath.exists() && backupPath.isDirectory()
				&& backupPath.exists()) {
				this.addPathCompletion.accept(changePath.getAbsolutePath(), backupPath
					.getAbsolutePath());
				this.dispose();
			}
		});
	}

	private void addComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 5, 5);
		add(new JLabel(Text.get("change_path") + ":"), c);

		c.gridy = 1;
		add(new JLabel(Text.get("backup_path") + ":"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.changePathField = new JTextField();
		this.changePathField.setPreferredSize(preferedTextFieldSize);
		this.changePathField.setMinimumSize(preferedTextFieldSize);

		add(this.changePathField, c);

		c.gridy = 1;
		this.backupPathField = new JTextField();
		this.backupPathField.setPreferredSize(preferedTextFieldSize);
		this.backupPathField.setMinimumSize(preferedTextFieldSize);
		add(this.backupPathField, c);

		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		this.browseChangePathButton = new JButton(Text.get("browse"));
		add(this.browseChangePathButton, c);

		c.gridy = 1;
		this.browseBackupPathButton = new JButton(Text.get("browse"));
		add(this.browseBackupPathButton, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		buttons.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.addButton = new JButton(Text.get("add"), new ImageIcon("ok icon-40.png"));
		buttons.add(this.addButton);
		this.cancelButton = new JButton(Text.get("cancel"), new ImageIcon(
			"cancel icon-40.png"));
		buttons.add(this.cancelButton);
		add(buttons, c);
	}

	public void setAddPathListener(BiConsumer<String, String> addPath) {
		this.addPathCompletion = addPath;
	}

}
