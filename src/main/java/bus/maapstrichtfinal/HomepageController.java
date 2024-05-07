package bus.maapstrichtfinal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.BreakIterator;

import bus.maapstrichtfinal.Map.CustomMapLayer;
import com.gluonhq.maps.MapPoint;
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
    public static double startLat = 0.0;
    public static double startLong = 00;
    public static double endLat = 0.0;
    public static double endLong = 0.0;
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

    private String csvPath = "C:\\Users\\User\\Desktop\\project-1-2\\src\\main\\java\\bus\\maapstrichtfinal\\Map\\data\\MassZipLatLon.csv";


    @FXML
    private void beginBtnClicked(ActionEvent event) {
        if (toggleGroup.getSelectedToggle() == null) {
            mainTxt.setText("Choose a transport option to continue.");
            CommonController.adjustFontSize(mainTxt, 28);
            return;
        }
        if (CommonController.isValidZipCode(startTxtField.getText()) && CommonController.isValidZipCode(endTxtField.getText())) {
            setStartLocation(startTxtField.getText());
            setEndLocation(endTxtField.getText());

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

    private void setStartLocation(String zipCode) {
        startLat = 0.0;
        startLong = 0.0;
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].trim().equalsIgnoreCase(zipCode)) {
                    startLat = Double.parseDouble(parts[1].trim());
                    startLong = Double.parseDouble(parts[2].trim());
                    System.out.println("Start location updated:");
                    System.out.println("Latitude: " + startLat + ", Longitude: " + startLong);
                    return;
                }
            }
            System.out.println("Zip code not found in CSV file: " + zipCode);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setEndLocation(String zipCode) {
        endLat = 0.0;
        endLong = 0.0;
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].trim().equalsIgnoreCase(zipCode)) {
                    endLat = Double.parseDouble(parts[1].trim());
                    endLong = Double.parseDouble(parts[2].trim());
                    System.out.println("End location updated:");
                    System.out.println("Latitude: " + endLat + ", Longitude: " + endLong);
                    return;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
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
            CommonController.displayWarning(invalidInputCount,mainTxt,secondaryTxt);
            return true;
        }
    }    

    private void setDestinationPoints(String start, String end){
        DestinationInfo.setStartDestination(start);
        DestinationInfo.setEndDestination(end);
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