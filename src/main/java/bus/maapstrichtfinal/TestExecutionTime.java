package bus.maapstrichtfinal;

import java.util.List;

import bus.maapstrichtfinal.Calculations.BusRouteShape;
import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.ZipCode.ZipCode;
import javafx.geometry.Point2D;

public class TestExecutionTime {
    public static void main(String[] args) {
        // Test data
        ZipCode startZipCode = new ZipCode("6225ee");
        ZipCode endZipCode = new ZipCode("6218tl");

        // Test findFastestRoutes method
        long startTime = System.nanoTime();
        List<Route> fastestRoutes = Route.findFastestRoute(startZipCode, endZipCode);
        long endTime = System.nanoTime();
        System.out.println("findFastestRoutes execution time: " + (endTime - startTime) + " nanoseconds");

        // // Test findCoordsOfAllRoutePoints method
        // long startTime1 = System.nanoTime();
        // for(int i = 0; i<fastestRoutes.size(); i++){
        // List<Point2D> allPointsCoords =
        // BusRouteShape.findCoordsOfAllDirectRoutePoints(fastestRoutes.get(i));
        // }
        // long endTime1 = System.nanoTime();
        // System.out.println("findCoordsOfAllRoutePoints execution time: " + (endTime1
        // - startTime1) + " nanoseconds");

        // long allTime = (endTime - startTime) + (endTime1 - startTime1);
        // System.out.println("All time: " + allTime + "nanoseconds");

    }
}
