package ch.judos.backupManager.view.table;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import ch.judos.backupManager.model.PathEntry;
import ch.judos.backupManager.model.PathStorage;
import ch.judos.generic.Text;

public class PathTableModel implements TableModel {

	private PathStorage storage;

	public PathTableModel(PathStorage storage, JTable table) {
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
			return "";
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex < 3)
			return String.class;
		return Boolean.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
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
			return entry.getLastBackup();
		}
		if (columnIndex == 3) {
			return entry.isSelected();
		}
		return "";
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}

}
