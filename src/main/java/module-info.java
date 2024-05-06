module bus.maapstrichtfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.gluonhq.maps;
    requires java.net.http;
    requires com.google.gson;
    requires java.sql;


    opens bus.maapstrichtfinal to javafx.fxml;
    exports bus.maapstrichtfinal;
}