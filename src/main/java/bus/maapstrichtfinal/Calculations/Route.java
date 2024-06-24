package bus.maapstrichtfinal.Calculations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import bus.maapstrichtfinal.ZipCode.ZipCode;

@SuppressWarnings("static-access")
public class Route {
    // Route attributes
    private int routeId;
    private String routeShortName;
    private String startStopId;
    private String endStopId;
    private int travelTime;
    private int distance;
    private int tripId;

    // Constructor
    public Route(int routeId, int tripId, String routeShortName, String startStopId, String endStopId, int travelTime,
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

    public static String getTimeMinutesAndSeconds(Route route) {
        int time = route.getTravelTime();
        int seconds = time % 60;
        int minutes = time / 60;
        if (seconds == 0)
            return minutes + " minutes";
        else
            return minutes + " minutes, " + seconds + " seconds";
    }

    // Static method to find routes between stops
    public static Route findRoutesBetweenStops(String startStopId, String endStopId) {
        // List to store information for every route that goes through the 2 bus stops
        // in the correct direction
        List<Route> routes = new ArrayList<>();

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
                "ORDER BY rs.travel_time_seconds ASC \n" +
                "LIMIT 1;";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameters for the query
            pstmt.setString(1, startStopId);
            pstmt.setString(2, endStopId);

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Create a Route object for each result and add it to the list of routes
                    Route route = new Route(
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
        if (routes.isEmpty())
            return null;
        else {
            return routes.get(0);
        }
    }
}

// Method to find all possible routes between two zip codes
// public static List<Route> findAllRoutes(ZipCode startZipCode, ZipCode
// endZipCode) {
// List<Route> allRoutes = new ArrayList<>();
// // Find the closest bus stops to start and end zip codes within 300m
// List<BusStop> startStops300m =
// ClosestStopFinder.findClosestStops(startZipCode.getLatitude(),
// startZipCode.getLongitude(),0.3);
// List<BusStop> endStops300m =
// ClosestStopFinder.findClosestStops(endZipCode.getLatitude(),
// endZipCode.getLongitude(), 0.3);
//
// // Check for routes between 300m stops
// for (int i = 0; i < startStops300m.size(); i++) {
// for (int j = 0; j < endStops300m.size(); j++) {
// allRoutes.addAll(findRoutesBetweenStops(startStops300m.get(i).getStopId(),
// endStops300m.get(j).getStopId()));
// }
// }
// // If no routes found for 300m stops, check 500m stops
// if (allRoutes.isEmpty()) {
// List<BusStop> startStops500m =
// ClosestStopFinder.findClosestStops(startZipCode.getLatitude(),
// startZipCode.getLongitude(), 0.5);
// List<BusStop> endStops500m =
// ClosestStopFinder.findClosestStops(endZipCode.getLatitude(),
// endZipCode.getLongitude(), 0.5);
//
// for (int i = 0; i < startStops500m.size(); i++) {
// for (int j = 0; j < endStops500m.size(); j++) {
// allRoutes.addAll(findRoutesBetweenStops(startStops500m.get(i).getStopId(),
// endStops500m.get(j).getStopId()));
// }
// }
// }
// return allRoutes;
// }
//
// // Method to find the fastest routes between two zip codes
// public static Route findFastestRoute(ZipCode startZipCode, ZipCode
// endZipCode) {
// // Find all possible routes between the start and end zip codes
// List<Route> allRoutes = findAllRoutes(startZipCode, endZipCode);
// // List to store the fastest routes
// List<Route> fastest = new ArrayList<Route>();
// // Variable to store the current minimum travel time
// int currentTravelTime;
//
// // If no routes are found, display a message and return null
// if (allRoutes.isEmpty()) {
// System.out.println("There is not a bus route connecting the two specified zip
// codes. We suggest seeking alternative transportation options.");
// return null;
// } else {
// // Initialize current minimum travel time with the travel time of the first
// route
// currentTravelTime = allRoutes.get(0).getTravelTime();
// // Iterate through each route to find the fastest one(s)
// for (Route route : allRoutes) {
// if (route.getTravelTime() < currentTravelTime) {
// // If a route with shorter travel time is found, clear the list of fastest
// routes and add the new route
// fastest.clear();
// fastest.add(route);
// currentTravelTime = route.getTravelTime();
// } else if (route.getTravelTime() == currentTravelTime) {
// // If a route with the same travel time is found, add it to the list of
// fastest routes
// fastest.add(route);
// }
// }
// if (fastest.isEmpty()) {
// return null;
// }
// return fastest.get(0);
// }
// }
// }
/*
 * public static boolean isRouteAvailable(ZipCode startZipCode, ZipCode
 * endZipCode){
 * if (!(DestinationInfo.getSelectedTransportOption().equalsIgnoreCase("bus")))
 * return true;
 * if (startZipCode == null || endZipCode == null) return false;
 * List<Route> routes = findAllRoutes(startZipCode,endZipCode);
 * return !routes.isEmpty();
 * }
 * }
 *
 */