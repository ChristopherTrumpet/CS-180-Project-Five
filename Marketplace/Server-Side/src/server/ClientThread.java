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

                            data = readData(input, 4);

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
                            data = readData(input, 2);

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
                            data = readData(input, 2);

                            if (ss.updateStoreName(data.get(0), data.get(1)))
                                System.out.println("[SERVER] Changed store name...");
                            else
                                System.out.println("[SERVER] Error occurred changing name.");
                        }
                        case "removeStore" -> {

                            // Seller id
                            // Store id
                            data = readData(input, 2);

                            if (ss.removeStore(data.get(1), data.get(0)))
                                System.out.println("[SERVER] Removed store...");
                            else
                                System.out.println("[SERVER] Problem occurred removing store...");
                        }
                        case "removeProduct" -> {

                            // Store id
                            // Product id
                            data = readData(input, 2);

                            if (ss.removeProduct(data.get(0), data.get(1)))
                                System.out.println("[SERVER] Removed product name...");
                            else
                                System.out.println("[SERVER] Product could not be removed...");
                        }
                        case "addProduct" -> {

                            // Store id
                            // Product id
                            // Quantity
                            // Price
                            data = readData(input, 4);

                            if (ss.addProduct(data.get(0), data.get(1), Integer.parseInt(data.get(2)), Double.parseDouble(data.get(3)))) {
                                System.out.println("[SERVER] Added product");
                            } else {
                                System.out.println("[SERVER] Could not add product...");
                            }
                        }
                        case "createStore" -> {

                            // Seller id
                            // Store name
                            data = readData(input, 2);

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
                            data = readData(input, 4);

                            if (ss.updateStoreProduct(data.get(0), data.get(1), data.get(2), data.get(3))) {
                                System.out.println("Changed details successfully");
                            } else {
                                System.out.println("Error occurred");
                            }
                        }
                        case "updateUserDetails" -> {

                            // User id
                            // User detail
                            // New value
                            data = readData(input, 3);

                            if (as.updateUserDetails(data.get(0), data.get(1), data.get(2))) {
                                System.out.println("Changed details successfully");
                            } else {
                                System.out.println("Error occurred");
                            }
                        }
                        case "deleteAccount" -> {

                            // User id
                            data = readData(input, 1);

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
                            data = readData(input, 5);

                            if (ts.addToCart(data.get(0), data.get(1), data.get(2), Integer.parseInt(data.get(3)), Double.parseDouble(data.get(4)))) {
                                System.out.println("[SERVER] Added to cart successfully!");
                            } else {
                                System.out.println("[SERVER] Error occurred writing cart.");
                            }
                        }
                        case "placeOrder" -> {

                            // User id
                            data = readData(input, 1);

                            if (ts.placeOrder(data.get(0))) {
                                System.out.println("[SERVER] Order successfully placed!");
                                writer.println("true");
                            } else {
                                writer.println("false");
                                System.out.println("[SERVER] Error occurred placing order.");
                            }
                            writer.flush();
                        }
                        case "addFunds" -> {

                            // Buyer id
                            // New Balance Amount
                            data = readData(input, 2);

                            if (ts.addFunds(data.get(0), Double.parseDouble(data.get(1)))) {
                                System.out.println("[SERVER] Funds added!");
                            } else {
                                System.out.println("[SERVER] Funds were not successfully added...");
                            }
                        }
                        case "getUser" -> {

                            // User id
                            data = readData(input, 1);

                            String user = as.getUser("id", data.get(0)).toString();
                            writer.println(user);
                            writer.flush();
                        }
                        case "getStore" -> {

                            // Store id
                            data = readData(input, 1);

                            String store = ss.getStoreById(data.get(0)).toString();
                            writer.println(store);
                            writer.flush();
                        }
                        case "exportHistory" -> {

                            // User id
                            // File Path
                            data = readData(input, 2);

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

                            data = readData(input, 3);
                            ts.removeFromCart(data.get(0), data.get(1), data.get(2));
                        }
                        case "search" -> {

                            // Search Query
                            data = readData(input, 1);

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
                            data = readData(input, 2);

                            ss.importProducts(new JSONObject(data.get(0)), data.get(1));
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

    public ArrayList<String> readData(BufferedReader input, int lines) throws IOException {

        ArrayList<String> data = new ArrayList<>();

        for (int i = 0; i < lines; i++) {
            data.add(input.readLine());
        }

        return data;

    }
}
