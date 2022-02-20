package com.example.appointmentscheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * This class provides the controller for the appointment edit view
 *
 * Class AppointmentEditController.java
 *
 * @author Joshua Call
 */

public class AppointmentEditController implements Initializable {

    Stage stage;
    Parent scene;
    private Appointment appointment;
    private Data data;
    private boolean update;
    User user;

    @FXML
    private TextField appointmentIDField;

    @FXML
    private Label appointmentIDLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private ChoiceBox<String> contactChoiceBox;

    @FXML
    private Label contactLabel;

    @FXML
    private ChoiceBox<Integer> customerIdChoiceBox;

    @FXML
    private Label customerIDLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField descriptionField;

    @FXML
    private Label descriptionLabel;

    @FXML
    private ChoiceBox<String> endChoiceBox;

    @FXML
    private Label endLabel;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private TextField locationField;

    @FXML
    private Label locationLabel;

    @FXML
    private Button saveButton;

    @FXML
    private ChoiceBox<String> startChoiceBox;

    @FXML
    private Label startLabel;

    @FXML
    private TextField titleField;

    @FXML
    private Label titleLabel;

    @FXML
    private ChoiceBox<String> typeChoiceBox;

    @FXML
    private Label typeLabel;

    @FXML
    private ChoiceBox<Integer> userIdChoiceBox;

    @FXML
    private Label userIDLabel;

    @FXML
    private Label userLabel;

    /**
     * This method leaves the current scene and returns to the appointment landing scene
     *
     * @param event the button press
     * @throws IOException the exception if the view can't be found
     */
    @FXML
    void onCancelButtonClick(ActionEvent event) throws IOException, SQLException {
        if(update){
            data.updateAppointment(appointment);
        }
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appointment_landing_view.fxml"));
        scene = loader.load();
        AppointmentLandingController controller = loader.getController();
        controller.getUser(user);
        stage.setScene(new Scene(scene));
    }

    /**
     * This method validates all user input, then either updates an appointment already in the database or
     * adds a new appointment to the database
     *
     * @param event the button press
     * @throws IOException the exception if the next view can't be found
     */
    @FXML
    void onSaveButtonClick(ActionEvent event) throws IOException, SQLException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        String errorText = "";
        boolean dataGood = true;
        if(titleField.getText().isEmpty()){
            errorText += "Appointment Title Required!\n";
            dataGood = false;
        }
        if(descriptionField.getText().isEmpty()){
            errorText += "Appointment Description Required!\n";
            dataGood = false;
        }
        if(locationField.getText().isEmpty()){
            errorText+="Appointment Location Required!\n";
            dataGood = false;
        }
        if(typeChoiceBox.getSelectionModel().getSelectedItem() == null){
            errorText += "Appointment Type Required!\n";
            dataGood = false;
        }

