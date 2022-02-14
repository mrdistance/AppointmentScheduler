package com.example.appointmentscheduler;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ReportController{

    private Data data = new Data();
    Stage stage;

    @FXML
    private TextArea displayarea;

    @FXML
    void onAppointmentsButtonClick(ActionEvent event) throws SQLException {
        String report1 = "";
        for(String string : data.getReport(1)){
            report1+=string + "\n";
        }
        displayarea.setText(report1);
    }

    @FXML
    void onBackButtonClick(ActionEvent event) throws IOException {

    }

    @FXML
    void onContactButtonClick(ActionEvent event) throws SQLException {
        String report2 = "";
        for(String string : data.getReport(2)){
            report2+=string + "\n";
        }
        displayarea.setText(report2);
    }

    @FXML
    void onCustomersButtonClick(ActionEvent event) throws SQLException {
        String report3 = "";
        for(String string: data.getReport(3)){
            report3 += string + "\n";
        }
        displayarea.setText(report3);
    }

}
