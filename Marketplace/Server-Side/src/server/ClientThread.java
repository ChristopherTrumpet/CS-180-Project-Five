package server;

import org.json.JSONObject;
import services.AccountService;
import services.SearchService;
import services.StoreService;
import services.TransactionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread {

    private final Socket socket;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            AccountService as = new AccountService();
            TransactionService ts = new TransactionService();
            StoreService ss = new StoreService();
            SearchService searchService = new SearchService();

            loop: while (true) {

                String clientData = input.readLine();
                ArrayList<String> data = new ArrayList<>();

                if (clientData != null) {

                    data.add(clientData);

                    switch (data.get(0)) {

                        case "signUpButton" -> {

                            data = read(input, 4);

                            char accountType = data.get(0).charAt(0);
                            String username = data.get(1);
                            String password = data.get(2);
                            String email = data.get(3);

                            as.createAccount(accountType, username, password, email);

                            writer.println(as.getUser("username", username));
                            writer.flush();
                        }
                        case "loginButton" -> {

                            // identifier
                            // password
                            data = read(input, 2);

                            String usernameOrEmail = data.get(0);
                            String password = data.get(1);

                            JSONObject user = as.validateLogin(usernameOrEmail, password);

                            if (user != null) {
                                writer.println("true");
                                writer.println(user);
                            } else {
                                writer.println("false"); // False - could not validate user
                                writer.println("False"); // False - There is no user to pass
                            }
                            writer.flush();

                        }
                        case "getStores" -> {

                            JSONObject stores = as.getJSONFromFile(ss.getStoreFileDirectory());

                            if (!stores.isEmpty()) {
                                writer.println(stores.getJSONArray("stores"));
                                System.out.println("[SERVER] Transferred stores.");

                            } else {
                                writer.println("empty");
                            }
                            writer.flush();
                        }
                        case "getProduct" -> {
                            // Product Name
                            data = read(input, 1);

                            JSONObject product = ss.getProduct("name", data.get(0));
                            writer.println(product.toString());
                            writer.flush();
                        }
                        case "getProducts" -> {

                            // Retrieve product file
                            JSONObject productFile = as.getJSONFromFile(ss.getProductFileDirectory());

                            if (!productFile.isEmpty()) {
                                writer.println(productFile.getJSONArray("products"));
                                System.out.println("[SERVER] Transferred products.");
                            } else {
                                writer.println("empty");
                            }
                            writer.flush();
                        }
                        case "changeStoreName" -> {

                            // Store id
                            // New Name
                            data = read(input, 2);

                            if (ss.updateStoreName(data.get(0), data.get(1)))
                                System.out.println("[SERVER] Changed store name...");
                            else
                                System.out.println("[SERVER] Error occurred changing name.");
                        }
                        case "removeStore" -> {

                            // Seller id
                            // Store Name
                            data = read(input, 2);
                            String storeId = ss.getStoreByName(data.get(1)).getString("id");

                            if (ss.removeStoreFromSeller(storeId, data.get(0)))
                                System.out.println("[SERVER] Removed store...");
                            else
                                System.out.println("[SERVER] Problem occurred removing store...");
                        }
                        case "removeProduct" -> {

                            // Store id
                            // Product id
                            data = read(input, 2);

                            String productId = ss.getProduct("name", data.get(1)).getString("product_id");

                            if (ss.removeProduct(data.get(0), productId))
                                System.out.println("[SERVER] Removed product name...");
                            else
                                System.out.println("[SERVER] Product could not be removed...");
                        }
                        case "addProduct" -> {

                            // Store id
                            // Product id
                            // Quantity
                            // Price
                            data = read(input, 4);

                            if (ss.addProduct(data.get(0), data.get(1), Integer.parseInt(data.get(2)), Double.parseDouble(data.get(3)))) {
                                System.out.println("[SERVER] Added product");
                            } else {
                                System.out.println("[SERVER] Could not add product...");
                            }
                        }
                        case "createStore" -> {

                            // Seller id
                            // Store name
                            data = read(input, 2);

                            if (ss.createStore(data.get(0), data.get(1))) {
                                writer.println(ss.getStoreByName(data.get(1)));
                                System.out.println("[SERVER] Created store and add to users store");
                            } else {
                                System.out.println("[SERVER] Store could not be created...");
                            }
                            writer.flush();
                        }
                        case "updateProduct" -> {

                            // Store id
                            // Product id
                            // Type
                            // Value
                            data = read(input, 4);
                            String productId = ss.getProduct("name", data.get(1)).getString("product_id");

                            if (ss.updateStoreProduct(data.get(0), productId, data.get(2), data.get(3))) {
                                System.out.println("Changed details successfully");
                            } else {
                                System.out.println("Error occurred");
                            }
                        }
                        case "updateUserDetails" -> {

                            // User id
                            // User detail
                            // New value
                            data = read(input, 3);

                            if (as.updateUserDetails(data.get(0), data.get(1), data.get(2))) {
                                System.out.println("Changed details successfully");
                            } else {
                                System.out.println("Error occurred");
                            }
                        }
                        case "deleteAccount" -> {

                            // User id
                            data = read(input, 1);

                            if (as.removeAccount(data.get(0))) {
                                System.out.println("[SERVER] Account removed successfully!");
                            } else {
                                System.out.println("[SERVER] Error occurred, account was not removed.");
                            }
                        }
                        case "addToCart" -> {

                            // User id
                            // Product id
                            // Store id
                            // Quantity
                            // Price
                            data = read(input, 5);

                            if (ts.addToCart(data.get(0), data.get(1), data.get(2), Integer.parseInt(data.get(3)), Double.parseDouble(data.get(4)))) {
                                System.out.println("[SERVER] Added to cart successfully!");
                            } else {
                                System.out.println("[SERVER] Error occurred writing cart.");
                            }
                        }
                        case "placeOrder" -> {

                            // User id
                            data = read(input, 1);

                            if (ts.placeOrder(data.get(0))) {
                                System.out.println("[SERVER] Order successfully placed!");
                                writer.println("true");
                            } else {
                                System.out.println("[SERVER] Error occurred placing order.");
                                writer.println("false");

                            }
                            writer.flush();
                        }
                        case "addFunds" -> {

                            // Buyer id
                            // New Balance Amount
                            data = read(input, 2);

                            if (ts.addFunds(data.get(0), Double.parseDouble(data.get(1)))) {
                                System.out.println("[SERVER] Funds added!");
                            } else {
                                System.out.println("[SERVER] Funds were not successfully added...");
                            }
                        }
                        case "getUser" -> {

                            // User id
                            data = read(input, 1);

                            String user = as.getUser("id", data.get(0)).toString();
                            writer.println(user);
                            writer.flush();
                        }
                        case "getUsers" -> {

                            JSONObject users = as.getJSONFromFile(as.getUserFileDirectory());

                            if (!users.isEmpty()) {
                                writer.println(users.getJSONArray("users"));
                                System.out.println("[SERVER] Transferred users.");

                            } else {
                                writer.println("empty");
                            }
                            writer.flush();

                        }
                        case "userExists" -> {
                            // Email
                            // Username
                            data = read(input, 2);

                            if (as.userExists(data.get(0), data.get(1)))
                                writer.println("true");
                            else
                                writer.println("false");

                            writer.flush();
                        }
                        case "getStore" -> {

                            // Store name
                            data = read(input, 1);

                            String store = ss.getStoreByName(data.get(0)).toString();
                            writer.println(store);
                            writer.flush();
                        }
                        case "getStoreById" -> {
                            // Store id
                            data = read(input, 1);
                            System.out.println(data.get(0));
                            String store = ss.getStoreById(data.get(0)).toString();
                            writer.println(store);
                            writer.flush();
                        }
                        case "exportHistory" -> {

                            // User id
                            // File Path
                            data = read(input, 2);

                            if (ts.exportProductHistory(data.get(0), data.get(1))) {
                                System.out.println("[SERVER] Successfully export product history");
                            } else {
                                System.out.println("[SERVER] Error occurred exporting product history.");
                            }
                        }
                        case "removeFromCart" -> {
                            // User id
                            // Product Name
                            // Store id

                            data = read(input, 2);

                            try {
                                String storeId = ss.getStoreProduct(data.get(1)).getString("id");
                                ts.removeFromCart(data.get(0), data.get(1), storeId);
                            } catch (NullPointerException e) {
                                System.out.println("[SERVER] Product no longer exists...");
                                ts.removeFromCart(data.get(0), data.get(1));

                            }


                        }
                        case "search" -> {

                            // Search Query
                            data = read(input, 1);

                            ArrayList<String> results = searchService.search(data.get(0));

                            if (results == null) {
                                writer.println("null");
                            } else {
                                for (String result : results) {
                                    writer.println(result);
                                }
                            }

                            writer.flush();
                        }
                        case "importProducts" -> {

                            // Seller id
                            // File path
                            data = read(input, 2);

                            ss.importProducts(new JSONObject(data.get(0)), data.get(1));
                        }
                        case "createProduct" -> {
                            // Product Name
                            // Product Description
                            // Product Quantity
                            // Product Price
                            // Store id
                            data = read(input, 5);
                            JSONObject product = ss.createProduct(data.get(0), data.get(1));
                            if (ss.addProduct(data.get(4), product.getString("product_id"), Integer.parseInt(data.get(2)), Double.parseDouble(data.get(3)))) {
                                System.out.println("Created a new product");
                                writer.println("true");
                            } else {
                                System.out.println("Error occurred creating new product.");
                                writer.println("true");
                            }
                            writer.flush();
                        }
                        case "getStoreProduct" -> {
                            // Product Object
                            data = read(input, 2);

                            JSONObject updatedProduct = ss.getStoreProduct(data.get(0), data.get(1));

                            writer.println(updatedProduct.toString());
                            writer.flush();

                        }
                        case "exit" -> {
                            break loop;
                        }

                    }
                }
            }


        } catch (IOException e) {
            System.err.println("Oops: " + e.getMessage());

        } finally {

            try {
                System.out.println("[SERVER] Client thread dispersed.");
                socket.close();
            } catch (IOException e) {
                // Oh, well!
            }
        }
    }

    public ArrayList<String> read(BufferedReader input, int lines) throws IOException {

        ArrayList<String> data = new ArrayList<>();

        for (int i = 0; i < lines; i++) {
            data.add(input.readLine());
        }

        return data;

    }
}
