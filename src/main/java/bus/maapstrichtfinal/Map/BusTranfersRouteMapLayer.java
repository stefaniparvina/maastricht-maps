package bus.maapstrichtfinal.Map;

import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.Calculations.BusRouteShape;
import bus.maapstrichtfinal.Calculations.Node;
import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import bus.maapstrichtfinal.ZipCode.ZipCode;
import com.gluonhq.maps.MapLayer;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.List;

public class BusTranfersRouteMapLayer extends MapLayer {
    private final Circle startZipCodeMarker;
    private final Circle endZipCodeMarker;
    private final List<Circle> transferMarkers;
    private final List<Polyline> pathTransfers;

    public BusTranfersRouteMapLayer() {
        startZipCodeMarker = new Circle(5, Color.GREEN);
        endZipCodeMarker = new Circle(5, Color.RED);
        transferMarkers = new ArrayList<>();
        pathTransfers = new ArrayList<>();
        getChildren().addAll(startZipCodeMarker, endZipCodeMarker);
    }

    @Override
    protected void layoutLayer() {
        updateMapComponents();
    }

    private void updateMapComponents() {
        ZipCode startZipCode = DestinationInfo.getStartZipCode();
        ZipCode endZipCode = DestinationInfo.getEndZipCode();
        List<Node> path = Node.reconstructPath(DestinationInfo.getEndNode());

        if (startZipCode == null || endZipCode == null) {
            return;
        }

        DestinationInfo.setEndNode();
        DestinationInfo.setBestRoute();
        List<Route> bestRoute = DestinationInfo.getBestRoute();

        // Get start and end points for zip codes
        Point2D startZipCodePoint = getMapPoint(startZipCode.getLatitude(), startZipCode.getLongitude());
        Point2D endZipCodePoint = getMapPoint(endZipCode.getLatitude(), endZipCode.getLongitude());

        // Clear previous markers and paths
        getChildren().removeAll(transferMarkers);
        getChildren().removeAll(pathTransfers);
        transferMarkers.clear();
        pathTransfers.clear();

        // Add transfer markers and paths
        for (int i = 1; i < path.size() - 1; i++) {
            Point2D transferZipCodePoint = getMapPoint(path.get(i).getLatitude(), path.get(i).getLongitude());
            Circle transferMarker = new Circle(5, Color.PURPLE);
            transferMarker.setCenterX(transferZipCodePoint.getX());
            transferMarker.setCenterY(transferZipCodePoint.getY());
            transferMarkers.add(transferMarker);
            getChildren().add(transferMarker);

            List<Point2D> routeCoords = BusRouteShape.findCoordsOfAllRoutePoints(bestRoute.get(i));
            if (routeCoords != null) {
                Polyline pathTransfer = new Polyline();
                List<Double> points = new ArrayList<>();
                for (Point2D point : routeCoords) {
                    Point2D shapePoint = getMapPoint(point.getX(), point.getY());
                    points.add(shapePoint.getX());
                    points.add(shapePoint.getY());
                }
                pathTransfer.getPoints().addAll(points);
                pathTransfers.add(pathTransfer);
                getChildren().add(pathTransfer);
            }
        }

        // Update marker positions
        startZipCodeMarker.setCenterX(startZipCodePoint.getX());
        startZipCodeMarker.setCenterY(startZipCodePoint.getY());
        endZipCodeMarker.setCenterX(endZipCodePoint.getX());
        endZipCodeMarker.setCenterY(endZipCodePoint.getY());
    }

    // private Point2D getMapPoint(double latitude, double longitude) {
    // // Assuming a method to convert lat/lon to map points
    // // Implementation of this method is needed
    // return new Point2D(latitude, longitude);
    // }
}