package backend.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

            // Receive data from the client, auto flushes to ensure data is sent
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String echoString = input.readLine();
                System.out.println("Received client input: " + echoString);

                if (echoString.equals("exit"))
                    break;

                output.println(echoString);
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
