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
    private String routeShortName;
    private String startStopId;
    private String endStopId;
    private int travelTime;
    private int distance;

    // Constructor
    public Route(int routeId, String routeShortName, String startStopId, String endStopId, int travelTime,
            int distance) {
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

    // Method to find direct routes between two stops
    public static List<List<Route>> findDirectRoutes(String startStopId, String endStopId) {
        List<List<Route>> routes = new ArrayList<>();

        String query = "SELECT DISTINCT " +
        "route_id, route_short_name, start_stop_id, end_stop_id, " +
        "travel_time_seconds, distance " +
        "FROM route_stop_view_final11_mat " +
        "WHERE start_stop_id = ? AND end_stop_id = ?";


        //old query kept for testing purposes 
        // String query = "SELECT DISTINCT " +
        //         "r.route_short_name, " +
        //         "r.route_id, " +
        //         "a.stop_id AS start_stop_id, " +
        //         "b.stop_id AS end_stop_id, " +
        //         "(TIME_TO_SEC(b.arrival_time) - TIME_TO_SEC(a.departure_time)) AS travel_time_seconds, " +
        //         "b.shape_dist_traveled - a.shape_dist_traveled AS distance " +
        //         "FROM routes r " +
        //         "JOIN trips t ON r.route_id = t.route_id " +
        //         "JOIN stop_times a ON t.trip_id = a.trip_id " +
        //         "JOIN stop_times b ON a.trip_id = b.trip_id " +
        //         "WHERE a.stop_sequence < b.stop_sequence " +
        //         "AND a.stop_id = ? AND b.stop_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, startStopId);
            pstmt.setString(2, endStopId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Route route = new Route(
                            rs.getInt("route_id"),
                            rs.getString("route_short_name"),
                            rs.getString("start_stop_id"),
                            rs.getString("end_stop_id"),
                            rs.getInt("travel_time_seconds"),
                            rs.getInt("distance"));
                    List<Route> routeOption = new ArrayList<>();
                    routeOption.add(route);
                    routes.add(routeOption);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return routes;
    }

    // Method to find transfer routes between two stops
    public static List<List<Route>> findTransferRoutes(String startStopId, String endStopId) {
        List<List<Route>> routes = new ArrayList<>();
        int count = 0;
        String query = "SELECT DISTINCT " +
        "first_leg.route_id AS first_route_id, first_leg.route_short_name ASfirst_route_short_name, " +
        "first_leg.start_stop_id, first_leg.end_stop_id AS transfer_stop_id, " +
        "first_leg.travel_time_seconds AS first_leg_travel_time_seconds, " +
        "first_leg.distance AS first_leg_distance, " +
        "second_leg.route_id AS second_route_id, second_leg.route_short_name AS second_route_short_name, " +
        "second_leg.start_stop_id AS transfer_stop_id, second_leg.end_stop_id, " +
        "second_leg.travel_time_seconds AS second_leg_travel_time_seconds, " +
        "second_leg.distance AS second_leg_distance " +
        "FROM route_stop_view_final11_mat first_leg " +
        "JOIN route_stop_view_final11_mat second_leg ON first_leg.end_stop_id = second_leg.start_stop_id " +
        "WHERE first_leg.start_stop_id = ? AND second_leg.end_stop_id = ? " +
        "AND first_leg.route_id <> second_leg.route_id";


        //old query kept for testing purposes
        // String query = "SELECT DISTINCT " +
        //         "r1.route_id AS first_route_id, r1.route_short_name AS first_route_short_name, " +
        //         "a1.stop_id AS start_stop_id, c1.stop_id AS transfer_stop_id, " +
        //         "(TIME_TO_SEC(c1.arrival_time) - TIME_TO_SEC(a1.departure_time)) AS first_leg_travel_time_seconds, " +
        //         "(c1.shape_dist_traveled - a1.shape_dist_traveled) AS first_leg_distance, " +
        //         "r2.route_id AS second_route_id, r2.route_short_name AS second_route_short_name, " +
        //         "c2.stop_id AS transfer_stop_id, b2.stop_id AS end_stop_id, " +
        //         "(TIME_TO_SEC(b2.arrival_time) - TIME_TO_SEC(c2.departure_time)) AS second_leg_travel_time_seconds, " +
        //         "(b2.shape_dist_traveled - c2.shape_dist_traveled) AS second_leg_distance " +
        //         "FROM routes r1 " +
        //         "JOIN trips t1 ON r1.route_id = t1.route_id " +
        //         "JOIN stop_times a1 ON t1.trip_id = a1.trip_id " +
        //         "JOIN stop_times c1 ON t1.trip_id = c1.trip_id " +
        //         "JOIN stop_times c2 ON c1.stop_id = c2.stop_id " +
        //         "JOIN stop_times b2 ON c2.trip_id = b2.trip_id " +
        //         "JOIN trips t2 ON b2.trip_id = t2.trip_id " +
        //         "JOIN routes r2 ON t2.route_id = r2.route_id " +
        //         "WHERE " +
        //         "a1.stop_sequence < c1.stop_sequence " +
        //         "AND c1.stop_sequence < b2.stop_sequence " +
        //         "AND c1.trip_id <> b2.trip_id " +
        //         "AND a1.stop_id = ? AND b2.stop_id = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, startStopId);
            pstmt.setString(2, endStopId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Route firstLeg = new Route(
                            rs.getInt("first_route_id"),
                            rs.getString("first_route_short_name"),
                            rs.getString("start_stop_id"),
                            rs.getString("transfer_stop_id"),
                            rs.getInt("first_leg_travel_time_seconds"),
                            rs.getInt("first_leg_distance"));

                    Route secondLeg = new Route(
                            rs.getInt("second_route_id"),
                            rs.getString("second_route_short_name"),
                            rs.getString("transfer_stop_id"),
                            rs.getString("end_stop_id"),
                            rs.getInt("second_leg_travel_time_seconds"),
                            rs.getInt("second_leg_distance"));

                    List<Route> routeOption = new ArrayList<>();
                    routeOption.add(firstLeg);
                    routeOption.add(secondLeg);
                    if (count == 0) {
                        count++;
                    }

                    // Add the routeOption to the routes list
                    routes.add(routeOption);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }

    // Method to find all possible routes between two zip codes
    public static List<List<Route>> findAllRoutes(ZipCode startZipCode, ZipCode endZipCode) {
        List<List<Route>> allRoutes = new ArrayList<>();
        try {
            // Try finding routes within 300m range first
            allRoutes = findRoutesWithinRange(startZipCode, endZipCode, 300);

            // If no routes found within 300m, try finding routes within 500m range
            if (allRoutes.isEmpty()) {
                allRoutes = findRoutesWithinRange(startZipCode, endZipCode, 500);
            }

            // If still no routes found within 500m, try finding transfer routes within 500m
            // range
            if (allRoutes.isEmpty()) {
                List<String> startStops500m = ClosestStopFinder.findStops500m(startZipCode.getLatitude(),
                        startZipCode.getLongitude());
                List<String> endStops500m = ClosestStopFinder.findStops500m(endZipCode.getLatitude(),
                        endZipCode.getLongitude());

                for (String startStop : startStops500m) {
                    for (String endStop : endStops500m) {
                        allRoutes.addAll(findTransferRoutes(startStop, endStop));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Something went wrong with the closest bus stops finder.");
            e.printStackTrace();
        }
        return allRoutes;
    }

    private static List<List<Route>> findRoutesWithinRange(ZipCode startZipCode, ZipCode endZipCode, int range)
            throws SQLException {
        List<List<Route>> routes = new ArrayList<>();
        List<String> startStops = (range == 300)
                ? ClosestStopFinder.findStops300m(startZipCode.getLatitude(), startZipCode.getLongitude())
                : ClosestStopFinder.findStops500m(startZipCode.getLatitude(), startZipCode.getLongitude());
        List<String> endStops = (range == 300)
                ? ClosestStopFinder.findStops300m(endZipCode.getLatitude(), endZipCode.getLongitude())
                : ClosestStopFinder.findStops500m(endZipCode.getLatitude(), endZipCode.getLongitude());

        for (String startStop : startStops) {
            for (String endStop : endStops) {
                routes.addAll(findDirectRoutes(startStop, endStop));
            }
        }
        return routes;
    }

    // Method to find the fastest route(s) between two zip codes and return the
    // fastest sequence of routes
    public static List<Route> findFastestRoute(ZipCode startZipCode, ZipCode endZipCode) {
        // Step 1: Find all possible routes (both direct and transfer)
        List<List<Route>> allRoutes = findAllRoutes(startZipCode, endZipCode);
        List<Route> fastestRoute = new ArrayList<>();

        if (allRoutes.isEmpty()) {
            System.out.println("There is no bus route connecting the two specified zip codes.");
            return fastestRoute;
        }

        int currentTravelTime = getTotalTravelTime(allRoutes.get(0));

        for (List<Route> routeOption : allRoutes) {
            int routeTravelTime = getTotalTravelTime(routeOption);
            if (routeTravelTime < currentTravelTime) {
                fastestRoute = new ArrayList<>(routeOption);
                currentTravelTime = routeTravelTime;
            }
        }
        return fastestRoute;
    }

    // Helper method to calculate total travel time for a list of routes
    private static int getTotalTravelTime(List<Route> routes) {
        return routes.stream().mapToInt(Route::getTravelTime).sum();
    }

    public static boolean isRouteAvailable(ZipCode startZipCode, ZipCode endZipCode) {
        if (!DestinationInfo.getSelectedTransportOption().equalsIgnoreCase("bus"))
            return true;
        if (startZipCode == null || endZipCode == null)
            return false;
        List<List<Route>> routes = findAllRoutes(startZipCode, endZipCode);
        return !routes.isEmpty();
    }

    // Method to determine if a route involves a transfer
    public boolean isTransfer() {
        // A route is considered a transfer if it consists of more than one segment
        return this.startStopId != null && this.endStopId != null && DestinationInfo.bestRoute.size() > 1;
    }
}
