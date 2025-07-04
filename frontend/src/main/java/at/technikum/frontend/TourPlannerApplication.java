package at.technikum.frontend;

import at.technikum.frontend.utils.AppProperties;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
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
    if(AppProperties.getInstance().getProperty("style").trim().equals("dark")) {
      Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    } else {
      Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    }
    log.info(Application.getUserAgentStylesheet()); 
    final FXMLLoader fxmlLoader = new FXMLLoader(TourPlannerApplication.class.getResource("main_window.fxml"),
        AppProperties.getInstance().getI18n());
    final Scene scene = new Scene(fxmlLoader.load(), 1250, 610);
    stage.setTitle(AppProperties.getInstance().getI18n().getString("main.title"));
    stage.setScene(scene);
    stage.show();
  }
}
