package com.example.appointmentscheduler;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * This class provides quick validation checking of credentials input by the user and logs all login attempts
 * Establishes location and language for login screen, as well as stores which user is currently logged in
 *
 * Class Login.java
 */

/**
 * @author Joshua Call
 */

public class Login {

    //Should pull data from users table in database to verify credentials, should gather info about users computer settings
    //like location and time zone (for time stamps)

    private User user;
    private String timestamp;
    private String location;


    /**
     * Constructor for class initializes and builds users array from database
     * Sets up timestamp and current zone id as well as language
     */
    public Login(){

        timestamp = new SimpleDateFormat("yyyy.MM.dd     HH.mm.ss").format(new Date());
        location = TimeZone.getDefault().getID();    //Location of user
        //fixme add computer language check functionality to translate login screen
        Database database = new Database();
    }

    /**
     * Determines if the username and password a user has entered are valid
     *
     * @param userName the username taken from the login form
     * @return true if valid credentials, false if invalid credentials
     */
    public boolean successful(String userName, String password){
        this.user = getUserByName(userName);
        if(user != null){                                   //Username is valid and exists
            if(password.equals(user.getPassword())){        //Password is valid and matches username
                logAttempt("SUCCESS");
                return true;
            }
        }
        logAttempt("FAIL   ");
        return false;
    }

    /**
     * @param userName the username taken from the login form
     * @return the user in the database associated with the username
     */
    private User getUserByName(String userName){
        //TODO add method to query database based on username and build a user object
        //  make prepared statement and pass to build users
        //  list = database.buildUsers(get specific user based on username from user table);
        return null;
    }

    /**
     * Logs successes/failures to login to the application
     *
     * @param result the result of the login attempt (success or fail)
     */
    private void logAttempt(String result){
        String logData = result + "     " + user.getUserName() +  "     " + timestamp +"     " + location;
        try (FileWriter writer = new FileWriter("login_activity.txt")){
            writer.append(logData);         //Write to file
        }catch (IOException ioe){
            System.out.println("Error loading file \"login_activity.txt\"");
        }
    }
}
