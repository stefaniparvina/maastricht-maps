package bus.maapstrichtfinal.GUIInterfaces;

import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

//Interfact for light and dark theme
public interface ThemeInterface {
    void applyTheme(BorderPane parent, ImageView backgroundImg, ImageView themeBtnImg);
    void applyTheme(BorderPane parent, ImageView themeBtnImg);
}
