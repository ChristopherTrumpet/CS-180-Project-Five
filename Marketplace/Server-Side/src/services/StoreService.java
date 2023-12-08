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
    private final String productFileDirectory;
    private final AccountService accountService = new AccountService();


    public StoreService() {

        this.storeFileDirectory = Paths.get(System.getProperty("user.dir") + "/Marketplace/Server-Side/data/stores.json").toString();
        this.productFileDirectory = Paths.get(System.getProperty("user.dir") + "/Marketplace/Server-Side/data/products.json").toString();

    }

    public boolean addStoreToSeller(String userId, String storeId) {

        JSONObject users = new JSONObject(Objects.requireNonNull(accountService.getJSONFile(accountService.getUserFileDirectory())));

        for (Object user : users.getJSONArray("users")) {
            JSONObject userObj = (JSONObject) user;
            if (userObj.getString("id").equals(userId)) {
                userObj.getJSONArray("stores").put(storeId);
            }
        }

        return writeJSONObjectToFile(users, accountService.getUserFileDirectory());
    }

    public boolean removeStore(String storeId, String userId) {

        AccountService as = new AccountService();
        JSONObject users = new JSONObject(as.getJSONFile(as.getUserFileDirectory()));

        for (Object user : users.getJSONArray("users")) {
            JSONObject userObj = (JSONObject) user;
            if (userObj.getString("id").equals(userId)) {
                JSONArray stores = userObj.getJSONArray("stores");
                for (int i = 0; i < stores.length(); i++) {
                    String userStoreId = (String) stores.get(i);
                    if (storeId.equals(userStoreId)) {
                        System.out.println("Found store!");
                        stores.remove(i);
                        as.writeJSONObjectToFile(users, as.getUserFileDirectory());
                        return true;
                    }
                }
            }
        }

        return false;
    }
    public boolean checkQuantity(String storeId, String productId, int quantity) {
        JSONObject store = getStoreById(storeId);
        for (Object product : store.getJSONArray("products")) {
            if (((JSONObject) product).getString("id").equals(productId)) {
                JSONObject theProduct = (JSONObject) product;
                if (theProduct.getInt("qty") < 1) {
                    return false;
                } else if (theProduct.getInt("qty") >= quantity) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean createStore(String sellerId, String name) {
        String store_id = generateStoreId();

        JSONObject stores = new JSONObject(Objects.requireNonNull(getStoreFile()));
        JSONObject store = new JSONObject();

        store.put("id", store_id);
        store.put("name", name);
        store.put("sales", 0.0);
        store.put("products", new JSONArray());
        stores.getJSONArray("stores").put(store);

        addStoreToSeller(sellerId, store.getString("id"));

        return writeJSONObjectToFile(stores, storeFileDirectory);
    }

    public JSONObject getStoreByName(String storeName) {
        JSONObject stores = new JSONObject(accountService.getJSONFile(storeFileDirectory));
        for (Object store : stores.getJSONArray("stores")) {
            if (((JSONObject) store).get("name").toString().equals(storeName)) {
                return (JSONObject) store;
            }
        }
        return null;
    }

    public boolean updateStoreName(String storeId, String newName) {
        JSONObject stores = new JSONObject(Objects.requireNonNull(getStoreFile()));

        for (Object store : stores.getJSONArray("stores")) {
            if (((JSONObject) store).get("id").toString().equals(storeId)) {
                ((JSONObject) store).put("name", newName);
                writeJSONObjectToFile(stores, storeFileDirectory);
                return true;
            }
        }

        return false;
    }


    public boolean importProducts(File csvFile) {

        return false;
    }

    public boolean exportProducts(String storeId, File exportToFile) {
        JSONObject store = getStoreById(storeId);
        JSONArray products = store.getJSONArray("products");
        String fileContents = "";
        for (Object product : products) {
            JSONObject productObj = getProduct((String) product);
            fileContents.concat(String.format("%s, %s, %s", productObj.get("name"), productObj.get("description"), productObj.get("item")));
        }

        try {

            FileWriter file = new FileWriter(exportToFile);
            file.write(fileContents);
            file.flush();
            file.close();
            return true;

        } catch (IOException e) {
            System.out.println("Error occurred writing json object to file...\n" + e.getMessage());
        }

        return false;
    }

    public JSONObject getProduct(String productId) {
        for (Object store : new JSONObject(Objects.requireNonNull(getProductFile())).getJSONArray("products")) {
            if (((JSONObject) store).get("id").toString().equals(productId)) {
                return (JSONObject) store;
            }
        }
        return null;
    }

    public boolean addProduct(String storeId, String productId, int qty, double price) {

        JSONObject stores = new JSONObject(Objects.requireNonNull(getStoreFile()));
        JSONArray storeArray = stores.getJSONArray("stores");

        for (Object store : storeArray) {
            if (((JSONObject) store).get("id").toString().equals(storeId)) {
                JSONObject product = new JSONObject();
                product.put("id", productId);
                product.put("qty", qty);
                product.put("price", price);
                ((JSONObject) store).getJSONArray("products").put(product);
                stores.put("stores", storeArray);
                return writeJSONObjectToFile(stores, storeFileDirectory);
            }
        }
        return false;
    }

    public boolean removeProduct(String storeId, String productId) {

        JSONObject stores = new JSONObject(Objects.requireNonNull(getStoreFile()));
        JSONArray storeArray = stores.getJSONArray("stores");
        for (Object store : storeArray) {
            if (((JSONObject) store).get("id").toString().equals(storeId)) {
                JSONArray storeProducts = ((JSONObject) store).getJSONArray("products");
                for (int i = 0; i < storeProducts.length(); i++) {
                    JSONObject product = (JSONObject) storeProducts.get(i);
                    if (product.getString("id").equals(productId)) {
                        ((JSONObject) store).getJSONArray("products").remove(i);
                        stores.put("stores", storeArray);
                        return writeJSONObjectToFile(stores, storeFileDirectory);
                    }
                }
            }
        }

        return false;
    }

    public boolean createProduct(String storeId, String name, String description, String itemType) {

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

    public String getStoreFile() {
        System.out.println(storeFileDirectory);
        try {
            return Files.readString(Path.of(storeFileDirectory));
        } catch (IOException e) {
            System.out.println("Error occurred retrieving store file...");
            e.printStackTrace();
        }

        return null;
    }

    public String getProductFile() {
        try {
            return Files.readString(Path.of(productFileDirectory));
        } catch (IOException e) {
            System.out.println("Error occurred retrieving store file...");
            e.printStackTrace();
        }

        return null;
    }

    public boolean writeJSONObjectToFile(JSONObject jsonObj, String fileDirectory) {

        try {

            FileWriter file = new FileWriter(fileDirectory);
            file.write(jsonObj.toString());
            file.flush();
            file.close();
            return true;

        } catch (IOException e) {
            System.out.println("Error occurred writing json object to file...\n" + e.getMessage());
        }

        return false;
    }
}
