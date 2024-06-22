package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import bus.maapstrichtfinal.Calculations.ClosestStopFinder;
import bus.maapstrichtfinal.Calculations.DatabaseConnection;
import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.ZipCode.ZipCode;

@ExtendWith(MockitoExtension.class)
public class RouteTest {

    private static ZipCode startZipCode;
    private static ZipCode endZipCode;

    @BeforeAll
    public static void setUp() {
        startZipCode = new ZipCode("6211JL"); 
        endZipCode = new ZipCode("6229EN"); 
    }

    @Test
    public void testFindRoutesBetweenStops() throws SQLException {
        // Mock the database connection and related objects
        try (MockedStatic<DatabaseConnection> mockedDbConnection = mockStatic(DatabaseConnection.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            ResultSet mockResultSet = mock(ResultSet.class);

            mockedDbConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            // Mocking the result set
            when(mockResultSet.next()).thenReturn(true).thenReturn(false);
            when(mockResultSet.getInt("route_id")).thenReturn(1);
            when(mockResultSet.getString("route_short_name")).thenReturn("Route1");
            when(mockResultSet.getString("start_stop_id")).thenReturn("StartStop");
            when(mockResultSet.getString("end_stop_id")).thenReturn("EndStop");
            when(mockResultSet.getInt("travel_time_seconds")).thenReturn(3600); // 1 hour
            when(mockResultSet.getInt("distance")).thenReturn(10000); // 10 km

            List<Route> routes = Route.findRoutesBetweenStops("StartStop", "EndStop");

            assertNotNull(routes);
            assertEquals(1, routes.size());
            assertEquals(1, routes.get(0).getRouteId());
            assertEquals("Route1", routes.get(0).getRouteShortName());
            assertEquals("StartStop", routes.get(0).getStartStopId());
            assertEquals("EndStop", routes.get(0).getEndStopId());
            assertEquals(3600, routes.get(0).getTravelTime());
            assertEquals(10000, routes.get(0).getDistance());
        }
    }

    
}
