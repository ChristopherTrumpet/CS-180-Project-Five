import backend.Product;
import backend.Store;
import backend.usertypes.Customer;
import backend.usertypes.Seller;
import backend.usertypes.User;

public class Database {

    // Root file path to all database storage
    private String filePath;

    // CONDITIONALS

    /**
     * <p>
     *     Checks if customer folder directory exists in product structure
     * </p>
     * @return True if directory exists, false otherwise
     */
    private boolean customerFolderExists() {
        /*
        TODO: Implement conditional which checks if customer folder directory exists
         */
        return false;
    }

    /**
     * <p>
     *     Checks if seller folder directory exists in product structure
     * </p>
     * @return True if directory exists, false otherwise
     */
    private boolean sellerFolderExists() {
        /*
        TODO: Implement conditional which checks if sellers folder directory exists
         */
        return false;
    }

    /**
     * <p>
     *     Checks if store folder directory exists in product structure
     * </p>
     * @return True if directory exists, false otherwise
     */
    private boolean storeFolderExists() {
        /*
        TODO: Implement conditional which checks if store folder directory exists
         */
        return false;
    }

    /**
     * <p>
     *     Checks if user folder exists in either customers or sellers directory
     * </p>
     * @return True if user object exists, false otherwise
     */
    private boolean userExists(User user) {
        /*
        TODO: Implement for loop and conditionals which checks if user exists in any directory
         */
        return false;
    }

    /**
     * <p>
     *     Checks if store folder exists in the sellers directory
     * </p>
     * @return True if store object exists, false otherwise
     */
    private boolean storeExists(Store store) {
        /*
        TODO: Implement for loop and conditionals which checks if store exists in any seller directory
         */
        return false;
    }

    /**
     * <p>
     *     Checks if project directory has users from either customers or sellers folder directories
     * </p>
     * @return True if program has user objects, False otherwise
     */
    private boolean hasUsers() {
        /*
        TODO: Implement conditional that utilizes .isEmpty() for both customer and seller directories
         */
        return false;
    }

    /**
     * <p>
     *     Checks if sellers directory has sellers within, essentially checks if directory is empty
     * </p>
     * @return True if sellers directory contains seller folders, False otherwise
     */
    private boolean hasSellers() {
        /*
        TODO: Implement conditional that utilizes .isEmpty() for seller directory
         */
        return false;
    }

    /**
     * <p>
     *     Checks if customers directory has customers within, essentially checks if directory is empty
     * </p>
     * @return True if customers directory contains customer folders, False otherwise
     */
    private boolean hasCustomers() {
        /*
        TODO: Implement conditional that utilizes .isEmpty() for customer directory
         */
        return false;
    }

    // WRITE TO FILE METHODS

    /**
     * <p>
     *     Writes a user object to appropriate file directory, utilizes getUserType()
     * </p>
     * @param user User object to be written or saved into a file.
     * @return True if writing operation was successful, False otherwise
     */
    private boolean writeUser(User user) {
        /*
        TODO: Write user object information into text files, utilize writeSeller() and writeCustomer() methods
         */

        return false;
    }

    /**
     * <p>
     *     Write seller object to sellers file directory, called from writeUser()
     * </p>
     * @param seller Seller object to be written to sellers directory
     * @return True if writing operation was successful, False otherwise
     */
    private boolean writeSeller(Seller seller) {
        /*
        TODO: Write seller object to sellers directory
         seller should get allocated folder (How folder is named is preference, MUST be unique)

        TODO:
         account-info.txt should be attached to folder, format of txt file should be as follows:
         EMAIL,PASSWORD,FILE_DIRECTORY_STRING

        TODO:
         a store sub-directory called "stores" should be created
         store should get allocated folder (How folder is named is preference, MUST be unique)

        TODO:
         Within "stores" sub-directory should be a products file, name of file should be (STORE_ID-products.csv)
         Each csv should contain store name along with all product information need for later reconstruction
         */

        return false;
    }

    /**
     * <p>
     *     Write customer object to sellers file directory, called from writeUser()
     * </p>
     * @param customer Customer object to be written to customer directory
     * @return True if writing operation was successful, False otherwise
     */
    private boolean writeCustomer(Customer customer) {
        /*
        TODO: Write customer object to customers directory
         customer should get allocated folder (How folder is named is preference, MUST be unique)

        TODO:
         account-info.txt should be attached to folder, format of txt file should be as follows:
         EMAIL,PASSWORD,FILE_DIRECTORY_STRING

        TODO:
         cart.csv should be attached to folder, CSV should store all information needed for later reconstruction
         history.csv should be attached to folder, CSV should store all information needed for later reconstruction
         */

        return false;
    }

