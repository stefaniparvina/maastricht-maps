package bus.maapstrichtfinal;

import java.io.IOException;

import com.gluonhq.maps.MapView;

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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.util.Duration;

import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import bus.maapstrichtfinal.Map.MapInitializer;
import bus.maapstrichtfinal.ThemeStrategy.DarkTheme;
import bus.maapstrichtfinal.ThemeStrategy.LightTheme;
import bus.maapstrichtfinal.ThemeStrategy.Theme;
import bus.maapstrichtfinal.GUIInfo.ThemeInfo;

public class MapController {

    @FXML
    private Button beginBtn;
    @FXML
    private RadioButton bicycleRadioBtn;
    @FXML
    private RadioButton busRadioBtn;
    @FXML
    private RadioButton walkRadioBtn;
    @FXML
    private Button contactUsBtn;
    @FXML
    private TextField startTxtField;
    @FXML
    private TextField endTxtField;
    @FXML
    private Label mainTxt;
    @FXML
    private Label secondaryTxt;
    @FXML
    private VBox menu;
    @FXML
    private Button menuBtn;
    @FXML
    private Button switchBtn;
    @FXML
    private Button themeBtn;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private Button homepageBtn;
    @FXML
    private Button discoverPanelBtn;
    @FXML
    private VBox discoverPanel;
    @FXML
    private VBox resultPanel;
    @FXML
    private Label transportOption;
    @FXML
    private Label timeResult;
    @FXML
    private Label zipCodes;
    @FXML
    private Button closeDiscoverPanelBtn;
    @FXML
    private BorderPane parent;
    @FXML
    private ImageView themeBtnImg;
    @FXML
    private VBox map;

    private int invalidInputCount;
    private boolean isMenuVisible = false;
    private boolean isDiscoverPanelVisible = true;
    private boolean isLightThemeOn = true;
    private TranslateTransition menuAnimation;
    private TranslateTransition discoverPanelAnimation;
    private TranslateTransition resultPanelAnimation;
    private TranslateTransition discoverPanelBtnAnimation;
    private Theme themeHandler;

    @FXML
    private void beginBtnClicked(ActionEvent event) {
        if (toggleGroup.getSelectedToggle() == null) {
            mainTxt.setText("Choose a transport option to continue.");
            CommonController.adjustFontSize(mainTxt, 28);
            return;
        }
        if (CommonController.isValidZipCode(startTxtField.getText()) && CommonController.isValidZipCode(endTxtField.getText())) {
            mainTxt.setText("Calculating Route...");
            invalidInputCount = 0;
        } else {
            invalidInputCount++;
            CommonController.displayWarning(invalidInputCount,mainTxt,secondaryTxt);
        }
    }

    @FXML
    private void discoverPanelBtnClicked(ActionEvent event){
        if (isDiscoverPanelVisible) {
            discoverPanelAnimation.setToX(discoverPanel.getPrefWidth() + 300);
            discoverPanelBtnAnimation.setToX(discoverPanelBtn.getPrefHeight() - 60);
        } else {
            discoverPanelAnimation.setToX(0);
            discoverPanelBtnAnimation.setToX(discoverPanelBtn.getPrefHeight() + 100);
        }
        discoverPanelAnimation.play();
        discoverPanelBtnAnimation.play();
        isDiscoverPanelVisible = !isDiscoverPanelVisible;
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
    private void switchBtnClicked(ActionEvent event) {
        String temp = startTxtField.getText();
        startTxtField.setText(endTxtField.getText());
        endTxtField.setText(temp);
    }

    @FXML
    private void homepageBtnClicked(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FileInitializer initializer = new FileInitializer();
            initializer.initializeFXML(stage, "homepage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void themeBtnClicked(ActionEvent event){
        toggleTheme();
    }

    private void toggleTheme(){
        if (isLightThemeOn) {
            themeHandler.setThemeStrategy(new DarkTheme());
        } else {
            themeHandler.setThemeStrategy(new LightTheme());
        }
        themeHandler.applyTheme(parent, themeBtnImg);
        isLightThemeOn = !isLightThemeOn;
        ThemeInfo.setLightTheme(isLightThemeOn);
    }

    private boolean startAndEndInfoAvailable(){
        return DestinationInfo.getStartDestination() != null && DestinationInfo.getEndDestination() != null;
    }

    // private MapView getMap(){
    //     MapView mapView = MapInitializer.initializeMap();
    //     return mapView;
    // }

    // private void placeMapIntoPane(MapView mapView){
    //     mapView.setPrefSize(map.getWidth(), map.getHeight());
    //     map.getChildren().add(mapView);
    // }

    private void applyThemeOnInitiation(){
        if (ThemeInfo.isLightThemeOn()) {
            themeHandler.setThemeStrategy(new LightTheme());
        } else {
            themeHandler.setThemeStrategy(new DarkTheme());
        }
        themeHandler.applyTheme(parent, themeBtnImg);
        isLightThemeOn = ThemeInfo.isLightThemeOn();
    }
    
    public void initialize() {
        themeHandler = new Theme();
        applyThemeOnInitiation();

        // placeMapIntoPane(getMap());
        MapView mapView = MapInitializer.createMapView();
        map.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);

        toggleGroup = new ToggleGroup();
        bicycleRadioBtn.setToggleGroup(toggleGroup);
        busRadioBtn.setToggleGroup(toggleGroup);
        walkRadioBtn.setToggleGroup(toggleGroup);

        menuAnimation = new TranslateTransition(Duration.millis(300), menu);
        menu.setTranslateX(menu.getPrefWidth() + 300);

        discoverPanelAnimation = new TranslateTransition(Duration.millis(300), discoverPanel);
        discoverPanelAnimation.setToX(0);

        discoverPanelBtnAnimation = new TranslateTransition(Duration.millis(300), discoverPanelBtn);
        discoverPanelBtn.setTranslateX(discoverPanelBtn.getPrefHeight() + 100);

        resultPanelAnimation = new TranslateTransition(Duration.millis(300), resultPanel);
        resultPanel.setTranslateX(resultPanel.getPrefWidth() - 800);

        if(startAndEndInfoAvailable()){
            startTxtField.setText(DestinationInfo.getStartDestination());
            endTxtField.setText(DestinationInfo.getEndDestination());
            mainTxt.setText("Route Calculated!");
            discoverPanel.setTranslateX(discoverPanel.getPrefWidth() + 800);
            discoverPanelBtn.setTranslateX(discoverPanelBtn.getPrefWidth() - 50);
            resultPanelAnimation.setToX(0);
            resultPanelAnimation.play();
        }
    }
}
