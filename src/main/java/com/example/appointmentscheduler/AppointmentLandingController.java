package com.example.appointmentscheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.ResourceBundle;


/**
 * This class provides the controller for the appointment landing view
 *
 * Class AppointmentLandingController.java
 *
 * @author Joshua Call
 */

public class AppointmentLandingController implements Initializable {

    Stage stage;
    Parent scene;
    Data data;
    Appointment appointment;
    User user;

    @FXML
    private Button addButton;

    @FXML
    private RadioButton allButton;

    @FXML
    private TableColumn<?, ?> appointmentIDField;

    @FXML
    private Label appointmentLabel;

    @FXML
    private TableColumn<?, ?> contactField;

    @FXML
    private TableColumn<?, ?> customerIDField;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<?, ?> descriptionField;

    @FXML
    private TableColumn<?, ?> endField;

    @FXML
    private Button homeButton;

    @FXML
    private TableColumn<?, ?> locationField;

    @FXML
    private Label messageLabel;

    @FXML
    private RadioButton monthButton;

    @FXML
    private TableColumn<?, ?> startField;

    @FXML
    private TableColumn<?, ?> titleField;

    @FXML
    private TableColumn<?, ?> typeField;

    @FXML
    private Button updateButton;

    @FXML
    private TableColumn<?, ?> userIDField;

    @FXML
    private RadioButton weekButton;

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private Label userLabel;

