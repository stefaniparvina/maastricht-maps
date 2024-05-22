package bus.maapstrichtfinal.Calculations;

public class BikeSpeed {
    private final static double AVERAGE_CYCLING_SPEED = 15.0;

    public static String calculateCyclingTime(String start, String end) {

        // Calculate the distance between start and end points in kilometers
        double distanceKM = Distance.calculateDistance(start, end);

        // Calculate the time taken to cycle the distance in hours
        double timeHours = distanceKM / AVERAGE_CYCLING_SPEED;

        // Convert the time from hours to minutes and seconds
        int minutes = (int) (timeHours * 60);
        int seconds = (int) ((timeHours * 3600) % 60);


        return minutes + " minutes, " + seconds + " seconds";
    }
}
