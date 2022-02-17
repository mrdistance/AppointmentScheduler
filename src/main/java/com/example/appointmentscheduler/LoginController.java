package com.example.appointmentscheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class provides the controller for the login view
 *
 * Class LoginController.java
 *
 * @author Joshua Call
 */
public class LoginController implements Initializable {

    Stage stage;
    Parent scene;

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

    /**
     * This method validates that the username and password are correct and displays an error message if necessary
     * an changes the scene to the dashboard view
     *
     * @param event the button press
     * @throws SQLException the exception if the connection fails with the database
     * @throws IOException the exception if the next view can't be found
     */
    @FXML
    void onLoginButtonClick(ActionEvent event) throws SQLException, IOException {
        User user = data.login(username.getText(), password.getText());

        if(user == null){
            errorMessage.setVisible(true);
            password.setText("");
        }else{
            errorMessage.setText("");
            //Get source of event (button) and where located, cast event to button, then window to stage
            stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard_view.fxml"));
            scene = loader.load();
            DashboardController controller = loader.getController();
            controller.getUser(user);
            stage.setScene(new Scene(scene));
        }
    }

    /**
     * This method initializes the view and sets up the location as well as specifies which language to display
     * the labels and buttons in
     *
     * @param url the url
     * @param resourceBundle the resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Locale.setDefault(new Locale("fr"));
        ResourceBundle rb = ResourceBundle.getBundle("com/example/appointmentscheduler/language_files/rb");
        titlelabel.setText(rb.getString("titlelabel"));
        userNameLabel.setText(rb.getString("usernamelabel"));
        username.setPromptText(rb.getString("usernameprompt"));
        passwordLabel.setText(rb.getString("passwordlabel"));
        password.setPromptText(rb.getString("passwordprompt"));
        errorMessage.setText(rb.getString("errormessage"));
        errorMessage.setVisible(false);
        loginButton.setText(rb.getString("buttonlabel"));
        userLocation.setText(data.getLocation());
    }
}