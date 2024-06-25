package test;

import bus.maapstrichtfinal.Calculations.Node;
import bus.maapstrichtfinal.ZipCode.ZipCode;
import bus.maapstrichtfinal.Calculations.BusStop;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {

    private static ZipCode startZip;
    private static ZipCode endZip;

    @BeforeAll
    public static void setUp() {
        startZip = new ZipCode("6212AA");
        endZip = new ZipCode("6221BE");
    }

    @Test
    public void testCalculateWalkingTimeG() {
        Node parent = new Node(50.85, 5.7);
        double latitude = 50.86;
        double longitude = 5.8;

        int walkingTime = Node.calculateWalkingTimeG(parent, latitude, longitude);

        assertTrue(walkingTime > 0);
        assertTrue(walkingTime < Integer.MAX_VALUE);
    }

    @Test
    public void testCalculateBusTimeG() {
        Node parent = new Node(50.85, 5.7);
        BusStop busStop = new BusStop(50.86, 5.8, "busStop1", "Bus Stop 1", List.of(1, 2, 3));

        int busTime = Node.calculateBusTimeG(parent, busStop);

        assertTrue(busTime > 0);
        assertTrue(busTime < Integer.MAX_VALUE);
    }

    @Test
    public void testCalculateTimeH() {
        int h = Node.calculateTimeH(50.85, 5.7, endZip);

        assertTrue(h > 0);
        assertTrue(h < Integer.MAX_VALUE);
    }

    @Test
    public void testGetCorrespondingNodeForBusStop() {
        BusStop busStop = new BusStop(50.85, 5.7, "busStop1", "Bus Stop 1", List.of(1, 2, 3));
        Node node = new Node(50.85, 5.7);
        node.setBusStop(busStop);
        List<Node> nodes = List.of(node);

        Node correspondingNode = Node.getCorrespondingNodeForBusStop(busStop, nodes);

        assertNotNull(correspondingNode);
        assertEquals(busStop.getLatitude(), correspondingNode.getLatitude());
        assertEquals(busStop.getLongitude(), correspondingNode.getLongitude());
    }

    @Test
    public void testFindConnectedStops() {
        BusStop busStop = new BusStop(50.85, 5.7, "busStop1", "Bus Stop 1", List.of(1, 2, 3));
        String currentTime = "12:00:00";

        List<BusStop> connectedStops = Node.findConnectedStops(busStop, currentTime);

        assertNotNull(connectedStops);
        assertFalse(connectedStops.isEmpty());
    }

    @Test
    public void testGetRouteShortName() {
        int routeId = 1;
        String routeShortName = Node.getRouteShortName(routeId);

        assertNotNull(routeShortName);
        assertFalse(routeShortName.isEmpty());
    }

    @Test
    public void testGetCorrectRouteBetweenStops() {
        Node from = new Node(50.85, 5.7);
        Node to = new Node(50.86, 5.8);

        int correctRoute = Node.getCorrectRouteBetweenStops(from, to);

        assertTrue(correctRoute >= 0); 
    }

    @Test
    public void testReconstructPath() {
        Node node1 = new Node(50.85, 5.7);
        Node node2 = new Node(50.86, 5.8);
        node2.setParent(node1);

        List<Node> path = Node.reconstructPath(node2);

        assertNotNull(path);
        assertEquals(2, path.size());
        assertEquals(node1, path.get(0));
        assertEquals(node2, path.get(1));
    }
}
