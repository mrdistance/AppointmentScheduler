package com.example.appointmentscheduler;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class provides quick validation checking of credentials input by the user and logs all login attempts
 *
 * Class Login.java
 */

/**
 * @author Joshua Call
 */

public class Login {

    //Should pull data from users table in database to verify credentials, should gather info about users computer settings
    //like location and time zone (for time stamps)

    private String userName;
    private String password;
    private String timestamp;
    private ZoneId zoneId;
    ArrayList<User> users;

    /**
     * @param userName the username taken from the login form
     * @param password the password taken from the login form
     */
    public Login(String userName, String password){
        this.userName = userName;
        this.password = password;
        users = new ArrayList<>();
        timestamp = new SimpleDateFormat("yyyy.MM.dd     HH.mm.ss").format(new Date());
        zoneId = ZoneId.systemDefault();
        Database database = new Database(this.users);
    }

    /**
     * Determines if the username and password a user has entered are valid
     *
     * @param userName the username taken from the login form
     * @return true if valid credentials, false if invalid credentials
     */
    public boolean successful(String userName){
        User user = getUserByName(userName);
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
        for(User user : users){
            if(user.getUserName().equals(userName)){
                return user;
            }
        }
        return null;
    }

    /**
     * Logs successes/failures to login to the application
     *
     * @param result the result of the login attempt (success or fail)
     */
    private void logAttempt(String result){
        String logData = result + "     " + userName +  "     " + timestamp +"     " + zoneId;
        try (FileWriter writer = new FileWriter("login_activity.txt")){
            writer.append(logData);         //Write to file
        }catch (IOException ioe){
            System.out.println("Error loading file \"login_activity.txt\"");
        }
    }
}
