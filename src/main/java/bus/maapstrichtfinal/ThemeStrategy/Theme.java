package bus.maapstrichtfinal.ThemeStrategy;

import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import bus.maapstrichtfinal.GUIInterfaces.ThemeInterface;

public class Theme {
    private ThemeInterface currentTheme;

    public void setThemeStrategy(ThemeInterface themeStrategy) {
        this.currentTheme = themeStrategy;
    }

    public void applyTheme(BorderPane parent, ImageView backgroundImg, ImageView themeBtnImg) {
        if (currentTheme != null) {
            currentTheme.applyTheme(parent, backgroundImg, themeBtnImg);
        }
    }

    public void applyTheme(BorderPane parent, ImageView themeBtnImg){
        if(currentTheme != null){
            currentTheme.applyTheme(parent, themeBtnImg);
        }
    }
}
