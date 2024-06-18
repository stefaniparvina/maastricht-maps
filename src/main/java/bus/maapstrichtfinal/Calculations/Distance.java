package bus.maapstrichtfinal.Calculations;

import bus.maapstrichtfinal.ZipCode.*;

public class Distance extends Coordinates {
    private static final double EARTH_RADIUS = 6371;

    // Method to calculate the distance between two points using their coordinates
    public static double calculateDistance(String start, String end) {
        // Convert start and end coordinates to radians
        convertCoordinatesToRadians(start, end);
        // Calculate the differences in longitude and latitude
        double deltaX = (finishLongitudeRad - startLongitudeRad) * Math.cos((startLatitudeRad + finishLatitudeRad) / 2);
        double deltaY = (finishLatitudeRad - startLatitudeRad);

        // Calculate the distance using the Haversine formula
        double distanceKM = Math.sqrt(deltaX * deltaX + deltaY * deltaY) * EARTH_RADIUS;

        // Round the distance to two decimal places
        double roundedDistanceKM = Math.round(distanceKM * 100.0) / 100.0;

        return roundedDistanceKM; // Return the rounded distance in kilometers
    }

    public static double calculateDistance(String start, double lat, double lon) {
        ZipCode zipStart = new ZipCode(start);
        double zipStartY = Math.toRadians(zipStart.getLatitude());
        double zipStartX = Math.toRadians(zipStart.getLongitude());
        lat = Math.toRadians(lat);
        lon = Math.toRadians(lon);

        // Calculate the differences in longitude and latitude
        double deltaX = (lon - zipStartX) * Math.cos((zipStartY + lat) / 2);
        double deltaY = (lat - zipStartY);

        // Calculate the distance using the Haversine formula
        double a = Math.sin(deltaY / 2) * Math.sin(deltaY / 2) +
                Math.cos(zipStartY) * Math.cos(lat) *
                        Math.sin(deltaX / 2) * Math.sin(deltaX / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distanceKM = EARTH_RADIUS * c;

        // Round the distance to two decimal places
        double roundedDistanceKM = Math.round(distanceKM * 100.0) / 100.0;

        return roundedDistanceKM; // Return the rounded distance in kilometers
    }

}
