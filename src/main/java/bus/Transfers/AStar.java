package bus.Transfers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import bus.Transfers.Node;
import bus.Transfers.Edge;
import java.util.*;

import bus.maapstrichtfinal.Calculations.DatabaseConnection;

public class AStar {

    static Graph graph;

    public static Graph createGraph() {
        graph = new Graph();
        String query_nodes = "SELECT *" +
                "FROM transfer_info_nodes";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query_nodes)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Node node = new Node(
                            rs.getString("stop_id"),
                            rs.getString("stop_name"),
                            rs.getDouble("stop_lat"),
                            rs.getDouble("stop_lon"));
                    graph.addNode(node);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query_edges = "SELECT *" +
                "FROM transfer_info_edges";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query_edges)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    graph.addEdge(
                            rs.getInt("route_id"),
                            rs.getString("route_short_name"),
                            rs.getString("start_stop_id"),
                            rs.getString("end_stop_id"),
                            rs.getInt("travel_time_seconds"),
                            rs.getTime("end_arrival_time"),
                            rs.getTime("start_departure_time"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(graph.toString());
        return graph;
    }

    public static List<Node> findShortestPath(Graph graph, String startId, String endId) {
        PriorityQueue<NodeRecord> openList = new PriorityQueue<>(
                Comparator.comparingDouble(nr -> nr.estimatedTotalCost));
        Map<String, NodeRecord> allRecords = new HashMap<>();

        Node startNode = graph.getNode(startId);
        Node endNode = graph.getNode(endId);

        NodeRecord startRecord = new NodeRecord(startNode, null, 0, heuristicCost(startNode, endNode));
        openList.add(startRecord);
        allRecords.put(startId, startRecord);

        while (!openList.isEmpty()) {
            NodeRecord current = openList.poll();
            if (current.node.stopId.equals(endId)) {
                return constructPath(current);
            }

            for (Edge edge : graph.getEdges(current.node.stopId)) {
                Node endNodeEdge = edge.end;
                double endNodeCost = current.costSoFar + edge.travelTime;

                NodeRecord endNodeRecord = allRecords.get(endNodeEdge.stopId);
                if (endNodeRecord == null) {
                    endNodeRecord = new NodeRecord(endNodeEdge, current, endNodeCost,
                            endNodeCost + heuristicCost(endNodeEdge, endNode));
                    allRecords.put(endNodeEdge.stopId, endNodeRecord);
                    openList.add(endNodeRecord);
                } else if (endNodeCost < endNodeRecord.costSoFar) {
                    endNodeRecord.costSoFar = endNodeCost;
                    endNodeRecord.connection = current;
                    endNodeRecord.estimatedTotalCost = endNodeCost + heuristicCost(endNodeEdge, endNode);
                    openList.remove(endNodeRecord);
                    openList.add(endNodeRecord);
                }
            }
        }
        return Collections.emptyList(); // No path found
    }

    private static double heuristicCost(Node start, Node end) {
        // Use straight-line distance as the heuristic
        return Math.sqrt(Math.pow(start.latitude - end.latitude, 2) + Math.pow(start.longitude - end.longitude, 2));
    }

    private static List<Node> constructPath(NodeRecord record) {
        List<Node> path = new ArrayList<>();
        while (record != null) {
            path.add(record.node);
            record = record.connection;
        }
        Collections.reverse(path);
        return path;
    }

    static class NodeRecord {
        Node node;
        NodeRecord connection;
        double costSoFar;
        double estimatedTotalCost;

        NodeRecord(Node node, NodeRecord connection, double costSoFar, double estimatedTotalCost) {
            this.node = node;
            this.connection = connection;
            this.costSoFar = costSoFar;
            this.estimatedTotalCost = estimatedTotalCost;
        }
    }

    public static void main(String[] args) {
        Graph graph = createGraph();
        System.out.println(graph.toString());
        System.out.print(findShortestPath(graph, "2578095", "2578363").toString());

    }
}
