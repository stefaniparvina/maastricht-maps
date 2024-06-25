package bus.maapstrichtfinal.Accessibility;

import bus.maapstrichtfinal.Calculations.ClosestStopFinder;
import bus.maapstrichtfinal.Calculations.DatabaseConnection;
import bus.maapstrichtfinal.ZipCode.ZipCode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    // Method to parse the geo json files and to list the coordinates of shops or tourist spots by postal code
    public static void countShopTourismByZip(String filePath, String csvFilePath) throws Exception {
        try (FileWriter writer = new FileWriter(csvFilePath)) {
            writer.write("PostalCode,Latitude,Longitude\n");

            // Read the content of the geojson file
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject geoJson = new JSONObject(content);
            JSONArray features = geoJson.getJSONArray("features");

            // Iterate through each feature (shop or tourism spot)
            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject geometry = feature.getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");
                double longitude = coordinates.getDouble(0);
                double latitude = coordinates.getDouble(1);

                // Call stored procedure to find the nearest postal code
                String postalCode = findNearestPostalCode(latitude, longitude);

                // Write entry to CSV
                writer.write(postalCode + "," + latitude + "," + longitude + "\n");
            }

            System.out.println("CSV file created successfully: " + csvFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Method to parse the geojson files and count types of amenities by postal code
        public static void countAmenitiesByPostalCode(String filePath, String schoolsCsvFilePath, String healthcareCsvFilePath) throws Exception {

        // Read the content of the geojson file
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject geoJson = new JSONObject(content);
        JSONArray features = geoJson.getJSONArray("features");

        // Initialize FileWriters for schools and healthcare CSV files
        try (FileWriter schoolWriter = new FileWriter(schoolsCsvFilePath);
             FileWriter healthcareWriter = new FileWriter(healthcareCsvFilePath)) {
            schoolWriter.write("PostalCode,Latitude,Longitude\n");
            healthcareWriter.write("PostalCode,Latitude,Longitude\n");

            // Iterate through each feature (amenity spot)
            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject geometry = feature.getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");
                double longitude = coordinates.getDouble(0);
                double latitude = coordinates.getDouble(1);

                // Extract amenity type information
                JSONObject properties = feature.getJSONObject("properties");
                String amenityType = properties.optString("amenity");

                // Determine if it's a school or healthcare facility
                boolean isSchool = "school".equalsIgnoreCase(amenityType) || "prep_school".equalsIgnoreCase(amenityType)
                        || "college".equalsIgnoreCase(amenityType);
                boolean isHealthcare = isHealthcareFacility(amenityType);

                // Call stored procedure to find the nearest postal code
                String postalCode = findNearestPostalCode(latitude, longitude);

                // Write to appropriate CSV files
                if (isSchool) {
                    schoolWriter.write(postalCode + "," + latitude + "," + longitude + "\n");
                }
                if (isHealthcare) {
                    healthcareWriter.write(postalCode + "," + latitude + "," + longitude + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("CSV files created successfully: " + schoolsCsvFilePath + ", " + healthcareCsvFilePath);
    }

    // Method to parse multiple geojson files and count wheelchair accessible locations by postal code
    public static void countWheelchairAccessibleByPostalCode(List<String> filePaths, String csvFilePath) throws Exception {
        Map<String, Integer> wheelchairCountByPostalCode = new HashMap<>();

        for (String filePath : filePaths) {
            // Read the content of the geojson file
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject geoJson = new JSONObject(content);
            JSONArray features = geoJson.getJSONArray("features");

            // Iterate through each feature (location)
            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject geometry = feature.getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");
                double longitude = coordinates.getDouble(0);
                double latitude = coordinates.getDouble(1);

                // Extract wheelchair accessibility information
                JSONObject properties = feature.getJSONObject("properties");
                String wheelchair = properties.optString("wheelchair");

                // Check if the location is wheelchair accessible
                if ("yes".equalsIgnoreCase(wheelchair)) {
                    // Call stored procedure to find the nearest postal code
                    String postalCode = findNearestPostalCode(latitude, longitude);

                    // Update the wheelchair accessible count for the postal code
                    wheelchairCountByPostalCode.put(postalCode, wheelchairCountByPostalCode.getOrDefault(postalCode, 0) + 1);
                }
            }
        }

        // Write the results to the CSV file
        try (FileWriter writer = new FileWriter(csvFilePath)) {
            writer.write("PostalCode,WheelchairAccessibleCount\n");
            for (Map.Entry<String, Integer> entry : wheelchairCountByPostalCode.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        }

        System.out.println("CSV file created successfully: " + csvFilePath);
    }

    // Method to call the stored procedure and find the nearest postal code for a given latitude and longitude
    private static String findNearestPostalCode(double latitude, double longitude) {
        String nearestPostalCode = null;

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "CALL find_nearest_zipcode(?, ?)";
            assert connection != null;
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setDouble(1, latitude);
                statement.setDouble(2, longitude);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        nearestPostalCode = resultSet.getString("Zip");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nearestPostalCode;
    }

    private static Map<String, Integer> buildZipCodeMap() {
        Map<String, Integer> zipCodeMap = new HashMap<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT Zip FROM zipcodes";
            assert connection != null;
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String zipCode = resultSet.getString("Zip");
                    zipCodeMap.put(zipCode, countBusStopsByZIP(zipCode));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return zipCodeMap;
    }

    private static int countBusStopsByZIP(String zipcode) throws SQLException {
        ZipCode zip = new ZipCode(zipcode);
        List<String> closestStops = ClosestStopFinder.findClosestStops(zip.getLatitude(), zip.getLongitude(), 0.5);
        return closestStops.size();
    }

    // Method to check if the amenity type is a healthcare facility
    private static boolean isHealthcareFacility(String amenityType) {
        return amenityType != null && (
                amenityType.equalsIgnoreCase("doctor") ||
                        amenityType.equalsIgnoreCase("hospital") ||
                        amenityType.equalsIgnoreCase("pharmacy") ||
                        amenityType.equalsIgnoreCase("clinic") ||
                        amenityType.equalsIgnoreCase("dentist")
        );
    }

    //Sum of the values of a given column of a csv file, for testing purposes
    public static double sumColumnValues(String filePath, int columnIndex) throws IOException {
        double sum = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (columnIndex >= 0 && columnIndex < values.length) {
                    try {
                        double value = Double.parseDouble(values[columnIndex].trim());
                        sum += value;
                    } catch (NumberFormatException e) {
                        // If the value in the column is not a valid number, you may want to handle or skip it
                        System.err.println("Skipping non-numeric value: " + values[columnIndex]);
                    }
                } else {
                    System.err.println("Column index " + columnIndex + " out of bounds for line: " + line);
                }
            }
        }

        return sum;
    }

    public static void main(String[] args) {
        try {
            Map<String, Integer> zipCodeMap = buildZipCodeMap();
            String shopFilePath = "geojson/shop.geojson";
            String tourismFilePath = "geojson/tourism.geojson";
            String amenityFilePath = "geojson/amenity.geojson";
            List<String> filePaths = List.of(
                    amenityFilePath,
                    tourismFilePath,
                    shopFilePath
            );

            countShopTourismByZip(shopFilePath,"src/main/java/bus/maapstrichtfinal/Accessibility/data/shops_data.csv");
            countShopTourismByZip(tourismFilePath,"src/main/java/bus/maapstrichtfinal/Accessibility/data/tourism_data.csv");
            countAmenitiesByPostalCode(amenityFilePath,"src/main/java/bus/maapstrichtfinal/Accessibility/data/schools_data.csv"
            ,"src/main/java/bus/maapstrichtfinal/Accessibility/data/healthcare_data.csv");
            countWheelchairAccessibleByPostalCode(filePaths,"src/main/java/bus/maapstrichtfinal/Accessibility/data/wheelchair_data.csv" );

            System.out.println(sumColumnValues("src/main/java/bus/maapstrichtfinal/Accessibility/data/shops_data.csv",1));
            System.out.println(sumColumnValues("src/main/java/bus/maapstrichtfinal/Accessibility/data/shops_data2.csv",1));

            System.out.println(sumColumnValues("src/main/java/bus/maapstrichtfinal/Accessibility/data/amenity_data2.csv",2));
            System.out.println(sumColumnValues("src/main/java/bus/maapstrichtfinal/Accessibility/data/wheelchair_data.csv",1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}