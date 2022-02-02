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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    //Track current date and time of user
    LocalDateTime dateTime;
    LocalDate date;
    LocalTime time;
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
        //Initialize variables
        dateTime = LocalDateTime.now();

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

    //Currently returns appointments this week or this month
    public ObservableList<Appointment> getAppointments(int filterLevel){
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        dateTime = LocalDateTime.now();                                         //Get the date and time of users machine
        date = dateTime.toLocalDate();
        time = dateTime.toLocalTime();
        int hour = time.getHour();
        int minute = time.getMinute();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        DayOfWeek week = date.getDayOfWeek();
        int wDay = week.getValue();

        for(Appointment appointment : appointments) {                           //Look at list of appointments and compare times
            LocalDateTime appDateTime = appointment.getStartDateTime();
            LocalDate appDate = appDateTime.toLocalDate();
            LocalTime appTime = appDateTime.toLocalTime();
            int appHour = appTime.getHour();
            int appMinute = appTime.getMinute();
            int appMonth = appDate.getMonthValue();
            int appDay = appDate.getDayOfMonth();
            DayOfWeek appWeek = appDate.getDayOfWeek();
            int appWDay = appWeek.getValue();

            if(filterLevel == 0){                                               //Display urgent upcoming appointments within 15 minutes of login
                //Same day
                if(date.equals(appDate)){
                    //case 1 start same hour
                    if(appHour == hour && (appMinute >= minute && appMinute <= minute + 15)){
                        appointmentList.add(appointment);
                    }
                    //case 2 start beginning of next hour
                    else if(appHour == hour + 1 && (60 + appMinute - minute <= 15)){
                        appointmentList.add(appointment);
                    }
                }
            }
            else if (filterLevel == 1) {                                             //Display all Appointments
                appointmentList.add(appointment);
            }

            else if (filterLevel == 2) {                                        //Display appointments for the week
                //Must fall within today and sunday--no "tuesday today , next monday appointment"
                if(wDay <= appWDay) {
                    //case 1 same month within 7 days of today
                    if (appMonth == month && (appDay >= day && appDay < day + 7)) {
                        appointmentList.add(appointment);

                    }
                    //case 2 different months, within 7 days of today
                    else if ((appMonth == month + 1 || month == 12 && appMonth == 1) && (date.lengthOfMonth() + appDay - day < 7)) {
                        appointmentList.add(appointment);
                    }
                }
            }
            else {      //Filter level == 3
                //Add all appointments this month
                if(appMonth == month && appDay >= day){
                    appointmentList.add(appointment);
                }
            }
        }
        return appointmentList;
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
