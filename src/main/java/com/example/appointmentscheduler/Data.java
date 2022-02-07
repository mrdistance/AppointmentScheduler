package com.example.appointmentscheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

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
    private Database database;


    //Track current date and time of user
    private LocalDateTime dateTime;
    private LocalDate date;
    private LocalTime time;

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
    public ObservableList<Customer> getCustomers(PreparedStatement ps) throws SQLException {return database.buildCustomers(ps);
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
        //        to perfectly filter results and then just return those instead of having to loop through list, all comparisons will be made
        // in eastern time, database uct is converted when build appointments is called, local date time variables need to be converted here to eastern
        // only for comparisons, then time needs to be converted to uct to make prepared statement and draw correct times in
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        //variables to track local date and time
        //in local time zone and localdatetimeformat
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
        for(Appointment appointment : database.buildAppointments()) {

            //Variables to track appointment date and time, these times
            //are returned and stored as sql uct time zone and format
            LocalDateTime appDateTime = stringToDate(appointment);                  //get Localdatetime conversion of string date
            LocalDate appDate = appDateTime.toLocalDate();
            LocalTime appTime = appDateTime.toLocalTime();
            int appHour = appTime.getHour();
            int appMinute = appTime.getMinute();
            int appMonth = appDate.getMonthValue();
            int appDay = appDate.getDayOfMonth();
            DayOfWeek appWeek = appDate.getDayOfWeek();
            int appWDay = appWeek.getValue();



            //before variables can be compared, need to convert local time variables to eastern time variables to
            //match time zone stored in appointment objects which was converted by database class upon read in.
            //need to take this conversion into account when making prepared statements and convert from eastern back
            //to uct before updating database
            //formatting is done in this class with private method to create localdatetime variables
            //System.out.println(dbTime.isAfter(estTime));
            //System.out.println(date.isEqual(dbTime.toLocalDate()));             //check if same day
            // System.out.println(time.isBefore(estTime.toLocalTime()));

            if (filterLevel == 0) {                                             //Display all Appointments
                //TODO call build appointment method with prepared statement specifying all
                appointmentList.add(appointment);
            }
            else if(filterLevel == 1){                                          //Display urgent upcoming appointments within 15 minutes of login
                //TODO call build appointment method with prepared statement specifying same day within 15 minutes
                // will need to convert local time zone to uct
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
                // will need to convert local time zone to uct
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


    /**
     * This method opens a connection to the database, composes a prepared statement to insert the specified customer,
     * then closes the connection
     *
     * @param customer the customer to add
     * @return 1 for success 0 for fail
     * @throws SQLException the exception if connection fails
     */
    public int addCustomer(Customer customer) throws SQLException{
        String query = "Insert into customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) " +
                        "values (?, ?, ?, ?, ?)";
        Connection connection = database.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, customer.getCustomerName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostalCode());
        ps.setString(4, customer.getPhone());
        ps.setInt(5, customer.getDivisionId());
        int result = database.update(ps);
        database.closeConnection();
        return result > 0 ? 1: 0;
    }

    /**
     * This method opens a connection to the database, composes a prepared statement to update the specified customer,
     * then closes the connection
     *
     * @param customer the customer to update
     * @return 1 for success 0 for fail
     * @throws SQLException the exception if connection fails
     */


    public int updateCustomer(Customer customer) throws SQLException{
        String query = "Update customers set Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? " +
                        "where Customer_ID = ?";
        Connection connection = database.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, customer.getCustomerName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostalCode());
        ps.setString(4, customer.getPhone());
        ps.setInt(5, customer.getDivisionId());
        ps.setInt(6, customer.getCustomerId());
        int result = database.update(ps);
        database.closeConnection();
        return result > 0 ? 1: 0;
    }

    /**
     * This method opens a connection to the database, composes a prepared statement to delete the specified customer\
     * and all of that customers appointments then closes the connection
     *
     * @param customer the customer to delete
     * @return 1 for success 0 for fail
     * @throws SQLException the exception if connection fails
     */
    public int deleteCustomer(Customer customer) throws SQLException {
        //Delete appointments
        String query = "Delete from appointments where Customer_ID = ?";
        Connection connection = database.getConnection();
        PreparedStatement ps1 = connection.prepareStatement(query);
        ps1.setInt(1, customer.getCustomerId());
        int result1 = database.update(ps1);
        //Delete customer
        String query2 = "Delete from customers where Customer_ID = ?";
        PreparedStatement ps2 = connection.prepareStatement(query2);
        ps2.setInt(1, customer.getCustomerId());
        int result2 = database.update(ps2);
        database.closeConnection();
        return result1 + result2 > 0 ? 1 : 0;
    }

    //========================================Appointment Manipulation Methods==========================================

    //TODO have these methods return a prepared statemtent specifying what to do and passing to database insert, update, or delete method
    //  they also have to do time formatting

    /**
     * This method opens a connection to the database, composes a prepared statement to add the specified appointment,
     * then closes the connection
     *
     * @param appointment the appointment to add
     * @return 1 for success 0 for fail
     * @throws SQLException the exception if connection fails
     */
    public int addAppointment(Appointment appointment) throws SQLException{
        String query = "Insert into appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                        "Values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = database.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setString(5, localToUTC(appointment.getStartDateTime()));
        ps.setString(6, localToUTC(appointment.getEndDateTime()));
        ps.setInt(7, appointment.getCustomerId());
        ps.setInt(8, appointment.getUserId());
        ps.setInt(9, appointment.getContactId());
        int result = database.update(ps);
        database.closeConnection();
        return result > 0 ? 1 : 0;
    }

    /**
     * This method opens a connection to the database, composes a prepared statement to update the specified appointment,
     * then closes the connection
     *
     * @param appointment the appointment to update
     * @return 1 for success 0 for fail
     * @throws SQLException the exception if connection fails
     */
    public int updateAppointment(Appointment appointment) throws SQLException{
        String query = "Update appointments set Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, " +
                        "User_ID = ?, Contact_ID = ? where Appointment_ID = ?";
        Connection connection = database.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setString(5, localToUTC(appointment.getStartDateTime()));
        ps.setString(6, localToUTC(appointment.getEndDateTime()));
        ps.setInt(7, appointment.getCustomerId());
        ps.setInt(8, appointment.getUserId());
        ps.setInt(9, appointment.getContactId());
        ps.setInt(10, appointment.getAppointmentId());
        int result = database.update(ps);
        database.closeConnection();
        return result > 0 ? 1 : 0;
    }

    /**
     * This method opens a connection to the database, composes a prepared statement to delete the specified appointment,
     * then closes the connection
     *
     * @param appointment the appointment to delete
     * @return 1 for success 0 for fail
     * @throws SQLException the exception if connection fails
     */
    public int deleteAppointment(Appointment appointment) throws SQLException{
        String query = "Delete from appointments where Appointment_ID = ?";
        Connection connection = database.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, appointment.getAppointmentId()));
        int result = database.update(ps);
        database.closeConnection();
        return result > 0 ? 1 : 0;
    }

    /**
     * This method takes in a string time in the users time zone, converts it to utc time zone, then converts it to a
     * LocalDateTime object for comparisons of appointments times  (getAppointments method)
     *
     * @param appointment the appointment to compare with
     * @return the date and time of the appointment as a LocalDateTime object
     */
    private LocalDateTime stringToDate(Appointment appointment){
        String dateTimeString = localToUTC(appointment.getStartDateTime());         //Look at list of appointments and compare times
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeString, formatter);

    }

    /**
     * This method takes a string time in the users time zone and converts it into a string in the utc time zone
     *
     * @param localString the string to convert
     * @return the converted string
     */
    private String localToUTC(String localString){
        ZoneId localZone = ZoneId.of(TimeZone.getDefault().getID());         //"This gets local timezone
        ZoneId utcZone = ZoneId.of("UTC");                                       //This is db timezone
        LocalDateTime localTime = LocalDateTime.parse(localString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ZonedDateTime localAppointmentTime = localTime.atZone(localZone);
        ZonedDateTime utcAppointmentTime = localAppointmentTime.withZoneSameInstant(utcZone);
        return utcAppointmentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}

//fixme  basically,  if you are querying the database for comparisons to build appointment arrays, convert appointment time from eastern to uct (getappointments prepared statement)
//                   if you are comparing local and appointment time, convert local to eastern (getappointments if else)
//                   when updating database or adding appointment, convert time from eastern to uct to store properly (update/add appointment)
//                   when pulling from the actually database and storing in appointment objects, convert from uct to eastern (database.buildappointments)
//                   when building hashmaps for time pickers, store as eastern, local time does not matter here and appointments are stored with eastern time
