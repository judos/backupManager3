package ch.judos.backupManager.view.table;

import java.awt.Component;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellRenderer;

import ch.judos.backupManager.model.PathStorage;

public class CustomBooleanTableCellRenderer extends BooleanRenderer {

	private static final long serialVersionUID = -5902721531270492583L;
	private PathStorage storage;

	public CustomBooleanTableCellRenderer(PathStorage storage) {
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
class BooleanRenderer extends JCheckBox implements TableCellRenderer, UIResource {
	private static final long serialVersionUID = -798128672110086979L;
	private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	public BooleanRenderer() {
		super();
		setHorizontalAlignment(JLabel.CENTER);
		setBorderPainted(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		}
		else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		setSelected((value != null && ((Boolean) value).booleanValue()));

		if (hasFocus) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		}
		else {
			setBorder(noFocusBorder);
		}

		return this;
	}
}
