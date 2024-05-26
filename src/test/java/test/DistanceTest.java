package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import bus.maapstrichtfinal.Calculations.Distance;

public class DistanceTest {
    @Test
    public void testCalculateDistance() {
        String start = "6211en";
        String end = "6211an";

        double result = Distance.calculateDistance(start, end);

        assertEquals(0.4, result);
    }
}
