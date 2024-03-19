/*
 * This class is responsible for calculating the distance in kilometers and meters between two coordinates
 */

public class CalculateDistance{
    private static final double EARTH_RADIUS = 6371;
    public static void main(String[] args) {
        System.out.println(calculateDistance(50.85523285,5.692237193, 50.82412246, 5.70572565));
    }
    public static String calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);
    
        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);
        double distanceInKm = Math.sqrt(x * x + y * y) * EARTH_RADIUS;
    
        double distanceInMeters = distanceInKm * 1000;
        
        String distances = "Distance in Km: " + distanceInKm + "\nDistance in meters: " + distanceInMeters;
        return distances;
    }
}