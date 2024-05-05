package bus.maapstrichtfinal.ThemeStrategy;

import bus.maapstrichtfinal.GUIInterfaces.ThemeInterface;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class DarkTheme implements ThemeInterface {
    protected static final String CSS_PATH = "/bus/maapstrichtfinal/styles/darkMode.css";
    private static final String THEME_ICON_PATH = "/bus/maapstrichtfinal/icons/darkTheme/sun.png";
    private static final String BACKGROUND_IMAGE_PATH = "/bus/maapstrichtfinal/background/dark_background.jpg";

    @Override
    public void applyTheme(BorderPane parent, ImageView backgroundImg, ImageView themeBtnImg) {
        parent.getStylesheets().remove(getClass().getResource(LightTheme.CSS_PATH).toString());
        parent.getStylesheets().add(getClass().getResource(CSS_PATH).toString());
        themeBtnImg.setImage(new Image(getClass().getResourceAsStream(THEME_ICON_PATH)));
        backgroundImg.setImage(new Image(getClass().getResourceAsStream(BACKGROUND_IMAGE_PATH)));
    }

    @Override
    public void applyTheme(BorderPane parent, ImageView themeBtnImg) {
        parent.getStylesheets().remove(getClass().getResource(LightTheme.CSS_PATH).toString());
        parent.getStylesheets().add(getClass().getResource(CSS_PATH).toString());
        themeBtnImg.setImage(new Image(getClass().getResourceAsStream(THEME_ICON_PATH)));
    }
}
