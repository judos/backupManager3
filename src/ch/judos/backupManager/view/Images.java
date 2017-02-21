package ch.judos.backupManager.view;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import ch.judos.backupManager.model.Constants;

public class Images {

	public static Icon add() {
		return new ImageIcon(Constants.APP_DATA.getPath() + "/add icon-40.png");
	}

	public static Icon done() {
		return new ImageIcon(Constants.APP_DATA.getPath() + "/ok icon-40.png");
	}

	public static Icon cancel() {
		return new ImageIcon(Constants.APP_DATA.getPath() + "/cancel icon-40.png");
	}

	public static Icon remove() {
		return new ImageIcon(Constants.APP_DATA.getPath() + "/remove icon-40.png");
	}

	public static Icon backup() {
		return new ImageIcon(Constants.APP_DATA.getPath() + "/backup icon-40.png");
	}

	public static Icon read() {
		return new ImageIcon("AppData/read icon-40.png");
	}

	public static Image backupImage() throws Exception {
		return ImageIO.read(new File("AppData/backup icon-40.png"));
	}

}
