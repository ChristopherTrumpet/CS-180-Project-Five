package backend;

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

    // CONDITIONALS
    public boolean sold() {

        return false;
    }


}
