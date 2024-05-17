package bus.maapstrichtfinal.ZipCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Reader {
    private static final String DATABASE = "C:\\Users\\stefa\\OneDrive\\Desktop\\David-fix\\project-1-2\\src\\main\\java\\bus\\maapstrichtfinal\\Map\\data\\MassZipLatLon.csv";
    private static HashMap<String, double[]> coordinatesCache = new HashMap<>();
    private static boolean isCacheLoaded = false; // Flag to check if the cache is loaded

    // Method to load coordinates into cache
    private static void loadCoordinatesCache() {
        if (isCacheLoaded) {
            return; // If already loaded, do nothing
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 3) {
                    double[] coordinates = new double[2];
                    coordinates[0] = Double.parseDouble(values[1]); // Latitude
                    coordinates[1] = Double.parseDouble(values[2]); // Longitude
                    String zipCode = values[0].trim().toLowerCase(); // Normalize zip code
                    coordinatesCache.put(zipCode, coordinates);
                }
            }
            isCacheLoaded = true; // Set flag to true after loading
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double[] readCoordinates(String zipCode) {
        if (!isCacheLoaded) {
            loadCoordinatesCache(); // Ensure cache is loaded before accessing
        }

        String normalizedZipCode = zipCode.trim().toLowerCase(); // Normalize zip code
        double[] coordinates = coordinatesCache.get(normalizedZipCode);
        if (coordinates != null) {
            System.out.println("Zip code found in database. No need for API: " + zipCode);
            return coordinates; // Return from cache if found
        } else {
            // Handle case when zip code is not found
            System.err.println("Zip code not found in database. Need to search with API: " + zipCode);
            
            Api api = Api.getInstance();
            try {
                coordinates = api.getCoordinatesByPostcode(zipCode);
                coordinatesCache.put(normalizedZipCode, coordinates);
                System.out.println("Found in the API: " + zipCode);
                return coordinates;
            } catch (Exception e) {
                System.out.println("AAAA");
                e.printStackTrace();
                return null;
            }
        }
    }
}
