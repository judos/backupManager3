package ch.judos.backupManager.view.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import ch.judos.backupManager.model.PathEntry;
import ch.judos.backupManager.model.PathStorage;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {

	private PathStorage storage;
	private static final long serialVersionUID = 3560731896154782944L;

	public CustomTableCellRenderer(PathStorage storage) {
		this.storage = storage;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column) {
		PathEntry entry = this.storage.getPathEntry(row);
		isSelected = entry.isSelected();
		boolean isMarkedRed = isMarkedRed(column, entry);

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		setBorder(PathTable.EMPTY_BORDER);
		if (!isSelected) {
			this.setBackground(Color.WHITE);
		}
		else if (!entry.isAvailabilityChecked()) {
			this.setBackground(PathTable.CHECKING_BG);
		}
		else if (isMarkedRed) {
			this.setBackground(PathTable.ERROR_BG);
			Border border = BorderFactory.createLineBorder(Color.red);
			Border border2 = BorderFactory.createCompoundBorder(border,
				PathTable.EMPTY_BORDER);
			setBorder(border2);
		}
		else {
			this.setBackground(PathTable.STANDARD_BG);
		}
		return this;
	}

	private boolean isMarkedRed(int column, PathEntry entry) {
		if (column == PathTable.INDEX_CHANGE_PATH && entry.isChangePathMissing())
			return true;
		if (column == PathTable.INDEX_BACKUP_PATH && entry.isBackupPathMissing())
			return true;
		return false;
	}
}
