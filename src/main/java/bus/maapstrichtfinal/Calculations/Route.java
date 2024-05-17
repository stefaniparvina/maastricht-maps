package bus.maapstrichtfinal.Calculations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bus.maapstrichtfinal.ZipCode.ZipCode;

public class Route {
    // Route attributes
    private int routeId;
    private String routeShortName;
    private String startStopId;
    private String endStopId;
    private int travelTime;
    private int distance;

    public Route(int routeId, String routeShortName, String startStopId, String endStopId, int travelTime, int distance) {
        this.routeId = routeId;
        this.routeShortName = routeShortName;
        this.startStopId = startStopId;
        this.endStopId = endStopId;
        this.travelTime = travelTime;
        this.distance = distance;
    }

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
    public static List<Route> findRoutesBetweenStops(String startStopId, String endStopId) {
        //Create a list that will store information for every route that goes trough the 2 bus stops in the correct direction
        List<Route> routes = new ArrayList<>();

        //The query returns a table with the possible routes from the start bus stop to the end bus stop
        String query = "SELECT DISTINCT r.route_short_name, r.route_id, a.stop_id AS start_stop_id, b.stop_id AS end_stop_id," +
                " (TIME_TO_SEC(b.arrival_time) - TIME_TO_SEC(a.departure_time)) as travel_time_seconds, b.shape_dist_traveled - a.shape_dist_traveled AS distance" +
                " FROM routes r JOIN trips t ON r.route_id = t.route_id JOIN stop_times a ON t.trip_id = a.trip_id JOIN stop_times b ON" +
                " a.trip_id = b.trip_id JOIN shapes sh ON t.shape_id = sh.shape_id WHERE a.stop_id = ? AND b.stop_id = ? AND a.stop_sequence < b.stop_sequence";

        try (
            Connection conn = DatabaseConnection.getConnection();
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
    
    public static List<Route> findAllRoutes(ZipCode startZipCode, ZipCode endZipCode){
        //Make a list that contains all possble routes between the two zip codes
        //Make a list with the possible start bus stops and a list with the end bus stops
        //Check for the routes for every possible pair and add them
        
        List<Route> allRoutes = new ArrayList<>();
        try {
            List<String> startStops = ClosestStopFinder.findStops(startZipCode.getLatitude(), startZipCode.getLongitude());
            List<String> endStops = ClosestStopFinder.findStops(endZipCode.getLatitude(), endZipCode.getLongitude());
            
            for (int i = 0; i < startStops.size(); i++) {
                for (int j = 0; j < endStops.size(); j++) {
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


    public static List<Route> findQuickestRoute(List<Route> allRoutes) {

        //Go trough every possible route and find the quickest one to add to the list
        //If some of the routes have the same travel time, add them all to the list
        
        List<Route> quickest = new ArrayList<Route>();
        
        int currentTravelTime;

        if (allRoutes.isEmpty()) {
            System.out.println("There is not a bus route connecting the two specified zip codes. We suggest seeking alternative transportation options.");
            return null;
        }else{
            currentTravelTime = allRoutes.get(0).getTravelTime();
            for (Route route : allRoutes) {
                if (route.getTravelTime() < currentTravelTime) {
                    quickest.clear();
                    quickest.add(route);
                    currentTravelTime = route.getTravelTime();
                }else if(route.getTravelTime() == currentTravelTime){
                    quickest.add(route);
                }
            }
            return quickest;
        }
    }
    // public static void main(String[] args){
    //     // List<String> startStops = new ArrayList<>(Arrays.asList("2578420"));
    //     // List<String> endStops = new ArrayList<>(Arrays.asList("2578423"));
    //     ZipCode start = new ZipCode("6217la");
    //     ZipCode end = new ZipCode("6211gl");
    //     List<Route> allRoutes = findAllRoutes(start,end);
    //     List<Route> fastest = findQuickestRoute(allRoutes);
    //     for(Route route: fastest){
    //         System.out.println("Route id: " + route.routeId);
    //         System.out.println("Travel time: " + route.travelTime + " seconds");
    //         System.out.println("Distance: " + route.distance + "m");
    //         System.out.println(route.startStopId);
    //         System.out.println(route.endStopId);
    //         System.out.println(route.routeShortName);
    //         System.out.println("-----------------------------");
    //     }
    // }
}
