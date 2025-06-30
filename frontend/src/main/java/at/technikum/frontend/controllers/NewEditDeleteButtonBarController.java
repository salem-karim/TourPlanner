package at.technikum.frontend.controllers;

import at.technikum.frontend.mediators.LogButtonsMediator;
import at.technikum.frontend.mediators.Mediator;
import at.technikum.frontend.mediators.SelectionState;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

@Setter
@Getter
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
      new TourButtonsMediator(editButton, tourListView, Map.of(
              SelectionState.NO_SELECTION, false,
              SelectionState.ONE_SELECTED, true,
              SelectionState.MANY_SELECTED, false
      ));

      // For delete button
      new TourButtonsMediator(deleteButton, tourListView, Map.of(
              SelectionState.NO_SELECTION, false,
              SelectionState.ONE_SELECTED, true,
              SelectionState.MANY_SELECTED, true
      ));
    }
  }

  public void setLogTableView(TableView<LogViewModel> logTableView) {

    if (logTableView != null) {
      mediators.add(new LogButtonsMediator(editButton, logTableView, Map.of(
              SelectionState.NO_SELECTION, false,
              SelectionState.ONE_SELECTED, true,
              SelectionState.MANY_SELECTED, false
      )));

      mediators.add(new LogButtonsMediator(deleteButton, logTableView, Map.of(
              SelectionState.NO_SELECTION, false,
              SelectionState.ONE_SELECTED, true,
              SelectionState.MANY_SELECTED, true
      )));
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
