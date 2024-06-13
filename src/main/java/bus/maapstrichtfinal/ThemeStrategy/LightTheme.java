package bus.maapstrichtfinal.ThemeStrategy;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import bus.maapstrichtfinal.GUIInterfaces.ThemeInterface;

public class LightTheme implements ThemeInterface {
    protected static final String CSS_PATH = "/bus/maapstrichtfinal/styles/lightMode.css";
    private static final String THEME_ICON_PATH = "/bus/maapstrichtfinal/icons/lightTheme/moon.png";
    private static final String BACKGROUND_IMAGE_PATH = "/bus/maapstrichtfinal/background/light_background.jpg";
    
    @Override
    //apply light theme
    public void applyTheme(BorderPane parent, ImageView backgroundImg, ImageView themeBtnImg) {
        parent.getStylesheets().remove(getClass().getResource(DarkTheme.CSS_PATH).toString());
        parent.getStylesheets().add(getClass().getResource(CSS_PATH).toString());
        themeBtnImg.setImage(new Image(getClass().getResourceAsStream(THEME_ICON_PATH)));
        backgroundImg.setImage(new Image(getClass().getResourceAsStream(BACKGROUND_IMAGE_PATH)));
    }

    @Override
    //apply light theme
    public void applyTheme(BorderPane parent, ImageView themeBtnImg) {
        parent.getStylesheets().remove(getClass().getResource(DarkTheme.CSS_PATH).toString());
        parent.getStylesheets().add(getClass().getResource(CSS_PATH).toString());
        themeBtnImg.setImage(new Image(getClass().getResourceAsStream(THEME_ICON_PATH)));
    }
}   
