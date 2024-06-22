package test;

import org.junit.Test;

import bus.maapstrichtfinal.Calculations.Distance;
import bus.maapstrichtfinal.ZipCode.Coordinates;
import bus.maapstrichtfinal.ZipCode.ZipCode;

import static org.junit.Assert.assertEquals;

public class DistanceTest {

    private static final double DELTA = 0.01; // Delta used for asserting equality on floating point numbers

    @Test
    public void testCalculateDistanceBetweenTwoPoints() {
        // Set up the coordinates manually for the test
        Coordinates.startLatitudeRad = Math.toRadians(50.8513682); // Maastricht latitude in radians
        Coordinates.finishLatitudeRad = Math.toRadians(50.7753455); // Aachen latitude in radians
        Coordinates.startLongitudeRad = Math.toRadians(5.6909725); // Maastricht longitude in radians
        Coordinates.finishLongitudeRad = Math.toRadians(6.0838868); // Aachen longitude in radians

        // Calculate distance and assert
        double expectedDistance = 0.94; // Approximate distance between Maastricht and Aachen in km
        double actualDistance = Distance.calculateDistance("6211JL", "6211CC"); // Assuming postal codes for Maastricht and Aachen
        assertEquals("Distance calculation between Maastricht and Aachen should be close to expected value",
                     expectedDistance, actualDistance, DELTA);
    }
}
