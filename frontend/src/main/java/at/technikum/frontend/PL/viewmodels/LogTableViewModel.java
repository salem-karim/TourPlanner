package at.technikum.frontend.PL.viewmodels;

import java.util.List;

import at.technikum.common.DAL.models.Logs;
import at.technikum.frontend.BL.utils.RequestHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

@Getter
public class LogTableViewModel {
  private final ObjectProperty<LogViewModel> selectedLog = new SimpleObjectProperty<>();
  private final ObservableList<LogViewModel> data = FXCollections.observableArrayList();

  public LogTableViewModel(final List<Logs> logs) {
    for (final Logs log : logs) {
      final LogViewModel logViewModel = new LogViewModel(log);
      data.add(logViewModel);
    }
  }

  public ObservableList<String> getDataComments() {
    return FXCollections.observableArrayList(data.stream().map(LogViewModel::getComment).toList());
  }

  public void newLog(final LogViewModel log) {
    data.add(log);
    RequestHandler.getInstance().postLog(log);
  }

  public void updateLog(final LogViewModel otherViewModel, final LogViewModel logViewModel) {
    logViewModel.setComment(otherViewModel.getComment());
    logViewModel.setDifficulty(otherViewModel.getDifficulty());
    logViewModel.setTotalDistance(otherViewModel.getTotalDistance());
    logViewModel.setRating(otherViewModel.getRating());
    logViewModel.setStartDate(otherViewModel.getStartDate());
    logViewModel.setEndDate(otherViewModel.getEndDate());
    logViewModel.setStartTime(otherViewModel.getStartTime());
    logViewModel.setEndTime(otherViewModel.getEndTime());

    RequestHandler.getInstance().putLog(logViewModel);
  }

  public void deleteLog(final LogViewModel logViewModel) {
    data.remove(logViewModel);
    RequestHandler.getInstance().deleteLog(logViewModel.getId());
  }

  public void deleteLog(final int index) {
    final LogViewModel removed = data.remove(index);
    if (removed != null) {
      RequestHandler.getInstance().deleteLog(removed.getId());
    }
  }

  public void setSelectedLog(final LogViewModel log) {
    selectedLog.set(log);
  }
}
