package com.needs.api.needsapi.persistence;

import java.io.IOException;
import com.needs.api.needsapi.model.Profile;

/**
 * Defines the interface for leaderboard object persistence
 * 
 * @author Veronika Kirievskaya
 */
public interface LeaderboardDAO {
    /**
     * Retrieves full leaderboard
     * 
     * @return A sorted array of string arrays; Each inner array contains the user's rank, their username, their fullname, and total contribution
     * @throws IOException if an issue with underlying storage
     */
    String[][] getLeaderboard() throws IOException;

    /**
     * Updates and saves the full leaderboard
     * 
     * @param {@link Profile profile} updated object to reload the leaderboard
     * 
     * @return updated leaderboard
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    String[][] updateLeaderboard(Profile profile) throws IOException;
}
