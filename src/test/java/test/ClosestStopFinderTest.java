package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;

import bus.maapstrichtfinal.Calculations.ClosestStopFinder;

/*
 * Test cases for finding stops within 300m and 500m
 */
public class ClosestStopFinderTest {

    @Test
    public void testFindStopsWithin300Meters() throws SQLException {
        List<String> closestStops = ClosestStopFinder.findStops300m(50.85, 5.7);

        assertEquals(2, closestStops.size());
    }

    @Test
    public void testFindStopsWithin500Meters() throws SQLException {
        List<String> closestStops = ClosestStopFinder.findStops500m(50.85, 5.7);

        assertEquals(2, closestStops.size());
    }
}
