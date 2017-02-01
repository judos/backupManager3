package view;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import controller.util.JTableColumnResizer;
import model.PathStorage;
import model.Text;
import view.tableModel.PathTableModel;

public class MainFrame extends JFrame {

	public PathTableModel tableModel;
	public JTable table;
	public JButton addButton;
	private PathStorage storage;
	public JButton removeButton;
	public JButton startBackupButton;
	private JScrollPane tableScrollPane;
	private static final long serialVersionUID = 6942789973092413163L;

	public MainFrame(PathStorage storage) throws Exception {
		this.storage = storage;
		setLookAndFeel();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("BackupManager 3 - judos 2017");
		addComponents();

		Dimension prefered = this.getPreferredSize();
		this.setPreferredSize(new Dimension(Math.max(600, prefered.width + 50),
			prefered.height));
		this.setMinimumSize(new Dimension(300, 200));
		this.pack();
		this.setLocationRelativeTo(null);
	}

	private void addComponents() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridwidth = 4;
		c.weightx = 1;
		c.weighty = 1;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.BOTH;
		this.tableModel = new PathTableModel(this.storage);
		this.table = new JTable(this.tableModel);
		this.tableScrollPane = new JScrollPane();
		this.tableScrollPane.setViewportView(table);
		this.add(this.tableScrollPane, c);
		this.table.setRowHeight(30);
		this.tableScrollPane.setHorizontalScrollBarPolicy(
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.tableScrollPane.setVerticalScrollBarPolicy(
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.table.setFillsViewportHeight(true);
		resizeColumnWidths();

		c.gridy = 1;
		c.weighty = 0;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		this.addButton = new JButton(Text.get("add_path"), new ImageIcon("add icon-40.png"));
		this.add(this.addButton, c);

		c.gridx = 1;
		this.removeButton = new JButton(Text.get("remove_path"), new ImageIcon(
			"remove icon-40.png"));
		this.add(this.removeButton, c);

		c.gridx = 3;
		c.anchor = GridBagConstraints.EAST;
		this.startBackupButton = new JButton(Text.get("start_backup"), new ImageIcon(
			"backup icon-40.png"));
		this.add(this.startBackupButton, c);

		c.gridx = 2;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(new JPanel(), c);
	}

	private void resizeColumnWidths() {
		JTableColumnResizer.adjustColumnSizes(this.table);
		Dimension d = table.getPreferredSize();
		this.tableScrollPane.setPreferredSize(new Dimension(d.width, 300));
	}

	private void setLookAndFeel() throws Exception {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
			| UnsupportedLookAndFeelException e) {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
	}

	public void addShutdownListener(Runnable listeningMethod) {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				listeningMethod.run();
			}
		});
	}
}
