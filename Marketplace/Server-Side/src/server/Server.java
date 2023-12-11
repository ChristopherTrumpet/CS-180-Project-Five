package server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Server
 * <p>
 * creates a server to receive Client data.
 *
 * @author Chris Trumpet, Matthew Lee, Mohit Ambe, Shrinand Perumal, Vraj Patel
 * @version December 11, 2023
 */
public class Server {
    public static void main(String[] args) {

        System.out.println("[SERVER] Server initialized...");

        // Arbitrary port number
        try (ServerSocket serverSocket = new ServerSocket(9080)) {

            while (true) {
                new ClientThread(serverSocket.accept()).start();
                System.out.println("[SERVER] Client connected!");

            }

        } catch (IOException e) {
            System.err.println("Server exception " + e.getMessage());
        }

    }
}
