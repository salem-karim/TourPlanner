package at.technikum.frontend.PL.controllers;

import at.technikum.common.DAL.models.Tour;
import at.technikum.frontend.BL.utils.AppProperties;
import at.technikum.frontend.PL.viewmodels.LogViewModel;
import at.technikum.frontend.PL.viewmodels.TourViewModel;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

//pdf import
import com.lowagie.text.pdf.PdfWriter;

import java.util.ArrayList;
import java.util.List;

//json import
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

import java.io.FileOutputStream;
import java.util.Objects;

import com.lowagie.text.pdf.PdfPTable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class NavbarController {
  private MenuItem importMenuItem;
  private MenuItem exportMenuItem;
  private MenuItem pdfMenuItem;

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
        log.error(e.getMessage());
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

  public void setLanguageMenuItems(RadioMenuItem englishButton, RadioMenuItem germanButton, RadioMenuItem polishButton,
      Stage stage) {
    // Reset all selections first
    englishButton.setSelected(false);
    germanButton.setSelected(false);
    polishButton.setSelected(false);

    // Set current language as selected
    String currentLang = AppProperties.getInstance().getLocale().getLanguage();
    switch (currentLang) {
      case "en" -> englishButton.setSelected(true);
      case "de" -> germanButton.setSelected(true);
      case "pl" -> polishButton.setSelected(true);
    }

    englishButton.setOnAction(e -> {
      onChangeLanguage("en", stage);
      updateLanguageSelection(englishButton, germanButton, polishButton, "en");
    });

    germanButton.setOnAction(e -> {
      onChangeLanguage("de", stage);
      updateLanguageSelection(englishButton, germanButton, polishButton, "de");
    });

    polishButton.setOnAction(e -> {
      onChangeLanguage("pl", stage);
      updateLanguageSelection(englishButton, germanButton, polishButton, "pl");
    });
  }

  public void setStyleMenuItems(RadioMenuItem lightButton, RadioMenuItem darkButton, Stage stage) {
    // Reset all selections first
    lightButton.setSelected(false);
    darkButton.setSelected(false);

    // Set current style as selected
    if (Application.getUserAgentStylesheet().contains("light")) {
      lightButton.setSelected(true);
    } else {
      darkButton.setSelected(true);
    }

    lightButton.setOnAction(e -> {
      Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
      stage.getScene().getStylesheets().clear();
      stage.getScene().getStylesheets().add(Application.getUserAgentStylesheet());
    });

    darkButton.setOnAction(e -> {
      Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
      stage.getScene().getStylesheets().clear();
      stage.getScene().getStylesheets().add(Application.getUserAgentStylesheet());
    });
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

        log.info(importedTour.getName()); // Print the imported tour name

        tourPlannerController.getTourTableViewModel().newTour(new TourViewModel(importedTour));

      } catch (IOException e) {
        log.error(e.getMessage());
      }
    }
  }

  public void onExport() {
    TourViewModel tvm = tourPlannerController.getSelectedTour();
    Tour tour = tvm.toTour();

    tour.setRoute_info(tour.getRoute_info() != null ? tour.getRoute_info() : "".getBytes());
    tour.setLogs(tour.getLogs() != null ? tour.getLogs() : new ArrayList<>());
    ObjectMapper mapper = new ObjectMapper();
    final var fileChooser = new FileChooser();
    fileChooser.setTitle("Export Tour to JSON");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
    fileChooser.setInitialFileName(tour.getName().replaceAll("\\s+", "-") + "-" + tour.getId() + ".json");

    // Show save dialog
    File file = fileChooser.showSaveDialog(exportMenuItem.getParentPopup().getOwnerWindow());

    try {
      // Write to file
      if (file != null && !file.getName().endsWith(".json")) {
        // Ensure the file has a .json extension
        file = new File(file.getAbsolutePath() + ".json");
      }
      mapper
          .writeValue(
              Objects
                  .requireNonNullElseGet(file,
                      () -> new File(
                          "json_files/" + tour.getName().replaceAll("\\s+", "-") + "-" + tour.getId() + ".json")),
              tour);
      log.info("Tour exported to JSON.");
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  public void onTourPDF() throws IOException {
    TourViewModel tvm = tourPlannerController.getSelectedTour();
    Tour tour = tvm.toTour();

    var logTableVM = tvm.getLogs();
    ObservableList<LogViewModel> logs = logTableVM.getData();

    // Create file chooser dialog
    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Tour PDF Report");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
    fileChooser.setInitialFileName(tour.getName().replaceAll("\\s+", "-") + "_report.pdf");

    // Show save dialog
    File file = fileChooser.showSaveDialog(pdfMenuItem.getParentPopup().getOwnerWindow());

    if (file != null) {
      // Ensure file has .pdf extension
      if (!file.getName().endsWith(".pdf")) {
        file = new File(file.getAbsolutePath() + ".pdf");
      }

      Document document = new Document();
      PdfWriter.getInstance(document, new FileOutputStream(file));
      document.open();

      // Add tour details
      document.add(new Paragraph("Tour Report"));
      document.add(new Paragraph("Tour: " + tour.getName()));
      document.add(new Paragraph("Description: " + tour.getDescription()));
      document.add(new Paragraph("From: " + tour.getFrom()));
      document.add(new Paragraph("To: " + tour.getTo()));
      document.add(new Paragraph("Transport Type: " + tour.getTransport_type()));
      document.add(new Paragraph("Total Distance: " + String.format("%.2f", tour.getTotal_distance()) + " km"));
      document.add(new Paragraph("Estimated Time: " + String.format("%.2f", tour.getEstimated_time()) + " minutes"));
      document.add(new Paragraph(" ")); // empty line

      // Add the route image if available
      if (tour.getRoute_info() != null && tour.getRoute_info().length > 0) {
        try {
          com.lowagie.text.Image routeImage = com.lowagie.text.Image.getInstance(tour.getRoute_info());
          // Scale image if needed (optional)
          routeImage.scaleToFit(400, 300);
          document.add(routeImage);
          document.add(new Paragraph(" ")); // empty line after image
        } catch (Exception e) {
          log.error("Failed to add route image to PDF: {}", e.getMessage());
        }
      }

      // Create table for logs
      PdfPTable table = getPdfPTable(logs);

      document.add(table);

      document.close();
      log.info("Tour PDF exported to: {}", file.getAbsolutePath());
    }
  }

  private static PdfPTable getPdfPTable(ObservableList<LogViewModel> logs) {
    PdfPTable table = new PdfPTable(6); // 6 columns for your Logs fields
    table.addCell("Start DateTime");
    table.addCell("End DateTime");
    table.addCell("Distance (km)");
    table.addCell("Comment");
    table.addCell("Difficulty");
    table.addCell("Rating");

    for (LogViewModel log : logs) {
      table.addCell(log.getStartDate().toString());
      table.addCell(log.getEndDate().toString());
      table.addCell(String.valueOf(log.getTotalDistance()));
      table.addCell(log.getComment());
      table.addCell(String.valueOf(log.getDifficulty()));
      table.addCell(String.valueOf(log.getRating()));
    }
    return table;
  }

  public void onSummarizePDF() throws FileNotFoundException {
    List<TourViewModel> tours = tourPlannerController.getAllTours();

    // Create file chooser dialog
    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Tours Summary PDF");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
    fileChooser.setInitialFileName("tours_summary.pdf");

    // Show save dialog
    File file = fileChooser.showSaveDialog(pdfMenuItem.getParentPopup().getOwnerWindow());

    if (file != null) {
      // Ensure file has .pdf extension
      if (!file.getName().endsWith(".pdf")) {
        file = new File(file.getAbsolutePath() + ".pdf");
      }

      Document document = new Document();
      PdfWriter.getInstance(document, new FileOutputStream(file));
      document.open();

      document.add(new Paragraph("Summarization of all Tours"));
      document.add(new Paragraph("\n\n"));

      PdfPTable table = new PdfPTable(4);
      table.addCell("Tour name");
      table.addCell("Average time");
      table.addCell("Average distance (km)");
      table.addCell("Average Rating");

      Tour currentTour = null;
      ObservableList<LogViewModel> currentLogs = null;

      int avgTimeMinutes = 0;
      int avgTimehours = 0;
      double avgDistance = 0;
      int avgRating = 0;

      for (TourViewModel tourVM : tours) {
        currentTour = tourVM.toTour();
        table.addCell(currentTour.getName());

        currentLogs = tourVM.getLogs().getData();

        if (currentLogs == null || currentLogs.isEmpty()) {
          table.addCell("N/A");
          table.addCell("N/A");
          table.addCell("N/A");
        } else {
          avgTimeMinutes = 0;
          avgDistance = 0;
          avgRating = 0;

          for (LogViewModel log : currentLogs) {
            avgTimeMinutes += ((log.toLog().getEnd_date_time().getHour() - log.toLog().getStart_date_time().getHour())
                * 60 + (log.toLog().getEnd_date_time().getMinute() - log.toLog().getStart_date_time().getMinute()));
            avgDistance += log.toLog().getTotal_distance();
            avgRating += log.getRating();
          }

          avgTimeMinutes = avgTimeMinutes / currentLogs.size();
          avgTimehours = avgTimeMinutes / 60;
          avgTimeMinutes = (avgTimeMinutes % 60);
          table.addCell(avgTimehours + "h " + avgTimeMinutes + "min");

          table.addCell(String.valueOf(avgDistance / currentLogs.size()));
          table.addCell(String.valueOf(avgRating / currentLogs.size()));
        }
      }
      document.add(table);
      document.close();
      log.info("Summary PDF exported to: {}", file.getAbsolutePath());
    }
  }

  private void onChangeLanguage(String newLang, Stage stage) {
    if (newLang.equals(AppProperties.getInstance().getLocale().getLanguage()))
      return;

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
      log.error("Failed to reload view after language change", e);
    }
  }

  private void updateLanguageSelection(RadioMenuItem englishButton, RadioMenuItem germanButton,
      RadioMenuItem polishButton, String language) {
    // Reset all selections first
    englishButton.setSelected(false);
    germanButton.setSelected(false);
    polishButton.setSelected(false);

    // Set only the selected language
    switch (language) {
      case "en" -> englishButton.setSelected(true);
      case "de" -> germanButton.setSelected(true);
      case "pl" -> polishButton.setSelected(true);
    }
  }
}
