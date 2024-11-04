package com.needs.api.needsapi.persistence;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.needs.api.needsapi.model.Need;
import com.needs.api.needsapi.model.Profile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Implements the functionality for JSON file-based peristance for a User Pofile
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Team Swiss Pandas
 * @author Ryan Garvin
 */

 @Component
public class ProfileFileDAO implements ProfileDAO {

    private static final Logger LOG = Logger.getLogger(ProfileFileDAO.class.getName());
    Map<String,Profile> profiles;   // Provides a local cache of the profile objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Profile
                                        // objects and JSON text format written
                                        // to the file
    private String filename;    // Filename to read from and write to

    private LoginFileDAO loginDAO;
    /**
     * Creates a Profile File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public ProfileFileDAO(@Value("${profiles.file}") String filename,ObjectMapper objectMapper, LoginFileDAO loginDAO) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.loginDAO = loginDAO;
        load();  // load the profiles from the file
    }

    /**
     * Generates an array of {@linkplain Profile user profile} from the tree map
     * 
     * @return  The array of {@link  Profile user profile}, may be empty
     */
    private Profile[] getProfilesArray() {
        return getProfilesArray(null);
    }

    /**
     * Generates an array of {@linkplain  Profile user profile} from the tree map for any
     * {@linkplain Profile user profile} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain  Profile user profile}
     * in the tree map
     * 
     * @return  The array of {@link  Profile user profile}, may be empty
     */
    private Profile[] getProfilesArray(String containsText) { // if containsText == null, no filter
        ArrayList<Profile> profileArrayList = new ArrayList<>();

        for (Profile profile : profiles.values()) {
            if (containsText == null || profile.getUserName().contains(containsText)) {
                profileArrayList.add(profile);
            }
        }
        Profile[] proflieArray = new Profile[profileArrayList.size()];
        profileArrayList.toArray(proflieArray);
        return proflieArray;
    }

    /**
     * Saves the {@linkplain Profile profiles} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Profile profiles} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Profile[] profileArray = getProfilesArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),profileArray);

        // Add each need to the tree map and keep track of the greatest id
        for (Profile profile : profileArray) {
            profiles.put(profile.getUserName(),profile);
            if(loginDAO.userExists(profile.getUserName()) == false){
                profiles.remove(profile.getUserName());
            }
        }
        return true;
    }

    /**
     * Loads {@linkplain Need needs} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        profiles = new TreeMap<>();
        

        // Deserializes the JSON objects from the file into an array of profiles
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        
        Profile[] profileArray = objectMapper.readValue(new File("data/profiles.json"),Profile[].class);

        // Add each need to the tree map and keep track of the greatest id
        for (Profile profile : profileArray) {
            profiles.put(profile.getUserName(),profile);
            if(loginDAO.userExists(profile.getUserName()) == false){
                profiles.remove(profile.getUserName());
            }
        }
        
        return true;
    }

    // /**
    // ** {@inheritDoc}
    //  */
    // public boolean profileExists(String userName) {
    //     // if no name is given, then need will not be created 
    //     if(userName == null){
    //         return false;
    //     }
        
    //     for (String key : profiles.keySet()) {
    //         if (key.equals(userName)) {
    //             return true;
    //         }
    //     }

    //     return false;
    // }


    @Override
    public Profile getProfile(String userName) throws IOException {
     
        synchronized(profiles) {
            if (profiles.containsKey(userName))
                return profiles.get(userName);
            else
                return null;
        }
    }
    

    @Override
    public Profile updateProfile(Profile profile) throws IOException {
        synchronized(profiles) {
            if (profiles.containsKey(profile.getUserName()) == false){
                return null;  // profile does not exist
            }

            // for partial updates: if any fields are left blank, they stay the same
            Profile profileToUpdate = getProfile(profile.getUserName());

            if(profile.getFirstName() == null){
                profile.setFirstName(profileToUpdate.getFirstName());
            }

            if(profile.getLastName() == null){
                profile.setLastName(profileToUpdate.getLastName());
            }

            if(profile.getEmail() == null){
                profile.setEmail(profileToUpdate.getEmail());
            }

            if(profile.getCountry() == null){
                profile.setCountry(profileToUpdate.getCountry());
            }

            profiles.put(profile.getUserName(),profile);
            save(); // may throw an IOException
            return profile;
        }
    }



    @Override
    public Profile createProfile(Profile profile) throws IOException {
        synchronized(profiles) {
            // We create a new Profile object because 
            Profile newProfile = new Profile(profile.getFirstName(),profile.getLastName(),profile.getEmail(), profile.getCountry(), profile.getUserName(), profile.getContributions());
            System.out.println(newProfile);
            profiles.put(newProfile.getUserName(), newProfile);
            save(); // may throw an IOException
            return newProfile;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteProfile(String userName) throws IOException {
        synchronized(profiles) {
            if (profiles.containsKey(userName)) {
                profiles.remove(userName);
                return save();
            }
            else
                return false;
        }
    }

    @Override
    public Profile switchPrivacy(Profile profile) throws IOException {
        synchronized(profiles) {
            profile.switchPrivacy();
            profiles.put(profile.getUserName(),profile);
            save();
            return profile;

        }
    }
}
   
    
