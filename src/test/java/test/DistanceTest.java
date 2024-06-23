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
        Coordinates.startLatitudeRad = Math.toRadians(50.8513682); 
        Coordinates.finishLatitudeRad = Math.toRadians(50.7753455); 
        Coordinates.startLongitudeRad = Math.toRadians(5.6909725); 
        Coordinates.finishLongitudeRad = Math.toRadians(6.0838868); 

        double expectedDistance = 0.94;
        double actualDistance = Distance.calculateDistance("6211JL", "6211CC"); 
        assertEquals("Distance calculation between Maastricht and Aachen should be close to expected value",
                     expectedDistance, actualDistance, DELTA);
    }
}
