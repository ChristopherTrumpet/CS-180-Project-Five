package services;

import java.io.File;

public class StoreService {

    public boolean addStore(String storeId) {

        return false;
    }

    public boolean removeStore(String storeId) {

        return false;
    }

    public boolean createStore(String name) {

        return false;
    }

    public boolean updateStoreName(String storeId, String newName) {

        return false;
    }

    public boolean importProducts(File csvFile) {

        return false;
    }

    public boolean exportProducts() {

        return false;
    }

    public boolean addProduct(String storeId, String productId, int qty, double price) {

        return false;
    }

    public boolean removeProduct(String storeId, String productId) {

        return false;
    }

    public boolean createProduct(String name, String description, String itemType) {

        return false;
    }

    public boolean updateProductName(String productId, String newName) {

        return false;
    }

    public boolean updateProductDescription(String productId, String newDescription) {

        return false;
    }

    public boolean updateProductPrice(String storeId, String productId, double newPrice) {

        return false;
    }

    public boolean updateProductQuantity(String storeId, String productId, int newQuantity) {

        return false;
    }

    public String generateProductId() {

        return null;
    }

    public String generateStoreId() {

        return null;
    }
}
