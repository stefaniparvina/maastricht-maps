package test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bus.maapstrichtfinal.Calculations.BusStop;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BusStopTest {

    @BeforeAll
    public static void setUp() {
        BusStop.loadAllBusStops();
    }

    @Test
    public void testGetAllBusStops() {
        List<BusStop> allBusStops = BusStop.getAllBusStops();

        assertNotNull(allBusStops);
        assertFalse(allBusStops.isEmpty());
    }

    @Test
    public void testLoadAllBusStops() {
        List<BusStop> allBusStops = BusStop.getAllBusStops();

        assertNotNull(allBusStops);
        assertFalse(allBusStops.isEmpty());

        for (BusStop busStop : allBusStops) {
            assertNotNull(busStop.getStopId());
            assertNotNull(busStop.getName());
            assertTrue(busStop.getLatitude() >= -90 && busStop.getLatitude() <= 90); 
            assertTrue(busStop.getLongitude() >= -180 && busStop.getLongitude() <= 180); 
            assertNotNull(busStop.getBuses());
        }
    }
}
