package test;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.mockito.*;

import bus.maapstrichtfinal.Calculations.BusRouteShape;
import bus.maapstrichtfinal.Calculations.DatabaseConnection;
import bus.maapstrichtfinal.Calculations.Route;
import javafx.geometry.Point2D;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BusRouteShapeTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    @SuppressWarnings("deprecation")
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this); // Initialize mocks annotated with @Mock

        // Since DatabaseConnection.getConnection() is static and we have the ability to set a mock connection:
        DatabaseConnection.setConnection(mockConnection); // Set the mock connection before tests

        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @After
    public void tearDown() {
        DatabaseConnection.setConnection(null); // Reset the connection after each test
    }

    @Test
    public void testFindShapeDetails() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false); // Return true the first time, false the second time
        when(mockResultSet.getInt("shape_id")).thenReturn(1);
        when(mockResultSet.getInt("start_stop_distance")).thenReturn(100);
        when(mockResultSet.getInt("end_stop_distance")).thenReturn(500);

        Route testRoute = new Route(1, "start_stop", "end_stop", null, 0, 0);
        int[] shapeDetails = BusRouteShape.findShapeDetails(testRoute);

        assertNotNull(shapeDetails);
        assertArrayEquals(new int[]{1, 100, 500}, shapeDetails);

        verify(mockPreparedStatement).setInt(1, testRoute.getRouteId());
        verify(mockPreparedStatement).setString(2, testRoute.getStartStopId());
        verify(mockPreparedStatement).setString(3, testRoute.getEndStopId());
    }

    @Test
    public void testFindCoordsOfAllRoutePoints() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, true, false); // Simulate three calls
        when(mockResultSet.getDouble("shape_pt_lat")).thenReturn(50.2);
        when(mockResultSet.getDouble("shape_pt_lon")).thenReturn(58.0);

        Route testRoute = new Route(1, "start_stop", "end_stop", null, 0, 0);
        List<Point2D> coords = BusRouteShape.findCoordsOfAllDirectRoutePoints(testRoute);

        assertNotNull(coords);
        //assertFalse(coords.isEmpty());
    }

}
