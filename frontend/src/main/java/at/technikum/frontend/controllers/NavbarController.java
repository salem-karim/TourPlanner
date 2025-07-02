package at.technikum.frontend.controllers;

// navbar: export und import as json(one tour), report(generate pdf from 1 tour+logs, searchbar (all infos)

import at.technikum.common.models.Tour;
import at.technikum.common.models.TransportType;
import at.technikum.frontend.TourPlannerApplication;
import at.technikum.frontend.utils.AppProperties;
import at.technikum.frontend.viewmodels.TourViewModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

//pdf import
import com.lowagie.text.pdf.PdfWriter;
import java.util.List;

//json import
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

import java.io.FileOutputStream;
import java.util.UUID;

//todo: add logging ???

public class NavbarController{
    private MenuItem importMenuItem;
    private MenuItem exportMenuItem;
    private MenuItem pdfMenuItem;

    private MenuItem language_englishMenuItem;
    private MenuItem language_germanMenuItem;
    private MenuItem language_polishMenuItem;

    TourPlannerController tourPlannerController;

    public NavbarController(TourPlannerController tourPlannerController) {
        this.tourPlannerController = tourPlannerController;
    }

    public void setImportMenuItem(MenuItem importMenuItem, Stage stage) {
        this.importMenuItem = importMenuItem;
        this.importMenuItem.setOnAction(event -> onImport(stage));
    }

    public void setExportMenuItem(MenuItem exportMenuItem) {
        this.exportMenuItem = exportMenuItem;
        this.exportMenuItem.setOnAction(event -> onExport());
    }

    public void setTourPDFMenuItem(MenuItem pdfMenuItem) {
        this.pdfMenuItem = pdfMenuItem;
        this.pdfMenuItem.setOnAction(event -> {
            try {
                onTourPDF();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void setSummarizePDFMenuItem(MenuItem pdfMenuItem) {
        this.pdfMenuItem = pdfMenuItem;
        this.pdfMenuItem.setOnAction(event -> {
            try {
                onSummarizePDF();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setLanguageMenuItems(MenuItem englishItem, MenuItem germanItem, MenuItem polishItem, Stage stage) {
        this.language_englishMenuItem = englishItem;
        this.language_germanMenuItem = germanItem;
        this.language_polishMenuItem = polishItem;

        this.language_englishMenuItem.setOnAction(event -> onChangeLanguage("en", stage));
        this.language_germanMenuItem.setOnAction(event -> onChangeLanguage("de", stage));
        this.language_polishMenuItem.setOnAction(event -> onChangeLanguage("pl", stage));
    }


    // ---- Logic for Navbar Items ----

    public void onImport(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select JSON File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                // Deserialize a single Tour, not a list
                Tour importedTour = objectMapper.readValue(selectedFile, Tour.class);

                System.out.println(importedTour.getName());  // Print the imported tour name

                tourPlannerController.addTourThroughImport(new TourViewModel(importedTour));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void onExport() {
        System.out.println("Export clicked");

        TourViewModel tvm = tourPlannerController.getSelectedTour();
        Tour tour = tvm.toTour();

        ObjectMapper mapper = new ObjectMapper();

        try {
            // Write to file
            mapper.writeValue(new File("json_files/"+ tour.getName() + "_"+tour.getId()+".json"), tour);
            System.out.println("Tour exported to JSON.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onTourPDF() throws IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("pdf_files/tourname_report.pdf"));
        document.open();

        document.add(new Paragraph("Sample PDF"));

        document.close();
    }

    public void onSummarizePDF() throws FileNotFoundException {



        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("pdf_files/tour_summary.pdf"));
        document.open();

        document.add(new Paragraph("Sample PDF"));

        document.close();
    }

    private void onChangeLanguage(String newLang, Stage stage) {
        if (newLang.equals(AppProperties.getInstance().getLocale().getLanguage())) return;

        // Update the language
        AppProperties.getInstance().setLanguage(newLang);

        try {
            // Reload the entire scene with the new language
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/frontend/main_window.fxml"),
                    AppProperties.getInstance().getI18n());
            Parent root = loader.load();

            // Replace the scene content
            Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
            stage.setScene(scene);
        } catch (IOException e) {
            //log.error("Failed to reload view after language change", e);
        }
    }
}
