package bus.maapstrichtfinal.GUIInfo;

import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.ZipCode.ZipCode;

public abstract class DestinationInfo {

    private static String startDestination;
    private static String endDestination;
    private static ZipCode startZipCode;
    private static ZipCode endZipCode;

    public static Route bestRoute;

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

    public static void setBestRoute(){bestRoute = Route.findFastestRoute(startZipCode, endZipCode);}

    public static void setSelectedTransportOption(String option) {
        selectedTransportOption = option;
    }

    public static String getSelectedTransportOption() {
        return selectedTransportOption;
    }
}
