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

@Slf4j
public class RequestHandler {

    private static final RequestHandler INSTANCE = new RequestHandler();

    private final String BASE_URL_TOUR = AppProperties.getInstance().getTourApiUrl();
    private final String BASE_URL_LOG = AppProperties.getInstance().getLogApiUrl();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private RequestHandler() {}

    public static RequestHandler getInstance() {
        return INSTANCE;
    }

    public void postTour(Tour tour) {
        if (System.getProperty("app.test") != null) return;
        try {
            String json = mapper.writeValueAsString(tour);

            HttpRequest request = HttpRequest.newBuilder()
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
        } catch (Exception e) {
            log.error("Error while processing request: ", e);
        }
    }

    public void putTour(TourViewModel tvm) {
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

    public void deleteTour(UUID tourId) {
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

    public void loadTours(Consumer<List<Tour>> callback) {
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

    public void postLog(LogViewModel lvm) {
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

    public void putLog(LogViewModel lvm) {
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

    public void deleteLog(UUID logId) {
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

    public void loadLogs(Consumer<List<Logs>> callback) {
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