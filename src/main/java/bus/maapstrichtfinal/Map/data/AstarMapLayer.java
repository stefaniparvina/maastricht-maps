package bus.maapstrichtfinal.Map.data;//package bus.maapstrichtfinal.Map;

import bus.maapstrichtfinal.Calculations.BusRouteShape;
import bus.maapstrichtfinal.Calculations.BusRouteShapeAstar;
import bus.maapstrichtfinal.Calculations.Node;
import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import bus.maapstrichtfinal.ZipCode.ZipCode;
import com.gluonhq.maps.MapLayer;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class AstarMapLayer extends MapLayer {
    private final Circle startZipCodeMarker;
    private final Circle endZipCodeMarker;
    private final List<Circle> getOnBusMarkers = new ArrayList<>();
    private final List<Circle> getOffBusMarkers = new ArrayList<>();

    private Polyline pathShape = new Polyline();
    private List<double[]> shapePointsCoords = new ArrayList<>(); // Consists of the coordinates of all points that lie on the shape including the two zip codes
    private List<double[]> getOnCoords = new ArrayList<>();
    private List<double[]> getOffCoords = new ArrayList<>();

    public AstarMapLayer() {
        startZipCodeMarker = new Circle(5, Color.GREEN);
        endZipCodeMarker = new Circle(5, Color.RED);

        pathShape.setStroke(Color.BLUE);
        pathShape.setStrokeWidth(2);

        getChildren().addAll(startZipCodeMarker, endZipCodeMarker, pathShape);

        // Initialize marker lists
        initializeMarkers(getOnBusMarkers, Color.HOTPINK);
        initializeMarkers(getOffBusMarkers, Color.PURPLE);
    }

    private void initializeMarkers(List<Circle> markers, Color color) {
        for (int i = 0; i < 10; i++) { // assuming a max of 10 markers for initialization, adjust as needed
            Circle marker = new Circle(5, color);
            markers.add(marker);
            getChildren().add(marker);
        }
    }

    @Override
    protected void layoutLayer() {
        ZipCode startZipCode = DestinationInfo.getStartZipCode();
        ZipCode endZipCode = DestinationInfo.getEndZipCode();

        if (startZipCode == null || endZipCode == null) {
            return;
        }

        if (shapePointsCoords.isEmpty()) { // Check if the coordinates of the shape points are already found
            fetchPathAsync(startZipCode, endZipCode);
        } else {
            updateMapLayer();
        }
    }

    private void fetchPathAsync(ZipCode startZipCode, ZipCode endZipCode) {
        Task<List<Node>> task = new Task<>() {
            @Override
            protected List<Node> call() throws SQLException {
                return Node.reconstructPath(Node.createGraph(startZipCode, endZipCode, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                List<Node> path = getValue();
                if (path != null && !path.isEmpty()) {
                    processPath(path);
                }
            }

            @Override
            protected void failed() {
                super.failed();
                getException().printStackTrace();
            }
        };

        new Thread(task).start();
    }

    private void processPath(List<Node> path) {
        Platform.runLater(() -> {
            if (path.isEmpty()) {
                return;
            }

            shapePointsCoords.add(new double[]{path.get(0).getLatitude(), path.get(0).getLongitude()});

            for (int i = 1; i < path.size() - 1; i++) {
                Node current = path.get(i);
                Node next = path.get(i + 1);
                if (current.isArrivedByBus()) {
                    List<double[]> shapePoints = BusRouteShapeAstar.findCoordsOfAllRoutePoints(
                            current.getChosenRouteToBusStop(),
                            current.getParent().getBusStop(),
                            current.getBusStop()
                    );
                    shapePointsCoords.addAll(shapePoints);
                    if (next.isArrivedByBus() && current.getChosenRouteToBusStop() != next.getChosenRouteToBusStop()) {
                        getOnCoords.add(new double[]{current.getLatitude(), current.getLongitude()});
                    } else if (!next.isArrivedByBus()) {
                        getOffCoords.add(new double[]{current.getLatitude(), current.getLongitude()});
                    }
                } else {
                    shapePointsCoords.add(new double[]{current.getLatitude(), current.getLongitude()});
                    if (next.isArrivedByBus()) {
                        getOnCoords.add(new double[]{current.getLatitude(), current.getLongitude()});
                    }
                }
            }
            shapePointsCoords.add(new double[]{path.get(path.size() - 1).getLatitude(), path.get(path.size() - 1).getLongitude()});

            updateMapLayer();
        });
    }

    private void updateMapLayer() {
        List<Double> allPoints = new ArrayList<>();
        for (double[] point : shapePointsCoords) {
            Point2D shapePoint = getMapPoint(point[0], point[1]);
            allPoints.add(shapePoint.getX());
            allPoints.add(shapePoint.getY());
        }

        ZipCode startZipCode = DestinationInfo.getStartZipCode();
        ZipCode endZipCode = DestinationInfo.getEndZipCode();

        Point2D startZipCodePoint = getMapPoint(startZipCode.getLatitude(), startZipCode.getLongitude());
        Point2D endZipCodePoint = getMapPoint(endZipCode.getLatitude(), endZipCode.getLongitude());

        startZipCodeMarker.setCenterX(startZipCodePoint.getX());
        startZipCodeMarker.setCenterY(startZipCodePoint.getY());
        endZipCodeMarker.setCenterX(endZipCodePoint.getX());
        endZipCodeMarker.setCenterY(endZipCodePoint.getY());

        updateMarkers(getOnCoords, getOnBusMarkers);
        updateMarkers(getOffCoords, getOffBusMarkers);

        pathShape.getPoints().setAll(allPoints);
    }

    private void updateMarkers(List<double[]> coords, List<Circle> markers) {
        for (int i = 0; i < markers.size(); i++) {
            if (i < coords.size()) {
                Point2D markPoint = getMapPoint(coords.get(i)[0], coords.get(i)[1]);
                markers.get(i).setCenterX(markPoint.getX());
                markers.get(i).setCenterY(markPoint.getY());
                markers.get(i).setVisible(true);
            } else {
                markers.get(i).setVisible(false); // Hide unused markers
            }
        }
    }
}
