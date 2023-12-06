package services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;

/**
 * Marketplace Application : Account Service
 *
 * <p>
 * This service handles each users account details as well as account creation / deletion
 * </p>
 *
 * @author [NAMES], CS 180 Lab
 * @version v0.0.1
 */

public class AccountService {

            // Directory of the users.json file which stores all user data
            public final String userFileDirectory;

            /**
             * Constructor initializes user file directory, creates a new one if one does not exist.
             */
    public AccountService() {

                // TODO: Check if file exists, create a new one if it does not

                this.userFileDirectory = Paths.get(System.getProperty("user.dir") + "\\data\\users.json").toString();
            }

            /**
             * Creates a new user object in the users.json, populated by the parameters.
             *
             * @param accountType Indicates whether user is a buyer or seller
             * @param userName    Stores user's username
             * @param password    Stores user's password, may encrypt later
             * @param email       Stores user's email
             * @param firstName   Stores user's first name
             * @param lastName    Stores user's last name
             * @return True if account was successfully created, false otherwise
             */
            public boolean createAccount(
            char accountType,
            String userName,
            String password,
            String email,
            String firstName,
            String lastName) {

                // Creates a new user id
                String userId = generateUserId(accountType);

                JSONObject userObj = new JSONObject();

                // Ensures users.json file exists
                if (getJSONFile(getUserFileDirectory()) != null)
            userObj = new JSONObject(getJSONFile(getUserFileDirectory()));

        // Uses user email as it is unique, user id cannot be used as it generated upon creation
        if (userExists(email))
            return false;

        JSONArray users = userObj.getJSONArray("users");
        JSONObject user = new JSONObject();

        user.put("id", userId);
        user.put("username", userName);
        user.put("password", password);
        user.put("email", email);
        user.put("first_name", firstName);
        user.put("last_name", lastName);

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
        userObj.put("users", users);

        return writeJSONObjectToFile(userObj, getUserFileDirectory());

    }

    /**
     * Checks if user exists in users.json database by email
     *
     * @param email The email of the sought out user
     * @return True if user exists, false otherwise
     */
    private boolean userExists(String email) {
        return getUserByEmail(email) != null;
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

    /**
     * Checks if the user is a seller
     *
     * @param userId Gets user from database using their unique user id
     * @return True if the user is a seller, false otherwise
     */
    public boolean isSeller(String userId) {
        return userId.charAt(userId.length() - 1) == 's';
    }


    public String getUserFileDirectory() {
        return userFileDirectory;
    }

    public JSONObject getUserById(String userId) {
        for (Object user : new JSONObject(Objects.requireNonNull(getJSONFile(getUserFileDirectory()))).getJSONArray("users")) {
            if (((JSONObject) user).get("id").toString().equals(userId)) {
                return (JSONObject) user;
            }
        }
        return null;
    }

    private JSONObject getUserByEmail(String email) {
        System.out.println(getJSONFile(getUserFileDirectory()));
        for (Object user : new JSONObject(Objects.requireNonNull(getJSONFile(getUserFileDirectory()))).getJSONArray("users")) {
            if (((JSONObject) user).get("email").toString().equals(email)) {
                return (JSONObject) user;
            }
        }
        return null;
    }

    public boolean updateUserDetails(String userId, String key, Object value) {

        JSONObject users = new JSONObject(Objects.requireNonNull(getJSONFile(getUserFileDirectory())));

        for (Object user : users.getJSONArray("users")) {
            if (((JSONObject) user).get("id").toString().equals(userId)) {
                ((JSONObject) user).put(key, value);
                writeJSONObjectToFile(users, getUserFileDirectory());
                return true;
            }
        }

        return false;
    }

    public boolean removeAccount(String userId) {

        JSONObject userObj = new JSONObject(Objects.requireNonNull(getJSONFile(getUserFileDirectory())));
        JSONArray users = userObj.getJSONArray("users");
        for (int i = 0; i < users.length(); i++) {
            if (((JSONObject) users.get(i)).get("id").toString().equals(userId)) {

                users.remove(i);
                return writeJSONObjectToFile(userObj, getUserFileDirectory());
            }
        }

        return false;
    }

    public String getJSONFile(String fileDirectory) {
        try {
            System.out.println(getUserFileDirectory());
            return Files.readString(Path.of(fileDirectory));
        } catch (IOException e) {
            System.out.println("Error occurred retrieving user file...");
        }

        return null;
    }

    public boolean writeJSONObjectToFile(JSONObject userObj, String fileDirectory) {

        try {

            FileWriter file = new FileWriter(fileDirectory);
            file.write(userObj.toString());
            file.flush();
            file.close();
            return true;

        } catch (IOException e) {
            System.out.println("Error occurred writing json object to file...\n" + e.getMessage());
        }

        return false;
    }

    public String[] getAccountDetails(String userId) {

        String[] accountDetails = new String[6];

        JSONObject user = getUserById(userId);

        if (user == null)
            return null;

        accountDetails[0] = user.get("username").toString();
        accountDetails[1] = user.get("password").toString();
        accountDetails[2] = user.get("email").toString();
        accountDetails[3] = user.get("first_name").toString();
        accountDetails[4] = user.get("last_name").toString();
        accountDetails[5] = userId.charAt(userId.length() - 1) == 'b' ? "Buyer" : "Seller";

        return accountDetails;
    }

    /**
     * Generates a unique user id string
     * @param AccountType
     * @return
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
}
