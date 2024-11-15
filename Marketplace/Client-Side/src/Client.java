import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Client
 * <p>
 * sends UI input data to the Server.
 *
 * @author Chris Trumpet, Matthew Lee, Mohit Ambe, Shrinand Perumal, Vraj Patel
 * @version December 11, 2023
 */
public class Client {
    public static Socket clientSocket;
    public static Scanner scanner;
    private static BufferedReader reader;
    private static PrintWriter stringToServer;

    public static void main(String[] args) {

        // MAKE SURE the port number is IDENTICAL to that of the server
        try (Socket socket = new Socket("localhost", 9080)) {

            clientSocket = socket;
            socket.setSoTimeout(5000);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            stringToServer = new PrintWriter(socket.getOutputStream(), false);

            try {
                // Set cross-platform Java L&F (also called "Metal")
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (UnsupportedLookAndFeelException e) {
                System.out.println("System does not support this library");
            } catch (ClassNotFoundException e) {
                // handle exception
                System.out.println("Library could not be found..");
            } catch (InstantiationException | IllegalAccessException e) {
                System.out.println("Error occurred.");
            }

            // Initialize GUI for Program
            SwingUtilities.invokeLater(() -> new OnboardingPage(true));

            scanner = new Scanner(System.in);
            String echoString;

            do {
                echoString = scanner.nextLine();
            } while (!echoString.equals("exit"));

            System.out.println("[CLIENT] Leaving...");

        } catch (SocketTimeoutException e) {
            System.err.println("The socket timed out");

        } catch (IOException e) {
            System.err.println("Client Error: " + e.getMessage());
        }
    }

    public static void sendToServer(String function, String... dataValues) {

        stringToServer.println(function);

        if (dataValues != null) {
            for (String value : dataValues) {
                stringToServer.println(value);
            }
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
        } catch (IOException e) {
            showErrorMessage("An error has occurred trying to read from the server");
            return null;
        }
    }

    public static void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void closeConnection() {
        try {
            sendToServer("quit");
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
