package bus.maapstrichtfinal.GUIInfo;

public class ThemeInfo {
    private static boolean isLightThemeOn = true;

    public static boolean isLightThemeOn() {
        return isLightThemeOn;
    }

    public static void setLightTheme(boolean lightThemeOn) {
        isLightThemeOn = lightThemeOn;
    }
}