    /**
     * This method changes the scene to the appointment edit view
     *
     * @param event the button press
     * @throws IOException the exception if the next view can't be found
     */
    @FXML
    void onAddButtonClick(ActionEvent event) throws IOException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appointment_edit_view.fxml"));
        scene = loader.load();
        AppointmentEditController controller = loader.getController();
        controller.getUser(user);
        stage.setScene(new Scene(scene));
    }

    /**
     * This method sets the items for the table view to show all appointments
     *
     * @param event the toggle selected
     * @throws SQLException the exception if the connection fails with the database
     */
    @FXML
    void onAllButtonSelected(ActionEvent event) throws SQLException {
        appointmentTable.setItems(data.getAppointments(0, data.getDate()));
        messageLabel.setText("");
    }

    /**
     * This method verifies an appointment is selected then verifies that the user wishes to delete it then deletes it
     * Two LAMBDAS are used here to more concisely display and hide the popup message that verifies the user wishes to delete
     * the selected appointment
     *
     * @param event the button press
     * @throws SQLException the exception if the connection fails with the database
     */
    @FXML
    void onDeleteButtonClick(ActionEvent event) throws SQLException {
        ResourceBundle rb = ResourceBundle.getBundle("com/example/appointmentscheduler/language_files/rb");
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        if(appointmentTable.getSelectionModel().getSelectedItem() != null){
            Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
            Popup popup = new Popup();
            AnchorPane popupPane = new AnchorPane();
            Label popupMessage = new Label(rb.getString("appointmentPopup") + " " + appointment.getAppointmentId() + ", " + appointment.getType() + "?");
            AnchorPane.setTopAnchor(popupMessage, 20.0);
            AnchorPane.setLeftAnchor(popupMessage, 20.0);
            AnchorPane.setRightAnchor(popupMessage, 20.0);
            Button popupYesButton = new Button("Yes");
            AnchorPane.setTopAnchor(popupYesButton, 80.0);
            AnchorPane.setLeftAnchor(popupYesButton, 110.0);
            Button popupNoButton = new Button("No");
            AnchorPane.setTopAnchor(popupNoButton, 80.0);
            AnchorPane.setLeftAnchor(popupNoButton, 160.0);
            popupPane.setPadding(new Insets(20, 20, 20, 20));
            popupPane.getChildren().addAll(popupMessage, popupYesButton, popupNoButton);
            popup.getContent().add(popupPane);
            popup.show(stage);
            popup.getScene().getRoot().setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-radius: 10px");
            popupYesButton.setOnAction((event1) -> {
                try {
                    data.deleteAppointment(appointmentTable.getSelectionModel().getSelectedItem());
                    messageLabel.setText(rb.getString("appointment")  + " "  +appointment.getAppointmentId() + ", " + appointment.getType() + " " + rb.getString("appointmentDeleteSuccess"));
                    popup.hide();
                    appointmentTable.setItems(data.getAppointments(0, data.getDate()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            popupNoButton.setOnAction((event2) -> {
                popup.hide();
                messageLabel.setText("");
            });
        }else{
            messageLabel.setText(rb.getString("appointmentWishDelete"));
        }
    }

    /**
     * This method changes the scene back to the dashboard view
     *
     * @param event the button press
     * @throws IOException the exception if the next view can't be found
     */
    @FXML
    void onHomeButtonClick(ActionEvent event) throws IOException {
        //Get source of event (button) and where located, cast event to button, then window to stage
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard_view.fxml"));
        scene = loader.load();
        DashboardController controller = loader.getController();
        controller.getUser(user);
        stage.setScene(new Scene(scene));
    }

    /**
     * This method sets the items for the table view to show appointments for the month
     *
     * @param event the toggle selected
     * @throws SQLException the exception if the connection fails with the database
     */
    @FXML
    void onMonthButtonSelected(ActionEvent event) throws SQLException {
        appointmentTable.setItems(data.getAppointments(1, data.getDate()));
        messageLabel.setText("");
    }

    /**
     * This method changes the scene to the appointment edit view and pre-populates all the fields with the data of
     * the selected appointment
     *
     * @param event the button press
     * @throws IOException the exception if the view can't be found
     * @throws SQLException the exception if the connection fails with the database
     */
    @FXML
    void onUpdateButtonClick(ActionEvent event) throws IOException, SQLException {
        ResourceBundle rb = ResourceBundle.getBundle("com/example/appointmentscheduler/language_files/rb");
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appointment_edit_view.fxml"));
        scene = loader.load();
        AppointmentEditController controller = loader.getController();
        if(appointmentTable.getSelectionModel().getSelectedItem() != null) {
            appointment = appointmentTable.getSelectionModel().getSelectedItem();
            controller.initializeData(appointment);
            controller.getUser(user);
            stage.setScene(new Scene(scene));

        }else{
            messageLabel.setText(rb.getString("appointmentModify"));
        }
    }

    /**
     * This method sets the items for the table view to show appointments for the week
     *
     * @param event the toggle selected
     * @throws SQLException the exception if the connection fails with the database
     */
    @FXML
    void onWeekButtonSelected(ActionEvent event) throws SQLException {
        appointmentTable.setItems(data.getAppointments(2, data.getDate()));
        messageLabel.setText("");
    }

    /**
     * This method initializes the view and sets up all the values in the table view
     *
     * @param url the url
     * @param resourceBundle the resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResourceBundle rb = ResourceBundle.getBundle("com/example/appointmentscheduler/language_files/rb");
        allButton.setText(rb.getString("all"));
        weekButton.setText(rb.getString("week"));
        monthButton.setText(rb.getString("month"));
        appointmentLabel.setText(rb.getString("appointments"));
        addButton.setText(rb.getString("add"));
        updateButton.setText(rb.getString("update"));
        deleteButton.setText(rb.getString("delete"));
        homeButton.setText(rb.getString("home"));

        messageLabel.setText("");

        data = new Data();
        appointmentIDField.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleField.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionField.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationField.setCellValueFactory(new PropertyValueFactory<>("location"));
        //todo change to contact name
        contactField.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        typeField.setCellValueFactory(new PropertyValueFactory<>("type"));
        startField.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endField.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        customerIDField.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIDField.setCellValueFactory(new PropertyValueFactory<>("userId"));
        //todo make division display name of division
        try {
            appointmentTable.setItems(data.getAppointments(0, data.getDate()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method allows for the appointment edit controller to change the text in this view after an appointment is
     * updated or added
     *
     * @param text the text to set the label to
     * @throws SQLException the exception if the connection fails with the database
     */
    public void setMessageLabel(String text) throws SQLException {
        ResourceBundle rb = ResourceBundle.getBundle("com/example/appointmentscheduler/language_files/rb");
        messageLabel.setText(rb.getString(text));
        appointmentTable.setItems(data.getAppointments(0, data.getDate()));
    }

    public void getUser(User userlogin){
        ResourceBundle rb = ResourceBundle.getBundle("com/example/appointmentscheduler/language_files/rb");
        user = userlogin;
        userLabel.setText(rb.getString("usernamelabel") + ": " + user.getUserName());
    }
}