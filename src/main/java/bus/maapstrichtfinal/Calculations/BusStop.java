package bus.maapstrichtfinal.Calculations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusStop {
    private double latitude;
    private double longitude;
    private String stopId;
    private String name;
    private List<Integer> buses;


    // Static list to store all bus stops in Maastricht
    private static List<BusStop> allBusStops;

    public BusStop(double latitude, double longitude, String stopId, String name, List<Integer> buses) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.stopId = stopId;
        this.name = name;
        this.buses = buses;
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

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getBuses() {
        return buses;
    }

    public void setBuses(List<Integer> buses) {
        this.buses = buses;
    }



    // Method to find all bus stops in Maastricht and populate the static list
    public static void loadAllBusStops() {
        if (allBusStops != null) return; // If already loaded, do nothing

        allBusStops = new ArrayList<>();

        String query = "SELECT s.stop_id, s.stop_name, s.stop_lat, s.stop_lon, GROUP_CONCAT(DISTINCT r.route_id ORDER BY r.route_id ASC SEPARATOR ',') AS route_ids " +
                "FROM stops s JOIN stop_times st ON s.stop_id = st.stop_id JOIN trips t ON st.trip_id = t.trip_id JOIN routes r ON t.route_id = r.route_id " +
                "WHERE s.stop_name LIKE '%Maastricht%' GROUP BY s.stop_id, s.stop_name, s.stop_lat, s.stop_lon;";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String stopId = rs.getString("stop_id");
                String stopName = rs.getString("stop_name");
                double latitude = rs.getDouble("stop_lat");
                double longitude = rs.getDouble("stop_lon");
                String busesStr = rs.getString("route_ids");

                // Convert the route_ids string to a list of integers
                List<Integer> buses = new ArrayList<>();
                if (busesStr != null && !busesStr.isEmpty()) {
                    for (String bus : busesStr.split(",")) {
                        buses.add(Integer.parseInt(bus.trim()));
                    }
                }

                BusStop busStop = new BusStop(latitude, longitude, stopId, stopName, buses);
                allBusStops.add(busStop);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<BusStop> getAllBusStops() {
        if (allBusStops == null) {
            loadAllBusStops();
        }
        return allBusStops;
    }
}