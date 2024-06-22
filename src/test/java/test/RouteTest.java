package test;

import static org.junit.jupiter.api.Assertions.*;
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

import bus.maapstrichtfinal.Calculations.DatabaseConnection;
import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.ZipCode.ZipCode;

public class RouteTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        DatabaseConnection.setConnection(mockConnection);  
    }

    @Test
    public void testGetTimeMinutesAndSeconds() {
        Route testRoute = new Route(1, "Test Route", "START", "END", 125, 10);
        String time = Route.getTimeMinutesAndSeconds(testRoute);
        assertEquals("2 minutes, 5 seconds", time);
    }

    // @Test
    // public void testFindDirectRoutes() throws Exception {
    //     when(mockResultSet.next()).thenReturn(true).thenReturn(false);
    //     when(mockResultSet.getInt("route_id")).thenReturn(1);
    //     when(mockResultSet.getString("route_short_name")).thenReturn("Route A");
    //     when(mockResultSet.getString("start_stop_id")).thenReturn("100");
    //     when(mockResultSet.getString("end_stop_id")).thenReturn("200");
    //     when(mockResultSet.getInt("travel_time_seconds")).thenReturn(360);
    //     when(mockResultSet.getInt("distance")).thenReturn(500);

    //     List<List<Route>> routes = Route.findDirectRoutes("100", "200");
    //     assertNotNull(routes);
    //     assertFalse(routes.isEmpty());
    //     assertEquals(1, routes.size());
    //     assertEquals(500, routes.get(0).get(0).getDistance());
    // }

    // @Test
    // public void testIsRouteAvailable() throws SQLException {
    //     ZipCode start = new ZipCode("1111");
    //     ZipCode end = new ZipCode("2222");

    //     when(mockResultSet.next()).thenReturn(true).thenReturn(false); 

    //     boolean available = Route.isRouteAvailable(start, end);
    //     assertTrue(available);
    // }

    // @Test
    // public void testIsTransfer() {
    //     Route testRoute = new Route(1, "Test Route", "START", "END", 125, 10);
    //     assertFalse(testRoute.isTransfer()); 
    // }
}
