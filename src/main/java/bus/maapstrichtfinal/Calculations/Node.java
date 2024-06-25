package bus.maapstrichtfinal.Calculations;

import java.sql.*;
import java.util.*;

import bus.maapstrichtfinal.ZipCode.ZipCode;

public class Node {
    private static final double radius = 0.5;

    private BusStop stop;
    private double latitude;
    private double longitude;
    private Node parent;
    private int g; // weight in time from start zip code to the node
    private int h; // weight in time from the node to end zip code
    private int f; // g + h
    private boolean arrivedByBus;
    private String busRoute; // Added to store the bus route taken

    public Node(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.g = 0;
        this.h = 0;
        this.f = 0;
        this.arrivedByBus = false;
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
        return busRoute;
    }

    public void setBusRoute(String busRoute) {
        this.busRoute = busRoute;
    }

    public static int calculateWalkingTimeG(Node parent, double latitude, double longitude) {
        if (parent == null)
            return Integer.MAX_VALUE; // Added null check
        int timeInSeconds = (int) (60 * 60 * (ClosestStopFinder.calculateDistance(parent.getLatitude(),
                parent.getLongitude(), latitude, longitude) / 5));
        return timeInSeconds;
    }

    public static int calculateBusTimeG(Node parent, BusStop busStop) {
        if (parent == null || busStop == null)
            return Integer.MAX_VALUE; // Added null check
        int timeInSeconds = (int) (60 * 60 * (ClosestStopFinder.calculateDistance(parent.getLatitude(),
                parent.getLongitude(), busStop.getLatitude(), busStop.getLongitude()) / 30));
        return timeInSeconds;
    }

    public static int calculateTimeH(double latitude, double longitude, ZipCode end) {
        if (end == null)
            return Integer.MAX_VALUE; // Added null check
        double distance = ClosestStopFinder.calculateDistance(latitude, longitude, end.getLatitude(),
                end.getLongitude());
        int h = (int) (((distance / 20) * 60 * 60));
        return h;
    }

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

