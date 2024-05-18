package bus.maapstrichtfinal.Calculations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusRouteShape {
    
    public static int[] findShapeDetails(Route route) {
        int[] shapeDetails = new int[3];
        
        //The query returns a with the id of the fastest trip on the chosen route and the corresponding shape_id 
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
            e.printStackTrace();
        }
        return shapeDetails;
    }

    public static List<double[]> findCoordsOfAllRoutePoints(Route route){
        
        //Create a list that will store arrays with coordinates for all pointers that lie on the chosen route and its quickest trip
        
        int[] shapeDetails = findShapeDetails(route);
        
        List<double[]> allPointsCoords = new ArrayList<double[]>();

        String query = "SELECT shape_pt_lat, shape_pt_lon, shape_pt_sequence FROM shapes WHERE shape_id = ?" + 
        " AND shape_dist_traveled  BETWEEN ? AND ? ORDER BY shape_pt_sequence ASC;";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, shapeDetails[0]);
            pstmt.setInt(2, shapeDetails[1]);
            pstmt.setInt(3, shapeDetails[2]);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    double[] current = new double[2];
                    current[0] = rs.getDouble("shape_pt_lat");
                    current[1] = rs.getDouble("shape_pt_lon");
                    allPointsCoords.add(current);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allPointsCoords;
    }








}
