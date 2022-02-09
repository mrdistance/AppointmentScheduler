package com.example.appointmentscheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
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

    public Data(){
        //Initialize variables
        dateTime = LocalDateTime.now();
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
     * @return the user stored in the database
     */
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
     * This method queries the database and returns an Observable list of all the contacts in it
     *
     * @return the contacts stored in the database
     */
    public ObservableList<Contact> getContacts() throws SQLException{
        String query = "Select * from contacts";
        Connection connection = database.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ObservableList<Contact> contacts = database.buildContacts(ps);
        database.closeConnection();
        return contacts;
    }

    /**
     * This method queries the database and returns an observable list of all the customers in it
     *
     * @return the customers stored in the database
     */
    public ObservableList<Customer> getCustomers() throws SQLException {
        String query = "Select * from customers";
        Connection connection = database.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ObservableList<Customer> customers = database.buildCustomers(ps);
        database.closeConnection();
        return customers;
    }

    /**
     * This method has been designed to allow the user to filter all the appointments and display the ones that
     * fall within the current day, week, or month.  It also allows for easy access to all appointments for display tables as
     * well as appointments that will occur within 15 minutes of user login.
     *
     * @param filterLevel the desired level of filtering for appointments(0: All, 1: This month, 2: This week, 3: Today, 4: 15 minutes)
     * @return A list holding the filtered appointments
     */
    public ObservableList<Appointment> getAppointments(int filterLevel) throws SQLException{
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        dateTime = LocalDateTime.now();                                         //Get the date and time of users machine
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        int hour = time.getHour();
        int minute = time.getMinute();
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        DayOfWeek week = date.getDayOfWeek();
        int wDay = week.getValue();
        int numDaysThisMonth = date.lengthOfMonth();

        String query = "";
        //Case One, get all appointments
        if(filterLevel == 0) {
            query = "select * from appointments order by start";
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            appointments = database.buildAppointments(ps);
            database.closeConnection();
        }
        //Case Two, get appointments for month
        else if(filterLevel == 1) {
            query = "select * from appointments where start >= ? and start < ? order by start";
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, localToUTC(String.valueOf(dateTime)));
            ps.setString(2, localToUTC(nextMonth(year, month) + "00:00:00"));
            appointments = database.buildAppointments(ps);
            database.closeConnection();
        }
        //Case Three, get appointments for week
        else if(filterLevel == 2) {
            query = "select * from appointments where start >= ? and start < ? order by start";
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, localToUTC(String.valueOf(dateTime)));
            ps.setString(2, localToUTC(nextWeek(year, month, day, numDaysThisMonth, wDay) + "00:00:00"));
            appointments = database.buildAppointments(ps);
            database.closeConnection();
        }
        //Case Four, get appointments for day
        else if(filterLevel == 3) {
            query = "select * from appointments where start >= ? and start < ? order by start";
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, localToUTC(String.valueOf(dateTime)));
            ps.setString(2, localToUTC(nextDay(year, month, day, numDaysThisMonth) + " 00:00:00"));
            appointments = database.buildAppointments(ps);
            database.closeConnection();
        }
        //Case Five, get appointments within 15 minutes of login
        else { //filterLevel == 4
            query = "select * from appointments where start >= ? and start < ? order by start";
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, localToUTC(String.valueOf(dateTime)));
            ps.setString(2, localToUTC(date + nextFifteenMinutes(hour, minute)));
            appointments = database.buildAppointments(ps);
            database.closeConnection();
        }
        return appointments;
    }

    /**
     * This method determines the string value of the date of next day, compared to current day
     *
     * @param year the current year
     * @param month the current month
     * @param day the current day
     * @param numDaysInMonth the number of days in the current month
     * @return the string value of the date of the next day
     */
    private String nextDay(int year, int month, int day, int numDaysInMonth){
        if(day == numDaysInMonth){
            if(month == 12){
                month = 1;
                year++;
            }
            month++;
            day = 1;
        }else{
            day += 1;
        }
        String monthString = month < 10 ? "0" + month: String.valueOf(month);
        String dayString = day < 10 ? "0" + day : String.valueOf(day);
        return year + "-" + monthString + "-" + dayString;
    }

    /**
     * This method determines the string value of the date of the first day of next month, compared to the current month
     *
     * @param year the current year
     * @param month the current month
     * @return the string value of the date of the first day of next month
     */
    private String nextMonth(int year, int month){
        if(month == 12){
            month = 1;
            year++;
        }else {
            month++;
        }
        String monthString = month < 10 ? "0" + month : String.valueOf(month);
        return year + "-" + monthString + "-01";
    }

    /**
     * This method determines the string value of the date of the first day of next week, compared to the current day
     *
     * @param year the current year
     * @param month the current month
     * @param day the current day
     * @param numDaysInMonth the number of days in the current month
     * @param weekDay the current day of the week as an integer between 1 and 7
     * @return the string value of the date of the first day of next week
     */
    private String nextWeek(int year, int month, int day, int numDaysInMonth, int weekDay){
        int daysLeftThisWeek = 7 - weekDay;
        //Leave current month
        if(day + daysLeftThisWeek + 1 > numDaysInMonth){
            //Leave current Year
            if(month == 12){
                month = 1;
                year++;
            }
            day = day + daysLeftThisWeek + 1 - numDaysInMonth;
        }else{
            day += daysLeftThisWeek + 1;
        }
        String monthString = month < 10 ? "0" + month: String.valueOf(month);
        String dayString = day < 10 ? "0" + day : String.valueOf(day);
        return year + "-" + monthString + "-" + dayString;
    }

    /**
     * This method determines the string value of the date of fifteen minutes from the current time
     *
     * @param hour the current hour
     * @param minute the current minute
     * @return the string value of the date fifteen minutes from current time
     */
    private String nextFifteenMinutes(int hour, int minute){
        if(minute + 15 >= 60) {
            hour++;
            minute = minute + 15 - 60;
        }else {
            minute += 15;
        }
        String hourString = hour < 10 ? "0" + hour : String.valueOf(hour);
        String minuteString = minute < 10 ? "0" + minute : String.valueOf(minute);
        return hourString + ":" + minuteString + ":" + "00";
    }

    /**
     * This method builds an observable list representing all available end time slots based on the specified start time
     *
     * @param startTime the start time specified
     * @param nextAppointmentStart the start time of the next appointment (The last available time slot without overlapping)
     * @return list of available end times
     */
    //todo next appointment start is either next appointment in list or end of hours converted from 10pm eastern to local
    public ObservableList<String> getEndTimes(String startTime, String nextAppointmentStart){
        ObservableList<String> availableTimes = FXCollections.observableArrayList();
        int availableTime = convertTimeToInt(startTime);
        int nextAppointmentTime = convertTimeToInt(nextAppointmentStart);
        int availableDuration = nextAppointmentTime - availableTime;

        while(availableDuration > 0){
            availableTime += 15;
            availableDuration -= 15;
            availableTimes.add(convertIntToTime(availableTime));
        }
        return availableTimes;
    }

    /**
     * This method builds an observable list representing all available start time slots for the specified date
     *
     * @param dateOfAppointment the date specified
     * @return list of available start times
     * @throws SQLException the exception if connection fails
     */
    public ObservableList<String> getStartTimes(Date dateOfAppointment) throws SQLException{
        //todo add checks to see if appointment is for today, if so only return appointments after current time
        ObservableList<Appointment> todaysAppointments = getAppointments(3);
        ObservableList<String> availableTimes = FXCollections.observableArrayList();                   //build a list to hold open time slots
        //Start of day 0800
        int availableTime = 480;

        //appointments must be sorted in ascending order by start time
        for(Appointment appointment : todaysAppointments) {
            int easternTimeConverted = convertDateTimeToInt(easternToLocal(dateOfAppointment + " " + convertIntToTime(availableTime)));
            int takenTime = convertDateTimeToInt(appointment.getStartDateTime());
            int duration = getAppointmentDurationAsInt(appointment);
            //Time slot available, add and advance
            while(!(easternTimeConverted == takenTime)) {
                availableTimes.add(convertIntToTime(easternTimeConverted));
                availableTime+=15;
                easternTimeConverted = convertDateTimeToInt(easternToLocal(dateOfAppointment + " " + convertIntToTime(availableTime)));
            }
            //Time slot taken, advance until get to appointment end time
            while (duration > 0) {
                availableTime += 15;
                duration -= 15;
            }
            //Examine next appointment time
        }
        //Get remaining available times after final appointment
        while(availableTime < 1320){
            int easternTimeConverted = convertDateTimeToInt(easternToLocal(dateOfAppointment + " " + convertIntToTime(availableTime)));
            availableTimes.add(convertIntToTime(easternTimeConverted));
            availableTime+=15;

        }
        return availableTimes;
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
     * This method takes in a string time and returns the time of day represented as minutes from 00
     *
     * @param time the string from which to retrieve minutes
     * @return the total number of minutes into the day represented by the string
     */
    private int convertTimeToInt(String time){
        String[] timeParts = time.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        return hours * 60 + minutes;
    }
    /**
     * This method takes in a string date and returns the time of day represented as minutes from 00
     *
     * @param date the string from which to retrieve the minutes
     * @return the total number of minutes into the day represented by the string
     */
    private int convertDateTimeToInt(String date){
        String[] dateParts = date.split("T");
        String time = dateParts[1];
        return convertTimeToInt(time);
    }

    /**
     * This method takes an appointment and determines how long it will last in minutes
     *
     * @param appointment the appointment to examine
     * @return the length of the appointment in minutes
     */
    private int getAppointmentDurationAsInt(Appointment appointment){
        int startTotalMinutes = convertDateTimeToInt(appointment.getStartDateTime());
        int endTotalMinutes = convertDateTimeToInt(appointment.getEndDateTime());
        return endTotalMinutes - startTotalMinutes;
    }

    /**
     * This method takes in a specific report number and returns that report to display in the GUI
     *
     * @param reportNumber the number of the report to generate
     * @return the report requested
     * @throws SQLException the exception if the connection fails
     */
    public List<String> getReport(int reportNumber) throws SQLException{
        List<String> report = new ArrayList<>();

        //Report 1, total number of customer appointments by type and month
        if(reportNumber == 1){
            report.add("------------------------CUSTOMER APPOINTMENTS------------------------\n\n");
            report.add("        TYPE             DATE            DESCRIPTION");
            ObservableList<Appointment> appointments = FXCollections.observableArrayList();
            String query = "select * from appointments order by type, year(start), month(start)";
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            appointments = database.buildAppointments(ps);
            database.closeConnection();
            for(Appointment appointment : appointments){
                report.add("     " + appointment.getType() + "     " + appointment.getStartDateTime() + "     " + appointment.getDescription());
            }
        }

        //Report 2, schedule for each contact in organization
        else if(reportNumber == 2){
            report.add("------------------------CONTACT SCHEDULES----------------------------\n\n");
            ObservableList<Contact> contacts = FXCollections.observableArrayList();
            String query1 = "select * from contacts";
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(query1);
            contacts = database.buildContacts(ps);
            database.closeConnection();
            for(Contact contact : contacts){
                ObservableList<Appointment> contactSchedule = FXCollections.observableArrayList();
                String query2 = "select * from appointments where Contact_ID = ?";
                Connection connection1 = database.getConnection();
                PreparedStatement ps1 = connection1.prepareStatement(query2);
                ps1.setInt(1, contact.getContactId());
                contactSchedule = database.buildAppointments(ps1);
                database.closeConnection();
                report.add(contact.getContactName());
                for(Appointment appointment : contactSchedule){
                    report.add("      ID = " + appointment.getAppointmentId() + " " + appointment.getTitle() + " " + appointment.getStartDateTime() + "  " + appointment.getEndDateTime() + " " + appointment.getType()
                           + " " + appointment.getDescription()  +  " Customer = " + appointment.getCustomerId());
                }
            }
        }
        //Report 3, total number of customers by country and region
        else{
            report.add("--------------------------CUSTOMERS----------------------------------\n\n");
            ObservableList<Country> countries = FXCollections.observableArrayList();
            String query1 = "select * from countries";
            Connection connection1 = database.getConnection();
            PreparedStatement ps1 = connection1.prepareStatement(query1);
            countries = database.buildCountries(ps1);
            database.closeConnection();
            ObservableList<FirstLevelDivision> divisions = FXCollections.observableArrayList();
            for(Country country : countries){
                report.add(country.getCountryName());
                String query2 = "select * from First_Level_Divisions where country_ID = ?";
                Connection connection2 = database.getConnection();
                PreparedStatement ps2 = connection2.prepareStatement(query2);
                ps2.setInt(1, country.getCountryId());
                divisions = database.buildDivisions(ps2);
                database.closeConnection();
                ObservableList<Customer> customers = FXCollections.observableArrayList();
                for(FirstLevelDivision division : divisions){
                    report.add("     " + division.getDivisionName());
                    String query3 = "select * from customers where division_ID = ?";
                    Connection connection3 = database.getConnection();
                    PreparedStatement ps3 = connection3.prepareStatement(query3);
                    ps3.setInt(1, division.getDivisionId());
                    customers = database.buildCustomers(ps3);
                    database.closeConnection();
                    for(Customer customer : customers){
                        report.add("          " + customer.getCustomerName() + " " + customer.getPhone() + " " + customer.getAddress());
                    }
                }
            }
        }
        return report;
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
        ps.setInt(1, appointment.getAppointmentId());
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

