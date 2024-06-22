package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import bus.maapstrichtfinal.Calculations.DatabaseConnection;

/*
 * Test case that checks if the connection to the database is successfull
 */
public class DatabaseConnectionTest {

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        assertNotNull(connection, "Connection should not be null");

        try {
            connection.close();
        } catch (Exception e) {
            fail("Failed to close the connection: " + e.getMessage());
        }
    }

}

