package UI;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import ZipCode.*;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import Calculations.BikeSpeed;
import Calculations.Distance;
import Calculations.WalkSpeed;


//east 50.864239, 5.762940
//north 50.911792, 5.705905
//west 50.847681, 5.638854
//south 50.803814, 5.716786

//east-west difference 0.124086
//north-south difference 0.107978

public class Map extends JFrame {

    // Variables declaration - do not modify
    private org.jxmapviewer.JXMapViewer jXMapViewer;
    // End of variables declaration
    private String start, end;
    public static double zipStartX, zipStartY, zipEndX, zipEndY;
    private JPanel drawPanel;
    private final Set<MyWaypoint> waypoints = new HashSet<>();
    private EventWaypoint event;
    private String[] options = {"Walking", "Bike"};

    public Map() {

        initComponents();
        initializeMap();
    }

    private void initializeMap() {

        TileFactoryInfo factoryInfo = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(factoryInfo);
        jXMapViewer.setTileFactory(tileFactory);

        GeoPosition position = new GeoPosition(50.8473739, 5.69147696);
        jXMapViewer.setAddressLocation(position);
        jXMapViewer.setZoom(6);

        MouseInputListener mouseListener = new PanMouseInputListener(jXMapViewer);
        jXMapViewer.addMouseMotionListener(mouseListener);
        jXMapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(jXMapViewer));

    }


    private void initComponents() {

        JFrame frame = new JFrame("Maapstricht");
        JPanel controlPanel = createControlPanel();
        jXMapViewer = new org.jxmapviewer.JXMapViewer();
        drawPanel = createDrawPanel();

        controlPanel.setBackground(Color.RED);
        drawPanel.setBackground(Color.BLUE);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(controlPanel, BorderLayout.NORTH);
        getContentPane().add(jXMapViewer, BorderLayout.CENTER);
        getContentPane().add(drawPanel, BorderLayout.SOUTH);

        frame.add(getContentPane());
        frame.setSize(1100,800);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private void submitActionPerformed(java.awt.event.ActionEvent e) {
        clearWaypoints();
        addWaypoint(new MyWaypoint(event, new GeoPosition(Coordinates.zipStartY, Coordinates.zipStartX)));
        addWaypoint(new MyWaypoint(event, new GeoPosition(Coordinates.zipEndY, Coordinates.zipEndX)));
    }


    private void addWaypoint(MyWaypoint waypoint) {

        waypoints.add(waypoint);
        initWaypoint();
    }
    private void clearWaypoints() {
        for (MyWaypoint point : waypoints) {
            jXMapViewer.remove(point.getButton());
        }
        waypoints.clear();
        jXMapViewer.repaint();
    }

    private void initWaypoint() {

        WaypointPainter<MyWaypoint> wayPoint = new WaypointRender();
        wayPoint.setWaypoints(waypoints);
        jXMapViewer.setOverlayPainter(wayPoint);
        for (MyWaypoint point : waypoints) {
            jXMapViewer.add(point.getButton());
        }
    }


    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Map();
            }
        });
    }


    private JPanel createControlPanel() {


        JPanel controlPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        JTextField zipStartField = new JTextField(5);
        JTextField zipEndField = new JTextField(5);
        JComboBox comboBox = new JComboBox<>();

        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                start = zipStartField.getText().toUpperCase();
                end = zipEndField.getText().toUpperCase();
                Coordinates.extractCoordinates(start, end);
                drawPanel.repaint();
                int choice = JOptionPane.showOptionDialog(null, "Choose time type: ", "Time type", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                double time = -1;
                switch(choice) {
                    case 0:
                        time = WalkSpeed.calculateWalkingTime(start, end);
                        break;
                    case 1:
                        time = BikeSpeed.calculateCyclingTime(start, end);
                        break;
                    default:
                        break;
                }
                submitActionPerformed(evt);
                JOptionPane.showMessageDialog(null, "Distance: " + Distance.calculateDistance(start, end) + " km\nTime: " + time + " minutes");
            }
        });


        controlPanel.add(zipStartField);
        controlPanel.add(zipEndField);
        controlPanel.add(submitButton);

        return controlPanel;
    }

    private JPanel createDrawPanel() {
        JPanel drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;



            }
        };
        return drawPanel;
    }
}
