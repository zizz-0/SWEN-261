package com.needs.api.needsapi.persistence;

import java.io.IOException;
import com.needs.api.needsapi.model.Need;

/**
 * Defines the interface for Need object persistence
 * 
 * @author Team Swiss Pandas
 * @author SWEN Faculty
 */
public interface NeedDAO {
    /**
     * Retrieves all {@linkplain Need needs}
     * 
     * @return An array of {@link Need need} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Need[] getNeeds() throws IOException;

    /**
     * Finds all {@linkplain Need needs} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Need needs} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Need[] findNeeds(String containsText) throws IOException;

    /**
     * Determines if a need already exists
     * 
     * @param needName The text to match against
     * 
     * @return true or false depending on if need exists in needs cupboard already
     * 
     * @throws IOException if an issue with underlying storage
     */
    boolean needExists(String needName) throws IOException;

    /**
     * Retrieves a {@linkplain Need need} with the given id
     * 
     * @param id The id of the {@link Need need} to get
     * 
     * @return a {@link Need need} object with the matching id
     * <br>
     * null if no {@link Need need} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Need getNeed(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain Need need}
     * 
     * @param need {@linkplain Need need} object to be created and saved
     * <br>
     * The id of the need object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Need need} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Need createNeed(Need need) throws IOException;

    /**
     * Updates and saves a {@linkplain Need need}
     * 
     * @param {@link Need need} object to be updated and saved
     * 
     * @return updated {@link Need need} if successful, null if
     * {@link Need need} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Need updateNeed(Need need) throws IOException;

    /**
     * gets the total quantity needed of all needs
     * @return int with total
     */
    public int getTotalNeeded();

    /**
     * gets the total quantity of needs fulfilled.
     * If a need has a higher quantity fulfilled than needed, then
     * only the quantity needed is added
     * @return int with total
     */
    public int getTotalFulfilled();

    /**
     * Deletes a {@linkplain Need need} with the given id
     * 
     * @param id The id of the {@link Need need}
     * 
     * @return true if the {@link Need need} was deleted
     * <br>
     * false if need with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteNeed(int id) throws IOException;
}
