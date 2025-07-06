package at.technikum.frontend.BL.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppProperties {
  private static AppProperties instance;
  @Getter
  public ResourceBundle i18n;
  @Getter
  private Locale locale;
  private final Properties appProperties;

  private AppProperties() {
    appProperties = new java.util.Properties();
    try (InputStream input = getClass().getResourceAsStream("/at/technikum/frontend/application.properties")) {
      appProperties.load(input);

      // Initialize language from properties file
      final String language = appProperties.getProperty("language", "en").replace("\"", "");
      setLanguage(language);
    } catch (final IOException e) {
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

  public void setLanguage(final String newLang) {
    locale = new Locale.Builder().setLanguage(newLang).build();
    i18n = ResourceBundle.getBundle("at.technikum.frontend.i18n", locale);
  }

  public String get(final String key) {
    return i18n.getString(key);
  }

  public String getProperty(final String key) {
    return appProperties.getProperty(key);
  }

  public String getProperty(final String key, final String defaultValue) {
    return appProperties.getProperty(key, defaultValue);
  }

  public String getTourApiUrl() {
    return getProperty("TourAPIURL");
  }

  public String getLogApiUrl() {
    return getProperty("LogAPIURL");
  }

  public String getORSApiUrl() {
    return getProperty("ORSAPIURL");
  }

  public String getORSApiKey() {
    return getProperty("ORSAPIKEY");
  }
}
