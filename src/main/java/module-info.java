module com.tourplanner.tourplanner {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.controlsfx.controls;
  requires org.kordamp.bootstrapfx.core;

  opens com.tourplanner.tourplanner to javafx.fxml;
  exports com.tourplanner.tourplanner;
}