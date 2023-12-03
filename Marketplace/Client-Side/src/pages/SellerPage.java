package pages;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.EmptyBorder;

public class SellerPage extends JFrame implements ListSelectionListener, ActionListener {

    DefaultListModel<String> storeList;
    JList<String> list;
    JButton removeStore = new JButton("Remove Store");

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

        String[] stores = new String[5];
        stores[0] = "Apple";
        stores[1] = "Samsung";
        stores[2] = "Macy's";
        stores[3] = "Best Buy";
        stores[4] = "Walmart";

        JLabel storesLabel = new JLabel("Stores");
        storesLabel.setFont(new Font("Serif", Font.BOLD, 24));
        storesLabel.setBounds(150,18, 400, 24);

        storeList = new DefaultListModel<>();
        list = new JList<>();
        list.addListSelectionListener(this);
        list.setBounds(150, 50, 400, 350);
        list.setFont(new Font("Calibri", Font.BOLD, 16));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);

        class MyCellRenderer extends DefaultListCellRenderer
        {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                // based on the index you set the color.  This produces the every other effect.
                if (index % 2 == 0) {
                    setBackground(Color.lightGray);
                }
                else {
                    setBackground(Color.white);
                }

                return this;
            }
        }

        list.setCellRenderer(new MyCellRenderer());

        list.addMouseListener(new MouseAdapter() {
            @SuppressWarnings("unchecked")
            public void mouseClicked(MouseEvent evt) {
                JList<String> list = (JList<String>) evt.getSource();
                if (evt.getClickCount() == 2) {
                    System.out.println("Selected " + list.getSelectedValue());
                    // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                } else if (evt.getClickCount() == 3) {

                    // Triple-click detected
                    int index = list.locationToIndex(evt.getPoint());
                }
            }
        });

        for (String store : stores) {
            storeList.addElement(store);
            list.setModel(storeList);
        }

        int padding = 13;
        JButton addStore = new JButton("Add Store");
        addStore.setBounds(150, 416, 125, 24);
        JButton editStore = new JButton("Edit Store");
        editStore.setBounds(275 + padding, 416, 125, 24);

        removeStore.addActionListener(this);
        removeStore.setBounds(400 + padding * 2, 416, 125, 24);

        this.add(storesLabel);
        this.add(list);

        this.add(addStore);
        this.add(editStore);
        this.add(removeStore);

        this.setVisible(true);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == removeStore) {
            int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete, " + list.getSelectedValue() + "?");
            if (input == 0)
                storeList.removeElementAt(list.getSelectedIndex());
        }
    }
}
