package com.example.appointmentscheduler;

import java.sql.*;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private Connection connection;
    //TODO add variables for database


    public Database() {

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

    public ObservableList<Customer> buildCustomers(PreparedStatement ps){
        //FIXME use private query method
        //  Query Customer table for id, name, address, postal code, phone, and division id
        //  Loop through Rows in Customers table and build list
        int id;
        String name;
        String address;
        String postalCode;
        String phone;
        int divisionId;
        Customer customer = new Customer(id, name, address, postalCode, phone, divisionId);
        customers.add(customer);
    }

    public ObservableList<Appointment> buildAppointments(PreparedStatement ps){
        //FIXME use private query method and pass ps
        //  Conduct sql datetime to java localdatetime conversion here -- private helper method
        //  Time needs to be converted into localdatetime for java, then compared to eastern time est for offic hours of business location
        //  Then changed into UTC to store in database,  Easiest to convert localDateTime to eastern time and database time to eastern time and do all
        //  comparisons on eastern time?  To store in application while user logged in need to display in localdatetime, but compare in eastern time.
        //  Eastern time is defined for business hours of 8am to 10pm,  Only need to convert those parameters to local time so that appointments set by
        //  User fall within specified hours, so only during appointment scheduling, can store everything else as localdatetime while in use/display until storing
        //  In database.  So convert from UTC of database into localdatetime,  Then for scheduling and updating appointments only display the correct
        //  conversion of eastern time to local time as options for available slots.
        //  Query Appointments table for id, name, address, postal code, phone, and division id
        //  Loop through Rows in Appointment table and build list
        int id;
        String title;
        String description;
        String location;
        String type;
        LocalDateTime start;
        LocalDateTime end;
        int customerId;
        int userId;
        int contactId;
        Appointment appointment = new Appointment(id, title, description, location, type, start, end, customerId,
                userId, contactId);
        appointments.add(appointment);
    }

    //TODO next 3 methods return 1 if success 0 if false

    public int insert(PreparedStatement ps){
        query(ps);
        try
            return 1 success
        catch
        return 0
    }

    public int update(PreparedStatement ps){
        query(ps);
    }

    public int delete(PreparedStatement ps){
        query(ps)
    }

    //TODO based on report number have specific prepared statement passed to private query method, that formats the returned
    //  data into a list to be passed back to the calling method
    public List<String> buildReport(PreparedStatement ps){
        this.query(ps)
    }


    //TODO convert anything dealing with times to localdatetime before building that object, only needs to be used in appointments?
    private LocalDateTime sqlToJavaTime(){
        
    }


    //FIXME query database based on prepared statement and return result set, calling method responsible for converting to
    //        correct format to build objects
    private ResultSet query(PreparedStatement ps){

    }


}
