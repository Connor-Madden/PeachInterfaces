import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.Map;


// User Catalog Class
public class UserCatalogueGUI {
    private JFrame frame;
    private JPanel itemPanel;
    private JScrollPane scrollPane;
    private JTextField searchField;
    private RoundedButton searchButton;
    private JComboBox<String> filterDropdown;

    public UserCatalogueGUI() {
        frame = new JFrame("Fashion and Clothing Catalogue");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        // --------------- TOP PANEL -------------------
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 235, 205)); // Light Orange

        // --------------- LOGO PANEL (Left Side) -------------------
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(new Color(255, 235, 205));

        String logoPath = "src/main/images/Logo.png";
        ImageIcon logoIcon = new ImageIcon(new ImageIcon(logoPath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JLabel logoLabel = new JLabel(logoIcon);
        logoPanel.add(logoLabel);

        // --------------- SEARCH & FILTER PANEL (Center) -------------------
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        searchPanel.setBackground(new Color(255, 235, 205));

        searchField = new JTextField(25);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        searchButton = new RoundedButton("Search", new Color(60, 179, 113), Color.WHITE);
        filterDropdown = new JComboBox<>(new String[]{"All", "Indoor Wear", "Outdoor Wear", "Jewelry", "Accessories", "Kids Clothing"});
        filterDropdown.setFont(new Font("Arial", Font.BOLD, 14));
        filterDropdown.setBackground(new Color(60, 179, 113));
        filterDropdown.setForeground(Color.WHITE);

        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel("Filter: "));
        searchPanel.add(filterDropdown);

        topPanel.add(logoPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);

        // --------------- ITEM GRID -------------------
        itemPanel = new JPanel(new GridLayout(0, 2, 5, 5)); // Reduced spacing between items
        itemPanel.setBackground(Color.WHITE);
        scrollPane = new JScrollPane(itemPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Increase scroll speed
        scrollPane.getVerticalScrollBar().setUnitIncrement(20); // Adjust scrolling speed
        scrollPane.getVerticalScrollBar().setBlockIncrement(100); // Optional: Click-to-scroll speed

        frame.add(scrollPane, BorderLayout.CENTER);


        // Load initial items
        loadClothingItems(ParseDatabase.getClothingItems());

        // Resize handling
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateItemWidth();
            }
        });

        searchButton.addActionListener(e -> performSearch());
        filterDropdown.addActionListener(e -> performFilter());
        searchField.addActionListener(e -> performSearch());

        frame.setVisible(true);
    }

    private void loadClothingItems(List<Map<String, Object>> items) {
        itemPanel.removeAll();

        for (Map<String, Object> item : items) {
            JPanel panel = new JPanel();
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224), 1));
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            // Load image
            String itemName = ((String) item.get("name")).replaceAll("\\s+", "").toLowerCase();
            String imagePath = "src/main/resources/images/" + itemName + ".png";
            ImageIcon icon = loadImage(imagePath);

            JLabel imageLabel = new JLabel(icon);
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel nameLabel = new JLabel("<html><b>" + item.get("name") + "</b></html>", JLabel.CENTER);
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            JLabel detailsLabel = new JLabel("<html>Color: " + item.get("colour") + "<br>"
                    + "Type: " + item.get("itemType") + "<br>"
                    + "Size: " + item.get("size") + "<br>"
                    + "<i>" + item.get("description") + "</i></html>", JLabel.CENTER);
            detailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));

            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(imageLabel);
            panel.add(Box.createVerticalStrut(5));
            panel.add(nameLabel);
            panel.add(detailsLabel);

            // Add click listener to open fullscreen
            panel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    ProductFullscreenViewer.openProductFullscreen(item, imagePath);
                }
            });

            itemPanel.add(panel);
        }

        itemPanel.revalidate();
        itemPanel.repaint();
    }


    private void openProductFullscreen(Map<String, Object> item, String imagePath) {
        JFrame fullscreenFrame = new JFrame();
        fullscreenFrame.setUndecorated(true);
        fullscreenFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        fullscreenFrame.getContentPane().setBackground(Color.WHITE);
        fullscreenFrame.setLayout(new BorderLayout());

        // Load large image
        ImageIcon fullImage = new ImageIcon(imagePath);
        if (fullImage.getIconWidth() == -1) {
            fullImage = new ImageIcon("src/main/images/Logo.png"); // Default image
        }

        JLabel imageLabel = new JLabel(fullImage);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        // Product details
        JLabel detailsLabel = new JLabel("<html><h1>" + item.get("name") + "</h1>"
                + "<p>Color: " + item.get("colour") + "</p>"
                + "<p>Type: " + item.get("itemType") + "</p>"
                + "<p>Size: " + item.get("size") + "</p>"
                + "<p><i>" + item.get("description") + "</i></p></html>", JLabel.CENTER);
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        // Panel to center image
        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.add(imageLabel);

        // Close fullscreen on click or ESC key
        fullscreenFrame.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fullscreenFrame.dispose();
            }
        });

        fullscreenFrame.add(imagePanel, BorderLayout.CENTER);
        fullscreenFrame.add(detailsLabel, BorderLayout.SOUTH);

        fullscreenFrame.setVisible(true);
    }



    private ImageIcon loadImage(String path) {
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            System.out.println("Image not found: " + path);
        }

        if (icon == null || icon.getIconWidth() == -1) {
            icon = new ImageIcon(new ImageIcon("src/main/images/Logo.png")
                    .getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH));
        }

        return icon;
    }

    private void updateItemWidth() {
        itemPanel.revalidate();
        itemPanel.repaint();
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

    private static class RoundedButton extends JButton {
        private final Color bgColor;
        private final Color fgColor;
        private Color currentColor;

        public RoundedButton(String text, Color bgColor, Color fgColor) {
            super(text);
            this.bgColor = bgColor;
            this.fgColor = fgColor;
            this.currentColor = bgColor;

            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setFont(new Font("Arial", Font.BOLD, 14));
            setForeground(fgColor);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    currentColor = bgColor.darker();
                    repaint();
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    currentColor = bgColor;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(currentColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 2;
            g2.setColor(fgColor);
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserCatalogueGUI::new);
    }
}
