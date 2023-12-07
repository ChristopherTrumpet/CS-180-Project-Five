package server;

import org.json.JSONObject;
import services.AccountService;

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

            while (true) {
                String clientData = input.readLine();
                ArrayList<String> data = new ArrayList<>();
                if (clientData != null) {
                    data.add(clientData);
                }
                String action = data.get(0);
                if(action.equals("[signUpButton]")) {
                    for(int i = 0; i < 4; i++) {
                        data.add(input.readLine());
                    }
                    char accountType = data.get(1).charAt(0);
                    String username = data.get(2);
                    String password = data.get(3);
                    String email = data.get(4);
                    as.createAccount(accountType, username, password, email);
                }
                else if(action.equals("[loginButton]")) {
                    data.add(input.readLine());
                    data.add(input.readLine());
                    String usernameOrEmail = data.get(1);
                    String password = data.get(2);
                    JSONObject user = as.validateLogin(usernameOrEmail, password);
                    if(user != null) {
                        String userID = user.get("id").toString();
                        writer.println("True");
                        writer.println(userID.substring(userID.length() - 1));
                    }
                    else {
                        writer.println("False");
                        writer.println("False"); //this is intentionally here twice don't delete
                    }
                    writer.flush();
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
