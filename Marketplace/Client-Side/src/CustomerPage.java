import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;

public class CustomerPage extends JFrame {
    CardLayout cardLayout = new CardLayout();
    Container container = new Container();
    JFrame reference;
    JTable table;
    JSONObject buyer;

    public CustomerPage(JSONObject buyer) {

        this.buyer = buyer;
        this.reference = this;

        // Set title of window
        this.setTitle("Purdue Marketplace (BUYER)");

        // Set behavior to "destroy" window when closed
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set dimensions of application
        this.setSize(800, 500);

        // Does not allow user to resize window
        this.setResizable(false);

        // Set window to open in the center of the screen
        this.setLocationRelativeTo(null);

        this.setLayout(new BorderLayout());

        JPanel sidePanel = SidePanel();

        container.setLayout(cardLayout);
        container.add("stores", Stores());
        container.add("settings", settings(sidePanel));
        container.add("statistics", statistics());
        container.add("cart", cart());
        container.setVisible(true);

        this.add(sidePanel, BorderLayout.WEST);
        this.add(container, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public JPanel SidePanel() {

        JPanel panel = new JPanel();
        GridBagLayout gridLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,80,0,24);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        panel.setLayout(gridLayout);

        JLabel welcomeMessage = new JLabel("Purdue Marketplace");
        welcomeMessage.setFont(new Font("serif", Font.BOLD, 18));
        c.gridy = 0;
        c.gridwidth = 4;
        gridLayout.setConstraints(welcomeMessage, c);
        panel.add(welcomeMessage);

        c.insets = new Insets(0,80,8,24);

        JTextArea nameMessage = new JTextArea("Hey, " + buyer.getString("username"));
        nameMessage.setFont(new Font("sans-serif", Font.PLAIN, 14));
        nameMessage.setLineWrap(true);
        nameMessage.setWrapStyleWord(true);
        nameMessage.setFocusable(false);
        nameMessage.setEditable(false);
        nameMessage.setOpaque(false);
        c.gridy = 1;
        c.gridwidth = 4;
        gridLayout.setConstraints(nameMessage,c);
        panel.add(nameMessage);

        c.insets = new Insets(0,80,0,24);

        JSeparator accountDetailsDivider = new JSeparator(JSeparator.HORIZONTAL);
        accountDetailsDivider.setMinimumSize(new Dimension(150, 4));
        accountDetailsDivider.setPreferredSize(new Dimension(150, 4));
        accountDetailsDivider.setBackground(Color.decode("#dbdbdb"));
        accountDetailsDivider.setForeground(Color.decode("#dbdbdb"));
        c.gridy = 2;
        c.gridwidth = 4;
        gridLayout.setConstraints(accountDetailsDivider, c);
        panel.add(accountDetailsDivider);

        c.insets = new Insets(4,80,4,24);

        JLabel balanceMessage = new JLabel("Balance: $" + buyer.getDouble("funds"));
        balanceMessage.setFont(new Font("sans-serif", Font.PLAIN, 14));
        c.gridy = 3;
        c.gridwidth = 4;
        gridLayout.setConstraints(balanceMessage,c);
        balanceMessage.setMaximumSize(new Dimension(300, 24));
        panel.add(balanceMessage);

        c.insets = new Insets(4,80,4,24);

        JButton storesButton = new JButton("Marketplace");
        storesButton.addActionListener(e -> cardLayout.show(container, "stores"));
        c.gridy = 4;
        c.gridwidth = 4;
        gridLayout.setConstraints(storesButton, c);
        panel.add(storesButton);

        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e -> cardLayout.show(container, "settings"));
        c.gridy = 5;
        c.gridwidth = 4;
        gridLayout.setConstraints(settingsButton, c);
        panel.add(settingsButton);

        JButton statisticsButton = new JButton("Statistics");
        statisticsButton.addActionListener(e -> cardLayout.show(container, "statistics"));
        c.gridy = 6;
        c.gridwidth = 4;
        gridLayout.setConstraints(statisticsButton, c);
        panel.add(statisticsButton);

        JButton fundsButton = new JButton("Add Funding");
        fundsButton.addActionListener(e -> {
            String funding = JOptionPane.showInputDialog(null, "How much would you like to add?", "Funding Panel", JOptionPane.QUESTION_MESSAGE);

            if (funding != null && !funding.isEmpty()) {
                balanceMessage.setText("Balance: $" + funding);
                JOptionPane.showMessageDialog (null, "Succesfully added $" + funding + " to your account!", "Funding Panel", JOptionPane.INFORMATION_MESSAGE);
            }

        });
        c.gridy = 7;
        c.gridwidth = 4;
        gridLayout.setConstraints(fundsButton, c);
        panel.add(fundsButton);

        JButton importProductsButton = new JButton("View Cart");
        importProductsButton.addActionListener(e -> {
            cardLayout.show(container, "cart");
        });
        c.gridy = 8;
        c.gridwidth = 4;
        gridLayout.setConstraints(importProductsButton, c);
        panel.add(importProductsButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout");
            if (input == 0) {
                reference.dispose();
                new OnboardingPage(true);
            }
        });
        c.gridy = 9;
        c.gridwidth = 2;
        c.gridx = 0;
        c.insets = new Insets(4,80,4,4);
        gridLayout.setConstraints(logoutButton, c);
        panel.add(logoutButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit");
            if (input == 0) {
                reference.dispose();
                Client.closeConnection();
            }
        });
        c.gridy = 9;
        c.gridx = 2;
        c.gridwidth = 2;
        c.insets = new Insets(4,4,4,24);
        gridLayout.setConstraints(exitButton, c);
        panel.add(exitButton);

        return panel;
    }

    public JPanel cart() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel storesPanel = new JPanel();
        storesPanel.setLayout(null);

        JLabel accountDetailsLabel = new JLabel("Your Cart");
        accountDetailsLabel.setFont(new Font("Serif", Font.BOLD, 18));
        accountDetailsLabel.setBounds(24, 16, 200, 24);

        JLabel supportLabel = new JLabel("View your cart contents and make purchase");
        supportLabel.setFont(new Font("serif", Font.PLAIN, 14));
        supportLabel.setBounds(24, 36, 400, 24);

        storesPanel.add(accountDetailsLabel);
        storesPanel.add(supportLabel);

        // Create a DefaultTableModel
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Product");
        model.addColumn("Quantity");
        model.addColumn("Price");

        Client.sendToServer(new ArrayList<>(List.of("[getProducts]")));
        String allProductsString = Objects.requireNonNull(Client.readFromServer(1)).get(0);

        JSONArray cart = buyer.getJSONArray("cart");
        double totalCost = 0.0;

        for (Object contents : cart) {
            for (Object product : new JSONArray(allProductsString)) {
                if (((JSONObject) product).getString("id").equals(((JSONObject) contents).getString("product_id")))
                {
                    totalCost += ((JSONObject) contents).getInt("quantity") * ((JSONObject) contents).getDouble("price");
                    model.addRow(new Object[]{((JSONObject) product).getString("name"), ((JSONObject) contents).getInt("quantity"), ((JSONObject) contents).getDouble("price")});
                }
            }
        }

        // Create a JTable using the model
        JTable cartTable = new JTable(model);

        for (int c = 0; c < cartTable.getColumnCount(); c++)
        {
            Class<?> col_class = cartTable.getColumnClass(c);
            cartTable.setDefaultEditor(col_class, null);        // remove editor
        }

        cartTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
            JTable table =(JTable) mouseEvent.getSource();
            if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                viewProductPage(new JSONObject(table.getModel().getValueAt(table.getSelectedRow(), 3).toString()), new JSONObject(table.getModel().getValueAt(table.getSelectedRow(), 4).toString()));
            }
            }
        });

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(cartTable.getModel());
        cartTable.setRowSorter(sorter);

        // SORTING BROKEN FIX THIS LATER
