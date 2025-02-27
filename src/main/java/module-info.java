module at.technikum.tourplanner {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.controlsfx.controls;
  requires org.kordamp.bootstrapfx.core;

  opens at.technikum.tourplanner to javafx.fxml;
  exports at.technikum.tourplanner;
}