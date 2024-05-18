package bus.maapstrichtfinal.Map;

import bus.maapstrichtfinal.HomepageController;
import bus.maapstrichtfinal.Calculations.BusRouteShape;
import bus.maapstrichtfinal.Calculations.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

public class CustomMapLayer extends MapLayer {
    private final Circle startMarker;
    private final Circle endMarker;

    private final Polyline path;

    public CustomMapLayer() {
        startMarker = new Circle(5, Color.RED);
        endMarker = new Circle(5, Color.BLUE);
        path = new Polyline();

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

        path.getPoints().clear();
        path.getPoints().addAll(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
    }
}
