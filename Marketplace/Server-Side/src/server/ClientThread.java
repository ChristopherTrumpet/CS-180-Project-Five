package server;

import services.AccountService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread {

    private final Socket socket;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    private final ArrayList<String> data = new ArrayList<>();

    @Override
    public void run() {
        try {

            // Send data to the client
            BufferedReader input = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );

            AccountService as = new AccountService();

            while (true) {
                String clientData = input.readLine();
                if (clientData != null) {
                    data.add(clientData);
                }

                switch (data.get(0)) {
                    case "signUpButton" -> {
                        if (data.size() == 5) {
                            as.createAccount(data.get(1).charAt(0),data.get(2),data.get(3),data.get(4));
                            data.clear();
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
