package com.needs.api.needsapi.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.util.HashMap;

import com.needs.api.needsapi.model.Profile;
import com.needs.api.needsapi.persistence.ProfileDAO;

/**
 * Test the Profile Controller class
 * 
 * @author Team Swiss Pandas
 * @author Ryan Garvin
 */
@Tag("Controller-tier")
public class ProfileControllerTest {
    private ProfileController profileController;
    private ProfileDAO mockprofileDAO;

    /**
     * Before each test, create a new ProfileController object and inject
     * a mock Profile DAO
     */
    @BeforeEach
    public void setupProfileController() {
        mockprofileDAO = mock(ProfileDAO.class);
        profileController = new ProfileController(mockprofileDAO);

    }


    @Test
    public void testGetProfile() throws IOException {  
        // Setup
        Profile profile = new Profile("Ryan","Garvin","ryangarvin19@gmail.com","New York", "RyanG", null);
        // When the same username is passed in, our mock profile DAO will return the Profile object
        when(mockprofileDAO.getProfile(profile.getUserName())).thenReturn(profile);

        // Invoke
        ResponseEntity<Profile> response = profileController.getProfile(profile.getUserName());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(profile,response.getBody());
    }


    @Test
    public void testGetProfileNotFound() throws Exception { 
        // Setup
        Profile profile = new Profile("Ryan","Garvin","ryangarvin19@gmail.com","New York", "RyanG", null);
        // When the same username is passed in, our mock Profile DAO will return null, simulating
        // no Profile found
        when(mockprofileDAO.getProfile("this will fail")).thenReturn(null);

        // Invoke
        ResponseEntity<Profile> response = profileController.getProfile("this will fail");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetProfileHandleException() throws Exception { 
        // Setup
        Profile profile = new Profile("Ryan","Garvin","ryangarvin19@gmail.com","New York", "RyanG", null);
        // When getProfile is called on the Mock Profile DAO, throw an IOException
        doThrow(new IOException()).when(mockprofileDAO).getProfile("RyanG");

        // Invoke
        ResponseEntity<Profile> response = profileController.getProfile("RyanG");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }



    @Test
    public void testUpdateProfileNotFound() throws IOException { 
        // Setup
        Profile profile = new Profile("Ryan","Garvin","ryangarvin19@gmail.com","New York", "RyanG", null);
        // when update is called, return null simulating an unsuccessful
        // update and save
        when(mockprofileDAO.updateProfile(profile)).thenReturn(null);
        ResponseEntity<Profile> response = profileController.updateProfile(profile);
        profile.setFirstName("Bob");

        // Invoke
        response = profileController.updateProfile(profile);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateProfileHandleException() throws IOException { 
        // Setup
        Profile profile = new Profile("Ryan","Garvin","ryangarvin19@gmail.com","New York", "RyanG", null);
        // When getProfile is called on the Mock Profile DAO, throw an IOException
        doThrow(new IOException()).when(mockprofileDAO).updateProfile(profile);
        ResponseEntity<Profile> response = profileController.updateProfile(profile);
        profile.setFirstName("Bob");

        // Invoke
        response = profileController.updateProfile(profile);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateProfile() throws IOException { 
        // Setup
        Profile profile = new Profile("Ryan","Garvin","ryangarvin19@gmail.com","New York", "RyanG", null);
        // when update is called, return true simulating successful
        // update and save
        when(mockprofileDAO.updateProfile(profile)).thenReturn(profile);
        ResponseEntity<Profile> response = profileController.updateProfile(profile);
        profile.setFirstName("Bob");

        // Invoke
        response = profileController.updateProfile(profile);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(profile,response.getBody());
    }

    @Test
    public void testCreateProfile() throws IOException { 
        // Setup
        Profile profile = new Profile("mkmlml","Garvin","ryangarvin19@gmail.com","New York", "RyanG", null);
        // when create Profile is called, return true simulating successful
        // creation and save
        when(mockprofileDAO.createProfile(profile)).thenReturn(profile);

        // Invoke
        ResponseEntity<Profile> response = profileController.createProfile(profile);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(profile,response.getBody());
    }

    @Test
    public void testCreateProfileConflict() throws IOException { 
        // Setup
        Profile profile = new Profile("mkmlml","Garvin","ryangarvin19@gmail.com","New York", "RyanG", null);
        // when createProfile is called, return null simulating an unsuccessful
        // creation and save
        when(mockprofileDAO.createProfile(profile)).thenReturn(null);

        // Invoke
        ResponseEntity<Profile> response = profileController.createProfile(profile);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
        
    }

    @Test
    public void testCreateProfileHandleException() throws IOException { 
        // Setup
        Profile profile = new Profile("mkmlml","Garvin","ryangarvin19@gmail.com","New York", "RyanG", null);
        // when createProfile is called, return null simulating an unsuccessful
        // creation and save
        doThrow(new IOException()).when(mockprofileDAO).createProfile(profile);

        // Invoke
        ResponseEntity<Profile> response = profileController.createProfile(profile);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        
    }

    @Test
    public void testSwitchPrivacyConflict() throws IOException { 
        // Setup
        Profile profile = new Profile("Ryan","Garvin","ryangarvin19@gmail.com","New York", "RyanG", null);
        // when update is called, return null simulating an unsuccessful
        // update and save
        when(mockprofileDAO.switchPrivacy(profile)).thenReturn(null);
        ResponseEntity<Profile> response = profileController.switchPrivacy(null);


        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testSwitchPrivacyHandleException() throws IOException { 
        // Setup
        Profile profile = new Profile("Ryan","Garvin","ryangarvin19@gmail.com","New York", "RyanG", null);
        // When getProfile is called on the Mock Profile DAO, throw an IOException
        doThrow(new IOException()).when(mockprofileDAO).switchPrivacy(profile);
        
        ResponseEntity<Profile> response = profileController.switchPrivacy(profile);
       

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSwitchPrivacy() throws IOException { 
        // Setup
        Profile profile = new Profile("Ryan","Garvin","ryangarvin19@gmail.com","New York", "RyanG", null);
        // when update is called, return true simulating successful
        // update and save
        when(mockprofileDAO.switchPrivacy(profile)).thenReturn(profile);
        ResponseEntity<Profile> response = profileController.switchPrivacy(profile);

        profile.switchPrivacy();

        // Invoke
        response = profileController.switchPrivacy(profile);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(profile.getIsPrivate(), true);        
    }

    @Test
    public void testAddContribution() throws IOException{
        //Setup
        HashMap<Integer,Integer> contributions = new HashMap<>();
        contributions.put(1, 1);
        Profile profile = new Profile("Ryan","Garvin","ryangarvin19@gmail.com","New York", "RyanG", contributions);
        int needId = 1;
        int quantity = 2;
        Profile profile2 = new Profile("Ryan","Garvin","ryangarvin19@gmail.com","New York", "RyanG", contributions);
        profile2.addContribution(needId, quantity);
        when(mockprofileDAO.updateProfile(profile2)).thenReturn(profile2);
        when(mockprofileDAO.getProfile(profile.getUserName())).thenReturn(profile);
        when(mockprofileDAO.getProfile("fakeUser")).thenReturn(null);
        doThrow(new IOException()).when(mockprofileDAO).getProfile("errorTest");
        
        //Invoke
        ResponseEntity<HttpStatus> response = profileController.addContribution(profile.getUserName(), needId, quantity);
        ResponseEntity<HttpStatus> response2 = profileController.addContribution("fakeUser", needId, quantity);
        ResponseEntity<HttpStatus> response3 = profileController.addContribution("errorTest", needId, quantity);

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response3.getStatusCode());
    }
}
