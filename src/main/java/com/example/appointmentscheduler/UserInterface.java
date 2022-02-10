package com.example.appointmentscheduler;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserInterface extends Application{

    //Add main method, start method, some sort of loading message should be displayed until all data built

    //Instantiate Login Object once application is open, login uses database object which only pulls users table
    //to validate credentials, login object also establishes user location, time, language, etc to display on login screen


    //instantiate Data object once user successfully logs in
    //Data object will be running the show while the application is open behind the scenes

    //This is where a private method will go to convert eastern into local for storing hashmap times in the appropriate
    //conversion of 8-10 eastern to local.  so put hashmap times as 8:00, 8:15, etc,  but call easterntolocal method on them
    //before displaying in the gui


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
        user = data.login("admin" , "admin");




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
        ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
        return rb.getString(fieldName);
    }

    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        //stage.setTitle("Hello!");
        //stage.setScene(scene);
        stage.show();

    }
}



