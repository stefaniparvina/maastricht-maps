package bus.maapstrichtfinal;

import java.io.IOException;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Duration;

import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import bus.maapstrichtfinal.ThemeStrategy.DarkTheme;
import bus.maapstrichtfinal.ThemeStrategy.LightTheme;
import bus.maapstrichtfinal.ThemeStrategy.Theme;
import bus.maapstrichtfinal.GUIInfo.ThemeInfo;


public class HomepageController {

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
    
    private int invalidInputCount;
    private boolean isMenuVisible = false;
    private TranslateTransition menuAnimation;
    private boolean isLightThemeOn = true;
    private Theme themeHandler;
    


    @FXML
    private void beginBtnClicked(ActionEvent event) {    
        if (toggleGroup.getSelectedToggle() == null) {
            mainTxt.setText("Choose a transport option to continue.");
            adjustFontSize(mainTxt, 28);
            return;
        }
        if (isValidZipCode(startTxtField.getText()) && isValidZipCode(endTxtField.getText())) {
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
        } else invalidInput();

        
    }

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

    @FXML
    private void switchBtnClicked(ActionEvent event) {
        String temp = startTxtField.getText();
        startTxtField.setText(endTxtField.getText());
        endTxtField.setText(temp);
    }

    private void adjustFontSize(Label label, double fontSize) {
        label.setStyle("-fx-font-size:" + fontSize + "px;"); 
    }    

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
    
    @FXML
    private void themeBtnClicked(ActionEvent event){
        toggleTheme();
    }
    
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
            invalidInputCount++;
            displayWarning();
            return true;
        }
    }    

    private void setDestinationPoints(String start, String end){
        DestinationInfo.setStartDestination(start);
        DestinationInfo.setEndDestination(end);
    }

    private boolean isValidZipCode(String zipCode) {
        return zipCode.matches("^[1-9][0-9]{3}\\s?[a-zA-Z]{2}$");
        //TODO change to check valid zipcodes from the hashmap
    }

    private void displayWarning() {
        switch (invalidInputCount) {
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
                mainTxt.setText("To Tatooine, mailing yourself, are you? No, hm?");
                adjustFontSize(mainTxt, 23);
                break;
            case 6:
                mainTxt.setText("Are we setting a new record today?");
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
                adjustFontSize(mainTxt, 30);
                adjustFontSize(secondaryTxt, 30);
                break;
            default:
                mainTxt.setText("If you're trying to test my patience, you're succeeding!");
                secondaryTxt.setText(null);
                adjustFontSize(mainTxt,20);
                break;
        }
    }
    
    private void setSelectedTransportOption() {
        if (toggleGroup.getSelectedToggle() != null) {
            DestinationInfo.setSelectedTransportOption(((RadioButton) toggleGroup.getSelectedToggle()).getText());
        } else {
            DestinationInfo.setSelectedTransportOption(null);
        }
    }      

    private void toggleTheme(){
        if (isLightThemeOn) {
            themeHandler.setThemeStrategy(new DarkTheme());
        } else {
            themeHandler.setThemeStrategy(new LightTheme());
        }
        themeHandler.applyTheme(parent, backgroundImg, themeBtnImg);
        isLightThemeOn = !isLightThemeOn;
        ThemeInfo.setLightTheme(isLightThemeOn);
    }

    private void applyThemeOnInitiation(){
        if (ThemeInfo.isLightThemeOn()) {
            themeHandler.setThemeStrategy(new LightTheme());
        } else {
            themeHandler.setThemeStrategy(new DarkTheme());
        }
        themeHandler.applyTheme(parent, backgroundImg, themeBtnImg);
        isLightThemeOn = ThemeInfo.isLightThemeOn();
    }

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