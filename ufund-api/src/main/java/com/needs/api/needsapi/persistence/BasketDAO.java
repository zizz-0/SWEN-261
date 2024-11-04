package com.needs.api.needsapi.persistence;

import java.io.IOException;
import java.util.HashMap;
import com.needs.api.needsapi.model.Need;
import com.needs.api.needsapi.model.FundingBasket;

/**
 * Defines the interface for Funding Basket object persistence
 * 
 * @author Team Swiss Pandas
 * @author Jack Sutton
 */
public interface BasketDAO {
    /**
     * Retrieves a {@link FundingBasket funding basket} with given id
     * 
     * @param basketId The id of the {@link FundingBasket funding basket}
     * 
     * @return A {@link FundingBasket funding basket} object
     * 
     * @throws IOException if an issue with underlying storage
     */
    FundingBasket getBasket(int basketId) throws IOException;

    /**
     * Retrieves an id using username
     * 
     * @param username The username of the {@link FundingBasket funding basket}
     * 
     * @return id of basket, or -1 if not found
     * 
     * @throws IOException if an issue with underlying storage
     */
    int getId(String username) throws IOException;

    /**
     * Retrieves all {@link Need needs} from given {@link FundingBasket funding basket}
     * 
     * @param basketId The id of the {@link FundingBasket funding basket}
     * 
     * @return An array of {@link Need need} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    HashMap<Integer, Integer> getNeeds(int basketId) throws IOException;

    /**
     * Retrieves the username for the {@link User user} that a given funding basket belongs to
     * 
     * @param basketId The id of the {@link FundingBasket funding basket}
     * 
     * @return A username attached to a {@link User user}, null if basket not found
     * 
     * @throws IOException if an issue with underlying storage
     */
    String getUsername(int basketId) throws IOException;

    /**
     * Sets the username for the {@link User user} that a given funding basket belongs to
     * 
     * @param basketId The id of the {@link FundingBasket funding basket}
     * @param userName The userName of the {@link FundingBasket funding basket}
     * 
     * @return true if successful, false otherwise
     * 
     * @throws IOException if an issue with underlying storage
     */
    boolean setUsername(int basketId, String userName) throws IOException;

    /**
     * Adds a {@linkplain Need need} ID to a {@link FundingBasket funding basket}
     * 
     * @param basketId the {@link FundingBasket funding basket} to be added to
     * @param need the {@linkplain Need need} to be added
     *
     * @return updated {@link FundingBasket funding basket} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    FundingBasket addNeed(int basketId, int needId, int quantity) throws IOException;

    /**
     * Removes a {@linkplain Need need} ID from a {@link FundingBasket funding basket}
     * 
     * @param basketId the {@link FundingBasket funding basket} to be removed from
     * @param need the {@linkplain Need need} to be removed
     *
     * @return updated {@link FundingBasket funding basket} if successful, null otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    FundingBasket removeNeed(int basketId, int needId) throws IOException;

    /**
     * Removes all {@linkplain Need needs} in a {@link FundingBasket funding basket} 
     * 
     * @param basketId the {@link FundingBasket funding basket} to be cleared
     *
     * @return the empty {@link FundingBasket funding basket} if successful, null otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    FundingBasket clearBasket(int basketId) throws IOException;

    /**
     * Creates an empty {@link FundingBasket funding basket} tied to a {@link User user}. 
     * Only to be called upon the creation of a user
     * 
     * @param username the username of the {@link User user} to get a funding basket
     *
     * @return the created {@link FundingBasket funding basket} if successful, null otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    FundingBasket createBasket(String username) throws IOException;

    /**
     * Deletes a {@link FundingBasket funding basket}. 
     * Only to be called upon the deletion of a user
     * 
     * @param basketId the {@link FundingBasket funding basket} to be deleted
     *
     * @return true if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    boolean deleteBasket(int basketId) throws IOException;

    /**
     * sets the quantity of a certain need in the funding basket
     * returns null if need or basket not found
     * @param basketId basket to edit
     * @param needId need within basket to edit
     * @param quantity quantity to set to
     * @return updated funding basket
     * @throws IOException
     */
    FundingBasket setQuantity(int basketId, int needId, int quantity) throws IOException;
    
    /**
     * gets the quantity of a need in a given basket
     * @param basketId basket to get need from
     * @param needId need to get quantity of
     * @return the quantity, returns -1 if basket or need not found
     */
    int getQuantity(int basketId, int needId);
}
