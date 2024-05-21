package bus.maapstrichtfinal.Map;

import bus.maapstrichtfinal.HomepageController;
import bus.maapstrichtfinal.GUIInfo.DestinationInfo;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

public class DirectPathMapLayer extends MapLayer {
    // Markers for start and end points
    private final Circle startMarker;
    private final Circle endMarker;
    private boolean walking = false;

    // Polyline representing the direct path
    private final Polyline path;

    // Constructor
    public DirectPathMapLayer(boolean walking) {
        this.walking = walking;

        // Initialize markers and path
        startMarker = new Circle(5, Color.RED);
        endMarker = new Circle(5, Color.RED);
        path = new Polyline();
        path.setStroke(Color.BLUE);
        path.setStrokeWidth(2.5);

        // Add markers and path to the map layer
        getChildren().addAll(startMarker, endMarker, path);
    }

    @Override
    protected void layoutLayer() {
        // Get start and end locations from HomepageController
        MapPoint startLocation = new MapPoint(DestinationInfo.getStartZipCode().getLatitude(),
                DestinationInfo.getStartZipCode().getLongitude());
        MapPoint endLocation = new MapPoint(DestinationInfo.getEndZipCode().getLatitude(),
                DestinationInfo.getEndZipCode().getLongitude());

        // Convert start and end locations to map points
        Point2D startPoint = getMapPoint(startLocation.getLatitude(), startLocation.getLongitude());
        Point2D endPoint = getMapPoint(endLocation.getLatitude(), endLocation.getLongitude());

        // Set positions of start and end markers
        startMarker.setCenterX(startPoint.getX());
        startMarker.setCenterY(startPoint.getY());
        endMarker.setCenterX(endPoint.getX());
        endMarker.setCenterY(endPoint.getY());

        // Add dash pattern to the path if it's a walking path
        if (walking) {
            path.getStrokeDashArray().addAll(5.0, 7.0); // Dash pattern: 5 pixels on, 6 pixels off
        }

        // Clear previous points and set new points for the path
        path.getPoints().clear();
        path.getPoints().addAll(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
    }
}
