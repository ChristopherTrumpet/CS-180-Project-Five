package services;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

class AccountServiceTest {

    @BeforeEach
    public void setup() {

    }

    @org.junit.jupiter.api.Test
    void createAccount() throws IOException {
        AccountService accountService = new AccountService();
        Assertions.assertTrue(accountService.createAccount(
            'b',
            "CTrumpet",
            "password",
            "ctrumpet@purdue.edu"
        ));
    }

    @org.junit.jupiter.api.Test
    void removeAccount() {

    }

    @org.junit.jupiter.api.Test
    void getAccountDetails() {
    }

}