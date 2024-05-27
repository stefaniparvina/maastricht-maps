package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.ZipCode.ZipCode;

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
