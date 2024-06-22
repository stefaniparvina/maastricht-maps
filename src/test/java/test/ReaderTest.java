package test;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import bus.maapstrichtfinal.ZipCode.Reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

public class ReaderTest {

    @BeforeClass
    public static void setupClass() {
        Reader.coordinatesCache = new HashMap<>();
        Reader.isCacheLoaded = false;
    }

    @Test
    public void testLoadCoordinatesCacheSuccessfully() throws IOException {
        String csvData = "ZIP,LAT,LON\n12345,10.0,-10.0\n67890,20.0,-20.0";
        BufferedReader fakeReader = new BufferedReader(new StringReader(csvData));

        Reader.loadCoordinatesCache();
        assertTrue("Cache should be loaded", Reader.isCacheLoaded);
        assertNotNull("Coordinates should be cached", Reader.coordinatesCache.get("12345"));
        assertArrayEquals("Coordinates should match", new double[]{10.0, -10.0},
                        Reader.coordinatesCache.get("12345"), 0.01);
    }


}
