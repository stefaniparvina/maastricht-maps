package UIfiles;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import javax.swing.JButton;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointPainter;

public class WaypointRender extends WaypointPainter<MyWaypoint> {

    @Override
    protected void doPaint(Graphics2D g2, JXMapViewer map, int width, int height) {

        for (MyWaypoint wayPoint : getWaypoints()) {

            Point2D point = map.getTileFactory().geoToPixel(wayPoint.getPosition(), map.getZoom());
            Rectangle rectangle = map.getViewportBounds();
            int x = (int) (point.getX() - rectangle.getX());
            int y = (int) (point.getY() - rectangle.getY());
            JButton submit = wayPoint.getButton();
            submit.setLocation(x - submit.getWidth() / 2, y - submit.getHeight());
        }
    }
}
