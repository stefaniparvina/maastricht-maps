package bus.maapstrichtfinal.Calculations;

import java.sql.*;
import java.util.*;

import bus.maapstrichtfinal.ZipCode.ZipCode;

public class Node {
    private static final int walkingSpeed = 5;
    private static final double radius = 0.5;
    private static final int busSpeed = 20;

    private BusStop stop;
    private double latitude;
    private double longitude;
    private Node parent;
    private int g; // weight in time from start zip code to the node
    private int h; // weight in time from the node to end zip code
    private int f; // g + h
    private boolean arrivedByBus;
    private String busRouteShortName; // Added to store the bus route taken
    private int chosenRouteToThatStop;

    public Node(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.g = 0;
        this.h = 0;
        this.f = 0;
        this.arrivedByBus = false;
        this.chosenRouteToThatStop = 0;
    }

    public BusStop getBusStop() {
        return stop;
    }

    public void setBusStop(BusStop stop) {
        this.stop = stop;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
        updateF();
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
        updateF();
    }

    public int getF() {
        return f;
    }

    private void updateF() {
        this.f = this.g + this.h;
    }

    public boolean isArrivedByBus() {
        return arrivedByBus;
    }

    public void setArrivedByBus(boolean arrivedByBus) {
        this.arrivedByBus = arrivedByBus;
    }

    public String getBusRoute() {
        return busRouteShortName;
    }

    public void setBusRoute(String busRouteShortName) {
        this.busRouteShortName = busRouteShortName;
    }

    public int getChosenRouteToBusStop() {
        return chosenRouteToThatStop;
    }

    public void setChosenRouteToBusStop(int chosenRouteToThatStop) {
        this.chosenRouteToThatStop = chosenRouteToThatStop;
    }

    //Calculate walking time for g in seconds
    public static int calculateWalkingTimeG(Node parent, double latitude, double longitude) {
        if (parent == null)
            return Integer.MAX_VALUE;
        int timeInSeconds = (int) (60 * 60 * (ClosestStopFinderAstar.calculateDistance(parent.getLatitude(),
                parent.getLongitude(), latitude, longitude) / walkingSpeed));
        return timeInSeconds;
    }

    //Calculate bus time for g in seconds
    public static int calculateBusTimeG(Node parent, BusStop busStop) {
        if (parent == null || busStop == null)
            return Integer.MAX_VALUE;
        int timeInSeconds = (int) (60 * 60 * (ClosestStopFinderAstar.calculateDistance(parent.getLatitude(),
                parent.getLongitude(), busStop.getLatitude(), busStop.getLongitude()) / busSpeed ));
        return timeInSeconds;
    }

    //Calculate H time in seconds using bus speed
    public static int calculateTimeH(double latitude, double longitude, ZipCode end) {
        if (end == null)
            return Integer.MAX_VALUE;
        double distance = ClosestStopFinderAstar.calculateDistance(latitude, longitude, end.getLatitude(),
                end.getLongitude());
        int h = (int) (((distance / busSpeed) * 60 * 60));
        return h;
    }

    //Method to find if there is an existing node for a specified bus stop. If there is one, return it
    public static Node getCorrespondingNodeForBusStop(BusStop stop, List<Node> nodes) {
        if (stop == null || nodes == null)
            return null; // Added null check
        for (Node node : nodes) {
            if (node.getLatitude() == stop.getLatitude() && node.getLongitude() == stop.getLongitude()) {
                return node;
            }
        }
        return null;
    }

