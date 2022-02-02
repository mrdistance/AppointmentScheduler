package com.example.appointmentscheduler;

/**
 * This class provides an object to store appointments pulled from and pushed to a database
 * Requires setters, getters, and input validation as appointments can be created and deleted
 *
 * Class Appointment.java
 */

import java.time.LocalDateTime;

/**
 * @author Joshua Call
 */

public class Appointment {

    //need some sort of datetime object to store booked times to black out in date time selector
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startDateTime;                //from sql will need to be converted to toLocalDateTime
    private LocalDateTime endDateTime;
    private int customerId;
    private int userId;
    private int contactId;

    /**
     * @param appointmentId the appointment id to set
     * @param title the title to set
     * @param description the description to set
     * @param location the location to set
     * @param type the type to set
     * @param startDateTime the start date and time to set
     * @param endDateTime the end date and time to set
     * @param customerId the customer id to set
     * @param userId the user id to set
     * @param contactId the contact id to set
     */
    public Appointment(int appointmentId, String title, String description, String location, String type,
                       LocalDateTime startDateTime, LocalDateTime endDateTime, int customerId, int userId, int contactId){
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }


    //Implement Error checks inside these setter methods to clean up gui and main classes-----------------------------------
    //Appointment Id should be generated from the database automatically upon creation -------------------------------------

    /**
     * @return the appointment id
     */
    public int getAppointmentId() {
        return this.appointmentId;
    }

    /**
     * @param appointmentId the appointment id to set
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the type
     */
    public String getType() {
        return this.type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the start date and time
     */
    public LocalDateTime getStartDateTime() {
        return this.startDateTime;
    }

    /**
     * @param startDateTime the start date and time to set
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * @return the end date and time
     */
    public LocalDateTime getEndDateTime() {
        return this.endDateTime;
    }

    /**
     * @param endDateTime the end date and time to set
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * @return the customer id
     */
    public int getCustomerId() {
        return this.customerId;
    }

    /**
     * @param customerId the customer id to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the user id
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * @param userId the user id to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the contact id
     */
    public int getContactId() {
        return this.contactId;
    }

    /**
     * @param contactId the contact id to set
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}
