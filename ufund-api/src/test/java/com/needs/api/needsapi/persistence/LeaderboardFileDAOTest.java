package com.needs.api.needsapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
//import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.needs.api.needsapi.model.Login;
import com.needs.api.needsapi.model.Need;
import com.needs.api.needsapi.model.NeedType;
import com.needs.api.needsapi.model.Profile;
import com.needs.api.needsapi.model.UrgencyTag;

/**
 * Test the Leaderboard File DAO class
 * 
 * @author Team Swiss Pandas
 * @author Veronika Kirievskaya
 */
public class LeaderboardFileDAOTest {
    LeaderboardFileDAO leaderboardFileDAO;
    String[][] testLeaderboard;
    Profile[] testProfiles;
    Need[] testNeeds;
    ObjectMapper mockObjectMapper;
    LoginDAO mockLoginDAO;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupLeaderboardFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        mockLoginDAO = mock(LoginDAO.class);
        testLeaderboard = new String[2][1];
        testLeaderboard[0] = new String[]{"1", "RyanG", "Dulce Garvin", "20"};
        testLeaderboard[1] = new String[]{"2", "not null", "null null", "0"};

        testNeeds = new Need[2];
        testNeeds[0] = new Need(100, "null", NeedType.EQUIPMENT, (double)10, 10, 0, UrgencyTag.LOW, "null", "null");
        testNeeds[1] = new Need(101, "not null", NeedType.EQUIPMENT, (double)20, 10, 0, UrgencyTag.LOW, "null", "null");

        String firstName = "Dulce";
        String lastName = "Garvin";
        String address = "New York";
        String email = "ryangarvin19@gmail.com";
        String userName = "RyanG";
        HashMap<Integer,Integer> contributions = new HashMap<>();
        contributions.put(1, 1);
        int needId = 100;
        int quantity = 2;
        Profile profile = new Profile(firstName, lastName, email, address, userName, contributions);
        profile.addContribution(needId, quantity);

        testProfiles = new Profile[2];
        testProfiles[0] = new Profile("null", "null", "null", "null", "null", null);
        testProfiles[1] = profile;


        // When the object mapper is supposed to read from a file
        // the mock object mapper will return the one of the arrays above
        when(mockObjectMapper
            .readValue(new File("data/needs.json"),Need[].class))
                .thenReturn(testNeeds);
        when(mockObjectMapper
            .readValue(new File("data/profiles.json"),Profile[].class))
                .thenReturn(testProfiles);
        leaderboardFileDAO = new LeaderboardFileDAO(mockObjectMapper, mockLoginDAO);
    }

    @Test
    public void testGetLeaderboard() {
        try {
            // Invoke
            String[][] leaderboard = leaderboardFileDAO.getLeaderboard();
        
            // Analyze
            assertEquals(leaderboard.length, testLeaderboard.length);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void testAnonymous() {
        testProfiles[0].switchPrivacy();
        testProfiles[1].switchPrivacy();

        try {
            when(mockObjectMapper
                .readValue(new File("data/profiles.json"),Profile[].class))
                    .thenReturn(testProfiles);
            leaderboardFileDAO = new LeaderboardFileDAO(mockObjectMapper, mockLoginDAO);
            // Invoke
            String[][] leaderboard = leaderboardFileDAO.updateLeaderboard(testProfiles[0]);

            // Analyze
            assertEquals(2, leaderboard.length);

            assertEquals("Anonymous", leaderboard[0][1]);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}
