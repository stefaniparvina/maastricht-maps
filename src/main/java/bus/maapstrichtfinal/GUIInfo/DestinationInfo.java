package bus.maapstrichtfinal.GUIInfo;

import bus.maapstrichtfinal.Calculations.Node;
import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.ZipCode.ZipCode;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class DestinationInfo {

    private static Node endNode;

    private static String startDestination;
    private static String endDestination;
    private static ZipCode startZipCode;
    private static ZipCode endZipCode;

    private static LocalTime currentTime = LocalTime.now();

    public static List<Route> bestRoute;

    private static String selectedTransportOption;

    public static void setStartDestination(String start) {
        startDestination = start;
    }

    public static void setEndDestination(String end) {
        endDestination = end;
    }

    public static void setStartZipCode(ZipCode start) {
        startZipCode = start;
    }

    public static void setEndZipCode(ZipCode end) {
        endZipCode = end;
    }

    public static ZipCode getStartZipCode() {
        return startZipCode;
    }

    public static ZipCode getEndZipCode() {
        return endZipCode;
    }

    public static String getStartDestination() {
        return startDestination;
    }

    public static String getEndDestination() {
        return endDestination;
    }

    public static Node getEndNode(){
        return endNode;
    }
    public static void setEndNode() {
        try {
            endNode = Node.createGraph(startZipCode, endZipCode, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        } catch (SQLException e) {
            System.out.println("sql");
        }
    }


    public static void setBestRoute(){
        bestRoute = Node.findRoutes(Node.reconstructPath(getEndNode()));
    }

    public static void setSelectedTransportOption(String option) {
        selectedTransportOption = option;
    }

    public static String getSelectedTransportOption() {
        return selectedTransportOption;
    }
    public static List<Route> getBestRoute(){
        return bestRoute;
    }
}
