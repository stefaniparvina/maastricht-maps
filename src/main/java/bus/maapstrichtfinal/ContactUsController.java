package bus.maapstrichtfinal;

import java.io.IOException;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.util.Duration;

import bus.maapstrichtfinal.ThemeStrategy.DarkTheme;
import bus.maapstrichtfinal.ThemeStrategy.LightTheme;
import bus.maapstrichtfinal.ThemeStrategy.Theme;
import bus.maapstrichtfinal.GUIInfo.ThemeInfo;

public class ContactUsController {
    @FXML
    private Button maapBtn;
    @FXML
    private Button menuBtn;
    @FXML
    private Button homepageBtn;
    @FXML
    private VBox menu;
    @FXML
    private Button themeBtn;
    @FXML
    private BorderPane parent;
    @FXML
    private ImageView themeBtnImg;

    private boolean isMenuVisible = false; // Flag to track the visibility of the menu
    private boolean isLightThemeOn = true; // Flag to track the current theme
    private TranslateTransition menuAnimation; // Animation for menu transition
    private Theme themeHandler; // Theme handler instance

    @FXML
    private void maapBtnClicked(ActionEvent event) {
        // Handling event when Maap button is clicked to navigate to the map
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FileInitializer initializer = new FileInitializer();
            initializer.initializeFXML(stage, "map");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void menuBtnClicked(ActionEvent event) {
        // Handling event when Menu button is clicked to toggle menu visibility
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
    private void homepageBtnClicked(ActionEvent event){
        // Handling event when Homepage button is clicked to navigate to the homepage
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
        // Handling event when Theme button is clicked to toggle theme
        toggleTheme();
    }

    private void toggleTheme(){
        // Toggling between light and dark themes
        if (isLightThemeOn) {
            themeHandler.setThemeStrategy(new DarkTheme());
        } else {
            themeHandler.setThemeStrategy(new LightTheme());
        }
        themeHandler.applyTheme(parent, themeBtnImg); // Applying the theme
        isLightThemeOn = !isLightThemeOn;
        ThemeInfo.setLightTheme(isLightThemeOn); // Updating theme info
    }

    private void applyThemeOnInitiation(){
        // Applying theme on initialization of the controller
        if (ThemeInfo.isLightThemeOn()) {
            themeHandler.setThemeStrategy(new LightTheme());
        } else {
            themeHandler.setThemeStrategy(new DarkTheme());
        }
        themeHandler.applyTheme(parent, themeBtnImg); // Applying the theme
        isLightThemeOn = ThemeInfo.isLightThemeOn(); // Updating theme flag
    }

    public void initialize() {
        // Initializing the controller
        themeHandler = new Theme();
        applyThemeOnInitiation();

        menuAnimation = new TranslateTransition(Duration.millis(300), menu); // Creating menu transition animation
        menu.setTranslateX(menu.getPrefWidth() + 300); // Setting initial menu position
    }
}
