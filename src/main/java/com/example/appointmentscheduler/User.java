package com.example.appointmentscheduler;

/**
 * This class provides an object to store users pulled from a database
 * Only requires getters as user creation is outside the scope of the application
 *
 * Class User.java
 */

/**
 * @author Joshua Call
 */

public class User {

    private int userId;
    private String userName;
    private String password;

    /**
     * @param userId the id to set
     * @param userName the name to set
     * @param password the password to set
     */
    public User(int userId, String userName, String password){
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    /**
     * @return the user id
     */
    public int getUserId(){
        return this.userId;
    }

    /**
     * @return the username
     */
    public String getUserName(){
        return this.userName;
    }

    /**
     * @return the password
     */
    public String getPassword(){
        return this.password;
    }

}
