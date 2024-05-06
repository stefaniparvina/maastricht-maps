package bus.maapstrichtfinal.Calculations;

public class BikeSpeed {
    // this is the average speed in kh/m
    private final static double AVERAGE_CYCLING_SPEED = 0.25;

    public static double calculateCyclingTime(String start, String end) {
        double distanceKM = Distance.calculateDistance(start, end);
        double timeMinutes = distanceKM / AVERAGE_CYCLING_SPEED;

        double roundedBikeTime = Math.round(timeMinutes * 100.0) / 100.0;
        return roundedBikeTime;
    }
}