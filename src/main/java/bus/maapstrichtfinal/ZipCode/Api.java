package bus.maapstrichtfinal.ZipCode;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Api {
    private static Api instance;
    private final HttpClient httpClient;
    private final Gson gson;

    // Private constructor to prevent external instantiation
    private Api() {

        // Initialize HTTP client with a 10-second timeout
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        gson = new Gson();
    }

    // Method to get the singleton instance of Api
    public static synchronized Api getInstance() {
        if (instance == null) {
            instance = new Api();
        }
        return instance;
    }

    // Method to get coordinates by postcode
    public double[] getCoordinatesByPostcode(String postcode) throws Exception {

        // Base URL of the API
        String baseUrl = "https://webmomentum.net/get_coordinates/";

        // Constructing the URI for the request
        URI uri = URI.create(baseUrl + "?postcode=" + postcode);

        // JSON body containing the postcode
        String json = "{ \"postcode\": \"" + postcode + "\" }";

        // Constructing a POST request
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // Sending the request and getting the response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Handling response status codes
        return switch (response.statusCode()) {
            case 200 -> parseCoordinates(response.body()); // OK
            case 429 -> throw new RuntimeException("Rate limit exceeded, try again later."); // Too Many Requests
            case 404 -> throw new RuntimeException("Postcode not found."); // Not Found
            case 500 -> throw new RuntimeException("Failed to fetch coordinates."); // Internal Server Error
            default -> throw new RuntimeException("Unexpected API error: " + response.statusCode());
        };
    }

    // Method to parse the coordinates from JSON response
    private double[] parseCoordinates(String responseBody) {
        JsonObject json = gson.fromJson(responseBody, JsonObject.class);
        double latitude = json.get("latitude").getAsDouble();
        double longitude = json.get("longitude").getAsDouble();
        return new double[] {latitude, longitude};
    }
}
