package bus.maapstrichtfinal.Map;

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
        startMarker = new Circle(5, Color.GREEN);
        endMarker = new Circle(5, Color.RED);
        path = new Polyline();
        path.setStroke(Color.BLUE);
        path.setStrokeWidth(2.5);

        // Add markers and path to the map layer
        getChildren().addAll(path, startMarker, endMarker);
    }

    // Overrides the layout method to position markers and polylines based on map data.
    @Override
    protected void layoutLayer() {
        // Get start and end locations from HomepageController
        if(DestinationInfo.getStartZipCode() == null || DestinationInfo.getEndZipCode() == null){
            return;
        }
        MapPoint startLocation = new MapPoint(DestinationInfo.getStartZipCode().getLatitude(),
                DestinationInfo.getStartZipCode().getLongitude());
        MapPoint endLocation = new MapPoint(DestinationInfo.getEndZipCode().getLatitude(),
                DestinationInfo.getEndZipCode().getLongitude());

        // Set positions of start and end markers
        setMarkerPositions(startMarker, startLocation);
        setMarkerPositions(endMarker, endLocation);

        // Add dash pattern to the path if it's a walking path
        if (walking) {
            path.getStrokeDashArray().addAll(5.0, 7.0); // Dash pattern: 5 pixels on, 6 pixels off
        }

        // Clear previous points and set new points for the path
        path.getPoints().clear();
        path.getPoints().addAll(getMapPoint(startLocation.getLatitude(), startLocation.getLongitude()).getX(),
                getMapPoint(startLocation.getLatitude(), startLocation.getLongitude()).getY(),
                getMapPoint(endLocation.getLatitude(), endLocation.getLongitude()).getX(),
                getMapPoint(endLocation.getLatitude(), endLocation.getLongitude()).getY());
    }

    // Method to set marker position based on MapPoint object
    private void setMarkerPositions(Circle marker, MapPoint point) {
        Point2D mapPoint = getMapPoint(point.getLatitude(), point.getLongitude());
        marker.setCenterX(mapPoint.getX());
        marker.setCenterY(mapPoint.getY());
    }
}
