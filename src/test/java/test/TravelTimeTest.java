package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import bus.maapstrichtfinal.Calculations.TravelTime;
/*
 * Test case for accuracy of the time calculator for walking 
 * and cycling
 */
public class TravelTimeTest {

    @Test
    public void testCalculateTravelTimeForWalk() {
    
        String start = "6211JL";
        String end = "6211EN";
        String method = "walk";

        String result = TravelTime.calculateTravelTime(start, end, method);

        assertEquals("10 minutes, 10 seconds", result);
    }

    @Test
    public void testCalculateTravelTimeForBike() {

        String start = "6211JL";
        String end = "6211an";
        String method = "bike";

        String result = TravelTime.calculateTravelTime(start, end, method);

        assertEquals("3 minutes, 43 seconds", result); 
    }
}