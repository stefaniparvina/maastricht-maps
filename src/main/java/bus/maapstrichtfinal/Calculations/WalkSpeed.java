package bus.maapstrichtfinal.Calculations;

public class WalkSpeed {
    // this is the average speed in km/h
    private final static double AVERAGE_WALKING_SPEED_KM_PER_HOUR = 5.0;

    public static String calculateWalkingTime(String start, String end) {
        double distanceKM = Distance.calculateDistance(start, end);
        double timeHours = distanceKM / AVERAGE_WALKING_SPEED_KM_PER_HOUR; 

        int minutes = (int) (timeHours * 60); 
        int seconds = (int) ((timeHours * 3600) % 60); 

        return minutes + " minutes " + seconds + " seconds";
    }
}