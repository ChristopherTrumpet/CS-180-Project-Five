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

public class AccountService {

    private final String userFileDirectory;

    public AccountService() throws IOException {
        this.userFileDirectory = Paths.get(System.getProperty("user.dir") + "\\Marketplace\\Server-Side\\data\\users.json").toString();
    }

    public boolean createAccount(

            char accountType,
            String userName,
            String password,
            String email,
            String firstName,
            String lastName) {

        String userId = generateUserId(accountType);
        JSONObject userObj = new JSONObject();

        if (getUserFile() != null)
            userObj = new JSONObject(getUserFile());

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

        return writeUserToFile(userObj);

    }

    private boolean userExists(String email, JSONObject userObj) {
        return getUserByEmail(email, userObj) != null;
    }

    private JSONObject getUserById(String userId) {
        for (Object user : new JSONObject(Objects.requireNonNull(getUserFile())).getJSONArray("users")) {
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

    private boolean updateUserDetails(String userId, String key, String value) {

        JSONObject users = new JSONObject(Objects.requireNonNull(getUserFile()));

        for (Object user : users.getJSONArray("users")) {
            if (((JSONObject) user).get("id").toString().equals(userId)) {
                ((JSONObject) user).put(key, value);
                writeUserToFile(users);
                return true;
            }
        }

        return false;
    }

    public boolean removeAccount(String userId) {

        JSONObject userObj = new JSONObject(Objects.requireNonNull(getUserFile()));
        JSONArray users = userObj.getJSONArray("users");
        for (int i = 0; i < users.length(); i++) {
            if (((JSONObject) users.get(i)).get("id").toString().equals(userId)) {

                users.remove(i);
                return writeUserToFile(userObj);
            }
        }

        return false;
    }

    private String getUserFile() {
        try {
            return Files.readString(Path.of(userFileDirectory));
        } catch (IOException e) {
            System.out.println("Error occurred retrieving user file...");
        }

        return null;
    }

    private boolean writeUserToFile(JSONObject userObj) {
        try {
            FileWriter file = new FileWriter(userFileDirectory);
            file.write(userObj.toString());
            file.flush();
            file.close();
            return true;


        } catch (IOException e) {
            System.out.println("Error occurred writing json object to file...\n" + e.getMessage());
        }

        return false;
    }

    public String[] getAccountDetails(String userId) throws IOException {

        String userFile = Files.readString(Paths.get("E:\\dev\\CS-180-Project-Five\\Marketplace\\Server-Side\\data\\users.json"));
        String[] accountDetails = new String[6];

        JSONObject obj = new JSONObject(userFile);
        JSONObject user = getUserById(userId);

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
