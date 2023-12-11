import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

/**
 * CustomerPage
 * <p>
 * handles the UI for customer operations.
 *
 * @author Chris Trumpet, Matthew Lee, Mohit Ambe, Shrinand Perumal, Vraj Patel
 * @version December 11, 2023
 */
public class CustomerPage extends JFrame {
    CardLayout cardLayout = new CardLayout();
    Container container = new Container();
    JFrame reference;
    JSONObject buyer;
    JLabel balanceMessage;

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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public JPanel SidePanel() {

        JPanel panel = new JPanel();
        GridBagLayout gridLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 80, 0, 24);
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.setLayout(gridLayout);


        // WELCOME MESSAGE
        JLabel welcomeMessage = new JLabel("Purdue Marketplace");
        welcomeMessage.setFont(new Font("serif", Font.BOLD, 18));
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 2;
        c.insets = new Insets(0, 80, 0, 24);
        gridLayout.setConstraints(welcomeMessage, c);
        panel.add(welcomeMessage);

        // GREETINGS MESSAGE
        JTextArea nameMessage = new JTextArea("Hey, " + buyer.getString("username"));
        nameMessage.setFont(new Font("sans-serif", Font.PLAIN, 14));
        nameMessage.setLineWrap(true);
        nameMessage.setWrapStyleWord(true);
        nameMessage.setFocusable(false);
        nameMessage.setEditable(false);
        nameMessage.setOpaque(false);
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 2;
        gridLayout.setConstraints(nameMessage, c);
        panel.add(nameMessage);

        // DIVIDER
        JSeparator accountDetailsDivider = new JSeparator(JSeparator.HORIZONTAL);
        accountDetailsDivider.setMinimumSize(new Dimension(150, 8));
        accountDetailsDivider.setPreferredSize(new Dimension(150, 8));
        accountDetailsDivider.setBackground(Color.decode("#dbdbdb"));
        accountDetailsDivider.setForeground(Color.decode("#dbdbdb"));
        c.gridy = 2;
        c.gridx = 0;
        c.gridwidth = 2;
        c.insets = new Insets(4, 80, 4, 24);
        gridLayout.setConstraints(accountDetailsDivider, c);
        panel.add(accountDetailsDivider);

        // BUYER'S BALANCE
        balanceMessage = new JLabel("Balance: $" + String.format("%.2f", buyer.getDouble("funds")));
        balanceMessage.setFont(new Font("sans-serif", Font.PLAIN, 14));
        c.gridy = 3;
        c.gridx = 0;
        c.gridwidth = 2;
        c.insets = new Insets(4, 80, 4, 24);
        gridLayout.setConstraints(balanceMessage, c);
        balanceMessage.setMaximumSize(new Dimension(300, 24));
        panel.add(balanceMessage);

        // NAVIGATION

        // MARKETPLACE
        JButton storesButton = new JButton("Marketplace");
        storesButton.addActionListener(e -> cardLayout.show(container, "stores"));
        c.gridy = 4;
        c.gridx = 0;
        c.gridwidth = 2;
        gridLayout.setConstraints(storesButton, c);
        panel.add(storesButton);

