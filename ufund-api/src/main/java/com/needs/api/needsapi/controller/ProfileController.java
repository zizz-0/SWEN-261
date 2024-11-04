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

import com.needs.api.needsapi.model.Need;
import com.needs.api.needsapi.model.Profile;
import com.needs.api.needsapi.persistence.ProfileDAO;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the REST API requests for the Profile resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Team Swiss Pandas
 * @author Ryan Garvin
 */
@RestController
@RequestMapping("profiles")
public class ProfileController {
    private static final Logger LOG = Logger.getLogger(ProfileController.class.getName());
    private ProfileDAO profileDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param profileDao The {@link ProfileDAO Profile Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public ProfileController(ProfileDAO profileDao) {
        this.profileDao = profileDao;
    }


    /**
     * Responds to the GET request for a {@linkplain Profile user profile} for the given username
     * 
     * @param userName The username used to locate the {@link Profile user profile}
     * 
     * @return ResponseEntity with {@link Profile user profile} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{userName}")
    public ResponseEntity<Profile> getProfile(@PathVariable String userName) {
        LOG.info("GET /profiles/" + userName);
        try {
            Profile profile = profileDao.getProfile(userName);
            if (profile != null){
                System.out.println(profile);
                return new ResponseEntity<Profile>(profile,HttpStatus.OK);
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
     * Updates the {@linkplain Profile user profile} with the provided {@linkplain Profile user profile} object, if it exists
     * 
     * @param profile The {@link Profile user profile} to update
     * 
     * @return ResponseEntity with updated {@link Profile user profile} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Profile> updateProfile(@RequestBody Profile profile) {
        LOG.info("PUT /profiles/" + profile);
        try {
            Profile existProfile = profileDao.updateProfile(profile);
            if (existProfile != null) {
                return new ResponseEntity<Profile>(existProfile, HttpStatus.OK);
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
     * Creates a {@linkplain Profile user profile} with the provided profile object
     * 
     * @param profile - The {@link Profile user profile} to create
     * 
     * @return ResponseEntity with created {@link Profile user profile} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Profile user profile} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Profile> createProfile(@RequestBody Profile profile) {
        LOG.info("POST /profiles/" + profile);

        try {
            Profile newProfile = profileDao.createProfile(profile);
            if (newProfile != null){
                System.out.println(newProfile);
                return new ResponseEntity<Profile>(newProfile, HttpStatus.CREATED);
            }
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


   /**
     * Switches a {@linkplain Profile user profile} to be private or public 
     *
     * @param profile {@linkplain Profile user profile} to update 
     * @return ResponseEntity with updated {@link Profile user profile} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of CONFILCT if pravicy is not changed<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/privacy")
    public ResponseEntity<Profile> switchPrivacy(@RequestBody Profile profile) {
        LOG.info("PUT /profiles/privacy/" + profile);
        try { 
            if (profile != null){
                profileDao.switchPrivacy(profile);
                System.out.println(profile);
                return new ResponseEntity<Profile>(profile, HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{userName}/contribute")
    public ResponseEntity<HttpStatus> addContribution(@PathVariable String userName, 
                                            @RequestParam(name = "needId") int needId, 
                                            @RequestParam(name = "quantity") int quantity){
        LOG.info("PUT /" + userName + "?needId="+needId+"&quantity="+quantity);
        try{
            Profile profile = profileDao.getProfile(userName);
            if(profile != null){
                profile.addContribution(needId, quantity);
                profileDao.updateProfile(profile);
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Profile profile} with the given username
     * 
     * @param id The id of the {@link Profile profile} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Profile> deleteProfile(@PathVariable String userName) {
        LOG.info("DELETE /profile/" + userName);
        try {
            boolean deleted = profileDao.deleteProfile(userName);
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


}
