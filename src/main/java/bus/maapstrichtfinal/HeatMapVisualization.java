package bus.maapstrichtfinal;

import bus.maapstrichtfinal.Calculations.DatabaseConnection;
import com.gluonhq.maps.MapLayer;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HeatMapVisualization extends MapLayer {

    private final List<Circle> circles = new ArrayList<>();

    // Constructor
    public HeatMapVisualization() {
        // Empty constructor, initialization is handled in layoutLayer()
    }

    // Method to initialize the map with circles for each zipcode
    @Override
    protected void layoutLayer() {
        // Clear existing circles before adding new ones
        clearCircles();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Connect to database
            conn = DatabaseConnection.getConnection();

            // SQL query to fetch zip codes with latitude, longitude, and accessibility
            String sql = "SELECT PostalCode, Lat, Lon, Accessibility FROM accessibility_with_coordinates";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            // Iterate through the result set
            while (rs.next()) {
                String postalCode = rs.getString("PostalCode");
                double lat = rs.getDouble("Lat");
                double lon = rs.getDouble("Lon");
                double accessibility = rs.getDouble("Accessibility");

                // Convert accessibility to color
                Color color = getColorForAccessibility(accessibility);

                // Convert latitude and longitude to map coordinates
                Point2D mapPoint = getMapPoint(lat, lon);
                if (mapPoint != null) {
                    // Create circle for the zip code with the appropriate color
                    Circle circle = new Circle(7, color); // Adjust circle size
                    circle.setCenterX(mapPoint.getX());
                    circle.setCenterY(mapPoint.getY());
                    circles.add(circle); // Store circle for later removal
                    getChildren().add(circle);
                } else {
                    System.err.println("Failed to convert coordinates for postal code: " + postalCode);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close JDBC objects
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to clear existing circles
    private void clearCircles() {
        getChildren().removeAll(circles);
        circles.clear();
    }

    // Method to map accessibility rate to color
    private Color getColorForAccessibility(double accessibility) {
        // Define color thresholds based on your ranges
        if (accessibility >= 6.438531962157154) {
            return Color.RED;
        }
        else if (accessibility >3.9892617853373586) {
            return Color.YELLOW;
        } else {
            return Color.GREEN;
        }
    }
}
//25 1