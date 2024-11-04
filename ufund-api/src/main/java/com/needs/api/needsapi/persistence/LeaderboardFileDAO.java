package com.needs.api.needsapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.needs.api.needsapi.model.Need;
import com.needs.api.needsapi.model.Profile;

/**
 * Implements the functionality for JSON file-based peristance for leaderboard
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Veronika Kirievskaya
 */
@Component
public class LeaderboardFileDAO implements LeaderboardDAO {
    private static final Logger LOG = Logger.getLogger(LeaderboardFileDAO.class.getName());
    private ObjectMapper objectMapper;  // Provides conversion between Profile/Need
                                        // objects and JSON text format written
                                        // to the file
    ArrayList<Profile> profiles;
    Map<Integer, Need> needs;

    private  LoginDAO loginDAO;

    /**
     * calculate the total amount, in dollars, that a given user has contributed
     * 
     * @param user the user to calculate the contributions for
     * @return a double representing the dollar amount of the user's contribution
     */
    private double calculate(Profile user) {
        double total = 0;
        HashMap<Integer, Integer> contributions = user.getContributions();

        if (contributions != null) {
            for (int need_index : contributions.keySet()) {
                Need need = needs.get(need_index);
                double price;

                if (need == null)
                    price = 1;
                else
                    price = need.getPrice();

                double contributed = price * contributions.get(need_index);
                total += contributed;
            }
        }

        return total;
    }

    /**
     * A comparator to sort the array list of profiles based on their total contributions
     */
    Comparator<Profile> comparator = (a,b) -> {
        double a_quantity = calculate(a);
        double b_quantity = calculate(b);

        if (a_quantity == b_quantity)
            return 0;
        else if (a_quantity > b_quantity)
            return -1;
        else
            return 1;
    };

    /**
     * Creates a Leaderboard File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public LeaderboardFileDAO(ObjectMapper objectMapper, LoginDAO loginDAO) throws IOException {
        this.objectMapper = objectMapper;
        this.loginDAO = loginDAO;
        load();  // load the profiles from the file
    }

    /**
     * Loads {@linkplain Need needs} and {@linkplain Profile profiles} from the JSON files into the map
     * <br>
     * The needs are used to calculate each user's total contributions
     * The profiles are used to rank each user for the leaderboard by contribution
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        // Deserializes the JSON objects from the file into an array of profiles
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        
        Profile[] profileArray = objectMapper.readValue(new File("data/profiles.json"),Profile[].class);
        Need[] needsArray = objectMapper.readValue(new File("data/needs.json"),Need[].class);

        profiles = new ArrayList<>();
        needs = new HashMap<>();

        for (Profile profile : profileArray) {
            
            // if(loginDAO.userExists(profile.getUserName()) == true){
                if(!profile.getUserName().equals("admin")){
                    profiles.add(profile);
                }
           // }
        }

        for (Need need : needsArray) {
            if (need != null)
                needs.put(need.getId(), need);
        }
        
        return true;
    }

    @Override
    public String[][] getLeaderboard() throws IOException {
        load();
        profiles.sort(comparator);
        int length = 0;

        if (profiles != null) {
            length = profiles.size();
        }

        LOG.info("length of profiles is " + length);
        String[][] board = new String[length][4];

        for (int i = 0; i < length; i++) {
            //board[i] = profiles.get(i);
            Profile user = profiles.get(i);
            String[] row = new String[4];
            row[0] = String.valueOf(i +1);

            if(user.getIsPrivate() == true){
                row[1] = "Anonymous";
                row[2] = "Anonymous";
            }
            else{
                row[1] = user.getUserName();
                row[2] = user.getFirstName() + " " + user.getLastName();
            }
            
            row[3] = String.valueOf(calculate(user)) + "0";
            board[i] = row;
        }

        return board;
    }

    @Override
    public String[][] updateLeaderboard(Profile profile) throws IOException {
        profiles.add(profile);
        return getLeaderboard();
    }
}
