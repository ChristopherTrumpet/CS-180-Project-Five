package server;

import org.json.JSONArray;
import org.json.JSONObject;
import services.AccountService;
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
        try {

            // Send data to the client
            BufferedReader input = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );

            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            AccountService as = new AccountService();
            TransactionService ts = new TransactionService();
            StoreService ss = new StoreService();

            boolean run = true;

            while (run) {

                String clientData = input.readLine();
                ArrayList<String> data = new ArrayList<>();

                if (clientData != null) {
                    data.add(clientData);
                }

                switch (data.get(0)) {
                    case "[signUpButton]" -> {
                        for(int i = 0; i < 4; i++) {
                            data.add(input.readLine());
                        }
                        char accountType = data.get(1).charAt(0);
                        String username = data.get(2);
                        String password = data.get(3);
                        String email = data.get(4);
                        as.createAccount(accountType, username, password, email);
                        writer.println(as.getUserByUsername(username));
                        writer.flush();
                        data.clear();
                    }
                    case "[loginButton]" -> {

                        data.add(input.readLine());
                        data.add(input.readLine());

                        String usernameOrEmail = data.get(1);
                        String password = data.get(2);

                        JSONObject user = as.validateLogin(usernameOrEmail, password);

                        if(user != null) {
                            writer.println("true");
                            writer.println(user);
                        }
                        else {
                            writer.println("false"); // False - could not validate user
                            writer.println("False"); // False - There is no user to pass
                        }
                        writer.flush();
                        data.clear();

                    }
                    case "[getStores]" -> {
                        System.out.println("[SERVER] Receiving stores...");
                        JSONObject storeFile = new JSONObject(ss.getStoreFile());
                        if (!storeFile.isEmpty()) {
                            writer.println(storeFile.getJSONArray("stores"));
                        } else {
                            writer.println("empty");
                        }
                        writer.flush();
                        data.clear();

                    }
                    case "[getProducts]" -> {
                        System.out.println("[SERVER] Receiving products...");
                        JSONObject productFile = new JSONObject(ss.getProductFile());
                        if (!productFile.isEmpty()) {
                            writer.println(productFile.getJSONArray("products"));
                        } else {
                            writer.println("empty");
                        }
                        writer.flush();
                        data.clear();
                    }
                    case "[changeStoreName]" -> {

                        data.add(input.readLine());
                        data.add(input.readLine());

                        ss.updateStoreName(data.get(1), data.get(2));
                        System.out.println("[SERVER] Changed store name...");
                        data.clear();
                    }
                    case "[removeStore]" -> {

                        data.add(input.readLine()); // Seller id
                        data.add(input.readLine()); // Store id

                        System.out.printf("Seller ID: %s\nStore ID: %s\n", data.get(1), data.get(2));

                        if (ss.removeStore(data.get(2), data.get(1)))
                            System.out.println("[SERVER] Removed store...");
                        else
                            System.out.println("[SERVER] Problem occurred removing store...");
                    }
                    case "[removeProduct]" -> {

                        data.add(input.readLine()); // Store Id
                        data.add(input.readLine()); // Product Id

                        if (ss.removeProduct(data.get(1), data.get(2)))
                            System.out.println("[SERVER] Removed product name...");
                        else
                            System.out.println("[SERVER] Product could not be removed...");
                        data.clear();
                    }
                    case "[addProduct]" -> {
                        data.add(input.readLine()); // Store id
                        data.add(input.readLine()); // Product id
                        data.add(input.readLine()); // Quantity
                        data.add(input.readLine()); // Price

                        if (ss.addProduct(data.get(1), data.get(2), Integer.parseInt(data.get(3)), Double.parseDouble(data.get(4)))) {
                            System.out.println("[SERVER] Added product");
                        } else {
                            System.out.println("[SERVER] Could not add product...");
                        }
                        data.clear();
                    }
                    case "[createStore]" -> {
                        data.add(input.readLine()); // Seller Id
                        data.add(input.readLine()); // Store Name

                        if (ss.createStore(data.get(1), data.get(2))) {
                            writer.println(ss.getStoreByName(data.get(2)));
                            System.out.println("[SERVER] Created store and add to users store");
                        } else {
                            System.out.println("[SERVER] Store could not be created...");
                        }
                        writer.flush();
                    }
                    case "[updateUserDetails]" -> {
                        data.add(input.readLine()); // User id
                        data.add(input.readLine()); // User info to change
                        data.add(input.readLine()); // New value

                        if (as.updateUserDetails(data.get(1), data.get(2), data.get(3))) {
                            System.out.println("Changed details successfully");
                        } else {
                            System.out.println("Error occurred");
                        }
                    }
                    case "[deleteAccount]" -> {
                        data.add(input.readLine()); // User id

                        if (as.removeAccount(data.get(1))) {
                            System.out.println("[SERVER] Account removed successfully!");
                        } else {
                            System.out.println("[SERVER] Error occurred, account was not removed.");
                        }
                    }
                    case "[addToCart]" -> {
                        data.add(input.readLine()); // User ID
                        data.add(input.readLine()); // Product ID
                        data.add(input.readLine()); // Quantity
                        data.add(input.readLine()); // Price

                        if (ts.addToCart(data.get(1), data.get(2), Integer.parseInt(data.get(3)), Double.parseDouble(data.get(4)))) {
                            System.out.println("[SERVER] Added to cart successfully!");
                        } else {
                            System.out.println("[SERVER] Error occurred writing cart.");
                        }
                    }
                    case "[quit]" -> {
                        System.out.println("[SERVER] Client has disconnected.");
                        run = false;
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
}
