package at.technikum.tourplanner.viewmodels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

@Getter
public class LogsTableViewModel {
  private final ObjectProperty<LogViewModel> selectedLog = new SimpleObjectProperty<>();
  private final ObservableList<LogViewModel> data = FXCollections.observableArrayList();

  public ObservableList<String> getDataComments() {
    return FXCollections.observableArrayList(data.stream().map(LogViewModel::getComment).toList());
  }

  public void newLog(LogViewModel log) {
    data.add(log);
  }

  public void updateLog(LogViewModel logViewModel) {
    for (final LogViewModel log : data) {
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

  public void deleteLog(LogViewModel logViewModel) {
    data.remove(logViewModel);
  }

  public void deleteLog(int index) {
    data.remove(index);
  }

  public void setSelectedLog(LogViewModel log) {
    selectedLog.set(log);
  }
}