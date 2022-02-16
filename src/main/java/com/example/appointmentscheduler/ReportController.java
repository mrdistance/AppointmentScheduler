package com.example.appointmentscheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

/**
 * This class provides the controller for the report view
 *
 * Class ReportController.java
 *
 * @author Joshua Call
 */
public class ReportController{

    Stage stage;
    Parent scene;
    private Data data = new Data();

    @FXML
    private TextArea displayarea;

    /**
     * This method displays the appointments report in the text area
     *
     * @param event the button press
     * @throws SQLException the exception if the connection fails with the database
     */
    @FXML
    //todo use stringbuilder
    void onAppointmentsButtonClick(ActionEvent event) throws SQLException {
        String report1 = "";
        for(String string : data.getReport(1)){
            report1+=string + "\n";
        }
        displayarea.setText(report1);
    }

    /**
     * This method changes the scene back to the dashboard view
     *
     * @param event the button press
     * @throws IOException the exception if the next view can't be found
     */
    @FXML
    void onBackButtonClick(ActionEvent event) throws IOException {
        //Get source of event (button) and where located, cast event to button, then window to stage
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("dashboard_view.fxml"));
        stage.setScene(new Scene(scene));
    }

    /**
     * This method displays the contact schedules report in the text area
     *
     * @param event the button press
     * @throws SQLException the exception if the connection fails with the database
     */
    @FXML
    void onContactButtonClick(ActionEvent event) throws SQLException {
        String report2 = "";
       for(String string : data.getReport(2)){
            report2+=string + "\n";
        }
        displayarea.setText(report2);
    }

    /**
     * This method displays the customers report in the text area
     *
     * @param event the button press
     * @throws SQLException the exception if the connection fails with the database
     */
    @FXML
    void onCustomersButtonClick(ActionEvent event) throws SQLException {
        String report3 = "";
        for(String string: data.getReport(3)){
            report3 += string + "\n";
        }
        displayarea.setText(report3);
    }
}
