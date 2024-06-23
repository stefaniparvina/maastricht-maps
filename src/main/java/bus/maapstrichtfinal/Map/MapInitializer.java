package bus.maapstrichtfinal.Map;

import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import bus.maapstrichtfinal.Calculations.Route;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import java.util.List;

public class MapInitializer {
    protected static final MapPoint maastrichtLatLon = new MapPoint(50.8514, 5.6910);

    public static MapView createMapView() {
        MapView mapView = new MapView();
        mapView.setPrefSize(1400, 795);

        String transportOption = DestinationInfo.getSelectedTransportOption();
        List<Route> bestRoute = DestinationInfo.bestRoute;

        // Determine if the best route involves a transfer
        boolean isTransferRoute = isTransferRoute(bestRoute.get(0));

        // Add the appropriate layer based on the transport option
        if ("bus".equalsIgnoreCase(transportOption)) {
            if (isTransferRoute) {
                mapView.addLayer(new BusTransferRouteMapLayer());
            } else {
                mapView.addLayer(new BusDirectRouteMapLayer());
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
    private static boolean isTransferRoute(Route route) {
        return route != null && route.isTransfer() == true;
    }
}
