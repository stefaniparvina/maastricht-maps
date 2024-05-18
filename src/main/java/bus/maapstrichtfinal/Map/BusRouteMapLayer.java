package bus.maapstrichtfinal.Map;

import java.util.ArrayList;
import java.util.List;

import com.gluonhq.maps.MapLayer;

import bus.maapstrichtfinal.HomepageController;
import bus.maapstrichtfinal.Calculations.BusRouteShape;
import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.ZipCode.ZipCode;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

public class BusRouteMapLayer extends MapLayer {
    private final Circle startZipCodeMarker;
    private final Circle endZipCodeMarker;
    private Polyline pathShape = new Polyline();
    private Polyline pathStartZipToStop = new Polyline();
    private Polyline pathEndStopToZip = new Polyline();
    private List<double[]> shapePointsCoords = new ArrayList<double[]>(); // Consists of the coordinates of all points that lie on the hsape including the two bus stops

    public BusRouteMapLayer() {
        startZipCodeMarker = new Circle(5, Color.RED);
        endZipCodeMarker = new Circle(5, Color.RED);
        
        pathStartZipToStop.setStroke(Color.BLUE);
        pathStartZipToStop.getStrokeDashArray().addAll(5.0, 7.0); // Setting a dash pattern: 5 pixels on, 6 pixels off
        pathStartZipToStop.setStrokeWidth(2.5);
        
        pathShape.setStroke(Color.BLUE); 
        pathShape.setStrokeWidth(2.5);

        pathEndStopToZip.setStroke(Color.BLUE);
        pathEndStopToZip.getStrokeDashArray().addAll(5.0, 7.0); // Setting a dash pattern: 5 pixels on, 6 pixels off
        pathEndStopToZip.setStrokeWidth(2.5);
        
        getChildren().addAll(startZipCodeMarker, endZipCodeMarker, pathShape, pathStartZipToStop, pathEndStopToZip);
    }

    @Override
    protected void layoutLayer() {
        ZipCode startZipCode = HomepageController.startZipCode;
        ZipCode endZipCode = HomepageController.endZipCode;
        
        if (shapePointsCoords.size() == 0) { // Check if the coordinates of the shape points are already found
            List<Route> fastestRoutes = Route.findFastestRoutes(startZipCode, endZipCode);
            shapePointsCoords = BusRouteShape.findCoordsOfAllRoutePoints(fastestRoutes.get(0));
        }
        
        List<Double> allPoints = new ArrayList<>();
        for (double[] point : shapePointsCoords) {
            Point2D shapePoint = getMapPoint(point[0], point[1]);
            allPoints.add(shapePoint.getX());
            allPoints.add(shapePoint.getY());
        }

        Point2D startZipCodePoint = getMapPoint(startZipCode.getLatitude(), startZipCode.getLongitude());
        Point2D endZipCodePoint = getMapPoint(endZipCode.getLatitude(), endZipCode.getLongitude());
        
        startZipCodeMarker.setCenterX(startZipCodePoint.getX());
        startZipCodeMarker.setCenterY(startZipCodePoint.getY());
        endZipCodeMarker.setCenterX(endZipCodePoint.getX());
        endZipCodeMarker.setCenterY(endZipCodePoint.getY());
        
        // Add the coordinates of all of the shape points for the line to connect them
        pathShape.getPoints().setAll(allPoints); 
        
        // Draw walking path from zip codes to the corresponding bus stops
        pathStartZipToStop.getPoints().setAll(startZipCodePoint.getX(), startZipCodePoint.getY(), pathShape.getPoints().get(0), pathShape.getPoints().get(1) ); 
        pathEndStopToZip.getPoints().setAll(endZipCodePoint.getX(), endZipCodePoint.getY(), allPoints.get(allPoints.size() - 2), allPoints.get(allPoints.size() - 1));

    }
}
