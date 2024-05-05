package bus.maapstrichtfinal;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FileInitializer {
    
    public static Scene scene;

    public void initializeFXML(Stage stage, String fileName) throws IOException{
        scene = new Scene(loadFXML(fileName), 1400, 900);
        stage.setScene(scene);
        stage.setTitle("MAAPSTRICHT");
        stage.setResizable(false);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
}
