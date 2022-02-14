package com.example.appointmentscheduler;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Application;

public class UserInterface extends Application{

    private static ObservableList<Country> countries;
    private static ArrayList<FirstLevelDivision> divisions;
    private static ArrayList<User> users;
    private static ArrayList<Contact> contacts;
    private static ObservableList<Customer> customers;
    private static ObservableList<Appointment> appointments;
    private static User user;
    private static Data data;

    public static void main(String[] args) throws SQLException {
        data = new Data();
        launch(UserInterface.class);
        //user = LoginController.user;




        for(String line : data.getReport(3)){
            System.out.println(line);
        }



    }


    //login button.onclick  User user = data.login(usernameTF.getText(), passwordTF.getText());
    //if user == null, error message, clear password field
    //else move to next screen and save user info

    //todo make sure translates all login page text : (do for all pages?, en spanish and french?) label.setText(translate(fieldname);
    public String translate(String fieldName){
        //Locale.setDefault(new Locale("fr"));
        ResourceBundle rb = ResourceBundle.getBundle("com/example/appointmentscheduler/language_files/rb");
        return rb.getString(fieldName);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("dashboard_view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Appointment Scheduler 2.0");
        stage.setScene(scene);
        stage.show();

    }
}



