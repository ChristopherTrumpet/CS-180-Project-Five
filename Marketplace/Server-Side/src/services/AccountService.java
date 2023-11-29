package services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class AccountService {

    public boolean createAccount(

            char accountType,
            String userName,
            String password,
            String email,
            String firstName,
            String lastName) throws IOException {

        String userFile = Files.readString(Paths.get("E:\\dev\\CS-180-Project-Five\\Marketplace\\Server-Side\\data\\users.json"));
        String userId = generateUserId(accountType);
        JSONObject userObj = new JSONObject(userFile);

        if (userExists(email, userObj))
            return false;

        JSONArray users = userObj.getJSONArray("users");
        JSONObject user = new JSONObject();

        user.put("id", userId);
        user.put("username", userName);
        user.put("password", password);
        user.put("email", email);
        user.put("first_name", firstName);
        user.put("last_name", lastName);

        users.put(user);

        userObj.put("users", users);

        try {

            FileWriter file = new FileWriter("E:\\dev\\CS-180-Project-Five\\Marketplace\\Server-Side\\data\\users.json");
            file.write(userObj.toString());
            file.flush();
            file.close();
            return true;


        } catch (IOException e) {
            System.out.println("Error occurred writing json object to file...");
        }

        return false;
    }

    private boolean userExists(String email, JSONObject userObj) {
        return getUserByEmail(email, userObj) != null;
    }

    private JSONObject getUserById(String userId, JSONObject userObj) {
        for (Object user : userObj.getJSONArray("users")) {
            if (((JSONObject) user).get("id").toString().equals(userId)) {
                return (JSONObject) user;
            }
        }
        return null;
    }

    private JSONObject getUserByEmail(String email, JSONObject userObj) {
        for (Object user : userObj.getJSONArray("users")) {
            if (((JSONObject) user).get("email").toString().equals(email)) {
                return (JSONObject) user;
            }
        }
        return null;
    }

    public boolean removeAccount(String userId) throws IOException {
        String userFile = Files.readString(Paths.get("E:\\dev\\CS-180-Project-Five\\Marketplace\\Server-Side\\data\\users.json"));
        JSONObject userObj = new JSONObject(userFile);
        JSONArray users = userObj.getJSONArray("users");
        for (int i = 0; i < users.length(); i++) {
            if (((JSONObject) users.get(i)).get("id").toString().equals(userId)) {
                users.remove(i);
                try {

                    FileWriter file = new FileWriter("E:\\dev\\CS-180-Project-Five\\Marketplace\\Server-Side\\data\\users.json");
                    file.write(userObj.toString());
                    file.flush();
                    file.close();
                    return true;


                } catch (IOException e) {
                    System.out.println("Error occurred writing json object to file...");
                }
                return true;
            }
        }

        return false;
    }

    public boolean updateUsername(String userId, String newUserName) {

        return false;
    }

    public boolean updatePassword(String userId, String newPassword) {

        return false;
    }

    public boolean updateEmail(String userId, String newUserEmail) {

        return false;
    }

    public boolean updateFirstName(String userId, String newFirstName) {

        return false;
    }

    public boolean updateLastName(String userId, String newLastName) {

        return false;
    }

    public String[] getAccountDetails(String userId) throws IOException {

        String userFile = Files.readString(Paths.get("E:\\dev\\CS-180-Project-Five\\Marketplace\\Server-Side\\data\\users.json"));
        String[] accountDetails = new String[6];

        JSONObject obj = new JSONObject(userFile);
        JSONObject user = getUserById(userId, obj);

        if (user == null)
            return null;

        accountDetails[0] = user.get("username").toString();
        accountDetails[1] = user.get("password").toString();
        accountDetails[2] = user.get("email").toString();
        accountDetails[3] = user.get("first_name").toString();
        accountDetails[4] = user.get("last_name").toString();
        accountDetails[5] = userId.charAt(userId.length()-1) == 'b' ? "Buyer" : "Seller";

        return accountDetails;
    }

    public String generateUserId(char AccountType) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * chars.length());
            salt.append(chars.charAt(index));
        }
        return salt.toString()+AccountType;
    }
}
