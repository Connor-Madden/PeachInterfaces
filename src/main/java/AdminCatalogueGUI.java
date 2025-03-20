import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AdminCatalogueGUI {
    private JFrame frame;
    public DefaultListModel<String> itemListModel;
    public JList<String> itemList;
    private Icon clothingIcon;
    private JLabel slidingTextLabel;
    private Timer slideOutTimer;
    private Timer slideInTimer;
    private int slidingTextWidth = 0;
    private final int SLIDING_TEXT_MAX_WIDTH = 150; // Maximum width of the sliding text
    private JTextField searchField;
    private JComboBox<String> filterDropdown;

    public AdminCatalogueGUI(String username) {
        frame = new JFrame("Admin - Fashion and Clothing Catalogue");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout());

        // Set the frame to full screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Open in full screen mode

        // Load and resize clothing icon
        ImageIcon originalIcon = new ImageIcon("src/main/java/ClothingIcon.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        clothingIcon = new ImageIcon(scaledImage);

        // Set background color
        frame.getContentPane().setBackground(Color.WHITE);

        // --------------- TOP PANEL -------------------
        JPanel topPanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for precise control
        topPanel.setBackground(new Color(255, 235, 205)); // Light Orange

        // --------------- LOGO PANEL (Left Side) -------------------
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(255, 235, 205));
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.X_AXIS)); // Use BoxLayout for flexible sizing

        String logoPath = "src/main/images/Logo.png";
        ImageIcon logoIcon = new ImageIcon(new ImageIcon(logoPath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JLabel logoLabel = new JLabel(logoIcon);
        logoPanel.add(logoLabel);

        // Panel for sliding text
        JPanel slidingTextPanel = new JPanel();
        slidingTextPanel.setBackground(new Color(255, 235, 205));
        slidingTextPanel.setLayout(new BoxLayout(slidingTextPanel, BoxLayout.X_AXIS));
        slidingTextPanel.setPreferredSize(new Dimension(0, 50)); // Initially hidden

        // Sliding text label
        slidingTextLabel = new JLabel("Peach Interfaces");
        slidingTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        slidingTextLabel.setForeground(new Color(60, 179, 113));
        slidingTextPanel.add(slidingTextLabel);
        logoPanel.add(slidingTextPanel);

        // Add logoPanel to topPanel with constraints
        GridBagConstraints gbcLogo = new GridBagConstraints();
        gbcLogo.gridx = 0;
        gbcLogo.gridy = 0;
        gbcLogo.weightx = 0; // Do not expand horizontally
        gbcLogo.anchor = GridBagConstraints.WEST; // Anchor to the left
        gbcLogo.insets = new Insets(0, 10, 0, 10); // Add some padding
        topPanel.add(logoPanel, gbcLogo);

        // --------------- SEARCH & FILTER PANEL (Center) -------------------
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        searchPanel.setBackground(new Color(255, 235, 205));

        searchField = new JTextField(25);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setBackground(new Color(60, 179, 113)); // Green
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);

        filterDropdown = new JComboBox<>(new String[]{"All", "Mens Clothing", "Womens Clothing", "Jewelry", "Accessories", "Kids Clothing"});
        filterDropdown.setFont(new Font("Arial", Font.BOLD, 14));
        filterDropdown.setBackground(new Color(60, 179, 113));
        filterDropdown.setForeground(Color.WHITE);

        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel("Filter: "));
        searchPanel.add(filterDropdown);

        // Add searchPanel to topPanel with constraints
        GridBagConstraints gbcSearch = new GridBagConstraints();
        gbcSearch.gridx = 1;
        gbcSearch.gridy = 0;
        gbcSearch.weightx = 1; // Expand to fill remaining space
        gbcSearch.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally
        gbcSearch.anchor = GridBagConstraints.CENTER; // Anchor to the center
        topPanel.add(searchPanel, gbcSearch);

        // Add the top panel to the frame (NORTH region)
        frame.add(topPanel, BorderLayout.NORTH);

        // Item List
        itemListModel = new DefaultListModel<>();
        itemList = new JList<>(itemListModel);
        itemList.setFont(new Font("Arial", Font.PLAIN, 14));
        itemList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        itemList.setBackground(new Color(255, 235, 205)); // Light Orange background for the list
        itemList.setOpaque(true); // Ensure the background is visible

        JScrollPane scrollPane = new JScrollPane(itemList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 179, 113), 4)); // Green border with 2px thickness
        scrollPane.getViewport().setBackground(new Color(255, 235, 205)); // Light Orange background for the scroll pane
        frame.add(scrollPane, BorderLayout.CENTER);

        // --------------- BOTTOM PANEL -------------------
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(255, 235, 205)); // Light Orange
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

// Text label (tagline) - Aligned to the left
        JLabel bottomTextLabel = new JLabel("© 2025 Peach Interfaces. Freshly Picked Clothing For You");
        bottomTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bottomTextLabel.setForeground(new Color(60, 179, 113));
        bottomTextLabel.setHorizontalAlignment(JLabel.LEFT); // Align text to the left

// Add hover effect to underline the tagline
        bottomTextLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Add underline when hovered
                bottomTextLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 16));
                bottomTextLabel.setText("<html><u>" + bottomTextLabel.getText() + "</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Remove underline when not hovered
                bottomTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
                bottomTextLabel.setText(bottomTextLabel.getText().replaceAll("<[^>]*>", "")); // Remove HTML tags
            }
        });

        bottomPanel.add(bottomTextLabel, BorderLayout.WEST); // Add tagline to WEST region

