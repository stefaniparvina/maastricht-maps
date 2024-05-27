package test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;

import org.junit.jupiter.api.Test;

import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.ZipCode.ZipCode;
/*
 * Test case that checks for all the bus routes between two zip codes
 */
public class RouteTest {
    private ZipCode startZipCode;
    private ZipCode endZipCode;

    @Test
    public void testFindAllRoutes() {
        startZipCode = new ZipCode("6211aa");
        endZipCode = new ZipCode("6211en");

        List<Route> routes = Route.findAllRoutes(startZipCode, endZipCode);
        assertNotNull(routes);
        assertFalse(routes.isEmpty());
    }

}
