package bus.TimeExecution;

import java.util.List;

import bus.maapstrichtfinal.Calculations.BusRouteShape;
import bus.maapstrichtfinal.Calculations.Route;

public class TestBusRouteShapeExecutionTime {
    public static void main(String[] args) {
        // Example route for testing
        Route route = new Route(1, "RouteName", "start_stop_id", "end_stop_id", 600, 10);

        // Test findShapeDetails method
        long startTime = System.nanoTime();
        int[] shapeDetails = BusRouteShape.findShapeDetails(route);
        long endTime = System.nanoTime();
        System.out.println("findShapeDetails execution time: " + (endTime - startTime) + " nanoseconds");

        // Test findCoordsOfAllRoutePoints method
        long startTime1 = System.nanoTime();
        List<double[]> allPointsCoords = BusRouteShape.findCoordsOfAllRoutePoints(route);
        long endTime1 = System.nanoTime();
        System.out.println("findCoordsOfAllRoutePoints execution time: " + (endTime1 - startTime1) + " nanoseconds");
        System.out.println("All time: " + ((endTime - startTime) + (endTime1 - startTime1)) + "nanoseconds");
    }
}
