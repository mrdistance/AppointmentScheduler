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

public class Data {

    //============================================================================================================
    //Might need global datetime variable to filter appointments based on how close they come to the current time
    //============================================================================================================

    //Bring in Database to build Data
    Database database;
    //Track who is using the app to correctly populate appointments and customers, get from text field once successful login
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

    public Data(int userId){
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
        database = new Database(countries, divisions, users, contacts, customers, appointments);
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

    //==========================================Database Interaction Methods============================================

    public void save(ArrayList<Customer> addedCustomers, ArrayList<Customer> updatedCustomers, ArrayList<Customer> deletedCustomers,
                     ArrayList<Appointment> addedAppointments, ArrayList<Appointment> updatedAppointments, ArrayList<Appointment> deletedAppointments){
        database.save(addedCustomers, updatedCustomers, deletedCustomers, addedAppointments, updatedAppointments, deletedAppointments);
    }

}
