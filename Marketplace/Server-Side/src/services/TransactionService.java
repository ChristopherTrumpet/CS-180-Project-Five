package services;

import org.json.JSONArray;
import org.json.JSONObject;

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


    public boolean removeFromCart(String userId, String productId, int quantity) {
        if (accountService.isBuyer(userId))
        {
            JSONArray cart = accountService.getUserById(userId).getJSONArray("cart");

            for (int i = 0; i < cart.length(); i++) {
                JSONObject product = (JSONObject) cart.get(i);
                System.out.println(product);
                String currProductId = (String) product.get("product_id");
                if (currProductId.equals(productId))
                {
                    if ((Integer) product.get("quantity") <= quantity)
                        cart.remove(i);
                    else
                    {
                        product.put("quantity", quantity);
                        cart.put(product);
                    }
                    return accountService.updateUserDetails(userId, "cart", cart);
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

    public boolean placeOrder(String userId) {

        JSONObject users = new JSONObject(accountService.getJSONFile(accountService.getUserFileDirectory()));

        for (int i = 0; i < users.getJSONArray("users").length(); i++) {
            JSONObject user = (JSONObject) users.getJSONArray("users").get(i);
            if (user.get("id").toString().equals(userId)) {

                double totalCost = 0.0;

                for (Object product : user.getJSONArray("cart")) {
                    JSONObject productObj = (JSONObject) product;
                    totalCost += productObj.getDouble("price") * Double.parseDouble(String.valueOf(productObj.getInt("quantity")));
                }

                if (user.getDouble("funds") >= totalCost) {
                    user.getJSONArray("product_history").putAll(user.getJSONArray("cart"));
                    user.getJSONArray("cart").clear();
                    user.put("funds", user.getDouble("funds") - totalCost);
                }

                users.getJSONArray("users").put(i, user);
                return accountService.writeJSONObjectToFile(users, accountService.getUserFileDirectory());
            }
        }

        return false;

    }

}
