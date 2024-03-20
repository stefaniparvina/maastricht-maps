package Calculations;

import UI.Map;
import ZipCode.*;

public class Distance extends Coordinates {
    private static final double EARTH_RADIUS = 6371; // Earth's radius in kilometers


    public static double calculateDistance(String start, String end) {

        convertCoordinatesToRadians(start, end);
        double deltaX = (finishLongitudeRad - startLongitudeRad) * Math.cos((startLatitudeRad + finishLatitudeRad) / 2);
        double deltaY = (finishLatitudeRad - startLatitudeRad);
        double distanceKM = Math.sqrt(deltaX * deltaX + deltaY * deltaY) * EARTH_RADIUS;

        double roundedDistanceKM = Math.round(distanceKM * 100.0) / 100.0;
        return roundedDistanceKM;
    }

}