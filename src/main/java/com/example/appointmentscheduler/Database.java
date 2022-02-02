package com.example.appointmentscheduler;

import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Database {

    //special constructor for login to get user id before initializing Data object, builds users only
    public Database(ArrayList<User> users){
        //Open connection and get users table to verify login info
        buildUsers(users);
    }

    //Special Constructor to build data lists, only use in data class
    public Database(ArrayList<Country> countries, ArrayList<FirstLevelDivision> divisions, ArrayList<User> users,
                      ArrayList<Contact> contacts, ObservableList<Customer> customers, ObservableList<Appointment> appointments){

        //Open Connection                                                         //Build lists--Draw from database
        //Establish Variable for database to be able to query within methods and pass as arguments
        buildCountries(countries);
        buildDivisions(divisions);
        buildUsers(users);
        buildContacts(contacts);
        buildCustomers(customers);
        buildAppointments(appointments);
        //Close Connection
    }

    //===================================Database Interaction Methods===================================================

    private void buildCountries(ArrayList<Country> countries){
        //Query Countries table for id and name
        //Loop through Rows in Country table and build list
        int id;
        String name;
        Country country = new Country(id, name);
        countries.add(country);
    }

    private void buildDivisions(ArrayList<FirstLevelDivision> divisions){
        //Query Divisions table for id, name, and country id
        //Loop through Rows in Division table and build list
        int id;
        String name;
        int countryId;
        FirstLevelDivision division = new FirstLevelDivision(id, name, countryId);
        divisions.add(division);
    }

    private void buildUsers(ArrayList<User> users){
        //Query Users table for id, name, and password
        //Loop through Rows in Users table and build list
        int id;
        String name;
        String password;
        User user = new User(id, name, password);
        users.add(user);
    }

    private void buildContacts(ArrayList<Contact> contacts){
        //Query Contacts table for id and name
        //Loop through Rows in Contacts table and build list
        int id;
        String name;
        Contact contact = new Contact(id, name);
        contacts.add(contact);
    }

    private void buildCustomers(ObservableList<Customer> customers){
        //Query Customer table for id, name, address, postal code, phone, and division id
        //Loop through Rows in Customers table and build list
        int id;
        String name;
        String address;
        String postalCode;
        String phone;
        int divisionId;
        Customer customer = new Customer(id, name, address, postalCode, phone, divisionId);
        customers.add(customer);
    }

    private void buildAppointments(ObservableList<Appointment> appointments){
        //Query Appointments table for id, name, address, postal code, phone, and division id
        //Loop through Rows in Appointment table and build list
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

    public void save(ArrayList<Customer> addedCustomers, ArrayList<Customer> updatedCustomers, ArrayList<Customer> deletedCustomers,
                     ArrayList<Appointment> addedAppointments, ArrayList<Appointment> updatedAppointments, ArrayList<Appointment> deletedAppointments){
        //Open Connection
        //Write all the data from Added, Updated, and Deleted Lists for both customers and appointments
        //Close Connection
    }
}
