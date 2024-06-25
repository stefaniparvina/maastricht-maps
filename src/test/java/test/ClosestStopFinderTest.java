package test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
/**
 * Tests the findClosestStops method in ClosestStopFinder with mocked database interactions.
 */
public class ClosestStopFinderTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        DatabaseConnection.setConnection(mockConnection);  
    }

    @Test
    public void testFindStopsWithin300Meters() throws SQLException {
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getDouble("stop_lat")).thenReturn(50.85, 50.85);
        when(mockResultSet.getDouble("stop_lon")).thenReturn(5.7, 5.7);
        when(mockResultSet.getString("stop_id")).thenReturn("Stop1", "Stop2");

        List<String> closestStops = ClosestStopFinder.findClosestStops(50.85, 5.7,0.3);
        assertNotNull(closestStops);
        assertEquals(2, closestStops.size());
        
    }

    @Test
    public void testFindStopsWithin500Meters() throws SQLException {
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getDouble("stop_lat")).thenReturn(50.85, 50.85);
        when(mockResultSet.getDouble("stop_lon")).thenReturn(5.7, 5.7);
        when(mockResultSet.getString("stop_id")).thenReturn("Stop1", "Stop2");

        List<String> closestStops = ClosestStopFinder.findClosestStops(50.85, 5.7,0.5);
        assertNotNull(closestStops);
        assertEquals(22, closestStops.size());
        
    }

    @Test
    public void testFindClosestStopsNoStopsWithinRadius() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);

        List<String> closestStops = ClosestStopFinder.findClosestStops(50.85, 5.7, 0.3);
        assertNotNull(closestStops);
        assertTrue(closestStops.isEmpty());
    }

    @Test
    public void testFindClosestStopsSQLException() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test exception"));

        List<String> closestStops = ClosestStopFinder.findClosestStops(50.85, 5.7, 0.3);
        assertNotNull(closestStops);
        assertTrue(closestStops.isEmpty());
    }
}
