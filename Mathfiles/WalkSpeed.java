package Mathfiles;

public class WalkSpeed {
    private final static int AVERAGE_WALKING_SPEED = 5;

    public static double calculateWalkingTime(String start, String end) {
        double distanceKM = Distance.calculateDistance(start, end);
        double distance = distanceKM * 1000;
        double timeMinutes = distance / (AVERAGE_WALKING_SPEED * 60);

        double roundedWalkTime = Math.round(timeMinutes * 100.0) / 100.0;
        return roundedWalkTime;
    }
}
