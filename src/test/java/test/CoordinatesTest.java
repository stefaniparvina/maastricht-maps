package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import bus.maapstrichtfinal.ZipCode.Coordinates;

/*
 * Test cases that verify the correctness of the coordinates
 * and the second one checks the accuracy of the convertCoordinatesToRadians() method
 */
public class CoordinatesTest {
    @Test
    public void testExtractCoordinates() {
        String startZip = "6211LC";
        String endZip = "6229EN";

        Coordinates.extractCoordinates(startZip, endZip);

        assertEquals(50.84932405, Coordinates.zipStartY);
        assertEquals(5.689337666666667, Coordinates.zipStartX);
        assertEquals(50.83939495, Coordinates.zipEndY);
        assertEquals(5.7142248166666665, Coordinates.zipEndX);
    }
}
