package backend;

/**
 * Marketplace: backend.Product Class
 *
 * <p>
 *     backend.Product object stores all information referring to a store product. Intended for mostly read-only data,
 *     stays parallel to the file version of a product, stored within a CSV
 * </p>
 *
 * @author [INSERT NAME], CS 180 Lab
 *
 * @version v0.0.1
 */
public class Product {

    // FIELDS
    private Store store;
    private String name;
    private String description;
    private double price;
    private int quantity;
    // For OPTIONAL feature, limits quantity a customer can buy at a time
    private int quantityLimit;
    private int amountSold;

    // GETTERS
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAmountSold() {
        return amountSold;
    }

    public Store getStore() {
        return store;
    }

    // SETTERS
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAmountSold(int amountSold) {
        this.amountSold = amountSold;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    // CONDITIONALS

    /**
     * Conditional for if product has been sold before
     * @return True if product has been sold, False if otherwise
     */
    public boolean sold() {

        return false;
    }


}
