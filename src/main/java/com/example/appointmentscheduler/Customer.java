package com.example.appointmentscheduler;

/**
 * This class provides an object to store customers pulled from and pushed to a database
 * Requires setters, getters, and input validation as customers can be created and deleted
 *
 * Class Customer.java
 */

/**
 * @author Joshua Call
 */

public class Customer {

    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;

    /**
     * @param customerId the id to set
     * @param customerName the name to set
     * @param address the address to set
     * @param postalCode the postal code to set
     * @param phone the phone to set
     * @param divisionId the division id to set
     */

    public Customer(int customerId, String customerName, String address, String postalCode, String phone, int divisionId){
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
    }


    //Implement Error checks inside these setter methods to clean up gui and main classes-----------------------------------
    //Customer Id should be generated from the database automatically upon creation ----------------------------------------


    /**
     * @param customerId the customer id to set
     */
    public void setCustomerId(int customerId){
        this.customerId =customerId;
    }

    /**
     * @return the customer id
     */
    public int getCustomerId(){
        return this.customerId;
    }

    /**
     * @param customerName the customer name to set
     */
    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }

    /**
     * @return the customer name
     */
    public String getCustomerName(){
        return this.customerName;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address){
        this.address = address;
    }

    /**
     * @return the address
     */
    public String getAddress(){
        return this.address;
    }

    /**
     * @param postalCode the postal code to set
     */
    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }

    /**
     * @return the postal code
     */
    public String getPostalCode(){
        return this.postalCode;
    }

    /**
     * @param phone the phone number to set
     */
    public void setPhone(String phone){
        this.phone = phone;
    }

    /**
     * @return the phone number
     */
    public String getPhone(){
        return this.phone;
    }

    /**
     * @param divisionId the division id to set
     */
    public void setDivisionId(int divisionId){
        this.divisionId = divisionId;
    }

    /**
     * @return the division id
     */
    public int getDivisionId(){
        return this.divisionId;
    }
}
