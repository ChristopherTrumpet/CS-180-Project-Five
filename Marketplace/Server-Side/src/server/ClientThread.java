package server;

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

    private ArrayList<String> data = new ArrayList<>();

    @Override
    public void run() {
        try {

            // Send data to the client
            BufferedReader input = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );

            // Receive data from the client, auto flushes to ensure data is sent
            PrintWriter output = new PrintWriter(socket.getOutputStream(), false);

            while (true) {
                String clientData = input.readLine();
                if (clientData != null) {
                    data.add(clientData);
                }
                if (data.get(0).equals("signUpButton") && data.size() == 5) {
                    AccountService as = new AccountService();
                    as.createAccount(data.get(1).charAt(0),data.get(2),data.get(3),data.get(4),"","");
                    data.clear();
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
