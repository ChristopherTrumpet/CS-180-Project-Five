package services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.SortedMap;

public class TransactionService {

    private final AccountService accountService = new AccountService();

    public boolean addToCart(String userId, String productId, int quantity, double price) {

        // TODO: Do NOT allow duplicates!

        if (accountService.isBuyer(userId))
        {
            JSONArray cart = accountService.getUserById(userId).getJSONArray("cart");
            JSONObject product = new JSONObject();

            product.put("product_id", productId);
            product.put("quantity", quantity);
            product.put("price", price);

            cart.put(product);
            return accountService.updateUserDetails(userId, "cart", cart);
        }
        return false;
    }

    private double getTotalAmount(String productId, String storeId, int quantity) {

        // TODO: Add real functionality
        double total = 0.0;
        if (accountService.isSeller(storeId))
        {
            JSONArray products = accountService.getUserById(storeId).getJSONArray("product_id");

            for (int i = 0; i < products.length(); i++) {
                JSONObject product = (JSONObject) products.get(i);
                if (product.get("product_id").equals(productId)) {
                    return (Double) product.get("price") * quantity;
                }
//                String currProductId = (String) product.get("product_id");
            }

        }

        return -1;
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
    public boolean writeProductHistory(String userId, JSONArray itemsBought) {
        AccountService as = new AccountService();
        JSONObject buyer = as.getUserById(userId);
        buyer.append("Product_History", itemsBought);
        return true;
    }

//    public boolean checkQuantity(String storeId, String productId, int quantity) {
//        JSONObject store = accountService.getUserById(storeId);
//        for (Object product : ((JSONObject) store).getJSONArray("products")) {
//            if (((JSONObject) product).get("id").toString().equals(productId)) {
//                JSONObject theProduct = (JSONObject) ((JSONObject) product).get("id");
//                if (theProduct.getInt("qty") < 1) {
//                    return false;
//                } else if (theProduct.getInt("qty") >= quantity) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        }
//        return false;
//    }



    public double cartTotal(String userId) {
        StoreService storeServ = new StoreService();
        double total = 0;
        JSONArray cart = accountService.getUserById(userId).getJSONArray("cart");
        for (int i = 0; i < cart.length(); i++) {
            JSONObject cartItem = cart.getJSONObject(i);
            String productID = cartItem.getString("product_id");
            int qty = cartItem.getInt("quantity");
            String storeId = cartItem.getString("store_id");

            total += getTotalAmount(userId, productID, qty);
            if (!storeServ.checkQuantity(storeId, productID, qty)) {
                System.out.println(cartItem.getString("name") + "count exceeds store stock");
            }
        }
        return total;
    }


    public boolean placeOrder(String userId) {
        StoreService storeServ = new StoreService();
        double total = 0;
        if (getCartContents(userId) != null) {
            JSONArray cart = accountService.getUserById(userId).getJSONArray("cart");
            total = cartTotal(userId);
            if ((Double) accountService.getUserById(userId).get("Balance") >= total) {
                for (int i = 0; i < cart.length(); i++) {
                    JSONObject cartItem = cart.getJSONObject(i);
                    String productID = cartItem.getString("product_id");
                    int qty = cartItem.getInt("quantity");
                    String storeId = cartItem.getString("store_id");
                    storeServ.updateProductQuantity(storeId, productID, qty);

                }

                boolean result = (writeProductHistory(userId, cart));
                accountService.getUserById(userId).put("Balance",
                        (Double) accountService.getUserById(userId).get("Balance") - total);
                clearCart(userId);
                return result;
            }
        }
        return false;
    }
}
