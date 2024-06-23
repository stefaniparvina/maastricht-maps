package bus.maapstrichtfinal.Calculations;

import bus.maapstrichtfinal.ZipCode.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Distance extends Coordinates {
    private static final double EARTH_RADIUS = 6371;

    // Method to calculate the distance between two points using their coordinates
    public static double calculateDistance(String start, String end) {
        // Convert start and end coordinates to radians
        convertCoordinatesToRadians(start, end);
        // Calculate the differences in longitude and latitude
        double deltaX = (finishLongitudeRad - startLongitudeRad) * Math.cos((startLatitudeRad + finishLatitudeRad) / 2);
        double deltaY = (finishLatitudeRad - startLatitudeRad);

        // Calculate the distance using the Haversine formula
        double distanceKM = Math.sqrt(deltaX * deltaX + deltaY * deltaY) * EARTH_RADIUS;

        // Round the distance to two decimal places
        double roundedDistanceKM = Math.round(distanceKM * 100.0) / 100.0;

        return roundedDistanceKM; // Return the rounded distance in kilometers
    }

    public static double calculateDistanceFromZipToStop(String start, String stopId) {
        double zipStartY = new ZipCode(start).getLatitude();
        double zipStartX = new ZipCode(start).getLongitude();

        double zipEndY = 0.0;
        double zipEndX = 0.0;

        // Query to get latitude and longitude of the stopId
        String query = "SELECT stop_lat, stop_lon FROM stops WHERE stop_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, stopId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    zipEndY = rs.getDouble("stop_lat");
                    zipEndX = rs.getDouble("stop_lon");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return an error code or handle the exception as needed
        }

        // Converting latitude and longitude of start and end zip codes to radians
        double startLatitudeRad = Math.toRadians(zipStartY);
        double startLongitudeRad = Math.toRadians(zipStartX);
        double finishLatitudeRad = Math.toRadians(zipEndY);
        double finishLongitudeRad = Math.toRadians(zipEndX);

        // Calculate the differences in longitude and latitude
        double deltaX = (finishLongitudeRad - startLongitudeRad) * Math.cos((startLatitudeRad + finishLatitudeRad) / 2);
        double deltaY = (finishLatitudeRad - startLatitudeRad);

        // Calculate the distance using the Haversine formula
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY) * EARTH_RADIUS;

        // Round the distance to two decimal places
        distance = Math.round(distance * 100.0) / 100.0;

        return distance;
    }
}
