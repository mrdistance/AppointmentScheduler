package com.example.appointmentscheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * This class provides the controller for the customer edit view
 *
 * Class CustomerEditController.java
 *
 * @author Joshua Call
 */

public class CustomerEditController implements Initializable {

    Stage stage;
    Parent scene;
    private Data data;
    boolean update = false;
    User user;

    @FXML
    private Button cancelButton;

    @FXML
    private ChoiceBox<String> countryChoiceBox;

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
    private ChoiceBox<String> divisionChoiceBox;

    @FXML
    private Label divisionLabel;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private Button saveButton;

    @FXML
    private Label userLabel;

    /**
     * This method leaves the current scene and returns to the customer landing scene
     *
     * @param event the button press
     * @throws IOException the exception if the view can't be found
     */
    @FXML
    void onCancelButtonClick(ActionEvent event) throws IOException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("customer_landing_view.fxml"));
        scene = loader.load();
        CustomerLandingController controller = loader.getController();
        controller.getUser(user);
        stage.setScene(new Scene(scene));
    }

    /**
     * This method validates all user input, then either updates a customer already in the database or
     * adds a new customer to the database
     *
     * @param event the button press
     * @throws IOException the exception if the next view can't be found
     */
    @FXML
    void onSaveButtonClick(ActionEvent event) throws IOException, SQLException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        String errorText = "";
        boolean dataGood = true;
        if(customerNameField.getText().isEmpty()){
            errorText += "Customer Name Required!\n";
            dataGood = false;
        }
        if(customerPhoneField.getText().isEmpty() || customerPhoneField.getText().length() < 10 || customerPhoneField.getText().length() > 10){
            errorText+="Customer Phone Required! Format 8885554444\n";
            dataGood = false;
        }else{
            try {
                Long.parseLong(customerPhoneField.getText());
            } catch (NumberFormatException nfe) {
                errorText += "Customer Phone Required! Format 8885554444\n";
                dataGood = false;
            }
        }
        if(customerAddressField.getText().isEmpty()){
            errorText+="Customer Address Required!\n";
            dataGood = false;
        }
        if(customerPostalCodeField.getText().isEmpty() || (customerPostalCodeField.getText().length() < 5 || customerPostalCodeField.getText().length() > 5)){
            errorText+="Customer Postal Code Required! Format 55555\n";
            dataGood = false;
        }else {
            try {
                Integer.parseInt(customerPostalCodeField.getText());
            } catch (NumberFormatException nfe) {
                errorText += "Customer Postal Code Required! Format 55555 number wrong\n";
                dataGood = false;
            }
        }

        if(countryChoiceBox.getSelectionModel().getSelectedItem()==null){
            errorText+="Customer Country Required!\n";
            dataGood = false;
        }
        if(divisionChoiceBox.getSelectionModel().getSelectedItem() == null){
            errorText+="Customer Division Required!\n";
            dataGood = false;
        }
        if(dataGood) {
            //build customer
            //Format Phone Field
            char[] phoneNotFormatted = customerPhoneField.getText().toCharArray();
            String phoneFormatted = "";
            int dashCount = 0;
            int dashPos = 0;
            int pos = 0;
            boolean dashPlaced = false;
            while(pos < phoneNotFormatted.length){
                if(dashCount < 2) {
                    if (dashPos % 3 == 0 && dashPos != 0 && !dashPlaced) {
                        phoneFormatted += "-";
                        dashCount++;
                        dashPlaced = true;
                        continue;
                    }
                }
                phoneFormatted += phoneNotFormatted[pos];
                pos++;
                dashPos++;
                dashPlaced = false;

            }
            FirstLevelDivision division = new FirstLevelDivision(1, "null", 1);
            String divisionName = divisionChoiceBox.getValue();
            for (FirstLevelDivision division1 : data.getDivisions()) {
                if (division1.getDivisionName().equals(divisionName)) {
                    division = division1;
                }
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("customer_landing_view.fxml"));
            scene = loader.load();
            CustomerLandingController controller = loader.getController();
            String messageText = "";
            if (update) {
                Customer customer = new Customer(Integer.parseInt(customerIDField.getText()), customerNameField.getText(), customerAddressField.getText(), customerPostalCodeField.getText(), phoneFormatted,
                        division.getDivisionId());
                data.updateCustomer(customer);
                messageText = "Customer Updated Successfully";
            } else {
                Customer customer = new Customer(1, customerNameField.getText(), customerAddressField.getText(), customerPostalCodeField.getText(), phoneFormatted,
                        division.getDivisionId());
                data.addCustomer(customer);
                messageText = "Customer Added Successfully";
            }
            controller.setMessageLabel(messageText);
            controller.getUser(user);
            stage.setScene(new Scene(scene));
        }
        else{
            errorMessageLabel.setText(errorText);
        }
    }

    /**
     * This method initializes the scene with data brought in from the previous scene (a customer)

     *
     * @param customer the appointment selected in the previous scene
     * @throws SQLException the exception if the connection fails with the database
     */
    void initializeData(Customer customer) throws SQLException {
        update = true;
        data = new Data();
        if(customer != null) {
            ObservableList<String> countryNames = FXCollections.observableArrayList();
            for(Country country : data.getCountries()){
                countryNames.add(country.getCountryName());
            }

            divisionChoiceBox.setDisable(false);
            FirstLevelDivision customerDivision = new FirstLevelDivision(1, "null", 1);
            Country customerCountry = new Country(1, "null");
            for (FirstLevelDivision division : data.getDivisions()) {
                if (division.getDivisionId() == customer.getDivisionId()) {
                    customerDivision = division;
                    break;
                }
            }
            for(Country country : data.getCountries()){
                if(country.getCountryId() == customerDivision.getCountryId()){
                    customerCountry = country;
                    break;
                }
            }
            ObservableList<String> divisionNames = FXCollections.observableArrayList();
            for(FirstLevelDivision division : data.getDivisions(customerCountry)){
                divisionNames.add(division.getDivisionName());
            }

            customerNameField.setText(customer.getCustomerName());
            customerPhoneField.setText(customer.getPhone().split("-")[0] + customer.getPhone().split("-")[1] + customer.getPhone().split("-")[2]);
            customerAddressField.setText(customer.getAddress());
            customerPostalCodeField.setText(customer.getPostalCode());
            customerIDField.setText(String.valueOf(customer.getCustomerId()));
            countryChoiceBox.setItems(countryNames);
            divisionChoiceBox.setItems(divisionNames);
            countryChoiceBox.setValue(customerCountry.getCountryName());
            divisionChoiceBox.setValue(customerDivision.getDivisionName());

        }
    }

    /**
     * This method initializes the scene sets up the choice box items
     *
     * @param url a url
     * @param resourceBundle a resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data = new Data();
        divisionChoiceBox.setDisable(true);
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        try {
            for(Country country : data.getCountries()){
                countryNames.add(country.getCountryName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        countryChoiceBox.setItems(countryNames);
    }

    /**
     * This method enables the division choice box and sets its items
     *
     * @param event the selection is made
     * @throws SQLException the exception if the connection fails with the database
     */
    public void onCountrySelection(ActionEvent event) throws SQLException {
        divisionChoiceBox.setDisable(false);
        String countryName = countryChoiceBox.getSelectionModel().getSelectedItem();
        ObservableList<Country> countries = data.getCountries();
        Country selectedCountry = new Country(1, "null");
        for(Country country : countries){
            if (country.getCountryName().equals(countryName)) {
                selectedCountry = country;
            }
        }
        ObservableList<String> divisions = FXCollections.observableArrayList();
        for(FirstLevelDivision division : data.getDivisions(selectedCountry)){
            divisions.add(division.getDivisionName());
        }
        divisionChoiceBox.setItems(divisions);
    }

    public void getUser(User userlogin){
        ResourceBundle rb = ResourceBundle.getBundle("com/example/appointmentscheduler/language_files/rb");
        user = userlogin;
        userLabel.setText(rb.getString("usernamelabel") + ": " + user.getUserName());
    }
}
