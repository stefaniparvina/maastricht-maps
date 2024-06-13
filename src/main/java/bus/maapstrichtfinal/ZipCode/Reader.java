package bus.maapstrichtfinal.ZipCode;

import bus.maapstrichtfinal.Calculations.DatabaseConnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Set;

public class Reader {
    // HashMap to cache zip code coordinates
    private static HashMap<String, double[]> coordinatesCache = new HashMap<>();

    // Flag to check if the cache is loaded
    private static boolean isCacheLoaded = false;

    // Method to load coordinates into cache
    public static void loadCoordinatesCache() {
        if (isCacheLoaded) {
            return; // If already loaded, do nothing
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT Zip, Lat, Lon FROM zipcodes");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String zipCode = resultSet.getString("Zip").trim().toLowerCase(); // Normalize zip code
                double latitude = resultSet.getDouble("Lat");
                double longitude = resultSet.getDouble("Lon");
                double[] coordinates = {latitude, longitude};
                coordinatesCache.put(zipCode, coordinates); // Store in cache
            }

            isCacheLoaded = true; // Set flag to true after loading
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to read coordinates for a given zip code
    public static double[] readCoordinates(String zipCode) {
        if (!isCacheLoaded) {
            loadCoordinatesCache(); // Ensure cache is loaded before accessing
        }

        String normalizedZipCode = zipCode.trim().toLowerCase(); // Normalize zip code
        double[] coordinates = coordinatesCache.get(normalizedZipCode); // Try to retrieve from cache
        if (coordinates != null) {
            System.out.println("Zip code found in database. No need for API: " + zipCode);
            return coordinates; // Return from cache if found
        } else {
            // Handle case when zip code is not found in cache
            System.err.println("Zip code not found in database. Need to search with API: " + zipCode);

            // Call the API to retrieve coordinates
            Api api = Api.getInstance();
            try {
                coordinates = api.getCoordinatesByPostcode(zipCode); // Retrieve coordinates using API
                coordinatesCache.put(normalizedZipCode, coordinates); // Store in cache
                System.out.println("Found in the API: " + zipCode);
                return coordinates;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
