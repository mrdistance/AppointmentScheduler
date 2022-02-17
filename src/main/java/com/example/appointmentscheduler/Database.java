package com.example.appointmentscheduler;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * This class provided our basis for interaction with the database.  The methods receive prepared statements as parameters
 * and return lists of the requested data or pass specified data back to the database.  All formatting and conversions take
 * place within this class to reduce the complexity of other class.  Data will be passed to this class as a formatted prepared
 * statement, and will be changed from a result set into objects before being returned to the calling class
 *
 * Class Database.java
 */

/**
 * @author Joshua Call
 */

public class Database {


    //TODO add variables for database, these are a test, copied from jdbc helper
    private final String protocol = "jdbc";
    private final String vendor = ":mysql:";
    private final String location = "//localhost/";
    private final String databaseName = "client_schedule";
    private final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference

    private static final String userName = "sqlUser"; // vm Username
    private static String password = "Passw0rd!"; // vm Password
    private Connection connection;  // Connection Interface


    //Constructor
    public Database() {

    }

    //========================================Connection Methods========================================================

    /**
     * This method establishes a connection with the database
     *
     * @return the connection that was established
     */
    public Connection getConnection(){
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }

        return this.connection;
    }

    /**
     * This method closes the connection with the database
     */
    public void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    //===========================================Data Building Methods===================================================

    /**
     * This method pulls countries from the database and builds them into an observable list
     *
     * @param ps the prepared statement to specify which countries to pull
     * @return the list of countries pulled from the database
     * @throws SQLException the exception if connection fails
     */
    public ObservableList<Country> buildCountries(PreparedStatement ps) throws SQLException{
        ObservableList<Country> countries = FXCollections.observableArrayList();
        ResultSet result = query(ps);
        while(result.next()){
            int id = result.getInt(1);
            String name = result.getString(2);
            Country country = new Country(id, name);
            countries.add(country);
        }
        return countries;
    }

    /**
     * This method pulls divisions from the database and builds them into an observable list
     *
     * @param ps the prepared statement to specify which divisions to pull
     * @return the list of divisions pulled from the database
     * @throws SQLException the exception if connection fails
     */
    public ObservableList<FirstLevelDivision> buildDivisions(PreparedStatement ps) throws SQLException{
        ObservableList<FirstLevelDivision> divisions = FXCollections.observableArrayList();
        ResultSet result = query(ps);
        while(result.next()) {
            int id = result.getInt(1);
            String name = result.getString(2);
            int cId = result.getInt(7);
            FirstLevelDivision division = new FirstLevelDivision(id, name, cId);
            divisions.add(division);
        }
        return divisions;
    }

    /**
     * This method pulls a list of all the users from the database
     *
     * @param ps the prepared statement
     * @return the list of users
     * @throws SQLException the exception if connection fails
     */
    public ObservableList<User> buildUsers(PreparedStatement ps) throws SQLException{
        ObservableList<User> users = FXCollections.observableArrayList();
        ResultSet result = query(ps);
        while(result.next()){
            int id = result.getInt(1);
            String name = result.getString(2);
            String password = result.getString(3);
            User user = new User(id, name, password);
            users.add(user);
        }
        return users;
    }

    /**
     * This method pulls the corresponding user from the database to validate credentials and track logged-in user
     *
     * @param ps the prepared statement to specify which user to look for
     * @return the user if found or null
     * @throws SQLException the exception if connection fails
     */
    public User buildUser(PreparedStatement ps) throws SQLException{
        ResultSet result = query(ps);
        if(!result.next()){
            return null;
        }
        int id = result.getInt(1);
        String name = result.getString(2);
        String password = result.getString(3);
        return new User(id, name, password);
    }

    /**
     * This method pulls contacts from the database and builds them into an observable list
     *
     * @param ps the prepared statement to specify which contacts to pull
     * @return the list of contacts pulled from the database
     * @throws SQLException the exception if connection fails
     */
    public ObservableList<Contact> buildContacts(PreparedStatement ps) throws SQLException{
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        ResultSet result = query(ps);
        while(result.next()){
            int id = result.getInt(1);
            String name = result.getString(2);
            Contact contact = new Contact(id, name);
            contacts.add(contact);
        }
        return contacts;
    }

    /**
     * This method pulls customers from the database and builds them into an observable list
     *
     * @param ps the prepared statement to specify which customers to pull
     * @return the list of customers pulled from the databse
     * @throws SQLException the exception if the connection fails
     */
    public ObservableList<Customer> buildCustomers(PreparedStatement ps) throws SQLException{
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        ResultSet result = query(ps);
        while(result.next()){
            int id = result.getInt(1);
            String name = result.getString(2);
            String address = result.getString(3);
            String postalCode = result.getString(4);
            String phone = result.getString(5);
            int divisionId = result.getInt(10);
            Customer customer = new Customer(id, name, address, postalCode, phone, divisionId);
            customers.add(customer);
        }
       return customers;
    }

    /**
     * This method pulls appointments from the database and builds them into an observable list
     *
     * @param ps the prepared statement to specify which appointments to pull
     * @return the list of appointments pulled from the database
     * @throws SQLException the exception if the connection fails
     */
    public ObservableList<Appointment> buildAppointments(PreparedStatement ps) throws SQLException{

        ObservableList<Appointment> appointments= FXCollections.observableArrayList();
        ResultSet result = query(ps);
        while(result.next()){
            int id = result.getInt(1);
            String title = result.getString(2);
            String desc = result.getString(3);
            String location = result.getString(4);
            String type = result.getString(5);
            String start = result.getString(6);
            String end = result.getString(7);
            int customerId= result.getInt(12);
            int userId = result.getInt(13);
            int contactId = result.getInt(14);

            String localStart = utcToLocal(start);
            String localEnd = utcToLocal(end);

            Appointment appointment = new Appointment(id, title, desc, location, type, localStart, localEnd, customerId, userId, contactId);
            appointments.add(appointment);

        }
        return appointments;
    }

    //=============================================Time Conversion Method===============================================

    /**
     * This method converts the times taken from the database into local time before building appointment objects
     *
     * @param utcString the time string in database time
     * @return the time string in local time
     */
    private String utcToLocal(String utcString){
        ZoneId localZone = ZoneId.of(TimeZone.getDefault().getID());         //"This gets local timezone
        ZoneId utcZone = ZoneId.of("UTC");                                       //This is db timezone
        LocalDateTime dbTime = LocalDateTime.parse(utcString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ZonedDateTime utcAppointmentTime = dbTime.atZone(utcZone);
        ZonedDateTime localAppointmentTime = utcAppointmentTime.withZoneSameInstant(localZone);
        return localAppointmentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    //===========================================Query Execution Methods================================================

    /**
     * This method takes a prepared statement and executes a query to the database then returns a result set
     * that is formatted by the calling method before any objects are built
     *
     * @param ps the prepared statement for the query
     * @return the result set
     * @throws SQLException the exception if the connection fails
     */
    private ResultSet query(PreparedStatement ps) throws SQLException{
        return ps.executeQuery();
    }

    /**
     * This method takes a prepared statement and inserts, updates, or deletes items in the database, returns a number
     * based on how many rows were affected
     *
     * @param ps the prepared statement for the query
     * @return the number of rows affected
     * @throws SQLException the exception if the connection fails
     */
    public int update(PreparedStatement ps) throws SQLException{
        return ps.executeUpdate();
    }




}
