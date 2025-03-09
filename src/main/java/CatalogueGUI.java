import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogueGUI {
    private JFrame frame;
    private DefaultListModel<String> itemListModel;
    private JList<String> itemList;
    private Icon clothingIcon;
    private ImageIcon fadedLogo;

    public CatalogueGUI() {
        frame = new JFrame("Fashion and Clothing Catalogue");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Load and resize clothing icon
        ImageIcon originalIcon = new ImageIcon("src/main/java/ClothingIcon.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        clothingIcon = new ImageIcon(scaledImage);

        // Set background color
        frame.getContentPane().setBackground(Color.WHITE);

        // Item List
        itemListModel = new DefaultListModel<>();
        itemList = new JList<>(itemListModel);
        itemList.setFont(new Font("Arial", Font.PLAIN, 14));
        itemList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(itemList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(220, 220, 220));
        JButton addButton = new JButton("Add Item");
        JButton editButton = new JButton("Edit Item");
        JButton removeButton = new JButton("Remove Item");
        JButton exitButton = new JButton("Exit");

        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        removeButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));

        addButton.setBackground(new Color(70, 130, 180)); // SteelBlue
        editButton.setBackground(new Color(210, 105, 30)); // Chocolate
        removeButton.setBackground(new Color(178, 34, 34)); // FireBrick
        exitButton.setBackground(new Color(255, 0, 0)); // Red-Orange

        addButton.setForeground(Color.WHITE);
        editButton.setForeground(Color.WHITE);
        removeButton.setForeground(Color.WHITE);
        exitButton.setForeground(Color.WHITE);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(exitButton); // Add the exit button to the panel
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Load items into the list
        loadClothingItems();

        // Button actions
        addButton.addActionListener(e -> addClothingItem());
        editButton.addActionListener(e -> editClothingItem());
        removeButton.addActionListener(e -> removeClothingItem());
        exitButton.addActionListener(e -> exitApplication()); // Action for exit button

        frame.setVisible(true);
    }

    private void loadClothingItems() {
        itemListModel.clear();
        List<Map<String, Object>> items = ParseDatabase.getClothingItems();
        for (Map<String, Object> item : items) {
            itemListModel.addElement("<html><body style='padding: 10px;'>" + item.get("id") + " | " + item.get("name") + " | " + item.get("colour") + " | " + item.get("itemType") + " | " + item.get("size") + " | " + item.get("description") + "</body></html>");
        }
    }

    private void addClothingItem() {
        JTextField nameField = new JTextField();
        JTextField colorField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField sizeField = new JTextField();
        JTextField descriptionField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Color:")); panel.add(colorField);
        panel.add(new JLabel("Type:")); panel.add(typeField);
        panel.add(new JLabel("Size:")); panel.add(sizeField);
        panel.add(new JLabel("Description:")); panel.add(descriptionField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Item", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, clothingIcon);
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
            JOptionPane.showMessageDialog(frame, "Select an item to edit.", "Edit Item", JOptionPane.PLAIN_MESSAGE, clothingIcon);
            return;
        }

        String selectedValue = itemList.getSelectedValue().replaceAll("<[^>]*>", "");
        int id = Integer.parseInt(selectedValue.split(" \\|")[0].trim());

        List<Map<String, Object>> clothingItems = ParseDatabase.getClothingItems();

        // Find the item by ID
        Map<String, String> existingItem = null;
        for (Map<String, Object> item : clothingItems) {
            if ((int) item.get("id") == id) { // Assuming the "id" field is an Integer
                existingItem = new HashMap<>();
                existingItem.put("name", (String) item.get("name"));
                existingItem.put("colour", (String) item.get("colour"));
                existingItem.put("itemType", (String) item.get("itemType"));
                existingItem.put("size", (String) item.get("size"));
                existingItem.put("description", (String) item.get("description"));
                break;
            }
        }

        // Pre-fill the fields with the existing item details
        JTextField nameField = new JTextField(existingItem.get("name"));
        JTextField colorField = new JTextField(existingItem.get("colour"));
        JTextField typeField = new JTextField(existingItem.get("itemType"));
        JTextField sizeField = new JTextField(existingItem.get("size"));
        JTextField descriptionField = new JTextField(existingItem.get("description"));

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Color:")); panel.add(colorField);
        panel.add(new JLabel("Type:")); panel.add(typeField);
        panel.add(new JLabel("Size:")); panel.add(sizeField);
        panel.add(new JLabel("Description:")); panel.add(descriptionField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Edit Item", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, clothingIcon);
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
            JOptionPane.showMessageDialog(frame, "Select an item to remove.", "Remove Item", JOptionPane.PLAIN_MESSAGE, clothingIcon);
            return;
        }

        String selectedValue = itemList.getSelectedValue().replaceAll("<[^>]*>", "");
        int id = Integer.parseInt(selectedValue.split(" \\|")[0].trim());

        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to remove this item?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, clothingIcon);
        if (confirm == JOptionPane.YES_OPTION) {
            ParseDatabase.removeClothingItem(id);
            loadClothingItems();
        }
    }

    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, clothingIcon);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0); // Exit the application
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CatalogueGUI::new);
        ParseDatabase.initializeDatabase();
        ParseDatabase.addGucciDress();
    }
}
