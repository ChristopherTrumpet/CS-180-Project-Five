package server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main(String[] args) {

        System.out.println("[SERVER] Server initialized...");

        // Arbitrary port number
        try (ServerSocket serverSocket = new ServerSocket(5000)) {

            while (true) {
                new ClientThread(serverSocket.accept()).start();
                System.out.println("[SERVER] Client connected!");

            }

        } catch (IOException e) {
            System.err.println("Server exception " + e.getMessage());
        }

    }
}
