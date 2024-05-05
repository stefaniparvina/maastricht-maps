module bus.maapstrichtfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.gluonhq.maps;


    opens bus.maapstrichtfinal to javafx.fxml;
    exports bus.maapstrichtfinal;
}