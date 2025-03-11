import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class UserCatalogueGUI {
    private JFrame frame;
    private DefaultListModel<String> itemListModel;
    private JList<String> itemList;
    private JTextField searchField;
    private JButton searchButton;
    private JComboBox<String> filterDropdown;
    private Icon clothingIcon;

    public UserCatalogueGUI() {
        frame = new JFrame("Fashion and Clothing Catalogue");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Load and resize clothing icon
        ImageIcon originalIcon = new ImageIcon("src/main/java/ClothingIcon.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        clothingIcon = new ImageIcon(scaledImage);

        // Set background color
        frame.getContentPane().setBackground(Color.WHITE);

        // Search and Filter Panel
        JPanel topPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        filterDropdown = new JComboBox<>(new String[]{"All", "Indoor Wear", "Outdoor Wear", "Jewelry", "Accessories", "Kids Clothing"});

        topPanel.add(new JLabel("Search: "));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(new JLabel("Filter: "));
        topPanel.add(filterDropdown);

        frame.add(topPanel, BorderLayout.NORTH);

        // Item List
        itemListModel = new DefaultListModel<>();
        itemList = new JList<>(itemListModel);
        itemList.setFont(new Font("Arial", Font.PLAIN, 14));
        itemList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(itemList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Load items into the list
        loadClothingItems(ParseDatabase.getClothingItems());

        // Add action listeners for search and filter
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        filterDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performFilter();
            }
        });

        // Add KeyListener to searchField for Enter key press
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == evt.VK_ENTER) {
                    performSearch(); // Trigger search when Enter is pressed
                }
            }
        });

        frame.setVisible(true);
    }

    private void loadClothingItems(List<Map<String, Object>> items) {
        itemListModel.clear();
        for (Map<String, Object> item : items) {
            itemListModel.addElement("<html><body style='padding: 10px;'>" +
                    item.get("name") + " | " + item.get("colour") + " | " +
                    item.get("itemType") + " | " + item.get("size") + " | " +
                    item.get("description") + "</body></html>");
        }
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        List<Map<String, Object>> results = ParseDatabase.searchItems(searchText);
        loadClothingItems(results);
    }

    private void performFilter() {
        String selectedFilter = (String) filterDropdown.getSelectedItem();
        if (selectedFilter != null && !selectedFilter.equals("All")) {
            // Ensure case-insensitive and consistent filtering
            List<Map<String, Object>> results = ParseDatabase.getClothingItems();
            results.removeIf(item -> !selectedFilter.equalsIgnoreCase((String) item.get("itemType")));
            loadClothingItems(results);
        } else {
            loadClothingItems(ParseDatabase.getClothingItems());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserCatalogueGUI::new);
    }
}