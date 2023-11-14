import pages.OnboardingPage;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        // MAKE SURE the port number is IDENTICAL to that of the server
        try (Socket socket = new Socket("localhost", 5000)) {

            socket.setSoTimeout(5000);

            BufferedReader echoes = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter stringToEcho = new PrintWriter(socket.getOutputStream(), true);

            // Initialize GUI for Program
            SwingUtilities.invokeLater(() -> {
                OnboardingPage onboardingPage = new OnboardingPage();
                onboardingPage.show();
            });

            Scanner scanner = new Scanner(System.in);
            String echoString;
            String reponse;

            do {
                System.out.println("Enter string to be echoed: ");
                echoString = scanner.nextLine();

                // Send input result to server
                stringToEcho.println(echoString);

                if (!echoString.equals("exit")) {
                    reponse = echoes.readLine();
                    System.out.println(reponse);
                }
            } while (!echoString.equals("exit"));

        } catch (SocketTimeoutException e) {
            System.err.println("The socket timed out");

        } catch (IOException e) {
            System.err.println("Client Error: " + e.getMessage());
        }
    }
}
