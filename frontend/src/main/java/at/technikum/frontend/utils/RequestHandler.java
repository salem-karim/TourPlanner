package at.technikum.frontend.utils;

import at.technikum.common.models.Tour;
import at.technikum.frontend.viewmodels.LogViewModel;
import at.technikum.frontend.viewmodels.TourViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

// Is responsible for sending the request to the backend via http
public class RequestHandler {

    private static final String BASE_URL = "http://localhost:8080/api/tours";
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
            // TODO: use try-with-resources to ensure the client is closed
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 201) {
                            System.out.println("Tour successfully saved to backend");
                        } else {
                            System.err.println("Failed to save tour: " + response.body());
                        }
                    });
        } catch (Exception e) {
          //TODO: use logger
            e.printStackTrace();
        }
    }

    //updates tour
    public static void putTour(TourViewModel tvm) {
        if (System.getProperty("app.test") != null) return;

        try {
            String json = mapper.writeValueAsString(tvm.toTour());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + tvm.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            System.out.println("Tour successfully updated on backend");
                        } else {
                            System.err.println("Failed to update tour: " + response.body());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteTour(UUID tourId) {
        if (System.getProperty("app.test") != null) return;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + tourId))
                    .DELETE()
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 204) {
                            System.out.println("Tour successfully deleted from backend");
                        } else {
                            System.err.println("Failed to delete tour: " + response.body());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------------LOGS--------------------------------


    public static void postLog(LogViewModel lvm) {
        if (System.getProperty("app.test") != null) return;

        try {
            String json = mapper.writeValueAsString(lvm.toLog());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/logs"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 201) {
                            System.out.println("Log saved to backend");
                        } else {
                            System.err.println("Failed to save log: " + response.body());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void putLog(LogViewModel lvm) {
        if (System.getProperty("app.test") != null) return;

        try {
            String json = mapper.writeValueAsString(lvm.toLog());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/logs/" + lvm.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            System.out.println("Log updated on backend");
                        } else {
                            System.err.println("Failed to update log: " + response.body());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteLog(UUID logId) {
        if (System.getProperty("app.test") != null) return;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/logs/" + logId))
                    .DELETE()
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 204) {
                            System.out.println("Log deleted from backend");
                        } else {
                            System.err.println("Failed to delete log: " + response.body());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

