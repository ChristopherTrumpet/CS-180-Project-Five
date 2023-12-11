package services;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StatServiceTest {

    private StatService statService;
    private TransactionService transactionService;
    private StoreService storeService;
    private AccountService accountService;
    private String buyerId;
    private String sellerId;
    private String storeId;
    private String productId;

    @Before
    public void setup() {
        statService = new StatService();
        transactionService = new TransactionService();
        storeService = new StoreService();
        accountService = new AccountService();
        accountService.createAccount('b', "buyer", "pass", "buyer@gmail.com");
        accountService.createAccount('s', "seller", "pass", "seller@gmail.com");
        buyerId = accountService.getUser("username", "buyer").getString("id");
        sellerId = accountService.getUser("username", "seller").getString("id");

        storeService.createStore(sellerId, "storeName");
        storeId = storeService.getStoreByName("storeName").getString("id");
        JSONObject product = storeService.createProduct("newProduct", "productDescription");
        productId = product.getString("product_id");
        storeService.addProduct(storeId, productId, 10, 10.0);
    }

    @Test
    public void productHistoryBeforePurchase() {
        assertNull(statService.sortCustomersByItemForStore(sellerId, storeId));
    }

    @Test
    public void productHistoryAfterPurchase() {
        transactionService.addFunds(buyerId, 100.00);
        transactionService.addToCart(buyerId, productId, storeId,1,10.00);
        transactionService.placeOrder(buyerId);
        assertNotNull(statService.sortProductsBySalesForStore(buyerId, storeId));
    }

    @After
    public void cleanup() {
        storeService.removeProductFromProducts(productId);
        storeService.removeStore(storeId);
        accountService.removeAccount(buyerId);
        accountService.removeAccount(sellerId);
    }

}
