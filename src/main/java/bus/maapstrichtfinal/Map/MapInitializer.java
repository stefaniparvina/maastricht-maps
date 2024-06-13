package bus.maapstrichtfinal.Map;

import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

public class MapInitializer {
    protected static final MapPoint maastrichtLatLon = new MapPoint(50.8514, 5.6910);

    public static MapView createMapView() {
        MapView mapView = new MapView();
        mapView.setPrefSize(1400, 795);

        String transportOption = DestinationInfo.getSelectedTransportOption();

        // Add the appropriate layer based on the transport option
        if ("bus".equalsIgnoreCase(transportOption)&& DestinationInfo.bestRoute != null) {
            mapView.addLayer(new BusRouteMapLayer());
        } else if ("walk".equalsIgnoreCase(transportOption)) {
            mapView.addLayer(new DirectPathMapLayer(true));
        } else if ("bicycle".equalsIgnoreCase(transportOption)) {
            mapView.addLayer(new DirectPathMapLayer(false));
        }

        mapView.setZoom(14);
        mapView.flyTo(0, maastrichtLatLon, 0.1);
        return mapView;
    }
}
