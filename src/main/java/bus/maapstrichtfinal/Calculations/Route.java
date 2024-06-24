package bus.maapstrichtfinal.Calculations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.sql.Time;

import bus.maapstrichtfinal.CommonController;
import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import bus.maapstrichtfinal.Map.MapInitializer;
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
    private Time departureTime;
    private Time arrivalTime;

    // Constructor
    public Route(int routeId, String routeShortName, String startStopId, String endStopId,
                 int travelTime, int distance, Time arrivalTime, Time departureTime) {
        this.routeId = routeId;
        this.routeShortName = routeShortName;
        this.startStopId = startStopId;
        this.endStopId = endStopId;
        this.travelTime = travelTime;
        this.distance = distance;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
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

    public Time getDepartureTime() {
        return departureTime;
    }
    public Time getArrivalTime(){return arrivalTime;}

    public static int getTime(List<Route> routes) {
        int totalTime = 0;
        for (Route route : routes) {
            totalTime += route.getTravelTime();
        }
        return totalTime;
    }


    // Method to find direct routes between two stops
    public static List<List<Route>> findDirectRoutes(String startStopId, String endStopId, Time deptTime) {
        List<List<Route>> routes = new ArrayList<>();

        String query = "SELECT " +
                "r.route_id, r.route_short_name, r.start_stop_id, r.end_stop_id, " +
                "r.travel_time_seconds, r.distance, s.departure_time, s.arrival_time " +
                "FROM route_stop_view_final11_mat r " +
                "JOIN stop_times s ON s.stop_id = r.end_stop_id " +
                "WHERE r.start_stop_id = ? AND r.end_stop_id = ? " +
                "AND s.departure_time > ? ";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, startStopId);
            pstmt.setString(2, endStopId);
            pstmt.setTime(3, deptTime);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Route route = new Route(
                            rs.getInt("route_id"),
                            rs.getString("route_short_name"),
                            rs.getString("start_stop_id"),
                            rs.getString("end_stop_id"),
                            rs.getInt("travel_time_seconds"),
                            rs.getInt("distance"),
                            rs.getTime("arrival_time"),
                            rs.getTime("departure_time")
                    );
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
    public static List<List<Route>> findTransferRoutes(String startStopId, String endStopId, Time deptTime) {
        List<List<Route>> routes = new ArrayList<>();
        String query = "SELECT DISTINCT " +
                "first_leg.route_id AS first_route_id, first_leg.route_short_name AS first_route_short_name, " +
                "first_leg.start_stop_id, first_leg.end_stop_id AS transfer_stop_id, " +
                "first_leg.travel_time_seconds AS first_leg_travel_time_seconds, " +
                "first_leg.distance AS first_leg_distance, " +
                "s.departure_time, s.arrival_time, " +
                "second_leg.route_id AS second_route_id, second_leg.route_short_name AS second_route_short_name, " +
                "second_leg.start_stop_id AS transfer_stop_id, second_leg.end_stop_id, " +
                "second_leg.travel_time_seconds AS second_leg_travel_time_seconds, " +
                "second_leg.distance AS second_leg_distance " +
                "FROM route_stop_view_final11_mat first_leg " +
                "JOIN route_stop_view_final11_mat second_leg ON first_leg.end_stop_id = second_leg.start_stop_id " +
                "JOIN stop_times s ON s.stop_id = first_leg.start_stop_id " +
                "WHERE first_leg.start_stop_id = ? " +
                "AND second_leg.end_stop_id = ? " +
                "AND first_leg.route_id <> second_leg.route_id " +
                "AND s.departure_time > ? ";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, startStopId);
            pstmt.setString(2, endStopId);
            pstmt.setTime(3, deptTime);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Route firstLeg = new Route(
                            rs.getInt("first_route_id"),
                            rs.getString("first_route_short_name"),
                            rs.getString("start_stop_id"),
                            rs.getString("transfer_stop_id"),
                            rs.getInt("first_leg_travel_time_seconds"),
                            rs.getInt("first_leg_distance"),
                            rs.getTime("arrival_time"),
                            rs.getTime("departure_time")
                    );

                    Route secondLeg = new Route(
                            rs.getInt("second_route_id"),
                            rs.getString("second_route_short_name"),
                            rs.getString("transfer_stop_id"),
                            rs.getString("end_stop_id"),
                            rs.getInt("second_leg_travel_time_seconds"),
                            rs.getInt("second_leg_distance"),
                            rs.getTime("arrival_time"),
                            rs.getTime("departure_time")
                    );

                    List<Route> routeOption = new ArrayList<>();
                    routeOption.add(firstLeg);
                    System.out.println(firstLeg.getRouteShortName());
                    routeOption.add(secondLeg);
                    System.out.println(secondLeg.getRouteShortName());
                    System.out.println("  ...  ");

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

            // If still no routes found within 500m, try finding transfer routes within 500m range
            if (allRoutes.isEmpty()) {
                List<String> startStops500m = ClosestStopFinder.findStops500m(startZipCode.getLatitude(), startZipCode.getLongitude());
                List<String> endStops500m = ClosestStopFinder.findStops500m(endZipCode.getLatitude(), endZipCode.getLongitude());

                for (String startStop : startStops500m) {
                    for (String endStop : endStops500m) {
                        allRoutes.addAll(findTransferRoutes(startStop, endStop, CommonController.getDeptTime()));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Something went wrong with the closest bus stops finder.");
            e.printStackTrace();
        }
        return allRoutes;
    }

    private static List<List<Route>> findRoutesWithinRange(ZipCode startZipCode, ZipCode endZipCode, int range) throws SQLException {
        List<List<Route>> routes = new ArrayList<>();
        List<String> startStops = (range == 300) ? ClosestStopFinder.findStops300m(startZipCode.getLatitude(), startZipCode.getLongitude())
                : ClosestStopFinder.findStops500m(startZipCode.getLatitude(), startZipCode.getLongitude());
        List<String> endStops = (range == 300) ? ClosestStopFinder.findStops300m(endZipCode.getLatitude(), endZipCode.getLongitude())
                : ClosestStopFinder.findStops500m(endZipCode.getLatitude(), endZipCode.getLongitude());

        for (String startStop : startStops) {
            for (String endStop : endStops) {
                routes.addAll(findDirectRoutes(startStop, endStop, CommonController.getDeptTime()));
            }
        }
        return routes;
    }


    // Method to find the fastest route(s) between two zip codes and return the fastest sequence of routes
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
        int time = (int) (routes.stream().mapToInt(Route::getTravelTime).sum() +
                TravelTime.getWalkingTimeBusStop(DestinationInfo.getStartDestination(), routes.get(0).getStartStopId()));

        if (routes.size() > 1) {
            time += (int) TravelTime.getWalkingTimeBusStop(DestinationInfo.getEndDestination(), routes.get(1).getEndStopId());

            // Convert departure and arrival times to LocalTime
            Time departureTime = routes.get(1).getDepartureTime();
            Time arrivalTime = routes.get(0).getArrivalTime();

            LocalTime departureLocalTime = departureTime.toLocalTime();
            LocalTime arrivalLocalTime = arrivalTime.toLocalTime();

            // Calculate the difference in seconds
            long timeDifferenceInSeconds = ChronoUnit.SECONDS.between(arrivalLocalTime, departureLocalTime);

            // Add the difference to time
            time += (int) timeDifferenceInSeconds;
        } else {
            time += (int) TravelTime.getWalkingTimeBusStop(DestinationInfo.getEndDestination(), routes.get(0).getEndStopId());
        }

        return time;
    }

    public static boolean isRouteAvailable(ZipCode startZipCode, ZipCode endZipCode) {
        if (!DestinationInfo.getSelectedTransportOption().equalsIgnoreCase("bus")) return true;
        if (startZipCode == null || endZipCode == null) return false;
        List<List<Route>> routes = findAllRoutes(startZipCode, endZipCode);
        return !routes.isEmpty();
    }

    // Method to determine if a route involves a transfer
    public boolean isTransfer() {
        // A route is considered a transfer if it consists of more than one segment
        return this.startStopId != null && this.endStopId != null && DestinationInfo.bestRoute.size() > 1;
    }
}
