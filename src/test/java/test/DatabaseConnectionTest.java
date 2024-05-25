package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import org.junit.jupiter.api.Test;

import bus.maapstrichtfinal.Calculations.DatabaseConnection;

public class DatabaseConnectionTest {

    @Test
    public void testGetConnection() {
        Connection connection = DatabaseConnection.getConnection();
        assertNotNull(connection, "Connection should not be null");

        try {
            connection.close();
        } catch (Exception e) {
            fail("Failed to close the connection: " + e.getMessage());
        }
    }

}

