package at.technikum.frontend.utils;

import at.technikum.common.models.Logs;
import at.technikum.common.models.Tour;
import at.technikum.frontend.viewmodels.LogViewModel;
import at.technikum.frontend.viewmodels.TourViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

// Is responsible for sending the request to the backend via http
@Slf4j
public class RequestHandler {

  private static final String BASE_URL_TOUR = "http://localhost:8080/api/tours";
  private static final String BASE_URL_LOG = "http://localhost:8080/api/logs";
  private static final HttpClient httpClient = HttpClient.newHttpClient();
  // needed for serialization of LocalDateTime
  private static final ObjectMapper mapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  public static void postTour(Tour tour) {
    // Send POST to backend
    if (System.getProperty("app.test") != null) return;
    try {
      ObjectMapper mapper = new ObjectMapper(); // Jackson
      String json = mapper.writeValueAsString(tour);

      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create("http://localhost:8080/api/tours"))
              .header("Content-Type", "application/json")
              .POST(HttpRequest.BodyPublishers.ofString(json))
              .build();
      // TODO: use try-with-resources to ensure the client is closed -> nicht notwendig?
      httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
              .thenAccept(response -> {
                if (response.statusCode() == 201) {
                  log.info("Tour successfully saved to backend");
                } else {
                  log.error("Failed to save tour: {}", response.body());
                }
              });
    } catch (Exception e) {
      log.error("Error while processing request: ", e);
    }
  }

  //updates tour
  public static void putTour(TourViewModel tvm) {
    if (System.getProperty("app.test") != null) return;

    try {
      String json = mapper.writeValueAsString(tvm.toTour());

      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(BASE_URL_TOUR + "/" + tvm.getId()))
              .header("Content-Type", "application/json")
              .PUT(HttpRequest.BodyPublishers.ofString(json))
              .build();

      httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
              .thenAccept(response -> {
                if (response.statusCode() == 200) {
                  log.info("Tour successfully updated on backend");
                } else {
                  log.error("Failed to update tour: {}", response.body());
                }
              });
    } catch (Exception e) {
      log.error("Error while processing request: ", e);
    }
  }

  public static void deleteTour(UUID tourId) {
    if (System.getProperty("app.test") != null) return;

    try {
      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(BASE_URL_TOUR + "/" + tourId))
              .DELETE()
              .build();

      httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
              .thenAccept(response -> {
                if (response.statusCode() == 204) {
                  log.info("Tour successfully deleted from backend");
                } else {
                  log.error("Failed to delete tour: {}", response.body());
                }
              });
    } catch (Exception e) {
      log.error("Error while processing request: ", e);
    }
  }

  public static void loadTours(Consumer<List<Tour>> callback) {
    if (System.getProperty("app.test") != null) return;

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL_TOUR))
            .GET()
            .build();

    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(body -> {
              try {
                List<Tour> tours = Arrays.asList(mapper.readValue(body, Tour[].class));
                callback.accept(tours);
              } catch (Exception e) {
                log.error("Error while processing request: ", e);
              }
            });
  }

  // ----------------------LOGS--------------------------------


  public static void postLog(LogViewModel lvm) {
    if (System.getProperty("app.test") != null) return;

    try {
      String json = mapper.writeValueAsString(lvm.toLog());
      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(BASE_URL_LOG))
              .header("Content-Type", "application/json")
              .POST(HttpRequest.BodyPublishers.ofString(json))
              .build();

      httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
              .thenAccept(response -> {
                if (response.statusCode() == 201) {
                  log.info("Log saved to backend");
                } else {
                  log.error("Failed to save log: {}", response.body());
                }
              });
    } catch (Exception e) {
      log.error("Error while processing request: ", e);
    }
  }

  public static void putLog(LogViewModel lvm) {
    if (System.getProperty("app.test") != null) return;

    try {
      String json = mapper.writeValueAsString(lvm.toLog());
      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(BASE_URL_LOG + "/" + lvm.getId()))
              .header("Content-Type", "application/json")
              .PUT(HttpRequest.BodyPublishers.ofString(json))
              .build();

      httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
              .thenAccept(response -> {
                if (response.statusCode() == 200) {
                  log.info("Log updated on backend");
                } else {
                  log.error("Failed to update log: {}", response.body());
                }
              });
    } catch (Exception e) {
      log.error("Error while processing request: ", e);
    }
  }

  public static void deleteLog(UUID logId) {
    if (System.getProperty("app.test") != null) return;

    try {
      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(BASE_URL_LOG + "/" + logId))
              .DELETE()
              .build();

      httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
              .thenAccept(response -> {
                if (response.statusCode() == 204) {
                  log.info("Log deleted from backend");
                } else {
                  log.error("Failed to delete log: {}", response.body());
                }
              });
    } catch (Exception e) {
      log.error("Error while processing request: ", e);
    }
  }

  public static void loadLogs(Consumer<List<Logs>> callback) {
    if (System.getProperty("app.test") != null) return;

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL_LOG))
            .GET()
            .build();

    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(body -> {
              try {
                List<Logs> logs = Arrays.asList(mapper.readValue(body, Logs[].class));
                callback.accept(logs);
              } catch (Exception e) {
                log.error("Error while processing request: ", e);
              }
            });
  }

}

