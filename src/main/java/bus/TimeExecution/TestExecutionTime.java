package bus.TimeExecution;

import java.util.List;

import bus.maapstrichtfinal.Calculations.BusRouteShape;
import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.ZipCode.ZipCode;

public class TestExecutionTime {
    public static void main(String[] args) {
        // Test data
        String startStopId = "some_start_stop_id";
        String endStopId = "some_end_stop_id";
        ZipCode startZipCode = new ZipCode("6211lc");
        ZipCode endZipCode = new ZipCode("6229en");

        // Test findFastestRoutes method
        long startTime = System.nanoTime();
        Route fastestRoutes = Route.findFastestRoute(startZipCode, endZipCode);
        long endTime = System.nanoTime();
        System.out.println("findFastestRoutes execution time: " + (endTime - startTime) + " nanoseconds");

        // Test findCoordsOfAllRoutePoints method
        long startTime1 = System.nanoTime();
        List<double[]> allPointsCoords = BusRouteShape.findCoordsOfAllRoutePoints(fastestRoutes);
        long endTime1 = System.nanoTime();
        System.out.println("findCoordsOfAllRoutePoints execution time: " + (endTime1 - startTime1) + " nanoseconds");

        long allTime = (endTime - startTime) + (endTime1 - startTime1);
        System.out.println("All time: " + allTime + "nanoseconds");

    }
}
