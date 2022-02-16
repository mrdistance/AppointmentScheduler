package com.example.appointmentscheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * This class provides the controller for the dashboard view
 *
 * Class DashboardController.java
 *
 * @author Joshua Call
 */
public class DashboardController implements Initializable {

    Stage stage;
    Parent scene;
    Data data;

    @FXML
    private Button appointmentButton;

    @FXML
    private Button customersButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button reportButton;

    @FXML
    private Label upcomingdisplay;

    /**
     * This method changes the scene to the appointment landing view
     *
     * @param event the button press
     * @throws IOException the exception if the next view can't be found
     */
    @FXML
    void onAppointmentButtonClick(ActionEvent event) throws IOException {
        //Get source of event (button) and where located, cast event to button, then window to stage
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("appointment_landing_view.fxml"));
        stage.setScene(new Scene(scene));
    }

    /**
     * This method changes the scene to the customer landing view
     *
     * @param event the button press
     * @throws IOException the exception if the next view can't be found
     */
    @FXML
    void onCustomerButtonClick(ActionEvent event) throws IOException {
        //Get source of event (button) and where located, cast event to button, then window to stage
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("customer_landing_view.fxml"));
        stage.setScene(new Scene(scene));
    }

    /**
     * This method changes the scene to the login view
     *
     * @param event the button press
     * @throws IOException the exception if the next view can't be found
     */
    @FXML
    void onLogoutButtonClick(ActionEvent event) throws IOException {
        //Get source of event (button) and where located, cast event to button, then window to stage
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("login_view.fxml"));
        stage.setScene(new Scene(scene));
    }

    /**
     * This method changes the scene to the report view
     *
     * @param event the button press
     * @throws IOException the exception if the next view can't be found
     */
    @FXML
    void onReportButtonClick(ActionEvent event) throws IOException {
        //Get source of event (button) and where located, cast event to button, then window to stage
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("report_view.fxml"));
        stage.setScene(new Scene(scene));
    }

    /**
     * This method initializes the view and sets up any upcoming appointments to be displayed
     *
     * @param url the url
     * @param resourceBundle the resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data = new Data();
        String upcomingAppointments = "";
        try {
            for(Appointment appointment : data.getAppointments(4, data.getDate())){
                if(upcomingAppointments.length() == 0){
                    upcomingAppointments = "Upcoming Appointments: \n";
                }
                upcomingAppointments += "Appointment ID:  " + appointment.getAppointmentId() + " \nStart:  " + appointment.getStartDateTime() + "\n\n";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(upcomingAppointments.length() == 0){
            upcomingAppointments = "No Upcoming Appointments";
        }
        upcomingdisplay.setText(upcomingAppointments);

    }
}
