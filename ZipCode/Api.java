package ZipCode;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Api {

    private static long lastRequestTime = -1;
    private static int requestCount = 0;

    private static boolean rateLimit() {
        long currentTime = System.currentTimeMillis();
        if (lastRequestTime == -1 || (currentTime - lastRequestTime) > 5000) { // reset every 5 seconds
            lastRequestTime = currentTime;
            requestCount = 1;
            return false; // no need to wait
        } else {
            if (requestCount < 1) { // only 1 request per 5 seconds allowed
                requestCount++;
                return false; // no need to wait
            } else {
                return true; // rate limit exceeded, must wait
            }
        }
    }

    public static double[] apiResponse(String postcode) {
        String responseBody;

        // Rate limiting check
        if (rateLimit()) {
            System.out.println("Rate limit exceeded. Please wait before making another request.");
            return new double[]{-1, -1};
        }

        String apiUrl = "https://computerscience.dacs.unimaas.nl/get_coordinates";
        String inputString = "{\"postcode\":\"" + postcode + "\"}";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Write the JSON data to the request
            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = inputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            // Read the response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = responseReader.readLine()) != null) {
                    response.append(inputLine);
                }
                responseReader.close();
                responseBody = response.toString();

            } else {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String errorLine;
                StringBuilder errorResponse = new StringBuilder();

                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                errorReader.close();

                responseBody = errorResponse.toString();
            }

            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

            if (jsonObject.has("error")) {
                String errorMessage = jsonObject.get("error").getAsString();
                System.out.println("Error: " + errorMessage);
                return new double[]{-1, -1};
            } else {
                double latitude = jsonObject.get("latitude").getAsDouble();
                double longitude = jsonObject.get("longitude").getAsDouble();

                return new double[]{latitude, longitude};
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("API error.");
            return new double[]{-1, -1};
        }
    }
}