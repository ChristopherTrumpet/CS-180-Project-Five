package backend.usertypes;

import backend.Store;

import java.util.Set;

public class Seller extends User {

    // We store the seller's stores in a set to ensure NO duplicates
    private Set<Store> stores;

    // CONDITIONALS
    public boolean hasStores() {

        return false;
    }

    // GETTERS
    public Set<Store> getStores() {
        return stores;
    }

    // SETTERS

    public void setStores(Set<Store> stores) {
        this.stores = stores;
    }
}