    //Find the first bus stops that are next in the sequence of the routes that go trough the specified bus stop and use current time
    public static List<BusStop> findConnectedStops(BusStop busStop, String currentTime) {
        String query = "SELECT next_stop.stop_id, stops.stop_lat, stops.stop_lon, stops.stop_name, " +
                "GROUP_CONCAT(DISTINCT routes.route_id ORDER BY routes.route_id ASC SEPARATOR ',') AS route_ids " +
                "FROM stop_times AS current_stop " +
                "JOIN stop_times AS next_stop ON current_stop.trip_id = next_stop.trip_id " +
                "JOIN stops ON stops.stop_id = next_stop.stop_id " +
                "JOIN trips ON next_stop.trip_id = trips.trip_id " +
                "JOIN routes ON trips.route_id = routes.route_id " +
                "WHERE current_stop.stop_id = ? " +
                "AND current_stop.departure_time >= ? " +
                "AND next_stop.stop_sequence = ( " +
                "SELECT MIN(stop_sequence) FROM stop_times WHERE trip_id = current_stop.trip_id " +
                "AND stop_sequence > current_stop.stop_sequence) " +
                "GROUP BY next_stop.stop_id, stops.stop_lat, stops.stop_lon, stops.stop_name, next_stop.arrival_time;";

        List<BusStop> stops = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, busStop.getStopId());
            pstmt.setString(2, currentTime);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String stopId = rs.getString("stop_id");
                    double latitude = rs.getDouble("stop_lat");
                    double longitude = rs.getDouble("stop_lon");
                    String stopName = rs.getString("stop_name");
                    String routeIdsStr = rs.getString("route_ids");

                    // Convert the route_ids string to a list of integers
                    List<Integer> routeIds = new ArrayList<>();
                    if (routeIdsStr != null && !routeIdsStr.isEmpty()) {
                        for (String routeId : routeIdsStr.split(",")) {
                            routeIds.add(Integer.parseInt(routeId.trim()));
                        }
                    }

