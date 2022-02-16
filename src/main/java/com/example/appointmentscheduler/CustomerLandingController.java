package com.example.appointmentscheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * This class provides the controller for the customer landing view
 *
 * Class CustomerLandingController.java
 *
 * @author Joshua Call
 */
public class CustomerLandingController implements Initializable {

    Stage stage;
    Parent scene;
    private Data data;
    private Customer customer;

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<?, ?> addressField;

    @FXML
    private Label customerLabel;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<?, ?> divisionField;

    @FXML
    private Button homeButton;

    @FXML
    private TableColumn<?, ?> idField;

    @FXML
    private Label messageLabel;

    @FXML
    private TableColumn<?, ?> nameField;

    @FXML
    private TableColumn<?, ?> phoneField;

    @FXML
    private TableColumn<?, ?> postalCodeField;

    @FXML
    private Button updateButton;

    /**
     * This method changes the scene to the customer edit view
     *
     * @param event the button press
     * @throws IOException the exception if the next view can't be found
     */
    @FXML
    void onAddButtonClick(ActionEvent event) throws IOException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("customer_edit_view.fxml"));
        stage.setScene(new Scene(scene));
    }

    /**
     * This method verifies a customer is selected then verifies that the user wishes to delete it then deletes it
     * Two LAMBDAS are used here to more concisely display and hide the popup message that verifies the user wishes to delete
     * the selected customer
     *
     * @param event the button press
     * @throws SQLException the exception if the connection fails with the database
     */
    @FXML
    void onDeleteButtonClick(ActionEvent event) throws IOException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        if(customerTable.getSelectionModel().getSelectedItem() != null){
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            Popup popup = new Popup();
            AnchorPane popupPane = new AnchorPane();
            Label popupMessage = new Label("Are you sure you wish to delete Customer " + customer.getCustomerId()  + "?");
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
                    data.deleteCustomer(customerTable.getSelectionModel().getSelectedItem());
                    messageLabel.setText("Customer "  +customer.getCustomerId() + " Deleted Successfully");
                    popup.hide();
                    customerTable.setItems(data.getCustomers());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            popupNoButton.setOnAction((event2) -> {
                popup.hide();
                messageLabel.setText("");
            });
        }else{
            messageLabel.setText("Select the Customer You Wish to Delete");
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
        scene = FXMLLoader.load(getClass().getResource("dashboard_view.fxml"));
        stage.setScene(new Scene(scene));
    }

    /**
     * This method changes the scene to the customer edit view and pre-populates all the fields with the data of
     * the selected customer
     *
     * @param event the button press
     * @throws IOException the exception if the view can't be found
     * @throws SQLException the exception if the connection fails with the database
     */
    @FXML
    void onUpdateButtonClick(ActionEvent event) throws IOException, SQLException {
        stage =(Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("customer_edit_view.fxml"));
        scene = loader.load();
        CustomerEditController controller = loader.getController();
        if(customerTable.getSelectionModel().getSelectedItem() != null) {
            customer = customerTable.getSelectionModel().getSelectedItem();
            controller.initializeData(customer);
            stage.setScene(new Scene(scene));

        }else{
            messageLabel.setText("Select the Customer You Wish to Modify");
        }
    }

    /**
     * This method initializes the view and sets up all the values in the table view
     *
     * @param url the url
     * @param resourceBundle the resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data = new Data();
        idField.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameField.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressField.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeField.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneField.setCellValueFactory(new PropertyValueFactory<>("phone"));
        //todo make division display name of division
        divisionField.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
        try {
            customerTable.setItems(data.getCustomers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method allows for the customer edit controller to change the text in this view after a customer is
     * updated or added
     *
     * @param text the text to set the label to
     * @throws SQLException the exception if the connection fails with the database
     */
    public void setMessageLabel(String text) throws SQLException {
        messageLabel.setText(text);
        customerTable.setItems(data.getCustomers());
    }
}
