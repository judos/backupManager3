//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ch.judos.generic;

import ch.judos.generic.data.DynamicList;
import ch.judos.java.util.UTF8Control;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Locale.LanguageRange;

public class Text {
  private ResourceBundle bundle;
  private static final Text sharedInstance = new Text();
  private boolean output = false;

  private Text() {
    Locale currentLocale = Locale.getDefault();
    if (this.output) {
      System.out.println("Host locale: " + currentLocale);
    }

    LanguageRange high = new LanguageRange(currentLocale.toLanguageTag());
    Locale best = Locale.lookup(new DynamicList(new LanguageRange[]{high, this.getDefaultLocale()}), this.existingTextPropertyBundles());
    if (this.output) {
      System.out.println("Best matching locale: " + best);
    }

    // TODO: fix for library
		best = new Locale("de");

    this.bundle = ResourceBundle.getBundle("TextBundle", best, new UTF8Control());
  }

  public LanguageRange getDefaultLocale() {
    DynamicList<Locale> existingLocales = this.existingTextPropertyBundles();
    return new LanguageRange(((Locale)existingLocales.get(0)).getLanguage(), 0.0D);
  }

  public DynamicList<Locale> existingTextPropertyBundles() {
    DynamicList<Locale> result = new DynamicList();
    result.add(new Locale("de"));
    return result;
  }

  public static String get(String string, Object... args) {
    try {
      String result = sharedInstance.bundle.getString(string);
      if (args.length > 0) {
        Object[] var6 = args;
        int var5 = args.length;

        for(int var4 = 0; var4 < var5; ++var4) {
          Object arg = var6[var4];
          result = result.replaceFirst("\\%", arg.toString());
        }
      }

      return result;
    } catch (Exception var7) {
      System.err.println("Text key not found: " + string);
      return string;
    }
  }
}
