package at.technikum.frontend.viewmodels;

import at.technikum.frontend.utils.RequestHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class LogTableViewModel {
  private final ObjectProperty<LogViewModel> selectedLog = new SimpleObjectProperty<>();
  private final ObservableList<LogViewModel> data = FXCollections.observableArrayList();

  public ObservableList<String> getDataComments() {
    return FXCollections.observableArrayList(data.stream().map(LogViewModel::getComment).toList());
  }

  public void newLog(LogViewModel log) {
    data.add(log);
    RequestHandler.postLog(log);
  }

  public void updateLog(final LogViewModel otherViewModel, final LogViewModel logViewModel) {
    logViewModel.setComment(otherViewModel.getComment());
    logViewModel.setDifficulty(otherViewModel.getDifficulty());
    logViewModel.setTotalDistance(otherViewModel.getTotalDistance());
    logViewModel.setRating(otherViewModel.getRating());
    logViewModel.setStartDateTime(otherViewModel.getStartDateTime());
    logViewModel.setEndDateTime(otherViewModel.getEndDateTime());

    RequestHandler.putLog(logViewModel);
  }


  public void deleteLog(LogViewModel logViewModel) {
    data.remove(logViewModel);
    RequestHandler.deleteLog(logViewModel.getId());
  }

  public void deleteLog(int index) {
    LogViewModel removed = data.remove(index);
    if (removed != null) {
      RequestHandler.deleteLog(removed.getId());
    }
  }


  public void setSelectedLog(LogViewModel log) {
    selectedLog.set(log);
  }
}