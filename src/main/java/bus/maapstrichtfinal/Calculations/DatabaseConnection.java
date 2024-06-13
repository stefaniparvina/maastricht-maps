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
    private static final String user = "BCS1510";
    static final String password = "BCS1510";
    private static int count = 0;

    @SuppressWarnings("exports")
    public static Connection getConnection() {
        try {
            count++;
            Connection conn = DriverManager.getConnection(url, user, password);
            if(count==1)System.out.println("Connection is successful database " + url);
            return conn;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
