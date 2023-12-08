import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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

public class SellerPage extends JFrame {
    CardLayout cardLayout = new CardLayout();
    Container container = new Container();
    JFrame reference;
    JTable table;
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

        JTextArea nameMessage = new JTextArea("Hey, " + seller.getString("username"));
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

        c.insets = new Insets(4,80,4,24);

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
        c.insets = new Insets(4,80,4,4);
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
        c.insets = new Insets(4,4,4,24);
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
        model.addColumn("Id");

        JSONArray stores = seller.getJSONArray("stores");

        Client.sendToServer(new ArrayList<>(List.of("[getStores]")));

        String allStoresString = Client.readFromServer(1).get(0);

        if (allStoresString.equals("empty"))
            System.out.println("User has no stores");
        else {
            JSONArray allStores = new JSONArray(allStoresString);
            for (Object storeGeneric : allStores) {
                JSONObject storeGenericObj = (JSONObject) storeGeneric;
                String storeGenericId = storeGenericObj.getString("id");

                for (Object store : stores) {
                    String storeId = (String) store;

                    if (storeId.equals(storeGenericId)) {
                        model.addRow(new Object[]{storeGenericObj.getString("name"), storeGenericObj.getDouble("sales"), storeGenericObj.toString()});
                    }

                }
            }
        }

        // Create a JTable using the model
        table = new JTable(model);

        for (int c = 0; c < table.getColumnCount(); c++)
        {
            Class<?> col_class = table.getColumnClass(c);
            table.setDefaultEditor(col_class, null);        // remove editor
        }

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    editStore(new JSONObject(table.getModel().getValueAt(table.getSelectedRow(), 2).toString()));
                }
            }
        });

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);

        // SORTING BROKEN FIX THIS LATER
