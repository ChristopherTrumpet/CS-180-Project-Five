import backend.Product;
import backend.Store;
import backend.usertypes.Customer;
import backend.usertypes.Seller;
import backend.usertypes.User;

public class Database {

    private String filePath;

    // CONDITIONALS
    private boolean customerFolderExists() {

        return false;
    }

    private boolean sellerFolderExists() {

        return false;
    }

    private boolean storeFolderExists() {

        return false;
    }

    private boolean userExists(User user) {

        return false;
    }

    private boolean storeExists(Store store) {

        return false;
    }

    private boolean hasUsers() {

        return false;
    }

    private boolean hasSellers() {

        return false;
    }

    private boolean hasCustomers() {

        return false;
    }

    // WRITE TO FILE METHODS
    private boolean writeUser(User user) {

        return false;
    }

    private boolean writeSeller(Seller seller) {

        return false;
    }

    private boolean writeCustomer(Customer seller) {

        return false;
    }

    // UPDATE FILE METHODS
    private boolean updateUser(User user) {

        return false;
    }

    private boolean updateSellerStores(Store[] stores) {

        return false;
    }

    private boolean updateCustomerCart(Product[] products) {

        return false;
    }

    private boolean updateCustomerPurchaseHistory(Product[] products) {

        return false;
    }

    // GETTERS
    public User[] getAllUsers() {

        return null;
    }

    public Seller getSeller(String name) {

        return null;
    }

    public Customer getCustomer(String name) {

        return null;
    }

    private String[] getCSVLines(String filePath) {

        return null;
    }

    public Product[] getProductsFromCSV(String filePath) {

        return null;
    }

    public Product[] getCartFromCSV(String filePath) {

        return null;
    }

    public Product[] getPurchaseHistoryFromCSV(String filePath) {

        return null;
    }

    public String[] getAccountInfoFromFile(String filePath) {

        return null;
    }

}
