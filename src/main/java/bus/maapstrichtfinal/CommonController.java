package bus.maapstrichtfinal;

import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import bus.maapstrichtfinal.ZipCode.ZipCode;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import bus.maapstrichtfinal.ZipCode.Reader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utility class containing common methods used in multiple classes.
 */
public class CommonController {
    // Variables to hold location and transport information
    protected static ZipCode startZipCode;
    protected static double startLat = 0.0;
    protected static double startLong = 0.0;
    protected static ZipCode endZipCode;
    protected static double endLat = 0.0;
    protected static double endLong = 0.0;
    private static HomepageController hp = new HomepageController();
    /**
     * Checks if the given zip code is valid by comparing it with a CSV file.
     *
     * @param zipCode The zip code to validate.
     * @return {@code true} if the zip code is valid, {@code false} otherwise.
     */
    static boolean isValidZipCode(String zipCode) {
        if(Reader.readCoordinates(zipCode) != null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Displays a warning message based on the given invalid input count.
     *
     * @param invalidInputCount The count of invalid inputs.
     * @param mainTxt           The main text label to display the warning.
     * @param secondaryTxt      The secondary text label to display additional information.
     */
    static void displayWarning(int invalidInputCount, Label mainTxt, Label secondaryTxt) {
        switch (invalidInputCount) {
            // Cases for different counts of invalid inputs and corresponding warning messages
            case 1:
                mainTxt.setText("Incorrect input.");
                break;
            case 2:
                mainTxt.setText("Please input a correct zipcode.");
                break;
            case 3:
                mainTxt.setText("Your postal code shouldn't be a secret code.");
                adjustFontSize(mainTxt, 25);
                break;
            case 4:
                mainTxt.setText("Are we playing guess the zipcode?");
                adjustFontSize(mainTxt, 30);
                break;
            case 5:
                mainTxt.setText("To Tatooine, mailing yourself, are you?");
                secondaryTxt.setText("No, hm?");
                adjustFontSize(mainTxt, 22);
                break;
            case 6:
                mainTxt.setText("Are we setting a new record today?");
                secondaryTxt.setText(null);
                adjustFontSize(mainTxt, 30);
                break;
            case 7:
                mainTxt.setText("You must be using the Force to input those zipcodes.");
                secondaryTxt.setText("Try using your eyes instead.");
                adjustFontSize(mainTxt, 20);
                adjustFontSize(secondaryTxt, 20);
                break;
            case 8:
                mainTxt.setText("Error 404: Correct zipcode not found.");
                secondaryTxt.setText("Maybe check a droid for help?");
                adjustFontSize(mainTxt, 28);
                adjustFontSize(secondaryTxt, 30);
                break;
            default:
                mainTxt.setText("If you're trying to test my patience, you're succeeding!");
                secondaryTxt.setText(null);
                adjustFontSize(mainTxt,20);
                break;
        }
    }

    /**
     * Adjusts the font size of the given label.
     *
     * @param label    The label to adjust font size for.
     * @param fontSize The font size to set.
     */
    static void adjustFontSize(Label label, double fontSize) {
        label.setStyle("-fx-font-size:" + fontSize + "px;"); // Set the font size for the label
    }

    protected static void routeCalculation(ActionEvent event, ZipCode startZipCode, ZipCode endZipCode, Label mainTxt) {
        Stage stage = new Stage();
        if (Route.isRouteAvailable(startZipCode, endZipCode)) {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        }
        else{
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            adjustFontSize(mainTxt,24);
            mainTxt.setText(" No route exists between the\n specified locations.\n Please try another transport method");
        }
            try {
                FileInitializer initializer = new FileInitializer();
                initializer.initializeFXML(stage, "map");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    protected static void checkRouting(ActionEvent event, Label mainTxt, Label secondaryTxt,
                                       TextField startTxtField, TextField endTxtField, ToggleGroup toggleGroup){
        // Check if a transport option is selected
        if (toggleGroup.getSelectedToggle() == null) {
            mainTxt.setText("Choose a transport option to continue.");
            CommonController.adjustFontSize(mainTxt, 28);
            return;
        }
        // Check if start and end zip codes are valid
        if (CommonController.isValidZipCode(startTxtField.getText())
                && CommonController.isValidZipCode(endTxtField.getText()) &&
                !startTxtField.getText().equalsIgnoreCase(endTxtField.getText())) {
            CommonController.startRouting(event, mainTxt, startTxtField, endTxtField,toggleGroup);
        } else {
            if(startTxtField.getText().equalsIgnoreCase(endTxtField.getText())) mainTxt.setText("Identical zipcodes.");
            else{
                hp.invalidInputCount++;
                CommonController.displayWarning(hp.invalidInputCount,mainTxt,secondaryTxt);
            }
        }
    }
    protected static void startRouting(ActionEvent event, Label mainTxt,
                                       TextField startTxtField, TextField endTxtField,ToggleGroup toggleGroup) {
        // Set start and end locations
        setStartLocation(startTxtField.getText());
        setEndLocation(endTxtField.getText());
        startZipCode = new ZipCode(startTxtField.getText());
        endZipCode = new ZipCode(endTxtField.getText());



        // Display message and initialize route calculation
        mainTxt.setText("Calculating Route...");
        setDestinationPoints(startTxtField.getText(), endTxtField.getText());
        setSelectedTransportOption(toggleGroup);
        hp.invalidInputCount = 0;
        if(DestinationInfo.getSelectedTransportOption().equalsIgnoreCase("bus"))setBestRoute();
        CommonController.routeCalculation(event,startZipCode,endZipCode,mainTxt);
    }
    // Method to set the start location based on the entered zip code
    protected static void setStartLocation(String zipCode) {
        startZipCode = new ZipCode(zipCode);
        startLat = startZipCode.getLatitude();
        startLong = startZipCode.getLongitude();
        DestinationInfo.setStartZipCode(startZipCode);
    }

    // Method to set the end location based on the entered zip code
    protected static void setEndLocation(String zipCode) {
        endZipCode = new ZipCode(zipCode);
        endLat = endZipCode.getLatitude();
        endLong = endZipCode.getLongitude();
        DestinationInfo.setEndZipCode(endZipCode);
    }
    protected static void setBestRoute() {
        DestinationInfo.setBestRoute();
    }

    // Method to set destination points for the route
    protected static void setDestinationPoints(String start, String end) {
        DestinationInfo.setStartDestination(start);
        DestinationInfo.setEndDestination(end);
    }

    // Method to set the selected transport option
    protected static void setSelectedTransportOption(ToggleGroup toggleGroup) {
        if (toggleGroup.getSelectedToggle() != null) {
            DestinationInfo.setSelectedTransportOption(((RadioButton) toggleGroup.getSelectedToggle()).getText());
        } else {
            DestinationInfo.setSelectedTransportOption(null);
        }
    }

}
