package com.needs.api.needsapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the FUnding Basket class
 * 
 * @author Team Swiss Pandas
 * @author SWEN Faculty
 */
@Tag("Model-tier")
public class FundingBasketTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 1;
        String expected_username = "tom";
        // Invoke
        FundingBasket fundingBasket = new FundingBasket(expected_id,expected_username);

        // Analyze
        assertEquals(expected_id,fundingBasket.getId());
        assertEquals(expected_username,fundingBasket.getUsername());
    }

    @Test
    public void testAddNeeds() {
        // Setup
        int expected_id = 1;
        String expected_username = "tom";
        FundingBasket fundingBasket = new FundingBasket(expected_id,expected_username);
        int needId = 2;
        // Invoke
        fundingBasket.addNeed(needId,1);
        // Analyze
        assertNotNull(fundingBasket.getNeeds().get(needId));
        assertEquals(fundingBasket.getNeeds().size(), 1);
    }
    @Test
    public void testRemoveNeeds() {
        // Setup
        int expected_id = 1;
        String expected_username = "tom";
        FundingBasket fundingBasket = new FundingBasket(expected_id,expected_username);
        int needId = 2;
        int needId2 = 4;
        fundingBasket.addNeed(needId,1);
        fundingBasket.addNeed(needId2,2);
        // Invoke
        fundingBasket.deleteNeed(needId);
        // Analyze
        assertEquals(fundingBasket.getNeeds().get(needId), null);
        assertNotNull(fundingBasket.getNeeds().get(needId2));
        assertFalse(fundingBasket.deleteNeed(5));
        assertEquals(fundingBasket.getNeeds().size(), 1);
    }
    @Test
    public void testClearNeeds() {
        // Setup
        int expected_id = 1;
        String expected_username = "tom";
        FundingBasket fundingBasket = new FundingBasket(expected_id,expected_username);
        int needId = 2;
        int needId2 = 4;
        fundingBasket.addNeed(needId,1);
        fundingBasket.addNeed(needId2,2);
        // Invoke
        fundingBasket.clearNeeds();
        // Analyze
        assertEquals(fundingBasket.getNeeds().size(), 0);
    }

    @Test
    public void testToString() {
        FundingBasket fundingBasket = new FundingBasket(1,"tom");
        fundingBasket.addNeed(1,2);
        String expectedString = "Funding Basket [id: 1, user: tom],\nNeed [id=1, quantity=2]";
        assertEquals(fundingBasket.toString(), expectedString);
    }

    @Test
    public void testSetQuantity(){
        //Setup
        FundingBasket fundingBasket = new FundingBasket(1,"tom");
        fundingBasket.addNeed(1,2);
        //Invoke
        fundingBasket.setQuantity(1, 1);
        //Analyze
        assertEquals(1, fundingBasket.getNeeds().get(1));
    }
    @Test
    public void testAddQuantity(){
        //Setup
        FundingBasket fundingBasket = new FundingBasket(1,"tom");
        fundingBasket.addNeed(1,2);
        //Invoke
        fundingBasket.addQuantity(1, 1);
        //Analyze
        assertEquals(3, fundingBasket.getNeeds().get(1));
    }
}