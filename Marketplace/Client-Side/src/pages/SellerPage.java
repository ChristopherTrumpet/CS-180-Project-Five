package pages;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SellerPage extends JFrame implements ActionListener {

    JButton removeStore = new JButton("Remove Store");
    JButton exitButton = new JButton("Exit");
    JButton logoutButton = new JButton("Logout");
    JButton accountDetailsButton = new JButton("Account Details");
    JButton statisticsButton = new JButton("Statistics");
    JButton storesButton = new JButton("Stores");

    JTable table;

    public SellerPage(String username) {

        // Set title of window
        this.setTitle("Marketplace");

        // Set behavior to "destroy" window when closed
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set dimensions of application
        this.setSize(800, 500);

        // Does not allow user to resize window
        this.setResizable(false);

        // Set window to open in the center of the screen
        this.setLocationRelativeTo(null);

        this.setLayout(null);

        JLabel welcomeMessage = new JLabel("Purdue Marketplace");
        welcomeMessage.setFont(new Font("serif", Font.BOLD, 18));
        welcomeMessage.setBounds(100, 8, 200, 24);

        JLabel nameMessage = new JLabel("Hey, " + username);
        nameMessage.setFont(new Font("Serif", Font.PLAIN, 14));
        nameMessage.setBounds(100, 24, 200, 24);

        storesButton.setBounds(100, 56, 200, 24);
        storesButton.addActionListener(this);

        statisticsButton.setBounds(100, 88, 200, 25);
        statisticsButton.addActionListener(this);

        accountDetailsButton.setBounds(100, 120, 200, 25);
        accountDetailsButton.addActionListener(this);

        logoutButton.setBounds(100, 416, 96, 25);
        logoutButton.addActionListener(this);

        exitButton.setBounds(204, 416, 96, 25);
        exitButton.addActionListener(this);

        JLabel storesLabel = new JLabel("Your Stores");
        storesLabel.setFont(new Font("Serif", Font.BOLD, 18));
        storesLabel.setBounds(325,8, 375, 24);

        JLabel helpfulLabel = new JLabel("Select a store to view");
        helpfulLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        helpfulLabel.setBounds(325, 24, 200, 24);

        // Create a DefaultTableModel
        DefaultTableModel model = new DefaultTableModel();

        // Add some data to the model
        model.addColumn("Stores");
        model.addColumn("Sales");

        model.addRow(new Object[]{"Apple", 30});
        model.addRow(new Object[]{"Five Guy's Burgers and Fries", 25});
        model.addRow(new Object[]{"Barnes and Noble", 35});
        model.addRow(new Object[]{"Keller Williams", 35034});
        model.addRow(new Object[]{"Raising Cane's", 9384});
        model.addRow(new Object[]{"Blackberry Farms", 9384});
        model.addRow(new Object[]{"General Electric", 9384});

        // Create a JTable using the model
        table = new JTable(model);

        table.setShowGrid(false);

        for (int c = 0; c < table.getColumnCount(); c++)
        {
            Class<?> col_class = table.getColumnClass(c);
            table.setDefaultEditor(col_class, null);        // remove editor
        }

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    System.out.println("Selected: " + table.getValueAt(table.getSelectedRow(), 0));
                }
            }
        });

        JScrollPane scrollPane= new  JScrollPane(table);
        scrollPane.setBounds(325, 56, 375, 350);

        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        if (defaults.get("Table.alternateRowColor") == null)
            defaults.put("Table.alternateRowColor", new Color(240, 240, 240));

        int padding = 8;
        int width = 375 / 3 - 4;
        JButton addStore = new JButton("Add Store");
        addStore.setBounds(325, 416, width, 24);
        JButton editStore = new JButton("Edit Store");
        editStore.setBounds(325 + width + padding, 416, width, 24);

        removeStore.addActionListener(this);
        removeStore.setBounds(325 + width * 2 + padding * 2, 416, width, 24);

        this.add(storesLabel);
        this.add(helpfulLabel);
        this.add(scrollPane);

        this.add(welcomeMessage);
        this.add(nameMessage);

        this.add(addStore);
        this.add(editStore);
        this.add(removeStore);
        this.add(storesButton);
        this.add(statisticsButton);
        this.add(accountDetailsButton);
        this.add(logoutButton);
        this.add(exitButton);

        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == removeStore) {
            int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete");
            if (input == 0) {
                ((DefaultTableModel)table.getModel()).removeRow(table.getSelectedRow());
            }
        }

        if (e.getSource() == logoutButton) {
            int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout");
            if (input == 0) {
                this.dispose();
                new OnboardingPage(null);
            }
        }

        if (e.getSource() == exitButton) {
            int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit");
            if (input == 0) {
                this.dispose();
            }
        }

    }
}
