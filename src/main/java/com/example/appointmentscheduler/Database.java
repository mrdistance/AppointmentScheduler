package com.example.appointmentscheduler;

/**
 * This class provides an object to store all the database information utilized while the app is running
 * A connection will be opened and data will be pulled from the database once the object is initialized
 * Data will be stored within data structures and organized based on type, then the connection will close
 * The connection will reopen as the user exits the program and any modifications will be saved back to the database
 * This allows for offline use of the program and only requires a connection when opening or closing the app
 *
 * Class Database.java
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * @author Joshua Call
 */

public class Database {

    //============================================================================================================
    //Might need global datetime variable to filter appointments based on how close they come to the current time
    //============================================================================================================

    //Track who is using the app to correctly populate appointments and customers
    private int userId;
    //Add variables needed for jdbc and connecting and interacting with database
    private ArrayList<Country> countries;
    private ArrayList<FirstLevelDivision> divisions;
    private ArrayList<User> users;
    private ArrayList<Contact> contacts;
    private ObservableList<Customer> customers;
    private ObservableList<Appointment> appointments;

    //Lists to hold changes that need to be pushed back to database
    private ArrayList<Customer> addedCustomers;
    private ArrayList<Customer> deletedCustomers;
    private ArrayList<Customer> updatedCustomers;
    private ArrayList<Appointment> addedAppointments;
    private ArrayList<Appointment> deletedAppointments;
    private ArrayList<Appointment> updatedAppointments;

    public Database(int userId){
        this.userId = userId;

        //Initialize lists
        countries = new ArrayList<>();
        divisions = new ArrayList<>();
        users = new ArrayList<>();
        contacts = new ArrayList<>();
        customers = FXCollections.observableArrayList();
        appointments = FXCollections.observableArrayList();

        //Lists to hold updates to be sent back to database, only used internally
        addedCustomers = new ArrayList<>();
        deletedCustomers = new ArrayList<>();
        updatedCustomers = new ArrayList<>();
        addedAppointments = new ArrayList<>();
        deletedAppointments = new ArrayList<>();
        updatedAppointments = new ArrayList<>();

        //Connect to the database and build objects
        build();
    }


    //============================================List Getter Methods============================================

    public ArrayList<Country> getCountries(){
        return this.countries;
    }

    public ArrayList<FirstLevelDivision> getDivisions(){
        return this.divisions;
    }

    public ArrayList<User> getUsers(){
        return this.users;
    }

    public ArrayList<Contact> getContacts(){
        return this.contacts;
    }

    public ObservableList<Customer> getCustomers(){
        return this.customers;
    }

    public ObservableList<Appointment> getAppointments(int filterLevel){
        if(filterLevel == 0) {
            ObservableList<Appointment> allList = FXCollections.observableArrayList();
            for (Appointment appointment : appointments) {
                if (appointment.getUserId() == userId) {
                    allList.add(appointment);
                }
            }
            return allList;
        }

        //Utilize global datetime variable
        else if(filterLevel == 1) {
            ObservableList<Appointment> weekList = FXCollections.observableArrayList();
            for (Appointment appointment : appointments) {
                //Add all appointments within the next week with matching user Id to weeklist and return
            }

        }else{      //Filter level == 2
            ObservableList<Appointment> monthList = FXCollections.observableArrayList();
            for(Appointment appointment: appointments){
                //Add all appointments within the next month with matching user Id to monthList and return
            }
        }
    }

    //===========================================Customer Manipulation Methods==========================================

    public void addCustomer(Customer customer){
        addedCustomers.add(customer);
        customers.add(customer);
    }

    public void updateCustomer(Customer customer){
        updatedCustomers.add(customer);
        Customer oldVersion = getCustomerById(customer.getCustomerId());
        if(oldVersion != null) {
            customers.set(customers.indexOf(oldVersion), customer);
        }
    }

    public void deleteCustomer(Customer customer){
        deletedCustomers.add(customer);
        for(Customer updatedCustomer: updatedCustomers){
            if(updatedCustomer.getCustomerId() == customer.getCustomerId()){
                updatedCustomers.remove(updatedCustomer);
            }
        }
        for(Appointment appointment: appointments){
            if(appointment.getCustomerId() == customer.getCustomerId()){
                deleteAppointment(appointment);
            }
        }

        customers.remove(customer);
    }

    private Customer getCustomerById(int id){
        for(Customer customer: customers){
            if(customer.getCustomerId() == id){
                return customer;
            }
        }return null;
    }

    //========================================Appointment Manipulation Methods==========================================

    public void addAppointment(Appointment appointment){
        addedAppointments.add(appointment);
        appointments.add(appointment);
    }

    public void updateAppointment(Appointment appointment){
        updatedAppointments.add(appointment);
        Appointment oldVersion = getAppointmentById(appointment.getAppointmentId());
        if(oldVersion != null) {
            appointments.set(appointments.indexOf(oldVersion), appointment);
        }
    }

    public void deleteAppointment(Appointment appointment){
        deletedAppointments.add(appointment);
        for(Appointment updatedAppointment: updatedAppointments){
            if(updatedAppointment.getAppointmentId() == appointment.getAppointmentId()){
                updatedAppointments.remove(updatedAppointment);
            }
        }
        appointments.remove(appointment);
    }

    private Appointment getAppointmentById(int id){
        for(Appointment appointment: appointments){
            if(appointment.getAppointmentId() == id){
                return appointment;
            }
        }return null;
    }

    //===================================Database Interaction Methods===================================================


    //Database.build(list1, list2, list3, list4, etc){
    //pass each list to correct build method
    public void build(){
        //Open Connection                                                         //Build lists--Draw from database
        //Establish Variable for database to be able to query within methods and pass as arguments
        buildCountries();
        buildDivisions();
        buildUsers();
        buildContacts();
        buildCustomers();
        buildAppointments();
        //Close Connection
    }


    private void buildCountries(){
        //Query Countries table for id and name
        //Loop through Rows in Country table and build list
        int id;
        String name;
        Country country = new Country(id, name);
        countries.add(country);
    }

    private void buildDivisions(){
        //Query Divisions table for id, name, and country id
        //Loop through Rows in Division table and build list
        int id;
        String name;
        int countryId;
        FirstLevelDivision division = new FirstLevelDivision(id, name, countryId);
        divisions.add(division);
    }

    private void buildUsers(){
        //Query Users table for id, name, and password
        //Loop through Rows in Users table and build list
        int id;
        String name;
        String password;
        User user = new User(id, name, password);
        users.add(user);
    }

    private void buildContacts(){
        //Query Contacts table for id and name
        //Loop through Rows in Contacts table and build list
        int id;
        String name;
        Contact contact = new Contact(id, name);
        contacts.add(contact);
    }

    private void buildCustomers(){
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

    private void buildAppointments(){
        //Query Appointments table for id, name, address, postal code, phone, and division id
        //Loop through Rows in Appointment table and build list
        int id;
        String title;
        String description;
        String location;
        String type;
        String start;
        String end;
        int customerId;
        int userId;
        int contactId;
        Appointment appointment = new Appointment(id, title, description, location, type, start, end, customerId,
                userId, contactId);
        appointments.add(appointment);
    }

    public void save(){
        //Open Connection
        //Write all the data from Added, Updated, and Deleted Lists for both customers and appointments
        //Close Connection
    }

}
