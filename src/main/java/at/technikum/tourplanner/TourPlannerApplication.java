package at.technikum.tourplanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class TourPlannerApplication extends Application {
  @Override
  public void start(Stage stage) throws IOException {
//    final Locale locale = new Locale.Builder().setLanguage("de").build();
//    final ResourceBundle i18n = ResourceBundle.getBundle("at.technikum.tourplanner.i18n", locale);
    FXMLLoader fxmlLoader = new FXMLLoader(TourPlannerApplication.class.getResource("main_window.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 900, 600);
    stage.setTitle("Tour Planner");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}