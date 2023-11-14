package backend;

public class Product {

    // FIELDS
    private String name;
    private String description;
    private double price;
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

    // CONDITIONALS
    public boolean sold() {

        return false;
    }


}
