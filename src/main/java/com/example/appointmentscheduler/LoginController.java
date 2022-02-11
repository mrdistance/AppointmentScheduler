package com.example.appointmentscheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Data data = new Data();
    @FXML
    private Label errorMessage;

    @FXML
    private TextField password;

    @FXML
    private Label userLocation;

    @FXML
    private TextField username;


    @FXML
    void onLoginButtonClick(ActionEvent event) throws SQLException {
        User user = data.login(username.getText(), password.getText());
        if(user == null){
            errorMessage.setText("Invalid Username or Password");
            password.setText("");
        }else{
            errorMessage.setText("");
            //change windows to dashboard
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLocation.setText(data.getLocation());
    }
}