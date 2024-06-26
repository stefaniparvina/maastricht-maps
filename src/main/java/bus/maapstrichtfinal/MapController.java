package bus.maapstrichtfinal;

import java.io.IOException;
import java.util.Objects;

import bus.maapstrichtfinal.Calculations.TravelTime;
import bus.maapstrichtfinal.ZipCode.ZipCode;
import com.gluonhq.maps.MapView;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import bus.maapstrichtfinal.Calculations.Route;
import bus.maapstrichtfinal.GUIInfo.DestinationInfo;
import bus.maapstrichtfinal.Map.MapInitializer;
import bus.maapstrichtfinal.ThemeStrategy.DarkTheme;
import bus.maapstrichtfinal.ThemeStrategy.LightTheme;
import bus.maapstrichtfinal.ThemeStrategy.Theme;
import bus.maapstrichtfinal.GUIInfo.ThemeInfo;

/**
 * Controller class for the map.fxml file.
 */
public class MapController {

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
    private Button closeDiscoverPanelBtn;
    @FXML
    private BorderPane parent;
    @FXML
    private ImageView themeBtnImg;
    @FXML
    private VBox map;
    @FXML
    private Label transportOptionTxt;
    @FXML
    private Label timeTxt;
    @FXML
    private Label zipCodesTxt;
    @FXML
    private TextField timeTxtField;
    @FXML
    private Label errorLabel;

    // Empty if the user did not choose bus as the transportation method
    @FXML
    private Label busTxt;
    @FXML
    private CheckBox aStarAlgorithmCheckBox;

    // Other variables for menu and panel animations, theme, and input validation
    private int invalidInputCount;
    private boolean isMenuVisible = false;
    private boolean isDiscoverPanelVisible = true;
    private boolean isLightThemeOn = true;
    private boolean isResultVisible = false;
    private TranslateTransition menuAnimation;
    private TranslateTransition discoverPanelAnimation;
    private TranslateTransition resultPanelAnimation;
    private TranslateTransition discoverPanelBtnAnimation;
    private Theme themeHandler;
    private String startStopId;
    private String endStopId;

    //Method to handle the click action of the accessibility button
    @FXML
    private void accessibilityBtnClicked(ActionEvent event){
        CommonController.displayHeatMap(event);
    }

    //Method to handle when the checkbox for A* is checked/unchecked
    @FXML
    private void setAlgorithmSelected(ActionEvent event){
        if(CommonController.getAStarBoolean()){
            timeTxtField.setVisible(false);
        }
        else {
            timeTxtField.setVisible(true);
        }
        CommonController.handleAlgorithmCheckbox();
    }

    // Method called when the "Begin" button is clicked
    @FXML
    private void beginBtnClicked(ActionEvent event) {
        CommonController.checkRouting(event,mainTxt, secondaryTxt,startTxtField,endTxtField,toggleGroup, timeTxtField, errorLabel);
    }
    // Method to set the selected transport option
    private void setSelectedTransportOption() {
        if (toggleGroup.getSelectedToggle() != null) {
            DestinationInfo.setSelectedTransportOption(((RadioButton) toggleGroup.getSelectedToggle()).getText());
        } else {
            DestinationInfo.setSelectedTransportOption(null);
        }
    }

    // Method called when the "Discover Panel" button is clicked to toggle panel visibility
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

    // Method called when the "Contact Us" button is clicked to navigate to the contact us page
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

    // Method called when the "Menu" button is clicked to toggle the visibility of the menu
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

    // Method called when the "Switch" button is clicked to swap start and end locations
    @FXML
    private void switchBtnClicked(ActionEvent event) {
        String temp = startTxtField.getText();
        startTxtField.setText(endTxtField.getText());
        endTxtField.setText(temp);
    }

    // Method called when the "Homepage" button is clicked to navigate to the homepage
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

    // Method called when the "Theme" button is clicked to toggle between light and dark themes
    @FXML
    private void themeBtnClicked(ActionEvent event){
        toggleTheme();
    }

    // Method to toggle between light and dark themes
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

    // Method to check if start and end locations are available
    private boolean startAndEndInfoAvailable(){
        return DestinationInfo.getStartDestination() != null && DestinationInfo.getEndDestination() != null;
    }

    // Method to apply theme settings on initialization
    private void applyThemeOnInitiation(){
        if (ThemeInfo.isLightThemeOn()) {
            themeHandler.setThemeStrategy(new LightTheme());
        } else {
            themeHandler.setThemeStrategy(new DarkTheme());
        }
        themeHandler.applyTheme(parent, themeBtnImg);
        isLightThemeOn = ThemeInfo.isLightThemeOn();
    }

    /**
     * Sets the text on the result panel based on the selected transport option.
     */
    private void setResultPanelText() {
        String transportOptionString = DestinationInfo.getSelectedTransportOption();
        try {
            TransportOption transportOption = TransportOption.fromString(transportOptionString);
            transportOptionChosen(transportOption);
        } catch (IllegalArgumentException e) {
            System.out.println("Error. Transport option is null or invalid.");
        }
    }

    private String startDest = DestinationInfo.getStartDestination();
    private String endDest = DestinationInfo.getEndDestination();
    //private  String startStopId = ;
    //private String endStopId = ;

