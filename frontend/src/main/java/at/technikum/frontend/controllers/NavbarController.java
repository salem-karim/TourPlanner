package at.technikum.frontend.controllers;

// navbar: export und import as json(one tour), report(generate pdf from 1 tour+logs, searchbar (all infos)

import at.technikum.frontend.TourPlannerApplication;
import at.technikum.frontend.utils.AppProperties;
import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NavbarController implements Initializable {
    @FXML
    private MenuItem importMenuItem;
    @FXML
    private MenuItem exportMenuItem;
    @FXML
    private MenuItem pdfMenuItem;
    @FXML
    public MenuItem englishButton, germanButton, polishButton;
    @FXML
    private MenuItem quitButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        importMenuItem.setOnAction(event -> onImport());
        exportMenuItem.setOnAction(event -> onExport());
        pdfMenuItem.setOnAction(event -> onPDF());

        //englishButton.setOnAction(event -> changeLanguage("en"));
        //germanButton.setOnAction(event -> changeLanguage("de"));
        //polishButton.setOnAction(event -> changeLanguage("pl"));

        //quitButton.setOnAction(event -> TourPlannerApplication.closeWindow(newEditDeleteButtonBar));
    }

    public void onImport() {
        //actual code
    }

    public void onExport() {
        //actual code
    }

    public void onPDF( ) {
        //actual code
    }

    /*
    private void changeLanguage(String newLang) {
        if (newLang.equals(AppProperties.getInstance().getLocale().getLanguage())) return;

        // Update the language
        AppProperties.getInstance().setLanguage(newLang);

        try {
            // Reload the entire scene with the new language
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/frontend/main_window.fxml"),
                    AppProperties.getInstance().getI18n());
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) tourListView.getScene().getWindow();

            // Replace the scene content
            Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
            stage.setScene(scene);
        } catch (IOException e) {
            log.error("Failed to reload view after language change", e);
        }
    }
     */


}
