package server;

import org.json.JSONArray;
import org.json.JSONObject;
import services.AccountService;
import services.StoreService;

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
            StoreService ss = new StoreService();

            while (true) {

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
                    }
                }
            }


        } catch (IOException e) {
            System.err.println("Oops: " + e.getMessage());

        } finally {

            try {
                socket.close();
            } catch (IOException e) {
                // Oh, well!
            }
        }
    }
}
