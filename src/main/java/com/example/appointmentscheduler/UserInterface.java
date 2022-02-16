package com.example.appointmentscheduler;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.application.Application;

/**
 * This class provides the entry point to the application and launches the GUI
 *
 * Class UserInterface.java
 *
 * @author Joshua Call
 */



public class UserInterface extends Application{

    /**
     * The start of the program
     * Lambdas are located in the onDelete methods of the appointment landing and customer landing classes as
     * well as in the initialize data and initialize methods of the appointment edit class, the javadoc index.html is
     * located in the appointment scheduler package
     *
     * @param args start of program
     */
    public static void main(String[] args){
        launch(UserInterface.class);
    }

    /**
     * This method sets up the application GUI
     *
     * @param stage the stage for the GUI
     * @throws IOException the exception if the view file can't be found
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Appointment Scheduler 2.0");
        stage.setScene(scene);
        stage.show();
    }
}