// Buttons Panel (EAST region)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5)); // Align buttons to the right
        buttonPanel.setBackground(new Color(255, 235, 205)); // Light Orange background
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add padding

        JButton addButton = new JButton("Add Item");
        JButton editButton = new JButton("Edit Item");
        JButton removeButton = new JButton("Remove Item");
        JButton exitButton = new JButton("Log Out");

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
        buttonPanel.add(exitButton);

        bottomPanel.add(buttonPanel, BorderLayout.EAST); // Add buttons to EAST region

// Add the bottom panel to the frame (SOUTH region)
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Load items into the list
        loadClothingItems();

        // Button actions
        addButton.addActionListener(e -> addClothingItem());
        editButton.addActionListener(e -> editClothingItem());
        removeButton.addActionListener(e -> removeClothingItem());
        exitButton.addActionListener(e -> exitApplication()); // Action for exit button

        // Search and Filter actions
        searchButton.addActionListener(e -> performSearch());
        filterDropdown.addActionListener(e -> performFilter());
        searchField.addActionListener(e -> performSearch());

        // Initialize timers for sliding animation
        slideOutTimer = new Timer(10, new ActionListener() { // Adjusted delay
            @Override
            public void actionPerformed(ActionEvent e) {
                if (slidingTextWidth < SLIDING_TEXT_MAX_WIDTH) {
                    slidingTextWidth += 5; // Adjust speed of sliding
                    slidingTextPanel.setPreferredSize(new Dimension(slidingTextWidth, 50));
                    slidingTextPanel.revalidate();
                    slidingTextPanel.repaint(); // Ensure the panel is repainted
                } else {
                    slideOutTimer.stop();
                }
            }
        });

        slideInTimer = new Timer(10, new ActionListener() { // Adjusted delay
            @Override
            public void actionPerformed(ActionEvent e) {
                if (slidingTextWidth > 0) {
                    slidingTextWidth -= 5; // Adjust speed of sliding
                    slidingTextPanel.setPreferredSize(new Dimension(slidingTextWidth, 50));
                    slidingTextPanel.revalidate();
                    slidingTextPanel.repaint(); // Ensure the panel is repainted
                } else {
                    slideInTimer.stop();
                }
            }
        });

        // Enable double buffering for smoother animation
        slidingTextPanel.setDoubleBuffered(true);

        // Add mouse listeners to the logo
        logoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                slideInTimer.stop();
                slideOutTimer.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                slideOutTimer.stop();
                slideInTimer.start();
            }
        });

        frame.setVisible(true);
    }

    public void loadClothingItems() {
        itemListModel.clear();
        List<Map<String, Object>> items = ParseDatabase.getClothingItems();
        for (Map<String, Object> item : items) {
            itemListModel.addElement("<html><body style='padding: 10px;'>" + item.get("id") + " | " + item.get("name") + " | " + item.get("colour") + " | " + item.get("itemType") + " | " + item.get("size") + " | " + item.get("description") + "</body></html>");
        }
    }

    public void performSearch() {
        String searchText = searchField.getText().trim();
        List<Map<String, Object>> results = ParseDatabase.searchItems(searchText);
        updateItemList(results);
    }

    public void performFilter() {
        String selectedFilter = (String) filterDropdown.getSelectedItem();
        List<Map<String, Object>> results = ParseDatabase.getClothingItems();
        if (selectedFilter != null && !selectedFilter.equals("All")) {
            results.removeIf(item -> !selectedFilter.equalsIgnoreCase((String) item.get("itemType")));
        }
        updateItemList(results);
    }

    public void updateItemList(List<Map<String, Object>> items) {
        itemListModel.clear();
        for (Map<String, Object> item : items) {
            itemListModel.addElement("<html><body style='padding: 10px;'>" + item.get("id") + " | " + item.get("name") + " | " + item.get("colour") + " | " + item.get("itemType") + " | " + item.get("size") + " | " + item.get("description") + "</body></html>");
        }
    }

    public void addClothingItem() {
        JTextField nameField = new JTextField();
        JTextField colorField = new JTextField();
        String[] itemTypes = {"Mens Clothing", "Womens Clothing", "Jewelry", "Accessories", "Kids Clothing"};
        JComboBox<String> typeField = new JComboBox<>(itemTypes);
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
            newItem.put("itemType", typeField.getSelectedItem().toString());
            newItem.put("size", sizeField.getText());
            newItem.put("description", descriptionField.getText());
            ParseDatabase.addClothingItem(newItem);
            loadClothingItems();
        }
    }

    public void editClothingItem() {
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
        String[] itemTypes = {"Mens Clothing", "Womens Clothing", "Jewelry", "Accessories", "Kids Clothing"};
        JComboBox<String> typeField = new JComboBox<>(itemTypes);
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
            updatedItem.put("itemType", typeField.getSelectedItem().toString());
            updatedItem.put("size", sizeField.getText());
            updatedItem.put("description", descriptionField.getText());
            ParseDatabase.editClothingItem(id, updatedItem);
            loadClothingItems();
        }
    }

    public void removeClothingItem() {
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

    public void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, clothingIcon);
        if (confirm == JOptionPane.YES_OPTION) {
            frame.dispose();  // Close the current AdminCatalogueGUI
            new LogInPanel(); // Open the login screen again
        }
    }

    public static void main(String[] args) {
        // Example usage with a hardcoded username
        SwingUtilities.invokeLater(() -> new AdminCatalogueGUI("admin"));
        ParseDatabase.initializeDatabase();
        ParseDatabase.addGucciDress();
    }
}