        // SETTINGS
        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e -> cardLayout.show(container, "settings"));
        c.gridy = 5;
        c.gridx = 0;
        c.gridwidth = 2;
        gridLayout.setConstraints(settingsButton, c);
        panel.add(settingsButton);

        // STATISTICS
        JButton statisticsButton = new JButton("Statistics");
        statisticsButton.addActionListener(e -> cardLayout.show(container, "statistics"));
        c.gridy = 6;
        c.gridx = 0;
        c.gridwidth = 2;
        gridLayout.setConstraints(statisticsButton, c);
        panel.add(statisticsButton);

        // FUNDING BUTTON
        JButton fundsButton = new JButton("Add Funding");
        fundsButton.addActionListener(e -> {
            String funding;
            do {
                funding = JOptionPane.showInputDialog(null, "How much would you like to add?", "Funding Panel", JOptionPane.QUESTION_MESSAGE);

                if (funding == null) {
                    break;
                }

                if (!funding.matches("([0-9]{1,12})") || !(Double.parseDouble(funding) > 0)) {
                    Client.showErrorMessage("Invalid input! Numbers only.");
                }
            } while (!funding.matches("([0-9]{1,12})") || !(Double.parseDouble(funding) > 0));

            if (funding != null) {
                if (!funding.isEmpty()) {

                    Client.sendToServer("addFunds", buyer.getString("id"), funding);

                    balanceMessage.setText("Balance: $" + (buyer.getDouble("funds") + Double.parseDouble(funding)));
                    JOptionPane.showMessageDialog(null, "Successfully added $" + funding + " to your account!", "Funding Panel", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        });
        c.gridy = 7;
        c.gridx = 0;
        c.gridwidth = 2;
        gridLayout.setConstraints(fundsButton, c);
        panel.add(fundsButton);

        // VIEW CART BUTTON
        JButton cartButton = new JButton("View Cart");
        cartButton.addActionListener(e -> {

            Client.sendToServer("getUser", buyer.getString("id"));
            String userString = Objects.requireNonNull(Client.readFromServer(1)).get(0);

            this.buyer = new JSONObject(userString);

            container.remove(cart());
            container.add("cart", cart());
            cardLayout.show(container, "cart");
        });
        c.gridy = 8;
        c.gridx = 0;
        c.gridwidth = 2;
        gridLayout.setConstraints(cartButton, c);
        panel.add(cartButton);

        // LOGOUT
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout");
            if (input == 0) {
                reference.dispose();
                new OnboardingPage(true);
            }
        });
        c.gridy = 9;
        c.gridx = 0;
        c.gridwidth = 1;
        c.insets = new Insets(4, 80, 4, 4);
        gridLayout.setConstraints(logoutButton, c);
        panel.add(logoutButton);

        // EXIT PROGRAM
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit");
            if (input == 0) {
                reference.dispose();
                Client.closeConnection();
            }
        });
        c.gridy = 9;
        c.gridx = 1;
        c.gridwidth = 1;
        c.insets = new Insets(4, 4, 4, 24);
        gridLayout.setConstraints(exitButton, c);
        panel.add(exitButton);

        return panel;
    }

    public JPanel cart() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(null);

        // TITLE

        JLabel accountDetailsLabel = new JLabel("Your Cart");
        accountDetailsLabel.setFont(new Font("Serif", Font.BOLD, 18));
        accountDetailsLabel.setBounds(24, 16, 200, 24);

        JLabel supportLabel = new JLabel("View your cart contents and make purchase");
        supportLabel.setFont(new Font("serif", Font.PLAIN, 14));
        supportLabel.setBounds(24, 36, 400, 24);

        cartPanel.add(accountDetailsLabel);
        cartPanel.add(supportLabel);

        // CART TABLE

        // Create a DefaultTableModel
        DefaultTableModel cartModel = new DefaultTableModel();

        cartModel.addColumn("Product");
        cartModel.addColumn("Quantity");
        cartModel.addColumn("Price");

        // RETRIEVE PRODUCTS FILE FROM SERVER-SIDE
        Client.sendToServer("getProducts");
        JSONArray products = new JSONArray(Objects.requireNonNull(Client.readFromServer(1)).get(0));

        // BUYER CART
        JSONArray cart = buyer.getJSONArray("cart");
        double totalCost = 0.0;

        // ADD CART CONTENTS TO TABLE
        for (Object contents : cart) {
            for (Object product : products) {

                String productId = ((JSONObject) product).getString("product_id");
                String cartProductId = ((JSONObject) contents).getString("product_id");

                // CHECKS IF PRODUCT IS IN BUYER'S CART
                if (productId.equals(cartProductId)) {
                    int cartProductQuantity = ((JSONObject) contents).getInt("quantity");
                    double cartProductPrice = ((JSONObject) contents).getDouble("price");

                    String productName = ((JSONObject) product).getString("name");

                    totalCost += cartProductQuantity * cartProductPrice;
                    cartModel.addRow(new Object[]{productName, cartProductQuantity, cartProductPrice});
                }
            }
        }

        // Create a JTable using the model
        JTable cartTable = new JTable(cartModel);

        // MAKE TABLE UN-EDITABLE
        for (int c = 0; c < cartTable.getColumnCount(); c++) {
            Class<?> col_class = cartTable.getColumnClass(c);
            cartTable.setDefaultEditor(col_class, null);        // remove editor
        }

        // SORTING
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(cartTable.getModel());
        cartTable.setRowSorter(sorter);

        // MAKES TABLE SCROLLABLE
        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBounds(24, 66, 400, 306);

        // ALTERNATING BACKGROUND COLOR
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.computeIfAbsent("Table.alternateRowColor", k -> new Color(240, 240, 240));

        cartPanel.add(scrollPane);

        // END OF TABLE CREATION

        // TOTAL COST LABEL
        JLabel totalCostLabel = new JLabel("Total Cost: $" + round(totalCost, 2));
        totalCostLabel.setBounds(24, 372 + 4, 400, 24);
        cartPanel.add(totalCostLabel);

        // PLACE ORDER BUTTON
        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.setBounds(24, 404, 400 / 3 - 4, 24);
        placeOrderButton.addActionListener(e -> placeOrder(cartTable, totalCostLabel));

        cartPanel.add(placeOrderButton);

        // REMOVE ITEM FROM CART
        JButton removeFromCartButton = new JButton("Remove Item");
        removeFromCartButton.setBounds(24 + 400 / 3 + 4, 404, 400 / 3 - 4, 24);

        removeFromCartButton.addActionListener(e -> {
            if (cartTable.getSelectedRow() > 0) removeFromCart(cartTable);
        });

        cartPanel.add(removeFromCartButton);

        // PRODUCT HISTORY
        JButton productHistoryButton = new JButton("History");
        productHistoryButton.setBounds(24 + 400 / 3 * 2 + 8, 404, 400 / 3 - 8, 24);
        productHistoryButton.addActionListener(e -> productHistory());
        cartPanel.add(productHistoryButton);


        panel.add(cartPanel, BorderLayout.CENTER);
        JSeparator divider = new JSeparator(JSeparator.VERTICAL);
        divider.setBackground(Color.decode("#dbdbdb"));
        divider.setForeground(Color.decode("#dbdbdb"));
        panel.add(divider, BorderLayout.LINE_START);

        return panel;
    }

    private void placeOrder(JTable cartTable, JLabel totalCostLabel) {
        if (cartTable.getRowCount() > 0) {

            Client.sendToServer("placeOrder", buyer.getString("id"));

            if (Objects.requireNonNull(Client.readFromServer(1)).get(0).equals("true")) {

                // REFRESH BUYER DATA
                Client.sendToServer("getUser", buyer.getString("id"));
                String userString = Objects.requireNonNull(Client.readFromServer(1)).get(0);
                this.buyer = new JSONObject(userString);

                // UPDATE BALANCE
                balanceMessage.setText("Balance: $" + round(buyer.getDouble("funds"), 2));

                // RESET TOTAL
                totalCostLabel.setText("Total Cost: $0.00");

                // CLEAR CART TABLE
                for (int i = 0; i < cartTable.getRowCount(); i++) {
                    ((DefaultTableModel) cartTable.getModel()).removeRow(i);
                }
            } else {
                Client.showErrorMessage("Could not place order! Insufficient funds or unable to meet stock demand (quantity is greater than stock).");
            }
        } else {
            Client.showErrorMessage("Please add items to cart to purchase!");
        }
    }

    private void removeFromCart(JTable cartTable) {
        if (cartTable.getRowCount() > 0) {

            // REMOVE CART ITEM FROM DATABASE
            Client.sendToServer("removeFromCart", buyer.getString("id"), cartTable.getValueAt(cartTable.getSelectedRow(), 0).toString());

            // REMOVE ITEM FROM CART TABLE
            ((DefaultTableModel) cartTable.getModel()).removeRow(cartTable.getSelectedRow());
        } else {
            Client.showErrorMessage("There is nothing in the cart to remove!");
        }
    }

    private void productHistory() {

        JFrame frame = new JFrame();
        GridBagLayout gridLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        // Set title of window
        frame.setTitle("Product History");

        // Set behavior to "destroy" window when closed
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set dimensions of application
        frame.setSize(448, 480);

        // Does not allow user to resize window
        frame.setResizable(false);

        // Set window to open in the center of the screen
        frame.setLocationRelativeTo(null);

        frame.setLayout(gridLayout);

        JLabel productLabel = new JLabel("Your Product History");
        productLabel.setFont(new Font("serif", Font.BOLD, 18));
        c.gridy = 0;
        c.gridwidth = 4;
        c.insets = new Insets(0, 24, 8, 24);
        c.anchor = GridBagConstraints.NORTH;
        gridLayout.setConstraints(productLabel, c);
        frame.add(productLabel);


        // Create a DefaultTableModel
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Product");
        model.addColumn("Quantity");
        model.addColumn("Price");

        Client.sendToServer("getProducts");
        String allProductsString = Objects.requireNonNull(Client.readFromServer(1)).get(0);

        JSONArray productHistory = buyer.getJSONArray("product_history");

        for (Object contents : productHistory) {
            for (Object product : new JSONArray(allProductsString)) {
                if (((JSONObject) product).getString("product_id").equals(((JSONObject) contents).getString("product_id"))) {
                    model.addRow(new Object[]{((JSONObject) product).getString("name"), ((JSONObject) contents).getInt("quantity"), ((JSONObject) contents).getDouble("price")});
                }
            }
        }
        // Create a JTable using the model
        JTable historyTable = new JTable(model);

        for (int k = 0; k < historyTable.getColumnCount(); k++) {
            Class<?> col_class = historyTable.getColumnClass(k);
            historyTable.setDefaultEditor(col_class, null);        // remove editor
        }

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(historyTable.getModel());
        historyTable.setRowSorter(sorter);

        // SORTING BROKEN FIX THIS LATER
//        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
//        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
//        sorter.setSortKeys(sortKeys);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setMinimumSize(new Dimension(400, 306));
        scrollPane.setPreferredSize(new Dimension(400, 306));
        scrollPane.setMaximumSize(new Dimension(400, 306));

        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.computeIfAbsent("Table.alternateRowColor", k -> new Color(240, 240, 240));

        c.gridy = 1;
        c.gridwidth = 4;
        c.insets = new Insets(0, 24, 4, 24);
        gridLayout.setConstraints(scrollPane, c);

        frame.add(scrollPane);

        JButton closeButton = new JButton("Close");
        c.gridy = 2;
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        gridLayout.setConstraints(closeButton, c);
        closeButton.addActionListener(e -> frame.dispose());
        frame.add(closeButton);

        JButton exportHistoryButton = new JButton("Export History");
        c.gridy = 2;
        c.gridx = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        gridLayout.setConstraints(exportHistoryButton, c);
        exportHistoryButton.addActionListener(e -> {

            if (historyTable.getRowCount() > 0) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(".csv", "csv");
                chooser.setFileFilter(filter);
                chooser.showOpenDialog(null);

                if (chooser.getSelectedFile() != null) {

                    Client.sendToServer("exportHistory", buyer.getString("id"), chooser.getSelectedFile().getAbsolutePath());

                    JOptionPane.showMessageDialog(null, "Exported history successfully!", "Product History", JOptionPane.INFORMATION_MESSAGE);

                }
            } else {
                Client.showErrorMessage("You have not made any purchases!");
            }

        });
        frame.add(exportHistoryButton);


        frame.setVisible(true);
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

        Client.sendToServer("getStores");
        String allStoresString = Objects.requireNonNull(Client.readFromServer(1)).get(0);

        Client.sendToServer("getProducts");
        String allProductsString = Objects.requireNonNull(Client.readFromServer(1)).get(0);

        for (Object product : new JSONArray(allProductsString)) {
            for (Object store : new JSONArray(allStoresString)) {
                for (Object storeProduct : ((JSONObject) store).getJSONArray("products")) {
                    if (((JSONObject) product).getString("product_id").equals(((JSONObject) storeProduct).getString("id"))) {
                        JSONObject productObj = (JSONObject) product;
                        JSONObject storeProductObj = (JSONObject) storeProduct;
                        JSONObject storeObj = (JSONObject) store;
                        model.addRow(new Object[]{productObj.getString("name"), storeObj.getString("name"), storeProductObj.getDouble("price")});
                    }
                }
            }
        }

        // Create a JTable using the model
        JTable marketTable = new JTable(model);

        for (int c = 0; c < marketTable.getColumnCount(); c++) {
            Class<?> col_class = marketTable.getColumnClass(c);
            marketTable.setDefaultEditor(col_class, null);        // remove editor
        }

        marketTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    viewProductPage(marketTable, marketTable.getSelectedRow());
                }
            }
        });

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(marketTable.getModel());
        marketTable.setRowSorter(sorter);

