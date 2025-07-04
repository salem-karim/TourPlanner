package at.technikum.frontend.controllers;

import at.technikum.common.models.Logs;
import at.technikum.common.models.Tour;
import at.technikum.frontend.utils.AppProperties;
import at.technikum.frontend.viewmodels.LogTableViewModel;
import at.technikum.frontend.viewmodels.LogViewModel;
import at.technikum.frontend.viewmodels.TourViewModel;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

//pdf import
import com.lowagie.text.pdf.PdfWriter;
import java.util.List;

//json import
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

import java.io.FileOutputStream;

import com.lowagie.text.pdf.PdfPTable;

//todo: add logging ???

public class NavbarController {
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

        System.out.println(importedTour.getName()); // Print the imported tour name

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
      mapper.writeValue(new File("json_files/" + tour.getName().replaceAll("\\s+", "-") + "-" + tour.getId() + ".json"),
          tour);
      System.out.println("Tour exported to JSON.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void onTourPDF() throws IOException {
    TourViewModel tvm = tourPlannerController.getSelectedTour();
    Tour tour = tvm.toTour();

    LogTableViewModel ltvm = tvm.getLogs();
    ObservableList<LogViewModel> logs = ltvm.getData();

    Document document = new Document();
    PdfWriter.getInstance(document, new FileOutputStream("pdf_files/" + tour.getName() + "_report.pdf"));
    document.open();

    // Add tour details
    document.add(new Paragraph("Tour Report"));
    document.add(new Paragraph("Tour: " + tour.getName()));
    document.add(new Paragraph("Description: " + tour.getDescription()));
    document.add(new Paragraph("From: " + tour.getFrom()));
    document.add(new Paragraph("To: " + tour.getTo()));
    document.add(new Paragraph("Transport Type: " + tour.getTransport_type()));
    document.add(new Paragraph("Total Distance: " + tour.getTotal_distance() + " km"));
    document.add(new Paragraph("Estimated Time: " + tour.getEstimated_time() + " minutes"));
    document.add(new Paragraph(" ")); // empty line

    // Create table for logs
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

    document.add(table);

    // todo image is still missing

    document.close();
  }

  public void onSummarizePDF() throws FileNotFoundException {

    List<TourViewModel> tours = tourPlannerController.getAllTours();

    Document document = new Document();
    PdfWriter.getInstance(document, new FileOutputStream("pdf_files/tours_summary.pdf"));
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
        table.addCell(String.valueOf(avgTimehours) + "h " + String.valueOf(avgTimeMinutes) + "min");

        table.addCell(String.valueOf(avgDistance / currentLogs.size()));
        table.addCell(String.valueOf(avgRating / currentLogs.size()));
      }
    }
    document.add(table);
    document.close();
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
      // log.error("Failed to reload view after language change", e);
    }
  }
}
