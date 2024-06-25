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

public class BusDirectRouteMapLayer extends MapLayer {
    // Markers for start and end zip codes
    private final Circle startZipCodeMarker;
    private final Circle endZipCodeMarker;

    // Polylines for the bus route shape, start zip code to bus stop, and end bus
    // stop to end zip code
    private Polyline pathShape = new Polyline();
    private Polyline pathStartZipToStop = new Polyline();
    private Polyline pathEndStopToZip = new Polyline();

    // List to store shape points coordinates
    private List<Point2D> shapePointsCoords = new ArrayList<>();

    // Constructor
    public BusDirectRouteMapLayer() {
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
        getChildren().addAll(pathShape, pathStartZipToStop, pathEndStopToZip, startZipCodeMarker, endZipCodeMarker);
    }

    // Overrides the layout method to position markers and polylines based on map data.
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
            List<Route> bestRoute = DestinationInfo.bestRoute;
            if (bestRoute == null){
                return; //return if there is no bestRoute
            }
            shapePointsCoords = BusRouteShape.findCoordsOfAllDirectRoutePoints(bestRoute.get(0));
        }

        // Convert shape points coordinates to map points
        List<Double> allPoints = new ArrayList<>();
        for (Point2D point : shapePointsCoords) {
            Point2D shapePoint = getMapPoint(point.getX(), point.getY());
            allPoints.add(shapePoint.getX());
            allPoints.add(shapePoint.getY());
        }

        // Set positions of start and end zip code markers
        setMarkerPositions(startZipCodeMarker, startZipCode);
        setMarkerPositions(endZipCodeMarker, endZipCode);

        // Set polyline points for the bus route shape
        pathShape.getPoints().setAll(allPoints);

        // Set polyline points for walking path from start zip code to bus stop
        Point2D startZipCodePoint = getMapPoint(startZipCode.getLatitude(), startZipCode.getLongitude());
        pathStartZipToStop.getPoints().setAll(startZipCodePoint.getX(), startZipCodePoint.getY(),
                pathShape.getPoints().get(0), pathShape.getPoints().get(1));

        // Set polyline points for walking path from end bus stop to end zip code
        Point2D endZipCodePoint = getMapPoint(endZipCode.getLatitude(), endZipCode.getLongitude());
        pathEndStopToZip.getPoints().setAll(endZipCodePoint.getX(), endZipCodePoint.getY(),
                allPoints.get(allPoints.size() - 2), allPoints.get(allPoints.size() - 1));
    }

    // Method to set marker position based on ZipCode object
    private void setMarkerPositions(Circle marker, ZipCode zipCode) {
        Point2D mapPoint = getMapPoint(zipCode.getLatitude(), zipCode.getLongitude());
        marker.setCenterX(mapPoint.getX());
        marker.setCenterY(mapPoint.getY());
    }
}
