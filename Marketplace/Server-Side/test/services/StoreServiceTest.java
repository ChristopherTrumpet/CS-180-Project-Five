package services;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;

import java.nio.file.Paths;

import static org.junit.Assert.*;

public class StoreServiceTest {

    private StoreService storeService;
    private AccountService accountService;
    private String userId;
    private String newStoreId;
    private String productId;

    @Before
    public void setup() {
        storeService = new StoreService();
        accountService = new AccountService();
        accountService.createAccount('s', "testUser", "pass", "testemail@gmail.com");
        userId = accountService.getUser("username", "testUser").getString("id");
    }

    @Test
    public void getStoreFile() {
        String storeFile = Paths.get(System.getProperty("user.dir") + "/Marketplace/Server-Side/data/stores.json").toString();
        assertEquals(storeFile, storeService.getStoreFileDirectory());
    }

    @Test
    public void getProductFile() {
        String storeFile = Paths.get(System.getProperty("user.dir") + "/Marketplace/Server-Side/data/products.json").toString();
        assertEquals(storeFile, storeService.getProductFileDirectory());
    }

    @Test
    public void removeSellerStore() {
        storeService.addStoreToSeller(userId, "storeId");
        assertTrue(storeService.removeStoreFromSeller("storeId", userId));
    }

    @Test
    public void createStore() {
        assertTrue(storeService.createStore(userId, "storeName"));
    }

    @Test
    public void changeStoreName() {
        newStoreId = storeService.getStoreByName("storeName").getString("id");
        storeService.updateStoreName(newStoreId, "newStoreName");
        assertEquals("newStoreName", storeService.getStoreById(newStoreId).getString("name"));
    }

    @Test
    public void createProduct() {
        storeService.createProduct("newProduct", "productDescription");
        productId = storeService.getProduct("name", "newProduct").getString("product_id");
        assertNotNull(storeService.getProduct("product_id", productId));
    }

    @After
    public void cleanup() {
        storeService.removeProductFromProducts(productId);
        storeService.removeStore(newStoreId);
        accountService.removeAccount(userId);
    }

}
