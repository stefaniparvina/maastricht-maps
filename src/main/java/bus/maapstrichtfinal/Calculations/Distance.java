package bus.maapstrichtfinal.Calculations;

import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
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

    public static double calculateDistanceFromStartZipToStop(String stopId) {
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
    public static double calculateDistanceFromEndZipToStop(String stopId) {
        double zipStartY = DestinationInfo.getEndZipCode().getLatitude();
        double zipStartX = DestinationInfo.getEndZipCode().getLongitude();


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

    public static double calculateDistance(String start, double lat, double lon) {
        ZipCode zipStart = new ZipCode(start);
        double zipStartY = Math.toRadians(zipStart.getLatitude());
        double zipStartX = Math.toRadians(zipStart.getLongitude());
        lat = Math.toRadians(lat);
        lon = Math.toRadians(lon);

        // Calculate the differences in longitude and latitude
        double deltaX = (lon - zipStartX) * Math.cos((zipStartY + lat) / 2);
        double deltaY = (lat - zipStartY);

        // Calculate the distance using the Haversine formula
        double a = Math.sin(deltaY / 2) * Math.sin(deltaY / 2) +
                Math.cos(zipStartY) * Math.cos(lat) *
                        Math.sin(deltaX / 2) * Math.sin(deltaX / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distanceKM = EARTH_RADIUS * c;

        // Round the distance to two decimal places
        double roundedDistanceKM = Math.round(distanceKM * 100.0) / 100.0;

        return roundedDistanceKM; // Return the rounded distance in kilometers
    }

}
