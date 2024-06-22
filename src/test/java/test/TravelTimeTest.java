package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import bus.maapstrichtfinal.Calculations.Distance;
import bus.maapstrichtfinal.Calculations.TravelTime;

@ExtendWith(MockitoExtension.class)
public class TravelTimeTest {

    @Test
    public void testCalculateTravelTimeWalking() {
        try (MockedStatic<Distance> mockedDistance = mockStatic(Distance.class)) {
            mockedDistance.when(() -> Distance.calculateDistance(anyString(), anyString())).thenReturn(3.0);

            String start = "52.370216, 4.895168"; 
            String end = "52.366374, 4.894515";   

            String result = TravelTime.calculateTravelTime(start, end, "walk");

            double distanceKM = 3.0;
            double timeHours = distanceKM / 5.0;
            int expectedMinutes = (int) (timeHours * 60);
            int expectedSeconds = (int) ((timeHours * 3600) % 60);

            String expected = expectedMinutes + " minutes, " + expectedSeconds + " seconds";
            assertEquals(expected, result);
        }
    }

    @Test
    public void testCalculateTravelTimeBiking() {
        try (MockedStatic<Distance> mockedDistance = mockStatic(Distance.class)) {
            mockedDistance.when(() -> Distance.calculateDistance(anyString(), anyString())).thenReturn(15.0);

            String start = "52.370216, 4.895168"; 
            String end = "52.309069, 4.941728";   

            String result = TravelTime.calculateTravelTime(start, end, "bike");

            double distanceKM = 15.0;
            double timeHours = distanceKM / 15.0;
            int expectedMinutes = (int) (timeHours * 60);
            int expectedSeconds = (int) ((timeHours * 3600) % 60);

            String expected = expectedMinutes + " minutes, " + expectedSeconds + " seconds";
            assertEquals(expected, result);
        }
    }
}
