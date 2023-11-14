package usertypes;

import backend.Product;
import backend.Store;

import java.util.ArrayList;
import java.util.Set;

public class Customer extends User {
    private double balance;

    // No product should have duplicates in cart; instead, they should be added onto existing product
    private Set<Product> cart;
    // Stored in arraylist for dynamic adding, allows duplicates
    private ArrayList<Product> purchaseHistory;

    // GETTER METHODS

    public double getBalance() {
        return balance;
    }

    public Set<Product> getCart() {
        return cart;
    }

    public ArrayList<Product> getPurchaseHistory() {
        return purchaseHistory;
    }

    // SETTER METHODS

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCart(Set<Product> cart) {
        this.cart = cart;
    }

    public void setPurchaseHistory(ArrayList<Product> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    /**
     * <p>
     *     Clears the cart set, typically after checkout has been completed
     * </p>
     * @return the status of the method operation, true means successful, false means failure
     */
    public boolean clearCart() {
        /*
        TODO: Clear the cart set
         ensure change is synced with customer record
         ensure change updates the customer array of affected stores
         */

        return false;
    }

    /**
     * <p>
     *     Ensures customer has proper funds to purchase all products in cart,
     *     Updates store stock of product,
     *     Adds cart items to customer purchase history,
     *     utilizes clearCart(),
     *     Update database
     * </p>
     * @return the status of the method operation, true means successful, false means failure
     */
    public boolean checkout() {
        // TODO: Implement features described in javadoc

        return false;
    }

    /**
     * <p>
     *     Adds a product to the customers cart set. Does not allow duplicates; instead, the
     *     "duplicate" product should get added onto an existing stack.
     * </p>
     * @param product backend.Product to be added to the cart set
     * @return the status of the method operation, true means successful, false means failure
     */
    public boolean addToCart(Product product) {
        /*
        TODO: Add specified product to cart
         ensure change is synced with customer record
         ensure change updates the customer array of affected stores
         */

        return false;
    }

    /**
     * <p>
     *     Removes a product from the customers cart set.
     * </p>
     * @param product backend.Product to be removed from the cart set
     * @return the status of the method operation, true means successful, false means failure
     */
    public boolean removeFromCart(Product product) {
        /*
        TODO: Remove specified product from cart
         ensure change is synced with customer record
         ensure change updates the customer array of affected stores
         */

        return false;
    }

    /**
     * <p>
     *     Sorts a stores array by product amount purchased by customer from least to greatest by default
     * </p>
     * @param stores Unsorted stores array that needs sorted by product amount purchased
     * @param inverse Sorts array from least to greatest instead of greatest to least which is default
     * @return A sorted stores array
     */
    private Store[] sortStoresByAmountPurchased(Store[] stores, boolean inverse) {
        /*
        TODO: Implement sorting algorithm which sorts an array from least value to greatest value,
         or the opposite if the inverse condition is true.
         */

        return null;
    }
}
