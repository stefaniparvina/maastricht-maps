package bus.maapstrichtfinal.ZipCode;

import bus.maapstrichtfinal.Calculations.DatabaseConnection;
import bus.maapstrichtfinal.MapController;

import java.sql.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reader {

    // HashMap to cache zip code coordinates
    private static final HashMap<String, double[]> coordinatesCache = new HashMap<>();

    // Flag to check if the cache is loaded
    private static boolean isCacheLoaded = false;
    private static final Logger LOGGER = Logger.getLogger(Reader.class.getName());


    // Method to load coordinates into cache
    private static void loadCoordinatesCache() {
        if (isCacheLoaded) {
            return; // If already loaded, do nothing
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            assert connection != null;
            String QUERY = "SELECT Zip, Lat, Lon FROM zipcodes";
            try (PreparedStatement statement = connection.prepareStatement(QUERY);
                 ResultSet resultSet = statement.executeQuery()) {

                // Read each row from the ResultSet
                while (resultSet.next()) {
                    String zipCode = resultSet.getString("Zip").trim().toLowerCase(); // Normalize zip code
                    double lat = resultSet.getDouble("Lat");
                    double lon = resultSet.getDouble("Lon");
                    double[] coordinates = {lat, lon};

                    // Store in cache
                    coordinatesCache.put(zipCode, coordinates);
                }
                isCacheLoaded = true; // Set flag to true after loading

            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading coordinates cache.", e);
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
                LOGGER.log(Level.SEVERE, "Error reading coordinates.", e);
                return null;
            }
        }
    }
}
