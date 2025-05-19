package at.technikum.frontend;

import at.technikum.frontend.utils.AppProperties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TourPlannerApplication extends Application {
  static public void closeWindow(Node node) {
    Stage stage = (Stage) node.getScene().getWindow();
    stage.close();
  }

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(TourPlannerApplication.class.getResource("main_window.fxml"), AppProperties.getInstance().getI18n());
    Scene scene = new Scene(fxmlLoader.load(), 1040, 600);
    stage.setTitle(AppProperties.getInstance().getI18n().getString("main.title"));
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}