import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.Map;

public class UserCatalogueGUI {
    private JFrame frame;
    private DefaultListModel<JPanel> itemListModel;
    private JList<JPanel> itemList;
    private JTextField searchField;
    private JButton searchButton;
    private JComboBox<String> filterDropdown;

    public UserCatalogueGUI() {
        frame = new JFrame("Fashion and Clothing Catalogue");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        // Top Panel with Search and Filter
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        topPanel.setBackground(new Color(255, 235, 205)); // Light Orange

        searchField = new JTextField(25);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        searchButton = createStyledButton("Search", new Color(60, 179, 113), new Color(255, 255, 255)); // Green button
        filterDropdown = new JComboBox<>(new String[]{"All", "Indoor Wear", "Outdoor Wear", "Jewelry", "Accessories", "Kids Clothing"});
        filterDropdown.setFont(new Font("Arial", Font.BOLD, 14));
        filterDropdown.setBackground(new Color(60, 179, 113)); // Orange background
        filterDropdown.setForeground(Color.WHITE); // White text

        topPanel.add(new JLabel("Search: "));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(new JLabel("Filter: "));
        topPanel.add(filterDropdown);

        frame.add(topPanel, BorderLayout.NORTH);

        // Item List
        itemListModel = new DefaultListModel<>();
        itemList = new JList<>(itemListModel);
        itemList.setCellRenderer(new PanelListRenderer());
        itemList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        JScrollPane scrollPane = new JScrollPane(itemList);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Load initial clothing items
        loadClothingItems(ParseDatabase.getClothingItems());

        // Add ComponentListener to adjust item width dynamically
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateItemWidth();
            }
        });

        // Action Listeners
        searchButton.addActionListener(e -> performSearch());
        filterDropdown.addActionListener(e -> performFilter());
        searchField.addActionListener(e -> performSearch());

        frame.setVisible(true);
    }

    private void loadClothingItems(List<Map<String, Object>> items) {
        itemListModel.clear();
        int index = 1;

        for (Map<String, Object> item : items) {
            JPanel panel = new JPanel();
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224), 1));

            // Layout change: Align items to the left and span the full width
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

            // Load image for item
            String imagePath = "src/main/resources/images/image" + index + ".png";
            ImageIcon icon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            JLabel imageLabel = new JLabel(icon);

            // Item details
            String details = "<html><b>" + item.get("name") + "</b><br>"
                    + "Color: " + item.get("colour") + "<br>"
                    + "Type: " + item.get("itemType") + "<br>"
                    + "Size: " + item.get("size") + "<br>"
                    + "<i>" + item.get("description") + "</i></html>";
            JLabel detailsLabel = new JLabel(details);
            detailsLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Adding components to the panel
            panel.add(imageLabel);
            panel.add(Box.createHorizontalStrut(10)); // Space between image and details
            panel.add(detailsLabel);

            itemListModel.addElement(panel);
            index++;
        }

        // Update the width of the items after loading
        updateItemWidth();
    }

    private void updateItemWidth() {
        // Update the item width dynamically based on the window size
        int frameWidth = frame.getWidth();
        itemList.setFixedCellWidth(frameWidth - 20); // Make items span the full width of the frame
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        List<Map<String, Object>> results = ParseDatabase.searchItems(searchText);
        loadClothingItems(results);
    }

    private void performFilter() {
        String selectedFilter = (String) filterDropdown.getSelectedItem();
        if (selectedFilter != null && !selectedFilter.equals("All")) {
            List<Map<String, Object>> results = ParseDatabase.getClothingItems();
            results.removeIf(item -> !selectedFilter.equalsIgnoreCase((String) item.get("itemType")));
            loadClothingItems(results);
        } else {
            loadClothingItems(ParseDatabase.getClothingItems());
        }
    }

    private static class PanelListRenderer implements ListCellRenderer<JPanel> {
        @Override
        public Component getListCellRendererComponent(JList<? extends JPanel> list, JPanel value, int index, boolean isSelected, boolean cellHasFocus) {
            value.setBackground(isSelected ? new Color(230, 230, 230) : Color.WHITE);
            return value;
        }
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserCatalogueGUI::new);
    }
}
