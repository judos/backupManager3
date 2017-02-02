package ch.judos.backupManager.view;

import javax.swing.JDialog;

import ch.judos.backupManager.model.Text;

public class BackupProgressFrame extends JDialog {

	private static final long serialVersionUID = -163978212692777202L;

	public BackupProgressFrame(MainFrame parent) {
		super(parent);
		setTitle(Text.get("backup_running"));

		pack();
		setLocationRelativeTo(parent);
	}

}
