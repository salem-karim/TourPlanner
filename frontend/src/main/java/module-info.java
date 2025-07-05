@SuppressWarnings("JavaModuleDefinition") module at.technikum.frontend {
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
  requires javafx.base;

  opens at.technikum.frontend to javafx.fxml;

  exports at.technikum.frontend;
  exports at.technikum.frontend.PL.controllers;
  exports at.technikum.frontend.PL.viewmodels;
  exports at.technikum.frontend.utils;
  exports at.technikum.frontend.BL.mediators;
  exports at.technikum.frontend.BL.services;

  opens at.technikum.frontend.PL.controllers to javafx.fxml;
  opens at.technikum.frontend.PL.viewmodels to javafx.fxml;
  opens at.technikum.frontend.utils to javafx.fxml;
  opens at.technikum.frontend.BL.mediators to javafx.fxml;
}
