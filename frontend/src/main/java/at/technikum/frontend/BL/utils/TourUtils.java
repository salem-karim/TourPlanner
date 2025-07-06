package at.technikum.frontend.BL.utils;

import at.technikum.common.DAL.models.Logs;
import at.technikum.common.DAL.models.Tour;
import at.technikum.common.DAL.models.TransportType;
import at.technikum.frontend.PL.viewmodels.LogViewModel;
import at.technikum.frontend.PL.viewmodels.TourViewModel;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class TourUtils {

  private TourUtils() {
    throw new UnsupportedOperationException("Utility class");
  }

  /**
   * @param tour The Tour which the Logs get added to
   *             Creates Sample Logs and adds them to the tour VM
   */
  public static void addSampleLogs(final TourViewModel tour) {
    final List<LogViewModel> samples = List.of(
        createSampleLog("Great weather, enjoyed the hike!", 3, 8, 4),
        createSampleLog("Rainy day but still fun", 4, 8, 3));
    samples.forEach(log -> tour.getLogs().newLog(log));
  }

  /**
   * @param comment    The Comment of the Log
   * @param difficulty The Difficulty of the Log
   * @param distance   The Distance of the Log
   * @param rating     The Rating of the Log
   * @return the newly created log
   */
  public static LogViewModel createSampleLog(final String comment, final int difficulty, final int distance,
      final int rating) {
    final LogViewModel log = new LogViewModel();
    log.idProperty().set(UUID.randomUUID());
    log.startDateProperty().set(LocalDate.now());
    log.endDateProperty().set(LocalDate.now().plusDays(1));
    log.startTimeProperty().set(LocalTime.now());
    log.endTimeProperty().set(LocalTime.now().plusHours(2));
    log.commentProperty().set(comment);
    log.difficultyProperty().set(difficulty);
    log.totalDistanceProperty().set(distance);
    log.ratingProperty().set(rating);
    return log;
  }

  /**
   * @param logs   The List of Logs
   * @param tourId The ID of the Tour the logs searched for belong
   * @return a List of Logs which are of the Tour with the tourId
   */
  public static List<LogViewModel> filterLogsByTourId(final List<LogViewModel> logs, final UUID tourId) {
    return logs.stream()
        .filter(log -> tourId.equals(log.getTourId()))
        .collect(Collectors.toList());
  }

  /**
   * @param name           The Name of the Tour
   * @param description    The Description of the Tour
   * @param from           The Starting Location of the Tour
   * @param to             The Destination of the Tour
   * @param transport_type The Transport Type of the Tour
   * @return a newly created Tour
   */
  public static Tour createSampleTour(final String name, final String description, final String from, final String to,
      final TransportType transport_type) {
    return new Tour(UUID.randomUUID(), name, description, from, to, transport_type, 23, 45, new byte[0],
        new ArrayList<>());
  }

  /**
   * @param data The ObservableList the Tours should be loaded to
   */
  public static void loadAllTours(final ObservableList<TourViewModel> data) {
    // sets callback for the request handler to load tours
    RequestHandler.getInstance().loadTours(tourList -> {
      for (final Tour tour : tourList) {
        final TourViewModel tvm = new TourViewModel(tour);
        data.add(tvm);
      }
    });

    // Load logs for each tour and add to the TourViewModel
    final List<LogViewModel> all_logs = new ArrayList<>(List.of());
    RequestHandler.getInstance().loadLogs(logs -> {
      for (final Logs log : logs) {
        final LogViewModel lvm = new LogViewModel(log);
        all_logs.add(lvm);
      }
    });

    while (!all_logs.isEmpty()) {
      for (final TourViewModel tour : data) {
        for (final LogViewModel log : all_logs) {
          if (tour.getId().equals(log.getTourId())) {
            tour.getLogs().getData().add(log);
            all_logs.remove(log);
            break;
          }
        }
      }
    }
  }
}
