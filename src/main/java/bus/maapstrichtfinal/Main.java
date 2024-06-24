package bus.maapstrichtfinal;

import bus.maapstrichtfinal.Calculations.Node;
import bus.maapstrichtfinal.ZipCode.ZipCode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import bus.maapstrichtfinal.Calculations.BusStop;
import bus.maapstrichtfinal.Calculations.DatabaseConnection;

@SuppressWarnings("exports")
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
        stage.setTitle("MAAPSTRICHT");
        stage.setResizable(false);
        stage.setScene(new Scene(root, 1400, 783));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

//import bus.maapstrichtfinal.Calculations.Node;
//import bus.maapstrichtfinal.ZipCode.ZipCode;

//public class Main {
//    public static void main(String[] args) {
//        // Load all bus stops into memory
//        BusStop.loadAllBusStops();
//
//        // Create a Scanner object for real-time input
//        Scanner scanner = new Scanner(System.in);
//
//        while (true) {
//            // Get start and end zip codes from the user
//            System.out.println("Enter start zip code:");
//            String startZip = scanner.nextLine();
//            System.out.println("Enter end zip code:");
//            String endZip = scanner.nextLine();
//
//            // Exit condition
//            if (startZip.equalsIgnoreCase("exit") || endZip.equalsIgnoreCase("exit")) {
//                break;
//            }
//
//            // Get the current time
//            String currentTime =
//                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
//
//            // Create ZipCode objects
//            ZipCode start = new ZipCode(startZip);
//            ZipCode end = new ZipCode(endZip);
//
//            try {
//                // Find the path
//                Node path = Node.createGraph(start, end, currentTime);
//                List<Node> pathNodes = Node.reconstructPath(path);
//                // Print the path and total travel time
//                Node.printPath(pathNodes);
//                System.out.println(pathNodes.size());
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        scanner.close();
//    }
//}