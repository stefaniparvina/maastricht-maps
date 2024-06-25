package bus.maapstrichtfinal.Calculations;

import javafx.geometry.Point2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BusRouteShape {
    private static final Logger LOGGER = Logger.getLogger(BusRouteShape.class.getName());


    // Method to find shape details for a given route
    public static int[] findShapeDetails(Route route) {
        int[] shapeDetails = new int[3];
        String query = "SELECT t.trip_id, r.route_id, t.shape_id, a.shape_dist_traveled as start_stop_distance, b.shape_dist_traveled as end_stop_distance," +
                " TIME_TO_SEC(b.arrival_time) - TIME_TO_SEC(a.departure_time) AS travel_time_seconds FROM trips t JOIN routes r ON t.route_id = r.route_id" +
                " AND r.route_id = ? JOIN stop_times a ON t.trip_id = a.trip_id JOIN stop_times b ON a.trip_id = b.trip_id" +
                " WHERE a.stop_id = ? AND b.stop_id = ? AND a.stop_sequence < b.stop_sequence ORDER BY travel_time_seconds ASC LIMIT 1";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, route.getRouteId());
            pstmt.setString(2, route.getStartStopId());
            pstmt.setString(3, route.getEndStopId());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    shapeDetails[0] = rs.getInt("shape_id");
                    shapeDetails[1] = rs.getInt("start_stop_distance");
                    shapeDetails[2] = rs.getInt("end_stop_distance");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding the shape details for a route.", e);
        }
        return shapeDetails;
    }

    // Method to find coordinates of all route points
    public static List<Point2D> findCoordsOfAllDirectRoutePoints(Route route) {
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
            LOGGER.log(Level.SEVERE, "Error in finding all coordinates for all route points.", e);
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
            LOGGER.log(Level.SEVERE, "Error in finding all coordinates for all route points.", e);
        }
        return allPointsCoords;
    }
}
