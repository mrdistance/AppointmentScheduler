package com.example.appointmentscheduler;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    private final String userName = "root"; // my pc Username
    private String password = "Them@1lman12."; // my pc Password
    //private static final String userName = "sqlUser"; // vm Username
    //private static String password = "Passw0rd!"; // vm Password
    private Connection connection;  // Connection Interface


    //Constructor
    public Database() {

    }

    //========================================Connection Methods========================================================

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

    //===================================Database Interaction Methods===================================================



    public ArrayList<Country> buildCountries(PreparedStatement ps){
        //FIXME query will get data in db this method will convert to correct format and object/s for local use
        //  user private query method
        //  Query Countries table for id and name
        //  Loop through Rows in Country table and build list
        int id;
        String name;
        Country country = new Country(id, name);
        countries.add(country);
    }

    public ArrayList<FirstLevelDivision> buildDivisions(PreparedStatement ps){
        //TODO use private query method
        //Query Divisions table for id, name, and country id
        //Loop through Rows in Division table and build list
        int id;
        String name;
        int countryId;
        FirstLevelDivision division = new FirstLevelDivision(id, name, countryId);
        divisions.add(division);
    }

    public ArrayList<User> buildUsers(PreparedStatement ps){
        //FIXME user private query method
        //  Query Users table for id, name, and password
        //  Loop through Rows in Users table and build list
        int id;
        String name;
        String password;
        User user = new User(id, name, password);
        users.add(user);
    }

    public ArrayList<Contact> buildContacts(PreparedStatement ps){
        //FIXME use private query method
        //  Query Contacts table for id and name
        //  Loop through Rows in Contacts table and build list
        int id;
        String name;
        Contact contact = new Contact(id, name);
        contacts.add(contact);
    }

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


    //TODO based on report number have specific prepared statement passed to private query method, that formats the returned
    //  data into a list to be passed back to the calling method
    public List<String> buildReport(PreparedStatement ps){
        this.query(ps)
    }


    //TODO convert anything dealing with times to localdatetime before building that object, only needs to be used in appointments?
    private String utcToLocal(String utcString){
        ZoneId localZone = ZoneId.of(TimeZone.getDefault().getID());         //"This gets local timezone
        ZoneId utcZone = ZoneId.of("UTC");                                       //This is db timezone
        LocalDateTime dbTime = LocalDateTime.parse(utcString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ZonedDateTime utcAppointmentTime = dbTime.atZone(utcZone);
        ZonedDateTime localAppointmentTime = utcAppointmentTime.withZoneSameInstant(localZone);
        return localAppointmentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


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




}
