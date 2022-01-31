package com.example.appointmentscheduler;

/**
 * This class provides an object to store contacts pulled from a database
 * Only requires getters as contact creation is outside the scope of the application
 *
 * Class Contact.java
 */

/**
 * @author Joshua Call
 */

public class Contact {

    private int contactId;
    private String contactName;

    /**
     * @param contactId the id to set
     * @param contactName the name to set
     */
    public Contact(int contactId, String contactName){
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /**
     * @return the contact id
     */
    public int getContactId(){
        return this.contactId;
    }

    /**
     * @return the contact name
     */
    public String getContactName(){
        return this.contactName;
    }
}
