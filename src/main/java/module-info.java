module com.example.appointmentscheduler {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.appointmentscheduler to javafx.fxml;
    exports com.example.appointmentscheduler;
}