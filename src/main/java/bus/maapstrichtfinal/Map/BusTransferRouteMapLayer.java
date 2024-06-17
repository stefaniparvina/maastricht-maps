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

    private List<double[]> shapePointsCoords1 = new ArrayList<>();
    private List<double[]> shapePointsCoords2 = new ArrayList<>();

    public BusTransferRouteMapLayer() {
        startZipCodeMarker = new Circle(5, Color.GREEN);
        endZipCodeMarker = new Circle(5, Color.RED);
        fromTransferMarker = new Circle(5, Color.PURPLE);
        toTransferMarker = new Circle(5, Color.BLUE);

        pathStartZipToStop.setStroke(Color.BLUE);
        pathStartZipToStop.getStrokeDashArray().addAll(5.0, 7.0);
        pathStartZipToStop.setStrokeWidth(2.5);

        pathShape1.setStroke(Color.BLUE);
        pathShape1.setStrokeWidth(2.5);

        pathTransfer.setStroke(Color.BLUE);
        pathTransfer.getStrokeDashArray().addAll(5.0, 7.0);
        pathTransfer.setStrokeWidth(2.5);

        pathShape2.setStroke(Color.BLUE);
        pathShape2.setStrokeWidth(2.5);

        pathEndStopToZip.setStroke(Color.BLUE);
        pathEndStopToZip.getStrokeDashArray().addAll(5.0, 7.0);
        pathEndStopToZip.setStrokeWidth(2.5);

        getChildren().addAll(startZipCodeMarker, endZipCodeMarker, fromTransferMarker, toTransferMarker, pathShape1, pathTransfer, pathShape2, pathStartZipToStop, pathEndStopToZip);
    }

    @Override
    protected void layoutLayer() {
        ZipCode startZipCode = DestinationInfo.getStartZipCode();
        ZipCode endZipCode = DestinationInfo.getEndZipCode();

        if (startZipCode == null || endZipCode == null) {
            new MapController();
            return;
        }
        //TODO
        // this part of the code needs revision as it is not correct
        if (shapePointsCoords1.isEmpty() || shapePointsCoords2.isEmpty()) {
            Route bestRoute = DestinationInfo.bestRoute;
            if (bestRoute == null){
                startZipCode = null;
                endZipCode = null;
                return;
            }
            //TODO
            // this part is entirely to change
            shapePointsCoords1 = BusRouteShape.findCoordsOfAllDirectRoutePoints(bestRoute);
            shapePointsCoords2 = BusRouteShape.findCoordsOfAllDirectRoutePoints(bestRoute);
          //
        }


        List<Double> allPoints1 = new ArrayList<>();
        for (double[] point : shapePointsCoords1) {
            Point2D shapePoint = getMapPoint(point[0], point[1]);
            allPoints1.add(shapePoint.getX());
            allPoints1.add(shapePoint.getY());
        }

        List<Double> allPoints2 = new ArrayList<>();
        for (double[] point : shapePointsCoords2) {
            Point2D shapePoint = getMapPoint(point[0], point[1]);
            allPoints2.add(shapePoint.getX());
            allPoints2.add(shapePoint.getY());
        }

        Point2D startZipCodePoint = getMapPoint(startZipCode.getLatitude(), startZipCode.getLongitude());
        Point2D endZipCodePoint = getMapPoint(endZipCode.getLatitude(), endZipCode.getLongitude());

        Point2D transferStart = getMapPoint(shapePointsCoords1.get(shapePointsCoords1.size() - 1)[0], shapePointsCoords1.get(shapePointsCoords1.size() - 1)[1]);
        Point2D transferEnd = getMapPoint(shapePointsCoords2.get(0)[0], shapePointsCoords2.get(0)[1]);

        startZipCodeMarker.setCenterX(startZipCodePoint.getX());
        startZipCodeMarker.setCenterY(startZipCodePoint.getY());
        endZipCodeMarker.setCenterX(endZipCodePoint.getX());
        endZipCodeMarker.setCenterY(endZipCodePoint.getY());

        fromTransferMarker.setCenterX(transferStart.getX());
        fromTransferMarker.setCenterY(transferStart.getY());
        toTransferMarker.setCenterX(transferEnd.getX());
        toTransferMarker.setCenterY(transferEnd.getY());

        pathShape1.getPoints().setAll(allPoints1);
        pathShape2.getPoints().setAll(allPoints2);

        pathStartZipToStop.getPoints().setAll(startZipCodePoint.getX(), startZipCodePoint.getY(), allPoints1.get(0), allPoints1.get(1));
        pathTransfer.getPoints().setAll(transferStart.getX(), transferStart.getY(), transferEnd.getX(), transferEnd.getY());
        pathEndStopToZip.getPoints().setAll(endZipCodePoint.getX(), endZipCodePoint.getY(), allPoints2.get(allPoints2.size() - 2), allPoints2.get(allPoints2.size() - 1));
    }
}
