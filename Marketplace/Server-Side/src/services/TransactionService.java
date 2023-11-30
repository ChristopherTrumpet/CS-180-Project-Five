package services;

import org.json.JSONArray;
import org.json.JSONObject;

public class TransactionService {

    private final AccountService accountService = new AccountService();

    public boolean addToCart(String userId, String productId, String storeId, int quantity) {

        // TODO: Do NOT allow duplicates!

        if (accountService.isBuyer(userId))
        {
            JSONArray cart = accountService.getUserById(userId).getJSONArray("cart");
            JSONObject product = new JSONObject();

            product.put("product_id", productId);
            product.put("quantity", quantity);
            product.put("amount", getTotalAmount(productId, storeId, quantity));

            cart.put(product);
            return accountService.updateUserDetails(userId, "cart", cart);
        }
        return false;
    }

    private double getTotalAmount(String productId, String storeId, int quantity) {

        // TODO: Add real functionality

        return 1000.0f;
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

    public boolean placeOrder(String userId, boolean paymentStatus) {

        return false;
    }
}
