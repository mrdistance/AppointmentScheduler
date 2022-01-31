package com.example.appointmentscheduler;

/**
 * This class provides an object to store first level divisions pulled from a database
 * Only requires getters as first level division creation is outside the scope of this application
 *
 * Class FirstLevelDivison.java
 */

/**
 * @author Joshua Call
 */

public class FirstLevelDivision {

    private int divisionId;
    private String divisionName;
    private int countryId;

    /**
     * @param divisionId the division id to set
     * @param divisionName the name to set
     * @param countryId the country id to set
     */
    public FirstLevelDivision(int divisionId, String divisionName, int countryId){
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }

    /**
     * @return the division id
     */
    public int getDivisionId() {
        return this.divisionId;
    }

    /**
     * @return the division name
     */
    public String getDivisionName(){
        return this.divisionName;
    }

    /**
     * @return the country id
     */
    public int getCountryId(){
        return this.countryId;
    }
}
