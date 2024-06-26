module bus.maapstrichtfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.gluonhq.maps;
    requires java.net.http;
    requires com.google.gson;
    requires java.sql;
    requires javafx.graphics;
    requires graphhopper.core;
    requires com.opencsv;
    requires json.simple;
    requires org.json;
    requires com.zaxxer.hikari;
    requires redis.clients.jedis;
    requires annotations;

    opens bus.maapstrichtfinal to javafx.fxml, org.powermock.core;
    opens bus.maapstrichtfinal.Calculations to com.google.gson, org.powermock.core;
    exports bus.maapstrichtfinal;
    exports bus.maapstrichtfinal.Calculations to com.google.gson;
    exports bus.maapstrichtfinal.Map;
    opens bus.maapstrichtfinal.Map to javafx.fxml, org.powermock.core;
}
