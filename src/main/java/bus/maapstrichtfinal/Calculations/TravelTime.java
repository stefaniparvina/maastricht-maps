package bus.maapstrichtfinal.Calculations;

public class TravelTime {
    private static double AVERAGE_SPEED = 0.0;
    public static double distanceKM = 0.0;

    public static String calculateTravelTime(String start, String end, String method) {

        if(method.equalsIgnoreCase("walk")){
            AVERAGE_SPEED = 5.0;
        }
        else if (method.equalsIgnoreCase("bike")){
            AVERAGE_SPEED = 15.0;
        }
        // Calculate the distance between start and end points in kilometers
        distanceKM = Distance.calculateDistance(start, end);

        // Calculate the time taken to cycle the distance in hours
        double timeHours = distanceKM /AVERAGE_SPEED;

        // Convert the time from hours to minutes and seconds
        int minutes = (int) (timeHours * 60);
        int seconds = (int) ((timeHours * 3600) % 60);


        return minutes + " minutes, " + seconds + " seconds";
    }
}
