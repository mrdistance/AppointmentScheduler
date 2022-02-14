package com.example.appointmentscheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AppointmentEditController {

    @FXML
    private TextField appointmentIDField;

    @FXML
    private Label appointmentIDLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private ChoiceBox<?> contactChoiceBox;

    @FXML
    private Label contactLabel;

    @FXML
    private TextField customerIDField;

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
    private ChoiceBox<?> endChoiceBox;

    @FXML
    private Label endLabel;

    @FXML
    private Label errorMessageField;

    @FXML
    private TextField locationField;

    @FXML
    private Label locationLabel;

    @FXML
    private Button saveButton;

    @FXML
    private ChoiceBox<?> startChoiceBox;

    @FXML
    private Label startLabel;

    @FXML
    private TextField titleField;

    @FXML
    private Label titleLabel;

    @FXML
    private ChoiceBox<?> typeChoiceBox;

    @FXML
    private Label typeLabel;

    @FXML
    private TextField userIDFIeld;

    @FXML
    private Label userIDLabel;

    @FXML
    void onCancelButtonClick(ActionEvent event) {

    }

    @FXML
    void onSaveButtonClick(ActionEvent event) {

    }

}
