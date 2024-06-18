package bus.Transfers;

import java.sql.Time;

public class Edge {
    int route_id;
    String bus_short_name;
    Node start;
    Node end;
    int travelTime;
    Time start_departure_time;
    Time end_arrival_time;

    public Edge(int route_id, String bus_short_name, Node start, Node end, int travelTime, Time end_arrival_time,
            Time start_departure_time) {
        this.route_id = route_id;
        this.bus_short_name = bus_short_name;
        this.start = start;
        this.end = end;
        this.travelTime = travelTime;
        this.start_departure_time = start_departure_time;
        this.end_arrival_time = end_arrival_time;
    }

    public int getRouteId() {
        return route_id;
    }

    public String getBusShortName() {
        return bus_short_name;
    }

    public Node getStartNode() {
        return start;
    }

    public Node getEndNode() {
        return end;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public Time getArrivalTime() {
        return end_arrival_time;
    }

    public Time getDepartureTime() {
        return start_departure_time;
    }
}