                    BusStop connectedStop = new BusStop(latitude, longitude, stopId, stopName, routeIds);
                    stops.add(connectedStop);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stops;
    }

    //Find the route short name from database using route id
    public static String getRouteShortName(int routeId) {
        String query = "SELECT DISTINCT route_short_name FROM routes WHERE route_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, routeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("route_short_name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no route is found
    }

    //Method that picks best route to use between bus stops. Checks if one of the fastest available routes in the list is the same as the
    //Route used to travel to the previous bus stop. If it is - it takes it, otherwise it takes the first route from the list
    //If no valid route is found, return -1
    public static int getCorrectRouteBetweenStops(Node from, Node to) {
        if (from.getBusStop() != null && to.getBusStop() != null) {
            List<RouteAstar> routes = RouteAstar.findFastestRoutesBetweenStops(from.getBusStop().getStopId(), to.getBusStop().getStopId());
            if (routes != null && !routes.isEmpty()) {
                for (RouteAstar route : routes) {
                    if (route.getRouteId() == from.getChosenRouteToBusStop()) {
                        return route.getRouteId();
                    }
                }
                return routes.get(0).getRouteId(); // return the first route if no matching route is found
            }
        }
        return -1; // Return -1 if no valid route is found
    }

    //Create graph for A* algorithm
    public static Node createGraph(ZipCode startZip, ZipCode endZip, String currentTime) throws SQLException {
        if (startZip == null || endZip == null)
            return null; // Added null check


        //Create a priority queue that has the unvisited nodes and orders them in asc order based on f
        PriorityQueue<Node> unvisited = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        Map<String, Node> allNodes = new HashMap<>(); //Hashmap to save the created nodes and ensure we have only 1 node for each bus stop
        List<Node> visited = new ArrayList<>(); //A list that saves the visited nodes

        // Create node for the start and end zip codes
        Node start = new Node(startZip.getLatitude(), startZip.getLongitude());
        Node end = new Node(endZip.getLatitude(), endZip.getLongitude());

        //Set values for start node and add to unvisited and allNodes hashmap
        start.setH(calculateTimeH(start.getLatitude(), start.getLongitude(), endZip));
        start.updateF();
        unvisited.add(start);
        allNodes.put(start.getLatitude() + "," + start.getLongitude(), start);

        while (!unvisited.isEmpty()) {
            Node current = unvisited.poll(); //take first node from unvisited
            if (current == null)
                continue;

            // if the parent node is not a bus stop, set travel type to current node to be walking
            if (current.getParent() != null && current.getParent().getBusStop() == null) {
                current.setArrivedByBus(false);
            }

            double distanceToEnd = ClosestStopFinderAstar.calculateDistance(current.getLatitude(), current.getLongitude(),
                    end.getLatitude(), end.getLongitude());


            //If the distance to end node is within walking distance, we have reached the end bus stop
            if (distanceToEnd < radius) {
                end.setParent(current);
                end.setG(current.getG() + (int) (60 * 60 * distanceToEnd / 5));
                return end; // Return the end node immediately if found
            }

            List<BusStop> stopsInRadius = ClosestStopFinderAstar.findClosestStops(current.getLatitude(),
                    current.getLongitude(), radius);

            List<BusStop> connectedStops;
            if (current.getBusStop() != null) {
                connectedStops = findConnectedStops(current.getBusStop(), currentTime);
            } else {
                connectedStops = new ArrayList<>();
            }

            List<BusStop> allStopToCheck = new ArrayList<>(); //The list with all bus stops that need to be checked for current node
            allStopToCheck.addAll(stopsInRadius);
            allStopToCheck.addAll(connectedStops);

            for (BusStop stop : allStopToCheck) {
                if (stop == current.getBusStop() || visited.contains(getCorrespondingNodeForBusStop(stop, visited))) {
                    continue;
                }

                //Check if child node exists already
                Node childNode = allNodes.get(stop.getLatitude() + "," + stop.getLongitude());
                if (childNode == null) {
                    childNode = new Node(stop.getLatitude(), stop.getLongitude());
                    childNode.setBusStop(stop);
                    allNodes.put(childNode.getLatitude() + "," + childNode.getLongitude(), childNode);
                }

                int busTime = calculateBusTimeG(current, stop);
                int walkingTime = calculateWalkingTimeG(current, stop.getLatitude(), stop.getLongitude());
                int minTime = Math.min(busTime, walkingTime); //the min g time

                //Ff child node has a mutual bus route with current node and min g < set g time for child node
                //check if it is fatser with bus or walk and update values
                //Otherwise set it walking and again update values
                if ((connectedStops.contains(stop) && (current.getG() + minTime) < childNode.getG())
                        && childNode.getG() != 0) {
                    if (minTime == busTime) {
                        childNode.setArrivedByBus(true);

                        // Update bus route
                        int routeID = getCorrectRouteBetweenStops(current, childNode);
                        childNode.setChosenRouteToBusStop(routeID);
                        childNode.setBusRoute(getRouteShortName(routeID));
                    } else {
                        childNode.setArrivedByBus(false);
                    }
                    childNode.setG(current.getG() + minTime);
                    childNode.setParent(current);
                } else if (current.getG() + walkingTime < childNode.getG() || childNode.getG() == 0) {
                    childNode.setG(current.getG() + walkingTime);
                    childNode.setParent(current);
                    childNode.setArrivedByBus(false);
                }

                childNode.setH(calculateTimeH(childNode.getLatitude(), childNode.getLongitude(), endZip));
                childNode.updateF();

                if (!unvisited.contains(childNode)) {
                    unvisited.add(childNode);
                }
            }

            visited.add(current); //Add current node to visited
        }

        return null; // If no path is found
    }

    // Method that goes backwards
    public static List<Node> reconstructPath(Node endNode) {
        List<Node> path = new ArrayList<>();
        Node current = endNode;
        while (current != null) {
            path.add(0, current);
            current = current.getParent();
        }
        return path;
    }

    //Print the path
    public static void printPath(List<Node> path, String startZipCode, String endZipCode) {
        if (path == null || path.size() < 2) {
            System.out.println("Invalid path");
            return;
        }

        System.out.println("Walk from " + startZipCode.toUpperCase() + " (" + path.get(0).getLatitude() + ", " + path.get(0).getLongitude() + ") to " + path.get(1).getBusStop().getName());

        for (int i = 2; i < path.size() - 1; i++) {
            if (path.get(i).isArrivedByBus()) {
                int routeId = path.get(i).getChosenRouteToBusStop();
                String busName = path.get(i).getBusRoute();
                if (routeId != -1 && busName != null) {
                    System.out.println("Take bus " + busName + " (" + routeId + ") from " + path.get(i - 1).getBusStop().getName() + " to " + path.get(i).getBusStop().getName());
                } else {
                    System.out.println("Error: Bus route not found between " + path.get(i - 1).getBusStop().getName() + " and " + path.get(i).getBusStop().getName());
                }
            } else {
                System.out.println("Walk from " + path.get(i - 1).getBusStop().getName() + " to " + path.get(i).getBusStop().getName());
            }
        }
        System.out.println("Walk from " + path.get(path.size() - 2).getBusStop().getName() + " to " + endZipCode.toUpperCase() + " (" + path.get(path.size() - 1).getLongitude() + ", " + path.get(path.size() - 1).getLatitude() + ")");
    }
}