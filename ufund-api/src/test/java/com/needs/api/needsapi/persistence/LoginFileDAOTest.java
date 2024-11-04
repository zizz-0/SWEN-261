package com.needs.api.needsapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.needs.api.needsapi.model.FundingBasket;
import com.needs.api.needsapi.model.Login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Login File DAO class
 * 
 * @author Team Swiss Pandas
 * @author Oscar Li
 */
@Tag("Persistence-tier")
public class LoginFileDAOTest {
    LoginFileDAO loginFileDAO;
    Login[] testLogins;
    ObjectMapper mockObjectMapper;
    BasketFileDAO basketFileDAO;
    FundingBasket[] testBaskets;
    NeedDAO mockNeedDAO;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupLoginFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testLogins = new Login[2];
        testLogins[0] = new Login("admin1", "admin1", 0);
        testLogins[1] = new Login("helper1", "helper1", 1);

        mockNeedDAO = mock(NeedDAO.class);
        testBaskets = new FundingBasket[2];
        testBaskets[0] = new FundingBasket(0, "admin1");
        testBaskets[1] = new FundingBasket(1, "helper1");
       

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the need array above
        when(mockObjectMapper
            .readValue(new File("data/baskets.json"),FundingBasket[].class))
                .thenReturn(testBaskets);
        basketFileDAO = new BasketFileDAO("data/baskets.json",mockObjectMapper, mockNeedDAO);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the login array above
        when(mockObjectMapper
            .readValue(new File("data/logins.json"),Login[].class))
                .thenReturn(testLogins);
        loginFileDAO = new LoginFileDAO("data/logins.json",mockObjectMapper, basketFileDAO);
    }

    @Test
    public void testGetLogins() {
        // Invoke
        Login[] logins = loginFileDAO.getLogins();

        // Analyze
        assertEquals(logins.length, testLogins.length);
        for (int i = 0; i < testLogins.length;++i)
            assertEquals(logins[i],testLogins[i]);
    }

    @Test
    public void testFindLogins() {
        // Invoke
        Login[] logins = loginFileDAO.findLogins("helper1");

        // Analyze
        assertEquals(logins.length, 1);
        assertEquals(logins[0],testLogins[1]);
    }

    @Test
    public void testGetLogin() {
        // Invoke
        Login login = loginFileDAO.getLogin("admin1");

        // Analzye
        assertEquals(login,testLogins[0]);
    }

    @Test
    public void testDeleteLogin() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> loginFileDAO.deleteLogin("helper1"),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test logins array - 1 (because of the delete)
        // Because logins attribute of LoginFileDAO is package private
        // we can access it directly
        assertEquals(loginFileDAO.logins.size(),testLogins.length-1);
    }

    @Test
    public void testCreateLogin() {
        // Setup
        Login login = new Login("helper2","helper2", 1);

        // Invoke
        Login result = assertDoesNotThrow(() -> loginFileDAO.createLogin(login),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Login actual = loginFileDAO.getLogin(login.getUserName());
        assertEquals(actual.getUserName(),login.getUserName());
        assertEquals(actual.getPass(),login.getPass());
    }

    @Test
    public void testCreateLoginConflict() {
        // Setup
        Login login = new Login("helper1", "password", 3);

        // tests that if a username already exists, a new login cannot be created with that same username

        // Invoke
        Login result = assertDoesNotThrow(() -> loginFileDAO.createLogin(login),
                                "Unexpected exception thrown");

        // Analyze
        assertEquals(null, result);
    }

    @Test
    public void testUpdateLogin() {
        // Setup
        Login login = new Login("admin2", "admin1", 1);


        // Invoke
        Login result = assertDoesNotThrow(() -> loginFileDAO.updateLogin("admin1", login),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Login actual = loginFileDAO.getLogin(login.getUserName());
        Login actualNull = loginFileDAO.getLogin("admin1");
        assertEquals(actual,login);
        assertEquals(actualNull,null);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Login[].class));

        Login login = new Login("helper3", "helper3", 1);

        assertThrows(IOException.class,
                        () -> loginFileDAO.createLogin(login),
                        "IOException not thrown");
    }

    @Test
    public void testGetLoginNotFound() {
        // Invoke
        Login login = loginFileDAO.getLogin("helper100");

        // Analyze
        assertEquals(login,null);
    }

    @Test
    public void testDeleteLoginNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> loginFileDAO.deleteLogin("helper100"),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(loginFileDAO.logins.size(),testLogins.length);
    }

    @Test
    public void testUpdateLoginNotFound() {
        // Setup
        Login login = new Login("helper200", "helper200", 1);

        // Invoke
        Login result = assertDoesNotThrow(() -> loginFileDAO.updateLogin("helper200", login),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the LoginFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("data/logins.json"),Login[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new LoginFileDAO("data/logins.json",mockObjectMapper, basketFileDAO),
                        "IOException not thrown");
    }

    @Test
    public void testSetBasketId() {
        // Setup
        Login login = new Login("admin1", "admin1", 1);


        // Invoke
        Login result = assertDoesNotThrow(() -> loginFileDAO.setBasketId(login.getUserName(), 2),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Login actual = loginFileDAO.getLogin(login.getUserName());
        assertEquals(actual,result);
    }

    @Test
    public void testSetBasketIdFailed() {
        // Setup
        //Login login = new Login("admin1", "admin1", 1);


        // Invoke
        Login result = assertDoesNotThrow(() -> loginFileDAO.setBasketId("user", 0),
                                "Unexpected exception thrown");

        // Analyze
        assertEquals(null,result);
    }

    @Test
    public void testGetBasketId() {
        // Setup
        Login login = new Login("helper1", "helper1", 1);


        // Invoke
        int result = assertDoesNotThrow(() -> loginFileDAO.getBasketId(login.getUserName()),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        int actual = login.getBasketId();
        assertEquals(actual,result);
    }

    @Test
    public void testGetBasketIdFailed() {
        // Setup
        //Login login = new Login("admin1", "admin1", 1);


        // Invoke
        int result = assertDoesNotThrow(() -> loginFileDAO.getBasketId("login"),
                                "Unexpected exception thrown");

        // Analyze
        assertEquals(0,result);
    }
}
