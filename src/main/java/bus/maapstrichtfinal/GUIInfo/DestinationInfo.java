package bus.maapstrichtfinal.GUIInfo;

public abstract class DestinationInfo {
    
    private static String startDestination;
    private static String endDestination;    
    private static String selectedTransportOption;

    public static void setStartDestination(String start){
        startDestination = start;
    }

    public static void setEndDestination(String end){
        endDestination = end;
    }

    public static String getStartDestination(){
        return startDestination;
    }

    public static String getEndDestination(){
        return endDestination;
    }

    public static void setSelectedTransportOption(String option){
        selectedTransportOption = option;
    }

    public static String getSelectedTransportOption(){
        return selectedTransportOption;
    }

    //TODO call the calculators or idk
}