    /**
     * Updates the result panel text based on the chosen transport option.
     *
     * @param transportOption the chosen transport option
     */
    private void transportOptionChosen(TransportOption transportOption) {
        switch (transportOption) {
            case WALK:
                setTransportText("By walking,", TravelTime.calculateTravelTime(startDest, endDest, "walk"),
                        TravelTime.distanceKM + "km.");
                break;
            case BICYCLE:
                setTransportText("By cycling,", TravelTime.calculateTravelTime(startDest, endDest, "bike"),
                        TravelTime.distanceKM + "km.");
                break;
            case BUS:
                if (DestinationInfo.bestRoute != null && !DestinationInfo.bestRoute.isEmpty()) {
                    StringBuilder busText = new StringBuilder();
                    for (Route route : DestinationInfo.bestRoute) {
                        if (route.getRouteShortName() != null && !route.getRouteShortName().isEmpty()) {
                            if (busText.length() > 0) {
                                busText.append(" then bus ");  // separator between segments
                            }
                            busText.append(route.getRouteShortName());
                        }
                    }
                    startStopId = DestinationInfo.bestRoute.get(0).getStartStopId();
                    if(MapInitializer.isTransferRoute(DestinationInfo.bestRoute.get(0))) {
                        endStopId = DestinationInfo.bestRoute.get(1).getEndStopId();
                    }
                    else{
                        endStopId = DestinationInfo.bestRoute.get(0).getEndStopId();
                    }
                    double totalTime = Route.getTime(DestinationInfo.bestRoute)  + TravelTime.getWalkingTimeBusStop(startStopId, endStopId);
                    String timeTxt = (int)(totalTime / 60) + " minutes";
                    setBusText(busText.toString(), timeTxt);
                }
                else {
                    setBusText(null,null);
                }
                break;
        }
    }

    /**
     * Sets the text for walking or cycling options.
     *
     * @param transportOptionText the transport option text
     * @param time the time it takes to travel
     * @param distance the distance to travel
     */
    private void setTransportText(String transportOptionText, String time, String distance) {
        transportOptionTxt.setText(transportOptionText);
        timeTxt.setText("it takes " + time);
        zipCodesTxt.setText("to go from " + startDest + " to " + endDest + ".");
        busTxt.setText("Distance: " + distance);
    }

    /**
     * Sets the text for the bus option.
     *
     * @param busNumber the bus number
     * @param time the time it takes to travel
     */
    private void setBusText(String busNumber, String time) {

        if(busNumber!=null&&time!=null) {
            transportOptionTxt.setText("By bus,");
            timeTxt.setText("it takes " + time);
            zipCodesTxt.setText("to go from " + startDest + " to " + endDest + ".");
            busTxt.setText("Take bus " + busNumber + ".");
        }
        else{
            transportOptionTxt.setText("No bus available ");
            timeTxt.setText("");
            zipCodesTxt.setText("to go from " + startDest + " to " + endDest + ".");
            busTxt.setText("");
        }
    }

    // Method called on controller initialization
    public void initialize() {
        themeHandler = new Theme();
        applyThemeOnInitiation();

        // Add the map view to the map VBox
        MapView mapView = MapInitializer.createMapView();
        map.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);

        // Toggle group setup for radio buttons
        toggleGroup = new ToggleGroup();
        bicycleRadioBtn.setToggleGroup(toggleGroup);
        busRadioBtn.setToggleGroup(toggleGroup);
        walkRadioBtn.setToggleGroup(toggleGroup);

        // Menu animation setup
        menuAnimation = new TranslateTransition(Duration.millis(300), menu);
        menu.setTranslateX(menu.getPrefWidth() + 300);

        // Discover panel animation setup
        discoverPanelAnimation = new TranslateTransition(Duration.millis(300), discoverPanel);
        discoverPanelAnimation.setToX(0);

        // Discover panel button animation setup
        discoverPanelBtnAnimation = new TranslateTransition(Duration.millis(300), discoverPanelBtn);
        discoverPanelBtn.setTranslateX(discoverPanelBtn.getPrefHeight() + 100);

        // Result panel animation setup
        resultPanelAnimation = new TranslateTransition(Duration.millis(300), resultPanel);
        resultPanel.setTranslateX(resultPanel.getPrefWidth() - 800);
        if(DestinationInfo.getStartZipCode() == null || CommonController.getAStarBoolean()){
            resultPanel.setVisible(false);
            if(CommonController.getHeatMap()){
                discoverPanel.setVisible(false);
            }
        }

        // Check if start and end locations are available and set them if so
        if(startAndEndInfoAvailable()){
            startTxtField.setText(DestinationInfo.getStartDestination());
            endTxtField.setText(DestinationInfo.getEndDestination());
            mainTxt.setText("Route Calculated!");
            discoverPanel.setTranslateX(discoverPanel.getPrefWidth() + 800);
            discoverPanelBtn.setTranslateX(discoverPanelBtn.getPrefWidth() - 50);
            isResultVisible = true;
            resultPanelAnimation.setToX(0);
            resultPanelAnimation.play();
            setResultPanelText();
        }
    }
}
