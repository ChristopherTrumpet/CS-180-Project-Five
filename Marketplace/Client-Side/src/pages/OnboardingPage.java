package pages;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class OnboardingPage extends JFrame {
    CardLayout cardLayout = new CardLayout();
    Container container;
    boolean firstTime;

    JFrame reference;

    HashMap<String, String> users = new HashMap<>();

    public OnboardingPage(HashMap<String, String> users, boolean firstTime) {

        this.reference = this;
        this.firstTime = firstTime;
        container = getContentPane();

        // Set title of window
        this.setTitle("Login Page");

        // Set behavior to "destroy" window when closed
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set dimensions of application
        this.setSize(800, 500);

        // Does not allow user to resize window
        this.setResizable(false);

        // Set window to open in the center of the screen
        this.setLocationRelativeTo(null);

        this.setBackground(Color.decode("#f4f4f4"));

        JPanel login = new LoginPanel();
        JPanel signUp = new SignUpPanel();

        container.setLayout(cardLayout);
        container.add("a", login);
        container.add("b", signUp);

        container.setVisible(true);
        this.setVisible(true);

    }

    public class LoginPanel extends JPanel {
        JLabel applicationNameLabel = new JLabel("Purdue Marketplace");
        JLabel identifierLabel = new JLabel("Email or Username");
        JTextField identifierField = new JTextField(16);
        JLabel passwordLabel = new JLabel("Password");
        JPasswordField passwordField = new JPasswordField(16);
        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create a new account");

        public LoginPanel() {

            this.setLayout(null);

            applicationNameLabel.setFont(new Font("Serif", Font.PLAIN, 36));
            applicationNameLabel.setBounds(250, 130-48, 300, 36);

            int start = 130;

            identifierLabel.setBounds(250,start,300,24);
            identifierField.setBounds(250, start + 24, 300, 24); // 24

            passwordLabel.setBounds(250, start + 48, 300, 24); // 24
            passwordField.setBounds(250, start + 72, 300, 24); // 24

            loginButton.setBackground(Color.decode("#A77F20"));
            loginButton.setForeground(Color.white);
            loginButton.setBorder(null);
            loginButton.setBounds(250, start + 112, 300, 24); // 48
            loginButton.setFocusable(false);

            loginButton.addActionListener(e -> {
                reference.dispose();
                new SellerPage("John Doe");
                cardLayout.next(container);
            });

            createAccountButton.setBackground(Color.decode("#f4f4f4"));
            createAccountButton.setForeground(Color.decode("#A77F20"));
            createAccountButton.setBorder(null);
            createAccountButton.setOpaque(false);
            createAccountButton.setBounds(250, start + 112 + 32, 300, 24); // 48
            createAccountButton.setFocusable(false);

            createAccountButton.addActionListener(e -> {
                cardLayout.next(container);
            });

            this.add(applicationNameLabel);
            this.add(identifierLabel);
            this.add(identifierField);
            this.add(passwordLabel);
            this.add(passwordField);
            this.add(loginButton);
            this.add(createAccountButton);
        }
    }

    public class SignUpPanel extends JPanel {
        JLabel applicationNameLabel = new JLabel("Purdue Marketplace");
        JLabel emailLabel = new JLabel("Email");
        JTextField emailField = new JTextField(16);
        JLabel passwordLabel = new JLabel("Password");
        JPasswordField passwordField = new JPasswordField(16);
        JLabel usernameLabel = new JLabel("Username");
        JTextField usernameField = new JTextField(16);
        JButton signUpButton = new JButton("Sign up");
        JButton existingAccountButton = new JButton("Login to existing account");

        public SignUpPanel() {

            this.setLayout(null);

            int start = 130;

            applicationNameLabel.setFont(new Font("Serif", Font.PLAIN, 36));
            applicationNameLabel.setBounds(250, start-48, 300, 36);

            emailLabel.setBounds(250,start,300,24);
            emailField.setBounds(250, start + 24, 300, 24); // 24

            usernameLabel.setBounds(250, start + 48, 300, 24); // 24
            usernameField.setBounds(250, start + 72, 300, 24); // 24

            passwordLabel.setBounds(250, start + 96, 300, 24);
            passwordField.setBounds(250, start + 120, 300, 24);

            signUpButton.setBackground(Color.decode("#A77F20"));
            signUpButton.setForeground(Color.white);
            signUpButton.setBorder(null);
            signUpButton.setBounds(250, start + 160, 300, 24); // 48
            signUpButton.setFocusable(false);
            signUpButton.addActionListener(e -> {
                reference.dispose();
                new SellerPage("John Doe");
            });

            existingAccountButton.setBackground(Color.decode("#f4f4f4"));
            existingAccountButton.setForeground(Color.decode("#A77F20"));
            existingAccountButton.setBorder(null);
            existingAccountButton.setOpaque(false);
            existingAccountButton.setBounds(250, start + 160 + 32, 300, 24); // 48
            existingAccountButton.setFocusable(false);
            existingAccountButton.addActionListener(e -> {
                cardLayout.next(container);
            });

            this.add(applicationNameLabel);
            this.add(emailLabel);
            this.add(emailField);
            this.add(usernameLabel);
            this.add(usernameField);
            this.add(passwordLabel);
            this.add(passwordField);
            this.add(existingAccountButton);
            this.add(signUpButton);
        }
    }
}
