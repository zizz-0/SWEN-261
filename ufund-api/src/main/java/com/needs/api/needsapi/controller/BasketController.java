package com.needs.api.needsapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;


import com.needs.api.needsapi.persistence.BasketDAO;
import com.needs.api.needsapi.model.FundingBasket;
import com.needs.api.needsapi.model.Need;

/**
 * Handles the REST API requests for the Basket resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Team Swiss Pandas
 * @author Ryan Garvin
 */

@RestController
@RequestMapping("baskets")
public class BasketController {
    private static final Logger LOG = Logger.getLogger(BasketController.class.getName());
    private BasketDAO basketDAO;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param basketDAO The {@link BasketDAO Basket Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public BasketController(BasketDAO basketDAO) {
        this.basketDAO = basketDAO;
    }

    /**
     * Responds to the GET request for a {@linkplain FundingBasket basket} for the given id
     * 
     * @param id The id used to locate the {@link FundingBasket basket}
     * 
     * @return ResponseEntity with {@link FundingBasket basket} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<FundingBasket> getBasket(@PathVariable int id) {
        LOG.info("GET /baskets/" + id);
        try {
            FundingBasket basket = basketDAO.getBasket(id);
            if (basket != null)
                return new ResponseEntity<FundingBasket>(basket,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Need needs} in a {@linkplain FundingBasket basket}
     * 
     * @return ResponseEntity with array of {@link Need need} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}/needs")
    public ResponseEntity<HashMap<Integer, Integer>> getNeeds(@PathVariable int id) {
        LOG.info("GET /baskets/" + id + "/needs");
        try {
            HashMap<Integer, Integer> needs = basketDAO.getNeeds(id);
            LOG.info(needs.toString());
            return new ResponseEntity<HashMap<Integer, Integer>>(needs,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for a {@linkplain User userName} for the given basketId
     * 
     * @param basketId The id used to locate the {@link FundingBasket basket}
     * 
     * @return ResponseEntity with userName String and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}/user")
    public ResponseEntity<String> getUsername(@PathVariable int id) {
        LOG.info("GET /baskets/" + id + "/user");
        try {
            String userName = basketDAO.getUsername(id);
            if (userName != null){
                LOG.info("get username returning " + userName);
                return new ResponseEntity<String>(userName,HttpStatus.OK);
            }
            else
                return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

      /**
     * sets the quantity of a given need in a given funding basket
     * @param basketId basket with need to change
     * @param username username to change 
     * @return updated funding basket with 200 OK, or 404 if basket or need is not found
     */
    @PutMapping("/{basketId}/user/{username}")
    public ResponseEntity<Boolean> setUserName(   @PathVariable int basketId,  
                                                        @PathVariable String username){
        LOG.info("PUT /baskets/"+ basketId + "/" + username);
        try{
        
            Boolean updatedBasket = basketDAO.setUsername(basketId, username);
            if(updatedBasket){
                return new ResponseEntity<Boolean>(updatedBasket, HttpStatus.OK);
            }    
    
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    /**
     * Creates a {@linkplain FundingBasket basket} with the provided userName
     * 
     * @param userName - The {@link User id} to create the basket
     * 
     * @return ResponseEntity with created {@link FundingBasket basket} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link FundingBasket basket} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<FundingBasket> createBasket(@RequestParam("userName") String userName) {
        LOG.info("POST /baskets/?userName= " + userName);

        try {
            FundingBasket newBasket = basketDAO.createBasket(userName);
            if (newBasket == null){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            else
                return new ResponseEntity<FundingBasket>(newBasket, HttpStatus.CREATED);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Need need} in a {@linkplain FundingBasket basket} with the given id
     * 
     * @param needId The id for the {@link Need need} to delete
     * @param basketId The id used to locate the {@link FundingBasket basket}
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{basketId}/{needId}")
    public ResponseEntity<FundingBasket> removeNeed(@PathVariable int basketId, @PathVariable int needId) {
        LOG.info("DELETE /baskets/"+ basketId + "/" + needId);
        try {
            FundingBasket updatedBasket = basketDAO.removeNeed(basketId, needId);
            if (updatedBasket == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else
                return new ResponseEntity<FundingBasket>(updatedBasket, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     /**
     * adds a {@linkplain Need need} to a {@linkplain FundingBasket basket} with the given id
     * quantity added is set to 1 unless otherwise specified
     * if the need already exists in the basket, the quantity given is added to the existing quantity
     * 
     * @param needId The id for the {@link Need need} to be added
     * @param basketId The id used to locate the {@link FundingBasket basket}
     * 
     * @return ResponseEntity HTTP status of OK if added<br>
     * ResponseEntity with HTTP status of CONFLICT if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/{basketId}/{needId}")
    public ResponseEntity<FundingBasket> addNeed(   @PathVariable int basketId, 
                                                    @PathVariable int needId, 
                                                    @RequestParam(defaultValue = "1", name = "quantity") int quantity) {
        LOG.info("PUT /baskets/"+ basketId + "/" + needId +"?quantity=" + quantity);
        try {
            FundingBasket updatedBasket;
            if(basketDAO.getQuantity(basketId, needId) != -1){
                updatedBasket = basketDAO.setQuantity(basketId, needId, basketDAO.getQuantity(basketId, needId) + quantity);
                LOG.info("Basket is doing quantity");
            }else{
                updatedBasket = basketDAO.addNeed(basketId, needId, quantity);
                LOG.info("Basket adding need");
            }
            if (updatedBasket != null && updatedBasket.getNeeds().size() > 0){
                return new ResponseEntity<FundingBasket>(updatedBasket, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * sets the quantity of a given need in a given funding basket
     * @param basketId basket with need to change
     * @param needId need with quantity to change
     * @param quantity quantity to change need to
     * @return updated funding basket with 200 OK, or 404 if basket or need is not found
     */
    @PutMapping("/{basketId}/{needId}/{quantity}")
    public ResponseEntity<FundingBasket> setQuantity(   @PathVariable int basketId, 
                                                        @PathVariable int needId, 
                                                        @PathVariable int quantity){
        LOG.info("PUT /baskets/"+ basketId + "/" + needId +"/" + quantity);
        try{
            if(basketDAO.getQuantity(basketId, needId) != -1){
                FundingBasket updatedBasket = basketDAO.setQuantity(basketId, needId, quantity);
                return new ResponseEntity<FundingBasket>(updatedBasket, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * gets the quantity of a given need in a given basket
     * @param basketId the basket to get the need from
     * @param needId the need to get the quantity from
     * @return response entity with quantity; 200 OK, 404 if no basket or need
     */
    @GetMapping("/{basketId}/{needId}")
    public ResponseEntity<Integer> getQuantity(@PathVariable int basketId, @PathVariable int needId){
        LOG.info("GET /baskets/"+ basketId + "/" + needId);
        int quantity = basketDAO.getQuantity(basketId, needId);
        if(quantity!=-1){
            return new ResponseEntity<Integer>(quantity, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Deletes a {@linkplain FundingBasket basket} with the given basked id
     * 
     * 
     * @param basketId The id used to locate the {@link FundingBasket basket}
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<FundingBasket> deleteBasket(@PathVariable int basketId) {
        LOG.info("DELETE /baskets/"+ basketId);
        try {
            boolean deleted = basketDAO.deleteBasket(basketId);
            if (deleted)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * claers the {@linkplain FundingBasket basket} with the given id
     * 
     * 
     * @param basketId The id used to locate the {@link FundingBasket basket}
     * 
     * @return ResponseEntity HTTP status of OK if cleared<br>
     * ResponseEntity with HTTP status of CONFLICT if not cleared<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/clear")
    public ResponseEntity<FundingBasket> clearBasket(@RequestParam(name = "basketId") int basketId) {
        LOG.info("PUT /baskets/clear?basketId = " + basketId);
        try {
            FundingBasket emptyBasket = basketDAO.clearBasket(basketId);
            if (emptyBasket.getNeeds().size() != 0){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            else
                return new ResponseEntity<FundingBasket>(emptyBasket, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
