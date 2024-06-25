package bus.maapstrichtfinal.Calculations;

import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import bus.maapstrichtfinal.ZipCode.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Distance extends Coordinates {
    private static final Logger LOGGER = Logger.getLogger(Distance.class.getName());

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
        return Math.round(distanceKM * 100.0) / 100.0; // Return the rounded distance in kilometers
    }

    //Method overloading of calculateDistance
    private static double calculateDistance(double zipStartY, double zipStartX) {
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

    //Method to calculate walking distance from a zipcode to a bus stop
    public static double calculateDistanceFromZipToStop(String stopId) {
        double zipStartY = DestinationInfo.getStartZipCode().getLatitude();
        double zipStartX = DestinationInfo.getStartZipCode().getLongitude();


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
            LOGGER.log(Level.SEVERE, "SQL Exception in calculateDistanceFromStartZipToStop", e);
            return -1; // Return an error code or handle the exception as needed
        }

        return calculateDistance(zipStartY, zipStartX);
    }
}
