package bus.maapstrichtfinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

@SuppressWarnings("exports")
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
        stage.setTitle("MAAPSTRICHT");
        stage.setResizable(false);
        stage.setScene(new Scene(root, 1400, 900));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}