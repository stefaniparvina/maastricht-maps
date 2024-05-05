package bus.maapstrichtfinal.Map;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CustomMapLayer extends MapLayer {
    private final Node startMarker;
    private final Node endMarker;

    private MapPoint startLocation = new MapPoint(50.8497, 5.7023);
    private MapPoint endLocation = new MapPoint(50.8398, 5.7125);

    public CustomMapLayer() {
        startMarker = new Circle(5, Color.RED);
        endMarker = new Circle(5, Color.BLUE);

        getChildren().add(startMarker);
        getChildren().add(endMarker);
    }

    @Override
    protected void layoutLayer() {
        Point2D startPoint = getMapPoint(startLocation.getLatitude(), startLocation.getLongitude());
        Point2D endPoint = getMapPoint(endLocation.getLatitude(), endLocation.getLongitude());
        startMarker.setLayoutX(startPoint.getX());
        startMarker.setLayoutY(startPoint.getY());
        endMarker.setLayoutX(endPoint.getX());
        endMarker.setLayoutY(endPoint.getY());
    }
}
