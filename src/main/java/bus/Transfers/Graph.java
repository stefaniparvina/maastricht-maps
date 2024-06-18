package bus.Transfers;

import java.sql.Time;
import java.util.*;

public class Graph {
    private Map<String, Node> nodes = new HashMap<>();
    private Map<String, List<Edge>> edges = new HashMap<>();

    public void addNode(Node node) {
        nodes.put(node.stopId, node);
        edges.putIfAbsent(node.stopId, new ArrayList<>());
    }

    public void addEdge(int route_id, String bus_short_name, String startId, String endId, int travelTime,
            Time end_arrival_time, Time start_departure_time) {
        Node start = nodes.get(startId);
        Node end = nodes.get(endId);
        edges.get(startId).add(
                new Edge(route_id, bus_short_name, start, end, travelTime, end_arrival_time, start_departure_time));
    }

    public List<Edge> getEdges(String stopId) {
        return edges.get(stopId);
    }

    public Node getNode(String stopId) {
        return nodes.get(stopId);
    }
}
