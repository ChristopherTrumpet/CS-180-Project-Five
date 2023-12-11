package services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

/**
 * Marketplace Application : Account Service
 * <p>
 * This service handles each users account details as well as account creation / deletion
 *
 * @author Chris Trumpet, Matthew Lee, Mohit Ambe, Shrinand Perumal, Vraj Patel
 * @version December 11, 2023
 */
public class AccountService {

    // Directory of the users.json file which stores all user data
    public final String userFileDirectory;

    /**
     * Constructor initializes user file directory, creates a new one if one does not exist.
     */
    public AccountService() {
        this.userFileDirectory = Paths.get(System.getProperty("user.dir") + "/data/users.json").toString();
    }

    /**
     * Creates a new user object in the users.json, populated by the parameters.
     *
     * @param accountType Indicates whether user is a buyer or seller
     * @param userName    Stores user's username
     * @param password    Stores user's password, may encrypt later
     * @param email       Stores user's email
     * @return True if account was successfully created, false otherwise
     */
    public boolean createAccount(char accountType, String userName, String password, String email) {

        // Creates a new user id
        String userId = generateUserId(accountType);

        JSONObject userObj = new JSONObject();

        // Ensures users.json file exists
        if (getJSONFromFile(getUserFileDirectory()) != null) userObj = getJSONFromFile(userFileDirectory);

        JSONArray users = userObj.getJSONArray("users");
        JSONObject user = new JSONObject();

        user.put("id", userId);
        user.put("username", userName);
        user.put("password", password);
        user.put("email", email);

        // Create an empty cart if the user is a buyer
        if (accountType == 'b') {
            user.put("cart", new JSONArray());
            user.put("product_history", new JSONArray());
            user.put("funds", 0.0f);
        }

        if (accountType == 's') {
            user.put("stores", new JSONArray());
        }

        users.put(user);
        return writeJSONObjectToFile(userObj, getUserFileDirectory());

    }


    /**
     * Checks if user exists in users.json database by email
     *
     * @param email The email of the sought out user
     * @return True if user exists, false otherwise
     */
    public boolean userExists(String email, String username) {
        return getUser("email", email) != null || getUser("username", username) != null;
    }

    /**
     * Checks if the user is a buyer
     *
     * @param userId Gets user from database using their unique user id
     * @return True if the user is a buyer, false otherwise
     */
    public boolean isBuyer(String userId) {
        return userId.charAt(userId.length() - 1) == 'b';
    }

    public String getUserFileDirectory() {
        return userFileDirectory;
    }

    public JSONObject getUser(String identifier, String value) {

        JSONObject users = getJSONFromFile(userFileDirectory);

        for (Object user : users.getJSONArray("users")) {

            String indexedUserUsername = ((JSONObject) user).get(identifier).toString();
            if (indexedUserUsername.equals(value)) return (JSONObject) user;
        }
        return null;
    }

    public boolean updateUserDetails(String userId, String key, Object value) {

        JSONObject users = getJSONFromFile(userFileDirectory);

        for (Object user : users.getJSONArray("users")) {
            if (((JSONObject) user).getString("id").equals(userId)) {
                ((JSONObject) user).put(key, value);
                writeJSONObjectToFile(users, getUserFileDirectory());
                return true;
            }
        }

        return false;
    }

    public boolean removeAccount(String userId) {

        JSONObject userObj = getJSONFromFile(userFileDirectory);
        JSONArray users = userObj.getJSONArray("users");

        for (int i = 0; i < users.length(); i++) {

            // Indexed users id
            String indexedUserId = ((JSONObject) users.get(i)).get("id").toString();

            if (indexedUserId.equals(userId)) {
                users.remove(i);
                return writeJSONObjectToFile(userObj, getUserFileDirectory());
            }
        }
        return false;
    }


    public JSONObject getJSONFromFile(String fileDirectory) {
        try {
            return new JSONObject(Files.readString(Path.of(fileDirectory)));
        } catch (IOException e) {
            System.out.println("Error occurred retrieving user file...");
        }

        return null;
    }

    public boolean writeJSONObjectToFile(JSONObject userObj, String fileDirectory) {

        try (FileWriter fileWriter = new FileWriter(fileDirectory)) {

            fileWriter.write(userObj.toString());
            fileWriter.flush();
            fileWriter.close();
            return true;

        } catch (IOException e) {
            System.out.println("Error occurred writing json object to file...\n" + e.getMessage());
        }

        return false;
    }

    /**
     * Generates a unique user id string
     *
     * @param AccountType appends either 'b' or 's' to signify whether account is buyer or seller
     * @return unique randomized id of user
     */
    public String generateUserId(char AccountType) {

        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; // Potential characters for id
        StringBuilder userId = new StringBuilder();
        Random rnd = new Random();

        while (userId.length() < 16) { // Creates a user id 16 characters long
            int index = (int) (rnd.nextFloat() * chars.length());
            userId.append(chars.charAt(index));
        }

        return userId.toString() + AccountType;
    }


    public JSONObject validateLogin(String identifier, String password) {

        JSONObject user = getUser("username", identifier);

        if (user == null) {
            user = getUser("email", identifier);

            if (user == null) return null;
        }

        if (user.getString("password").equals(password)) return user;

        return null;
    }
}
