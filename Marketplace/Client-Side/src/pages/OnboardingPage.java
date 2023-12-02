package pages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OnboardingPage extends JFrame implements ActionListener {

    private final JFrame window;

    public OnboardingPage() {
        window = new JFrame();

        // Set title of window
        window.setTitle("Sign Up");

        // Set behavior to "destroy" window when closed
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set dimensions of application
        window.setSize(800, 500);

        // Set window to open in the center of the screen
        window.setLocationRelativeTo(null);
    }

    public void show() {
        window.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
