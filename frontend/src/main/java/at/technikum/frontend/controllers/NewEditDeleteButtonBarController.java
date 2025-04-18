package at.technikum.frontend.controllers;

import at.technikum.frontend.mediators.LogButtonsMediator;
import at.technikum.frontend.mediators.Mediator;
import at.technikum.frontend.mediators.TourButtonsMediator;
import at.technikum.frontend.viewmodels.LogViewModel;
import at.technikum.frontend.viewmodels.TourViewModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Setter
@NoArgsConstructor
public class NewEditDeleteButtonBarController implements Initializable {

  @FXML
  private ButtonBar buttonBar;
  @FXML
  private Button newButton;
  @FXML
  private Button editButton;
  @FXML
  private Button deleteButton;

  private EventHandler<ActionEvent> newButtonListener;
  private EventHandler<ActionEvent> editButtonListener;
  private EventHandler<ActionEvent> deleteButtonListener;

  private ArrayList<Mediator> mediators = new ArrayList<>();


  public void setTourListView(ListView<TourViewModel> tourListView) {

    if (tourListView != null) {
      mediators.add(new TourButtonsMediator(editButton, tourListView, new boolean[]{true, false, true}));
      mediators.add(new TourButtonsMediator(deleteButton, tourListView, new boolean[]{true, false, false}));
    }
  }

  public void setLogTableView(TableView<LogViewModel> logTableView) {

    if (logTableView != null) {
      mediators.add(new LogButtonsMediator(editButton, logTableView, new boolean[]{true, false, true}));
      mediators.add(new LogButtonsMediator(deleteButton, logTableView, new boolean[]{true, false, false}));
    }
  }

  public void onNew(ActionEvent event) {
    if (newButtonListener != null) {
      newButtonListener.handle(null);
    }
  }

  public void onEdit(ActionEvent event) {
    if (editButtonListener != null) {
      editButtonListener.handle(null);
    }
  }

  public void onDelete(ActionEvent event) {
    if (deleteButtonListener != null) {
      deleteButtonListener.handle(null);
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    buttonBar.getProperties().put("newEditDeleteButtonBarController", this);
    newButton.setOnAction(this::onNew);
    editButton.setOnAction(this::onEdit);
    deleteButton.setOnAction(this::onDelete);
  }
}
