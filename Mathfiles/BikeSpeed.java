package Mathfiles;

public class BikeSpeed {
    private final static int AVERAGE_CYCLING_SPEED = 15;

    public static double calculateCyclingTime(String start, String end) {
        double distanceKM = Distance.calculateDistance(start, end);
        double distance = distanceKM * 1000;
        double timeMinutes = distance / (AVERAGE_CYCLING_SPEED * 60);

        double roundedBikeTime = Math.round(timeMinutes * 100.0) / 100.0;
        return roundedBikeTime;
    }
}
