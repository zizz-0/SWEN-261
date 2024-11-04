package com.needs.api.needsapi.persistence;

import java.io.IOException;
import java.util.HashMap;

import com.needs.api.needsapi.model.Login;
import com.needs.api.needsapi.model.Need;
import com.needs.api.needsapi.model.Profile;


/**
 * Defines the interface for UserPorfile object persistence
 * 
 * @author Team Swiss Pandas
 * @author Ryan Garvin
 */
public interface ProfileDAO {
    /**
     * Retrieves a {@link Profile user profile} with given username 
     * 
     * @param userName The username of the {@link  Profile user profile}
     * 
     * @return A {@link  Profile user profile} object
     * 
     * @throws IOException if an issue with underlying storage
     */
    Profile getProfile(String userName) throws IOException;


    /**
     * updates a {@link Profile user profile} first name 
     * 
     * @param profile {@link Profile profile} the updated profile
     *
     * @return updated {@link Profile user profile} if successful, null otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Profile updateProfile(Profile profile) throws IOException;


    /**
     * Creates and saves a {@linkplain Profile user profile}
     * 
     * @param profile {@linkplain Profile user profile} object to be created and saved
     *
     * @return new {@link Profile user profile} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Profile createProfile(Profile profile) throws IOException;

    /**
     * Deletes a {@linkplain Profile profile} with the given string
     * 
     * @param containsText The userName of the {@link Profile profile}
     * 
     * @return true if the {@link Profile profile} was deleted
     * <br>
     * false if profile with the given userName does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteProfile(String containsText) throws IOException;


    /**
     * Switches a {@linkplain Profile user profile} to be private or public 
     *
     * @param profile {@linkplain Profile user profile} to update 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Profile switchPrivacy(Profile profile) throws IOException;


    
}
