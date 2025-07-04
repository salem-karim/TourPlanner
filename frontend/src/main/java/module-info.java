@SuppressWarnings("JavaModuleDefinition")
module at.technikum.frontend {
  requires transitive javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires org.slf4j;
  requires static lombok;

  requires org.controlsfx.controls;
  requires org.kordamp.ikonli.javafx;

  requires transitive at.technikum.common;
  requires java.net.http;
  requires com.fasterxml.jackson.datatype.jsr310;
  requires com.fasterxml.jackson.databind;
  requires jakarta.persistence;
  requires org.hibernate.orm.core;
  requires atlantafx.base;
    requires jdk.compiler;
    requires com.github.librepdf.openpdf;

    opens at.technikum.frontend to javafx.fxml;

  exports at.technikum.frontend;
  exports at.technikum.frontend.controllers;

  opens at.technikum.frontend.controllers to javafx.fxml;

  exports at.technikum.frontend.viewmodels;

  opens at.technikum.frontend.viewmodels to javafx.fxml;

  exports at.technikum.frontend.mediators;

  opens at.technikum.frontend.mediators to javafx.fxml;

  exports at.technikum.frontend.services;
  exports at.technikum.frontend.utils;
}
