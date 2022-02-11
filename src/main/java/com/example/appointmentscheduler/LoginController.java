package com.example.appointmentscheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Data data = new Data();
    @FXML
    private Label errorMessage;

    @FXML
    private TextField password;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label userNameLabel;

    @FXML
    private Button loginButton;

    @FXML
    private Label userLocation;

    @FXML
    private TextField username;

    @FXML
    private Label titlelabel;


    @FXML
    void onLoginButtonClick(ActionEvent event) throws SQLException {
        User user = data.login(username.getText(), password.getText());
        if(user == null){
            errorMessage.setVisible(true);
            password.setText("");
        }else{
            errorMessage.setText("");
            //change windows to dashboard
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Locale.setDefault(new Locale("fr"));
        ResourceBundle rb = ResourceBundle.getBundle("com/example/appointmentscheduler/language_files/rb");
        titlelabel.setText(rb.getString("titlelabel"));
        titlelabel.setAlignment(Pos.CENTER);
        userNameLabel.setText(rb.getString("usernamelabel"));
        username.setPromptText(rb.getString("usernameprompt"));
        passwordLabel.setText(rb.getString("passwordlabel"));
        password.setPromptText(rb.getString("passwordprompt"));
        errorMessage.setText(rb.getString("errormessage"));
        errorMessage.setVisible(false);
        loginButton.setText(rb.getString("buttonlabel"));
        loginButton.setAlignment(Pos.CENTER);
        userLocation.setText(data.getLocation());
    }
}