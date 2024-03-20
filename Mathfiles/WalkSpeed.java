package Mathfiles;

public class WalkSpeed {
    // this is the average speed in kh/m
    private final static double AVERAGE_WALKING_SPEED = 0.08;

    public static double calculateWalkingTime(String start, String end) {
        double distanceKM = Distance.calculateDistance(start, end);
        double timeMinutes = distanceKM / AVERAGE_WALKING_SPEED;

        double roundedWalkTime = Math.round(timeMinutes * 100.0) / 100.0;
        return roundedWalkTime;
    }
}
