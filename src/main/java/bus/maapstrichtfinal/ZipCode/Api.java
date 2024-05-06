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

    private Api() {
        httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
        gson = new Gson();
    }
    //Making sure there will be only one instance of this class
    public static synchronized Api getInstance() {
        if (instance == null) {
            instance = new Api();
        }
        return instance;
    }

    public double[] getCoordinatesByPostcode(String postcode) throws Exception {

        //private final String baseUrl = "https://computerscience.dacs.unimaas.nl/get_coordinates";
        String baseUrl = "https://webmomentum.net/get_coordinates/";

        URI uri = URI.create(baseUrl + "?postcode=" + postcode);
    
        // JSON body containing the postcode
        String json = "{ \"postcode\": \"" + postcode + "\" }";
    
        // Doing a POST request
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    
        // Sending the request and getting the response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    
        // Handling response status codes
        return switch (response.statusCode()) {
            case 200 -> parseCoordinates(response.body());
            case 429 -> throw new RuntimeException("Rate limit exceeded, try again later.");
            case 404 -> throw new RuntimeException("Postcode not found.");
            case 500 -> throw new RuntimeException("Failed to fetch coordinates.");
            default -> throw new RuntimeException("Unexpected API error: " + response.statusCode());
        };
    }

    private double[] parseCoordinates(String responseBody) {
        JsonObject json = gson.fromJson(responseBody, JsonObject.class);
        double latitude = json.get("latitude").getAsDouble();
        double longitude = json.get("longitude").getAsDouble();
        return new double[] {latitude, longitude};
    }
}