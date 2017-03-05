package ch.judos.backupManager.view.table;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import ch.judos.backupManager.model.PathEntry;
import ch.judos.backupManager.model.PathStorage;

public class PathTable extends JTable {

	private static final long serialVersionUID = -6914521663472724152L;
	public static final int INDEX_CHANGE_PATH = 0;
	public static final int INDEX_BACKUP_PATH = 1;
	public static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(2, 5, 2, 5);
	public static final Color STANDARD_BG = new Color(230, 255, 240);
	public static final Color CHECKING_BG = new Color(230, 240, 255);
	public static final Color ERROR_BG = new Color(255, 240, 230);

	public PathTable(PathStorage storage) {
		setRowHeight(30);
		setFillsViewportHeight(true);
		setDefaultRenderer(Object.class, new CustomTableCellRenderer(storage));
		setDefaultRenderer(Boolean.class, new CustomBooleanTableCellRenderer(storage));
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int rowIndex = PathTable.this.rowAtPoint(e.getPoint());
				if (rowIndex > -1) {
					PathEntry entry = storage.getPathEntry(rowIndex);
					entry.setSelected(!entry.isSelected());
					SwingUtilities.invokeLater(PathTable.this::repaint);
				}
			}
		});

		setGridColor(Color.LIGHT_GRAY);
		setSelectionForeground(Color.BLACK);
		setSelectionBackground(STANDARD_BG);
	}
}
