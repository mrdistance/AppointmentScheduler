package com.example.appointmentscheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.sql.PreparedStatement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides methods to retrieve and send data to and from the database.  Each method takes a prepared statment
 * as an argument and in turn passes that statement on to the database class for processing.  Methods either return data
 * in the form of lists, or return an integer specifying successful or failed database inserts, updates, and deletions.
 *
 * Class Database.java
 */

/**
 * @author Joshua Call
 */


public class Data {

    //Bring in Database to build Data
    Database database;

    //Track current date and time of user
    LocalDateTime dateTime;
    LocalDate date;
    LocalTime time;

    public Data(){
        //Initialize variables
        dateTime = LocalDateTime.now();

        //Connect to the database and build objects
        database = new Database();
    }


    //============================================Data Getter Methods============================================

    /**
     * @return the countries stored in the database
     */
    //TODO query database for list of countries matching prepared statement argument
    public ArrayList<Country> getCountries(PreparedStatement ps){
        return database.buildCountries(ps);
    }

    /**
     * @return the first level divisions stored in the database
     */
    //TODO query database for list of divisions matching prepared statement argument
    public ArrayList<FirstLevelDivision> getDivisions(PreparedStatement ps){
        return database.buildDivisions(ps);
    }

    /**
     * @return the users stored in the dabase
     */
    //TODO query database for list of users matching prepared statement argument
    public ArrayList<User> getUsers(PreparedStatement ps){
        return database.buildUsers(ps);
    }

    /**
     * @return the contacts stored in the database
     */
    //TODO query database for list of contacts matching prepared statement argument
    public ArrayList<Contact> getContacts(PreparedStatement ps){
        return database.buildContacts(ps);
    }

    /**
     * @return the customers stored in the database
     */
    //TODO query database for list of customers matching prepared statement argument
    public ObservableList<Customer> getCustomers(PreparedStatement ps) {return database.buildCustomers(ps);
    }

    /**
     * This method has been designed to allow the user to filter all the appointments and display the ones that
     * fall within the current week or month.  It also allows for easy access to all appointments for display tables as
     * well as appointments that will occur within 15 minutes of user login.
     *
     * @param filterLevel the desired level of filtering for appointments(0: All, 1: Within 15 Minutes, 2: This week, 3: This month)
     * @return A list holding the filtered appointments
     */
    public ObservableList<Appointment> getAppointments(int filterLevel){
        //FIXME call build appointment method of database to build a list for further filtering or make query specific enough
        //        to perfectly filter results and then just return those instead of having to loop through list, so converty all local
        //        variables into uct so can direct compare to database in query
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

        //TODO move loop inside each filter level or make private helper method
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

            if (filterLevel == 0) {                                             //Display all Appointments
                //TODO call build appointment method with prepared statement specifying all
                appointmentList.add(appointment);
            }
            else if(filterLevel == 1){                                          //Display urgent upcoming appointments within 15 minutes of login
                //TODO call build appointment method with prepared statement specifying same day within 15 minutes
                //Must fall on same day
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
            else if (filterLevel == 2) {                                        //Display appointments for the week
                //TODO call build appointment method with prepared statement specifying  within 7 days of same week
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
            else {      //Filter level == 3                                     //Display appointments for the month
                //TODO call build appointment method with prepared statement specifying within 30 days of same month
                if(appMonth == month && appDay >= day){
                    appointmentList.add(appointment);
                }
            }
        }
        return appointmentList;
    }

    public List<String> getReport(int reportNumber){
        //TODO build 3 separate Prepared statements based on which report you want to build  to be passed to database.buildReport()
        //  which will return a list back up that we can return and pass to a text area object line by line to display the report
    }

    //===========================================Customer Manipulation Methods==========================================

    //TODO have these methods return a prepared statemtent specifying what to do and passing to database insert, update, or delete method
    /**
     * @param customer the customer to add
     */
    public int addCustomer(Customer customer){
        //TODO build prepared statment
        int result = database.insert(ps)

    }

    /**
     * @param customer the customer to update
     */
    public int updateCustomer(Customer customer){
        //TODO build prepared statement
                int result = database.update(ps)
    }

    /**
     * @param customer the customer to delete
     */
    public PreparedStatement deleteCustomer(Customer customer){
        //TODO build prepared statment for all appointments connected to customer
        int result1 = database.delete(ps)
        //TODO build prepared statment for customer matching
        int result2 = database.insert(ps)

        //TODO return 0 1 or 2 if all fail appointment success, customer and appointment success
    }

    //========================================Appointment Manipulation Methods==========================================

    //TODO have these methods return a prepared statemtent specifying what to do and passing to database insert, update, or delete method
    /**
     * @param appointment the appointment to add
     */
    public int addAppointment(Appointment appointment){
        //TODO build prepared statment
        int result = database.insert(ps)
    }

    /**
     * @param appointment the appointment to update
     */
    public int updateAppointment(Appointment appointment){
        //TODO build prepared statment
        int result = database.update(ps)
    }

    /**
     * @param appointment the appointment to delete
     */
    public int deleteAppointment(Appointment appointment){
        //TODO build prepared statment
        int result = database.delete(ps)
    }

}
