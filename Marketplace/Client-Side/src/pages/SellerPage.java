package pages;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SellerPage extends JFrame {

    JButton removeStore = new JButton("Remove Store");
    JButton exitButton = new JButton("Exit");
    JButton logoutButton = new JButton("Logout");
    JButton accountDetailsButton = new JButton("Account Details");
    JButton importProductsButton = new JButton("Import Products");
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
        storesButton.addActionListener(e -> {

        });

        statisticsButton.setBounds(100, 88, 200, 25);
        statisticsButton.addActionListener(e -> {

        });

        accountDetailsButton.setBounds(100, 120, 200, 25);
        accountDetailsButton.addActionListener(e -> {

        });

        importProductsButton.setBounds(100, 152, 200, 25);
        importProductsButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(".csv","csv");
            chooser.setFileFilter(filter);
            chooser.showSaveDialog(null);
        });

        logoutButton.setBounds(100, 416, 96, 25);
        logoutButton.addActionListener(e -> {
            int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout");
            if (input == 0) {
                this.dispose();
                new OnboardingPage(null, true);
            }
        });

        exitButton.setBounds(204, 416, 96, 25);
        exitButton.addActionListener(e -> {
            int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit");
            if (input == 0) {
                this.dispose();
            }
        });

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

        model.addRow(new Object[]{"Apple", "$20"});
        model.addRow(new Object[]{"Five Guy's Burgers and Fries", "$25.32"});
        model.addRow(new Object[]{"Barnes and Noble", "$35.11"});
        model.addRow(new Object[]{"Keller Williams", "$503,494,240,240.34"});
        model.addRow(new Object[]{"Raising Cane's", "$9384.67"});
        model.addRow(new Object[]{"Blackberry Farms", "$9384.24"});
        model.addRow(new Object[]{"General Electric", "$9384.52"});

        // Create a JTable using the model
        table = new JTable(model);

        for (int c = 0; c < table.getColumnCount(); c++)
        {
            Class<?> col_class = table.getColumnClass(c);
            table.setDefaultEditor(col_class, null);        // remove editor
        }

        table.getColumnModel().getColumn(1).setPreferredWidth(135);
        table.getColumnModel().getColumn(1).setMaxWidth(135);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    editStore(table.getValueAt(table.getSelectedRow(), 0).toString());
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
        addStore.addActionListener(e -> {
            addStore();
        });

        JButton sortStoreButton = new JButton("Sort Stores");
        sortStoreButton.setBounds(325 + width + padding, 416, width, 24);
        sortStoreButton.addActionListener(e -> {
            System.out.println("Sorting stores...");
        });

        removeStore.addActionListener(e -> {
            int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete");
            if (input == 0) {
                ((DefaultTableModel)table.getModel()).removeRow(table.getSelectedRow());
            }
        });

        removeStore.setBounds(325 + width * 2 + padding * 2, 416, width, 24);

        this.add(storesLabel);
        this.add(helpfulLabel);
        this.add(scrollPane);

        this.add(welcomeMessage);
        this.add(nameMessage);

        this.add(addStore);
        this.add(sortStoreButton);
        this.add(removeStore);
        this.add(storesButton);
        this.add(statisticsButton);
        this.add(accountDetailsButton);
        this.add(importProductsButton);
        this.add(logoutButton);
        this.add(exitButton);

        this.setVisible(true);
    }

    public void addStore() {

        // Create a dialog box with a text field and a button
        String storeName = JOptionPane.showInputDialog("Enter store name", "Store");
        System.out.println("User created store: " + storeName);

        // Create a DefaultTableModel
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{storeName, "$0.00"});


    }

    public void editStore(String storeName) {

        JFrame storePage = new JFrame();

        // Set title of window
        storePage.setTitle("Store Details");

        // Set behavior to "destroy" window when closed
        storePage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set dimensions of application
        storePage.setSize(640, 480);

        // Does not allow user to resize window
        storePage.setResizable(false);

        // Set window to open in the center of the screen
        storePage.setLocationRelativeTo(null);

        storePage.setLayout(null);

        DefaultTableModel model = new DefaultTableModel();

        // Add some data to the model
        model.addColumn("Product Name");
        model.addColumn("Quantity");
        model.addColumn("Price");

        model.addRow(new Object[]{"iPhone", "1000", "$999.99"});
        model.addRow(new Object[]{"MacBook", "75", "$1499.99"});
        model.addRow(new Object[]{"AirPods", "225", "199.99"});

        // Create a JTable using the model
        JTable productTable = new JTable(model);

        JLabel titleMessage = new JLabel(storeName + "'s Products");
        titleMessage.setFont(new Font("serif", Font.BOLD, 18));
        titleMessage.setBounds(233, 24, 365, 24);

        JLabel optionsTitle = new JLabel("Options");
        optionsTitle.setFont(new Font("serif", Font.BOLD, 18));
        optionsTitle.setBounds(24, 24, 200, 24);

        JButton changeStoreName = new JButton("Change Store Name");
        changeStoreName.setBounds(24, 56, 185, 24);

        JButton addProduct = new JButton("Add Product");
        addProduct.setBounds(24, 88, 185, 24);

        JButton removeProduct = new JButton("Remove Product");
        removeProduct.setBounds(24, 120, 185, 24);
        removeProduct.addActionListener(e -> {
            if (!productTable.getSelectionModel().isSelectionEmpty()) {
                int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + productTable.getValueAt(productTable.getSelectedRow(), 0) + "?");
                if (input == 0) {
                    ((DefaultTableModel)productTable.getModel()).removeRow(productTable.getSelectedRow());
                }
            }
        });

        JButton closeProduct = new JButton("Close");
        closeProduct.setBounds(24, 397, 185, 24);
        closeProduct.addActionListener(e -> {
            storePage.dispose();
        });

        JScrollPane sp= new  JScrollPane(productTable);
        sp.setBounds(233, 56, 365, 365);

        storePage.add(optionsTitle);
        storePage.add(changeStoreName);
        storePage.add(addProduct);
        storePage.add(removeProduct);
        storePage.add(closeProduct);
        storePage.add(titleMessage);
        storePage.add(sp);

        storePage.setVisible(true);



    }
}
