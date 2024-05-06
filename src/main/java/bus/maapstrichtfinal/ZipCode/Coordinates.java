package bus.maapstrichtfinal.ZipCode;

public class Coordinates {

    public static double zipStartX, zipStartY, zipEndX, zipEndY;
    protected static double startLatitudeRad;
    protected static double startLongitudeRad;
    protected static double finishLatitudeRad;
    protected static double finishLongitudeRad;

    public static void extractCoordinates(String start, String end) {

        ZipCode zipStart = new ZipCode(start);
        ZipCode zipEnd = new ZipCode(end);

        zipStartY = zipStart.getLatitude();
        zipStartX = zipStart.getLongitude();
        zipEndY = zipEnd.getLatitude();
        zipEndX = zipEnd.getLongitude();
    }

    protected static void convertCoordinatesToRadians(String start, String end) {

        startLatitudeRad = Math.toRadians(zipStartY);
        startLongitudeRad = Math.toRadians(zipStartX);
        finishLatitudeRad = Math.toRadians(zipEndY);
        finishLongitudeRad = Math.toRadians(zipEndX);
    }
}