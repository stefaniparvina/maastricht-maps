package bus.maapstrichtfinal.Calculations;

import org.jetbrains.annotations.NotNull;

public class TravelTime {
    private static double AVERAGE_SPEED = 5.0;
    public static double distanceKM = 0.0;

    public static String calculateTravelTime(String start, String end, String method) {
        double AVERAGE_SPEED;
        if (method.equalsIgnoreCase("walk")) {
            AVERAGE_SPEED = 5.0;
        } else if (method.equalsIgnoreCase("bike")) {
            AVERAGE_SPEED = 15.0;
        } else {
            throw new IllegalArgumentException("Unsupported travel method: " + method);
        }

        // Calculate the distance between start and end points in kilometers
        distanceKM = Distance.calculateDistance(start, end);

        // Calculate the time taken to cover the distance in hours
        String timeTxt = getTimeTxt(distanceKM, AVERAGE_SPEED);

        return timeTxt;
    }

    @NotNull
    private static String getTimeTxt(double distanceKM, double AVERAGE_SPEED) {
        double timeHours = distanceKM / AVERAGE_SPEED;

        // Convert the time from hours to total minutes
        int totalMinutes = (int) (timeHours * 60);
        int seconds = (int) ((timeHours * 3600) % 60);

        // Calculate hours and remaining minutes
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        String timeTxt;
        if (hours > 0) {
            timeTxt = hours + " hour" + (hours > 1 ? "s " : " ") + minutes + " minute" + (minutes != 1 ? "s" : "");
        } else {
            timeTxt = minutes + " minute" + (minutes != 1 ? "s" : "") + ", " + seconds + " second" + (seconds != 1 ? "s" : "");
        }
        return timeTxt;
    }

    public static double getWalkingTimeBusStop(String startStopId, String endStopId){

        double distance = Distance.calculateDistanceFromZipToStop(startStopId) + Distance.calculateDistanceFromZipToStop(endStopId);

        double time =  (distance / AVERAGE_SPEED) * 3600;

        return time;
    }

}
