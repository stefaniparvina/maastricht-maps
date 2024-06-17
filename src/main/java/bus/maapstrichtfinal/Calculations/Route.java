package bus.maapstrichtfinal.Calculations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import bus.maapstrichtfinal.ZipCode.ZipCode;
import javafx.css.StyleableStringProperty;

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

    public  static String getTimeMinutesAndSeconds(Route route){
        int time = route.getTravelTime();
        int seconds = time % 60;
        int minutes = time / 60;
        if(seconds == 0) return minutes + " minutes";
        else return minutes + " minutes, " + seconds + " seconds";
    }

    public static List<Route> findDirectRoutes(String startStopId, String endStopId) {
        List<Route> routes = new ArrayList<>();

        String query = "SELECT DISTINCT " +
                "route_id, route_short_name, start_stop_id, end_stop_id, " +
                "travel_time_seconds, distance " +
                "FROM route_stop_view_final11 " +
                "WHERE start_stop_id = ? AND end_stop_id = ?";

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

    //TODO
    // i think this function is okay it finds all the buses we need correctly.
    public static List<Route> findTransferRoutes(String startStopId, String endStopId) {
        List<Route> routes = new ArrayList<>();
        int count = 0;
        String query = "SELECT DISTINCT " +
                "first_leg.route_id AS first_route_id, first_leg.route_short_name AS first_route_short_name, " +
                "first_leg.start_stop_id, first_leg.end_stop_id AS transfer_stop_id, " +
                "first_leg.travel_time_seconds AS first_leg_travel_time_seconds, " +
                "first_leg.distance AS first_leg_distance, " +
                "second_leg.route_id AS second_route_id, second_leg.route_short_name AS second_route_short_name, " +
                "second_leg.start_stop_id AS transfer_stop_id, second_leg.end_stop_id, " +
                "second_leg.travel_time_seconds AS second_leg_travel_time_seconds, " +
                "second_leg.distance AS second_leg_distance " +
                "FROM route_stop_view_final11 first_leg " +
                "JOIN route_stop_view_final11 second_leg ON first_leg.end_stop_id = second_leg.start_stop_id " +
                "WHERE first_leg.start_stop_id = ? AND second_leg.end_stop_id = ? " +
                "AND first_leg.route_id <> second_leg.route_id";

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
                            rs.getInt("first_leg_distance")
                    );

                    Route secondLeg = new Route(
                            rs.getInt("second_route_id"),
                            rs.getString("second_route_short_name"),
                            rs.getString("transfer_stop_id"),
                            rs.getString("end_stop_id"),
                            rs.getInt("second_leg_travel_time_seconds"),
                            rs.getInt("second_leg_distance")
                    );

                    routes.add(firstLeg);
                    if(count == 0) {
                        System.out.println("Bus transfer: \n" + routes.get(0).getRouteShortName());
                    }
                    routes.add(secondLeg);
                    if(count == 0) {
                        System.out.println(routes.get(1).getRouteShortName() + "\n" );
                        count++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }


    // Method to find all possible routes between two zip codes
    public static List<Route> findAllRoutes(ZipCode startZipCode, ZipCode endZipCode) {
        List<Route> allRoutes = new ArrayList<>();
        try {
            List<String> startStops300m = ClosestStopFinder.findStops300m(startZipCode.getLatitude(), startZipCode.getLongitude());
            List<String> endStops300m = ClosestStopFinder.findStops300m(endZipCode.getLatitude(), endZipCode.getLongitude());

            for (String startStop : startStops300m) {
                for (String endStop : endStops300m) {
                    allRoutes.addAll(findDirectRoutes(startStop, endStop));
                }
            }

            if (allRoutes.isEmpty()) {
                List<String> startStops500m = ClosestStopFinder.findStops500m(startZipCode.getLatitude(), startZipCode.getLongitude());
                List<String> endStops500m = ClosestStopFinder.findStops500m(endZipCode.getLatitude(), endZipCode.getLongitude());

                for (String startStop : startStops500m) {
                    for (String endStop : endStops500m) {
                        allRoutes.addAll(findDirectRoutes(startStop, endStop));

                    }
                }
                if(allRoutes.isEmpty()){
                    for (String startStop : startStops500m) {
                        for (String endStop : endStops500m) {
                            allRoutes.addAll(findTransferRoutes(startStop, endStop));
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Something went wrong with the closest bus stops finder.");
            e.printStackTrace();
        }
        return allRoutes;
    }


    //TODO
    // this function doesnt work for transfers as it can take only one route (fastest) not 2.
    // Method to find the fastest routes between two zip codes
    public static Route findFastestRoute(ZipCode startZipCode, ZipCode endZipCode) {
        List<Route> allRoutes = findAllRoutes(startZipCode, endZipCode);
        List<Route> fastest = new ArrayList<>();

        if (allRoutes.isEmpty()) {
            System.out.println("There is not a bus route connecting the two specified zip codes. We suggest seeking alternative transportation options.");
            return null;
        }

        int currentTravelTime = allRoutes.get(0).getTravelTime();
        for (Route route : allRoutes) {
            if (route.getTravelTime() < currentTravelTime) {
                fastest.clear();
                fastest.add(route);
                currentTravelTime = route.getTravelTime();
            } else if (route.getTravelTime() == currentTravelTime) {
                fastest.add(route);
            }
        }
        return fastest.isEmpty() ? null : fastest.get(0);
    }

    public static boolean isRouteAvailable(ZipCode startZipCode, ZipCode endZipCode){
        if (!(DestinationInfo.getSelectedTransportOption().equalsIgnoreCase("bus"))) return true;
        if (startZipCode == null || endZipCode == null) return false;
        List<Route> routes = findAllRoutes(startZipCode,endZipCode);
        return !routes.isEmpty();
    }

    //TODO
    // this function is temporary we need to actually code it
    public boolean isTransfer() {
        return true;
    }
}
