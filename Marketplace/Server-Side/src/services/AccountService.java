package services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class AccountService {

    public boolean createAccount(

            char accountType,
            String userName,
            String password,
            String email,
            String firstName,
            String lastName) {

        JSONObject userObj = new JSONObject();

        JSONArray users = new JSONArray();
        JSONObject user = new JSONObject();

        user.put("id", generateUserId(accountType));
        user.put("username", userName);
        user.put("password", password);
        user.put("email", email);
        user.put("first_name", firstName);
        user.put("last_name", lastName);

        users.put(user);

        userObj.put("users", users);

        try {

            FileWriter file = new FileWriter("data.json");
            file.write(userObj.toString());
            file.flush();
            file.close();
            return true;

        } catch (IOException e) {
            System.err.println("Issue occurred writing to data file");
        }

        System.out.print(userObj);

        return false;
    }

    public boolean removeAccount(String userId) {

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
        JSONArray arr = obj.getJSONArray("users");

        for (Object user : arr) {
            if (((JSONObject) user).get("id").toString().equals(userId)) {
                String id = ((JSONObject) user).get("id").toString();
                accountDetails[0] = ((JSONObject) user).get("username").toString();
                accountDetails[1] = ((JSONObject) user).get("password").toString();
                accountDetails[2] = ((JSONObject) user).get("email").toString();
                accountDetails[3] = ((JSONObject) user).get("first_name").toString();
                accountDetails[4] = ((JSONObject) user).get("last_name").toString();
                accountDetails[5] = id.charAt(id.length()-1) == 'b' ? "Buyer" : "Seller";
            }
        }

        return accountDetails;
    }

    public String generateUserId(char AccountType) {

        return "id"+AccountType;
    }
}
