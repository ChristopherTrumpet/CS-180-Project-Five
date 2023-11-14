package pages;

import javax.swing.*;

public class SellerPage {

    private final JFrame window;

    public SellerPage() {
        window = new JFrame();

        // Set title of window
        window.setTitle("Customer");

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
}
