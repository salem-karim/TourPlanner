package at.technikum.frontend;

import at.technikum.frontend.utils.AppProperties;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TourPlannerApplication extends Application {
  static public void closeWindow(final Node node) {
    final Stage stage = (Stage) node.getScene().getWindow();
    stage.close();
  }

  public static void main(final String[] args) {
    launch();
  }

  @Override
  public void start(final Stage stage) throws IOException {
    Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    final FXMLLoader fxmlLoader = new FXMLLoader(TourPlannerApplication.class.getResource("main_window.fxml"),
        AppProperties.getInstance().getI18n());
    final Scene scene = new Scene(fxmlLoader.load(), 1040, 600);
    stage.setTitle(AppProperties.getInstance().getI18n().getString("main.title"));
    stage.setScene(scene);
    stage.show();
  }
}
