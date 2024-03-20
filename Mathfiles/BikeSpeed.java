package Mathfiles;

public class BikeSpeed {
    private final static int AVERAGE_CYCLING_SPEED = 15;

    public static int calculateCyclingTime(String start, String end) {
        double distanceKM = Distance.calculateDistance(start, end);
        double distance = distanceKM * 1000;
        double timeMinutes = distance / (AVERAGE_CYCLING_SPEED * 60);

        return (int)timeMinutes;
    }
}
