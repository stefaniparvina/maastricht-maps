package bus.maapstrichtfinal.GUIInfo;

import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.ZipCode.ZipCode;

import java.util.List;

public abstract class DestinationInfo {

    private static String startDestination;
    private static String endDestination;
    private static ZipCode startZipCode;
    private static ZipCode endZipCode;

    public static List<Route> bestRoute;

    private static String selectedTransportOption;
    private static int totalTime;

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
    public static void setTotalTime(int time){totalTime = time;}


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

    public static int getTotalTime(){return totalTime;}

    // Method to find and set the best route, considering transfers
    public static void setBestRoute() {
        bestRoute = Route.findFastestRoute(startZipCode, endZipCode);
    }

    // Method to get the best route(s)
    public static List<Route> getBestRoute() {
        return bestRoute;
    }

    public static void setSelectedTransportOption(String option) {
        selectedTransportOption = option;
    }

    public static String getSelectedTransportOption() {
        return selectedTransportOption;
    }
}
