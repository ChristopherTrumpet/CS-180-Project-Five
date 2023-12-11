package backend;

import usertypes.Customer;
import usertypes.Seller;

import java.util.Set;

/**
 * Marketplace: backend.Store Class
 *
 * <p>
 *     backend.Store object for marketplace application, contains all functionality related to store level actions:
 *     including making a purchase, managing available products of store,
 *     manages displaying customer carts that contain products of the store.
 * </p>
 *
 * @author [INSERT NAME], CS 180 Lab
 *
 * @version v0.0.1
 */

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
     * @return backend.Product matching name, null if product was not found
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

    /**
     * Checks if store has either an empty or null product list.
     * @return True if products array is not empty or null, False otherwise
     */
    public boolean hasProducts() {
        /*
        TODO: Implement null check and utilize .isEmpty() function
         */

        return false;
    }

    /**
     * Adds customer object to customers set, added when customer adds one of the stores products to their cart
     * @param customer Customer to be added to customers set
     * @return True if operation was successful, False otherwise
     */
    public boolean addCustomer(Customer customer) {
        /*
        TODO: Add customer object to set, update database accordingly, ensure seller client has up-to-date data
         */

        return false;
    }

    /**
     * Removes customer object from customers set, removed when customer either check out or remove item from cart
     * @param customer Customer to be removed from customers set
     * @return True if operation was successful, False otherwise
     */
    public boolean removeCustomer(Customer customer) {
        /*
        TODO: Remove customer object from set, update database accordingly, ensure seller client has up-to-date data
         */

        return false;
    }

    /**
     * Add product to the store, stored in a set
     * @param product backend.Product to be added to store
     * @return True if operation was successful, False otherwise
     */
    public boolean addProduct(Product product) {
        /*
        TODO: Add a product to the store within products set, ensure data is saved in file, and that set and file exists
         */

        return false;
    }

    /**
     * Remove product from the store, removed from a set
     * @param product backend.Product to be removed from store
     * @return True if operation was successful, False otherwise
     */
    public boolean removeProduct(Product product) {
        /*
        TODO: Remove a product from the store within products set, ensure data is removed from file, and that set and file exists
         */

        return false;
    }

    /**
     * Remove product from the store, removed from a set
     * @param productToReplace backend.Product that is going to be replaced
     * @param newProduct backend.Product with new information to override old product information
     * @return True if operation was successful, False otherwise
     */
    public boolean editProduct(Product productToReplace, Product newProduct) {

        return false;
    }
}