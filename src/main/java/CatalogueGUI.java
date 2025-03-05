import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogueGUI {
    private JFrame frame;
    private DefaultListModel<String> itemListModel;
    private JList<String> itemList;

    public CatalogueGUI() {
        frame = new JFrame("Fashion and Clothing Catalogue");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());

        // Item List
        itemListModel = new DefaultListModel<>();
        itemList = new JList<>(itemListModel);
        JScrollPane scrollPane = new JScrollPane(itemList);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Item");
        JButton editButton = new JButton("Edit Item");
        JButton removeButton = new JButton("Remove Item");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Load items into the list
        loadClothingItems();

        // Button actions
        addButton.addActionListener(e -> addClothingItem());
        editButton.addActionListener(e -> editClothingItem());
        removeButton.addActionListener(e -> removeClothingItem());

        frame.setVisible(true);
    }

    private void loadClothingItems() {
        itemListModel.clear();
        List<Map<String, Object>> items = ParseDatabase.getClothingItems();
        for (Map<String, Object> item : items) {
            itemListModel.addElement(item.get("id") + " | " + item.get("name") + " | " + item.get("colour"));
        }
    }

    private void addClothingItem() {
        JTextField nameField = new JTextField();
        JTextField colorField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField sizeField = new JTextField();
        JTextField descriptionField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Color:")); panel.add(colorField);
        panel.add(new JLabel("Type:")); panel.add(typeField);
        panel.add(new JLabel("Size:")); panel.add(sizeField);
        panel.add(new JLabel("Description:")); panel.add(descriptionField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Map<String, String> newItem = new HashMap<>();
            newItem.put("name", nameField.getText());
            newItem.put("colour", colorField.getText());
            newItem.put("itemType", typeField.getText());
            newItem.put("size", sizeField.getText());
            newItem.put("description", descriptionField.getText());
            ParseDatabase.addClothingItem(newItem);
            loadClothingItems();
        }
    }

    private void editClothingItem() {
        if (itemList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(frame, "Select an item to edit.");
            return;
        }

        String selectedValue = itemList.getSelectedValue();
        int id = Integer.parseInt(selectedValue.split(" \\|")[0]);

        JTextField nameField = new JTextField();
        JTextField colorField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField sizeField = new JTextField();
        JTextField descriptionField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Color:")); panel.add(colorField);
        panel.add(new JLabel("Type:")); panel.add(typeField);
        panel.add(new JLabel("Size:")); panel.add(sizeField);
        panel.add(new JLabel("Description:")); panel.add(descriptionField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Edit Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Map<String, String> updatedItem = new HashMap<>();
            updatedItem.put("name", nameField.getText());
            updatedItem.put("colour", colorField.getText());
            updatedItem.put("itemType", typeField.getText());
            updatedItem.put("size", sizeField.getText());
            updatedItem.put("description", descriptionField.getText());
            ParseDatabase.editClothingItem(id, updatedItem);
            loadClothingItems();
        }
    }

    private void removeClothingItem() {
        if (itemList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(frame, "Select an item to remove.");
            return;
        }

        String selectedValue = itemList.getSelectedValue();
        int id = Integer.parseInt(selectedValue.split(" \\|")[0]);

        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to remove this item?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ParseDatabase.removeClothingItem(id);
            loadClothingItems();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CatalogueGUI::new);
    }
}