//        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
//        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
//        sorter.setSortKeys(sortKeys);

        JScrollPane scrollPane= new  JScrollPane(cartTable);
        scrollPane.setBounds(24, 66, 400, 306);

        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.computeIfAbsent("Table.alternateRowColor", k -> new Color(240, 240, 240));

        JLabel totalCostLabel = new JLabel("Total Cost: $" + totalCost);
        totalCostLabel.setBounds(24, 372+4, 400, 24);
        storesPanel.add(totalCostLabel);

        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.setBounds(24, 404, 400/3 - 4, 24);
        placeOrderButton.addActionListener(e -> {

        });

        JButton removeFromCartButton = new JButton("Remove Item");
        removeFromCartButton.setBounds(24 + 400/3+4, 404, 400/3 - 4, 24);
        removeFromCartButton.addActionListener(e -> {

        });

        JButton productHistoryButton = new JButton("History");
        productHistoryButton.setBounds(24 + 400/3 * 2 + 8, 404, 400/3 - 8, 24);
        productHistoryButton.addActionListener(e -> productHistory());

        storesPanel.add(scrollPane);

        storesPanel.add(placeOrderButton);
        storesPanel.add(removeFromCartButton);
        storesPanel.add(productHistoryButton);

        panel.add(storesPanel, BorderLayout.CENTER);
        JSeparator divider = new JSeparator(JSeparator.VERTICAL);
        divider.setBackground(Color.decode("#dbdbdb"));
        divider.setForeground(Color.decode("#dbdbdb"));
        panel.add(divider, BorderLayout.LINE_START);


        return panel;
    }

    public void productHistory() {

    }

    public JPanel Stores() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel storesPanel = new JPanel();
        storesPanel.setLayout(null);

        JLabel storesLabel = new JLabel("Market");
        storesLabel.setFont(new Font("Serif", Font.BOLD, 18));
        storesLabel.setBounds(24, 16, 400, 24);

        JLabel helpfulLabel = new JLabel("Select a store to view");
        helpfulLabel.setFont(new Font("serif", Font.PLAIN, 14));
        helpfulLabel.setBounds(24, 36, 400, 24);

        // Create a DefaultTableModel
        DefaultTableModel model = new DefaultTableModel();

        // Add some data to the model
        model.addColumn("Product");
        model.addColumn("Store");
        model.addColumn("Price");
        model.addColumn("Product Id");
        model.addColumn("Store Product Id");

        Client.sendToServer(new ArrayList<>(List.of("[getStores]")));
        String allStoresString = Client.readFromServer(1).get(0);
        Client.sendToServer(new ArrayList<>(List.of("[getProducts]")));
        String allProductsString = Objects.requireNonNull(Client.readFromServer(1)).get(0);

        for (Object product : new JSONArray(allProductsString)) {
            for (Object store : new JSONArray(allStoresString)) {
                for (Object storeProduct : ((JSONObject) store).getJSONArray("products")) {
                    if (((JSONObject) product).getString("id").equals(((JSONObject) storeProduct).getString("id"))) {
                        JSONObject productObj = (JSONObject) product;
                        JSONObject storeProductObj = (JSONObject) storeProduct;
                        JSONObject storeObj = (JSONObject) store;
                        model.addRow(new Object[]{productObj.getString("name"), storeObj.getString("name"), storeProductObj.getDouble("price"), productObj.toString(), storeProductObj.toString()});
                    }
                }
            }
        }

        // Create a JTable using the model
        JTable marketTable = new JTable(model);

        for (int c = 0; c < marketTable.getColumnCount(); c++)
        {
            Class<?> col_class = marketTable.getColumnClass(c);
            marketTable.setDefaultEditor(col_class, null);        // remove editor
        }

        TableColumnModel tcm = marketTable.getColumnModel();
        tcm.removeColumn( tcm.getColumn(3) );
        tcm.removeColumn( tcm.getColumn(3) );

        marketTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    viewProductPage(new JSONObject(table.getModel().getValueAt(table.getSelectedRow(), 3).toString()), new JSONObject(table.getModel().getValueAt(table.getSelectedRow(), 4).toString()));
                }
            }
        });

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(marketTable.getModel());
        marketTable.setRowSorter(sorter);

        // SORTING BROKEN FIX THIS LATER