//        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
//        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
//        sorter.setSortKeys(sortKeys);

        TableColumnModel tcm = table.getColumnModel();
        tcm.removeColumn( tcm.getColumn(2) );

        JScrollPane scrollPane= new  JScrollPane(table);
        scrollPane.setBounds(24, 66, 400, 330);

        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.computeIfAbsent("Table.alternateRowColor", k -> new Color(240, 240, 240));

        JButton selectStoreButton = new JButton("Select Store");
        selectStoreButton.setBounds(24, 404, 400/3 - 4, 24);
        selectStoreButton.addActionListener(e -> {
            if(!table.getSelectionModel().isSelectionEmpty()) {
                editStore(new JSONObject(table.getModel().getValueAt(table.getSelectedRow(), 2).toString()));
            }

        });

        JButton addStore = new JButton("Add Store");
        addStore.setBounds(24 + 400/3 + 4, 404, 400/3 - 8, 24);
        addStore.addActionListener(e -> addStore());

        JButton removeStore = new JButton("Remove Store");
        removeStore.setBounds(24 + 400/3 * 2 + 4, 404, 400/3 - 4, 24);
        removeStore.addActionListener(e -> {
            if(!table.getSelectionModel().isSelectionEmpty()) {
                int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove " + table.getValueAt(table.getSelectedRow(), 0).toString() + "?");
                if (input == 0) {
                    ArrayList<String> data = new ArrayList<>();
                    data.add("[removeStore]");
                    data.add(seller.getString("id"));
                    data.add(new JSONObject(table.getModel().getValueAt(table.getSelectedRow(), 2).toString()).getString("id"));
                    Client.sendToServer(data);

                    ((DefaultTableModel)table.getModel()).removeRow(table.getSelectedRow());
                }
            }
        });

        storesPanel.add(storesLabel);
        storesPanel.add(helpfulLabel);
        storesPanel.add(scrollPane);

        storesPanel.add(addStore);
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
            ArrayList<String> data = new ArrayList<>();
            data.add("[updateUserDetails]");
            data.add(seller.getString("id"));
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
        emailLabel.setBounds(24, 116, 200, 24);

        JTextField emailField = new JTextField(12);
        emailField.setBounds(24, 140, 268, 24);
        emailField.setText(seller.getString("email"));

        JButton emailButton = new JButton("Change Email");
        emailButton.addActionListener(e -> {
            ArrayList<String> data = new ArrayList<>();
            data.add("[updateUserDetails]");
            data.add(seller.getString("id"));
            data.add("email");
            data.add(emailField.getText());
            Client.sendToServer(data);

            JOptionPane.showMessageDialog (null, "Email changed successfully!", "Updated Account Details", JOptionPane.INFORMATION_MESSAGE);
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
            ArrayList<String> data = new ArrayList<>();
            data.add("[updateUserDetails]");
            data.add(seller.getString("id"));
            data.add("password");
            data.add(String.valueOf(passwordField.getPassword()));
            Client.sendToServer(data);

            JOptionPane.showMessageDialog (null, "Password changed successfully!", "Updated Account Details", JOptionPane.INFORMATION_MESSAGE);
        });

        settingsPanel.add(passwordLabel);
        settingsPanel.add(passwordField);
        settingsPanel.add(passwordButton);

        JSeparator accountDetailsDivider = new JSeparator(JSeparator.HORIZONTAL);
        accountDetailsDivider.setBounds(24, 240, 450,24);
        accountDetailsDivider.setBackground(Color.decode("#dbdbdb"));
        accountDetailsDivider.setForeground(Color.decode("#dbdbdb"));

        JButton deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.setForeground(Color.decode("#d11111"));
        deleteAccountButton.setBounds(24, 264, 150, 24);
        deleteAccountButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account?", "Account Removal", JOptionPane.OK_CANCEL_OPTION);
            if (option == 0) {
                ArrayList<String> data = new ArrayList<>();
                data.add("[deleteAccount]");
                data.add(seller.getString("id"));
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

        JLabel statisticsLabel = new JLabel("Seller Statistics");
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

    public void addStore() {

        // Create a dialog box with a text field and a button
        String storeName = JOptionPane.showInputDialog("Enter store name (Must be less than 16 characters)", "Store");
        ArrayList<String> data = new ArrayList<>();

        if(storeName != null) {
            if (storeName.length() <= 16) {
                System.out.println("User created store: " + storeName);

                data.add("[createStore]");
                data.add(seller.getString("id"));
                data.add(storeName);
                Client.sendToServer(data);

                String storeId = Client.readFromServer(1).get(0);

                // Create a DefaultTableModel
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.addRow(new Object[]{storeName, "$0.00", storeId});
            } else {
                Client.showErrorMessage("Store name cannot be more than 16 characters!");
            }

        }

    }

    public void editStore(JSONObject store) {

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
        model.addColumn("Id");

        JSONArray products = store.getJSONArray("products");

        Client.sendToServer(new ArrayList<>(List.of("[getProducts]")));

        String allProductsString = Objects.requireNonNull(Client.readFromServer(1)).get(0);
        JSONArray allProducts = new JSONArray(allProductsString);
        ArrayList<String> productNames = new ArrayList<>();

        if (allProductsString.equals("empty"))
            System.out.println("Store has no products");
        else {
            for (Object productGeneric : allProducts) {
                JSONObject productGenericObj = (JSONObject) productGeneric;
                String storeGenericId = productGenericObj.getString("id");

                productNames.add(productGenericObj.getString("name"));

                for (Object product : products) {
                    JSONObject productObj = (JSONObject) product;
                    String productId = productObj.getString("id");

                    if (productId.equals(storeGenericId)) {
                        productNames.remove(productGenericObj.getString("name"));
                        model.addRow(new Object[]{productGenericObj.getString("name"), productObj.getInt("qty"), String.format("$%.2f", productObj.getDouble("price")), productObj.getString("id")});
                    }
                }
            }
        }

        // Create a JTable using the model
        JTable productTable = new JTable(model);

        TableColumnModel tcm = productTable.getColumnModel();
        tcm.removeColumn( tcm.getColumn(3) );

        JLabel titleMessage = new JLabel(store.getString("name") + "'s Products");
        titleMessage.setFont(new Font("serif", Font.BOLD, 18));
        titleMessage.setBounds(233, 24, 365, 24);

        JLabel optionsTitle = new JLabel("Options");
        optionsTitle.setFont(new Font("serif", Font.BOLD, 18));
        optionsTitle.setBounds(24, 24, 200, 24);

        JButton changeStoreName = new JButton("Change Store Name");
        changeStoreName.setBounds(24, 56, 185, 24);
        changeStoreName.addActionListener(e -> {
            String storeName = JOptionPane.showInputDialog("What would you like to change the name to?");

            if (storeName.length() <= 16) {

                boolean storeExists = false;
                Client.sendToServer(new ArrayList<>(List.of("[getStores]")));
                String storesString = Client.readFromServer(1).get(0);
                JSONArray stores = new JSONArray(storesString);

                for (Object storeObj : stores) {
                    if (((JSONObject) storeObj).getString("name").equals(storeName)) {
                        storeExists = true;
                    }
                }

                if (storeExists) {
                    Client.showErrorMessage("Store already exists!");
                } else {
                    ArrayList<String> data = new ArrayList<>();
                    data.add("[changeStoreName]");
                    data.add(store.getString("id"));
                    data.add(storeName);
                    Client.sendToServer(data);

                    titleMessage.setText(storeName + "'s Products");
                    table.getModel().setValueAt(storeName, table.getSelectedRow(), 0);
                }
            } else {
                Client.showErrorMessage("Please enter a store name that is less than 16 characters!");
            }

        });

        JButton addProduct = new JButton("Add Product");
        addProduct.setBounds(24, 88, 185, 24);
        addProduct.addActionListener(e -> {

            String[] array = new String[productNames.size()];
            for(int i = 0; i < array.length; i++) {
                array[i] = productNames.get(i);
            }
            JComboBox productList = new JComboBox(array);
            productList.setEditable(true);
            AutoCompletion.enable(productList);

            JTextField quantity = new JTextField();
            JTextField price = new JTextField();
            Object[] message = {
                    "Select a Product: ", productList,
                    "Quantity (1-99):", quantity,
                    "Price: $", price
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Add a product", JOptionPane.OK_CANCEL_OPTION);

            String productId = "";

            if (option == JOptionPane.OK_OPTION) {

                if (isValidQuantity(quantity.getText(), 100)) {
                    if (isValidPrice(price.getText(), 1000000.00)) {
                        String productName = Objects.requireNonNull(productList.getSelectedItem()).toString();

                        for (Object product : allProducts) {
                            JSONObject productObj = (JSONObject) product;
                            if (productObj.getString("name").equals(productName))
                                productId = productObj.getString("id");
                        }

                        ArrayList<String> data = new ArrayList<>();
                        data.add("[addProduct]");
                        data.add(store.getString("id"));
                        data.add(productId);
                        data.add(quantity.getText());
                        data.add(price.getText());
                        Client.sendToServer(data);

                        model.addRow(new Object[]{productName, quantity.getText(), String.format("$%.2f", Double.parseDouble(price.getText())), productId});
                    } else {
                        Client.showErrorMessage("Please enter a valid price. Must be less than $1,000,000");
                    }

                } else {
                    Client.showErrorMessage("Please enter a valid quantity!");
                }

            } else {
                System.out.println("Product addition canceled");
            }
        });

        JButton removeProduct = new JButton("Remove Product");
        removeProduct.setBounds(24, 120, 185, 24);
        removeProduct.addActionListener(e -> {

            if (!productTable.getSelectionModel().isSelectionEmpty()) {
                int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + productTable.getValueAt(productTable.getSelectedRow(), 0) + "?");
                if (input == 0) {
                    ArrayList<String> data = new ArrayList<>();
                    data.add("[removeProduct]");
                    data.add(store.getString("id"));
                    data.add(productTable.getModel().getValueAt(productTable.getSelectedRow(), 3).toString());
                    Client.sendToServer(data);
                    ((DefaultTableModel)productTable.getModel()).removeRow(productTable.getSelectedRow());
                }
            }
        });

        JButton closeProduct = new JButton("Close");
        closeProduct.setBounds(24, 397, 185, 24);
        closeProduct.addActionListener(e -> storePage.dispose());

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


