module at.technikum.frontend {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires org.slf4j;
  requires static lombok;

  requires org.controlsfx.controls;
  requires org.kordamp.ikonli.javafx;

  requires at.technikum.common;
    requires java.net.http;
  requires com.fasterxml.jackson.databind;

  opens at.technikum.frontend to javafx.fxml;
  exports at.technikum.frontend ;
  exports at.technikum.frontend .controllers;
  opens at.technikum.frontend .controllers to javafx.fxml;
  exports at.technikum.frontend .viewmodels;
  opens at.technikum.frontend .viewmodels to javafx.fxml;
  exports at.technikum.frontend .mediators;
  opens at.technikum.frontend .mediators to javafx.fxml;
  exports at.technikum.frontend .services;
}