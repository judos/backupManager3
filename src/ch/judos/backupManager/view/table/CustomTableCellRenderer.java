package ch.judos.backupManager.view.table;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

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
		isSelected = this.storage.getPathEntry(row).isSelected();
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		return this;
	}
}
