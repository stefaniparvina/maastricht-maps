package bus.maapstrichtfinal.Calculations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ClosestStopFinder {

    // Method to find the closest bus stops to a given location
    public static List<String> findStops300m(double userLat, double userLon) throws SQLException {
        // List to store the closest bus stops
        List<String> closestStops = new ArrayList<>();
        // Maximum distance to search within
        double maxDistance = 0.3; // in kilometers

        // SQL query to retrieve all bus stops
        String query = "SELECT stop_id, stop_lat, stop_lon FROM stops";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            // Iterate over the result set of bus stops
            while (rs.next()) {
                double stopLat = rs.getDouble("stop_lat");
                double stopLon = rs.getDouble("stop_lon");
                // Calculate the distance between the user's location and the bus stop
                double distance = calculateDistance(userLat, userLon, stopLat, stopLon);

                // If the distance is within the maximum distance, add the bus stop to the list of closest stops
                if (distance <= maxDistance) {
                    closestStops.add(rs.getString("stop_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return closestStops;
    }

    public static List<String> findStops500m(double userLat, double userLon) throws SQLException {
        // List to store the closest bus stops
        List<String> closestStops = new ArrayList<>();
        // Maximum distance to search within
        double maxDistance = 0.5; // in kilometers

        // SQL query to retrieve all bus stops
        String query = "SELECT stop_id, stop_lat, stop_lon FROM stops";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            // Iterate over the result set of bus stops
            while (rs.next()) {
                double stopLat = rs.getDouble("stop_lat");
                double stopLon = rs.getDouble("stop_lon");
                // Calculate the distance between the user's location and the bus stop
                double distance = calculateDistance(userLat, userLon, stopLat, stopLon);

                // If the distance is within the maximum distance, add the bus stop to the list of closest stops
                if (distance <= maxDistance) {
                    closestStops.add(rs.getString("stop_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return closestStops;
    }

    // Method to calculate the distance between two geographical coordinates using the Haversine formula
    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // Earth's radius in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c; // Distance in kilometers
    }
}
