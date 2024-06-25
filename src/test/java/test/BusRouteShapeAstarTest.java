package test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bus.maapstrichtfinal.Calculations.BusRouteShapeAstar;
import bus.maapstrichtfinal.Calculations.BusStop;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BusRouteShapeAstarTest {

    private static BusStop start;
    private static BusStop end;

    @BeforeAll
    public static void setup() {
        start = new BusStop(0, 0, "start_stop_id", "Start Stop Name", null);
        end = new BusStop(0, 0, "end_stop_id", "End Stop Name", null);
    }

    @Test
    public void testFindShapeDetails() {
        int routeId = 1;

        int[] shapeDetails = BusRouteShapeAstar.findShapeDetails(routeId, start, end);

        assertNotNull(shapeDetails);
        assertEquals(3, shapeDetails.length);
        assertNotEquals(0, shapeDetails[0]); 
        assertNotEquals(0, shapeDetails[1]); 
        assertNotEquals(0, shapeDetails[2]); 
    }

    @Test
    public void testFindCoordsOfAllRoutePoints() {
        int routeId = 1; 

        List<double[]> allPointsCoords = BusRouteShapeAstar.findCoordsOfAllRoutePoints(routeId, start, end);

        assertNotNull(allPointsCoords);
        assertFalse(allPointsCoords.isEmpty());

        for (double[] coords : allPointsCoords) {
            assertNotNull(coords);
            assertEquals(2, coords.length);
            assertNotEquals(0.0, coords[0]); 
            assertNotEquals(0.0, coords[1]); 
        }
    }
}
