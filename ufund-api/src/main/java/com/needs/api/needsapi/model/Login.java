package com.needs.api.needsapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a User entity
 * 
 * @author Team Swiss Pandas
 */

public class Login {
    private static final Logger LOG = Logger.getLogger(Need.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Login [username=%s, password=%s]";

    @JsonProperty("userName") private String username;
    @JsonProperty("pass") private String password;
    @JsonProperty("basketId") private int basketId;

    /**
     * Create a user with the given id, username, and password
     * @param username The user's username
     * @param password The user's password
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Login(@JsonProperty("userName") String username, @JsonProperty("pass") String password, @JsonProperty("basketId") int basketId) {
        this.username = username;
        this.password = password;
        this.basketId = basketId;
    }

    /**
     * Retrieves the user's username
     * @return The user's username
     */
    public String getUserName() {return username;}

    /**
     * Retrieves the user's password
     * @return The user's password
     */
    public String getPass() {return password;}

    /**
     * Retrieves the user's funding basket ID
     * @return The user's basketId
     */
    public int getBasketId(){return basketId;}

    /**
     * Sets the user's username - necessary for JSON object to Java object deserialization
     * @param username The user's username
     */
    public void setName(String username) {this.username = username;}

    /**
     * sets the users basket id to the given basketId
     * @param baskedId basketId to set
     */
    public void setBasketId(int basketId){
        this.basketId = basketId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,username,password);
    }
}
