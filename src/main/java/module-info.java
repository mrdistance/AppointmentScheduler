module com.example.appointmentscheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.appointmentscheduler to javafx.fxml;
    exports com.example.appointmentscheduler;
}