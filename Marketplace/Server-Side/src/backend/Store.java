package backend;

import backend.usertypes.Customer;
import backend.usertypes.Seller;

import java.util.Set;

public class Store {

    // FIELDS
    private String name;
    private Seller seller;

    // We store store0-products.csv in a set to ensure there are NO duplicates
    private Set<Product> products;
    private Set<Customer> customers;

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

    /**
     * Retrieves specified product using product name
     * @param productName Name of product to be searched
     * @return Product matching name, null if product was not found
     */
    public Product getProduct(String productName) {
        /*
        TODO: Search for product in products list with criteria of matching names
        */

        return null;
    }

    /**
     * Retrieves array of all customers that have a product of store in their cart
     * @return Array of customers with a product of store in their cart
     */
    public Customer[] getCustomers() {
        /*
        TODO: Convert customer set to primitive array
        */
        return null;
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

    // CONDITIONALS
    public boolean hasProducts() {

        return false;
    }

    public boolean addCustomer() {

        return false;
    }

    public boolean removeCustomer() {

        return false;
    }

    public boolean addProduct() {

        return false;
    }

    public boolean removeProduct() {

        return false;
    }

    public boolean editProduct(Product productToReplace) {

        return false;
    }
}
