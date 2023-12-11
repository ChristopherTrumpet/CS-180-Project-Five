package services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

/**
 * StoreService
 * <p>
 * handles data and back end operations regarding stores.
 *
 * @author Chris Trumpet, Matthew Lee, Mohit Ambe, Shrinand Perumal, Vraj Patel
 * @version December 11, 2023
 */
public class StoreService {

    private final String storeFileDirectory;
    private final String productFileDirectory;
    private final AccountService as = new AccountService();


    public StoreService() {

        this.storeFileDirectory = Paths.get(System.getProperty("user.dir") + "/data/stores.json").toString();
        this.productFileDirectory = Paths.get(System.getProperty("user.dir") + "/data/products.json").toString();

    }

    public String getStoreFileDirectory() {
        return storeFileDirectory;
    }

    public String getProductFileDirectory() {
        return productFileDirectory;
    }

    public void addStoreToSeller(String userId, String storeId) {

        JSONObject users = as.getJSONFromFile(as.userFileDirectory);

        for (Object user : users.getJSONArray("users")) {
            JSONObject userObj = (JSONObject) user;
            if (userObj.getString("id").equals(userId)) {
                userObj.getJSONArray("stores").put(storeId);
            }
        }

        writeJSONObjectToFile(users, as.getUserFileDirectory());
    }

    public boolean removeStoreFromSeller(String storeId, String sellerId) {

        AccountService as = new AccountService();
        JSONObject users = as.getJSONFromFile(as.getUserFileDirectory());

        for (Object user : users.getJSONArray("users")) {
            JSONObject userObj = (JSONObject) user;

            if (userObj.getString("id").equals(sellerId)) {

                JSONArray stores = userObj.getJSONArray("stores");

                for (int i = 0; i < stores.length(); i++) {

                    String userStoreId = stores.get(i).toString();

                    if (storeId.equals(userStoreId)) {
                        removeStore(storeId);
                        stores.remove(i);
                        return as.writeJSONObjectToFile(users, as.getUserFileDirectory());
                    }
                }
            }
        }

        return false;
    }

    public void removeStore(String storeId) {

        JSONObject storesObj = as.getJSONFromFile(storeFileDirectory);
        JSONArray stores = storesObj.getJSONArray("stores");

        for (int i = 0; i < stores.length(); i++) {

            JSONObject store = (JSONObject) stores.get(i);

            if (store.getString("id").equals(storeId)) {
                stores.remove(i);
                storesObj.put("stores", stores);
                writeJSONObjectToFile(storesObj, storeFileDirectory);
            }
        }
    }

    public boolean createStore(String sellerId, String name) {
        String store_id = generateStoreId();

        JSONObject stores = as.getJSONFromFile(storeFileDirectory);
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
        JSONObject stores = as.getJSONFromFile(storeFileDirectory);
        for (Object store : stores.getJSONArray("stores")) {
            if (((JSONObject) store).getString("name").equals(storeName)) {
                return (JSONObject) store;
            }
        }
        return null;
    }

    public boolean updateStoreName(String storeId, String newName) {
        JSONObject stores = as.getJSONFromFile(storeFileDirectory);

        for (Object store : stores.getJSONArray("stores")) {
            if (((JSONObject) store).get("id").toString().equals(storeId)) {
                ((JSONObject) store).put("name", newName);
                writeJSONObjectToFile(stores, storeFileDirectory);
                return true;
            }
        }

        return false;
    }


