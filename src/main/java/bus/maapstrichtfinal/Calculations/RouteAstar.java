package bus.maapstrichtfinal.Calculations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("static-access")
public class RouteAstar {
    // Route attributes
    private int routeId;
    private String routeShortName;
    private String startStopId;
    private String endStopId;
    private int travelTime;
    private int distance;
    private int tripId;

    // Constructor
    public RouteAstar(int routeId, int tripId, String routeShortName, String startStopId, String endStopId, int travelTime,
                 int distance) {
        this.routeId = routeId;
        this.routeShortName = routeShortName;
        this.startStopId = startStopId;
        this.endStopId = endStopId;
        this.travelTime = travelTime;
        this.distance = distance;
        this.tripId = tripId;
    }

    // Getters for route attributes
    public int getRouteId() {
        return routeId;
    }

    public String getRouteShortName() {
        return routeShortName;
    }

    public String getStartStopId() {
        return startStopId;
    }

    public String getEndStopId() {
        return endStopId;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public int getDistance() {
        return distance;
    }


    // Static method to find routes between stops
    public static List<RouteAstar> findFastestRoutesBetweenStops(String startStopId, String endStopId) {
        // List to store information for every route that goes through the 2 bus stops
        // in the correct direction
        List<RouteAstar> routes = new ArrayList<>();

        // SQL query to retrieve routes between the start and end bus stops
        String query = "SELECT DISTINCT \n" +
                "    rs.route_short_name, \n" +
                "    rs.route_id, \n" +
                "    t.trip_id, \n" +
                "    rs.start_stop_id, \n" +
                "    rs.end_stop_id, \n" +
                "    rs.travel_time_seconds, \n" +
                "    rs.distance \n" +
                "FROM route_stop_view_final11_mat rs \n" +
                "JOIN trips t ON rs.route_id = t.route_id \n" +
                "JOIN stop_times a ON t.trip_id = a.trip_id AND rs.start_stop_id = a.stop_id \n" +
                "WHERE rs.start_stop_id = ? \n" +
                "  AND rs.end_stop_id = ? \n" +
                "  AND a.departure_time >= CURTIME() \n" +
                "  AND rs.travel_time_seconds = (\n" +
                "    SELECT MIN(rs2.travel_time_seconds) \n" +
                "    FROM route_stop_view_final11_mat rs2 \n" +
                "    JOIN trips t2 ON rs2.route_id = t2.route_id \n" +
                "    JOIN stop_times a2 ON t2.trip_id = a2.trip_id AND rs2.start_stop_id = a2.stop_id \n" +
                "    WHERE rs2.start_stop_id = ? \n" +
                "      AND rs2.end_stop_id = ? \n" +
                "      AND a2.departure_time >= CURTIME() \n" +
                "  )\n;";
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameters for the query
            pstmt.setString(1, startStopId);
            pstmt.setString(2, endStopId);
            pstmt.setString(3, startStopId);
            pstmt.setString(4, endStopId);

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Create a Route object for each result and add it to the list of routes
                    RouteAstar route = new RouteAstar(
                            rs.getInt("route_id"),
                            rs.getInt("trip_id"),
                            rs.getString("route_short_name"),
                            rs.getString("start_stop_id"),
                            rs.getString("end_stop_id"),
                            rs.getInt("travel_time_seconds"),
                            rs.getInt("distance"));
                    routes.add(route);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }
}