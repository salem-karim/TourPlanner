@SuppressWarnings("Java9RedundantRequiresStatement")
module at.technikum.tourplanner {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires org.slf4j;
  requires static lombok;

  opens at.technikum.tourplanner to javafx.fxml;
  exports at.technikum.tourplanner;
  exports at.technikum.tourplanner.models;
  opens at.technikum.tourplanner.models to javafx.fxml;
  exports at.technikum.tourplanner.controllers;
  opens at.technikum.tourplanner.controllers to javafx.fxml;
  exports at.technikum.tourplanner.viewmodels;
  opens at.technikum.tourplanner.viewmodels to javafx.fxml;
  exports at.technikum.tourplanner.mediators;
  opens at.technikum.tourplanner.mediators to javafx.fxml;
  exports at.technikum.tourplanner.utils;
  opens at.technikum.tourplanner.utils to javafx.fxml;
  exports at.technikum.tourplanner.services;
}