package ch.judos.backupManager.view;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Images {

	private static ImageIcon loadIcon(String relative) {
		return new ImageIcon(Images.class.getResource("/AppData/" + relative));
	}

	private static BufferedImage loadImage(String relative) {
		try {
			return ImageIO.read(Images.class.getResource("/AppData/" + relative));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Icon add() {
		return loadIcon("add icon-40.png");
	}

	public static Icon done() {
		return loadIcon("ok icon-40.png");
	}

	public static Icon cancel() {
		return loadIcon("cancel icon-40.png");
	}

	public static Icon remove() {
		return loadIcon("remove icon-40.png");
	}

	public static Icon backup() {
		return loadIcon("backup icon-40.png");
	}

	public static Icon read() {
		return loadIcon("read icon-40.png");
	}

	public static Image backupImage() throws Exception {
		return loadImage("backup icon-40.png");
	}

}
