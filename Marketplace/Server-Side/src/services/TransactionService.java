package services;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class TransactionService {

    private final AccountService as = new AccountService();

    public boolean addToCart(String userId, String productId, String storeId, int quantity, double price) {

        if (as.isBuyer(userId))
        {
            JSONArray cart = as.getUser("id", userId).getJSONArray("cart");
            JSONObject product = new JSONObject();

            product.put("product_id", productId);
            product.put("store_id", storeId);
            product.put("quantity", quantity);
            product.put("price", price);

            cart.put(product);
            return as.updateUserDetails(userId, "cart", cart);
        }
        return false;
    }


    public boolean removeFromCart(String userId, String productName, String storeId) {
        if (as.isBuyer(userId))
        {
            JSONArray cart = as.getUser("id", userId).getJSONArray("cart");
            StoreService ss = new StoreService();
            String productId = ss.getProduct(productName).getString("product_id");
            for (int i = 0; i < cart.length(); i++) {
                JSONObject product = (JSONObject) cart.get(i);
                String currProductId = product.getString("product_id");

                if (currProductId.equals(productId) && product.getString("store_id").equals(storeId))
                {
                    cart.remove(i);
                    System.out.println("Updating cart...");
                    return as.updateUserDetails(userId, "cart", cart);
                }
            }
        }

        return false;
    }

    public boolean clearCart(String userId) {
        if (as.isBuyer(userId))
        {
            JSONArray cart = as.getUser("id", userId).getJSONArray("cart");
            cart.clear();

            return as.updateUserDetails(userId, "cart", cart);
        }

        return false;
    }

    public boolean addFunds(String buyerId, double newBalance) {
        JSONObject users = new JSONObject(as.getJSONFromFile(as.getUserFileDirectory()));

        for (int i = 0; i < users.getJSONArray("users").length(); i++) {
            JSONObject user = (JSONObject) users.getJSONArray("users").get(i);
            if (user.getString("id").equals(buyerId)) {
                user.put("funds", user.getDouble("funds") + newBalance);
                users.getJSONArray("users").put(i, user);
                return as.writeJSONObjectToFile(users, as.getUserFileDirectory());
            }
        }

        return false;
    }

    public boolean exportProductHistory(String buyerId, String filePath) {

        JSONObject buyer = as.getUser("id", buyerId);
        String csvString = CDL.toString(buyer.getJSONArray("product_history"));

        try (FileWriter fileWriter = new FileWriter(filePath+".csv")) {

            fileWriter.write(csvString);
            fileWriter.flush();
            fileWriter.close();
            return true;

        } catch (IOException e) {
            System.out.println("Error occurred exporting product file...\n" + e.getMessage());
        }

        return false;
    }

    public boolean placeOrder(String userId) {

        StoreService ss = new StoreService();
        JSONObject stores = as.getJSONFromFile(ss.getStoreFileDirectory());
        JSONObject users = as.getJSONFromFile(as.userFileDirectory);

        for (int i = 0; i < users.getJSONArray("users").length(); i++) {
            JSONObject user = (JSONObject) users.getJSONArray("users").get(i);

            // Retrieves user
            if (user.get("id").toString().equals(userId)) {

                double totalCost = 0.0;

                // Loops through user cart
                for (Object product : user.getJSONArray("cart")) {

                    // Stores reference to current indexed product
                    JSONObject productObj = (JSONObject) product;

                    // Loops through the stores database
                    for (int j = 0; j < stores.getJSONArray("stores").length(); j++) {

                        // Stores reference to current indexed store
                        JSONObject store = (JSONObject) stores.getJSONArray("stores").get(j);

                        // Conditional for if the store reference id is identical to the store id of the product
                        if (store.getString("id").equals(productObj.getString("store_id"))) {

                            // Loops through the stores products
                            for (Object storeProduct : store.getJSONArray("products")) {

                                // Conditional for if the product id is identical to the stores product id
                                if (((JSONObject) storeProduct).getString("id").equals(productObj.getString("product_id"))) {

                                    // Checks if store has sufficient quantity
                                    if (((JSONObject) storeProduct).getInt("qty") >= productObj.getInt("quantity")){
                                        System.out.println("Product in stock");
                                        ((JSONObject) storeProduct).put("qty", ((JSONObject) storeProduct).getInt("qty") - productObj.getInt("quantity"));
                                        store.put("sales", productObj.getDouble("price") * Double.parseDouble(String.valueOf(productObj.getInt("quantity"))));
                                    } else {
                                        return false;
                                    }
                                }
                            }
                        }

                        // Updates stores object
                        stores.getJSONArray("stores").put(j,store);

                    }

                    totalCost += productObj.getDouble("price") * Double.parseDouble(String.valueOf(productObj.getInt("quantity")));
                }

                users.getJSONArray("users").put(i, user);

                // Sufficient funds check
                if (user.getDouble("funds") >= totalCost) {
                    user.getJSONArray("product_history").putAll(user.getJSONArray("cart"));
                    user.getJSONArray("cart").clear();
                    user.put("funds", round((user.getDouble("funds") - totalCost), 2));
                    ss.writeJSONObjectToFile(stores, ss.getStoreFileDirectory());
                    return as.writeJSONObjectToFile(users, as.getUserFileDirectory());
                } else {
                    return false;
                }
            }
        }

        return false;

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
