package Mathfiles;

import ZipCode.ZipCode;

public class Distance {
    private static final double EARTH_RADIUS = 6371; // Earth's radius in kilometers
    private double startLatitudeRad;
    private double startLongitudeRad;
    private double finishLatitudeRad;
    private double finishLongitudeRad;

    public Distance(ZipCode start, ZipCode end) {

        convertCoordinatesToRadians(start, end);
    }

    private void convertCoordinatesToRadians(ZipCode start, ZipCode end) {

        startLatitudeRad = Math.toRadians(start.getLatitude());
        startLongitudeRad = Math.toRadians(start.getLongitude());
        finishLatitudeRad = Math.toRadians(end.getLatitude());
        finishLongitudeRad = Math.toRadians(end.getLongitude());
    }

    public double calculateDistance() {

        double deltaX = (finishLongitudeRad - startLongitudeRad) * Math.cos((startLatitudeRad + finishLatitudeRad) / 2);
        double deltaY = (finishLatitudeRad - startLatitudeRad);

        double distanceInKm = Math.sqrt(deltaX * deltaX + deltaY * deltaY) * EARTH_RADIUS;
        return distanceInKm;
    }

    public static void main(String[] args) {

        ZipCode start = new ZipCode("6216RD");
        ZipCode end = new ZipCode("6228CN");

        Distance distance = new Distance(start, end);
        double calculatedDistance = distance.calculateDistance();

        System.out.println("Distance in Km: " + calculatedDistance);
    }
}
