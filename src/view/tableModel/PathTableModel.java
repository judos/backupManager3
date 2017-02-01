package view.tableModel;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.PathEntry;
import model.PathStorage;
import model.Text;

public class PathTableModel implements TableModel {

	private PathStorage storage;

	public PathTableModel(PathStorage storage) {
		this.storage = storage;
	}

	@Override
	public int getRowCount() {
		return this.storage.getSize();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0)
			return Text.get("change_path");
		else if (columnIndex == 1)
			return Text.get("backup_path");
		else if (columnIndex == 2)
			return Text.get("status");
		else
			return "X";
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex < 3)
			return String.class;
		return Boolean.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 3;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		PathEntry entry = this.storage.getPathEntry(rowIndex);
		if (entry == null)
			return "";
		if (columnIndex == 0) {
			return entry.getChangePath();
		}
		if (columnIndex == 1) {
			return entry.getBackupPath();
		}
		if (columnIndex == 2) {
			return entry.getStatus();
		}
		if (columnIndex == 3) {
			return entry.isSelected();
		}
		return "";
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex == 3) {
			PathEntry entry = this.storage.getPathEntry(rowIndex);
			entry.setSelected((boolean) aValue);
		}
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}

}
