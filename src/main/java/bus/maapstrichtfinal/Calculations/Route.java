package bus.maapstrichtfinal.Calculations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import bus.maapstrichtfinal.ZipCode.ZipCode;

@SuppressWarnings("static-access")
public class Route {
    // Route attributes
    private int routeId;
    private static String routeShortName;
    private String startStopId;
    private String endStopId;
    private static int travelTime;
    private int distance;

    // Constructor
    public Route(int routeId, String routeShortName, String startStopId, String endStopId, int travelTime, int distance) {
        this.routeId = routeId;
        this.routeShortName = routeShortName;
        this.startStopId = startStopId;
        this.endStopId = endStopId;
        this.travelTime = travelTime;
        this.distance = distance;
    }

    // Getters for route attributes
    public int getRouteId() {
        return routeId;
    }

    public static String getRouteShortName() {
        return routeShortName;
    }

    public String getStartStopId() {
        return startStopId;
    }

    public String getEndStopId() {
        return endStopId;
    }

    public static int getTravelTime() {
        return travelTime;
    }

    public int getDistance() {
        return distance;
    }

    // Static method to find routes between stops
    public static List<Route> findRoutesBetweenStops(String startStopId, String endStopId) {
        // List to store information for every route that goes through the 2 bus stops in the correct direction
        List<Route> routes = new ArrayList<>();

        // SQL query to retrieve routes between the start and end bus stops
        String query = "SELECT DISTINCT r.route_short_name, r.route_id, a.stop_id AS start_stop_id, b.stop_id AS end_stop_id," +
                " (TIME_TO_SEC(b.arrival_time) - TIME_TO_SEC(a.departure_time)) as travel_time_seconds, b.shape_dist_traveled - a.shape_dist_traveled AS distance" +
                " FROM routes r JOIN trips t ON r.route_id = t.route_id JOIN stop_times a ON t.trip_id = a.trip_id JOIN stop_times b ON" +
                " a.trip_id = b.trip_id JOIN shapes sh ON t.shape_id = sh.shape_id WHERE a.stop_id = ? AND b.stop_id = ? AND a.stop_sequence < b.stop_sequence";

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
                            rs.getString("route_short_name"),
                            rs.getString("start_stop_id"),
                            rs.getString("end_stop_id"),
                            rs.getInt("travel_time_seconds"),
                            rs.getInt("distance")
                    );
                    routes.add(route);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }

    // Method to find all possible routes between two zip codes
    public static List<Route> findAllRoutes(ZipCode startZipCode, ZipCode endZipCode){
        // Make a list that contains all possible routes between the two zip codes
        // Make a list with the possible start bus stops and a list with the end bus stops
        // Check for the routes for every possible pair and add them

        List<Route> allRoutes = new ArrayList<>();
        try {
            // Find the closest bus stops to start and end zip codes
            List<String> startStops = ClosestStopFinder.findStops(startZipCode.getLatitude(), startZipCode.getLongitude());
            List<String> endStops = ClosestStopFinder.findStops(endZipCode.getLatitude(), endZipCode.getLongitude());

            // Iterate through each pair of start and end stops
            for (int i = 0; i < startStops.size(); i++) {
                for (int j = 0; j < endStops.size(); j++) {
                    // Find routes between the current pair of stops and add them to the list of all routes
                    allRoutes.addAll(findRoutesBetweenStops(startStops.get(i), endStops.get(j)));
                }
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong with the closest bus stops finder.");
            e.printStackTrace();
            return allRoutes;
        }
        return allRoutes;
    }

    // Method to find the fastest routes between two zip codes
    public static List<Route> findFastestRoutes(ZipCode startZipCode, ZipCode endZipCode) {
        // Find all possible routes between the start and end zip codes
        List<Route> allRoutes = findAllRoutes(startZipCode, endZipCode);
        // List to store the fastest routes
        List<Route> fastest = new ArrayList<Route>();
        // Variable to store the current minimum travel time
        int currentTravelTime;

        // If no routes are found, display a message and return null
        if (allRoutes.isEmpty()) {
            System.out.println("There is not a bus route connecting the two specified zip codes. We suggest seeking alternative transportation options.");
            return null;
        } else {
            // Initialize current minimum travel time with the travel time of the first route
            currentTravelTime = allRoutes.get(0).getTravelTime();
            // Iterate through each route to find the fastest one(s)
            for (Route route : allRoutes) {
                if (route.getTravelTime() < currentTravelTime) {
                    // If a route with shorter travel time is found, clear the list of fastest routes and add the new route
                    fastest.clear();
                    fastest.add(route);
                    currentTravelTime = route.getTravelTime();
                } else if (route.getTravelTime() == currentTravelTime) {
                    // If a route with the same travel time is found, add it to the list of fastest routes
                    fastest.add(route);
                }
            }
            return fastest;
        }
    }

    public static boolean isRouteAvailable(ZipCode startZipCode, ZipCode endZipCode){
        if (!(DestinationInfo.getSelectedTransportOption().equalsIgnoreCase("bus"))) return true;
        if (startZipCode == null || endZipCode == null) return false;
        List<Route> routes = findAllRoutes(startZipCode,endZipCode);
        return !routes.isEmpty();
    }
}
