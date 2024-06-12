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

    private static final String url = "jdbc:mysql://localhost:3306/gtfs";
    private static final String user = "root";
    static final String password = "pass";

    @SuppressWarnings("exports")
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection is successful database " + url);
            return conn;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
