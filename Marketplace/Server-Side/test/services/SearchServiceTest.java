package services;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Marketplace Application : Testing
 *
 * <p>
 *     Tests the functions related to search algorithm
 * </p>
 *
 * @author Christoher Trumpet, Matthew Lee, Shrinand Perunal, Mohit Ambe, Vraj Patel
 */


public class SearchServiceTest {

    private SearchService searchService;
    private StoreService storeService;
    private AccountService accountService;
    private String userId;
    private String storeId;
    private String productId;

    @Before
    public void setup() {
        searchService = new SearchService();
        storeService = new StoreService();
        accountService = new AccountService();
        accountService.createAccount('s', "testUser", "pass", "testemail@gmail.com");
        userId = accountService.getUser("username", "testUser").getString("id");
        storeService.createStore(userId, "storeName");
        storeId = storeService.getStoreByName("storeName").getString("id");
    }

    @Test
    public void searchNonexistentProduct() {
        ArrayList<String> results = searchService.search("aaaaaaaaaaaaaaa");
        assertNull(results);
    }

    @Test
    public void searchExistingProduct() {
        JSONObject product = storeService.createProduct("newProduct", "productDescription");
        productId = product.getString("product_id");
        storeService.addProduct(storeId, productId, 10, 10.0);
        ArrayList<String> results = searchService.search("newProduct");
        assertNotNull(results);
    }

    @After
    public void cleanup() {
        storeService.removeProductFromProducts(productId);
        storeService.removeProduct(storeId, productId);
        storeService.removeStore(storeId);
        accountService.removeAccount(userId);
    }

}
