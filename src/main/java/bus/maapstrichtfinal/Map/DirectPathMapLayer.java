package bus.maapstrichtfinal.Map;

import bus.maapstrichtfinal.HomepageController;
import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

public class DirectPathMapLayer extends MapLayer {
    private final Circle startMarker;
    private final Circle endMarker;
    private boolean walking = false;

    private final Polyline path;

    public DirectPathMapLayer(boolean walking) {
        
        this.walking = walking;

        startMarker = new Circle(5, Color.RED);
        endMarker = new Circle(5, Color.RED);
        path = new Polyline();
        path.setStroke(Color.BLUE);
        path.setStrokeWidth(2.5);

        getChildren().addAll(startMarker, endMarker, path);
    }

    @Override
    protected void layoutLayer() {
        MapPoint startLocation = new MapPoint(HomepageController.startLat, HomepageController.startLong);
        MapPoint endLocation = new MapPoint(HomepageController.endLat, HomepageController.endLong);

        Point2D startPoint = getMapPoint(startLocation.getLatitude(), startLocation.getLongitude());
        Point2D endPoint = getMapPoint(endLocation.getLatitude(), endLocation.getLongitude());
    
        startMarker.setCenterX(startPoint.getX());
        startMarker.setCenterY(startPoint.getY());
        endMarker.setCenterX(endPoint.getX());
        endMarker.setCenterY(endPoint.getY());
        
        if(walking){
            path.getStrokeDashArray().addAll(5.0, 7.0); // Setting a dash pattern: 5 pixels on, 6 pixels off
        }

        path.getPoints().clear();
        path.getPoints().addAll(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
    }
}
