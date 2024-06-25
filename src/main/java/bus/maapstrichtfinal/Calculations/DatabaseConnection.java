package bus.maapstrichtfinal.Calculations;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import redis.clients.jedis.Jedis;

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
     private static final String password = "";
     private static HikariDataSource dataSource;
     private static Jedis jedis;

    static {
    
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("allowPublicKeyRetrieval", "true");
        config.addDataSourceProperty("useSSL", "false");
        dataSource = new HikariDataSource(config);

        
        jedis = new Jedis("localhost", 6379); 
    }

    @SuppressWarnings("exports")
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void saveToCache(String key, String value) {
        jedis.set(key, value);
    }

    public static String getFromCache(String key) {
        return jedis.get(key);
    }

    public static String getData(String key) {
        
        String data = getFromCache(key);
        if (data != null) {
            return data; 
        }

        String fetchedData = "Data from database";  
        saveToCache(key, fetchedData);  
        return fetchedData;
    }
    
    // This method allows for injecting a mock or alternate connection.
    public static void setConnection(@SuppressWarnings("exports") Connection mockConnection) {
        Connection activeConnection = mockConnection;
    }
}
