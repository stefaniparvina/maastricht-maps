package bus.maapstrichtfinal.Calculations;

public class WalkSpeed {
    // Average walking speed in kilometers per hour
    private final static double AVERAGE_WALKING_SPEED_KM_PER_HOUR = 5.0;

    // Method to calculate walking time between two points
    public static String calculateWalkingTime(String start, String end) {

        // Calculate the distance between start and end points in kilometers
        double distanceKM = Distance.calculateDistance(start, end);

        // Calculate the time taken to walk the distance in hours
        double timeHours = distanceKM / AVERAGE_WALKING_SPEED_KM_PER_HOUR;

        // Convert the time from hours to minutes and seconds
        int minutes = (int) (timeHours * 60);
        int seconds = (int) ((timeHours * 3600) % 60);

        return minutes + " minutes, " + seconds + " seconds";
    }
}
