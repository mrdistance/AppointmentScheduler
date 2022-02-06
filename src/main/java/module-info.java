module com.example.appointmentscheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.example.appointmentscheduler to javafx.fxml;
    exports com.example.appointmentscheduler;
}