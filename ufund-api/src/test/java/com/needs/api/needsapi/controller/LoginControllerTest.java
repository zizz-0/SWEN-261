package com.needs.api.needsapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.needs.api.needsapi.persistence.LoginDAO;
import com.needs.api.needsapi.model.Login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Login Controller class
 * 
 * @author Veronika Kirievskaya, Oscar Li, Zoe Rizzo
 */
@Tag("Controller-tier")
public class LoginControllerTest {
    private LoginController loginController;
    private LoginDAO mockLoginDAO;

    /**
     * Before each test, create a new LoginController object and inject
     * a mock Login DAO
     */
    @BeforeEach
    public void setupLoginController() {
        mockLoginDAO = mock(LoginDAO.class);
        loginController = new LoginController(mockLoginDAO);
    }

    @Test
    public void testGetLogin() throws IOException {  // getLogin may throw IOException
        // Setup
        Login login = new Login("helper1", "helper1", 1);
        // When the same id is passed in, our mock Login DAO will return the Login object
        when(mockLoginDAO.getLogin(login.getUserName())).thenReturn(login);

        // Invoke
        ResponseEntity<Login> response = loginController.getLogin(login.getUserName());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(login,response.getBody());
    }

    @Test
    public void testGetLoginNotFound() throws Exception { // createLogin may throw IOException
        // Setup
        String username = "helper1";
        // When the same id is passed in, our mock Login DAO will return null, simulating
        // no login found
        when(mockLoginDAO.getLogin(username)).thenReturn(null);

        // Invoke
        ResponseEntity<Login> response = loginController.getLogin(username);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetLoginHandleException() throws Exception { // createLogin may throw IOException
        // Setup
        String username = "helper1";
        // When getLogin is called on the Mock Login DAO, throw an IOException
        doThrow(new IOException()).when(mockLoginDAO).getLogin(username);

        // Invoke
        ResponseEntity<Login> response = loginController.getLogin(username);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all LoginController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateLogin() throws IOException {  // createLogin may throw IOException
        // Setup
        Login login = new Login("helper1", "helper1", 1);
        // when createLogin is called, return true simulating successful
        // creation and save
        when(mockLoginDAO.createLogin(login)).thenReturn(login);

        // Invoke
        ResponseEntity<Login> response = loginController.createLogin(login);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(login,response.getBody());
    }

    @Test
    public void testCreateLoginFailed() throws IOException {  // createLogin may throw IOException
        // Setup
        Login login = new Login("helper1", "helper1", 1);
        // when createLogin is called, return false simulating failed
        // creation and save
        when(mockLoginDAO.createLogin(login)).thenReturn(null);

        // Invoke
        ResponseEntity<Login> response = loginController.createLogin(login);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateLoginHandleException() throws IOException {  // createLogin may throw IOException
        // Setup
        Login login = new Login("helper1", "helper1", 1);

        // When createLogin is called on the Mock Login DAO, throw an IOException
        doThrow(new IOException()).when(mockLoginDAO).createLogin(login);

        // Invoke
        ResponseEntity<Login> response = loginController.createLogin(login);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetLogins() throws IOException { // getLogins may throw IOException
        // Setup
        Login[] logins = new Login[2];
        logins[0] = new Login("helper1", "helper1", 1);
        logins[1] = new Login("helper2", "helper2", 1);
        // When getLogins is called return the logins created above
        when(mockLoginDAO.getLogins()).thenReturn(logins);

        // Invoke
        ResponseEntity<Login[]> response = loginController.getLogins();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(logins,response.getBody());
    }

    @Test
    public void testGetLoginsHandleException() throws IOException { // getLogins may throw IOException
        // Setup
        // When getLogins is called on the Mock Login DAO, throw an IOException
        doThrow(new IOException()).when(mockLoginDAO).getLogins();

        // Invoke
        ResponseEntity<Login[]> response = loginController.getLogins();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchLogins() throws IOException { // findLogins may throw IOException
        // Setup
        String searchString = "in";
        Login[] logins = new Login[2];
        logins[0] = new Login("helper1", "helper1", 1);
        logins[1] = new Login("helper2", "helper2", 1);
        // When findLogins is called with the search string, return the two
        /// logins above
        when(mockLoginDAO.findLogins(searchString)).thenReturn(logins);

        // Invoke
        ResponseEntity<Login[]> response = loginController.searchLogins(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(logins,response.getBody());
    }

    @Test
    public void testSearchLoginsNotFound() throws IOException { // findLogins may throw IOException
        // Setup
        String searchString = "z";
        // when a searchString is null return false, simulating no login found
        // or when no login is found from searchString return false, simulating no login found
        when(mockLoginDAO.findLogins(searchString)).thenReturn(null);

        // Invoke
        ResponseEntity<Login[]> response1 = loginController.searchLogins(searchString);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response1.getStatusCode());
    }

    @Test
    public void testSearchLoginsHandleException() throws IOException { // findLogins may throw IOException
        // Setup
        String searchString = "an";
        // When createLogin is called on the Mock Login DAO, throw an IOException
        doThrow(new IOException()).when(mockLoginDAO).findLogins(searchString);

        // Invoke
        ResponseEntity<Login[]> response = loginController.searchLogins(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteLogin() throws IOException { // deleteLogin may throw IOException
        // Setup
        String username = "helper";
        // when deleteLogin is called return true, simulating successful deletion
        when(mockLoginDAO.deleteLogin(username)).thenReturn(true);

        // Invoke
        ResponseEntity<Login> response = loginController.deleteLogin(username);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteLoginNotFound() throws IOException { // deleteLogin may throw IOException
        // Setup
        String username = "helper";
        // when deleteLogin is called return false, simulating failed deletion
        when(mockLoginDAO.deleteLogin(username)).thenReturn(false);

        // Invoke
        ResponseEntity<Login> response = loginController.deleteLogin(username);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }


    @Test
    public void testDeleteLoginHandleException() throws IOException { // deleteLogin may throw IOException
        // Setup
        String username = "helper";
        // When deleteLogin is called on the Mock Login DAO, throw an IOException
        doThrow(new IOException()).when(mockLoginDAO).deleteLogin(username);

        // Invoke
        ResponseEntity<Login> response = loginController.deleteLogin(username);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateLogin() throws IOException { // updateLogin may throw IOException
        // Setup
        Login login = new Login("helper1", "helper1", 1);
        // when updateLogin is called, return true simulating successful
        // update and save
        when(mockLoginDAO.updateLogin("helper1", login)).thenReturn(login);
        ResponseEntity<Login> response = loginController.updateLogin( "helper1", login);
        login.setName("helper2");

        // Invoke
        response = loginController.updateLogin("helper1", login);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(login,response.getBody());
    }

    @Test
    public void testUpdateLoginFailed() throws IOException { // updateLogin may throw IOException
        // Setup
        Login login = new Login("admin1", "admin1", 1);
        // when updateLogin is called, return true simulating successful
        // update and save
        when(mockLoginDAO.updateLogin("admin1", login)).thenReturn(null);

        // Invoke
        ResponseEntity<Login> response = loginController.updateLogin("admin1", login);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateLoginHandleException() throws IOException { // updateLogin may throw IOException
        // Setup
        Login login = new Login("helper1", "helper1", 1);
        // When updateLogin is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockLoginDAO).updateLogin("admin1", login);

        // Invoke
        ResponseEntity<Login> response = loginController.updateLogin("admin1", login);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testVerifyLogin() throws IOException {  // createLogin may throw IOException
        // Setup
        Login login = new Login("helper1", "helper1", 1);
        // when verifyLogin is called, return true simulating successful
        // creation and save
        when(mockLoginDAO.getLogin(login.getUserName())).thenReturn(login);

        // Invoke
        ResponseEntity<Boolean> response = loginController.verifyLogin(login.getUserName(), login.getPass());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(true,response.getBody());
    }

    @Test
    public void testVerifyLoginFail() throws IOException {  // createLogin may throw IOException
        // Setup
        Login login1 = new Login("helper1", "helper1", 1);
        Login login2 = new Login("helper2", "helper2", 2);
        // when verifyLogin is called, return false simulating failure
        // creation and save
        when(mockLoginDAO.createLogin(login1)).thenReturn(null);
        when(mockLoginDAO.createLogin(login2)).thenReturn(login2);

        // Invoke
        ResponseEntity<Boolean> response1 = loginController.verifyLogin(login1.getUserName(), login1.getPass());
        ResponseEntity<Boolean> response2 = loginController.verifyLogin(login2.getUserName(), null);

        // Analyze
        assertEquals(HttpStatus.OK,response1.getStatusCode());
        assertEquals(false,response1.getBody());

        assertEquals(HttpStatus.OK,response2.getStatusCode());
        assertEquals(false,response2.getBody());
    }

    @Test
    public void testVerifyLoginWrongPassword() throws IOException {  // createLogin may throw IOException
        // Setup
        Login login = new Login("helper1", "helper1", 1);
        // when verifyLogin is called, return true simulating successful
        // creation and save
        when(mockLoginDAO.getLogin(login.getUserName())).thenReturn(login);

        // Invoke
        ResponseEntity<Boolean> response = loginController.verifyLogin(login.getUserName(), "incorrect");

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(false,response.getBody());
    }

    @Test
    public void testVerifyLoginHandleException() throws IOException { // updateLogin may throw IOException
        // Setup
        Login login = new Login("helper1", "helper1", 1);
        // When verifyLogin is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockLoginDAO).getLogin(login.getUserName());

        // Invoke
        ResponseEntity<Boolean> response = loginController.verifyLogin(login.getUserName(), login.getPass());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateBasketId() throws IOException {  // createLogin may throw IOException
        // Setup
        Login login = new Login("helper1", "helper1", 1);
        // when verifyLogin is called, return true simulating successful
        // creation and save
        when(mockLoginDAO.setBasketId(login.getUserName(), login.getBasketId())).thenReturn(login);

        // Invoke
        ResponseEntity<Login> response = loginController.updateBasketId(login.getUserName(), login.getBasketId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(login,response.getBody());
    }

    @Test
    public void testUpdateBasketIdNotFound() throws IOException {  // createLogin may throw IOException
        // Setup
        Login login = new Login("helper1", "helper1", 1);
        // when verifyLogin is called, return true simulating successful
        // creation and save
        when(mockLoginDAO.setBasketId(login.getUserName(), login.getBasketId())).thenReturn(null);

        // Invoke
        ResponseEntity<Login> response = loginController.updateBasketId(login.getUserName(), login.getBasketId());

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateBasketIdHandleException() throws IOException { // updateLogin may throw IOException
        // Setup
        Login login = new Login("helper1", "helper1", 1);
        // When verifyLogin is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockLoginDAO).setBasketId(login.getUserName(), login.getBasketId());

        // Invoke
        ResponseEntity<Login> response = loginController.updateBasketId(login.getUserName(), login.getBasketId());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetBasketId() throws IOException {  // createLogin may throw IOException
        // Setup
        Login login = new Login("helper1", "helper1", 1);
        // when verifyLogin is called, return true simulating successful
        // creation and save
        when(mockLoginDAO.getBasketId(login.getUserName())).thenReturn(login.getBasketId());

        // Invoke
        ResponseEntity<Integer> response = loginController.getBasketId(login.getUserName());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(Integer.valueOf(login.getBasketId()),response.getBody());
    }

    @Test
    public void testGetBasketIdNotFound() throws IOException {  // createLogin may throw IOException
        // Setup
        Login login = new Login("helper1", "helper1", 1);
        // when verifyLogin is called, return true simulating successful
        // creation and save
        when(mockLoginDAO.getBasketId(login.getUserName())).thenReturn(0);

        // Invoke
        ResponseEntity<Integer> response = loginController.getBasketId(login.getUserName());

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }
}
