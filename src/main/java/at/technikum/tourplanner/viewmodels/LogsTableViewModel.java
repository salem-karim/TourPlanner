package at.technikum.tourplanner.viewmodels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

@Getter
public class LogsTableViewModel {
  private final ObjectProperty<LogsViewModel> selectedLog = new SimpleObjectProperty<>();
  private final ObservableList<LogsViewModel> data = FXCollections.observableArrayList();

  public ObservableList<String> getDataComments() {
    return FXCollections.observableArrayList(data.stream().map(LogsViewModel::getComment).toList());
  }

  public void newLog(LogsViewModel log) {
    data.add(log);
  }

  public void updateLog(LogsViewModel logViewModel) {
    for (final LogsViewModel log : data) {
      if (log.getId().equals(logViewModel.getId())) {
        log.dateTimeProperty().set(logViewModel.dateTimeProperty().get());
        log.commentProperty().set(logViewModel.commentProperty().get());
        log.difficultyProperty().set(logViewModel.difficultyProperty().get());
        log.totalDistanceProperty().set(logViewModel.totalDistanceProperty().get());
        log.totalTimeProperty().set(logViewModel.totalTimeProperty().get());
        log.ratingProperty().set(logViewModel.ratingProperty().get());
      }
    }
  }

  public void deleteLog(LogsViewModel logViewModel) {
    data.remove(logViewModel);
  }

  public void deleteLog(int index) {
    data.remove(index);
  }

  public void setSelectedLog(LogsViewModel log) {
    selectedLog.set(log);
  }
}