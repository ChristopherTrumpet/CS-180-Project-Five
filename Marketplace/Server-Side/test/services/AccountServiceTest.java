package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Marketplace Application : Testing
 *
 * <p>
 *     Tests the functions related to user details and onboarding
 * </p>
 *
 * @author Christoher Trumpet, Matthew Lee, Shrinand Perunal, Mohit Ambe, Vraj Patel
 */

public class AccountServiceTest {

    private AccountService accountService;
    private String userId;

    @Before
    public void setup() {
        accountService = new AccountService();
    }

    @Test
    public void getStoreFile() {
        String storeFile = Paths.get(System.getProperty("user.dir") + "/data/users.json").toString();
        assertEquals(storeFile, accountService.getUserFileDirectory());
    }

    @Test
    public void createAccount() {
        assertTrue(accountService.createAccount('b', "testUser", "pass", "testemail@gmail.com"));
    }

    @Test
    public void userIsBuyer() {
        userId = accountService.getUser("username", "testUser").getString("id");
        assertTrue(accountService.isBuyer(userId));
    }

    @Test
    public void validateUser() {
        assertNotNull(accountService.validateLogin("testemail@gmail.com", "pass"));
    }

    @After
    public void cleanup() {
        accountService.removeAccount(userId);
    }

}