package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerPage extends JFrame implements ActionListener {

    public CustomerPage(String username) {

        // Set title of window
        this.setTitle("Hey there, " + username);

        // Set behavior to "destroy" window when closed
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set dimensions of application
        this.setSize(800, 500);

        // Does not allow user to resize window
        this.setResizable(false);

        // Set window to open in the center of the screen
        this.setLocationRelativeTo(null);

        this.setLayout(null);

        String[] stores = new String[3];
        stores[0] = "Apple";
        stores[1] = "Samsung";
        stores[2] = "Macy's";

        JLabel storesLabel = new JLabel("Your stores: ");
        storesLabel.setBounds(250,26, 300, 24);

        JList<String> storeList = new JList<>(stores);
        storeList.setBounds(250, 50, 300, 350);

        JButton addStore = new JButton("Add Store");
        addStore.setBounds(250, 400, 100, 24);
        JButton editStore = new JButton("Edit Store");
        editStore.setBounds(250 + 100, 400, 100, 24);
        JButton removeStore = new JButton("Remove Store");
        removeStore.setBounds(250 + 200, 400, 100, 24);

        this.add(storesLabel);
        this.add(storeList);

        this.add(addStore);
        this.add(editStore);
        this.add(removeStore);

        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
