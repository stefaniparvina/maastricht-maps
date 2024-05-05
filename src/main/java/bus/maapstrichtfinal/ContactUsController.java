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

    private boolean isMenuVisible = false;
    private boolean isLightThemeOn = true;
    private TranslateTransition menuAnimation;
    private Theme themeHandler;

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

        menuAnimation = new TranslateTransition(Duration.millis(300), menu);
        menu.setTranslateX(menu.getPrefWidth() + 300);
    }    
}
