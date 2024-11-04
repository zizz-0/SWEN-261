package com.needs.api.needsapi.model;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
/**
 * The unit test suite for the Profile class
 * 
 * @author Team Swiss Pandas
 * @author Ryan Garvin
 */

@Tag("Model-tier")
public class ProfileTest {

    @Test
    public void testFirstName() {
        // Setup
        String fristName = "Dulce";
        String lastName = "Garvin";
        String address = "New York";
        String email = "ryangarvin19@gmail.com";
        String userName = "RyanG";
        HashMap<Integer,Integer> contributions = null;

        Profile profile = new Profile(fristName, lastName, email, address, userName, contributions);

        String expectedFristName = "Ryan";

        // Invoke
        profile.setFirstName(expectedFristName);

        // Analyze
        
        assertEquals(expectedFristName, profile.getFirstName());
    }

    @Test
    public void testLastName() {
        // Setup
        String fristName = "Ryan";
        String lastName = "Dulce";
        String address = "New York";
        String email = "ryangarvin19@gmail.com";
        String userName = "RyanG";
        HashMap<Integer,Integer> contributions = null;

        Profile profile = new Profile(fristName, lastName, email, address, userName, contributions);

        String expectedLastName = "Garvin";

        // Invoke
        profile.setLastName(expectedLastName);

        // Analyze
        
        assertEquals(expectedLastName, profile.getLastName());
    }

    @Test
    public void testEmail() {
        // Setup
        String fristName = "Dulce";
        String lastName = "Garvin";
        String address = "New York";
        String email = "ryangarvin120@gmail.com";
        String userName = "RyanG";
        HashMap<Integer,Integer> contributions = null;

        Profile profile = new Profile(fristName, lastName, email, address, userName, contributions);

        String expectedEmail = "ryangarvin19@gmail.com";

        // Invoke
        profile.setEmail(expectedEmail);

        // Analyze
        
        assertEquals(expectedEmail, profile.getEmail());
    }


    @Test
    public void testAddress() {
        // Setup
        String fristName = "Dulce";
        String lastName = "Garvin";
        String country = "New Jersey";
        String email = "ryangarvin19@gmail.com";
        String userName = "RyanG";
        HashMap<Integer,Integer> contributions = null;

        Profile profile = new Profile(fristName, lastName, email, country, userName, contributions);

        String expectedCountry = "New York";

        // Invoke
        profile.setCountry(expectedCountry);

        // Analyze
        
        assertEquals(expectedCountry, profile.getCountry());
    }



    @Test
    public void testUserName() {
        // Setup
        String fristName = "Dulce";
        String lastName = "Garvin";
        String address = "New York";
        String email = "ryangarvin19@gmail.com";
        String userName = "RyanG";
        HashMap<Integer,Integer> contributions = null;

        Profile profile = new Profile(fristName, lastName, email, address, userName, contributions);

        String expectedUserName = "RyanG";

    
        // Analyze
        
        assertEquals(expectedUserName, profile.getUserName());
    }

    @Test
    public void testToString() {
        // Setup
        String fristName = "Dulce";
        String lastName = "Garvin";
        String address = "New York";
        String email = "ryangarvin19@gmail.com";
        String userName = "RyanG";
        boolean isPrivate = false;
        HashMap<Integer,Integer> contributions = null;
       
        Profile profile = new Profile(fristName, lastName, email, address, userName, contributions);
        String expected_string = profile.getUserName() + "'s " + String.format(Profile.STRING_FORMAT,fristName, lastName, email, address, isPrivate);

        // Invoke
        String actual_string = profile.toString();

        // Analyze
        assertEquals(expected_string, actual_string);
    }

    @Test
    public void testPrivacy() {
        // Setup
        String fristName = "Dulce";
        String lastName = "Garvin";
        String address = "New York";
        String email = "ryangarvin19@gmail.com";
        String userName = "RyanG";
        HashMap<Integer,Integer> contributions = null;

        Profile profile = new Profile(fristName, lastName, email, address, userName, contributions);

        boolean expected = true;

        profile.switchPrivacy();
        // Analyze
        
        assertEquals(expected, profile.getIsPrivate());
    }

    @Test
    public void testPrivacyMutipleSwitches() {
        // Setup
        String fristName = "Dulce";
        String lastName = "Garvin";
        String address = "New York";
        String email = "ryangarvin19@gmail.com";
        String userName = "RyanG";
        HashMap<Integer,Integer> contributions = null;

        Profile profile = new Profile(fristName, lastName, email, address, userName, contributions);

        boolean expected = false;

        profile.switchPrivacy();
        profile.switchPrivacy();
        // Analyze
        
        assertEquals(expected, profile.getIsPrivate());
    }
    
    @Test
    public void testAddGetContributions(){
        // Setup
        String firstName = "Dulce";
        String lastName = "Garvin";
        String address = "New York";
        String email = "ryangarvin19@gmail.com";
        String userName = "RyanG";
        HashMap<Integer,Integer> contributions = new HashMap<>();
        contributions.put(3, 1);

        int needId = 1;
        int quantity = 2;

        //Invoke
        Profile profile = new Profile(firstName, lastName, email, address, userName, contributions);
        profile.addContribution(needId, quantity);
        

        //Analyze
        // assertEquals(true, contributions.containsKey(needId));
        // assertEquals(quantity, contributions.get(needId));

        //test adding more quantity
        profile.addContribution(needId, quantity);
        contributions = profile.getContributions();
        //Analyze
        assertEquals(quantity*2, contributions.get(needId));
    }
}
