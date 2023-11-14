package server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main(String[] args) {

        // Arbitrary port number
        try (ServerSocket serverSocket = new ServerSocket(5000)) {

            while (true) {
                new ClientThread(serverSocket.accept()).start();
            }

        } catch (IOException e) {
            System.err.println("Server exception " + e.getMessage());
        }

    }
}
