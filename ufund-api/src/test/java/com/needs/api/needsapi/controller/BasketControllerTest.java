package com.needs.api.needsapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import java.util.HashMap;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.needs.api.needsapi.model.FundingBasket;

import com.needs.api.needsapi.persistence.BasketDAO;

import org.springframework.http.HttpStatus;
/**
 * Test the FundingBasket Controller class
 * 
 * @author Team Swiss Pandas
 */
@Tag("Controller-tier")
public class BasketControllerTest {
    private BasketController basketController;
    private BasketDAO mockBasketDAO;

    /**
     * Before each test, create a new basketController object and inject
     * a mock Basket DAO
     */
    @BeforeEach
    public void setupFundingBasketController() {
        mockBasketDAO = mock(BasketDAO.class);   
        basketController = new BasketController(mockBasketDAO);
    }

    @Test
    public void testGetBasket() throws IOException {  
        // Setup
        FundingBasket basket = new FundingBasket(1, "Username");
        // When the same id is passed in, our mock Basket DAO will return the FundingBasket object
        when(mockBasketDAO.getBasket(basket.getId())).thenReturn(basket);

        // Invoke
        ResponseEntity<FundingBasket> response = basketController.getBasket(basket.getId());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(basket,response.getBody());
    }

    @Test
    public void testGetBasketNotFound() throws Exception {
        // Setup
        int basketId = 99;
        // When the same id is passed in, our mock Bakset DAO will return null, simulating
        // no basket found
        when(mockBasketDAO.getBasket(basketId)).thenReturn(null);

        // Invoke
        ResponseEntity<FundingBasket> response = basketController.getBasket(basketId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetBasketHandleException() throws Exception { 
        // Setup
        int basketId = 99;
        // When getFundingBasket is called on the Mock FundingBasket DAO, throw an IOException
        doThrow(new IOException()).when(mockBasketDAO).getBasket(basketId);
        // Invoke
        ResponseEntity<FundingBasket> response = basketController.getBasket(basketId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }


    @Test
    public void testCreateBasket() throws IOException {  
        // Setup
        FundingBasket basket = new FundingBasket(2, "username");
        
        // when create basket is called, return true simulating successful
        // creation and save
        when(mockBasketDAO.createBasket(basket.getUsername())).thenReturn(basket);

        // Invoke
        ResponseEntity<FundingBasket> response = basketController.createBasket(basket.getUsername());

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(basket,response.getBody());
    }

    @Test
    public void testCreateBasketConflict() throws IOException {  
        // Setup
        FundingBasket basket = new FundingBasket(2, "username");
        
        // when create Basket is called, return null simulating a conflict
        when(mockBasketDAO.createBasket(basket.getUsername())).thenReturn(null);

        // Invoke
        ResponseEntity<FundingBasket> response = basketController.createBasket(basket.getUsername());

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateBasketHandleException() throws IOException {  
        // Setup
        FundingBasket basket = new FundingBasket(2, "username");

        // When create Basket is called on the Mock Basket DAO, throw an IOException
        doThrow(new IOException()).when(mockBasketDAO).createBasket(basket.getUsername());

        // Invoke
        ResponseEntity<FundingBasket> response = basketController.createBasket(basket.getUsername());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testAddNeed() throws IOException { 
        // Setup
        FundingBasket basket = new FundingBasket(2, "username");
        int need = 0;
        // adds a need to the basket
        basket.addNeed(need, 1);
        
        // when add Need is called, return the updated basket with new need
        when(mockBasketDAO.addNeed(basket.getId(), need, 1)).thenReturn(basket);
        when(mockBasketDAO.getQuantity(basket.getId(), need)).thenReturn(-1);
       

        // Invoke
        ResponseEntity<FundingBasket> response = basketController.addNeed(basket.getId(), need,1 );

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        
    }

    @Test
    public void testAddNeedConflict() throws IOException { 
        // Setup
        FundingBasket basket = new FundingBasket(2, "username");
        int need = 0;
        // adds a need to the basket
        basket.addNeed(need,1 );
        
        // when add Need is called, return null simiulating a conflict
        when(mockBasketDAO.addNeed(basket.getId(), need, 1)).thenReturn(null);

        // Invoke
        ResponseEntity<FundingBasket> response = basketController.addNeed(basket.getId(), need, 1);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
        
    }

    @Test
    public void testAddNeedHandleExeption() throws IOException { 
        // Setup
        FundingBasket basket = new FundingBasket(2, "username");
        int need = 0;
        // adds a need to the basket
        basket.addNeed(need, 1);
        
        // When add Need is called on the Mock Basket DAO, throw an IOException
        doThrow(new IOException()).when(mockBasketDAO).addNeed(basket.getId(), need, 1);
        when(mockBasketDAO.getQuantity(basket.getId(), need)).thenReturn(-1);

        // Invoke
        ResponseEntity<FundingBasket> response = basketController.addNeed(basket.getId(), need, 1);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        
    }

    

    @Test
    public void testGetNeeds() throws IOException { 
        
        // Setup
        FundingBasket basket = new FundingBasket(0, "username");
        HashMap<Integer, Integer>  needs = new HashMap<>();
        // adds a needs to the basket
        basket.addNeed(0, 1);
        basket.addNeed(1, 2);
        // gets the needs from the basket and puts them in the needs array
        needs.put(0, basket.getNeeds().get(0));
        needs.put(1, basket.getNeeds().get(1));
      
        // When getNeeds is called return the needs created above
        when(mockBasketDAO.getNeeds(basket.getId())).thenReturn(needs);
      
        // Invoke
        ResponseEntity<HashMap<Integer, Integer>> response = basketController.getNeeds(basket.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());
    }

    @Test
    public void testGetNeedsHandleException() throws IOException { 
        
        // Setup
        FundingBasket basket = new FundingBasket(0, "username");
        HashMap<Integer, Integer>  needs = new HashMap<>();
        basket.addNeed(0, 1);
        basket.addNeed(1, 2);
        needs.put(0, basket.getNeeds().get(0));
        needs.put(1, basket.getNeeds().get(1));
      
        // When get Need is called on the Mock Basket DAO, throw an IOException
        doThrow(new IOException()).when(mockBasketDAO).getNeeds(basket.getId());
      
        // Invoke
        ResponseEntity<HashMap<Integer, Integer>> response = basketController.getNeeds(basket.getId());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }


    @Test
    public void testDeleteBasket() throws IOException { 
        // Setup
        int basketId = 0;

        // when delete Basket is called return true, simulating successful deletion
        when(mockBasketDAO.deleteBasket(basketId)).thenReturn(true);

        // Invoke
        ResponseEntity<FundingBasket> response = basketController.deleteBasket(basketId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteBasketNotFound() throws IOException { 
        // Setup
        int basketId = 0;

        // when delete Basket is called return false, simulating failed deletion
        when(mockBasketDAO.deleteBasket(basketId)).thenReturn(false);

        // Invoke
        ResponseEntity<FundingBasket> response = basketController.deleteBasket(basketId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteBasketHandleException() throws IOException { 
        // Setup
        int basketId = 0;

        // When delete Basket is called on the Mock Basket DAO, throw an IOException
        doThrow(new IOException()).when(mockBasketDAO).deleteBasket(basketId);

        // Invoke
        ResponseEntity<FundingBasket> response = basketController.deleteBasket(basketId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }


    @Test
    public void testRemoveNeed() throws IOException { 
        // Setup
        FundingBasket basket = new FundingBasket(2, "username");
        int need = 0;
        // adds a need to the basket
        basket.addNeed(need, 1);
        // deletes that need form the basket 
        basket.deleteNeed(need);
        
        
        // when remove need is called, returns the basket
        when(mockBasketDAO.removeNeed(basket.getId(), need)).thenReturn(basket);
       

        // Invoke
        ResponseEntity<FundingBasket> response = basketController.removeNeed(basket.getId(), need);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        
    }

    @Test
    public void testRemoveNeedNotFound() throws IOException { 
        // Setup
        FundingBasket basket = new FundingBasket(2, "username");
        int need = 0;
       // adds a need to the basket
       basket.addNeed(need, 1);
       // deletes that need from the basket 
       basket.deleteNeed(need);
       
        
       // when remove need is called, returns null simulating that the need is not found
        when(mockBasketDAO.removeNeed(basket.getId(), need)).thenReturn(null);

        // Invoke
        ResponseEntity<FundingBasket> response = basketController.removeNeed(basket.getId(), need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        
    }


    @Test
    public void testRemoveNeedHandleException() throws IOException { 
        // Setup
        FundingBasket basket = new FundingBasket(2, "username");
        int need = 0;
       // adds a need to the basket
       basket.addNeed(need,1);
       // deletes that need form the basket 
       basket.deleteNeed(need);
       
        
       // When remove need is called on the Mock Basket DAO, throw an IOException
       doThrow(new IOException()).when(mockBasketDAO).removeNeed(basket.getId(), need);

        // Invoke
        ResponseEntity<FundingBasket> response = basketController.removeNeed(basket.getId(), need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        
    }

    @Test
    public void testClearBasket() throws IOException { 
        // Setup
        FundingBasket basket = new FundingBasket(2, "username");
        int need = 0;
       // adds a need to the basket
       basket.addNeed(need,1);
       // clears all needs from the basket 
       basket.clearNeeds();
        
        // when clear basket is called, returns the basket 
        when(mockBasketDAO.clearBasket(basket.getId())).thenReturn(basket);
    
        // Invoke
        ResponseEntity<FundingBasket> response = basketController.clearBasket(basket.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    
    }

    @Test
    public void testClearBasketHandleException() throws IOException { 
        // Setup
        FundingBasket basket = new FundingBasket(2, "username");
        int need = 0;
       // adds a need to the basket
       basket.addNeed(need,1);
       // clears all needs from the basket 
       basket.clearNeeds();
        
        // When clear basket is called on the Mock Basket DAO, throw an IOException
        doThrow(new IOException()).when(mockBasketDAO).clearBasket(basket.getId());
    
        // Invoke
        ResponseEntity<FundingBasket> response = basketController.clearBasket(basket.getId());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    
    }

     @Test
    public void testClearBasketConflict() throws IOException { 
        // Setup
        FundingBasket basket = new FundingBasket(2, "username");
        int need = 0;
       // adds a need to the basket
       basket.addNeed(need,1);
       
        
        // When clear basket is called returns a basket that is not empty to simulat a conflict
        when(mockBasketDAO.clearBasket(basket.getId())).thenReturn(basket);
    
        // Invoke
        ResponseEntity<FundingBasket> response = basketController.clearBasket(basket.getId());

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    
    }

    @Test
    public void testGetUsername() throws IOException {
        // Setup
        FundingBasket basket = new FundingBasket(2, "username");
        String username = "";
        
        // when get username is called, returns the username of the user from the basket
        when(mockBasketDAO.getUsername(basket.getId())).thenReturn(username);

        // Invoke
        ResponseEntity<String> response = basketController.getUsername(basket.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        
    }

    @Test
    public void testGetUsernameNotFound() throws IOException { 
        // Setup
        FundingBasket basket = new FundingBasket(2, "username");
         
        // when get username is called, returns null simulating that the basket is not found 
        when(mockBasketDAO.getUsername(basket.getId())).thenReturn(null);
    
        // Invoke
        ResponseEntity<String> response = basketController.getUsername(basket.getId());

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        
    }

    @Test
    public void testGetUsernameHandleException() throws IOException { 
        // Setup
        FundingBasket basket = new FundingBasket(2, "username");
      
        // When get username is called on the Mock Basket DAO, throw an IOException
        doThrow(new IOException()).when(mockBasketDAO).getUsername(basket.getId());
       
        // Invoke
        ResponseEntity<String> response = basketController.getUsername(basket.getId());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        
    }

    @Test
    public void testGetQuantity() throws IOException{
        //Setup
        FundingBasket basket = new FundingBasket(2, "username");
        int needId = 2;
        basket.addNeed(needId, 1);
        when(mockBasketDAO.getQuantity(basket.getId(), needId)).thenReturn(1);

        //Invoke
        ResponseEntity<Integer> response = basketController.getQuantity(basket.getId(), needId);

        //Analyze
        assertEquals(basket.getNeeds().get(needId), response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetQuantityNoNeed() throws IOException{
        //Setup
        FundingBasket basket = new FundingBasket(2, "username");
        int needId = 2;
        when(mockBasketDAO.getQuantity(basket.getId(), needId)).thenReturn(-1);
        
        //invoke
        ResponseEntity<Integer> response = basketController.getQuantity(basket.getId(), needId);

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testSetQuantity() throws IOException{
        //Setup
        FundingBasket basket = new FundingBasket(2, "username");
        FundingBasket basket2 = new FundingBasket(2, "username");
        int needId = 2;
        int quantity = 4;
        basket.addNeed(needId, 1);
        basket2.addNeed(needId, quantity);
        when(mockBasketDAO.setQuantity(basket.getId(), needId, quantity)).thenReturn(basket2);

        // Invoke
        ResponseEntity<FundingBasket> response = basketController.setQuantity(basket.getId(), needId, quantity);

        //Analyze
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), basket2);
    }

    @Test
    public void testSetQuantityNoNeed() throws IOException{
        //Setup
        FundingBasket basket = new FundingBasket(2, "username");
        int needId = 2;
        int quantity = 4;
        when(mockBasketDAO.setQuantity(basket.getId(), needId, quantity)).thenReturn(null);
        when(mockBasketDAO.getQuantity(basket.getId(), needId)).thenReturn(-1);

        // Invoke
        ResponseEntity<FundingBasket> response = basketController.setQuantity(basket.getId(), needId, quantity);

        //Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testSetQuantityServerError() throws IOException{
        //Setup
        FundingBasket basket = new FundingBasket(2, "username");
        doThrow(new IOException()).when(mockBasketDAO).setQuantity(basket.getId(), 1, 1);

        //Invoke
        ResponseEntity<FundingBasket> response = basketController.setQuantity(basket.getId(), 1, 1);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
