package at.technikum.frontend.BL.utils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import at.technikum.common.DAL.models.Logs;
import at.technikum.common.DAL.models.Tour;
import at.technikum.common.DAL.models.TransportType;
import at.technikum.frontend.PL.viewmodels.LogViewModel;
import at.technikum.frontend.PL.viewmodels.TourViewModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestHandler {

  private static final RequestHandler INSTANCE = new RequestHandler();

  private final String BASE_URL_TOUR = AppProperties.getInstance().getTourApiUrl();
  private final String BASE_URL_LOG = AppProperties.getInstance().getLogApiUrl();
  private final String BASE_URL_ORS = AppProperties.getInstance().getORSApiUrl();
  private final String ORS_API_KEY = AppProperties.getInstance().getORSApiKey();
  private final HttpClient httpClient = HttpClient.newHttpClient();
  private final ObjectMapper mapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  private RequestHandler() {
  }

  public static RequestHandler getInstance() {
    return INSTANCE;
  }

  /**
   * @param tour The Tour to send to the API Endpoint
   */
  public void postTour(final Tour tour) {
    if (System.getProperty("app.test") != null)
      return;
    try {
      final String json = mapper.writeValueAsString(tour);

      final HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(BASE_URL_TOUR))
              .header("Content-Type", "application/json")
              .POST(HttpRequest.BodyPublishers.ofString(json))
              .build();
      httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
              .thenAccept(response -> {
                if (response.statusCode() == 201) {
                  log.info("Tour successfully saved to backend");
                } else {
                  log.error("Failed to save tour: {}", response.body());
                }
              });
    } catch (final Exception e) {
      log.error("Error while processing request: ", e);
    }
  }

  /**
   * @param tvm The Tour View Model to send to the API Endpoint
   */
  public void putTour(final TourViewModel tvm) {
    if (System.getProperty("app.test") != null)
      return;

    try {
      final String json = mapper.writeValueAsString(tvm.toTour());

      final HttpRequest request = HttpRequest.newBuilder()
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
    } catch (final Exception e) {
      log.error("Error while processing request: ", e);
    }
  }

  /**
   * @param tourId The ID of the Tour to send to the API Endpoint
   */
  public void deleteTour(final UUID tourId) {
    if (System.getProperty("app.test") != null)
      return;

    try {
      final HttpRequest request = HttpRequest.newBuilder()
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
    } catch (final Exception e) {
      log.error("Error while processing request: ", e);
    }
  }

  /**
   * @param callback Method to run after the Response returns
   */
  public void loadTours(final Consumer<List<Tour>> callback) {
    if (System.getProperty("app.test") != null)
      return;

    final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL_TOUR))
            .GET()
            .build();

    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(body -> {
              try {
                final List<Tour> tours = Arrays.asList(mapper.readValue(body, Tour[].class));
                callback.accept(tours);
              } catch (final Exception e) {
                log.error("Error while processing request: ", e);
              }
            });
  }

  // ----------------------LOGS--------------------------------

  /**
   * @param lvm The Log View Model to send to the API Endpoint
   */
  public void postLog(final LogViewModel lvm) {
    if (System.getProperty("app.test") != null)
      return;

    try {
      final String json = mapper.writeValueAsString(lvm.toLog());
      final HttpRequest request = HttpRequest.newBuilder()
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
    } catch (final Exception e) {
      log.error("Error while processing request: ", e);
    }
  }

  /**
   * @param lvm The Log View Model to send to the API Endpoint
   */
  public void putLog(final LogViewModel lvm) {
    if (System.getProperty("app.test") != null)
      return;

    try {
      final String json = mapper.writeValueAsString(lvm.toLog());
      final HttpRequest request = HttpRequest.newBuilder()
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
    } catch (final Exception e) {
      log.error("Error while processing request: ", e);
    }
  }

  /**
   * @param logId The ID of the Log to send to the API Endpoint
   */
  public void deleteLog(final UUID logId) {
    if (System.getProperty("app.test") != null)
      return;

    try {
      final HttpRequest request = HttpRequest.newBuilder()
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
    } catch (final Exception e) {
      log.error("Error while processing request: ", e);
    }
  }

  /**
   * @param callback Method to run after the Response returns
   */
  public void loadLogs(final Consumer<List<Logs>> callback) {
    if (System.getProperty("app.test") != null)
      return;

    final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL_LOG))
            .GET()
            .build();

    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(body -> {
              try {
                final List<Logs> logs = Arrays.asList(mapper.readValue(body, Logs[].class));
                callback.accept(logs);
              } catch (final Exception e) {
                log.error("Error while processing request: ", e);
              }
            });
  }

  /**
   * @param location The location to get the Coordinates of
   * @return an Optional Object of an 2 sized Double Array Gets Coordinates from a Location String using ORS' geocode
   * endpoint
   */
  public Optional<double[]> getCoordinates(final String location) {
    final String url = BASE_URL_ORS + "/geocode/search?api_key=" + ORS_API_KEY + "&text="
            + URLEncoder.encode(location, StandardCharsets.UTF_8);
    final var req = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();
    try {
      final var res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
      final JsonNode root = mapper.readTree(res.body());
      final JsonNode features = root.get("features");

      if (features != null && features.isArray() && !features.isEmpty()) {
        final JsonNode coordinates = features.get(0).get("geometry").get("coordinates");
        return Optional.of(new double[]{coordinates.get(0).asDouble(), coordinates.get(1).asDouble()});
      }
    } catch (final Exception e) {
      log.error("Could not find coordinates of Location: {}", location, e.getMessage());
    }
    return Optional.empty();
  }

  /**
   * @param fromCoordsOpt The starting coordinates of the route as an Optional
   * @param toCoordsOpt   The destination coordinates of the route as an Optional
   * @param profile_type  The transport Type of the rout (CAR, BIKE, FOOT)
   * @return A {@link JsonNode} representing the rout response of the API or null if routing failed or no route was
   * found.
   */
  public JsonNode RouteBetween(final Optional<double[]> fromCoordsOpt, final Optional<double[]> toCoordsOpt,
                               final TransportType profile_type) {
    final Map<TransportType, String> profileMap = Map.of(
            TransportType.CAR, "driving-car",
            TransportType.BIKE, "cycling-regular",
            TransportType.FOOT, "foot-walking");
    final var profile = profileMap.get(profile_type);

    if (fromCoordsOpt.isEmpty() || toCoordsOpt.isEmpty())
      return null;

    final double[] fromCoords = fromCoordsOpt.get();
    final double[] toCoords = toCoordsOpt.get();

    try {
      final String body = String.format(Locale.US, """
              {
                "coordinates": [[%f, %f], [%f, %f]]
              }
              """, fromCoords[0], fromCoords[1], toCoords[0], toCoords[1]);

      final var req = HttpRequest.newBuilder()
              .uri(URI.create(BASE_URL_ORS + "/v2/directions/" + profile))
              .header("Authorization", ORS_API_KEY)
              .header("Content-Type", "application/json")
              .POST(HttpRequest.BodyPublishers.ofString(body))
              .build();

      final var res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

      if (res.statusCode() == 200) {
        final JsonNode json = mapper.readTree(res.body());
        if (json.has("routes") && !json.get("routes").isEmpty())
          return json;
      }
    } catch (final Exception e) {
      log.error("Routing failed for profile: {}", profile, e);
    }
    return null;
  }

  public Optional<RouteInfo> parseRouteInfo(JsonNode json) {
    if (json != null && json.has("routes") && !json.get("routes").isEmpty()) {
      JsonNode summary = json.get("routes").get(0).get("summary");
      if (summary != null && summary.has("distance") && summary.has("duration")) {
        double distance = summary.get("distance").asDouble() / 1000.0; // in km
        double duration = summary.get("duration").asDouble() / 60.0; // in minutes
        return Optional.of(new RouteInfo(distance, duration));
      }
    }
    return Optional.empty();
  }
}