        if(contactChoiceBox.getSelectionModel().getSelectedItem()==null){
            errorText+="Appointment Contact Required!\n";
            dataGood = false;
        }
        if(customerIdChoiceBox.getSelectionModel().getSelectedItem() == null){
            errorText+="Customer ID Required!\n";
            dataGood = false;
        }
        if(datePicker.getValue() == null){
            errorText+="Date Required!\n";
            dataGood = false;
        }
        if(startChoiceBox.getSelectionModel().getSelectedItem() == null){
            errorText+="Start Time Required!\n";
            dataGood = false;
        }
        if(endChoiceBox.getSelectionModel().getSelectedItem() == null){
            errorText+="End Time Required!\n";
            dataGood = false;
        }
        if(dataGood) {

            String messageText = "";
            int contactID = 0;
            for(Contact contact1 : data.getContacts()){
                if(contact1.getContactName().equals(contactChoiceBox.getValue())){
                    contactID = contact1.getContactId();
                    break;
                }
            }
            if (update) {
                Appointment appointment = new Appointment(Integer.parseInt(appointmentIDField.getText()), titleField.getText(), descriptionField.getText(), locationField.getText(),
                        typeChoiceBox.getValue(), datePicker.getValue() + " " +startChoiceBox.getValue(), datePicker.getValue() + " " + endChoiceBox.getValue(),
                        customerIdChoiceBox.getValue(), userIdChoiceBox.getValue(), contactID);
                data.updateAppointment(appointment);
                messageText = "appointmentUpdateSuccess";

            } else {
                Appointment appointment = new Appointment(1, titleField.getText(), descriptionField.getText(), locationField.getText(),
                        typeChoiceBox.getValue(), datePicker.getValue() + " " +startChoiceBox.getValue(), datePicker.getValue() + " " + endChoiceBox.getValue(),
                        customerIdChoiceBox.getValue(), userIdChoiceBox.getValue(), contactID);
                data.addAppointment(appointment);
                messageText = "appointmentAddSuccess";
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("appointment_landing_view.fxml"));
            scene = loader.load();
            AppointmentLandingController controller = loader.getController();
            controller.setMessageLabel(messageText);
            controller.getUser(user);
            stage.setScene(new Scene(scene));
        }
        else{
            errorMessageLabel.setText(errorText);
        }
    }

    /**
     * This method enables the start time choice box and clears the previous time selections
     *
     * @param event the button press
     * @throws SQLException the exception if the connection fails with the database
     */
    @FXML
    void onDatePickerSelection(ActionEvent event) throws SQLException {
        startChoiceBox.setDisable(false);
        endChoiceBox.getSelectionModel().clearSelection();
        endChoiceBox.setValue(null);
        startChoiceBox.setValue(null);
        startChoiceBox.setItems(data.getStartTimes(datePicker.getValue()));
        startChoiceBox.getSelectionModel().clearSelection();


    }

    /**
     * This method enables the end time choice box and clears the previous time selections
     *
     * @param event the button press
     * @throws SQLException the exception if the connection fails with the database
     */
    @FXML
    void onStartTimeSelection(ActionEvent event) throws SQLException {
        endChoiceBox.getSelectionModel().clearSelection();
        endChoiceBox.setDisable(false);
        if(!(startChoiceBox.getValue() == null)) {
            endChoiceBox.setItems(data.getEndTimes(datePicker.getValue(), startChoiceBox.getValue()));
        }
    }

    /**
     * This method initializes the scene with data brought in from the previous scene (an appointment)
     * A LAMBDA expression is used to more concisely set restrictions on the date picker so that previous dates are not
     * able to be selected  This helps by removing the need to implement the specific interface and instead simply pass the
     * correct parameters to the function through the lambda.
     *
     * @param appointment the appointment selected in the previous scene
     * @throws SQLException the exception if the connection fails with the database
     */
    void initializeData(Appointment appointment) throws SQLException {
        this.appointment = appointment;
        data = new Data();
        update = true;
        if(appointment != null) {
            ObservableList<Integer> customerIds = FXCollections.observableArrayList();
            ObservableList<Integer> userIds = FXCollections.observableArrayList();
            try{
                for(User user : data.getUsers()){
                    userIds.add(user.getUserId());
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            try {
                for(Customer customer : data.getCustomers()){
                    customerIds.add(customer.getCustomerId());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                contactChoiceBox.setItems(data.getContactNames());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            appointmentIDField.setText(String.valueOf(appointment.getAppointmentId()));
            titleField.setText(appointment.getTitle());
            descriptionField.setText(appointment.getDescription());
            locationField.setText(appointment.getLocation());
            customerIdChoiceBox.setItems(customerIds);
            userIdChoiceBox.setItems(userIds);
            typeChoiceBox.setItems(data.getTypes());
            try {
                contactChoiceBox.setValue(data.getContactName(appointment.getContactId()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            typeChoiceBox.setValue(appointment.getType());
            customerIdChoiceBox.setValue(appointment.getCustomerId());
            userIdChoiceBox.setValue(appointment.getUserId());
            datePicker.setValue(data.getAppointmentDate(appointment));
            //Compare appointment date to today
            LocalDate today = LocalDate.now();
            LocalDate appointmentDate = LocalDate.parse(appointment.getStartDateTime(),  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if(appointmentDate.isBefore(today)) {
                startChoiceBox.setValue(appointment.getStartDateTime().split(" ")[1]);
                endChoiceBox.setValue(appointment.getEndDateTime().split(" ")[1]);
                startChoiceBox.setDisable(true);
                endChoiceBox.setDisable(true);
            }else{
                Appointment placeHolderAppointment = new Appointment(appointment.getAppointmentId(), appointment.getTitle(), appointment.getDescription(),
                        appointment.getLocation(), appointment.getType(), null, null, appointment.getCustomerId(), appointment.getUserId(), appointment.getContactId());
                data.updateAppointment(placeHolderAppointment);

                startChoiceBox.setItems(data.getStartTimes(datePicker.getValue()));
                startChoiceBox.setValue(appointment.getStartDateTime().split(" ")[1]);
                endChoiceBox.setItems(data.getEndTimes(datePicker.getValue(), startChoiceBox.getValue()));
                endChoiceBox.setValue(appointment.getEndDateTime().split(" ")[1]);
            }
            datePicker.setDayCellFactory(picker -> new DateCell(){
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate thisDay = LocalDate.now();
                    setDisable(empty || date.compareTo(thisDay) < 0);
                }
            });
        }
    }

    /**
     * This method initializes the scene sets up the choice box items
     * A LAMBDA expression is used to more concisely set restrictions on the date picker so that previous dates are not
     * able to be selected  This helps by removing the need to implement the specific interface and instead simply pass the
     * correct parameters to the function through the lambda.
     *
     * @param url a url
     * @param resourceBundle a resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data = new Data();
        update = false;
        datePicker.setDayCellFactory(picker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate thisDay = LocalDate.now();
                setDisable(empty || date.compareTo(thisDay) < 0);
            }
        });
        ObservableList<Integer> customerIds = FXCollections.observableArrayList();
        ObservableList<Integer> userIds = FXCollections.observableArrayList();
        try{
            for(User user : data.getUsers()){
                userIds.add(user.getUserId());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            for(Customer customer : data.getCustomers()){
                customerIds.add(customer.getCustomerId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            contactChoiceBox.setItems(data.getContactNames());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userIdChoiceBox.setItems(userIds);
        customerIdChoiceBox.setItems(customerIds);
        typeChoiceBox.setItems(data.getTypes());
    }

    public void getUser(User userlogin){
        ResourceBundle rb = ResourceBundle.getBundle("com/example/appointmentscheduler/language_files/rb");
        user = userlogin;
        userLabel.setText(rb.getString("usernamelabel") + ": " + user.getUserName());
        if(!update) {
            userIdChoiceBox.setValue(user.getUserId());
            userIdChoiceBox.setDisable(true);
        }
    }
}
