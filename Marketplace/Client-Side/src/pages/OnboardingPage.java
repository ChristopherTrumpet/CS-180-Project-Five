package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class OnboardingPage extends JFrame implements ActionListener {

    JLabel identifierLabel = new JLabel("Email or Username");
    JTextField identifierField = new JTextField(16);
    JLabel passwordLabel = new JLabel("Password");
    JPasswordField passwordField = new JPasswordField(16);
    JButton loginButton = new JButton("Login");
    JButton signUpButton = new JButton("Create a new account");

    HashMap<String, String> users = new HashMap<>();

    public OnboardingPage(HashMap<String, String> users) {

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
        this.setLayout(null);

        int start = 130;

        this.setBackground(Color.decode("#f4f4f4"));

        JLabel welcomeMessage = new JLabel("Purdue Marketplace");
        welcomeMessage.setFont(new Font("Serif", Font.PLAIN, 36));
        welcomeMessage.setBounds(250, start-48, 300, 36);

        identifierLabel.setBounds(250,start,300,24);
        identifierField.setBounds(250, start + 24, 300, 24); // 24

        passwordLabel.setBounds(250, start + 48, 300, 24); // 24
        passwordField.setBounds(250, start + 72, 300, 24); // 24

        loginButton.setBackground(Color.decode("#A77F20"));
        loginButton.setForeground(Color.white);
        loginButton.setBorder(null);
        loginButton.setBounds(250, start + 120, 300, 24); // 48
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);

        signUpButton.setBackground(Color.decode("#f4f4f4"));
        signUpButton.setForeground(Color.decode("#A77F20"));
        signUpButton.setBorder(null);
        signUpButton.setOpaque(false);
        signUpButton.setBounds(250, start + 120 + 32, 300, 24); // 48
        signUpButton.setFocusable(false);
        signUpButton.addActionListener(this);

        this.add(welcomeMessage);
        this.add(identifierLabel);
        this.add(identifierField);
        this.add(passwordLabel);
        this.add(passwordField);
        this.add(loginButton);
        this.add(signUpButton);

        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String identifier = identifierField.getText();
            String password = String.valueOf(passwordField.getPassword());
            this.dispose();
            new SellerPage(identifier);

//            if (accountType == 'b') {
//                CustomerPage customerPage = new CustomerPage(identifier);
//            } else {
//                SellerPage sellerPage = new SellerPage();
//            }
        }
    }
}