    public void importProducts(JSONObject seller, String filePath) {

        CSVtoJSON converter = new CSVtoJSON(filePath);
        String jsonContents = converter.convert();
        JSONArray productList = new JSONArray(jsonContents);
        JSONArray storeIdList = new JSONArray();

        storeIdList.putAll(seller.getJSONArray("stores"));

        for (Object product : productList) {
            JSONObject productObj = (JSONObject) product;
            createProduct(productObj.getString("name"), productObj.getString("description"));
            String storeName = ((JSONObject) product).getString("store");

            JSONObject storeObj = as.getJSONFromFile(storeFileDirectory);
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
                    if (storeItem.getString("name").equals(storeName)) {
                        store = storeItem;
                        index = i;
                    }
                }

                JSONArray products = store.getJSONArray("products");
                JSONObject newProduct = new JSONObject();
                newProduct.put("price", productObj.getDouble("price"));
                newProduct.put("qty", productObj.getInt("quantity"));
                String id = getProduct("name", productObj.getString("name")).getString("product_id");
                newProduct.put("id", id);
                newProduct.put("sales", 0.0);
                products.put(newProduct);
                store.put("products", products);
                storeArray.put(index, store);
                storeObj.put("stores", storeArray);
                storeIdList.put(store.getString("id"));
                writeJSONObjectToFile(storeObj, getStoreFileDirectory());
            }
        }

        as.updateUserDetails(seller.getString("id"), "stores", storeIdList);
    }


    public JSONObject getProduct(String identifier, String value) {
        for (Object product : as.getJSONFromFile(productFileDirectory).getJSONArray("products")) {
            if (((JSONObject) product).getString(identifier).equals(value)) return (JSONObject) product;
        }
        return null;
    }

    public JSONObject getStoreProduct(String storeId, String productId) {

        JSONObject store = getStoreById(storeId);
        JSONArray storeProducts = store.getJSONArray("products");

        for (Object storeProduct : storeProducts) {
            if (((JSONObject) storeProduct).getString("id").equals(productId)) return (JSONObject) storeProduct;
        }

        return null;
    }

    public JSONObject getStoreProduct(String productId) {

        JSONObject stores = as.getJSONFromFile(storeFileDirectory);

        for (Object store : stores.getJSONArray("stores")) {
            for (Object product : ((JSONObject) store).getJSONArray("products")) {
                if (((JSONObject) product).getString("id").equals(productId)) return (JSONObject) product;
            }
        }

        return null;
    }

    public boolean addProduct(String storeId, String productId, int qty, double price) {

        JSONObject storeObj = as.getJSONFromFile(storeFileDirectory);
        JSONArray stores = storeObj.getJSONArray("stores");

        for (int i = 0; i < stores.length(); i++) {
            JSONObject store = stores.getJSONObject(i);
            if (store.getString("id").equals(storeId)) {
                JSONArray products = store.getJSONArray("products");
                JSONObject newProduct = new JSONObject();
                newProduct.put("id", productId);
                newProduct.put("price", price);
                newProduct.put("qty", qty);
                newProduct.put("sales", 0.0);
                products.put(newProduct);
                store.put("products", products);
                stores.put(i, store);
                storeObj.put("stores", stores);
                return writeJSONObjectToFile(storeObj, storeFileDirectory);

            }
        }

        return false;
    }

    public boolean removeProduct(String storeId, String productId) {

        JSONObject stores = as.getJSONFromFile(storeFileDirectory);
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

    public void removeProductFromProducts(String productId) {

        JSONObject productsObj = as.getJSONFromFile(productFileDirectory);
        JSONArray products = productsObj.getJSONArray("products");

        for (int i = 0; i < products.length(); i++) {

            JSONObject product = (JSONObject) products.get(i);

            if (product.getString("product_id").equals(productId)) {
                products.remove(i);
                productsObj.put("products", products);
                writeJSONObjectToFile(productsObj, productFileDirectory);
            }
        }
    }

    public JSONObject createProduct(String name, String description) {

        JSONObject productObj = as.getJSONFromFile(productFileDirectory);
        JSONArray products = productObj.getJSONArray("products");
        JSONObject product = new JSONObject();
        String product_id = generateProductId();

        if (getProduct("name", name) == null) {
            product.put("product_id", product_id);
            product.put("name", name);
            product.put("description", description);

            products.put(product);
            productObj.put("products", products);
            writeJSONObjectToFile(productObj, productFileDirectory);
            return product;
        }

        return null;
    }

    public boolean updateStoreProduct(String storeId, String productId, String type, String value) {
        JSONObject stores = as.getJSONFromFile(storeFileDirectory);

        for (Object store : stores.getJSONArray("stores")) {
            if (((JSONObject) store).get("id").toString().equals(storeId)) {
                for (Object product : ((JSONObject) store).getJSONArray("products")) {
                    if (((JSONObject) product).get("id").toString().equals(productId)) {
                        switch (type) {
                            case "quantity" -> ((JSONObject) product).put("qty", Integer.parseInt(value));
                            case "price" -> ((JSONObject) product).put("price", Double.parseDouble(value));
                        }
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
        for (Object store : as.getJSONFromFile(storeFileDirectory).getJSONArray("stores")) {
            System.out.println(store);
            if (((JSONObject) store).getString("id").equals(storeId)) {
                System.out.println(store);
                return (JSONObject) store;
            }
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
