package com.needs.api.needsapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.needs.api.needsapi.persistence.LeaderboardDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Leaderboard Controller class
 * 
 * @author Veronika Kirievskaya
 */
@Tag("Controller-tier")
public class LeaderboardControllerTest {
    private LeaderboardController leaderboardController;
    private LeaderboardDAO mockLeaderboardDAO;

    /**
     * Before each test, create a new LeaderboardController object and inject
     * a mock Leaderboard DAO
     */
    @BeforeEach
    public void setupLeaderboardController() {
        mockLeaderboardDAO = mock(LeaderboardDAO.class);
        leaderboardController = new LeaderboardController(mockLeaderboardDAO);
    }

    @Test
    public void testGetLeaderboard() throws IOException {  // getLeaderboard may throw IOException
        // Setup
        String[][] leaderboard = {{"Rank 1"}, {"Rank 2"}}; //new String[2][1];
        // When the same id is passed in, our mock Leaderboard DAO will return the Leaderboard object
        when(mockLeaderboardDAO.getLeaderboard()).thenReturn(leaderboard);

        // Invoke
        ResponseEntity<String[][]> response = leaderboardController.getLeaderboard();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(leaderboard,response.getBody());
    }

    @Test
    public void testGetLeaderboardHandleException() throws Exception { // createLeaderboard may throw IOException
        // When getLeaderboard is called on the Mock Leaderboard DAO, throw an IOException
        doThrow(new IOException()).when(mockLeaderboardDAO).getLeaderboard();

        // Invoke
        ResponseEntity<String[][]> response = leaderboardController.getLeaderboard();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
