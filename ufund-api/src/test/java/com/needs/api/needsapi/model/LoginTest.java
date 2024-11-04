package com.needs.api.needsapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Login class
 * 
 * @author Veronika Kirievskaya
 */
@Tag("Model-tier")
public class LoginTest {
    @Test
    public void testCtor() {
        // Setup
        String expected_username = "helper1";
        String expected_password = "helper1";
        int expected_basketId = 1;
        // Invoke
        Login login = new Login(expected_username, expected_password, expected_basketId);

        // Analyze
        assertEquals(expected_username, login.getUserName());
        assertEquals(expected_password, login.getPass());
        assertEquals(expected_basketId, login.getBasketId());
    }

    @Test
    public void testToString() {
        // Setup
        String expected_username = "helper1";
        String expected_password = "helper1";
        String expected_string = String.format(Login.STRING_FORMAT, expected_username, expected_password);
        Login login = new Login(expected_username, expected_password, 1);

        // Invoke
        String actual_string = login.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }

    @Test
    public void testsetName() {
        // Setup
        String first_username = "helper1";
        String expected_password = "helper1";
        String expected_username = "helper2";
        Login login = new Login(first_username, expected_password, 1);

        // Invoke
        login.setName(expected_username);

        // Analyze
        assertEquals(expected_username, login.getUserName());
    }
}