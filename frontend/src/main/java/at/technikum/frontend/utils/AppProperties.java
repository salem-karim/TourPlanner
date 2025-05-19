package at.technikum.frontend.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

// TODO: change this to a singleton
@Slf4j
@Getter
public class AppProperties {
  private static AppProperties instance;
  public ResourceBundle i18n;
  private Locale locale;
  private final Properties appProperties;

  private AppProperties() {
    appProperties = new java.util.Properties();
    try (InputStream input = getClass().getResourceAsStream("/at/technikum/frontend/application.properties")) {
      appProperties.load(input);

      // Initialize language from properties file
      String language = appProperties.getProperty("language", "en").replace("\"", "");
      setLanguage(language);
    } catch (IOException e) {
      log.error("Failed to load application properties", e);
      // Default to English if properties can't be loaded
      setLanguage("en");
    }
  }
  
  public static synchronized AppProperties getInstance() {
    if (instance == null) {
      instance = new AppProperties();
    }
    return instance;
  }
  

  public void setLanguage(String newLang) {
    locale = new Locale.Builder().setLanguage(newLang).build();
    i18n = ResourceBundle.getBundle("at.technikum.frontend.i18n", locale);
  }

  public ResourceBundle getBundle() {
    return i18n;
  }

  public String get(String key) {
    return i18n.getString(key);
  }

  public String getProperty(String key) {
    return appProperties.getProperty(key);
  }

  public String getProperty(String key, String defaultValue) {
    return appProperties.getProperty(key, defaultValue);
  }

  public String getTourApiUrl() {
    return getProperty("TourAPIURL");
  }

  public String getLogApiUrl() {
    return getProperty("LogAPIURL");
  }
  
}
