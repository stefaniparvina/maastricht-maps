package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import bus.maapstrichtfinal.ZipCode.Coordinates;

public class CoordinatesTest {
    @Test
    public void testExtractCoordinates() {
        String startZip = "6211LC";
        String endZip = "6229EN";

        Coordinates.extractCoordinates(startZip, endZip);

        assertEquals(50.85, Coordinates.zipStartY);
        assertEquals(5.7, Coordinates.zipStartX);
        assertEquals(50.86, Coordinates.zipEndY);
        assertEquals(5.71, Coordinates.zipEndX);
    }

    @Test
    public void testConvertCoordinatesToRadians() {
        String startZip = "6211LC";
        String endZip = "6229EN";

        Coordinates.convertCoordinatesToRadians(startZip, endZip);

        assertEquals(Math.toRadians(50.85), Coordinates.startLatitudeRad);
        assertEquals(Math.toRadians(5.7), Coordinates.startLongitudeRad);
        assertEquals(Math.toRadians(50.86), Coordinates.finishLatitudeRad);
        assertEquals(Math.toRadians(5.71), Coordinates.finishLongitudeRad);
    }
}
