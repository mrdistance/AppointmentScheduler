package com.example.appointmentscheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CustomerEditController {

    @FXML
    private Button cancelButton;

    @FXML
    private ChoiceBox<?> countryChoiceBox;

    @FXML
    private Label countryLabel;

    @FXML
    private TextField customerAddressField;

    @FXML
    private Label customerAddressLabel;

    @FXML
    private TextField customerIDField;

    @FXML
    private Label customerIDLabel;

    @FXML
    private TextField customerNameField;

    @FXML
    private Label customerNameLabel;

    @FXML
    private TextField customerPhoneField;

    @FXML
    private Label customerPhoneLabel;

    @FXML
    private TextField customerPostalCodeField;

    @FXML
    private Label customerPostalCodeLabel;

    @FXML
    private ChoiceBox<?> divisionChoiceBox;

    @FXML
    private Label divisionLabel;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private Button saveButton;

    @FXML
    void onCancelButtonClick(ActionEvent event) {

    }

    @FXML
    void onSaveButtonClick(ActionEvent event) {

    }

}
