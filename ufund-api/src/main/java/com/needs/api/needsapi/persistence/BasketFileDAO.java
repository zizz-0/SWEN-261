package com.needs.api.needsapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.needs.api.needsapi.model.FundingBasket;
import com.needs.api.needsapi.model.Login;
import com.needs.api.needsapi.model.Need;
import com.needs.api.needsapi.model.Profile;


/**
 * Implements the functionality for JSON file-based peristance for Funding Baskets
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Team Swiss Pandas
 * @author Jack Sutton
 */
@Component
public class BasketFileDAO implements BasketDAO {
    private static final Logger LOG = Logger.getLogger(BasketFileDAO.class.getName());
    Map<Integer,FundingBasket> baskets;   // Provides a local cache of the basket objects
                                            // so that we don't need to read from the file
                                            // each time
    private ObjectMapper objectMapper;  // Provides conversion between FundingBasket
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new need
    private String filename;    // Filename to read from and write to

    //Need Controller to deal with getting needs inside baskets

    /**
     * Creates a Funding Basket File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public BasketFileDAO(@Value("${baskets.file}") String filename,ObjectMapper objectMapper, NeedDAO needDAO) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the needs from the file
    }

    /**
     * Generates the next id for a new {@linkplain FundingBasket funding basket}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain FundingBasket funding baskets}
     * 
     * @return  The array of {@link FundingBasket funding baskets}, may be empty
     */
    private FundingBasket[] getBasketArray(){
        ArrayList<FundingBasket> basketArrayList = new ArrayList<>();

        for(FundingBasket basket : baskets.values()){
            basketArrayList.add(basket);
        }

        FundingBasket[] basketArray = new FundingBasket[basketArrayList.size()];
        basketArrayList.toArray(basketArray);
        return basketArray;
    }


    /**
     * Saves the {@linkplain FundingBasket baskets} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link FundingBasket baskets} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        FundingBasket[] basketArray = getBasketArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),basketArray);
        return true;
    }

    /**
     * Loads {@linkplain FundingBasket funding baskets} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        baskets = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of funding baskets
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        
        FundingBasket[] basketArray = objectMapper.readValue(new File("data/baskets.json"),FundingBasket[].class);

        // Add each need to the tree map and keep track of the greatest id
        for (FundingBasket basket : basketArray) {
            baskets.put(basket.getId(),basket);
            if (basket.getId() > nextId)
                nextId = basket.getId();
        }
        
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public HashMap<Integer, Integer> getNeeds(int basketId) {
        synchronized(baskets) {
            if (baskets.containsKey(basketId)){
                return baskets.get(basketId).getNeeds();
            }
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public FundingBasket getBasket(int basketId) {
        synchronized(baskets) {
            if (baskets.containsKey(basketId))
                return baskets.get(basketId);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public FundingBasket clearBasket(int basketId) throws IOException{
        synchronized(baskets){
            if(baskets.containsKey(basketId)){
                baskets.get(basketId).clearNeeds();
                save();
                return baskets.get(basketId);
            }
            else
                return null;
        }
    }
    
    /**
    ** {@inheritDoc}
     */
    @Override
    public FundingBasket createBasket(String userName) throws IOException {
        synchronized(baskets) {
            FundingBasket newBasket = new FundingBasket(nextId(),userName);
            baskets.put(newBasket.getId(),newBasket);
            save(); // may throw an IOException
            return newBasket;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteBasket(int basketId) throws IOException {
        synchronized(baskets) {
            if(baskets.containsKey(basketId)){
                baskets.remove(basketId);
                return save();
            }
            return false;
        }
    }

    /**
     * Adds a {@linkplain Need need} ID to a {@link FundingBasket funding basket}
     * 
     * @param basketId the {@link FundingBasket funding basket} to be added to
     * @param needId the id of the {@linkplain Need need} to be added
     *
     * @return updated {@link FundingBasket funding basket} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    public FundingBasket addNeed(int basketId, int needId, int quantity) throws IOException {
        synchronized(baskets) {
            if(
            baskets.containsKey(basketId)
            ){
                baskets.get(basketId).addNeed(needId, quantity);
                save();
                return baskets.get(basketId);
            }
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    public FundingBasket setQuantity(int basketId, int needId, int quantity) throws IOException{
        synchronized(baskets){
            if(baskets.containsKey(basketId)){
                if(baskets.get(basketId).getNeeds().containsKey(needId)){
                    baskets.get(basketId).setQuantity(needId, quantity);
                    save();
                    return baskets.get(basketId);
                }
            }
            return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    public int getQuantity(int basketId, int needId){
        synchronized(baskets){
            if(baskets.containsKey(basketId) && baskets.get(basketId).getNeeds().get(needId) != null){
                return baskets.get(basketId).getNeeds().get(needId);
            }
            return -1;
        }
    }


    /**
    ** {@inheritDoc}
     */
    public FundingBasket removeNeed(int basketId, int needId) throws IOException {
        synchronized(baskets) {
            if(
            baskets.containsKey(basketId) &&
            baskets.get(basketId).deleteNeed(needId)){
                return baskets.get(basketId);
            }
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public String getUsername(int basketId) throws IOException{
        synchronized(baskets){
            if(baskets.containsKey(basketId)){
                return baskets.get(basketId).getUsername();
            }
            return null;
        }
    }

    @Override
    public int getId(String username) throws IOException {
        for(FundingBasket basket : baskets.values()){
            if(basket.getUsername() == username){
                return basket.getId();
            }
        }
        return -1;
    }

    @Override
    public boolean setUsername(int basketId, String userName) throws IOException {
        synchronized(baskets){
            if(baskets.containsKey(basketId)){
                baskets.get(basketId).setUsername(userName);
                save();
                return true;
            
            }
            return false;
        }
    }
}
