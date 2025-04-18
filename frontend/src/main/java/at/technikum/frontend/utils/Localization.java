package at.technikum.frontend.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class Localization {
  private static String language = "en";
  private static Locale locale = new Locale.Builder().setLanguage(language).build();
  public static ResourceBundle i18n = ResourceBundle.getBundle("at.technikum.frontend.i18n", locale);

  public static void setLanguage(String newLang) {
    language = newLang;
    locale = new Locale.Builder().setLanguage(language).build();
    i18n = ResourceBundle.getBundle("at.technikum.frontend.i18n", locale);
  }

  public static ResourceBundle getBundle() {
    return i18n;
  }

  public static String get(String key) {
    return i18n.getString(key);
  }

//  public static Locale getLocale() {
//    return locale;
//  }
}
