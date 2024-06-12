package bus.maapstrichtfinal.Map;

import java.util.ArrayList;
import java.util.List;

import bus.maapstrichtfinal.MapController;
import com.gluonhq.maps.MapLayer;

import bus.maapstrichtfinal.Calculations.BusRouteShape;
import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import bus.maapstrichtfinal.ZipCode.ZipCode;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

public class BusRouteMapLayer extends MapLayer {
    // Markers for start and end zip codes
    private final Circle startZipCodeMarker;
    private final Circle endZipCodeMarker;

    // Polylines for the bus route shape, start zip code to bus stop, and end bus
    // stop to end zip code
    private Polyline pathShape = new Polyline();
    private Polyline pathStartZipToStop = new Polyline();
    private Polyline pathEndStopToZip = new Polyline();

    // List to store shape points coordinates
    private List<double[]> shapePointsCoords = new ArrayList<double[]>();

    // Constructor
    public BusRouteMapLayer() {
        startZipCodeMarker = new Circle(5, Color.GREEN);
        endZipCodeMarker = new Circle(5, Color.RED);

        // Styling for the polylines
        pathStartZipToStop.setStroke(Color.BLUE);
        pathStartZipToStop.getStrokeDashArray().addAll(5.0, 7.0); // Dash pattern: 5 pixels on, 6 pixels off
        pathStartZipToStop.setStrokeWidth(2.5);

        pathShape.setStroke(Color.BLUE);
        pathShape.setStrokeWidth(2.5);

        pathEndStopToZip.setStroke(Color.BLUE);
        pathEndStopToZip.getStrokeDashArray().addAll(5.0, 7.0); // Dash pattern: 5 pixels on, 6 pixels off
        pathEndStopToZip.setStrokeWidth(2.5);

        // Adding all nodes to the map layer
        getChildren().addAll(startZipCodeMarker, endZipCodeMarker, pathShape, pathStartZipToStop, pathEndStopToZip);
    }

    @Override
    protected void layoutLayer() {
        // Getting start and end zip codes from the HomepageController
        ZipCode startZipCode = DestinationInfo.getStartZipCode();
        ZipCode endZipCode = DestinationInfo.getEndZipCode();

        // Check if start or end zip code is null
        if (startZipCode == null || endZipCode == null) {
            new MapController();
            return;
        }
        // Checking if shape points coordinates are already found
        if (shapePointsCoords.isEmpty()) {
            // Find the fastest route and its shape points coordinates

            Route bestRoute = DestinationInfo.bestRoute;
            if (bestRoute == null){
                startZipCode = null;
                endZipCode = null;
                return;
            }

            shapePointsCoords = BusRouteShape.findCoordsOfAllRoutePoints(bestRoute);
        }

        List<Double> allPoints = new ArrayList<>();
        // Convert shape points coordinates to map points
        for (double[] point : shapePointsCoords) {
            Point2D shapePoint = getMapPoint(point[0], point[1]);
            allPoints.add(shapePoint.getX());
            allPoints.add(shapePoint.getY());
        }

        // Convert start and end zip code coordinates to map points
        Point2D startZipCodePoint = getMapPoint(startZipCode.getLatitude(), startZipCode.getLongitude());
        Point2D endZipCodePoint = getMapPoint(endZipCode.getLatitude(), endZipCode.getLongitude());

        // Set positions of start and end zip code markers
        startZipCodeMarker.setCenterX(startZipCodePoint.getX());
        startZipCodeMarker.setCenterY(startZipCodePoint.getY());
        endZipCodeMarker.setCenterX(endZipCodePoint.getX());
        endZipCodeMarker.setCenterY(endZipCodePoint.getY());

        // Set polyline points for the bus route shape
        pathShape.getPoints().setAll(allPoints);

        // Set polyline points for walking path from start zip code to bus stop
        pathStartZipToStop.getPoints().setAll(startZipCodePoint.getX(), startZipCodePoint.getY(),
                pathShape.getPoints().get(0), pathShape.getPoints().get(1));

        // Set polyline points for walking path from end bus stop to end zip code
        pathEndStopToZip.getPoints().setAll(endZipCodePoint.getX(), endZipCodePoint.getY(),
                allPoints.get(allPoints.size() - 2), allPoints.get(allPoints.size() - 1));
    }
}
