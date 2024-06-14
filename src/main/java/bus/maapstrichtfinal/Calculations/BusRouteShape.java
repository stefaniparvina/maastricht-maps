package bus.maapstrichtfinal.Calculations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BusRouteShape {

    // Method to find shape details for a given route
    public static int[] findShapeDetails(Route route) {
        // Array to store shape details: shape_id, start_stop_distance,
        // end_stop_distance
        int[] shapeDetails = new int[3];

        // SQL query to find the shape details for the fastest trip on the chosen route
        // String query = "SELECT t.trip_id, r.route_id, t.shape_id,
        // a.shape_dist_traveled as start_stop_distance, b.shape_dist_traveled as
        // end_stop_distance,"
        // +
        // " TIME_TO_SEC(b.arrival_time) - TIME_TO_SEC(a.departure_time) AS
        // travel_time_seconds FROM trips t JOIN routes r ON t.route_id = r.route_id"
        // +
        // " AND r.route_id = ? JOIN stop_times a ON t.trip_id = a.trip_id JOIN
        // stop_times b ON a.trip_id = b.trip_id"
        // +
        // " WHERE a.stop_id = ? AND b.stop_id = ? AND a.stop_sequence < b.stop_sequence
        // ORDER BY travel_time_seconds ASC LIMIT 1";

        String query = "SELECT shape_id, start_stop_distance, end_stop_distance FROM route_stop_view_final11 rsvf " +
                "WHERE rsvf.route_id = ? AND rsvf.start_stop_id = ? AND rsvf.end_stop_id = ?"
                +
                "ORDER BY rsvf.travel_time_seconds ASC LIMIT 1;";
        System.out.println("Executing query: " + query);
        System.out.println("Parameters: " + route.getRouteId() + ", " + route.getStartStopId() + ", "
                + route.getEndStopId());

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
                System.out.println("Executing query: " + query);
                System.out.println("Parameters: " + route.getRouteId() + ", " + route.getStartStopId() + ", "
                        + route.getEndStopId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shapeDetails;
    }

    // Method to find coordinates of all route points
    public static List<double[]> findCoordsOfAllRoutePoints(Route route) {

        // Create a list to store arrays with coordinates for all points that lie on the
        // chosen route and its quickest trip
        List<double[]> allPointsCoords = new LinkedList<>();
        // Find shape details for the given route
        int[] shapeDetails = findShapeDetails(route);

        // SQL query to retrieve shape points
        String shapePointsQuery = "SELECT shape_pt_lat, shape_pt_lon, shape_pt_sequence FROM shapes " +
                "WHERE shapes.shape_id = ? AND shapes.shape_dist_traveled BETWEEN ? AND ? " +
                "ORDER BY shape_pt_sequence ASC;";

        // String shapePointsQuery = "SELECT s.shape_pt_lat, s.shape_pt_lon,
        // s.shape_pt_sequence " +
        // "FROM shapes s JOIN trip_shape_view_final tsvf ON s.shape_id = tsvf.shape_id
        // "
        // +
        // "JOIN route_stop_view_final rsvf ON tsvf.route_id = rsvf.route_id " +
        // "WHERE tsvf.route_id = ? AND rsvf.start_stop_id = ? AND rsvf.end_stop_id = ?
        // "
        // +
        // "AND s.shape_dist_traveled BETWEEN tsvf.start_stop_distance AND
        // tsvf.end_stop_distance "
        // +
        // "ORDER BY s.shape_pt_sequence ASC;";
        // ;

        // SQL query to retrieve stop coordinates
        String stopsQuery = "SELECT stop_id, stop_lat, stop_lon FROM stops " +
                "WHERE stop_id IN (?, ?)";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(shapePointsQuery)) {

            // Set parameters for the shape points query
            pstmt.setInt(1, shapeDetails[0]);
            pstmt.setInt(2, shapeDetails[1]);
            pstmt.setInt(3, shapeDetails[2]);

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Store the retrieved shape point coordinates in an array
                    double[] current = new double[2];
                    current[0] = rs.getDouble("shape_pt_lat");
                    current[1] = rs.getDouble("shape_pt_lon");
                    allPointsCoords.add(current);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Retrieve stop coordinates and add them to the list of all points coordinates
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(stopsQuery)) {

            // Set parameters for the stops query
            pstmt.setString(1, route.getStartStopId());
            pstmt.setString(2, route.getEndStopId());

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Store the retrieved stop coordinates in an array
                    double[] current = new double[2];
                    current[0] = rs.getDouble("stop_lat");
                    current[1] = rs.getDouble("stop_lon");
                    // Add the stop coordinates to the list of all points coordinates
                    if (rs.getString("stop_id").equals(route.getStartStopId())) {
                        ((LinkedList<double[]>) allPointsCoords).addFirst(current);
                    } else {
                        ((LinkedList<double[]>) allPointsCoords).addLast(current);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allPointsCoords;
    }
}
