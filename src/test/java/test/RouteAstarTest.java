package test;

import bus.maapstrichtfinal.Calculations.DatabaseConnection;
import bus.maapstrichtfinal.Calculations.RouteAstar;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RouteAstarTest {

    @BeforeAll
    public static void setUp() throws SQLException {
        DatabaseConnection.getConnection(); 
    }

    @Test
    public void testFindFastestRoutesBetweenStops() {
        String startStopId = "startStop1";
        String endStopId = "endStop1";

        List<RouteAstar> routes = RouteAstar.findFastestRoutesBetweenStops(startStopId, endStopId);

        assertNotNull(routes);
        assertFalse(routes.isEmpty());

        for (RouteAstar route : routes) {
            assertNotNull(route.getRouteId());
            assertNotNull(route.getRouteShortName());
            assertNotNull(route.getStartStopId());
            assertNotNull(route.getEndStopId());
            assertTrue(route.getTravelTime() > 0);
            assertTrue(route.getDistance() > 0);
        }
    }

    @Test
    public void testFindFastestRoutesBetweenStopsNoRoutesFound() {
        String startStopId = "nonExistingStartStop";
        String endStopId = "nonExistingEndStop";

        List<RouteAstar> routes = RouteAstar.findFastestRoutesBetweenStops(startStopId, endStopId);

        assertNotNull(routes);
        assertTrue(routes.isEmpty());
    }
}
