package services;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.SortedMap;

public class TransactionService {

    private final AccountService accountService = new AccountService();

    public boolean addToCart(String userId, String productId, String storeId, int quantity, double price) {

        // TODO: Do NOT allow duplicates!

        if (accountService.isBuyer(userId))
        {
            JSONArray cart = accountService.getUserById(userId).getJSONArray("cart");
            JSONObject product = new JSONObject();

            product.put("product_id", productId);
            product.put("store_id", storeId);
            product.put("quantity", quantity);
            product.put("price", price);

            cart.put(product);
            return accountService.updateUserDetails(userId, "cart", cart);
        }
        return false;
    }


    public boolean removeFromCart(String userId, String productName, String storeId) {
        if (accountService.isBuyer(userId))
        {
            JSONArray cart = accountService.getUserById(userId).getJSONArray("cart");
            StoreService ss = new StoreService();
            String productId = ss.getProductByName(productName).getString("id");
            for (int i = 0; i < cart.length(); i++) {
                JSONObject product = (JSONObject) cart.get(i);
                String currProductId = product.getString("product_id");

                if (currProductId.equals(productId) && product.getString("store_id").equals(storeId))
                {
                    cart.remove(i);
                    System.out.println("Updating cart...");
                    return accountService.updateCart(userId, cart);
                }
            }
        }

        return false;
    }

    public boolean clearCart(String userId) {
        if (accountService.isBuyer(userId))
        {
            JSONArray cart = accountService.getUserById(userId).getJSONArray("cart");
            cart.clear();

            return accountService.updateUserDetails(userId, "cart", cart);
        }

        return false;
    }

    public String getCartContents(String userId) {

        // TODO: Get actual product name and improve formatting instead of displaying raw data

        if (accountService.isBuyer(userId))
        {
            JSONArray cart = accountService.getUserById(userId).getJSONArray("cart");
            return cart.isEmpty() ? "Cart is currently empty..." : cart.toString();
        }

        return null;
    }

    public boolean addFunds(String buyerId, double newBalance) {
        JSONObject users = new JSONObject(accountService.getJSONFile(accountService.getUserFileDirectory()));

        for (int i = 0; i < users.getJSONArray("users").length(); i++) {
            JSONObject user = (JSONObject) users.getJSONArray("users").get(i);
            if (user.getString("id").equals(buyerId)) {
                user.put("funds", user.getDouble("funds") + newBalance);
                users.getJSONArray("users").put(i, user);
                return accountService.writeJSONObjectToFile(users, accountService.getUserFileDirectory());
            }
        }

        return false;
    }

    public String getUser(String buyerId) {
        JSONObject user = accountService.getUserById(buyerId);
        return user.toString();
    }

    public boolean exportProductHistory(String buyerId, String filePath) {

        JSONObject buyer = accountService.getUserById(buyerId);
        String csvString = CDL.toString(buyer.getJSONArray("product_history"));

        try {
            FileWriter file = new FileWriter(filePath+".csv");
            file.write(csvString);
            file.flush();
            file.close();
            return true;

        } catch (IOException e) {
            System.out.println("Error occurred writing json object to file...\n" + e.getMessage());
        }

        return false;
    }

    public boolean placeOrder(String userId) {

        StoreService ss = new StoreService();
        JSONObject stores = new JSONObject(ss.getStoreFile());
        JSONObject users = new JSONObject(accountService.getJSONFile(accountService.getUserFileDirectory()));

        for (int i = 0; i < users.getJSONArray("users").length(); i++) {
            JSONObject user = (JSONObject) users.getJSONArray("users").get(i);
            if (user.get("id").toString().equals(userId)) {

                double totalCost = 0.0;

                for (Object product : user.getJSONArray("cart")) {
                    JSONObject productObj = (JSONObject) product;
                    for (int j = 0; j < stores.getJSONArray("stores").length(); j++) {
                        JSONObject store = (JSONObject) stores.getJSONArray("stores").get(j);
                        if (store.getString("id").equals(productObj.getString("store_id"))) {
                            for (Object storeProduct : store.getJSONArray("products")) {
                                if (((JSONObject) storeProduct).getString("id").equals(productObj.getString("product_id"))) {
                                    ((JSONObject) storeProduct).put("qty", ((JSONObject) storeProduct).getInt("qty") - productObj.getInt("quantity"));
                                    System.out.println(store);
                                }
                            }
                        }

                        stores.getJSONArray("stores").put(j,store);
                        ss.writeJSONObjectToFile(stores, ss.getStoreFileDirectory());
                    }
                    totalCost += productObj.getDouble("price") * Double.parseDouble(String.valueOf(productObj.getInt("quantity")));
                }

                if (user.getDouble("funds") >= totalCost) {
                    user.getJSONArray("product_history").putAll(user.getJSONArray("cart"));
                    user.getJSONArray("cart").clear();
                    user.put("funds", round((user.getDouble("funds") - totalCost), 2));
                }

                users.getJSONArray("users").put(i, user);
                return accountService.writeJSONObjectToFile(users, accountService.getUserFileDirectory());
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
