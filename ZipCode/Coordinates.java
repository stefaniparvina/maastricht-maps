package ZipCode;

public class Coordinates {

    public static double zipStartX, zipStartY, zipEndX, zipEndY;
    protected double startLatitudeRad, startLongitudeRad, finishLatitudeRad, finishLongitudeRad;

    public static void extractCoordinates(String start, String end){

        ZipCode zipStart = new ZipCode(start);
        ZipCode zipEnd = new ZipCode(end);

        zipStartY = zipStart.getLatitude();
        zipStartX = zipStart.getLongitude();
        zipEndY = zipEnd.getLatitude();
        zipEndX = zipEnd.getLongitude();
    }

    protected void convertCoordinatesToRadians(String start, String end) {

        startLatitudeRad = Math.toRadians(zipStartY);
        startLongitudeRad = Math.toRadians(zipStartX);
        finishLatitudeRad = Math.toRadians(zipEndY);
        finishLongitudeRad = Math.toRadians(zipEndX);
    }

    public static int startLineX(){

        int startLineX = (int) Math.round(((zipStartX-5.638854)/0.124086)*396);
        return startLineX;

    }

    public static int startLineY(){

        int startLineY = (int) Math.round(((50.911792-zipStartY)/0.107978)*545);
        return startLineY;

    }

    public static int endLineX(){

        int endLineX = (int) Math.round(((zipEndX-5.638854)/0.124086)*396);
        return endLineX;

    }

    public static int endtLineY(){

        int endLineY = (int) Math.round(((50.911792-zipEndY)/0.107978)*545);
        return endLineY;

    }
}
