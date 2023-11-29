package services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class AccountService {

    public static void main(String[] args) {
        AccountService accountService = new AccountService();

        if (accountService.createAccount('b',"ctrumpet","passwrod","email@email.com","chris","trumpet")) {
            System.out.println("Success!");
        } else
            System.out.println("Fail");
    }

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

    public String[] getAccountDetails(String userId) {

        return null;
    }

    public String generateUserId(char AccountType) {

        return "id"+AccountType;
    }
}
