package com.example.appointmentscheduler;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;

public class UserInterface {

    //Add main method, start method, some sort of loading message should be displayed until all data built

    //Instantiate Login Object once application is open, login uses database object which only pulls users table
    //to validate credentials, login object also establishes user location, time, language, etc to display on login screen


    //instantiate Data object once user successfully logs in
    //Data object will be running the show while the application is open behind the scenes


    private ArrayList<Country> countries;
    private ArrayList<FirstLevelDivision> divisions;
    private ArrayList<User> users;
    private ArrayList<Contact> contacts;
    private ObservableList<Customer> customers;
    private ObservableList<Appointment> appointments;


    //TODO create datepicker to get date from user, create 1 hashmap to get date chosen and display list of available start times for that day,
    //  Key == intervals every 15 minutes of working day converted from eastern time to user time, Value == true or false whether available
    //  or not.  create 1 hashmap to get start time from first map, and closest next appointment start time from calender, and display
    //  list of available end times that must fall between those two times so all other keys in map that don't fall within those can be
    //  set to false. all available times need to be converted from eastern time to usertime before displayed and saved, then
    //  when an appointment object is created it will store the time in user time, and when saved to the database it will be converted
    //  from usertime to uct time for the database
    private HashMap<String, Boolean> availableTimes;
}
