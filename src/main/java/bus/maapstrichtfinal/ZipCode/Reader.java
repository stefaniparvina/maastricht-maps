package bus.maapstrichtfinal.ZipCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Reader {
    // Path to the CSV file containing zip code coordinates
    private static final String DATABASE = "src\\main\\java\\bus\\maapstrichtfinal\\Map\\data\\MassZipLatLon.csv";

    // HashMap to cache zip code coordinates
    private static HashMap<String, double[]> coordinatesCache = new HashMap<>();

    // Flag to check if the cache is loaded
    private static boolean isCacheLoaded = false;

    // Method to load coordinates into cache
    private static void loadCoordinatesCache() {
        if (isCacheLoaded) {
            return; // If already loaded, do nothing
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE))) {
            String line;
            reader.readLine(); // Skip the header line
            // Read each line of the CSV file
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 3) {
                    double[] coordinates = new double[2];
                    // Extract latitude and longitude from the CSV
                    coordinates[0] = Double.parseDouble(values[1]); // Latitude
                    coordinates[1] = Double.parseDouble(values[2]); // Longitude
                    String zipCode = values[0].trim().toLowerCase(); // Normalize zip code
                    coordinatesCache.put(zipCode, coordinates); // Store in cache
                }
            }
            isCacheLoaded = true; // Set flag to true after loading
        } catch (IOException e) {
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
