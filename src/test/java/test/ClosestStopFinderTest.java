package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import bus.maapstrichtfinal.Calculations.ClosestStopFinder;
import bus.maapstrichtfinal.Calculations.DatabaseConnection;

public class ClosestStopFinderTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockStatement;
    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);

        // Make sure this line is correctly setting the mocked connection
        DatabaseConnection.setConnection(mockConnection);  // Or however you're setting the mock
    }


    @Test
    public void testFindStopsWithin300Meters() throws SQLException {
        List<String> closestStops = ClosestStopFinder.findClosestStops(50.85, 5.7, 0.3);
        assertEquals(0, closestStops.size());
    }

    @Test
    public void testFindStopsWithin500Meters() throws SQLException {
        List<String> closestStops = ClosestStopFinder.findClosestStops(50.85, 5.7, 0.5);
        assertEquals(0, closestStops.size());
    }

    @Test
    public void testFindClosestStops() throws SQLException {
        List<String> stops = ClosestStopFinder.findClosestStops(50.85, 5.7, 0.2);
        assertNotNull(stops);
    }

}
