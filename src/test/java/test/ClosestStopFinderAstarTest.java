package test;

import org.junit.jupiter.api.Test;

import bus.maapstrichtfinal.Calculations.ClosestStopFinderAstar;

import static org.junit.jupiter.api.Assertions.*;

public class ClosestStopFinderAstarTest {

    @Test
    public void testCalculateDistance() {
        double distance1 = ClosestStopFinderAstar.calculateDistance(50.85, 5.7, 50.86, 5.8);
        assertEquals(13.747, distance1, 0.001); 

        double distance2 = ClosestStopFinderAstar.calculateDistance(50.85, 5.7, 50.85, 5.7);
        assertEquals(0.0, distance2, 0.001);
    }
}
