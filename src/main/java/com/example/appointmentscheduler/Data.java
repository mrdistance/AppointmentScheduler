package com.example.appointmentscheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
 *
 * @author Joshua Call
 */


public class Data {

    //Bring in Database to build Data
    private Database database;
    //Track current date and time of user
    private LocalDateTime dateTime;
    //Track user location
    private ZoneId zoneId;

    /**
     * This method creates a data object to be used by other classes
     */
    public Data(){
        //Initialize variables
        dateTime = LocalDateTime.now();
        database = new Database();
        zoneId = ZoneId.systemDefault();
    }


    //=====================================Location Getter Methods======================================================

    /**
     * This method queries the database and returns an observable list of all the countries
     *
     * @return the countries stored in the database
     * @throws SQLException the exception if the connection fails with the database
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
     * This method queries the database and returns an observable list of all the divisions
     *
     * @return the first level divisions stored in the database
     * @throws SQLException the exception if the connection fails with the database
     */
    public ObservableList<FirstLevelDivision> getDivisions() throws SQLException {
        String query = "Select * from first_level_divisions";
        Connection connection = database.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ObservableList<FirstLevelDivision> divisions = database.buildDivisions(ps);
        database.closeConnection();
        return divisions;
    }
    /**
     * This method queries the database and returns an observable list of all the divisions that pertain
     * to the specified country
     *
     * @param country the country to match divisions to
     * @return the first level divisions stored in the database that are a match
     * @throws SQLException the exception if the connection fails with the database
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
     * This method determines if the username and password a user has entered are valid and logs the attempt
     *
     * @param userName the username taken from the login form
     * @param password the password taken from the login form
     * @return a valid user or null
     * @throws SQLException the exception if the connection fails with the database
     */
    public User login(String userName, String password) throws SQLException{
        try {
            User user = getUser(userName);
            if (user != null) {                                   //Username is valid and exists
                if (password.equals(user.getPassword())) {        //Password is valid and matches username
                    logAttempt("SUCCESS", userName);
                    return user;
                }
            }
        }catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        logAttempt("FAIL", userName);
        return null;
    }

    /**
     * This method logs successes/failures logging into the application to the file login_activity.txt
     *
     * @param result the result of the login attempt (success or fail)
     * @param userName the username taken from the login form
     */
    private void logAttempt(String result, String userName){
        String timestamp = new SimpleDateFormat("yyyy.MM.dd  HH.mm.ss").format(new Date());
        String location = TimeZone.getDefault().getID();    //Location of user
        String logData =  String.format("%1$-15s %2$-15s %3$-30s %4$-15s%n", result, userName, timestamp, location);

        try (FileWriter writer = new FileWriter("login_activity.txt", true)){
            writer.append(logData);         //Write to file
        }catch (IOException ioe){
            System.out.println("Error loading file \"login_activity.txt\"");
        }
    }

    //=============================================Data Getter Methods==================================================

    /**
     * This method gets the zone id of the user ex: America/Denver
     *
     * @return the zone id as a string
     */
    public String getLocation(){
        return zoneId.toString();
    }

    /**
     * This method gets the current date of the user
     *
     * @return the date
     */
    public LocalDate getDate(){
        return this.dateTime.toLocalDate();
    }

    /**
     * This method gets the date of an appointment
     *
     * @param appointment the appointment to find the date for
     * @return the date of the chosen appointment
     */
    public LocalDate getAppointmentDate(Appointment appointment){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(appointment.getStartDateTime(), formatter).toLocalDate();
    }

    /**
     * This method queries the database and returns an Observable List of all the users in it
     *
     * @return the users stored in the database
     * @throws SQLException the exception if the connection fails with the database
     */
    public ObservableList<User> getUsers() throws SQLException{
        String query = "Select * from users";
        Connection connection = database.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ObservableList<User> users = database.buildUsers(ps);
        database.closeConnection();
        return users;
    }

    /**
     * This method checks to see if there is a user in the database that matches the username entered on
     * the login form
     *
     * @param username the username taken from the login form
     * @return the user stored in the database
     * @throws SQLException the exception if the connection fails with the database
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
     * @throws SQLException the exception if the connection fails with the database
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
     * This method returns an Observable list of all the names of all the contacts in the database
     *
     * @return the contact names stored in the database
     * @throws SQLException the exception if the connection fails with the database
     */
    public ObservableList<String> getContactNames() throws SQLException {
        ObservableList<Contact> contacts = getContacts();
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        for(Contact contact : contacts){
            contactNames.add(contact.getContactName());
        }
        return contactNames;
    }

