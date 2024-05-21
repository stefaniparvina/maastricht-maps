package bus.maapstrichtfinal;

import java.io.IOException;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import bus.maapstrichtfinal.ThemeStrategy.DarkTheme;
import bus.maapstrichtfinal.ThemeStrategy.LightTheme;
import bus.maapstrichtfinal.ThemeStrategy.Theme;
import bus.maapstrichtfinal.ZipCode.ZipCode;
import bus.maapstrichtfinal.GUIInfo.ThemeInfo;

/**
 * Controller class for the homepage.fxml file.
 */
public class HomepageController {

    // FXML annotations to inject components from the FXML file
    @FXML
    private Button beginBtn;
    @FXML
    private RadioButton bicycleRadioBtn;
    @FXML
    private RadioButton busRadioBtn;
    @FXML
    private RadioButton walkRadioBtn;
    @FXML
    private Button maapBtn;
    @FXML
    private Label mainTxt;
    @FXML
    private Label secondaryTxt;
    @FXML
    private Button menuBtn;
    @FXML
    private TextField startTxtField;
    @FXML
    private TextField endTxtField;
    @FXML
    private Button switchBtn;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private VBox menu;
    @FXML
    private Button themeBtn;
    @FXML
    private Button contactUsBtn;
    @FXML
    private ImageView backgroundImg;
    @FXML
    private ImageView themeBtnImg;
    @FXML
    private BorderPane parent;

    // Variables to hold location and transport information
    public static ZipCode startZipCode;
    public static double startLat = 0.0;
    public static double startLong = 0.0;
    public static ZipCode endZipCode;
    public static double endLat = 0.0;
    public static double endLong = 0.0;

    // Other variables for menu animation, theme, and input validation
    private int invalidInputCount;
    private boolean isMenuVisible = false;
    private TranslateTransition menuAnimation;
    private boolean isLightThemeOn = true;
    private Theme themeHandler;

    // Method called when the "Begin" button is clicked
    @FXML
    private void beginBtnClicked(ActionEvent event) {
        // Check if a transport option is selected
        if (toggleGroup.getSelectedToggle() == null) {
            mainTxt.setText("Choose a transport option to continue.");
            CommonController.adjustFontSize(mainTxt, 28);
            return;
        }
        // if(toggleGroup.getSelectedToggle() == )
        // Check if start and end zip codes are valid
        if (CommonController.isValidZipCode(startTxtField.getText())
                && CommonController.isValidZipCode(endTxtField.getText())) {
            // Set start and end locations
            setStartLocation(startTxtField.getText());
            setEndLocation(endTxtField.getText());

            // Display message and initialize route calculation
            mainTxt.setText("Calculating Route...");
            invalidInputCount = 0;
            setDestinationPoints(startTxtField.getText(), endTxtField.getText());
            setSelectedTransportOption();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            try {
                FileInitializer initializer = new FileInitializer();
                initializer.initializeFXML(stage, "map");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            invalidInput(); // Display error message for invalid input
    }

    // Method to set the start location based on the entered zip code
    private void setStartLocation(String zipCode) {
        startZipCode = new ZipCode(zipCode);
        startLat = startZipCode.getLatitude();
        startLong = startZipCode.getLongitude();
        DestinationInfo.setStartZipCode(startZipCode);
    }

    // Method to set the end location based on the entered zip code
    private void setEndLocation(String zipCode) {
        endZipCode = new ZipCode(zipCode);
        endLat = endZipCode.getLatitude();
        endLong = endZipCode.getLongitude();
        DestinationInfo.setEndZipCode(endZipCode);
    }

    // Method called when the "maap" button is clicked
    @FXML
    private void maapBtnClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FileInitializer initializer = new FileInitializer();
            initializer.initializeFXML(stage, "map");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method called when the "Switch" button is clicked to swap start and end
    // locations
    @FXML
    private void switchBtnClicked(ActionEvent event) {
        String temp = startTxtField.getText();
        startTxtField.setText(endTxtField.getText());
        endTxtField.setText(temp);
    }

    // Method called when the "Menu" button is clicked to toggle the visibility of
    // the menu
    @FXML
    private void menuBtnClicked(ActionEvent event) {
        if (isMenuVisible) {
            menuAnimation.setToX(menu.getPrefWidth() + 100);
            menuAnimation.play();
        } else {
            menuAnimation.setToX(0);
            menuAnimation.play();
        }
        isMenuVisible = !isMenuVisible;
    }

    // Method called when the "Contact Us" button is clicked to navigate to the
    // contact us page
    @FXML
    private void contactUsBtnClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FileInitializer initializer = new FileInitializer();
            initializer.initializeFXML(stage, "contactUs");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method called when the "Theme" button is clicked to toggle between light and
    // dark themes
    @FXML
    private void themeBtnClicked(ActionEvent event) {
        toggleTheme();
    }

    // Method to handle invalid input
    private boolean invalidInput() {
        if (startTxtField.getText().isEmpty() && !endTxtField.getText().isEmpty()) {
            mainTxt.setText("Please input your starting point.");
            return true;
        } else if (!startTxtField.getText().isEmpty() && endTxtField.getText().isEmpty()) {
            mainTxt.setText("Please input your destination point.");
            return true;
        } else if (startTxtField.getText().isEmpty() && endTxtField.getText().isEmpty()) {
            mainTxt.setText("Please input your trip details.");
            return true;
        } else {
            // Increment count and display warning message
            invalidInputCount++;
            CommonController.displayWarning(invalidInputCount, mainTxt, secondaryTxt);
            return true;
        }
    }

    // Method to set destination points for the route
    private void setDestinationPoints(String start, String end) {
        DestinationInfo.setStartDestination(start);
        DestinationInfo.setEndDestination(end);
    }

    // Method to set the selected transport option
    private void setSelectedTransportOption() {
        if (toggleGroup.getSelectedToggle() != null) {
            DestinationInfo.setSelectedTransportOption(((RadioButton) toggleGroup.getSelectedToggle()).getText());
        } else {
            DestinationInfo.setSelectedTransportOption(null);
        }
    }

    // Method to toggle between light and dark themes
    private void toggleTheme() {
        if (isLightThemeOn) {
            themeHandler.setThemeStrategy(new DarkTheme());
        } else {
            themeHandler.setThemeStrategy(new LightTheme());
        }
        themeHandler.applyTheme(parent, backgroundImg, themeBtnImg);
        isLightThemeOn = !isLightThemeOn;
        ThemeInfo.setLightTheme(isLightThemeOn);
    }

    // Method to apply theme settings on initialization
    private void applyThemeOnInitiation() {
        if (ThemeInfo.isLightThemeOn()) {
            themeHandler.setThemeStrategy(new LightTheme());
        } else {
            themeHandler.setThemeStrategy(new DarkTheme());
        }
        themeHandler.applyTheme(parent, backgroundImg, themeBtnImg);
        isLightThemeOn = ThemeInfo.isLightThemeOn();
    }

    // Method called on controller initialization
    public void initialize() {
        themeHandler = new Theme();
        applyThemeOnInitiation();

        toggleGroup = new ToggleGroup();
        bicycleRadioBtn.setToggleGroup(toggleGroup);
        busRadioBtn.setToggleGroup(toggleGroup);
        walkRadioBtn.setToggleGroup(toggleGroup);

        menuAnimation = new TranslateTransition(Duration.millis(300), menu);
        menu.setTranslateX(menu.getPrefWidth() + 300);
    }
}
