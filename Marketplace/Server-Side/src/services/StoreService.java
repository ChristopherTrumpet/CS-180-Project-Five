package services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Paths;
import java.util.Objects;
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

    public String getStoreFileDirectory() {
        return storeFileDirectory;
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
            System.out.println(((JSONObject) store).getString("name"));
            if (((JSONObject) store).getString("name").equals(storeName)) {
                System.out.println("Found store!");
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


    public void importProducts(String filePath) {
        CSVtoJSON converter = new CSVtoJSON(filePath);
        String jsonContents = converter.convert();
        JSONArray productList = new JSONArray(jsonContents);

        for (Object product : productList) {
            JSONObject productObj = (JSONObject) product;
            createProduct(productObj.getString("name"), productObj.getString("description"));
            String storeName = ((JSONObject) product).getString("store");

            JSONObject storeObj =  new JSONObject(accountService.getJSONFile(getStoreFileDirectory()));
            JSONArray storeArray = storeObj.getJSONArray("stores");
            JSONObject store = new JSONObject();

            if (getStoreByName(storeName) == null) {
                store.put("name", storeName);
                store.put("id", generateStoreId());
                store.put("sales", 0.0);
                store.put("products", new JSONArray());
                storeArray.put(store);
                storeObj.put("stores", storeArray);
                writeJSONObjectToFile(storeObj, getStoreFileDirectory());
            }

            if (getStoreByName(storeName) != null) {

                int index = 0;

                for (int i = 0; i < storeArray.length(); i++) {
                    JSONObject storeItem = (JSONObject) storeArray.get(i);
                    if (storeItem.getString("name").equals(storeName))
                    {
                        store = storeItem;
                        index = i;
                    }
                }

                JSONArray products = store.getJSONArray("products");
                JSONObject newProduct = new JSONObject();
                newProduct.put("price", productObj.getDouble("price"));
                newProduct.put("qty", productObj.getInt("quantity"));
                String id = getProductByName(productObj.getString("name")).getString("product_id");
                newProduct.put("id", id);
                products.put(newProduct);
                store.put("products", products);
                storeArray.put(index, store);
                storeObj.put("stores", storeArray);
                writeJSONObjectToFile(storeObj, getStoreFileDirectory());
            }
        }

        System.out.println("Added products!");

    }


    public JSONObject getProduct(String productId) {
        for (Object store : new JSONObject(Objects.requireNonNull(getProductFile())).getJSONArray("products")) {
            if (((JSONObject) store).get("id").toString().equals(productId)) {
                return (JSONObject) store;
            }
        }
        return null;
    }

    public JSONObject  getProductByName(String productName) {
        for (Object product : new JSONObject(getProductFile()).getJSONArray("products")) {
            if (((JSONObject) product).getString("name").equals(productName))
                return (JSONObject) product;
        }
        return null;
    }

    public boolean addProduct(String storeId, String productId, int qty, double price) {

        JSONObject storeObj = new JSONObject(getStoreFile());
        JSONArray stores = storeObj.getJSONArray("stores");
        JSONObject newStore = new JSONObject();

        if (getStoreById(storeId) == null) {
            newStore.put("id", productId);
            newStore.put("price", price);
            newStore.put("qty", qty);
            stores.put(newStore);
            storeObj.put("stores", stores);
            return writeJSONObjectToFile(storeObj, productFileDirectory);
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

    public boolean createProduct(String name, String description) {

        JSONObject productObj = new JSONObject(getProductFile());
        JSONArray products = productObj.getJSONArray("products");
        JSONObject product = new JSONObject();
        String product_id = generateProductId();

        if (getProductByName(name) == null) {
            product.put("product_id", product_id);
            product.put("name", name);
            product.put("description", description);
            products.put(product);
            productObj.put("products", products);
            return writeJSONObjectToFile(productObj, productFileDirectory);
        }
        return false;
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
