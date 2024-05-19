package bus.maapstrichtfinal.Map;

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

public class MapInitializer{
    protected static final MapPoint maastrichtLatLon = new MapPoint(50.8514, 5.6910);

    public static MapView createMapView() {

        MapView mapView = new MapView();
        mapView.setPrefSize(1400, 795);
        mapView.addLayer(new BusRouteMapLayer());
        mapView.setZoom(14);
        mapView.flyTo(0, maastrichtLatLon, 0.1);
        return mapView;
    }
}
