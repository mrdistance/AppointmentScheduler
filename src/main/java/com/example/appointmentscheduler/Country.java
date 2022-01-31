package com.example.appointmentscheduler;

/**
 * This class provides an object to store countries pulled from a database
 * Only requires getters as country creation is outside the scope of the application
 *
 * Class Country.java
 */

/**
 * @author Joshua Call
 */

public class Country {

    private int countryId;
    private String countryName;

    /**
     * @param countryId the id to set
     * @param countryName the name to set
     */
    public Country(int countryId, String countryName){
        this.countryId = countryId;
        this.countryName = countryName;
    }

    /**
     * @return the country id
     */
    public int getCountryId() {
        return this.countryId;
    }

    /**
     * @return the country name
     */
    public String getCountryName() {
        return this.countryName;
    }
}
