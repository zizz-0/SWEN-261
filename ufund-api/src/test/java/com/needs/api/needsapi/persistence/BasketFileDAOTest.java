package com.needs.api.needsapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.needs.api.needsapi.model.FundingBasket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Funding Basket File DAO class
 * 
 * @author Team Swiss Pandas
 * @author Jack Sutton
 */
@Tag("Persistence-tier")
public class BasketFileDAOTest {
    BasketFileDAO basketFileDAO;
    FundingBasket[] testBaskets;
    ObjectMapper mockObjectMapper;
    NeedDAO mockNeedDAO;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupNeedFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        mockNeedDAO = mock(NeedDAO.class);
        testBaskets = new FundingBasket[3];
        testBaskets[0] = new FundingBasket(0, "tom");
        testBaskets[1] = new FundingBasket(1, "mark");
        testBaskets[1].addNeed(1,1);
        testBaskets[1].addNeed(2,2);
        testBaskets[2] = new FundingBasket(2, "john");

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the need array above
        when(mockObjectMapper
            .readValue(new File("data/baskets.json"),FundingBasket[].class))
                .thenReturn(testBaskets);
        basketFileDAO = new BasketFileDAO("data/baskets.json",mockObjectMapper, mockNeedDAO);
    }

    @Test
    public void testGetBasket() {
        // Invoke
        FundingBasket basket = basketFileDAO.getBasket(1);

        // Analyze
        assertEquals(basket,testBaskets[1]);
    }

    @Test
    public void testGetFakeBasket() {
        // Invoke
        FundingBasket basket = basketFileDAO.getBasket(3);

        // Analyze
        assertNull(basket);
    }

    @Test
    public void testGetNeeds() {
        // Invoke
        int id = 1;
        HashMap<Integer, Integer> needs = basketFileDAO.getNeeds(id);

        // Analyze
        assertEquals(needs.size(),2);
        assertEquals(needs.get(1),testBaskets[id].getNeeds().get(1));
        assertEquals(needs.get(2),testBaskets[id].getNeeds().get(2));
    }

    @Test
    public void testGetNeedsFakeBasket() {
        // Invoke
        int id = 100;
        HashMap<Integer, Integer> needs = basketFileDAO.getNeeds(id);

        // Analyze
        assertNull(needs);
    }

    @Test
    public void testGetUsername() {
        // Invoke
        int id = 1;
        String username = assertDoesNotThrow(() -> basketFileDAO.getUsername(id), "Unexpected exception thrown");

        // Analzye
        assertEquals(username,testBaskets[id].getUsername());
    }

    @Test
    public void testGetUsernameFakeBasket() {
        // Invoke
        int id = 100;
        String username = assertDoesNotThrow(() -> basketFileDAO.getUsername(id), "Unexpected exception thrown");

        // Analzye
        assertNull(username);
    }

    @Test
    public void testAddNeeds() {
        // Invoke
        int basket = 1;
        int need = 3;
        int initialSize = testBaskets[basket].getNeeds().size();
        assertDoesNotThrow(() -> basketFileDAO.addNeed(basket, need, 1), "Unexpected exception thrown");

        //Analyze
        assertEquals(testBaskets[basket].getNeeds().size()-initialSize, 1);
        assertEquals(testBaskets[basket].getNeeds().get(initialSize),2);
    }

    @Test
    public void testAddNeedsFakeBasket() {
        // Invoke & Analyse
        int basket = 100;
        int need = 2;
        assertNull(assertDoesNotThrow(() -> basketFileDAO.addNeed(basket, need,1), "Unexpected exception thrown"));
    }

    @Test
    public void testRemoveNeeds() {
        // Invoke
        int basket = 1;
        int need = 2;

        FundingBasket result = assertDoesNotThrow(() -> basketFileDAO.removeNeed(basket, need), "Unexpected exception thrown");

        //Analyze
        assertEquals(result.getNeeds().size(), 1);
        assertTrue(!(result.getNeeds().containsKey(need)));
    }

    @Test
    public void testRemoveNeedsFakeNeed() {
        // Invoke
        int basket = 1;
        int need = 347;

        FundingBasket result = assertDoesNotThrow(() -> basketFileDAO.removeNeed(basket, need), "Unexpected exception thrown");

        //Analyze
        assertNull(result);
    }

    @Test
    public void testRemoveNeedsFakeBasket() {
        // Invoke
        int basket = 129;
        int need = 2;

        FundingBasket result = assertDoesNotThrow(() -> basketFileDAO.removeNeed(basket, need), "Unexpected exception thrown");

        //Analyze
        assertNull(result);
    }

    @Test
    public void testClearBasket() {
        // Invoke
        int basket = 1;
        FundingBasket result = assertDoesNotThrow(() -> basketFileDAO.clearBasket(basket), "Unexpected exception thrown");

        // Analyze
        assertEquals(result.getNeeds().size(),0);
    }

    @Test
    public void testClearFakeBasket() {
        // Invoke
        int basket = 138;
        FundingBasket result = assertDoesNotThrow(() -> basketFileDAO.clearBasket(basket), "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testCreateBasket() throws IOException{
        //Invoke
        FundingBasket result = basketFileDAO.createBasket("mike");
        
        //Analyze
        assertEquals(result.getUsername(), "mike");
    }

    @Test
    public void testDeleteBasket() {
        //invoke
        boolean result = assertDoesNotThrow(() -> basketFileDAO.deleteBasket(2), "Unexpected exception thrown");
        
        // Analyze
        assertTrue(result);
        assertEquals(basketFileDAO.baskets.size(),testBaskets.length -1);
    }

    @Test
    public void testDeleteFakeBasket() {
        //Invoke
        boolean result = assertDoesNotThrow(() -> basketFileDAO.deleteBasket(3), "Unexpected exception thrown");      

        // Analyze
        assertTrue(!result);
        assertEquals(basketFileDAO.baskets.size(),testBaskets.length);
    }

    @Test
    public void testSetQuantity() throws IOException{
        int basketId = 1;
        int needId = 1;
        int quantity = 2;
        //Invoke
        basketFileDAO.setQuantity(basketId,needId,quantity);

        //Analyze
        assertEquals(quantity, basketFileDAO.getBasket(1).getNeeds().get(needId));
    }
    @Test
    public void testSetQuantityNoNeed() throws IOException{
        int basketId = 1;
        int needId = 5;
        int quantity = 2;
        //Invoke
        basketFileDAO.setQuantity(basketId,needId,quantity);

        //Analyze
        assertEquals(null, basketFileDAO.getBasket(1).getNeeds().get(needId));
    }

    @Test
    public void testGetQuantity() throws IOException{
        int basketId = 1;
        int needId = 1;
        int expectedQuantity = 1;
        //Invoke
        int quantity = basketFileDAO.getQuantity(basketId, needId);
        //Analyze
        assertEquals(expectedQuantity, quantity);
    }

    @Test
    public void testGetQuantityNoNeed() throws IOException{
        int basketId = 1;
        int needId = 5;
        //Invoke
        int quantity = basketFileDAO.getQuantity(basketId, needId);
        //Analyze
        assertEquals(-1, quantity);
    }


    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the BasketFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("data/baskets.json"),FundingBasket[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new BasketFileDAO("data/baskets.json",mockObjectMapper, mockNeedDAO),
                        "IOException not thrown");
    }
}
