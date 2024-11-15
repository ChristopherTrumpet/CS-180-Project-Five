import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * SellerPage
 * <p>
 * handles the UI for seller operations.
 *
 * @author Chris Trumpet, Matthew Lee, Mohit Ambe, Shrinand Perumal, Vraj Patel
 * @version December 11, 2023
 */
public class SellerPage extends JFrame {
    CardLayout cardLayout = new CardLayout();
    Container container = new Container();
    JFrame reference;
    JSONObject seller;

    public SellerPage(JSONObject seller) {

        this.seller = seller;
        this.reference = this;

        // Set title of window
        this.setTitle("Purdue Marketplace (SELLER)");

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
        container.setVisible(true);


        this.add(sidePanel, BorderLayout.WEST);
        this.add(container, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public JPanel SidePanel() {

        JPanel panel = new JPanel();
        GridBagLayout gridLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 80, 0, 24);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        panel.setLayout(gridLayout);

        JLabel welcomeMessage = new JLabel("Purdue Marketplace");
        welcomeMessage.setFont(new Font("serif", Font.BOLD, 18));
        c.gridy = 0;
        c.gridwidth = 4;
        gridLayout.setConstraints(welcomeMessage, c);
        panel.add(welcomeMessage);

        c.insets = new Insets(0, 80, 8, 24);

        JTextArea nameMessage = new JTextArea("Hey, " + seller.getString("username"));
        nameMessage.setFont(new Font("sans-serif", Font.PLAIN, 14));
        nameMessage.setLineWrap(true);
        nameMessage.setWrapStyleWord(true);
        nameMessage.setFocusable(false);
        nameMessage.setEditable(false);
        nameMessage.setOpaque(false);
        c.gridy = 1;
        c.gridwidth = 4;
        gridLayout.setConstraints(nameMessage, c);
        panel.add(nameMessage);

        c.insets = new Insets(4, 80, 4, 24);

        JButton storesButton = new JButton("Stores");
        storesButton.addActionListener(e -> cardLayout.show(container, "stores"));
        c.gridy = 2;
        c.gridwidth = 4;
        gridLayout.setConstraints(storesButton, c);
        panel.add(storesButton);

        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e -> cardLayout.show(container, "settings"));
        c.gridy = 3;
        c.gridwidth = 4;
        gridLayout.setConstraints(settingsButton, c);
        panel.add(settingsButton);

        JButton statisticsButton = new JButton("Statistics");
        statisticsButton.addActionListener(e -> cardLayout.show(container, "statistics"));
        c.gridy = 4;
        c.gridwidth = 4;
        gridLayout.setConstraints(statisticsButton, c);
        panel.add(statisticsButton);

        JButton importProductsButton = new JButton("Import Products");
        importProductsButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(".csv", "csv");
            chooser.setFileFilter(filter);
            chooser.showOpenDialog(null);

            if (chooser.getSelectedFile() != null) {

                Client.sendToServer("importProducts", seller.toString(), chooser.getSelectedFile().getAbsolutePath());

                // UPDATE SELLER DATA
                Client.sendToServer("getUser", seller.getString("id"));
                String userString = Objects.requireNonNull(Client.readFromServer(1)).get(0);
                this.seller = new JSONObject(userString);

                container.remove(Stores());
                container.add("stores", Stores());
                cardLayout.show(container, "stores");
                JOptionPane.showMessageDialog(null, "Imported Products successfully!", "Product Imports", JOptionPane.INFORMATION_MESSAGE);

            }
        });
        c.gridy = 5;
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
        c.gridy = 6;
        c.gridwidth = 2;
        c.gridx = 0;
        c.insets = new Insets(4, 80, 4, 4);
        gridLayout.setConstraints(logoutButton, c);
        panel.add(logoutButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit");
            if (input == 0) {
                reference.dispose();
            }
        });
        c.gridy = 6;
        c.gridx = 2;
        c.gridwidth = 2;
        c.insets = new Insets(4, 4, 4, 24);
        gridLayout.setConstraints(exitButton, c);
        panel.add(exitButton);

        return panel;
    }

    public JPanel Stores() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel storesPanel = new JPanel();
        storesPanel.setLayout(null);

        JLabel storesLabel = new JLabel("Your Stores");
        storesLabel.setFont(new Font("Serif", Font.BOLD, 18));
        storesLabel.setBounds(24, 16, 400, 24);

        JLabel helpfulLabel = new JLabel("Select a store to view");
        helpfulLabel.setFont(new Font("serif", Font.PLAIN, 14));
        helpfulLabel.setBounds(24, 36, 400, 24);

        // Create a DefaultTableModel
        DefaultTableModel model = new DefaultTableModel();

        // Add some data to the model
        model.addColumn("Stores");
        model.addColumn("Sales");

        JSONArray stores = seller.getJSONArray("stores");

        Client.sendToServer("getStores");

        String allStoresString = Objects.requireNonNull(Client.readFromServer(1)).get(0);

        if (allStoresString.equals("empty")) System.out.println("User has no stores");
        else {
            JSONArray allStores = new JSONArray(allStoresString);
            for (Object storeGeneric : allStores) {
                JSONObject storeGenericObj = (JSONObject) storeGeneric;
                String storeGenericId = storeGenericObj.getString("id");

                for (Object store : stores) {
                    String storeId = (String) store;

                    if (storeId.equals(storeGenericId)) {
                        boolean storeExists = false;
                        for (int i = 0; i < model.getRowCount(); i++) {
                            if (model.getValueAt(i, 0).equals(storeGenericObj.getString("name"))) {
                                storeExists = true;
                            }
                        }
                        if (!storeExists) {
                            DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
                            model.addRow(new Object[]{storeGenericObj.getString("name"), "$" + decimalFormat.format(storeGenericObj.getDouble("sales"))});
                        }
                    }
                }
            }
        }

        // Create a JTable using the model
        JTable storesTable = new JTable(model);

        for (int c = 0; c < storesTable.getColumnCount(); c++) {
            Class<?> col_class = storesTable.getColumnClass(c);
            storesTable.setDefaultEditor(col_class, null);        // remove editor
        }

        storesTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {

                    Client.sendToServer("getStore", table.getModel().getValueAt(table.getSelectedRow(), 0).toString());

                    JSONObject store = new JSONObject(Objects.requireNonNull(Client.readFromServer(1)).get(0));
                    editStore(store, storesTable, storesTable.getSelectedRow());
                }
            }
        });

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(storesTable.getModel());
        storesTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(storesTable);
        scrollPane.setBounds(24, 66, 400, 330);

        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.computeIfAbsent("Table.alternateRowColor", k -> new Color(240, 240, 240));

        JButton selectStoreButton = new JButton("Select Store");
        selectStoreButton.setBounds(24, 404, 400 / 3 - 4, 24);
        selectStoreButton.addActionListener(e -> {
            if (!storesTable.getSelectionModel().isSelectionEmpty()) {

                Client.sendToServer("getStore", storesTable.getModel().getValueAt(storesTable.getSelectedRow(), 0).toString());

                JSONObject store = new JSONObject(Objects.requireNonNull(Client.readFromServer(1)).get(0));
                editStore(store, storesTable, storesTable.getSelectedRow());
            } else {
                Client.showErrorMessage("Please add or select a store from the list!");
            }

        });

        JButton createStore = new JButton("Create Store");
        createStore.setBounds(24 + 400 / 3 + 4, 404, 400 / 3 - 8, 24);
        createStore.addActionListener(e -> addStore(model));

        JButton removeStore = new JButton("Remove Store");
        removeStore.setBounds(24 + 400 / 3 * 2 + 4, 404, 400 / 3 - 4, 24);
        removeStore.addActionListener(e -> {
            if (!storesTable.getSelectionModel().isSelectionEmpty()) {
                int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove " + storesTable.getValueAt(storesTable.getSelectedRow(), 0).toString() + "?");
                if (input == 0) {

                    Client.sendToServer("removeStore", seller.getString("id"), storesTable.getModel().getValueAt(storesTable.getSelectedRow(), 0).toString());

                    ((DefaultTableModel) storesTable.getModel()).removeRow(storesTable.getSelectedRow());
                }
            }
        });

        storesPanel.add(storesLabel);
        storesPanel.add(helpfulLabel);
        storesPanel.add(scrollPane);

        storesPanel.add(createStore);
        storesPanel.add(selectStoreButton);
        storesPanel.add(removeStore);

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
        usernameField.setText(seller.getString("username"));

        JButton usernameButton = new JButton("Change Username");
        usernameButton.setBounds(300, 88, 174, 24);
        usernameButton.addActionListener(e -> {

            Client.sendToServer("updateUserDetails", seller.getString("id"), "username", usernameField.getText());


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
        emailField.setText(seller.getString("email"));

        JButton emailButton = new JButton("Change Email");
        emailButton.addActionListener(e -> {

            Client.sendToServer("updateUserDetails", seller.getString("id"), "email", emailField.getText());

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
        passwordField.setText(seller.getString("password"));

        JButton passwordButton = new JButton("Change Password");
        passwordButton.setBounds(300, 192, 174, 24);
        passwordButton.addActionListener(e -> {

            Client.sendToServer("updateUserDetails", seller.getString("id"), "password", String.valueOf(passwordField.getPassword()));

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

                Client.sendToServer("deleteAccount", seller.getString("id"));
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

        JLabel statisticsLabel = new JLabel("Seller Statistics");
        statisticsLabel.setFont(new Font("Serif", Font.BOLD, 18));
        statisticsLabel.setBounds(24, 16, 200, 24);

        JLabel supportLabel = new JLabel("View your sales statistics");
        supportLabel.setFont(new Font("serif", Font.PLAIN, 14));
        supportLabel.setBounds(24, 36, 400, 24);

        statisticsPanel.add(statisticsLabel);
        statisticsPanel.add(supportLabel);

        JLabel customerLabel = new JLabel("Top items by customer");
        customerLabel.setFont(new Font("serif", Font.BOLD, 14));
        customerLabel.setBounds(24, 60, 150, 24);

        JLabel productsLabel = new JLabel("Top items by sales");
        productsLabel.setFont(new Font("serif", Font.BOLD, 14));
        productsLabel.setBounds(236, 60, 150, 24);

        statisticsPanel.add(customerLabel);
        statisticsPanel.add(productsLabel);

        // Create a DefaultTableModel
        DefaultTableModel customerModel = new DefaultTableModel();

        // Add some data to the model
        customerModel.addColumn("Customer");
        customerModel.addColumn("Items");

        Client.sendToServer("getUsers");
        JSONArray users = new JSONArray(Objects.requireNonNull(Client.readFromServer(1)).get(0));

        Client.sendToServer("getStores");
        JSONArray stores = new JSONArray(Objects.requireNonNull(Client.readFromServer(1)).get(0));

        HashMap<String, Integer> customerSort = new HashMap<>();

        for (Object user : users) {
            try {
                if (!((JSONObject) user).getJSONArray("product_history").isEmpty()) {
                    for (Object productHistory : ((JSONObject) user).getJSONArray("product_history")) {
                        for (Object store : stores) {
                            String storeId = ((JSONObject) store).getString("id");
                            String historyStoreId = ((JSONObject) productHistory).getString("store_id");
                            if (storeId.equals(historyStoreId)) {

                                String customerName = ((JSONObject) user).getString("username");
                                int qtyLength = ((JSONObject) user).getJSONArray("product_history").length();
                                customerSort.put(customerName, qtyLength);
                            }
                        }
                    }
                }
            } catch (JSONException ignore) {
                System.out.println("User not a buyer");
            }
        }

        for (String customer : customerSort.keySet()) {
            customerModel.addRow(new Object[]{customer, customerSort.get(customer)});
        }

        // Create a JTable using the model
        JTable customerTable = new JTable(customerModel);

        for (int c = 0; c < customerTable.getColumnCount(); c++) {
            Class<?> col_class = customerTable.getColumnClass(c);
            customerTable.setDefaultEditor(col_class, null);        // remove editor
        }

        JScrollPane customerTableStat = new JScrollPane(customerTable);
        customerTableStat.setBounds(24, 92, 200, 344);

        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.computeIfAbsent("Table.alternateRowColor", k -> new Color(240, 240, 240));

        statisticsPanel.add(customerTableStat);

        // Create a DefaultTableModel
        DefaultTableModel productModel = new DefaultTableModel();

        // Add some data to the model
        productModel.addColumn("Product");
        productModel.addColumn("Sales");

        Client.sendToServer("getProducts");
        JSONArray products = new JSONArray(Objects.requireNonNull(Client.readFromServer(1)).get(0));

        HashMap<String, Double> productSort = new HashMap<>();


        for (Object sellerStores : seller.getJSONArray("stores")) {
            for (Object store : stores) {
                if (sellerStores.equals(((JSONObject) store).getString("id"))) {
                    for (Object storeProduct : ((JSONObject) store).getJSONArray("products")) {
                        for (Object product : products) {
                            if (((JSONObject) product).getString("product_id").equals(((JSONObject) storeProduct).getString("id"))) {
                                productSort.put(((JSONObject) product).getString("name"), ((JSONObject) storeProduct).getDouble("sales"));
                            }
                        }
                    }
                }
            }
        }

        for (String product : productSort.keySet()) {
            productModel.addRow(new Object[]{product, productSort.get(product)});
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

    public void addStore(DefaultTableModel storeModel) {

        // Create a dialog box with a text field and a button
        String storeName = JOptionPane.showInputDialog("Enter store name (Must be less than 16 characters)", "Store");

        if (storeName != null) {
            if (storeName.length() <= 16) {

                Client.sendToServer("createStore", seller.getString("id"), storeName);

                String storeId = Objects.requireNonNull(Client.readFromServer(1)).get(0);

                // Create a DefaultTableModel
                storeModel.addRow(new Object[]{storeName, "$0.00", storeId});
                storeModel.fireTableDataChanged();
            } else {
                Client.showErrorMessage("Store name cannot be more than 16 characters!");
            }

        }

    }

    public void editStore(JSONObject store, JTable storeTable, int storeRow) {

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

        DefaultTableModel productModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // make read only fields except column 1 and 2
                return column == 1 || column == 2;
            }
        };

        // Add some data to the model
        productModel.addColumn("Product Name");
        productModel.addColumn("Quantity");
        productModel.addColumn("Price");

        JSONArray products = store.getJSONArray("products");
        Client.sendToServer("getProducts");

        String allProductsString = Objects.requireNonNull(Client.readFromServer(1)).get(0);
        JSONArray allProducts = new JSONArray(allProductsString);

        // Removes products already in store list from being added again.
        if (allProductsString.equals("empty")) System.out.println("Store has no products");
        else {
            for (Object product : allProducts) {
                for (Object storeProduct : products) {

                    String indexedProductId = ((JSONObject) product).getString("product_id");
                    String productId = ((JSONObject) storeProduct).getString("id");

                    String name = ((JSONObject) product).getString("name");
                    int quantity = ((JSONObject) storeProduct).getInt("qty");
                    double price = ((JSONObject) storeProduct).getDouble("price");

                    if (indexedProductId.equals(productId)) {
                        productModel.addRow(new Object[]{name, quantity, price});
                    }
                }
            }
        }

        // Create a JTable using the model
        JTable productTable = new JTable(productModel);

        productModel.addTableModelListener(e -> {

            int row = e.getFirstRow();
            int column = e.getColumn();

            try {
                String valueStr = productTable.getValueAt(row, column).toString();

                // Type
                switch (column) {
                    case 1 ->
                            Client.sendToServer("updateProduct", store.getString("id"), productModel.getValueAt(row, 0).toString(), "quantity", valueStr);
                    case 2 ->
                            Client.sendToServer("updateProduct", store.getString("id"), productModel.getValueAt(row, 0).toString(), "price", valueStr.substring(1));
                }
            } catch (ArrayIndexOutOfBoundsException ignore) {
            }

        });

        JLabel titleMessage = new JLabel(store.getString("name") + "'s Products");
        titleMessage.setFont(new Font("serif", Font.BOLD, 18));
        titleMessage.setBounds(233, 24, 365, 24);
        storePage.add(titleMessage);

        JLabel optionsTitle = new JLabel("Options");
        optionsTitle.setFont(new Font("serif", Font.BOLD, 18));
        optionsTitle.setBounds(24, 24, 200, 24);
        storePage.add(optionsTitle);

        JButton changeStoreName = new JButton("Change Store Name");
        changeStoreName.setBounds(24, 56, 185, 24);
        changeStoreName.addActionListener(e -> {

            String storeName = JOptionPane.showInputDialog("What would you like to change the name to?");

            if (storeName != null) {
                if (storeName.length() <= 16) {

                    boolean storeExists = false;

                    Client.sendToServer("getStores");
                    String storesString = Objects.requireNonNull(Client.readFromServer(1)).get(0);
                    JSONArray stores = new JSONArray(storesString);

                    for (Object storeObj : stores) {
                        if (((JSONObject) storeObj).getString("name").equals(storeName)) {
                            storeExists = true;
                        }
                    }

                    if (storeExists) {
                        Client.showErrorMessage("Store already exists!");
                    } else {

                        Client.sendToServer("changeStoreName", store.getString("id"), storeName);

                        // UPDATE TITLE NAME
                        titleMessage.setText(storeName + "'s Products");
                        storeTable.getModel().setValueAt(storeName, storeRow, 0);

                    }
                } else {
                    Client.showErrorMessage("Please enter a store name that is less than 16 characters!");
                }
            }

        });

        storePage.add(changeStoreName);

        ArrayList<String> productNames = new ArrayList<>();

        // Removes products already in store list from being added again.
        if (allProductsString.equals("empty")) System.out.println("Store has no products");
        else {
            for (Object product : allProducts) {
                for (Object storeProduct : products) {

                    String indexedProductId = ((JSONObject) product).getString("product_id");
                    String productId = ((JSONObject) storeProduct).getString("id");

                    String name = ((JSONObject) product).getString("name");

                    if (!indexedProductId.equals(productId)) {
                        productNames.add(name);
                    }
                }
            }
        }

        JButton addProduct = new JButton("Add Product");
        addProduct.setBounds(24, 88, 185, 24);
        addProduct.addActionListener(e -> {

            if (!productNames.isEmpty()) {

                String[] array = new String[productNames.size()];
                for (int i = 0; i < array.length; i++) {
                    array[i] = productNames.get(i);
                }

                JComboBox<String> productList = new JComboBox<>(array);
                productList.setEditable(true);
                AutoCompletion.enable(productList);

                JTextField quantity = new JTextField();
                JTextField price = new JTextField();
                Object[] message = {"Select a Product: ", productList, "Quantity (1-9999):", quantity, "Price: $", price};

                int option = JOptionPane.showConfirmDialog(null, message, "Add a product", JOptionPane.OK_CANCEL_OPTION);

                String productId = "";

                if (option == JOptionPane.OK_OPTION && productList.getSelectedItem() != null) {

                    if (isValidQuantity(quantity.getText(), 9999)) {
                        if (isValidPrice(price.getText(), 1000000.00)) {
                            String productName = productList.getSelectedItem().toString();

                            for (Object product : allProducts) {
                                JSONObject productObj = (JSONObject) product;
                                if (productObj.getString("name").equals(productName))
                                    productId = productObj.getString("product_id");
                            }

                            Client.sendToServer("addProduct", store.getString("id"), productId, quantity.getText(), price.getText());

                            productModel.addRow(new Object[]{productName, quantity.getText(), String.format("$%.2f", Double.parseDouble(price.getText()))});

                        } else {
                            Client.showErrorMessage("Please enter a valid price. Must be less than $1,000,000");
                        }

                    } else {
                        Client.showErrorMessage("Please enter a valid quantity!");
                    }

                } else {
                    System.out.println("Product addition canceled");
                }
            } else {
                Client.showErrorMessage("There are currently no items in the system, please create a new product!");
            }

        });

        storePage.add(addProduct);

        JButton createProductButton = new JButton("Create Product");
        createProductButton.setBounds(24, 120, 185, 24);
        createProductButton.addActionListener(e -> {

            JTextField productName = new JTextField();
            JTextArea productDescription = new JTextArea();
            productDescription.setLineWrap(true);
            productDescription.setWrapStyleWord(true);

            JScrollPane descriptionScroll = new JScrollPane(productDescription);
            descriptionScroll.setPreferredSize(new Dimension(300, 64));

            JTextField productQuantity = new JTextField();
            JTextField productPrice = new JTextField();
            Object[] message = {"Product Name: ", productName, "Description: ", descriptionScroll, "Quantity (1-9999): ", productQuantity, "Price: $", productPrice};

            int input;
            boolean validResponse;
            do {
                input = JOptionPane.showConfirmDialog(null, message, "Create a product", JOptionPane.OK_CANCEL_OPTION);

                if (input == JOptionPane.CANCEL_OPTION) break;

                if (productName.getText() == null || productDescription.getText() == null || productQuantity.getText() == null) {
                    Client.showErrorMessage("Please ensure all fields are filled out!");
                    validResponse = false;
                    continue;
                }

                try {
                    if (Integer.parseInt(productQuantity.getText()) < 0 || Integer.parseInt(productQuantity.getText()) > 9999) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    Client.showErrorMessage("Please input a valid quantity");
                    validResponse = false;
                    continue;
                }

                try {
                    if (Double.parseDouble(productPrice.getText()) < 0 || Double.parseDouble(productPrice.getText()) > 999999999) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    Client.showErrorMessage("Please input a valid price");
                    validResponse = false;
                    continue;
                }

                validResponse = true;


            } while (!validResponse);

            if (input == JOptionPane.OK_OPTION) {
                System.out.println("Create product request...");

                Client.sendToServer("createProduct", productName.getText(), productDescription.getText(), productQuantity.getText(), productPrice.getText(), store.getString("id"));

                boolean success = Objects.requireNonNull(Client.readFromServer(1)).get(0).equals("true");

                if (success) {
                    productModel.addRow(new Object[]{productName.getText(), productQuantity.getText(), String.format("$%.2f", Double.parseDouble(productPrice.getText()))});
                    productModel.fireTableDataChanged();
                } else {
                    Client.showErrorMessage("Error occurred. Product may already exist!");
                }
            }

        });
        storePage.add(createProductButton);

        JButton removeProduct = new JButton("Remove Product");
        removeProduct.setBounds(24, 152, 185, 24);
        removeProduct.addActionListener(e -> {

            if (!productTable.getSelectionModel().isSelectionEmpty()) {
                int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + productTable.getValueAt(productTable.getSelectedRow(), 0) + "?");
                if (input == 0) {

                    Client.sendToServer("removeProduct", store.getString("id"), productTable.getValueAt(productTable.getSelectedRow(), 0).toString());

                    ((DefaultTableModel) productTable.getModel()).removeRow(productTable.getSelectedRow());
                }
            }
        });
        storePage.add(removeProduct);

        JButton closeProduct = new JButton("Close");
        closeProduct.setBounds(24, 397, 185, 24);
        closeProduct.addActionListener(e -> storePage.dispose());
        storePage.add(closeProduct);

        JScrollPane sp = new JScrollPane(productTable);
        sp.setBounds(233, 56, 365, 365);
        storePage.add(sp);

        storePage.setVisible(true);

    }

    public boolean isValidQuantity(String text, int limit) {
        try {
            return Integer.parseInt(text) < limit && Integer.parseInt(text) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isValidPrice(String text, double limit) {
        try {
            return Double.parseDouble(text) < limit && Double.parseDouble(text) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}


