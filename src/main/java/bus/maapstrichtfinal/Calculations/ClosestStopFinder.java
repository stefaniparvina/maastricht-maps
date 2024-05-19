package bus.maapstrichtfinal.Calculations;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ClosestStopFinder {

    public static List<String> findStops(double userLat, double userLon) throws SQLException {
        List<String> closestStops = new ArrayList<>();

        double[] distances = {0.2, 0.3, 0.5}; 
        String query = "SELECT stop_id, stop_lat, stop_lon FROM stops";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            for (double maxDistance : distances) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        double stopLat = rs.getDouble("stop_lat");
                        double stopLon = rs.getDouble("stop_lon");
                        double distance = calculateDistance(userLat, userLon, stopLat, stopLon);

                        if (distance <= maxDistance) {
                            closestStops.add(rs.getString("stop_id"));
                        }
                    }
                }

                if (!closestStops.isEmpty()) { //If found bus stops then good, if no then search in bigger radius
                    break; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return closestStops;
    }

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }



    // public static void main(String[] args) {
    //     try {
    //         List<String> closestStops1 = findStops(50.85, 5.7);
    //         System.out.println(closestStops1);
    //         List<String> closestStops2 = findStops(50.86, 5.8);
    //         System.out.println(closestStops2);

    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }
}