package bus.maapstrichtfinal.Calculations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    /*
     * Here the /gtfs is the name of the database we are connecting to
     * 
     * If it doesn't work, after gtfs add:
     * ?allowPublicKeyRetrieval=true&useSSL=false";
     * 
     * You need to change user and password
     * Use the ones you have for mysql
     */

    public static final String url = "jdbc:mysql://localhost:3306/gtfs";
    public static final String user = "BCS1510";
    public static final String password = "BCS1510";
    
    // This static variable holds the active connection.
    private static Connection activeConnection = null;
    
    @SuppressWarnings("exports")
    public static Connection getConnection() {
        if (activeConnection != null) {
            return activeConnection; // Return the existing connection if it's set
        }
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection is successful to database " + url);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // This method allows for injecting a mock or alternate connection.
    public static void setConnection(@SuppressWarnings("exports") Connection mockConnection) {
        activeConnection = mockConnection;
    }
}
