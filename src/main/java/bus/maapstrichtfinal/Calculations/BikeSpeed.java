package bus.maapstrichtfinal.Calculations;

public class BikeSpeed {
    // this is the average speed in kh/m
    private final static double AVERAGE_CYCLING_SPEED = 15.0;

    public static String calculateCyclingTime(String start, String end) {
        double distanceKM = Distance.calculateDistance(start, end);
        double timeHours = distanceKM / AVERAGE_CYCLING_SPEED;

        int minutes = (int) (timeHours * 60); 
        int seconds = (int) ((timeHours * 3600) % 60);

        return minutes + " minutes " + seconds + " seconds";
    }
}