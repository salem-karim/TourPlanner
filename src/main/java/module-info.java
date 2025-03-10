module at.technikum.tourplanner {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.controlsfx.controls;

  opens at.technikum.tourplanner to javafx.fxml;
  exports at.technikum.tourplanner;
  exports at.technikum.tourplanner.models;
  opens at.technikum.tourplanner.models to javafx.fxml;
  exports at.technikum.tourplanner.controllers;
  opens at.technikum.tourplanner.controllers to javafx.fxml;
}