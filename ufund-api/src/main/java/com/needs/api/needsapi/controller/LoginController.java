package com.needs.api.needsapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.needs.api.needsapi.persistence.LoginDAO;
import com.needs.api.needsapi.model.Login;

/**
 * Handles the REST API requests for the Login resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Team Swiss Pandas
 * @author SWEN Faculty
 */

@RestController
@RequestMapping("logins")
public class LoginController {
    private static final Logger LOG = Logger.getLogger(LoginController.class.getName());
    private LoginDAO loginDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param loginDao The {@link LoginDAO Login Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public LoginController(LoginDAO loginDao) {
        this.loginDao = loginDao;
    }

    /**
     * Responds to the GET request for a {@linkplain Login login} for the given username
     * 
     * @param username The username used to locate the {@link Login login}
     * 
     * @return ResponseEntity with {@link Login login} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{userName}")
    public ResponseEntity<Login> getLogin(@PathVariable String userName) {
        LOG.info("GET /logins/" + userName);
        try {
            Login login = loginDao.getLogin(userName);
            if (login != null)
                return new ResponseEntity<Login>(login,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Login logins}
     * 
     * @return ResponseEntity with array of {@link Login login} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Login[]> getLogins() {
        LOG.info("GET /logins");
        try {
            Login[] login = loginDao.getLogins();
            return new ResponseEntity<Login[]>(login,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Login logins} whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link Login logins}
     * 
     * @return ResponseEntity with array of {@link Login login} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all logins that contain the text "ma"
     * GET http://localhost:8080/logins/?name=ma
     */
    @GetMapping("/")
    public ResponseEntity<Login[]> searchLogins(@RequestParam String name) {
        LOG.info("GET /logins/?name="+name);

        try {
            Login[] login = loginDao.findLogins(name);

            if (login == null) {
                return new ResponseEntity<Login[]>(login,HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<Login[]>(login,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    /**
     * Creates a {@linkplain Login login} with the provided login object
     * 
     * @param login - The {@link Login login} to create
     * 
     * @return ResponseEntity with created {@link Login login} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Login login} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Login> createLogin(@RequestBody Login login) {
        LOG.info("POST /logins " + login);

        try {
            Login newLogin = loginDao.createLogin(login);
            if (newLogin == null){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            else
                return new ResponseEntity<Login>(newLogin, HttpStatus.CREATED);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Login login} with the provided {@linkplain Login login} object, if it exists
     * 
     * @param login The {@link Login login} to update
     * @param oldLogin The old {@link Login login} to change
     * 
     * @return ResponseEntity with updated {@link Login login} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/{userName}")
    public ResponseEntity<Login> updateLogin(@PathVariable String userName, @RequestBody Login login) {
        LOG.info("PUT /logins " + login);
        try {
            Login exist_login = loginDao.updateLogin(userName, login);
            if (exist_login != null) {
                return new ResponseEntity<Login>(exist_login, HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Login login} with the given username
     * 
     * @param username The username of the {@link Login login} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Login> deleteLogin(@PathVariable String userName) {
        LOG.info("DELETE /logins/" + userName);
        try {
            boolean deleted = loginDao.deleteLogin(userName);
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
     * Verifies a login given a username or password
     * @param userName
     * @param pass
     * @return Boolean response entity that is True if the login is successful, and false otherwise
     */
    @GetMapping("/login")
    public ResponseEntity<Boolean> verifyLogin(@RequestParam String userName, @RequestParam String pass){
        LOG.info("GET /logins/login?userName=" + userName +"&pass=" + pass);
        try{
            Login login = loginDao.getLogin(userName);
            if(login != null){
                if(login.getPass().equals(pass)){
                    LOG.info("Successful Login");
                    return new ResponseEntity<Boolean>(true, HttpStatus.OK);
                }
                return new ResponseEntity<Boolean>(false, HttpStatus.OK);
            }else{
                return new ResponseEntity<Boolean>(false, HttpStatus.OK);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates a logins basketId
     * @param userName - username of the login to change
     * @param basketId - basketId to set to
     * @return - response entitiy containing the updated Login object
     */
    @PutMapping("/basketId")
    public ResponseEntity<Login> updateBasketId(@RequestParam String userName, @RequestParam int basketId){
        LOG.info("PUT /basketId/ {userName: " + userName + ", basketId:" + basketId +"}");
        try{
            Login login = loginDao.setBasketId(userName, basketId);
            if(login != null){
                return new ResponseEntity<Login>(login, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * gets the basket id of a given login
     * @param userName
     * @return the basketId int
     */
    @GetMapping("/basketId")
    public ResponseEntity<Integer> getBasketId(@RequestParam String userName){
        LOG.info("GET /basketId/?userName=" + userName);
        int basketId = loginDao.getBasketId(userName);
        if(basketId != 0){
            LOG.info("basketId: "+basketId);
            return new ResponseEntity<Integer>(Integer.valueOf(basketId), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
