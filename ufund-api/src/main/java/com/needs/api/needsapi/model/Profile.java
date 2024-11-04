package com.needs.api.needsapi.model;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a User Profile entity
 * 
 * @author Team Swiss Pandas
 * @author Ryan
 */
public class Profile {
    
    @JsonProperty("firstName") private String firstName;
    @JsonProperty("lastName") private String lastName;
    @JsonProperty("email") private String email;
    @JsonProperty("country") private String country;
    @JsonProperty("isPrivate") private boolean isPrivate;
    @JsonProperty("userName") private String userName;
    @JsonProperty("contributions") private HashMap<Integer,Integer> contributions;

    static final String STRING_FORMAT = "Profile [firstName = %s, lastName = %s, email = %s, country = %s, isPrivate = %s]";
    
    
    /**
     * Creates an empty Profile {@linkplain Profile profile} 
     * @param firstName users first name
     * @param lastName users last name
     * @param email users eamil address
     * @param country users country
     * @param userName users username
     */
    public Profile(@JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("email") String email, @JsonProperty("country") String country, @JsonProperty("userName") String userName, @JsonProperty("contributions")  HashMap<Integer,Integer> contributions){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.country = country; 
        this.userName = userName;
        this.isPrivate = false;
        this.contributions = contributions;
    }

    /**
     * @return users first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * sets the users frist name
     * @param firstName users first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return users last name
     */
    public String getLastName() {
        return lastName;
    }

     /**
     * sets the users last name
     * @param lastName users last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return users email address
     */
    public String getEmail() {
        return email;
    }

     /**
     * sets the users email address
     * @param email users email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return users country
     */
    public String getCountry() {
        return country;
    }

     /**
     * sets the users country
     * @param country users country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return users username 
     */
    public String getUserName() {
        return this.userName;
    }


    /**
     * @return if a user is private 
     */
    public boolean getIsPrivate() {
        return this.isPrivate;
    }

    /**
     * switchs the user to private or un privates them
     */
    public void switchPrivacy() {
        if(this.isPrivate == true){
            this.isPrivate = false;
        }else{
            this.isPrivate = true;
        } 
    }

        /**
     * adds a contribution to the users contributions list
     * @param needId id of need contributed
     * @param quantity quantity contributed
     */
    public void addContribution(int needId, int quantity){
        if(contributions.containsKey(needId)){
            contributions.put(needId, contributions.get(needId) + quantity);
        }else{
            contributions.put(needId, quantity);
        }
    }

    public HashMap<Integer, Integer> getContributions(){
        return this.contributions;
    }

    public void setContributions(HashMap<Integer, Integer> contributions){
        this.contributions = contributions;
    }

     /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.userName + "'s " + String.format(STRING_FORMAT,firstName,lastName,email,country,isPrivate);
    }

}