//        // SORTING BROKEN FIX THIS LATER
//        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
//        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
//        sorter.setSortKeys(sortKeys);

        JScrollPane scrollPane = new JScrollPane(marketTable);
        scrollPane.setBounds(24, 66, 400, 330);

        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.computeIfAbsent("Table.alternateRowColor", k -> new Color(240, 240, 240));

        JButton selectProductButton = new JButton("Select Product");
        selectProductButton.setBounds(24, 404, 400 / 2 - 4, 24);
        selectProductButton.addActionListener(e -> {
            if (!marketTable.getSelectionModel().isSelectionEmpty()) {
                viewProductPage(marketTable, marketTable.getSelectedRow());
            } else {
                JOptionPane.showMessageDialog(null, "Please highlight a product to select!", "No Product Highlighted!", JOptionPane.INFORMATION_MESSAGE);

            }
        });

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(24 + 400 / 2 + 4, 404, 400 / 2 - 8, 24);
        searchButton.addActionListener(e -> search(marketTable));

        storesPanel.add(storesLabel);
        storesPanel.add(helpfulLabel);
        storesPanel.add(scrollPane);

        storesPanel.add(selectProductButton);
        storesPanel.add(searchButton);

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
        usernameLabel.setBounds(24, 64, 200, 24);

        JTextField usernameField = new JTextField(12);
        usernameField.setBounds(24, 88, 268, 24);
        usernameField.setText(buyer.getString("username"));

        JButton usernameButton = new JButton("Change Username");
        usernameButton.setBounds(300, 88, 174, 24);
        usernameButton.addActionListener(e -> {

            Client.sendToServer("updateUserDetails", buyer.getString("id"), "username", usernameField.getText());

            // UPDATE GREETING
            for (Component jc : sidePanel.getComponents()) {
                if (jc instanceof JTextArea) {
                    ((JTextArea) jc).setText("Hey, " + usernameField.getText());
                }
            }

            JOptionPane.showMessageDialog(null, "Username changed successfully!", "Updated Account Details", JOptionPane.INFORMATION_MESSAGE);
        });

        settingsPanel.add(usernameLabel);
        settingsPanel.add(usernameField);
        settingsPanel.add(usernameButton);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(24, 116, 200, 24);

        JTextField emailField = new JTextField(12);
        emailField.setBounds(24, 140, 268, 24);
        emailField.setText(buyer.getString("email"));

        JButton emailButton = new JButton("Change Email");
        emailButton.addActionListener(e -> {

            Client.sendToServer("updateUserDetails", buyer.getString("id"), "email", emailField.getText());

            JOptionPane.showMessageDialog(null, "Email changed successfully!", "Updated Account Details", JOptionPane.INFORMATION_MESSAGE);
        });

        emailButton.setBounds(300, 140, 174, 24);

        settingsPanel.add(emailLabel);
        settingsPanel.add(emailField);
        settingsPanel.add(emailButton);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(24, 168, 200, 24);

        JPasswordField passwordField = new JPasswordField(12);
        passwordField.setBounds(24, 192, 268, 24);
        passwordField.setText(buyer.getString("password"));

        JButton passwordButton = new JButton("Change Password");
        passwordButton.setBounds(300, 192, 174, 24);
        passwordButton.addActionListener(e -> {

            Client.sendToServer("updateUserDetails", buyer.getString("id"), "password", String.valueOf(passwordField.getPassword()));

            JOptionPane.showMessageDialog(null, "Password changed successfully!", "Updated Account Details", JOptionPane.INFORMATION_MESSAGE);
        });

        settingsPanel.add(passwordLabel);
        settingsPanel.add(passwordField);
        settingsPanel.add(passwordButton);

        JSeparator accountDetailsDivider = new JSeparator(JSeparator.HORIZONTAL);
        accountDetailsDivider.setBounds(24, 240, 450, 24);
        accountDetailsDivider.setBackground(Color.decode("#dbdbdb"));
        accountDetailsDivider.setForeground(Color.decode("#dbdbdb"));

        JButton deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.setForeground(Color.decode("#d11111"));
        deleteAccountButton.setBounds(24, 264, 150, 24);
        deleteAccountButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account?", "Account Removal", JOptionPane.OK_CANCEL_OPTION);
            if (option == 0) {
                Client.sendToServer("deleteAccount", buyer.getString("id"));
                dispose();

                // NAVIGATE TO SIGNUP PAGE
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

        JLabel grossSalesLabel = new JLabel("Best Selling Items");
        grossSalesLabel.setFont(new Font("serif", Font.BOLD, 14));
        grossSalesLabel.setBounds(24, 60, 150, 24);

        JLabel customerSalesLabel = new JLabel("Your Top Stores");
        customerSalesLabel.setFont(new Font("serif", Font.BOLD, 14));
        customerSalesLabel.setBounds(236, 60, 150, 24);

        statisticsPanel.add(grossSalesLabel);
        statisticsPanel.add(customerSalesLabel);

        // Create a DefaultTableModel
        DefaultTableModel storeModel = new DefaultTableModel();

        // Add some data to the model
        storeModel.addColumn("Store");
        storeModel.addColumn("Product");

        Client.sendToServer("getStores");
        JSONArray stores = new JSONArray(Objects.requireNonNull(Client.readFromServer(1)).get(0));

        Client.sendToServer("getProducts");
        JSONArray products = new JSONArray(Objects.requireNonNull(Client.readFromServer(1)).get(0));

        ArrayList<JSONObject> productList = new ArrayList<>();

        for (Object store : stores) {
            JSONArray productsArray = ((JSONObject) store).getJSONArray("products");


            for (Object product : productsArray) {

                JSONObject productToAdd = (JSONObject) product;

                for (Object allProduct : products) {
                    if (((JSONObject) allProduct).getString("product_id").equals(((JSONObject) product).getString("id")))
                        productToAdd.put("name", ((JSONObject) allProduct).getString("name"));
                }


                productList.add(productToAdd);
            }
        }

        productList.sort(Comparator.comparingDouble(o -> o.getDouble("sales")));
        Collections.reverse(productList);

        for (JSONObject product : productList) {
            storeModel.addRow(new Object[]{product.getString("name"), product.getDouble("sales")});
        }


        // Create a JTable using the model
        JTable storesTable = new JTable(storeModel);

        for (int c = 0; c < storesTable.getColumnCount(); c++) {
            Class<?> col_class = storesTable.getColumnClass(c);
            storesTable.setDefaultEditor(col_class, null);        // remove editor
        }

        JScrollPane storeTableStat = new JScrollPane(storesTable);
        storeTableStat.setBounds(24, 92, 200, 344);

        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.computeIfAbsent("Table.alternateRowColor", k -> new Color(240, 240, 240));

        statisticsPanel.add(storeTableStat);

        // Create a DefaultTableModel
        DefaultTableModel productModel = new DefaultTableModel();

        // Add some data to the model
        productModel.addColumn("Store");
        productModel.addColumn("Product");

        ArrayList<JSONObject> userProducts = new ArrayList<>();

        try {
            if (!buyer.getJSONArray("product_history").isEmpty()) {
                for (Object product : buyer.getJSONArray("product_history")) {
                    JSONObject productToAdd = (JSONObject) product;

                    for (Object allProduct : products) {

                        String historyId = productToAdd.getString("product_id");
                        String productId = ((JSONObject) allProduct).getString("product_id");

                        if (historyId.equals(productId)) {
                            Client.sendToServer("getStoreById", productToAdd.getString("store_id"));
                            JSONObject store = new JSONObject(Objects.requireNonNull(Client.readFromServer(1)).get(0));
                            productToAdd.put("store", store.getString("name"));
                            productToAdd.put("name", ((JSONObject) allProduct).getString("name"));
                            productToAdd.put("sales", (Double) (productToAdd.getInt("quantity") * productToAdd.getDouble("price")));
                            System.out.println(productToAdd);
                            userProducts.add(productToAdd);

                        }
                    }

                }
            } else {
                System.out.println("[CLIENT] Buyer has no product history.");
            }
        } catch (JSONException ignore) {
            System.out.println("User not a buyer");
        }

        try {
            userProducts.sort(Comparator.comparingDouble(o -> o.getDouble("sales")));
            Collections.reverse(userProducts);

            for (JSONObject product : userProducts) {
                String productName = product.getString("name");
                String productStore = product.getString("store");
                productModel.addRow(new Object[]{productStore, productName});
            }
        } catch (JSONException ignore) {
        }

        // Create a JTable using the model
        JTable productTable = new JTable(productModel);

        for (int c = 0; c < productTable.getColumnCount(); c++) {
            Class<?> col_class = productTable.getColumnClass(c);
            productTable.setDefaultEditor(col_class, null);        // remove editor
        }

        JScrollPane productTableStat = new JScrollPane(productTable);
        productTableStat.setBounds(236, 92, 200, 344);

        statisticsPanel.add(productTableStat);

        JSeparator divider = new JSeparator(JSeparator.VERTICAL);
        divider.setBackground(Color.decode("#dbdbdb"));
        divider.setForeground(Color.decode("#dbdbdb"));

        panel.add(statisticsPanel, BorderLayout.CENTER);
        panel.add(divider, BorderLayout.LINE_START);

        return panel;
    }

    public void search(JTable table) {
        String searchQuery = JOptionPane.showInputDialog("Search:", "Type here...");

        if (searchQuery != null) {

            Client.sendToServer("search", searchQuery);

            int numOfResults;
            try {
                numOfResults = Integer.parseInt(Objects.requireNonNull(Client.readFromServer(1)).get(0));
                ArrayList<String> results = Client.readFromServer(numOfResults);

                assert results != null;
                String[] array = new String[results.size()];
                for (int i = 0; i < array.length; i++) {
                    array[i] = results.get(i);
                }
                JComboBox<String> resultList = new JComboBox<>(array);
                resultList.setEditable(true);
                AutoCompletion.enable(resultList);

                int option = JOptionPane.showConfirmDialog(null, resultList, "Select result", JOptionPane.OK_CANCEL_OPTION);

                if (option == 0) {
                    System.out.println("Selection made.");
                    for (int i = 0; i < table.getRowCount(); i++) {

                        if (Objects.equals(resultList.getSelectedItem(), table.getValueAt(i, 0).toString()))
                            viewProductPage(table, table.getSelectedRow());

                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("No Results...");
                Client.showErrorMessage("There were no results...");
            }
        }

    }

    public void viewProductPage(JTable marketTable, int productRow) {

        JFrame storePage = new JFrame();

        JPanel panel = new JPanel();
        GridBagLayout gridLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 24, 0, 24);
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.setLayout(gridLayout);

        Client.sendToServer("getStore", marketTable.getValueAt(productRow, 1).toString());
        String storeId = new JSONObject(Objects.requireNonNull(Client.readFromServer(1)).get(0)).getString("id");

        Client.sendToServer("getProduct", marketTable.getValueAt(productRow, 0).toString());
        JSONObject product = new JSONObject(Objects.requireNonNull(Client.readFromServer(1)).get(0));
        String productId = product.getString("product_id");
        Client.sendToServer("getStoreProduct", storeId, productId);
        JSONObject storeProduct = new JSONObject(Objects.requireNonNull(Client.readFromServer(1)).get(0));

        Client.sendToServer("getStoreProduct", storeId, storeProduct.getString("id"));
        storeProduct = new JSONObject(Objects.requireNonNull(Client.readFromServer(1)).get(0));

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

        panel.setMinimumSize(new Dimension(320, 480));
        panel.setPreferredSize(new Dimension(320, 480));
        panel.setMaximumSize(new Dimension(320, 480));

        JTextArea productLabel = new JTextArea(product.getString("name"));
        productLabel.setMinimumSize(new Dimension(276, 32));
        productLabel.setPreferredSize(new Dimension(276, 32));
        productLabel.setMaximumSize(new Dimension(276, 32));
        productLabel.setFont(new Font("serif", Font.BOLD, 24));
        productLabel.setLineWrap(true);
        productLabel.setWrapStyleWord(true);
        productLabel.setOpaque(false);
        productLabel.setFocusable(false);
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        gridLayout.setConstraints(productLabel, c);
        panel.add(productLabel);

        c.insets = new Insets(4, 24, 0, 24);

        JTextArea descriptionLabel = new JTextArea(product.getString("description"));
        descriptionLabel.setFont(new Font("sans-serif", Font.PLAIN, 12));
        descriptionLabel.setLineWrap(true);
        descriptionLabel.setWrapStyleWord(true);
        descriptionLabel.setOpaque(false);
        descriptionLabel.setFocusable(false);

        c.gridy = 1;
        gridLayout.setConstraints(descriptionLabel, c);
        panel.add(descriptionLabel);


        JSeparator productPageDivider = new JSeparator(JSeparator.HORIZONTAL);
        productPageDivider.setMinimumSize(new Dimension(150, 8));
        productPageDivider.setPreferredSize(new Dimension(150, 8));
        productPageDivider.setBackground(Color.decode("#dbdbdb"));
        productPageDivider.setForeground(Color.decode("#dbdbdb"));
        c.insets = new Insets(8, 24, 0, 24);
        c.gridy = 2;
        gridLayout.setConstraints(productPageDivider, c);
        panel.add(productPageDivider);

        JLabel quantityLabel = new JLabel("Quantity Remaining: " + storeProduct.getInt("qty"));
        quantityLabel.setFont(new Font("sans-serif", Font.PLAIN, 12));
        c.insets = new Insets(4, 24, 0, 24);
        c.gridy = 3;
        gridLayout.setConstraints(quantityLabel, c);
        panel.add(quantityLabel);

        JLabel priceLabel = new JLabel("Price: $" + storeProduct.getDouble("price"));
        priceLabel.setFont(new Font("sans-serif", Font.BOLD, 12));
        c.insets = new Insets(4, 24, 0, 24);

        c.gridy = 4;
        c.gridwidth = 4;
        gridLayout.setConstraints(priceLabel, c);
        panel.add(priceLabel);

        JSeparator divider = new JSeparator(JSeparator.VERTICAL);
        divider.setBackground(Color.decode("#dbdbdb"));
        divider.setForeground(Color.decode("#dbdbdb"));

        storePage.add(panel, BorderLayout.WEST);
        storePage.add(divider, BorderLayout.CENTER);
        storePage.add(purchasePanel(storePage, product, storeProduct, storeId), BorderLayout.EAST);

        storePage.setVisible(true);

    }

    public JPanel purchasePanel(JFrame productFrame, JSONObject product, JSONObject storeProduct, String storeId) {

        JPanel panel = new JPanel();
        panel.setMinimumSize(new Dimension(300, 480));
        panel.setPreferredSize(new Dimension(300, 480));
        panel.setMaximumSize(new Dimension(300, 480));
        GridBagLayout gridLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panel.setLayout(gridLayout);
        c.insets = new Insets(0, 24, 0, 24);

        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel();
        spinnerNumberModel.setMinimum(1);
        spinnerNumberModel.setMaximum(9999999);
        JSpinner spinner = new JSpinner();
        spinner.setModel(spinnerNumberModel);
        spinner.setValue(1);
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SwingUtilities.invokeLater(spinner::updateUI);
                }
            }
        });

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
        c.insets = new Insets(8, 24, 0, 24);
        addToCartButton.setMinimumSize(new Dimension(150, 24));
        addToCartButton.setPreferredSize(new Dimension(150, 24));
        gridLayout.setConstraints(addToCartButton, c);
        addToCartButton.addActionListener(e -> {
            if (Integer.parseInt(spinner.getValue().toString()) > 0) {
                if (Integer.parseInt(spinner.getValue().toString()) <= storeProduct.getInt("qty")) {

                    Client.sendToServer("addToCart", buyer.getString("id"), product.getString("product_id"), storeId, spinner.getValue().toString(), String.valueOf(storeProduct.getDouble("price")));

                    JOptionPane.showMessageDialog(null, "Successfully added to cart!", "Cart Panel", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Client.showErrorMessage("Quantity exceeds amount available!");
                }
            } else {
                Client.showErrorMessage("Quantity must be greater than 0!");
            }


        });
        panel.add(addToCartButton);

        JButton closeWindow = new JButton("Close");
        c.gridy = 34;
        c.gridwidth = 4;
        c.insets = new Insets(8, 24, 0, 24);
        closeWindow.setMinimumSize(new Dimension(150, 24));
        closeWindow.setPreferredSize(new Dimension(150, 24));
        gridLayout.setConstraints(closeWindow, c);
        closeWindow.addActionListener(e -> productFrame.dispose());
        panel.add(closeWindow);

        return panel;
    }
}


