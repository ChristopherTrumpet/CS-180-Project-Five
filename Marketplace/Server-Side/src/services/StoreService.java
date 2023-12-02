package services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;

public class StoreService {
    private String storeFileDirectory;

    public StoreService() {

        this.storeFileDirectory = Paths.get(System.getProperty("user.dir") + "\\data\\stores.json").toString();

    }


    public boolean addStore(String storeId) {

        return false;
    }

    public boolean removeStore(String storeId) {

        return false;
    }

    public boolean createStore(String name) {
        String store_id = generateStoreId();
        JSONObject StoreObj = new JSONObject();
        JSONObject store = new JSONObject();
        JSONArray products = new JSONArray();
        store.put("store_id", store_id);
        store.put("name", name);
        store.put("products", products);
        AccountService as = new AccountService();
        return as.writeJSONObjectToFile(store, storeFileDirectory);
    }

    public boolean updateStoreName(String storeId, String newName) {

        return false;
    }

    public boolean importProducts(File csvFile) {

        return false;
    }

    public boolean exportProducts() {

        return false;
    }

    public boolean createProduct(String productId, int qty, double price) {
//        JSONObject product = new JSONObject();
//        product.put("productID", productId);
//        product.put("qty", qty);
//        product.put("price", price);
//        AccountService as = new AccountService();
//        return as.writeJSONObjectToFile(product, storeFileDirectory);
//
//        return false;
    }
    public boolean addProduct(String storeId, String productId, int qty, double price) {
        JSONObject product = new JSONObject();
        product.put("productID", productId);
        product.put("qty", qty);
        AccountService as = new AccountService();

        return as.writeJSONObjectToFile(product, storeFileDirectory);

    }

    public boolean removeProduct(String storeId, String productId) {
        JSONObject storeObj = new JSONObject(Objects.requireNonNull())
        return false;
    }

    public boolean createProduct(String name, String description, String itemType) {
        JSONObject product = new JSONObject();
        String product_id = generateProductId();
        product.put("product_id", product_id);
        product.put("name", name);
        product.put("description", description);
        product.put("item", itemType);
        String productFileDirectory = Paths.get(System.getProperty("user.dir") + "\\data\\stores.json").toString();
        AccountService as =  new AccountService();
        return as.writeJSONObjectToFile(product, productFileDirectory);
    }

    public boolean updateProductName(String productId, String newName) {

        return false;
    }

    public boolean updateProductDescription(String productId, String newDescription) {

        return false;
    }

    public boolean updateProductPrice(String storeId, String productId, double newPrice) {

        return false;
    }

    public boolean updateProductQuantity(String storeId, String productId, int newQuantity) {

        return false;
    }

    public String generateProductId() {

        return null;
    }

    public String generateStoreId() {

        return null;
    }
}
