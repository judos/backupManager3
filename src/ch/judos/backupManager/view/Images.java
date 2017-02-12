package ch.judos.backupManager.view;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Images {

	public static Icon add() {
		return new ImageIcon("AppData/add icon-40.png");
	}

	public static Icon done() {
		return new ImageIcon("AppData/ok icon-40.png");
	}

	public static Icon cancel() {
		return new ImageIcon("AppData/cancel icon-40.png");
	}

	public static Icon remove() {
		return new ImageIcon("AppData/remove icon-40.png");
	}

	public static Icon backup() {
		return new ImageIcon("AppData/backup icon-40.png");
	}

	public static Image backupImage() throws Exception {
		return ImageIO.read(new File("AppData/backup icon-40.png"));
	}

}
