package bus.maapstrichtfinal.Map;

import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.MapController;
import bus.maapstrichtfinal.ZipCode.ZipCode;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.geometry.Point2D;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.shape.Circle;
import com.gluonhq.maps.MapLayer;

public class MapInitializer extends MapLayer{
    private static final Logger LOGGER = Logger.getLogger(MapInitializer.class.getName());

    protected static final MapPoint maastrichtLatLon = new MapPoint(50.8514, 5.6910);

    public static MapView createMapView() {
        MapView mapView = new MapView();
        mapView.setPrefSize(1400, 795);

        String transportOption = DestinationInfo.getSelectedTransportOption();

        // Add the appropriate layer based on the transport option
        if ("bus".equalsIgnoreCase(transportOption)) {
            List<Route> bestRoute = DestinationInfo.bestRoute;

            // Determine if the best route involves a transfer
            try {
                boolean isTransferRoute = isTransferRoute(bestRoute.get(0));
                if (isTransferRoute) {
                    mapView.addLayer(new BusTransferRouteMapLayer());
                } else {
                    mapView.addLayer(new BusDirectRouteMapLayer());
                }
            }
            catch (IndexOutOfBoundsException e){
                LOGGER.log(Level.SEVERE, "Error creating mapView",e);
            }
        } else if ("walk".equalsIgnoreCase(transportOption)) {
            mapView.addLayer(new DirectPathMapLayer(true));
        } else if ("bicycle".equalsIgnoreCase(transportOption)) {
            mapView.addLayer(new DirectPathMapLayer(false));
        }

        mapView.setZoom(14);
        mapView.flyTo(0, maastrichtLatLon, 0.1);
        return mapView;
    }

    // Method to determine if the route is a transfer route
    public static boolean isTransferRoute(Route route) {
        return route != null && route.isTransfer();
    }
}
