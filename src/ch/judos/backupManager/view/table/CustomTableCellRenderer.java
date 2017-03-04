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
	private static final int INDEX_CHANGE_PATH = 0;
	private static final int INDEX_BACKUP_PATH = 1;
	private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(2, 5, 2, 5);
	private static final Color STANDARD_BG = new Color(230, 240, 255);
	private static final Color ERROR_BG = new Color(255, 240, 230);

	public CustomTableCellRenderer(PathStorage storage) {
		this.storage = storage;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column) {
		PathEntry entry = this.storage.getPathEntry(row);
		isSelected = entry.isSelected();
		boolean isMarkedRed = isMarkedRed(column, entry);

		table.setGridColor(Color.LIGHT_GRAY);
		table.setSelectionBackground(STANDARD_BG);
		table.setSelectionForeground(Color.BLACK);

		if (isMarkedRed) {
			table.setSelectionBackground(ERROR_BG);
		}
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (isMarkedRed) {
			Border border = BorderFactory.createLineBorder(Color.red);
			Border border2 = BorderFactory.createCompoundBorder(border, EMPTY_BORDER);
			setBorder(border2);
		}
		else {
			setBorder(EMPTY_BORDER);
		}
		return this;
	}

	private boolean isMarkedRed(int column, PathEntry entry) {
		if (column == INDEX_CHANGE_PATH && entry.isChangePathMissing())
			return true;
		if (column == INDEX_BACKUP_PATH && entry.isBackupPathMissing())
			return true;
		return false;
	}
}
