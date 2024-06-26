package bus.maapstrichtfinal.Calculations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ClosestStopFinder {

    // Method to find the closest bus stops to a given location
    public static List<String> findClosestStops(double userLat, double userLon, double radius) throws SQLException {
        // List to store the closest bus stops
        List<String> closestStops = new ArrayList<>();

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
                double distance = ClosestStopFinderAstar.calculateDistance(userLat, userLon, stopLat, stopLon);

                // If the distance is within the maximum distance, add the bus stop to the list of closest stops
                if (distance <= radius) {
                    closestStops.add(rs.getString("stop_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return closestStops;
    }
}
