package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import bus.maapstrichtfinal.ZipCode.Api;
/*
 * Test case to test the functionality of the getCoordinatesByPostCodes() method
 */

public class ApiTest {
    @Test
    public void testGetCoordinatesByPostCodes() {
        Api api = Api.getInstance();

        try {
            String postcode = "6211JL";
            double[] coordinates = api.getCoordinatesByPostcode(postcode);

            assertNotNull(coordinates);

            assertEquals(2, coordinates.length);

            double latitude = coordinates[0];
            double longitude = coordinates[1];

            //checks if the latitude is within the range
            assertTrue(latitude >= -90 && latitude <= 90);
            //checks if the longitude is within the range
            assertTrue(longitude >= -180 && longitude <= 180);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
