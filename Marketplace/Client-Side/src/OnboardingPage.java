import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class OnboardingPage extends JFrame {
    CardLayout cardLayout = new CardLayout();
    Container container;
    boolean firstTime;

    JFrame reference;

    public OnboardingPage(boolean firstTime) {

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
        JLabel loginLabel = new JLabel("Welcome back!");
        JLabel identifierLabel = new JLabel("Email or Username");
        JTextField identifierField = new JTextField(16);
        JLabel passwordLabel = new JLabel("Password");
        JPasswordField passwordField = new JPasswordField(16);
        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create a new account");

        public LoginPanel() {

            this.setLayout(null);

            applicationNameLabel.setFont(new Font("Serif", Font.PLAIN, 36));
            applicationNameLabel.setBounds(250, 54, 300, 36);

            loginLabel.setFont(new Font("sans-serif", Font.PLAIN, 16));
            loginLabel.setBounds(250, 90, 300, 24);

            int start = 130;

            identifierLabel.setBounds(250,start,300,24);
            identifierField.setBounds(250, start + 24, 300, 24); // 24

            passwordLabel.setBounds(250, start + 48, 300, 24); // 24
            passwordField.setBounds(250, start + 72, 300, 24); // 24
            passwordField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                String password = new String(passwordField.getPassword());

                                if(!(identifierField.getText().isEmpty() || password.isEmpty())) {

                                    ArrayList<String> data = new ArrayList<>();

                                    data.add("[loginButton]");
                                    data.add(identifierField.getText());
                                    data.add(password);

                                    Client.sendToServer(data);

                                    data = Client.readFromServer(2);

                                    if (data.get(0).equals("true")) {
                                        JSONObject user = new JSONObject(data.get(1));
                                        if (user.getString("id").endsWith("b")) {
                                            new CustomerPage(user);
                                        } else {
                                            new SellerPage(user);
                                        }
                                        reference.dispose();
                                        cardLayout.next(container);
                                    } else {
                                        Client.showErrorMessage("User does not exist. " +
                                                "Make sure you have typed in your username and password correctly.");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog (null, "Please make sure all fields are populated!", "Empty Fields", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        });
                    }
                }
            });

            loginButton.setBackground(Color.decode("#A77F20"));
            loginButton.setOpaque(true);
            loginButton.setForeground(Color.white);
            loginButton.setBorder(null);
            loginButton.setBounds(250, start + 112, 300, 24); // 48
            loginButton.setFocusable(false);

            loginButton.addActionListener(e -> {

                String password = new String(passwordField.getPassword());

                if(!(identifierField.getText().isEmpty() || password.isEmpty())) {

                    ArrayList<String> data = new ArrayList<>();

                    data.add("[loginButton]");
                    data.add(identifierField.getText());
                    data.add(password);

                    Client.sendToServer(data);

                    data = Client.readFromServer(2);

                    if (data.get(0).equals("true")) {
                        JSONObject user = new JSONObject(data.get(1));
                        if (user.getString("id").endsWith("b")) {
                            new CustomerPage(user);
                        } else {
                            new SellerPage(user);
                        }
                        reference.dispose();
                        cardLayout.next(container);
                    } else {
                        Client.showErrorMessage("User does not exist. " +
                                "Make sure you have typed in your username and password correctly.");
                    }
                } else {
                    JOptionPane.showMessageDialog (null, "Please make sure all fields are populated!", "Empty Fields", JOptionPane.INFORMATION_MESSAGE);
                }
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
            this.add(loginLabel);
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
        JLabel signUpLabel = new JLabel("Let's create an account!");
        JLabel emailLabel = new JLabel("Email");
        JTextField emailField = new JTextField(16);
        JLabel passwordLabel = new JLabel("Password");
        JPasswordField passwordField = new JPasswordField(16);
        JLabel usernameLabel = new JLabel("Username");
        JTextField usernameField = new JTextField(16);
        JButton signUpButton = new JButton("Sign up");
        JButton existingAccountButton = new JButton("Login to existing account");
        JRadioButton buyerType, sellerType;

        public SignUpPanel() {

            this.setLayout(null);

            int start = 130;

            applicationNameLabel.setFont(new Font("Serif", Font.PLAIN, 36));
            applicationNameLabel.setBounds(250, 54, 300, 36);

            signUpLabel.setFont(new Font("sans-serif", Font.PLAIN, 16));
            signUpLabel.setBounds(250, 90, 300, 24);

            emailLabel.setBounds(250,start,300,24);
            emailField.setBounds(250, start + 24, 300, 24); // 24

            usernameLabel.setBounds(250, start + 48, 300, 24); // 24
            usernameField.setBounds(250, start + 72, 300, 24); // 24

            passwordLabel.setBounds(250, start + 96, 300, 24);
            passwordField.setBounds(250, start + 120, 300, 24);

            ButtonGroup accountType = new ButtonGroup();

            buyerType = new JRadioButton();
            sellerType = new JRadioButton();

            accountType.add(buyerType);
            accountType.add(sellerType);

            JLabel accountTypeLabel = new JLabel("Account Type");
            accountTypeLabel.setBounds(250, 274, 300, 24);

            buyerType.setText("Buyer");
            buyerType.setBounds(250, 294, 75, 24);
            sellerType.setText("Seller");
            sellerType.setBounds(250+75,294, 75, 24);

            signUpButton.setBackground(Color.decode("#A77F20"));
            signUpButton.setOpaque(true);
            signUpButton.setForeground(Color.white);
            signUpButton.setBorder(null);
            signUpButton.setBounds(250, 326, 300, 24); // 48
            signUpButton.setFocusable(false);
            signUpButton.addActionListener(e -> {
                String password = String.valueOf(passwordField.getPassword());
                if (!emailField.getText().isEmpty() && !usernameField.getText().isEmpty() && !password.isEmpty()) {
                    if (sellerType.isSelected() || buyerType.isSelected()) {
                        ArrayList<String> data = new ArrayList<>();

                        data.add("[signUpButton]");

                        if (sellerType.isSelected()) {
                            data.add("s");
                        } else {
                            data.add("b");
                        }

                        data.add(usernameField.getText());
                        data.add(password);
                        data.add(emailField.getText());

                        Client.sendToServer(data);

                        String userString = Client.readFromServer(1).get(0);
                        JSONObject user = new JSONObject(userString);

                        reference.dispose();

                        if (sellerType.isSelected()) {
                            new SellerPage(user);
                        } else {
                            new CustomerPage(user);
                        }
                    }
                }
                else {
                    Client.showErrorMessage("Please make sure no fields are empty");
                }
            });

            existingAccountButton.setBackground(Color.decode("#f4f4f4"));
            existingAccountButton.setForeground(Color.decode("#A77F20"));
            existingAccountButton.setBorder(null);
            existingAccountButton.setOpaque(false);
            existingAccountButton.setBounds(250, 326 + 32, 300, 24); // 48
            existingAccountButton.setFocusable(false);
            existingAccountButton.addActionListener(e -> {
                cardLayout.next(container);
            });

            this.add(applicationNameLabel);
            this.add(signUpLabel);
            this.add(emailLabel);
            this.add(emailField);
            this.add(usernameLabel);
            this.add(usernameField);
            this.add(passwordLabel);
            this.add(passwordField);
            this.add(accountTypeLabel);
            this.add(buyerType);
            this.add(sellerType);
            this.add(existingAccountButton);
            this.add(signUpButton);
        }
    }
}
