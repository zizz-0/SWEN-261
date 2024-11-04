package com.needs.api.needsapi.model;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.needs.api.needsapi.persistence.BasketFileDAO;

/**
 * Represents a Funding Basket entity
 * 
 * @author Team Swiss Pandas
 * @author Ryan
 */
public class FundingBasket {
    private static final Logger LOG = Logger.getLogger(FundingBasket.class.getName());
    @JsonProperty("id") private int basketId;
    @JsonProperty("needs") private HashMap<Integer, Integer> listOfNeeds;
    @JsonProperty ("userName") private String userName;
    
    static final String STRING_FORMAT = "Need [id=%d, quantity=%d]";
    
    /**
     * Creates an empty funding basket 
     * @param id the id for the basket, given by nextId in the {@link BasketFileDAO file DAO}
     * 
     */
    public FundingBasket(@JsonProperty("id") int id, @JsonProperty("userName") String userName){
        this.basketId = id;
        this.userName = userName;
        listOfNeeds = new HashMap<Integer, Integer>();
    }

    /*
     * Retrieves the id of the basket
     */
    public int getId(){
        return this.basketId;
    }
    /*
     * Retrieves the userName of the user tied to the basket
     */
    public String getUsername(){
        return this.userName;
    }

    /*
     * Sets the userName of the user tied to the basket
     */
    public void setUsername(String userName){
        this.userName = userName;
    }

     /**
     * Retrieves all needs in the basket
     * @return all needs in the basket 
     */
    public HashMap<Integer, Integer> getNeeds() {
        return this.listOfNeeds;
    }

    /**
     * adds a given quantity of a need to the funding basket
     * @param needId id of {@link Need need} that you want to add to the funding basket
     * does not check if need exists, this is checked by BasketController.
     */
    public void addNeed(int needId, int quantity) {
        listOfNeeds.put(needId, quantity);
    }

    /**
     * sets the quantity of a certain need in a users basket
     * @param needId need to be changed
     * @param quantity quantity to change to
     * does not check if need exists, this is checked by BasketController.
     * returns quantity set
     */
    public int setQuantity(int needId, int quantity){
        listOfNeeds.put(needId, quantity);
        return listOfNeeds.get(needId);
    }

    /**
     * adds to the quantity of a certain need in a users basket
     * @param needId need to be changed
     * @param quantity quantity to add
     * does not check if need exists, this is checked by BasketController.
     */
    public void addQuantity(int needId, int quantity){
        listOfNeeds.put(needId, listOfNeeds.get(needId) + quantity);
    }

    /**
     * Deletes a Need form the funding basket
     * @param needID the id of the need
     */
    public boolean deleteNeed(int needId){
        if(listOfNeeds.containsKey(needId)){
            listOfNeeds.remove(Integer.valueOf(needId));
            return true;
        }
        return false;
    }

    public void clearNeeds(){
        listOfNeeds = new HashMap<Integer, Integer>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String returnValue = "Funding Basket [id: " + this.basketId + ", user: " +this.userName+ "]";
        for(Map.Entry<Integer, Integer> set : listOfNeeds.entrySet()){
            returnValue += ",\n";
            returnValue += String.format(STRING_FORMAT,set.getKey(),set.getValue());
        }
        return returnValue;
    }

}
