package com.needs.api.needsapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.needs.api.needsapi.model.FundingBasket;
import com.needs.api.needsapi.model.Login;
import com.needs.api.needsapi.model.Profile;


/**
 * Implements the functionality for JSON file-based peristance for Logins
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as logined
 * 
 * @author Team Swiss Pandas
 * @author Oscar Li
 */
@Component
public class LoginFileDAO implements LoginDAO {
    private static final Logger LOG = Logger.getLogger(LoginFileDAO.class.getName());
    Map<String,Login> logins;   // Provides a local cache of the login objects
                                // so that we don't login to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Login
                                        // objects and JSON text format written
                                        // to the file
    private String filename;    // Filename to read from and write to

    private NeedFileDAO needDAO;
    private BasketFileDAO basketDAO;

    /**
     * Creates a Login File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public LoginFileDAO(@Value("${logins.file}") String filename,ObjectMapper objectMapper,BasketFileDAO basketDAO) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.basketDAO = basketDAO;
        load();  // load the logins from the file
    }

    /**
     * Generates an array of {@linkplain Login logins} from the tree map
     * 
     * @return  The array of {@link Login logins}, may be empty
     */
    private Login[] getLoginsArray() {
        return getLoginsArray(null);
    }

    /**
     * Generates an array of {@linkplain Login logins} from the tree map for any
     * {@linkplain Login logins} that contains the text specified by userName
     * <br>
     * If userName is null, the array contains all of the {@linkplain Login logins}
     * in the tree map
     * 
     * @return  The array of {@link Login logins}, may be empty
     */
    private Login[] getLoginsArray(String userName) { // if userName == null, no filter
        ArrayList<Login> loginArrayList = new ArrayList<>();

        for (Login login : logins.values()) {
            if (userName == null || login.getUserName().contains(userName)) {
                loginArrayList.add(login);
            }
        }

        Login[] loginArray = new Login[loginArrayList.size()];
        loginArrayList.toArray(loginArray);
        return loginArray;
    }

    /**
     * Saves the {@linkplain Login logins} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Login logins} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Login[] loginArray = getLoginsArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),loginArray);
        return true;
    }

    /**
     * Loads {@linkplain Login logins} from the JSON file into the map
     * <br>
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        logins = new TreeMap<>();

        // Deserializes the JSON objects from the file into an array of logins
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        
        Login[] loginArray = objectMapper.readValue(new File("data/logins.json"),Login[].class);

        // Add each login to the tree map
        for (Login login : loginArray) {
            logins.put(login.getUserName(),login);
        }
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Login[] getLogins() {
        synchronized(logins) {
            return getLoginsArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Login[] findLogins(String userName) {
        synchronized(logins) {
            return getLoginsArray(userName);
        }
    }

    @Override
    public boolean userExists(String userName) {
        for (Login login : logins.values()) {
            if (login.getUserName().equals(userName)) {
                return true;
            }
        }

        return false;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Login getLogin(String userName) {
        synchronized(logins) {
            if (logins.containsKey(userName))
                return logins.get(userName);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Login createLogin(Login login) throws IOException {
        synchronized(logins) {
            // We create a new login object because the name field is immutable
            // and we login to assign the next unique name
            Login newLogin = new Login(login.getUserName(), login.getPass(), login.getBasketId());

            // checks if userName already exists
            if(userExists(newLogin.getUserName()) == true){
                return null;
            }

            logins.put(newLogin.getUserName(),newLogin);
            save(); // may throw an IOException
            return newLogin;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Login updateLogin(String userName, Login login) throws IOException {
        synchronized(logins) {
            if (logins.containsKey(userName) == false){
                return null;  // login does not exist
            }
            if(login.getUserName() != userName && userExists(login.getUserName())){
                return null;
            }

            

            int id = basketDAO.getId(userName);
            if(id != -1){
                login.setBasketId(id);
            }

            basketDAO.setUsername(id, login.getUserName());

            logins.remove(userName);
            logins.put(login.getUserName(), login);


            save(); // may throw an IOException

            return login;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteLogin(String userName) throws IOException {
        synchronized(logins) {
            if (logins.containsKey(userName)) {
                logins.remove(userName);
                return save();
            }
            else
                return false;
        }
    }

    /**
    ** {@inheritDoc}
     */
    public Login setBasketId(String userName, int basketId) throws IOException {
        synchronized(logins){
            if(logins.containsKey(userName)){
                Login login = logins.get(userName);
                login.setBasketId(basketId);
                save();
                return login;
            }else{
                return null;
            }
        }
    }

    /**
    ** {@inheritDoc}
     */
    public int getBasketId(String userName){
        synchronized(logins){
            if(logins.containsKey(userName)){
                Login login = logins.get(userName);
                return login.getBasketId();
            }else{
                return 0;
            }
        }
    }
}

