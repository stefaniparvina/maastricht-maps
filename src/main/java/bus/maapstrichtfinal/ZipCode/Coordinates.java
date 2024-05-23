package bus.maapstrichtfinal.ZipCode;

public class Coordinates {

    // Variables to store coordinates of start and end zip codes
    public static double zipStartX, zipStartY, zipEndX, zipEndY;
    // Variables to store coordinates in radians
    protected static double startLatitudeRad;
    protected static double startLongitudeRad;
    protected static double finishLatitudeRad;
    protected static double finishLongitudeRad;

    // Method to extract coordinates from zip code strings
    public static void extractCoordinates(String start, String end) {

        // Creating ZipCode objects for start and end zip codes
        ZipCode zipStart = new ZipCode(start);
        ZipCode zipEnd = new ZipCode(end);

        // Storing latitude and longitude of start and end zip codes
        zipStartY = zipStart.getLatitude();
        zipStartX = zipStart.getLongitude();
        zipEndY = zipEnd.getLatitude();
        zipEndX = zipEnd.getLongitude();
    }

    // Method to convert coordinates to radians
    protected static void convertCoordinatesToRadians(String start, String end) {

        zipStartY = new ZipCode(start).getLatitude();
        zipStartX = new ZipCode(start).getLongitude();

        zipEndY = new ZipCode(end).getLatitude();
        zipEndX = new ZipCode(end).getLongitude();

        // Converting latitude and longitude of start and end zip codes to radians
        startLatitudeRad = Math.toRadians(zipStartY);
        startLongitudeRad = Math.toRadians(zipStartX);

        finishLatitudeRad = Math.toRadians(zipEndY);
        finishLongitudeRad = Math.toRadians(zipEndX);
    }
}
