package com.example.appointmentscheduler;

import java.util.ArrayList;

public class Login {

    //Should pull data from users table in database to verify credentials, should gather info about users computer settings
    //like language and location and time zone and should be able to pass time zone into application?  Otherwise place
    //user computer settings variables in the interface itself, no need for class?  Convert time to correct format from utc of database
    //to user zone

    private String userName;
    private String password;
    private Database database;
    ArrayList<User> users;

    public Login(String userName, String password){
        this.userName = userName;
        this.password = password;
        users = new ArrayList<>();
        database = new Database(users);
    }

    public boolean successful(String userName){
        User user = getUserByName(userName);
        if(user != null){
            if(password == user.getPassword()) {
                return true;
            }
        }
        return false;
    }

    private User getUserByName(String userName){
        for(User user : users){
            if(user.getUserName() == userName){
                return user;
            }
        }
        return null;
    }
}
