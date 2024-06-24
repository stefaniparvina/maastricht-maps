package bus.maapstrichtfinal.Calculations;

import java.util.ArrayList;
import java.util.List;

public class ClosestStopFinder {

    // Method to find the closest stops from a given location, within a given radius
    public static List<BusStop> findClosestStops(double userLat, double userLon, double maxDistance) {
        List<BusStop> closestStops = new ArrayList<>();

        // All bus stops in Maastricht
        List<BusStop> busStops = BusStop.getAllBusStops();

        // Iterate over the list of bus stops
        for (BusStop busStop : busStops) {
            double stopLat = busStop.getLatitude();
            double stopLon = busStop.getLongitude();
            // Calculate the distance between the user's location and the bus stop
            double distance = calculateDistance(userLat, userLon, stopLat, stopLon);

            // If the distance is within the maximum distance, add the bus stop to the list of closest stops
            if (distance <= maxDistance) {
                closestStops.add(busStop);
            }
        }

        return closestStops;
    }

    // Method to calculate the distance between two geographical coordinates using the Haversine formula
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // Earth's radius in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c; // Distance in kilometers
    }
}


// Main method for testing purposes
    /*public static void main(String[] args) {
        try {
            List<String> closestStops1 = findStops(50.85, 5.7);
            System.out.println(closestStops1);
            List<String> closestStops2 = findStops(50.86, 5.8);
            System.out.println(closestStops2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/