package com.example.appointmentscheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

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


    //=====================================Location Getter Methods======================================================

    /**
     * This method queries the database and returns an observable list of all the countries
     *
     * @return the countries stored in the database
     */

    public ObservableList<Country> getCountries() throws SQLException{
        String query = "Select * from countries";
        Connection connection = database.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ObservableList<Country> countries =  database.buildCountries(ps);
        database.closeConnection();
        return countries;
    }

    /**
     * This method queries the database and returns an observable list of all the divisions that pertain
     * to the specified country
     *
     * @param country the country to match divisions to
     * @return the first level divisions stored in the database that are a match
     */
    public ObservableList<FirstLevelDivision> getDivisions(Country country) throws SQLException{
        String query = "Select * from first_level_divisions where country_id = ?";
        Connection connection = database.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, country.getCountryId());
        ObservableList<FirstLevelDivision> divisions = database.buildDivisions(ps);
        database.closeConnection();
        return divisions;
    }


    //============================================Login Validation======================================================

    /**
     * This method determines if the username and password a user has entered are valid
     *
     * @param userName the username taken from the login form
     * @param password the password taken from the login form
     * @return a valid user or null
     */
    public User login(String userName, String password) throws SQLException{
        User user = getUser(userName);
        if(user != null){                                   //Username is valid and exists
            if(password.equals(user.getPassword())){        //Password is valid and matches username
                logAttempt("SUCCESS", userName);
                return user;
            }
        }
        logAttempt("FAIL   ", userName);
        return null;
    }

    /**
     * This method logs successes/failures logging into the application to the file login_activity.txt
     *
     * @param result the result of the login attempt (success or fail)
     * @param userName the username taken from the login form
     */
    private void logAttempt(String result, String userName){
        String timestamp = new SimpleDateFormat("yyyy.MM.dd     HH.mm.ss").format(new Date());
        String location = TimeZone.getDefault().getID();    //Location of user
        String logData = result + "     " + userName +  "     " + timestamp +"     " + location;
        try (FileWriter writer = new FileWriter("login_activity.txt")){
            writer.append(logData);         //Write to file
        }catch (IOException ioe){
            System.out.println("Error loading file \"login_activity.txt\"");
        }
    }

    //=============================================Data Getter Methods==================================================

    /**
     * This method checks to see if there is a user in the database that matches the username entered on
     * the login form
     *
     * @param username the username taken from the login form
     * @return the users stored in the dabase
     */
    //TODO query database for list of users matching prepared statement argument
    private User getUser(String username) throws SQLException{
        String query = "Select * from users where User_Name = ?";
        Connection connection = database.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, username);
        User user = database.buildUser(ps);
        database.closeConnection();
        return user;
    }

    /**
     * @return the contacts stored in the database
     */
    //TODO query database for list of contacts matching prepared statement argument
    public ObservableList<Contact> getContacts() throws SQLException{
        String query = "Select * from contacts";
        Connection connection = database.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ObservableList<Contact> contacts = database.buildContacts(ps);
        database.closeConnection();
        return contacts;
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

    //todo appointments must be sorted in ascending order based on start time
    public ObservableList<Appointment> getAppointments(int filterLevel) throws SQLException{
        String query = "";
        //Case One, get all appointments
        if(filterLevel == 0) {
            query = "select * from appointments order by start asc";
        }
        //Case Two, get appointments for month
        else if(filterLevel == 1) {
        }
        //Case Three, get appointments for week
        else if(filterLevel == 2) {
        }
        //Case Four, get appointments for day
        else if(filterLevel == 3) {
            query = "select * from appointments where start >= ? order by start asc";
            dateTime = LocalDateTime.now();
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(dateTime);
        }
        //Case Five, get appointments within 15 minutes of login
        else { //filterLevel == 4
        }










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

    //todo make method to get end times

    /**
     * This method builds an observable list representing all available start time slots for the day
     *
     * @return list of available start times
     * @throws SQLException the exception if connection fails
     */
    public ObservableList<String> getStartTimes() throws SQLException{
        ObservableList<Appointment> todaysAppointments = getAppointments(3);
        ObservableList<String> availableTimes = FXCollections.observableArrayList();                   //build a list to hold open time slots
        String today = dateAsString(todaysAppointments.get(0).getStartDateTime());
        int i = 0;
        int availableTime = 480;

        for(Appointment appointment : todaysAppointments) {
            int easternTimeConverted = getTimeAsInt(easternToLocal(today + " " + convertIntToTime(availableTime)));
            int takenTime = getTimeAsInt(appointment.getStartDateTime());
            int duration = getAppointmentDurationAsInt(appointment);
            //Time slot available
            if (!(easternTimeConverted == takenTime)) {
                availableTimes.add(convertIntToTime(easternTimeConverted));
            }
            //Time slot taken, advance until get to appointment end time
            else {
                while (duration > 0) {
                    availableTime += 15;
                    duration -= 15;
                }
            }
        }
        return availableTimes;
    }

    /**
     * This method returns just the date portion of a string date time
     *
     * @param date the date to extract from
     * @return the date portion of the date string
     */
    private String dateAsString(String date){
        String[] parts = date.split("T");
        return parts[0];
    }

    /**
     * This method takes in the number of minutes that have passed in a day as an integer and converts it back to
     * a string representation of the time of day
     *
     * @param timeInMinutes the time of day in minutes
     * @return the time of day as a string
     */
    private String convertIntToTime(int timeInMinutes){
        int hours = timeInMinutes % 60;
        int minutes = timeInMinutes - (hours * 60);
        String hoursString = hours < 10 ? "0" + hours : String.valueOf(hours);
        String minutesString = minutes == 0 ? "00" : String.valueOf(minutes);
        return hoursString + ":" + minutesString + ":00";
    }

    /**
     * This method takes in a string date and returns the time of day represented as minutes from 00
     *
     * @param date the string from which to retrieve the minutes
     * @return the total number of minutes into the day represented by the string
     */
    private int getTimeAsInt(String date){
        String[] dateParts = date.split("T");
        String time = dateParts[1];
        String[] timeParts = time.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        return hours * 60 + minutes;

    }

    /**
     * This method takes an appointment and determines how long it will last in minutes
     *
     * @param appointment the appointment to examine
     * @return the length of the appointment in minutes
     */
    private int getAppointmentDurationAsInt(Appointment appointment){
        int startTotalMinutes = getTimeAsInt(appointment.getStartDateTime());
        int endTotalMinutes = getTimeAsInt(appointment.getEndDateTime());
        return endTotalMinutes - startTotalMinutes;
    }

    //TODO build 3 separate Prepared statements based on which report you want to build  to be passed to database.buildReport()
    //  which will return a list back up that we can return and pass to a text area object line by line to display the report
    public List<String> getReport(int reportNumber){
        //Report 1, total number of customer appointments by type and month

        //Report 2, schedule for each contact in organization

        //Report 3, total customers per country and division

        return null;
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

    //==========================================String Manipulation Methods=============================================

    /**
     * This method takes in a string time in the users time zone, converts it to utc time zone, then converts it to a
     * LocalDateTime object for comparisons of appointments times  (getAppointments method)
     *
     * @param appointment the appointment to compare with
     * @return the date and time of the appointment as a LocalDateTime object
     */
    private LocalDateTime stringToDate(Appointment appointment){
        String dateTimeString = localToUTC(appointment.getStartDateTime());         //Look at list of appointments and compare times in database prepared statement for getappointment
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
        ZoneId localZone = ZoneId.of(TimeZone.getDefault().getID());             //"This gets local timezone
        ZoneId utcZone = ZoneId.of("UTC");                                       //This is db timezone
        LocalDateTime localTime = LocalDateTime.parse(localString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ZonedDateTime localAppointmentTime = localTime.atZone(localZone);
        ZonedDateTime utcAppointmentTime = localAppointmentTime.withZoneSameInstant(utcZone);
        return utcAppointmentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * This method takes a string time in eastern time zone from 8AM to 10PM
     * and converts it into a string in the user's time zone to see if the time is available or taken
     *
     * @param easternString the string to convert
     * @return the converted string
     */
    private String easternToLocal(String easternString){
        ZoneId localZone = ZoneId.of(TimeZone.getDefault().getID());                            //"This gets local timezone
        ZoneId eastZone = ZoneId.of("America/New_York");                                        //This is eastern timezone
        LocalDateTime easternTime = LocalDateTime.parse(easternString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ZonedDateTime easternAppointmentTime = easternTime.atZone(eastZone);
        ZonedDateTime localAppointmentTime = easternAppointmentTime.withZoneSameInstant(localZone);
        return localAppointmentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}

