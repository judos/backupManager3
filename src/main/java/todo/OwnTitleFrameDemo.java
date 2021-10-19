package todo;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

public class OwnTitleFrameDemo extends JFrame {
	private JLabel titleBar;
	private JLabel messageLabel;
	private JPanel contentPanel;
	private JButton button;
	private JPanel buttonPanel;
	private int xPos, yPos;

	public OwnTitleFrameDemo(String title) {
		super();
		setUndecorated(true); // Fensterrahmen entfernen

		// neuer Fensterrahmen, ohne Titelleiste
		contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

		// Textinhalt - Message
		messageLabel = new JLabel(
			"Du siehst dir gerade eine Demonstration eines JFrames mit eigener Titelleiste an.",
			SwingConstants.CENTER);
		messageLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		contentPanel.add(messageLabel, BorderLayout.CENTER);

		// Titelleiste
		titleBar = new JLabel(title);
		titleBar.setOpaque(true);
		titleBar.setBorder(BorderFactory.createEmptyBorder(2, 3, 2, 2));

		// Farben des Betriebssystems für Titelleisten ohne Design verwenden
		titleBar.setBackground(UIManager.getDefaults().getColor(
			"InternalFrame.activeTitleBackground"));
		titleBar.setForeground(UIManager.getDefaults().getColor(
			"InternalFrame.activeTitleForeground"));

		// MouseMotionListener für die Titelleiste
		titleBar.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				Point window = OwnTitleFrameDemo.this.getLocation();
				OwnTitleFrameDemo.this.setLocation(window.x + e.getX() - xPos, window.y + e
					.getY() - yPos);
			}
		});

		// MouseListener für die Titelleiste
		titleBar.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				xPos = e.getX();
				yPos = e.getY();
			}
		});

		button = new JButton("OK");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		buttonPanel = new JPanel();
		buttonPanel.setBorder(new EmptyBorder(new Insets(0, 0, 10, 0)));
		buttonPanel.add(button);

		contentPanel.add(titleBar, BorderLayout.NORTH);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);
		add(contentPanel);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		new OwnTitleFrameDemo("<html>[b]OwnTitleFrameDemo[/b]</html>");
	}
}