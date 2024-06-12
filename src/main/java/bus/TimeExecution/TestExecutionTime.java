package bus.TimeExecution;

import java.util.List;

import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.ZipCode.ZipCode;

public class TestExecutionTime {
    public static void main(String[] args) {
        // Test data
        String startStopId = "some_start_stop_id";
        String endStopId = "some_end_stop_id";
        ZipCode startZipCode = new ZipCode("6211lc");
        ZipCode endZipCode = new ZipCode("6229en");

        // Test findRoutesBetweenStops method
        long startTime = System.nanoTime();
        List<Route> routes = Route.findRoutesBetweenStops(startStopId, endStopId);
        long endTime = System.nanoTime();
        System.out.println("findRoutesBetweenStops execution time: " + (endTime - startTime) + " nanoseconds");

        // Test findAllRoutes method
        long startTime1 = System.nanoTime();
        List<Route> allRoutes = Route.findAllRoutes(startZipCode, endZipCode);
        long endTime1 = System.nanoTime();
        System.out.println("findAllRoutes execution time: " + (endTime1 - startTime1) + " nanoseconds");

        // Test findFastestRoutes method
        long startTime2 = System.nanoTime();
        List<Route> fastestRoutes = Route.findFastestRoutes(startZipCode, endZipCode);
        long endTime2 = System.nanoTime();
        System.out.println("findFastestRoutes execution time: " + (endTime2 - startTime2) + " nanoseconds");
        System.out.println("All times: " + ((endTime - startTime) + (endTime1 - startTime1) + (endTime2 - startTime2))
                + " nanoseconds");
    }
}
