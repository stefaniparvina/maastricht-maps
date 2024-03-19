package Mathfiles;

import UIfiles.Map;
import ZipCode.*;

public class Distance extends Coordinates {
    private static final double EARTH_RADIUS = 6371; // Earth's radius in kilometers


    protected double calculateDistance(String start, String end) {

        convertCoordinatesToRadians(start, end);
        double deltaX = (finishLongitudeRad - startLongitudeRad) * Math.cos((startLatitudeRad + finishLatitudeRad) / 2);
        double deltaY = (finishLatitudeRad - startLatitudeRad);
        double distanceKM = Math.sqrt(deltaX * deltaX + deltaY * deltaY) * EARTH_RADIUS;


        return distanceKM;
    }

}
