package at.technikum.tourplanner;

import at.technikum.tourplanner.controllers.TourPlannerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class TourPlannerApplication extends Application {
  static final private String LANGUAGE = "en";
  static final private Locale locale = new Locale.Builder().setLanguage(LANGUAGE).build();
  static final public ResourceBundle i18n = ResourceBundle.getBundle("at.technikum.tourplanner.i18n", locale);

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(TourPlannerApplication.class.getResource("main_window.fxml"), i18n);
    fxmlLoader.setController(new TourPlannerController());
    Scene scene = new Scene(fxmlLoader.load(), 900, 600);
    stage.setTitle(i18n.getString("main.title"));
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}