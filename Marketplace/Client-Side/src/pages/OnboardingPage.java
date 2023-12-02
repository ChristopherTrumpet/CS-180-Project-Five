package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OnboardingPage extends JFrame implements ActionListener {

    public OnboardingPage() {

        // Set title of window
        this.setTitle("Login Page");

        // Set behavior to "destroy" window when closed
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set dimensions of application
        this.setSize(800, 500);

        // Set window to open in the center of the screen
        this.setLocationRelativeTo(null);

        this.setLayout(new FlowLayout());

        JLabel identifierLabel = new JLabel("Email or Username");
        JTextField identifierField = new JTextField();

        JLabel passwordLabel = new JLabel("Password");
        JPasswordField passwordField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        JButton signUpBtn = new JButton("Sign Up");

        this.add(identifierLabel);
        this.add(identifierField);

        this.add(passwordLabel);
        this.add(passwordField);

        this.add(loginBtn);
        this.add(signUpBtn);

        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
