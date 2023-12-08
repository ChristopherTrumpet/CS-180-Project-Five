import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private static BufferedReader reader;
    private static PrintWriter stringToServer;

    public static void main(String[] args) {

        // MAKE SURE the port number is IDENTICAL to that of the server
        try (Socket socket = new Socket("localhost", 5000)) {

            socket.setSoTimeout(5000);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            stringToServer = new PrintWriter(socket.getOutputStream(), false);

            // Initialize GUI for Program
            SwingUtilities.invokeLater(() -> new OnboardingPage(true));

            Scanner scanner = new Scanner(System.in);
            String echoString;

            do {
                echoString = scanner.nextLine();

                // Send input result to server
                stringToServer.println(echoString);

            } while (!echoString.equals("exit"));

        } catch (SocketTimeoutException e) {
            System.err.println("The socket timed out");

        } catch (IOException e) {
            System.err.println("Client Error: " + e.getMessage());
        }
    }

    public static void sendToServer(ArrayList<String> lines) {
        for (String line : lines) {
            stringToServer.println(line);
        }
        stringToServer.flush();
    }
    public static ArrayList<String> readFromServer(int numLines) {
        try {
            ArrayList<String> data = new ArrayList<>();

            for (int i = 0; i < numLines; i++) {
                data.add(reader.readLine());
            }
            return data;
        } catch(IOException e) {
            showErrorMessage("An error has occurred trying to read from the server");
            return null;
        }
    }

    public static void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
