package ZipCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Api {
    
    public static double[] apiResponse(String postcode) {
        String responseBody;
        String apiUrl = "computescience.dacs.unimaas.nl/get_coordinates";
        String inputString = "{"+"\"postcode\":" + "\"" + postcode + "\"}"; // input string to send to the API
        try {
            URL url = new URL(apiUrl + "?postcode=" + inputString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
         
            int responseCode = conn.getResponseCode();
            //if the request was successful we read the response line by line and append it all in the response variable 
            if (responseCode == HttpURLConnection.HTTP_OK) { 
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder  response = new StringBuilder();

                while ((inputLine = responseReader.readLine()) != null) { //if it is null we have reached the end of the response
                    response.append(inputLine);
                }
                responseReader.close();
                responseBody = response.toString();

            } else { //if the request was not successful we read the response line by line and append it all in the errorResponse variable 
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String errorLine;
                StringBuilder errorResponse = new StringBuilder();

                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                errorReader.close();

                responseBody = errorResponse.toString();
            }

            // parse JSON response body
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject(); //make it a JSON object to extract data from it in a structured manner

            // check for errors
            if (jsonObject.has("error")) {
                String errorMessage = jsonObject.get("error").getAsString();
                System.out.println("Error: " + errorMessage);
                return null;
            } else {
                // the response must contains latitude and longitude
                double latitude = jsonObject.get("latitude").getAsDouble();
                double longitude = jsonObject.get("longitude").getAsDouble();

                return new double[]{latitude, longitude};
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null; 
        }
    }
}