    // UPDATE FILE METHODS

    /**
     * <p>
     *     Updates existing user file with updated information
     *     Changes based on user object type (Customer or Seller) utilizing getUserType()
     * </p>
     * @param user User object with new information to update to old reference
     * @return True if operation was successful, False otherwise
     */
    private boolean updateUser(User user) {
        /*
        TODO:
         Update contents of an existing user, ensure data is persistent and up to date
         */

        return false;
    }

    /**
     * <p>
     *     Updates existing seller store directories with updated information
     * </p>
     * @param stores Store array with new information to update to old reference
     * @return True if operation was successful, False otherwise
     */
    private boolean updateSellerStores(Store[] stores) {
        /*
        TODO:
         Update contents of seller stores directory, override existing stores with new data
        */

        return false;
    }

    /**
     * <p>
     *     Updates existing cart CSV with updated information, utilizes writeToCSV() method
     * </p>
     * @param products Products array with new information to update to old reference
     * @return True if operation was successful, False otherwise
     */
    private boolean updateCustomerCart(Product[] products) {
        /*
        TODO:
         Update contents of customer cart.csv, override existing data with new content
        */

        return false;
    }
    /**
     * <p>
     *     Updates existing purchase history CSV with updated information, utilizes writeToCSV() method
     * </p>
     * @param products Products array with new information to update to old reference
     * @return True if operation was successful, False otherwise
     */
    private boolean updateCustomerPurchaseHistory(Product[] products) {
        /*
        TODO:
         Update contents of customer history.csv, override existing data with new content
        */

        return false;
    }

    /**
     * <p>
     *     Writes each line of a provided array to a CSV file, each line is a new row
     * </p>
     * @param array Array to be added to CSV file
     * @return True if operation was successful, False otherwise
     */
    private boolean writeToCSV(Object[] array) {
        /*
        TODO: Implement a general method for writing data to a .csv file using an array
        */

        return false;
    }

    // GETTERS

    /**
     * <p>
     *     Gets all user objects from both seller and customer directories
     * </p>
     * @return User array of all users in database
     */
    public User[] getAllUsers() {
        /*
        TODO: Use for loops to get an array of all users from directories
        */

        return null;
    }

    /**
     * Gets whether user object is a Customer object or Seller object
     * @param user User object to be checked
     * @return char representing user type, 'c' is customer. 's' is seller
     */
    public char getUserType(User user) {
        /*
        TODO: Utilize instanceof to identify user object as either customer or seller and assign appropriate char
        */
        return 'n';
    }

    public Seller getSeller(String name) {

        return null;
    }

    public Customer getCustomer(String name) {

        return null;
    }

    /**
     * Generic method which gets lines from a csv file
     * @param filePath File path of .csv file
     * @return String array of lines from csv file
     */
    private String[] getCSVLines(String filePath) {

        return null;
    }

    /**
     * Retrieves store products from csv file
     * @param filePath File path of the store's products.csv file
     * @return Product array of store's products
     */
    public Product[] getProductsFromCSV(String filePath) {

        return null;
    }

    /**
     * Retrieves customer cart information from csv file
     * @param filePath File path of the customer's cart.csv file
     * @return Product array of customer's cart
     */
    public Product[] getCartFromCSV(String filePath) {

        return null;
    }

    /**
     * Retrieves customer purchase history information from csv file
     * @param filePath File path of the customer's account history csv file
     * @return Product array of customer product history
     */
    public Product[] getPurchaseHistoryFromCSV(String filePath) {

        return null;
    }

    /**
     * Retrieves user account information (e.x Email, Password, File Directory)
     * @param filePath File path of the user's account information
     * @return String array of user account information (e.x [EMAIL, PASSWORD, FILE_DIRECTORY])
     */
    public String[] getAccountInfoFromFile(String filePath) {

        return null;
    }

    /**
     * <p>
     *     Exports a customer's purchase history to a pre-named CSV file
     * </p>
     * @param customer Customer to get product history data from
     * @return True if operation was successful, False otherwise
     */
    public boolean exportCustomerPurchaseHistoryData(Customer customer) {
        /*
        TODO: Export purchase history data to a file follow this name scheme,
         customerID-purchase_history.csv
        */
        return false;
    }

}