    /**
     * This method returns the contact name associated with a contact ID
     *
     * @param contactID the id to search for
     * @return the contact name that is a match
     * @throws SQLException the exception if the connection fails with the database
     */
    public String getContactName(int contactID) throws SQLException {
        String name = "";
        for(Contact contact : getContacts()){
            if(contact.getContactId() == contactID){
                return contact.getContactName();
            }
        }
        return name;
    }

    /**
     * This method builds an Observable list of fixed possible types for use by the user when working with appointments
     *
     * @return the list of all the available appointment types
     */
    public ObservableList<String> getTypes(){
        ObservableList<String> types = FXCollections.observableArrayList();
        types.add("Planning Session");
        types.add("Lunch");
        types.add("Pitch Meeting");
        types.add("Interview");
        types.add("Product Review");
        types.add("Testing");
        types.add("Personal");
        types.add("Budget");
        types.add("Audit");
        return types;
    }

    /**
     * This method queries the database and returns an observable list of all the customers in it
     *
     * @return the customers stored in the database
     * @throws SQLException the exception if the connection fails with the database
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
     * @param filterDate the date to filter the appointments for
     * @return A list holding the filtered appointments
     * @throws SQLException the exception if the connection fails with the database
     */
    public ObservableList<Appointment> getAppointments(int filterLevel, LocalDate filterDate) throws SQLException{
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String chosenDayString = filterDate + " " + "00:00:00";

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
        String[] dateTimeStringArray = dateTime.toString().split("T");
        String[] dateTimeTimeArray = dateTimeStringArray[1].split("\\.");
        String dateTimeString = dateTimeStringArray[0] + " " + dateTimeTimeArray[0];

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
            //ps.setString(1, localToUTC(String.valueOf(dateTime)));
            ps.setString(1, localToUTC(dateTimeString));
            ps.setString(2, localToUTC(nextMonth(year, month) + " 00:00:00"));
            appointments = database.buildAppointments(ps);
            database.closeConnection();
        }
        //Case Three, get appointments for week
        else if(filterLevel == 2) {
            query = "select * from appointments where start >= ? and start < ? order by start";
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, localToUTC(dateTimeString));
            ps.setString(2, localToUTC(nextWeek(year, month, day, numDaysThisMonth, wDay) + " 00:00:00"));
            appointments = database.buildAppointments(ps);
            database.closeConnection();
        }
        //Case Four, get appointments for today
        else if(filterLevel == 3) {
            query = "select * from appointments where start >= ? and start < ? order by start";
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, localToUTC(dateTimeString));
            ps.setString(2, localToUTC(nextDay(year, month, day, numDaysThisMonth) + " 00:00:00"));
            appointments = database.buildAppointments(ps);
            database.closeConnection();
        }
        //Case Five, get appointments within 15 minutes of login
        else if(filterLevel == 4){
            query = "select * from appointments where start >= ? and start < ? order by start";
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, localToUTC(dateTimeString));
            ps.setString(2, localToUTC(date + " " + nextFifteenMinutes(hour, minute)));
            appointments = database.buildAppointments(ps);
            database.closeConnection();
        }
        //get appointments for given day
        else{  //filterLevel == 5
            query = "select * from appointments where date(start) = date(?) order by start";
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, localToUTC(chosenDayString));
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
     * This method takes in a date and a start time and returns the start time of the next closest appointment
     *
     * @param dayOfAppointment the day selected
     * @param appointmentStartTime the start time selected
     * @return the available end times
     * @throws SQLException the exception if the connection fails
     */
    private String getNextAppointmentStart(LocalDate dayOfAppointment, String appointmentStartTime) throws SQLException{
        ObservableList<Appointment> appointments = getAppointments(5, dayOfAppointment);
        int numericStart = convertTimeToInt(appointmentStartTime);

        for(Appointment appointment : appointments){
            int numericNextStart = convertDateTimeToInt(appointment.getStartDateTime());
            if(numericNextStart > numericStart){
                return convertIntToTime(numericNextStart);

            }
        }
        return convertIntToTime(convertDateTimeToInt(easternToLocal(dayOfAppointment + " 22:00:00" )));
    }

    /**
     * This method builds an observable list representing all available end time slots based on the specified start time
     *
     * @param dayOfAppointment the day specified
     * @param startTime the start time specified
     * @return list of available end times
     * @throws SQLException the exception if the connection fails with the database
     */
    public ObservableList<String> getEndTimes(LocalDate dayOfAppointment, String startTime) throws SQLException{
        String nextAppointmentStart = getNextAppointmentStart(dayOfAppointment, startTime);
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
     * This method builds an observable list representing all available start time slots for today specified date
     *
     * @param dateOfAppointment the date specified
     * @return list of available start times
     * @throws SQLException the exception if connection fails
     */
    public ObservableList<String> getStartTimes(LocalDate dateOfAppointment) throws SQLException{
        ObservableList<Appointment> todaysAppointments = getAppointments(5, dateOfAppointment);
        for(Appointment appointment : todaysAppointments){
            System.out.println(appointment.getStartDateTime());
        }
        ObservableList<String> availableTimes = FXCollections.observableArrayList();                   //build a list to hold open time slots
        dateTime = LocalDateTime.now();
        LocalDate today = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        String[] timeParts = time.toString().split("\\.");
        String timeNoMilli = timeParts[0];
        LocalDateTime easternDateTime = LocalDateTime.parse(localToEastern(today + " " + timeNoMilli), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalTime easternTime = easternDateTime.toLocalTime();
        //Start of day 0800 or after current time if today
        int availableTime = 480;
        if(dateOfAppointment.isEqual(today)){
            availableTime = (easternTime.getHour() * 60) + (easternTime.getMinute() - (easternTime.getMinute() % 15) + 15);
        }
        //appointments must be sorted in ascending order by start time
        for(Appointment appointment : todaysAppointments) {
            int easternTimeConverted = convertDateTimeToInt(easternToLocal(dateOfAppointment + " " + convertIntToTime(availableTime)));
            System.out.println(easternTimeConverted);
            //Skip appointments for the day that are already passed
            if(convertDateTimeToInt(appointment.getStartDateTime()) < easternTimeConverted){
                System.out.println("appointment skipped: " + appointment.getStartDateTime());
                continue;
            }
            System.out.println("nothing skipped");
            int takenTime = convertDateTimeToInt(appointment.getStartDateTime());
            int duration = getAppointmentDurationAsInt(appointment);
            System.out.println("takenTime: " + takenTime + " duration:  " + duration);
            //Time slot available, add and advance
            //fixme something wrong here? convert int to time
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

        while(availableTime <1320){
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
        int hours;
        int minutes;
        int time = timeInMinutes;
        while(timeInMinutes > 1440){
            time -= 1440;
            //fixme add day to date if hours were greater than 24?
        }
        hours = time / 60;

        minutes = time - (hours * 60);
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
        String[] dateParts = date.split(" ");
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
            report.add("------------------------------------------CUSTOMER APPOINTMENTS------------------------------------------\n");
            report.add("TYPE                                              DATE                                        DESCRIPTION\n\n");
            ObservableList<Appointment> appointments = FXCollections.observableArrayList();
            String query = "select * from appointments order by type, start";
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            appointments = database.buildAppointments(ps);
            database.closeConnection();
            for(Appointment appointment : appointments){
                String line = String.format("%1$-42s %2$s %3$42s\n", appointment.getType(), appointment.getStartDateTime(), appointment.getDescription());
                report.add(line);
            }
            return report;
        }
        //Report 2, schedule for each contact in organization
        else if(reportNumber == 2){
            report.add("---------------------------------------------CONTACT SCHEDULES---------------------------------------------\n");
            report.add("   #     TITLE            START                END            TYPE                 DESCRIPTION     CUSTOMER\n\n");
            ObservableList<Contact> contacts = FXCollections.observableArrayList();
            String query1 = "select * from contacts order by contact_name";
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(query1);
            contacts = database.buildContacts(ps);
            database.closeConnection();
            for(Contact contact : contacts){
                ObservableList<Appointment> contactSchedule = FXCollections.observableArrayList();
                String query2 = "select * from appointments where Contact_ID = ? order by start";
                Connection connection1 = database.getConnection();
                PreparedStatement ps1 = connection1.prepareStatement(query2);
                ps1.setInt(1, contact.getContactId());
                contactSchedule = database.buildAppointments(ps1);
                database.closeConnection();
                report.add(contact.getContactName());
                if(contactSchedule.isEmpty()){
                    report.add("   No Appointments");
                }
                for(Appointment appointment : contactSchedule){
                    String line = String.format("   %1$-5s %2$-10s %3$-20s %4$-20s %5$-20s %6$-20s %7$s", appointment.getAppointmentId(), appointment.getTitle(), appointment.getStartDateTime(), appointment.getEndDateTime(),
                            appointment.getType(), appointment.getDescription(), appointment.getCustomerId());
                    report.add(line);
                }
            }
            return report;
        }
        //Report 3, total number of customers by country and region
        else{
            report.add("-------------------------------------------Customers by Region--------------------------------------------\n");
            report.add("                    Name                       Address                         Phone\n\n");
            ObservableList<Customer> customers = FXCollections.observableArrayList();
            String query1 = "select * from customers order by division_ID";
            Connection connection1 = database.getConnection();
            PreparedStatement ps1 = connection1.prepareStatement(query1);
            customers = database.buildCustomers(ps1);
            database.closeConnection();
            FirstLevelDivision division;
            String reportDivisionName = "";
            String reportCountryName = "";
            for (Customer customer : customers) {
                String query2 = "select * from First_Level_Divisions where division_ID = ?";
                Connection connection2 = database.getConnection();
                PreparedStatement ps2 = connection2.prepareStatement(query2);
                ps2.setInt(1, customer.getDivisionId());
                division = database.buildDivisions(ps2).get(0);
                String customerDivision = division.getDivisionName();
                Country country;
                String query3 = "select * from countries where country_ID = ?";
                Connection connection3 = database.getConnection();
                PreparedStatement ps3 = connection3.prepareStatement(query3);
                ps3.setInt(1, division.getCountryId());
                country = database.buildCountries(ps3).get(0);
                String customerCountry = country.getCountryName();
                //First case, customer new country and division
                String text = String.format("%1$20s %2$30s %3$30s", "                " + customer.getCustomerName(), customer.getAddress(), customer.getPhone());
                if (!(customerCountry.equals(reportCountryName))) {

                    report.add(customerCountry);
                    report.add("     " + customerDivision);
                    report.add(text);
                    reportCountryName = customerCountry;
                    reportDivisionName = customerDivision;
                    //second case, customer same country new division
                } else if (!(customerDivision.equals(reportDivisionName))) {
                    report.add("     " + customerDivision);
                    report.add(text);
                    reportDivisionName = customerDivision;
                    //third case, customer same country and division
                } else {
                    report.add(text);
                }
            }
            return report;
        }

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

    //==========================================Time Manipulation Methods=============================================

    /**
     * This method takes a string time in the users time zone and converts it into a string in the utc time zone
     *
     * @param localString the string to convert
     * @return the converted string
     */
    private String localToUTC(String localString){
        if(localString == null){
            return null;
        }
        ZoneId localZone = ZoneId.of(TimeZone.getDefault().getID());             //"This gets local timezone
        ZoneId utcZone = ZoneId.of("UTC");                                       //This is db timezone
        LocalDateTime localTime = LocalDateTime.parse(localString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ZonedDateTime localAppointmentTime = localTime.atZone(localZone);
        ZonedDateTime utcAppointmentTime = localAppointmentTime.withZoneSameInstant(utcZone);
        return utcAppointmentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * This method takes a string time in the users time zone and converts it into a string in the eastern time zone
     *
     * @param localString the string to convert
     * @return the converted string
     */
    private String localToEastern(String localString){
        ZoneId localZone = ZoneId.of(TimeZone.getDefault().getID());
        ZoneId eastZone = ZoneId.of("America/New_York");
        LocalDateTime localTime = LocalDateTime.parse(localString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ZonedDateTime localAppointmentTime = localTime.atZone((localZone));
        ZonedDateTime easternAppointmentTime = localAppointmentTime.withZoneSameInstant(eastZone);
        return easternAppointmentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
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

