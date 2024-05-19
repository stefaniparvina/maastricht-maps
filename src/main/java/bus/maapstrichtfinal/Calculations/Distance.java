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
}
