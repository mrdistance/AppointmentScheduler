package com.example.appointmentscheduler;

public class UserInterface {

    //Add main method, start method, some sort of loading message should be displayed until all data built

    //exit button needs to delete everything in the update add delete arrays for customers and Appointments
    //Taken care of automatically because opening initiates database constructor which makes empty arrays;

    //Instantiate Login Object once application is open, login uses database object which only pulls users table
    //to validate credentials, login object also establishes user location, time, language, etc to display on login screen


    //instantiate Data object once user successfully logs in
    //Data object will be running the show while the application is open behind the scenes, needs to implement save function
    //From database class when user exits or presses save, which updates the database to mirror what the user is already seeing,
    //no need to requery database for that altered data because it is saved in the application first

    //Have methods to get local date time and location and language to correctly display login page

}
