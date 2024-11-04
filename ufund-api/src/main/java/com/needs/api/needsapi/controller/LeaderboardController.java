package com.needs.api.needsapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.needs.api.needsapi.persistence.LeaderboardDAO;

/**
 * Handles the REST API requests for the leaderboard resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Veronika Kirievskaya
 */

@RestController
@RequestMapping("leaderboard")
public class LeaderboardController {
    private static final Logger LOG = Logger.getLogger(LeaderboardController.class.getName());
    private LeaderboardDAO leaderboardDAO;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param leaderboardDAO The {@link LeaderboardDAO Leaderboard Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public LeaderboardController(LeaderboardDAO leaderboardDAO) {
        this.leaderboardDAO = leaderboardDAO;
    }

    /**
     * Responds to the GET request for the full leaderboard
     * 
     * @return ResponseEntity with map of integer leaderboard position to string user full name (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<String[][]> getLeaderboard() {
        LOG.info("GET /leaderboard");
        try {
            String[][] leaderboard = leaderboardDAO.getLeaderboard();
            LOG.info("Successful leaderboard fetch, length of profiles is " + leaderboard.length);
            return new ResponseEntity<String[][]>(leaderboard,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
