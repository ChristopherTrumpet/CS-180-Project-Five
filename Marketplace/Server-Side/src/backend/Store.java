package backend;

import backend.usertypes.Seller;

import java.util.Set;

public class Store {

    // FIELDS
    private String name;
    private Seller seller;

    // We store products.csv in a set to ensure there are NO duplicates
    private Set<Product> products;

    // TODO: Create constructor

    // GETTERS

    public String getName() {
        return name;
    }

    public Seller getSeller() {
        return seller;
    }

    public Set<Product> getProducts() {
        return products;
    }


    // SETTERS

    public void setName(String name) {
        this.name = name;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public boolean hasProducts() {

        return false;
    }
}
