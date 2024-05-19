package bus.maapstrichtfinal;

import javafx.scene.control.Label;
import bus.maapstrichtfinal.ZipCode.Reader;

/**
 * Utility class containing common methods used in multiple classes.
 */
public class CommonController {

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
}
