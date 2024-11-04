package com.needs.api.needsapi.persistence;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.needs.api.needsapi.model.Login;
import com.needs.api.needsapi.model.Need;
import com.needs.api.needsapi.model.Profile;

/**
 * Test the Profile File DAO class
 * 
 * @author Team Swiss Pandas
 * @author Ryan Garvin
 */
@Tag("Persistence-tier")
public class ProfileFileDAOTest {
    ProfileFileDAO profileFileDAO;
    Profile[] testProfiles;
    ObjectMapper mockProfileObjectMapper;
    ObjectMapper mockLoginObjectMapper;
    LoginFileDAO loginFileDAO;
    Login[] testLogins;


    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupProfileFileDAO() throws IOException {
        mockProfileObjectMapper = mock(ObjectMapper.class);
        testProfiles = new Profile[3];
        HashMap<Integer,Integer> contributions = new HashMap<>();
        contributions.put(1, 1);
        testProfiles[0] = new Profile("Ryan","Garvin","ryangarvin19@gmail.com","New York", "RyanG9786", contributions);
        testProfiles[1] = new Profile("Dulce","Villages","ryangarvin20@gmail.com","New Jesrsy", "RyanZ", contributions);
        testProfiles[2] = new Profile("Oscar","Li","ryangarvin22@gmail.com","Connecticut", "OscarL9789", contributions);


        mockLoginObjectMapper = mock(ObjectMapper.class);
        testLogins = new Login[2];
        testLogins[0] = new Login("RyanG9786", "1", 10);
        testLogins[1] = new Login("RyanZ", "helper1", 20);
        testLogins[0] = new Login("OscarL9789", "1", 30);
        testLogins[0] = new Login("Test", "1", 40);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the login array above
        when(mockLoginObjectMapper
            .readValue(new File("data/logins.json"),Login[].class))
                .thenReturn(testLogins);
        loginFileDAO = new LoginFileDAO("data/logins.json",mockLoginObjectMapper, null);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the profile array above
        when(mockProfileObjectMapper
            .readValue(new File("data/profiles.json"),Profile[].class))
                .thenReturn(testProfiles);
        profileFileDAO = new ProfileFileDAO("data/profiles.json",mockProfileObjectMapper, loginFileDAO);
    }

    @Test
    public void testGetProfile() throws IOException {
        // Invoke
        Profile profile = profileFileDAO.getProfile("RyanZ");
        
        // Analyze
        assertEquals(profile.getFirstName(), "Dulce");
        assertEquals(profile.getLastName(), "Villages");
        assertEquals(profile.getCountry(), "New Jesrsy");
        assertEquals(profile.getEmail(), "ryangarvin20@gmail.com");
        assertEquals(profile.getUserName(), "RyanZ");
    }
    
    @Test
    public void testGetProfileNotFound() throws IOException {
        // Invoke
        Profile profile = profileFileDAO.getProfile("RyanGarvin");

        // Analyze
        assertEquals(profile, null);
 
    }

    @Test
    public void testUpdateProfile()  throws IOException {
        // Setup
        HashMap<Integer,Integer> contributions = new HashMap<>();
        contributions.put(1, 1);
        Profile profile = new Profile("Bob","Sulek","ryangarvin20@gmail.com","Maine", "RyanZ", contributions);


        // Invoke
        Profile result = assertDoesNotThrow(() -> profileFileDAO.updateProfile(profile),
                                "Unexpected exception thrown");
 
        // Analyze
        assertNotNull(result);
        Profile actual = profileFileDAO.getProfile(profile.getUserName());
        assertEquals(actual,profile);
    }

    @Test
    public void testPartiallyUpdateProfile() throws IOException {
        // Setup
        HashMap<Integer,Integer> contributions = new HashMap<>();
        contributions.put(1, 1);
        Profile profile2 = new Profile("Joe",null,null,null, "RyanZ", contributions);

        // tests that any fields that are null stay the same

        // Invoke
        Profile result2 = assertDoesNotThrow(() -> profileFileDAO.updateProfile(profile2),
                                "Unexpected exception thrown");

        // Analyze
        
        assertNotNull(result2);
       
        Profile actual2 = profileFileDAO.getProfile(profile2.getUserName());
    
        assertEquals(actual2,profile2);
    }

    // @Test
    // public void testPartiallyUpdateProfilePart2() throws IOException {
    //     // Setup
    //     HashMap<Integer,Integer> contributions = new HashMap<>();
    //     contributions.put(1, 1);
    //     Profile profile1 = new Profile(null,null,null,null, "OscarL", contributions);
    //     Profile profile2 = new Profile("Joe",null,null,null, "RyanZ", contributions);

    //     // tests that any fields that are null stay the same

    //     // Invoke
    //     Profile result1 = assertDoesNotThrow(() -> profileFileDAO.updateProfile(profile1),
    //                             "Unexpected exception thrown");
    //     Profile result2 = assertDoesNotThrow(() -> profileFileDAO.updateProfile(profile2),
    //                             "Unexpected exception thrown");

    //     // Analyze
    //     assertNotNull(result1);
    //     assertNotNull(result2);
    //     Profile actual1 = profileFileDAO.getProfile(profile1.getUserName());
    //     Profile actual2 = profileFileDAO.getProfile(profile2.getUserName());
    //     assertEquals(actual1,profile1);
    //     assertEquals(actual2,profile2);
    // }


    @Test
    public void testCreateProfile() throws  IOException{
        // Setup
        HashMap<Integer,Integer> contributions = new HashMap<>();
        contributions.put(1, 1);
        Profile profile = new Profile("russell","Garvin","ryangarvin19@gmail.com","New York", "Test", contributions);

        // Invoke
        Profile result = assertDoesNotThrow(() -> profileFileDAO.createProfile(profile),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Profile actual = profileFileDAO.getProfile(profile.getUserName());
        assertEquals(actual.getFirstName(),profile.getFirstName());
        assertEquals(actual.getLastName(),profile.getLastName());
        assertEquals(actual.getEmail(),profile.getEmail());
        assertEquals(actual.getCountry(),profile.getCountry());
        assertEquals(actual.getUserName(),profile.getUserName());
    }


    @Test
    public void testSwitchPrivacy() throws  IOException{
        // Setup
        HashMap<Integer,Integer> contributions = new HashMap<>();
        contributions.put(1, 1);
        Profile profile = new Profile("russell","Garvin","ryangarvin19@gmail.com","New York", "RyanZ", contributions);

        // Invoke
        Profile result = assertDoesNotThrow(() -> profileFileDAO.switchPrivacy(profile),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Profile actual = profileFileDAO.getProfile(profile.getUserName());
        
        assertEquals(actual.getIsPrivate(), true);
    }

        
    
}
