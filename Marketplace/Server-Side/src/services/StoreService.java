package services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class StoreService {

    private final String storeFileDirectory;

    public StoreService() {

        this.storeFileDirectory = Paths.get(System.getProperty("user.dir") + "\\data\\stores.json").toString();

    }

    public boolean addStore(String userId, String storeId) {
        AccountService as = new AccountService();
        if (as.isSeller(userId)) {
            JSONObject seller = as.getUserById(userId);
            JSONArray idList = (JSONArray) seller.get("stores");
            idList.put(storeId);
            seller.put("stores", idList);
            return true;
        }
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
        return false;
    }
    public boolean addProduct(String storeId, String productId, int qty, double price) {
        JSONObject product = new JSONObject();
        product.put("productID", productId);
        product.put("qty", qty);
        AccountService as = new AccountService();

        return as.writeJSONObjectToFile(product, storeFileDirectory);

    }

    public boolean removeProduct(String storeId, String productId) {
        boolean removed = false;
        JSONArray storeList = (JSONArray) new JSONObject("stores.json").get("stores");
        JSONObject storeObj = new JSONObject();
        for(int i = 0; i < storeList.length(); i++) {
            JSONObject store = (JSONObject) storeList.get(i);
            if(store.get("id").equals(storeId)) {
                storeObj = store;
                break;
            }
        }
        JSONArray productList = (JSONArray) storeObj.get("products");
        for(int i = 0; i < productList.length(); i++) {
            JSONObject product = (JSONObject) productList.get(i);
            if(product.get("id").equals(productId)) {
                productList.remove(i);
                removed = true;
                break;
            }
        }
        JSONArray products = (JSONArray) new JSONObject("products.json").get("products");
        for(int i = 0; i < products.length(); i++) {
            JSONObject product = (JSONObject) products.get(i);
            if(product.get("id").equals(productId)) {
                products.remove(i);
                removed = true;
                break;
            }
        }
        return removed;
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

    public boolean updateProductName(String storeId, String productId, String newName) {
        JSONObject stores = new JSONObject(Objects.requireNonNull(getStoreFile()));

        for (Object store : stores.getJSONArray("stores")) {
            if (((JSONObject) store).get("id").toString().equals(storeId)) {
                for (Object product : ((JSONObject) store).getJSONArray("products")) {
                    if (((JSONObject) product).get("id").toString().equals(productId)) {
                        ((JSONObject) product).put("name", newName);
                        writeJSONObjectToFile(stores, storeFileDirectory);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean updateProductDescription(String storeId, String productId, String newDescription) {
        JSONObject stores = new JSONObject(Objects.requireNonNull(getStoreFile()));

        for (Object store : stores.getJSONArray("stores")) {
            if (((JSONObject) store).get("id").toString().equals(storeId)) {
                for (Object product : ((JSONObject) store).getJSONArray("products")) {
                    if (((JSONObject) product).get("id").toString().equals(productId)) {
                        ((JSONObject) product).put("description", newDescription);
                        writeJSONObjectToFile(stores, storeFileDirectory);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean updateProductPrice(String storeId, String productId, double newPrice) {
        JSONObject stores = new JSONObject(Objects.requireNonNull(getStoreFile()));

        for (Object store : stores.getJSONArray("stores")) {
            if (((JSONObject) store).get("id").toString().equals(storeId)) {
                for (Object product : ((JSONObject) store).getJSONArray("products")) {
                    if (((JSONObject) product).get("id").toString().equals(productId)) {
                        ((JSONObject) product).put("price", newPrice);
                        writeJSONObjectToFile(stores, storeFileDirectory);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean updateProductQuantity(String storeId, String productId, int newQuantity) {
        JSONObject stores = new JSONObject(Objects.requireNonNull(getStoreFile()));

        for (Object store : stores.getJSONArray("stores")) {
            if (((JSONObject) store).get("id").toString().equals(storeId)) {
                for (Object product : ((JSONObject) store).getJSONArray("products")) {
                    if (((JSONObject) product).get("id").toString().equals(productId)) {
                        ((JSONObject) product).put("qty", newQuantity);
                        writeJSONObjectToFile(stores, storeFileDirectory);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public String generateProductId() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; // Potential characters for id
        StringBuilder productId = new StringBuilder();
        Random rnd = new Random();

        while (productId.length() < 18) { // Creates a product id 18 characters long
            int index = (int) (rnd.nextFloat() * chars.length());
            productId.append(chars.charAt(index));
        }

        return productId.toString();
    }

    public String generateStoreId() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; // Potential characters for id
        StringBuilder storeId = new StringBuilder();
        Random rnd = new Random();

        while (storeId.length() < 20) { // Creates a store id 20 characters long
            int index = (int) (rnd.nextFloat() * chars.length());
            storeId.append(chars.charAt(index));
        }

        return storeId.toString();
    }

    public JSONObject getStoreById(String storeId) {
        for (Object store : new JSONObject(Objects.requireNonNull(getStoreFile())).getJSONArray("stores")) {
            if (((JSONObject) store).get("id").toString().equals(storeId)) {
                return (JSONObject) store;
            }
        }
        return null;
    }

    private String getStoreFile() {
        try {
            System.out.println(storeFileDirectory);
            return Files.readString(Path.of(storeFileDirectory));
        } catch (IOException e) {
            System.out.println("Error occurred retrieving store file...");
            e.printStackTrace();
        }

        return null;
    }

    public boolean writeJSONObjectToFile(JSONObject userObj, String fileDirectory) {

        try {

            FileWriter file = new FileWriter(fileDirectory);
            file.write(userObj.toString());
            file.flush();
            file.close();
            return true;

        } catch (IOException e) {
            System.out.println("Error occurred writing json object to file...\n" + e.getMessage());
        }

        return false;
    }
}