    public static List<BusStop> findConnectedStops(BusStop busStop, String currentTime) {
        String query = "SELECT next_stop.stop_id, stops.stop_lat, stops.stop_lon, stops.stop_name, " +
                "GROUP_CONCAT(DISTINCT routes.route_id ORDER BY routes.route_id ASC SEPARATOR ',') AS route_ids, " +
                "next_stop.arrival_time " +
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
                    String arrivalTime = rs.getString("arrival_time");

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

    public static String getBusNameBetweenStops(BusStop startStop, BusStop endStop) {
        Route route = Route.findRoutesBetweenStops(startStop.getStopId(), endStop.getStopId());
        if (route == null)
            return null;
        else {
            return route.getRouteShortName();
        }
    }

    public static Node createGraph(ZipCode startZip, ZipCode endZip, String currentTime) throws SQLException {
        if (startZip == null || endZip == null)
            return null; // Added null check

        PriorityQueue<Node> unvisited = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        Map<String, Node> allNodes = new HashMap<>();
        List<Node> visited = new ArrayList<>();

        Node start = new Node(startZip.getLatitude(), startZip.getLongitude());
        Node end = new Node(endZip.getLatitude(), endZip.getLongitude());

        start.setH(calculateTimeH(start.getLatitude(), start.getLongitude(), endZip));
        start.updateF();

        unvisited.add(start);
        allNodes.put(start.getLatitude() + "," + start.getLongitude(), start);

        while (!unvisited.isEmpty()) {
            Node current = unvisited.poll();
            if (current == null)
                continue; // Added null check

            if (current.getParent() != null && current.getParent().getBusStop() == null) {
                current.setArrivedByBus(false);
            }

            double distanceToEnd = ClosestStopFinder.calculateDistance(current.getLatitude(), current.getLongitude(),
                    end.getLatitude(), end.getLongitude());

            if (distanceToEnd < radius) {
                end.setParent(current);
                end.setG(current.getG() + (int) (60 * 60 * distanceToEnd / 5));
                return end; // Return the end node immediately if found
            }

            List<BusStop> stopsInRadius = ClosestStopFinder.findClosestStops(current.getLatitude(),
                    current.getLongitude(), radius);
            List<BusStop> connectedStops = current.getBusStop() != null
                    ? findConnectedStops(current.getBusStop(), currentTime)
                    : new ArrayList<>();

            List<BusStop> allStopToCheck = new ArrayList<>();
            allStopToCheck.addAll(stopsInRadius);
            allStopToCheck.addAll(connectedStops);

            for (BusStop stop : allStopToCheck) {
                if (stop == current.getBusStop() || visited.contains(getCorrespondingNodeForBusStop(stop, visited))) {
                    continue;
                }

                Node childNode = allNodes.get(stop.getLatitude() + "," + stop.getLongitude());
                if (childNode == null) {
                    childNode = new Node(stop.getLatitude(), stop.getLongitude());
                    childNode.setBusStop(stop);
                    allNodes.put(childNode.getLatitude() + "," + childNode.getLongitude(), childNode);
                }

                int busTime = calculateBusTimeG(current, stop);
                int walkingTime = calculateWalkingTimeG(current, stop.getLatitude(), stop.getLongitude());
                int minTime = Math.min(busTime, walkingTime);

                if ((connectedStops.contains(stop) && (current.getG() + minTime) < childNode.getG())
                        && childNode.getG() != 0) {
                    if (minTime == busTime) {
                        childNode.setArrivedByBus(true);
                        // String busName = getBusNameBetweenStops(current.getBusStop(), stop);
                        // childNode.setBusRoute(busName);
                    } else {
                        childNode.setArrivedByBus(false);
                    }
                    childNode.setG(current.getG() + minTime);
                    childNode.setParent(current);
                } else if (current.getG() + walkingTime < childNode.getG() || childNode.getG() == 0) {
                    childNode.setG(current.getG() + walkingTime);
                    childNode.setParent(current);
                }

                childNode.setH(calculateTimeH(childNode.getLatitude(), childNode.getLongitude(), endZip));
                childNode.updateF();

                if (!unvisited.contains(childNode)) {
                    unvisited.add(childNode);
                }
            }

            visited.add(current);
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

    public static void printPath(List<Node> path) {
        if (path != null && !path.isEmpty()) {
            System.out.println("path found");
            for (int i = 0; i < path.size(); i++) {
                if (i == 0 && i + 1 == path.size() - 1) {
                    System.out.println(
                            "Walk from " + path.get(i).getLatitude() + " " + path.get(i).getLongitude() + " to "
                                    + path.get(i + 1).getLatitude() + " " + path.get(i + 1).getLongitude());
                } else if (i == 0) {
                    System.out.println("Walk from " + path.get(i).getLatitude() + " " + path.get(i).getLongitude()
                            + " to " + path.get(i + 1).getBusStop().getName());
                } else if (i != 0 && i != path.size() - 1) {
                    if (i == 1) {
                        continue;
                    }
                    BusStop stop1 = path.get(i - 1).getBusStop();
                    BusStop stop2 = path.get(i).getBusStop();
                    if (path.get(i).isArrivedByBus()) {
                        String busName = getBusNameBetweenStops(stop1, stop2);
                        System.out
                                .println("Take bus " + busName + " from " + stop1.getName() + " to " + stop2.getName());

                    } else {
                        System.out.println("Walk from " + stop1.getName() + " to " + stop2.getName());
                    }

                } else {
                    System.out.println("Walk from " + path.get(i - 1).getBusStop().getName() + " to "
                            + path.get(i).getLatitude() + " " + path.get(i).getLongitude());
                }
            }

        } else {
            System.out.println("no path");
        }

    }

    public static List<Route> findRoutes(List<Node> path) {
        List<Route> routes = new ArrayList<>();
        for (int i = 0; i < path.size(); i++) {
            // maybe null cases

            if (path.get(i).getBusStop() != null && path.get(i + 1).getBusStop() != null) {
                Route route = Route.findRoutesBetweenStops(path.get(i).getBusStop().getStopId(),
                        path.get(i + 1).getBusStop().getStopId());
                routes.add(route);
            }

        }
        return routes;
    }
}