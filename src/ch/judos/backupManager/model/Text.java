package ch.judos.backupManager.model;

import java.util.Locale;
import java.util.Locale.LanguageRange;
import java.util.ResourceBundle;

import ch.judos.generic.data.DynamicList;

public class Text {

	private ResourceBundle bundle;
	private static final Text sharedInstance = new Text();
	private boolean output = false;

	private Text() {
		Locale currentLocale = Locale.getDefault();
		if (output)
			System.out.println("Host locale: " + currentLocale);
		LanguageRange high = new LanguageRange(currentLocale.toLanguageTag());
		LanguageRange low = new LanguageRange("en-US", LanguageRange.MIN_WEIGHT);
		// Locale l1 = new Locale("en");
		Locale l2 = new Locale("de");
		Locale best = Locale.lookup(new DynamicList<>(high, low), new DynamicList<>(l2));
		if (output)
			System.out.println("Best matching locale: " + best);
		this.bundle = ResourceBundle.getBundle("TextBundle", best);
	}

	public static String get(String string) {
		try {
			return sharedInstance.bundle.getString(string);
		}
		catch (Exception e) {
			System.err.println("Text key not found: " + string);
			return string;
		}
	}
}
