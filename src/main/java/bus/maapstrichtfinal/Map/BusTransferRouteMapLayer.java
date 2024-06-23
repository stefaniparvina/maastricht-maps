package bus.maapstrichtfinal.Map;

import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.Calculations.BusRouteShape;
import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import bus.maapstrichtfinal.ZipCode.ZipCode;
import com.gluonhq.maps.MapLayer;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.List;

public class BusTransferRouteMapLayer extends MapLayer {
    private final Circle startZipCodeMarker;
    private final Circle endZipCodeMarker;
    private final Circle fromTransferMarker;
    private final Circle toTransferMarker;

    private Polyline pathShape1 = new Polyline();
    private Polyline pathShape2 = new Polyline();
    private Polyline pathStartZipToStop = new Polyline();
    private Polyline pathEndStopToZip = new Polyline();
    private Polyline pathTransfer = new Polyline();

    public BusTransferRouteMapLayer() {
        startZipCodeMarker = new Circle(5, Color.GREEN);
        endZipCodeMarker = new Circle(5, Color.RED);
        fromTransferMarker = new Circle(5, Color.PURPLE);
        toTransferMarker = new Circle(5, Color.BLUE);

        pathStartZipToStop.setStroke(Color.BLUE);
        pathStartZipToStop.getStrokeDashArray().addAll(5.0, 7.0);
        pathStartZipToStop.setStrokeWidth(2.5);

        pathShape1.setStroke(Color.RED);
        pathShape1.setStrokeWidth(2.5);

        pathTransfer.setStroke(Color.BLUE);
        pathTransfer.getStrokeDashArray().addAll(5.0, 7.0);
        pathTransfer.setStrokeWidth(2.5);

        pathShape2.setStroke(Color.BLUE);
        pathShape2.setStrokeWidth(2.5);

        pathEndStopToZip.setStroke(Color.BLUE);
        pathEndStopToZip.getStrokeDashArray().addAll(5.0, 7.0);
        pathEndStopToZip.setStrokeWidth(2.5);

        getChildren().addAll(toTransferMarker, pathShape1, pathTransfer, pathShape2, pathStartZipToStop,
                pathEndStopToZip, startZipCodeMarker, endZipCodeMarker, fromTransferMarker);
    }

    @Override
    protected void layoutLayer() {
        ZipCode startZipCode = DestinationInfo.getStartZipCode();
        ZipCode endZipCode = DestinationInfo.getEndZipCode();

        if (startZipCode == null || endZipCode == null) {
            return;
        }

        List<Route> bestRoute = DestinationInfo.getBestRoute();

        // Clear existing shapes
        clearPaths();

        // Fetch route coordinates using BusRouteShape class
        List<Point2D> route1Coords = BusRouteShape.findCoordsOfAllDirectRoutePoints(bestRoute.get(0));
        List<Point2D> route2Coords = BusRouteShape.findCoordsOfAllDirectRoutePoints(bestRoute.get(1));

        // Update map components
        updateMapComponents(route1Coords, route2Coords);
    }

    private void clearPaths() {
        pathShape1.getPoints().clear();
        pathShape2.getPoints().clear();
        pathStartZipToStop.getPoints().clear();
        pathEndStopToZip.getPoints().clear();
        pathTransfer.getPoints().clear();
    }

    private void updateMapComponents(List<Point2D> route1Coords, List<Point2D> route2Coords) {
        if (route1Coords.isEmpty() || route2Coords.isEmpty()) {
            return;
        }

        // Populate path points for route1
        List<Double> points1 = new ArrayList<>();
        for (Point2D point : route1Coords) {
            Point2D shapePoint = getMapPoint(point.getX(), point.getY());
            points1.add(shapePoint.getX());
            points1.add(shapePoint.getY());
        }

        // Populate path points for route2
        List<Double> points2 = new ArrayList<>();
        for (Point2D point : route2Coords) {
            Point2D shapePoint = getMapPoint(point.getX(), point.getY());
            points2.add(shapePoint.getX());
            points2.add(shapePoint.getY());
        }

        // Get start and end points for zip codes
        Point2D startZipCodePoint = getMapPoint(DestinationInfo.getStartZipCode().getLatitude(),
                DestinationInfo.getStartZipCode().getLongitude());
        Point2D endZipCodePoint = getMapPoint(DestinationInfo.getEndZipCode().getLatitude(),
                DestinationInfo.getEndZipCode().getLongitude());

        // Identify transfer points
        Point2D transferStart = getMapPoint(route1Coords.get(route1Coords.size() - 1).getX(),
                route1Coords.get(route1Coords.size() - 1).getY());
        Point2D transferEnd = getMapPoint(route2Coords.get(0).getX(), route2Coords.get(0).getY());

        // Update marker positions
        startZipCodeMarker.setCenterX(startZipCodePoint.getX());
        startZipCodeMarker.setCenterY(startZipCodePoint.getY());
        endZipCodeMarker.setCenterX(endZipCodePoint.getX());
        endZipCodeMarker.setCenterY(endZipCodePoint.getY());

        fromTransferMarker.setCenterX(transferStart.getX());
        fromTransferMarker.setCenterY(transferStart.getY());
        toTransferMarker.setCenterX(transferEnd.getX());
        toTransferMarker.setCenterY(transferEnd.getY());

        // Update paths with new points
        pathShape1.getPoints().addAll(points1);
        pathShape2.getPoints().addAll(points2);

        // walking paths
        pathStartZipToStop.getPoints().addAll(startZipCodePoint.getX(), startZipCodePoint.getY(), points1.get(0),
                points1.get(1));
        pathTransfer.getPoints().addAll(transferStart.getX(), transferStart.getY(), transferEnd.getX(),
                transferEnd.getY());
        pathEndStopToZip.getPoints().addAll(endZipCodePoint.getX(), endZipCodePoint.getY(),
                points2.get(points2.size() - 2), points2.get(points2.size() - 1));

    }

}
