package com.needs.api.needsapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.lenient;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Need class
 * 
 * @author Team Swiss Pandas
 * @author SWEN Faculty
 */
@Tag("Model-tier")
public class NeedTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        String expected_name = "syringes";
        NeedType expected_type = NeedType.EQUIPMENT;
        double expected_price = 50.0;
        int expected_quantityNeeded = 10;
        int expected_quantityFulfilled = 0;
        UrgencyTag expected_urgency = UrgencyTag.LOW;
        String expected_description = "some description";
        String expected_image = "image";
        // Invoke
        Need need = new Need(expected_id,expected_name,expected_type,expected_price,expected_quantityNeeded,expected_quantityFulfilled, expected_urgency, expected_description, expected_image);

        // Analyze
        assertEquals(expected_id,need.getId());
        assertEquals(expected_name,need.getName());
        assertEquals(expected_type,need.getType());
        assertEquals(expected_price,need.getPrice());
        assertEquals(expected_quantityNeeded,need.getQuantityNeeded());
        assertEquals(expected_quantityFulfilled,need.getQuantityFulfilled());
        assertEquals(expected_description,need.getDescription());
        assertEquals(expected_image, need.getImage());
    }

    @Test
    public void testName() {
        // Setup
        int id = 99;
        String name = "milk formula";
        NeedType type = NeedType.EQUIPMENT;
        double price = 100.0;
        Need need = new Need(id,name,type,price,1,0, UrgencyTag.LOW, "some description", "image");

        String expected_name = "syringes";

        // Invoke
        need.setName(expected_name);

        // Analyze
        assertEquals(expected_name,need.getName());
    }

    @Test
    public void testPrice() {
        //Setup
        int id = 99;
        String name = "syringes";
        NeedType type = NeedType.EQUIPMENT;
        double price = 100.0;
        Need need = new Need(id, name, type, price,1,0, UrgencyTag.LOW, "some description", "image");

        double expected_price = 125.0;

        //Invoke
        need.setPrice(expected_price);

        //Analyze
        assertEquals(expected_price,need.getPrice());
    }

    @Test
    public void testQuantityNeeded(){
        //Setup
        int id = 99;
        String name = "syringes";
        NeedType type = NeedType.EQUIPMENT;
        double price = 100.0;
        Need need = new Need(id, name, type, price,1,0, UrgencyTag.LOW, "some description", "image");

        int expected_quantity = 10;

        //Invoke
        need.setQuantityNeeded(expected_quantity);

        //Analyze
        assertEquals(expected_quantity,need.getQuantityNeeded());
    }

    @Test
    public void testQuantityFulfilled(){
        //Setup
        int id = 99;
        String name = "syringes";
        NeedType type = NeedType.EQUIPMENT;
        double price = 100.0;
        Need need = new Need(id, name, type, price,10,0, UrgencyTag.LOW, "some description", "image");

        int expected_quantityFulfilled = 5;

        //Invoke
        need.setQuantityFulfilled(expected_quantityFulfilled);

        //Analyze
        assertEquals(expected_quantityFulfilled,need.getQuantityFulfilled());
    }

    @Test
    public void testDescription(){
        //Setup
        int id = 99;
        String name = "syringes";
        NeedType type = NeedType.EQUIPMENT;
        double price = 100.0;
        String description = "some description";
        String image = "image";
        Need need = new Need(id, name, type, price,10,0, UrgencyTag.LOW, description, image);

        String expected_description = "updated description";

        //Invoke
        need.setDescription(expected_description);

        //Analyze
        assertEquals(expected_description,need.getDescription());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String name = "syringes";
        NeedType type = NeedType.EQUIPMENT;
        double price = 50.0;
        String expected_string = String.format(Need.STRING_FORMAT,id,name,price,1,0, "some description");
        Need need = new Need(id,name,type,price,1,0, UrgencyTag.LOW, "some description","image");

        // Invoke
        String actual_string = need.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
}