//        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
//        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
//        sorter.setSortKeys(sortKeys);

        JScrollPane scrollPane= new  JScrollPane(marketTable);
        scrollPane.setBounds(24, 66, 400, 330);

        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.computeIfAbsent("Table.alternateRowColor", k -> new Color(240, 240, 240));

        JButton selectProductButton = new JButton("Select Product");
        selectProductButton.setBounds(24, 404, 400/2 - 4, 24);
        selectProductButton.addActionListener(e -> {
            System.out.println("Button Pressed!");
            if (!marketTable.getSelectionModel().isSelectionEmpty())
            {
                viewProductPage(new JSONObject(marketTable.getModel().getValueAt(marketTable.getSelectedRow(), 3).toString()), new JSONObject(marketTable.getModel().getValueAt(marketTable.getSelectedRow(), 4).toString()));
            }
        });

        JButton filterButton = new JButton("Filter");
        filterButton.setBounds(24 + 400/2 + 4, 404, 400/2 - 8, 24);
        filterButton.addActionListener(e -> filter());

        storesPanel.add(storesLabel);
        storesPanel.add(helpfulLabel);
        storesPanel.add(scrollPane);

        storesPanel.add(selectProductButton);
        storesPanel.add(filterButton);

        panel.add(storesPanel, BorderLayout.CENTER);
        JSeparator divider = new JSeparator(JSeparator.VERTICAL);
        divider.setBackground(Color.decode("#dbdbdb"));
        divider.setForeground(Color.decode("#dbdbdb"));
        panel.add(divider, BorderLayout.LINE_START);

        return panel;
    }

    public JPanel settings(JPanel sidePanel) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(null);

        JLabel accountDetailsLabel = new JLabel("Account Details");
        accountDetailsLabel.setFont(new Font("Serif", Font.BOLD, 18));
        accountDetailsLabel.setBounds(24, 16, 200, 24);

        JLabel supportLabel = new JLabel("View your account details");
        supportLabel.setFont(new Font("serif", Font.PLAIN, 14));
        supportLabel.setBounds(24, 36, 400, 24);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(24, 60, 200, 24);

        JTextField usernameField = new JTextField(12);
        usernameField.setBounds(24, 84, 268, 24);
        usernameField.setText(buyer.getString("username"));

        JButton usernameButton = new JButton("Change Username");
        usernameButton.setOpaque(true);
        usernameButton.setBorderPainted(false);
        usernameButton.setBackground(Color.black);
        usernameButton.setForeground(Color.white);
        usernameButton.setBounds(300, 84, 174, 24);
        usernameButton.addActionListener(e -> {
            ArrayList<String> data = new ArrayList<>();
            data.add("[updateUserDetails]");
            data.add(buyer.getString("id"));
            data.add("username");
            data.add(usernameField.getText());
            Client.sendToServer(data);

            for (Component jc : sidePanel.getComponents()) {
                if (jc instanceof JTextArea) {
                    ((JTextArea) jc).setText("Hey, " + usernameField.getText());
                }
            }

            JOptionPane.showMessageDialog (null, "Username changed successfully!", "Updated Account Details", JOptionPane.INFORMATION_MESSAGE);
        });

        settingsPanel.add(usernameLabel);
        settingsPanel.add(usernameField);
        settingsPanel.add(usernameButton);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(24, 108, 200, 24);

        JTextField emailField = new JTextField(12);
        emailField.setBounds(24, 132, 268, 24);
        emailField.setText(buyer.getString("email"));

        JButton emailButton = new JButton("Change Email");
        emailButton.setOpaque(true);
        emailButton.setBackground(Color.black);
        emailButton.setForeground(Color.white);
        emailButton.setBorderPainted(false);
        emailButton.addActionListener(e -> {
            ArrayList<String> data = new ArrayList<>();
            data.add("[updateUserDetails]");
            data.add(buyer.getString("id"));
            data.add("email");
            data.add(emailField.getText());
            Client.sendToServer(data);

            JOptionPane.showMessageDialog (null, "Email changed successfully!", "Updated Account Details", JOptionPane.INFORMATION_MESSAGE);
        });

        emailButton.setBounds(300, 132, 174, 24);

        settingsPanel.add(emailLabel);
        settingsPanel.add(emailField);
        settingsPanel.add(emailButton);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(24, 156, 200, 24);

        JPasswordField passwordField = new JPasswordField(12);
        passwordField.setBounds(24, 180, 268, 24);
        passwordField.setText(buyer.getString("password"));

        JButton passwordButton = new JButton("Change Password");
        passwordButton.setBorderPainted(false);
        passwordButton.setOpaque(true);
        passwordButton.setBackground(Color.black);
        passwordButton.setForeground(Color.white);
        passwordButton.setBounds(300, 180, 174, 24);
        passwordButton.addActionListener(e -> {
            ArrayList<String> data = new ArrayList<>();
            data.add("[updateUserDetails]");
            data.add(buyer.getString("id"));
            data.add("password");
            data.add(String.valueOf(passwordField.getPassword()));
            Client.sendToServer(data);

            JOptionPane.showMessageDialog (null, "Password changed successfully!", "Updated Account Details", JOptionPane.INFORMATION_MESSAGE);
        });

        settingsPanel.add(passwordLabel);
        settingsPanel.add(passwordField);
        settingsPanel.add(passwordButton);

        JSeparator accountDetailsDivider = new JSeparator(JSeparator.HORIZONTAL);
        accountDetailsDivider.setBounds(24, 228, 450,24);
        accountDetailsDivider.setBackground(Color.decode("#dbdbdb"));
        accountDetailsDivider.setForeground(Color.decode("#dbdbdb"));

        JButton deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.setBackground(Color.decode("#f4f4f4"));
        deleteAccountButton.setOpaque(false);
        deleteAccountButton.setForeground(Color.decode("#d11111"));
        deleteAccountButton.setBounds(24, 253, 150, 24);
        deleteAccountButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account?", "Account Removal", JOptionPane.OK_CANCEL_OPTION);
            if (option == 0) {
                ArrayList<String> data = new ArrayList<>();
                data.add("[deleteAccount]");
                data.add(buyer.getString("id"));
                Client.sendToServer(data);
                dispose();
                new OnboardingPage(true);
            }
        });

        settingsPanel.add(accountDetailsDivider);
        settingsPanel.add(deleteAccountButton);

        settingsPanel.add(accountDetailsLabel);
        settingsPanel.add(supportLabel);

        JSeparator divider = new JSeparator(JSeparator.VERTICAL);
        divider.setBackground(Color.decode("#dbdbdb"));
        divider.setForeground(Color.decode("#dbdbdb"));

        panel.add(settingsPanel, BorderLayout.CENTER);
        panel.add(divider, BorderLayout.LINE_START);

        return panel;
    }

    public JPanel statistics() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel statisticsPanel = new JPanel();
        statisticsPanel.setLayout(null);

        JLabel statisticsLabel = new JLabel("Buyer Statistics");
        statisticsLabel.setFont(new Font("Serif", Font.BOLD, 18));
        statisticsLabel.setBounds(24, 16, 200, 24);

        JLabel supportLabel = new JLabel("View your sales statistics");
        supportLabel.setFont(new Font("serif", Font.PLAIN, 14));
        supportLabel.setBounds(24, 36, 400, 24);

        statisticsPanel.add(statisticsLabel);
        statisticsPanel.add(supportLabel);

        JSeparator divider = new JSeparator(JSeparator.VERTICAL);
        divider.setBackground(Color.decode("#dbdbdb"));
        divider.setForeground(Color.decode("#dbdbdb"));

        panel.add(statisticsPanel, BorderLayout.CENTER);
        panel.add(divider, BorderLayout.LINE_START);

        return panel;
    }

    public void filter() {

    }

    public void viewProductPage(JSONObject product, JSONObject storeProduct) {

        JFrame storePage = new JFrame();

        JPanel panel = new JPanel();
        GridBagLayout gridLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,60,0,24);
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.setLayout(gridLayout);

        // Set title of window
        storePage.setTitle(product.getString("name"));

        // Set behavior to "destroy" window when closed
        storePage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set dimensions of application
        storePage.setSize(640, 480);

        // Does not allow user to resize window
        storePage.setResizable(false);

        // Set window to open in the center of the screen
        storePage.setLocationRelativeTo(null);

        storePage.setLayout(new BorderLayout());

        JLabel productLabel = new JLabel(product.getString("name"));
        productLabel.setFont(new Font("serif", Font.BOLD, 24));
        c.gridy = 0;
        c.gridwidth = 4;
        c.anchor = GridBagConstraints.NORTH;
        gridLayout.setConstraints(productLabel, c);
        panel.add(productLabel);

        JTextArea descriptionLabel = new JTextArea(product.getString("description"));
        descriptionLabel.setFont(new Font("sans-serif", Font.PLAIN, 14));
        descriptionLabel.setLineWrap(true);
        descriptionLabel.setWrapStyleWord(true);
        descriptionLabel.setOpaque(false);
        descriptionLabel.setFocusable(false);

        c.gridy = 1;
        gridLayout.setConstraints(descriptionLabel, c);
        panel.add(descriptionLabel);

        JLabel quantityLabel = new JLabel("Quantity Remaining: " + storeProduct.getInt("qty"));
        quantityLabel.setFont(new Font("sans-serif", Font.PLAIN, 16));
        c.insets = new Insets(4,60,0,24);
        c.gridy = 3;
        gridLayout.setConstraints(quantityLabel, c);
        panel.add(quantityLabel);

        JLabel priceLabel = new JLabel("Price: $" + storeProduct.getDouble("price"));
        priceLabel.setFont(new Font("sans-serif", Font.BOLD, 16));
        c.insets = new Insets(0,60,0,24);

        c.gridy = 4;
        c.gridwidth = 4;
        gridLayout.setConstraints(priceLabel, c);
        panel.add(priceLabel);

        JSeparator divider = new JSeparator(JSeparator.VERTICAL);
        divider.setBackground(Color.decode("#dbdbdb"));
        divider.setForeground(Color.decode("#dbdbdb"));

        storePage.add(panel, BorderLayout.WEST);
        storePage.add(divider, BorderLayout.CENTER);
        storePage.add(purchasePanel(storePage, product, storeProduct), BorderLayout.EAST);

        storePage.setVisible(true);

    }

    public JPanel purchasePanel(JFrame productFrame, JSONObject product, JSONObject storeProduct) {

        JPanel panel = new JPanel();
        GridBagLayout gridLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panel.setLayout(gridLayout);
        c.insets = new Insets(0,0,0,80);

        JSpinner spinner = new JSpinner();
        c.gridy = 2;
        c.gridwidth = 4;
        spinner.setMinimumSize(new Dimension(150, 24));
        spinner.setPreferredSize(new Dimension(150, 24));
        gridLayout.setConstraints(spinner, c);
        panel.add(spinner);

        JLabel qtyLabel = new JLabel("Quantity: ");
        c.gridy = 1;
        c.gridwidth = 4;
        qtyLabel.setMinimumSize(new Dimension(150, 24));
        qtyLabel.setPreferredSize(new Dimension(150, 24));
        gridLayout.setConstraints(qtyLabel, c);
        panel.add(qtyLabel);

        JButton addToCartButton = new JButton("Add to Cart");
        c.gridy = 3;
        c.gridwidth = 4;
        c.insets = new Insets(8,0,0,80);
        addToCartButton.setMinimumSize(new Dimension(150, 24));
        addToCartButton.setPreferredSize(new Dimension(150, 24));
        gridLayout.setConstraints(addToCartButton, c);
        addToCartButton.addActionListener(e -> {
            ArrayList<String> data = new ArrayList<>();
            data.add("[addToCart]");
            data.add(buyer.getString("id"));
            data.add(product.getString("id"));
            data.add(spinner.getValue().toString());
            data.add(String.valueOf(storeProduct.getDouble("price")));

            Client.sendToServer(data);

            JOptionPane.showMessageDialog (null, "Successfully added to cart!", "Cart Panel", JOptionPane.INFORMATION_MESSAGE);

        });
        panel.add(addToCartButton);

        JButton closeWindow = new JButton("Close");
        c.gridy = 34;
        c.gridwidth = 4;
        c.insets = new Insets(8,0,0,80);
        closeWindow.setMinimumSize(new Dimension(150, 24));
        closeWindow.setPreferredSize(new Dimension(150, 24));
        gridLayout.setConstraints(closeWindow, c);
        closeWindow.addActionListener(e -> {
            productFrame.dispose();
        });
        panel.add(closeWindow);

        return panel;
    }
}


