package backend;

/**
 * Marketplace: backend.Validation Class
 *
 * <p>
 *     Static validation checks actions within program.
 * </p>
 *
 * @author [INSERT NAME], CS 180 Lab
 *
 * @version v0.0.1
 */
public class Validation {

    /**
     * <p>
     *     Checks if email is a valid email that passes typical structure of an email
     * </p>
     * @param email Email to be checked for validation
     * @return True if email is valid, False otherwise
     */
    public static boolean validEmail(String email) {
        /*
        TODO: Implement validation check using regex which checks if email is valid
         (ex. jdoe@purdue.edu is valid, @email is not valid)
         */

        return false;
    }

    /**
     * <p>
     *     Checks if price is valid, price should not be less than 0, nor should it exceed an excessive amount
     * </p>
     * @param price Price to be checked for validation
     * @return True if price is valid, False otherwise
     */
    public static boolean validPrice(double price) {
        /*
        TODO: Implement price validation to ensure price can't be negative or too high
         */

        return false;
    }

    /**
     * <p>
     *     Validates password based off certain parameters intended to ensure a secure password
     * </p>
     * @param password Password to be checked for validation
     * @return True if password is valid, False otherwise
     */
    public static boolean validSecurePassword(String password) {
        /*
        TODO: Implement password security to ensure password is secure enough using regex
         (ex. Password must contain 1 capital letter, 1 special character, length of at least 8 characters)
         */

        return false;
    }

    /**
     * <p>
     *     Validates quantity, checks if quantity is greater than 0 and does not exceed an excessive amount
     * </p>
     * @param quantity Quantity to be validated
     * @return True if valid quantity, False otherwise
     */
    public static boolean validQuantity(int quantity) {
        /*
        TODO: Implement check that quantity is greater than 0 and does not reach excessive amount
         */

        return false;
    }

    /**
     * <p>
     *     Validates quantity, checks if quantity is greater than 0 and does not exceed a specified limit
     * </p>
     * @param quantity Quantity to be validated
     * @param limit Amount that the quantity cannot exceed
     * @return True if valid quantity, False otherwise
     */
    public static boolean validQuantity(int quantity, int limit) {
        /*
        TODO: Implement check that quantity is greater than 0 and does not reach specified limit
         */

        return false;
    }

    /**
     * <p>
     *     Validates purchase, checks if there are any issues with purchasing a product by customer
     * </p>
     *
     * @return True if valid quantity, False otherwise
     */
    public static boolean validPurchase() {
        /*
        TODO: Ensure purchase by customer has correct funding, and that quantity is correct
         */

        return false;
    }

}
