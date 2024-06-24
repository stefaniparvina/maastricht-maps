package bus.maapstrichtfinal.Calculations;

import javafx.geometry.Point2D;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BusRouteShape {

    // Method to find shape details for a given route
    public static int[] findShapeDetails(Route route) {
        // Array to store shape details: shape_id, start_stop_distance, end_stop_distance
        int[] shapeDetails = new int[3];

        // SQL query to find the shape details for the fastest trip on the chosen route
        String query = "SELECT t.trip_id, r.route_id, t.shape_id, a.shape_dist_traveled as start_stop_distance, b.shape_dist_traveled as end_stop_distance," +
                " TIME_TO_SEC(b.arrival_time) - TIME_TO_SEC(a.departure_time) AS travel_time_seconds FROM trips t JOIN routes r ON t.route_id = r.route_id" +
                " AND r.route_id = ? JOIN stop_times a ON t.trip_id = a.trip_id JOIN stop_times b ON a.trip_id = b.trip_id" +
                " WHERE a.stop_id = ? AND b.stop_id = ? AND a.stop_sequence < b.stop_sequence ORDER BY travel_time_seconds ASC LIMIT 1";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameters for the query
            pstmt.setInt(1, route.getRouteId());
            pstmt.setString(2, route.getStartStopId());
            pstmt.setString(3, route.getEndStopId());

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Store the retrieved shape details in the array
                    shapeDetails[0] = rs.getInt("shape_id");
                    shapeDetails[1] = rs.getInt("start_stop_distance");
                    shapeDetails[2] = rs.getInt("end_stop_distance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shapeDetails;
    }

    // Method to find coordinates of all route points
    public static List<Point2D> findCoordsOfAllRoutePoints(Route route) {
        List<Point2D> allPointsCoords = new ArrayList<>();
        int[] shapeDetails = findShapeDetails(route);

        String shapePointsQuery = "SELECT shape_pt_lat, shape_pt_lon, shape_pt_sequence FROM shapes WHERE shape_id = ?" +
                " AND shape_dist_traveled  BETWEEN ? AND ? ORDER BY shape_pt_sequence ASC;";

        String stopsQuery = "SELECT stop_id, stop_lat, stop_lon FROM stops WHERE stop_id IN (?, ?)";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(shapePointsQuery)) {

            pstmt.setInt(1, shapeDetails[0]);
            pstmt.setInt(2, shapeDetails[1]);
            pstmt.setInt(3, shapeDetails[2]);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    double lat = rs.getDouble("shape_pt_lat");
                    double lon = rs.getDouble("shape_pt_lon");
                    Point2D point = new Point2D(lat, lon);
                    allPointsCoords.add(point);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(stopsQuery)) {

            pstmt.setString(1, route.getStartStopId());
            pstmt.setString(2, route.getEndStopId());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    double lat = rs.getDouble("stop_lat");
                    double lon = rs.getDouble("stop_lon");
                    Point2D point = new Point2D(lat, lon);
                    if (rs.getString("stop_id").equals(route.getStartStopId())) {
                        allPointsCoords.add(0, point); // Add start stop at the beginning
                    } else {
                        allPointsCoords.add(point); // Add end stop at the end
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allPointsCoords;
    }
}
