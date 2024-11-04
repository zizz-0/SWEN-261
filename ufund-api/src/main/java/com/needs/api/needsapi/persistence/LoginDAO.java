package com.needs.api.needsapi.persistence;

import java.io.IOException;
import com.needs.api.needsapi.model.Login;

/**
 * Defines the interface for Login object persistence
 * 
 * @author Team Swiss Pandas
 * @author Oscar Li
 */
public interface LoginDAO {
    /**
     * Retrieves all {@linkplain Login logins}
     * 
     * @return An array of {@link Login login} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Login[] getLogins() throws IOException;

    /**
     * Finds all {@linkplain Login logins} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Login logins} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Login[] findLogins(String containsText) throws IOException;

    /**
     * Checks if a username already exists
     * 
     * @param userName userName to check if exists
     * 
     * @return true or false depending on if userName already exists
     * 
     * @throws IOException if an issue with underlying storage
     */
    public boolean userExists(String userName) throws IOException;

    /**
     * Retrieves a {@linkplain Login login} with the given containText
     * 
     * @param containsText The userName of the {@link Login login} to get
     * 
     * @return a {@link Login login} object with the matching userName
     * <br>
     * null if no {@link Login login} with a matching userName is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Login getLogin(String containsText) throws IOException;

    /**
     * Creates and saves a {@linkplain Login login}
     * 
     * @param login {@linkplain Login login} object to be created and saved
     *
     * @return new {@link Login login} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Login createLogin(Login login) throws IOException;

    /**
     * Updates and saves a {@linkplain Login login}
     * 
     * @param {@link Login login} object to be updated and saved
     * @param {@link String userName} the user name of the login that is being updated
     * 
     * @return updated {@link Login login} if successful, null if
     * {@link Login login} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Login updateLogin( String userName, Login login) throws IOException;

    /**
     * Deletes a {@linkplain Login login} with the given string
     * 
     * @param containsText The userName of the {@link Login login}
     * 
     * @return true if the {@link Login login} was deleted
     * <br>
     * false if login with the given userName does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteLogin(String containsText) throws IOException;

    /**
     * Sets the basketId of the Login
     * @param userName - username of the login to change the id of
     * @param basketId - basketId to set to
     * @return {@linkplain Login login} with changed basketId
     * @throws IOException if underlying storage cannot be accessed
     */
    Login setBasketId(String userName, int basketId) throws IOException;

    /**
     * Gets basketId of a given login
     * @param userName the username of the login thats basketId will be returned
     * @return the basketId int
     */
    int getBasketId(String userName);
}
