package com.example.appointmentscheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class provides the controller for the report view
 *
 * Class ReportController.java
 *
 * @author Joshua Call
 */
public class ReportController implements Initializable {

    Stage stage;
    Parent scene;
    private Data data = new Data();
    User user;

    @FXML
    private TextArea displayArea;

    @FXML
    private Button appointmentsButton;

    @FXML
    private Button contactsButton;

    @FXML
    private Button customersButton;

    @FXML
    private Button backButton;

    @FXML
    private Label userLabel;

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
        displayArea.setText(report1);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard_view.fxml"));
        scene = loader.load();
        DashboardController controller = loader.getController();
        controller.getUser(user);
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
        displayArea.setText(report2);
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
        displayArea.setText(report3);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResourceBundle rb = ResourceBundle.getBundle("com/example/appointmentscheduler/language_files/rb");
        appointmentsButton.setText(rb.getString("appointments"));
        contactsButton.setText(rb.getString("schedules"));
        customersButton.setText(rb.getString("customers"));
        backButton.setText(rb.getString("back"));
        displayArea.setPromptText(rb.getString("display"));
    }

    public void getUser(User userlogin){
        ResourceBundle rb = ResourceBundle.getBundle("com/example/appointmentscheduler/language_files/rb");
        user = userlogin;
        userLabel.setText(rb.getString("usernamelabel") + ": " + user.getUserName());
    }
}
