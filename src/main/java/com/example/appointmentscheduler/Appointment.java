package com.example.appointmentscheduler;

/**
 * This class provides an object to store appointments pulled from and pushed to a database
 * Requires setters, getters, and input validation as appointments can be created and deleted
 *
 * Class Appointment.java
 */
public class Appointment {

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private String startDateTime;
    private String endDateTime;
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
                       String startDateTime, String endDateTime, int customerId, int userId, int contactId){
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
    public String getStartDateTime() {
        return this.startDateTime;
    }

    /**
     * @param startDateTime the start date and time to set
     */
    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * @return the end date and time
     */
    public String getEndDateTime() {
        return this.endDateTime;
    }

    /**
     * @param endDateTime the end date and time to set
     */
    public void setEndDateTime(String endDateTime) {
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
