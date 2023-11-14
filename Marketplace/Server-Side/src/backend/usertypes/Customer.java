package backend.usertypes;

import backend.Product;

import java.util.ArrayList;
import java.util.Set;

public class Customer extends User {
    private double balance;

    // No product should have duplicates in cart; instead, they should be added onto existing product
    private Set<Product> cart;
    // Stored in arraylist for dynamic adding, allows duplicates
    private ArrayList<Product> purchaseHistory;

    // GETTERS

    public double getBalance() {
        return balance;
    }

    public Set<Product> getCart() {
        return cart;
    }

    public ArrayList<Product> getPurchaseHistory() {
        return purchaseHistory;
    }

    // SETTERS

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCart(Set<Product> cart) {
        this.cart = cart;
    }

    public void setPurchaseHistory(ArrayList<Product> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }
}
