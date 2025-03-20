import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


// User Catalog Class
public class UserCatalogueGUI {
    private JFrame frame;
    private JPanel itemPanel;
    private JScrollPane scrollPane;
    private JTextField searchField;
    private RoundedButton searchButton;
    private JComboBox<String> filterDropdown;
    private JLabel slidingTextLabel;
    private Timer slideOutTimer;
    private Timer slideInTimer;
    private int slidingTextWidth = 0;
    private final int SLIDING_TEXT_MAX_WIDTH = 150; // Maximum width of the sliding text
    private Icon clothingIcon;

    // Hardcoded mapping of item IDs to image paths


    public UserCatalogueGUI(String username) {
        frame = new JFrame("User - Fashion and Clothing Catalogue");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        // Set the frame to full screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Open in full screen mode

        // Load and resize clothing icon
        ImageIcon originalIcon = new ImageIcon("src/main/java/ClothingIcon.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        clothingIcon = new ImageIcon(scaledImage);

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

        searchButton = new RoundedButton("Search", new Color(60, 179, 113), Color.WHITE);
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

        // --------------- BOTTOM PANEL -------------------
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(255, 235, 205)); // Light Orange
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

// Text label (centered)
        JLabel bottomTextLabel = new JLabel("© 2025 Peach Interfaces. Freshly Picked Clothing For You");
        bottomTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bottomTextLabel.setForeground(new Color(60, 179, 113));
        bottomTextLabel.setHorizontalAlignment(JLabel.CENTER);

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

        bottomPanel.add(bottomTextLabel, BorderLayout.CENTER); // Add to CENTER region

// Log Out button
        JButton logOutButton = new JButton("Log Out");
        logOutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logOutButton.setBackground(new Color(255, 0, 0)); // Red-Orange
        logOutButton.setForeground(Color.WHITE);
        logOutButton.setFocusPainted(false);
        logOutButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Add padding

// Hover effect for Log Out button
        logOutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logOutButton.setBackground(new Color(200, 0, 0)); // Darker red on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logOutButton.setBackground(new Color(255, 0, 0)); // Original color on exit
            }
        });

// Log Out button with confirmation dialog
        logOutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to log out?",
                    "Log Out",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    clothingIcon
            );

            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose(); // Close the current window
                new LogInPanel(); // Open the login panel
            }
        });

// Add the Log Out button to the bottom panel (EAST region)
        bottomPanel.add(logOutButton, BorderLayout.EAST);

// Add the bottom panel to the frame
        frame.add(bottomPanel, BorderLayout.SOUTH);

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

        // Initialize timers for sliding animation
        slideOutTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (slidingTextWidth < SLIDING_TEXT_MAX_WIDTH) {
                    slidingTextWidth += 5; // Adjust speed of sliding
                    slidingTextPanel.setPreferredSize(new Dimension(slidingTextWidth, 50));
                    slidingTextPanel.revalidate();
                } else {
                    slideOutTimer.stop();
                }
            }
        });

        slideInTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (slidingTextWidth > 0) {
                    slidingTextWidth -= 5; // Adjust speed of sliding
                    slidingTextPanel.setPreferredSize(new Dimension(slidingTextWidth, 50));
                    slidingTextPanel.revalidate();
                } else {
                    slideInTimer.stop();
                }
            }
        });

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

    private void loadClothingItems(List<Map<String, Object>> items) {
        itemPanel.removeAll();

        for (Map<String, Object> item : items) {
            JPanel panel = new JPanel();
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224), 1));
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            // Load image from database
            String imagePath = (String) item.get("imageUrl");
            ImageIcon icon = loadImage(imagePath);

            JLabel imageLabel = new JLabel(icon);
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Item name and details
            String itemName = (String) item.get("name");
            JLabel nameLabel = new JLabel(
                    "<html><div style='text-align: center; font-size: medium; font-weight: bold; color: #333;'>" +
                            itemName +
                            "</div></html>",
                    JLabel.CENTER
            );

            JLabel detailsLabel = new JLabel(
                    "<html><div style='text-align: center; font-size: small; color: #555;'>" +
                            "<p style='margin: 2px 0;'><b>Color:</b> " + item.get("colour") + "</p>" +
                            "<p style='margin: 2px 0;'><b>Type:</b> " + item.get("itemType") + "</p>" +
                            "<p style='margin: 2px 0;'><b>Size:</b> " + item.get("size") + "</p>" +
                            "<p style='margin: 4px 0; font-style: italic; color: #777;'>" + item.get("description") + "</p>" +
                            "</div></html>",
                    JLabel.CENTER
            );

            panel.add(imageLabel);
            panel.add(Box.createVerticalStrut(5));
            panel.add(nameLabel);
            panel.add(detailsLabel);

            itemPanel.add(panel);
        }

        itemPanel.revalidate();
        itemPanel.repaint();
    }

    private ImageIcon loadImage(String path) {
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            System.out.println("Image not found: " + path);
            icon = new ImageIcon(new ImageIcon("src/main/images/Logo.png")
                    .getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH));
        }
        return icon;
    }



    private void openProductFullscreen(Map<String, Object> item, String imagePath) {
        JFrame fullscreenFrame = new JFrame();
        fullscreenFrame.setUndecorated(true);
        fullscreenFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        fullscreenFrame.getContentPane().setBackground(Color.WHITE);
        fullscreenFrame.setLayout(new BorderLayout());

        // Correctly resize the image maintaining the aspect ratio
        ImageIcon fullImage = loadImage(imagePath); // Use the specific image path
        if (fullImage.getIconWidth() == -1) {
            fullImage = new ImageIcon("src/main/images/Logo.png"); // Default image
        }

        Image image = fullImage.getImage();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();
        double imageWidth = image.getWidth(null);
        double imageHeight = image.getHeight(null);

// Calculate scaling factor to maintain aspect ratio
        double scaleFactor = Math.min(screenWidth / imageWidth, screenHeight / imageHeight);
        int scaledWidth = (int) (imageWidth * scaleFactor);
        int scaledHeight = (int) (imageHeight * scaleFactor);

// Resize the image to fit the screen while maintaining aspect ratio
        Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel imageLabel = new JLabel(scaledIcon);
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

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(255, 0, 0)); // Red-Orange
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Add padding

        // Close button action
        closeButton.addActionListener(e -> fullscreenFrame.dispose());

        // Panel for the close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeButton);

        // Add components to the frame
        fullscreenFrame.add(imagePanel, BorderLayout.CENTER);
        fullscreenFrame.add(detailsLabel, BorderLayout.SOUTH);
        fullscreenFrame.add(buttonPanel, BorderLayout.NORTH);

        fullscreenFrame.setVisible(true);
        fullscreenFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the frame
        fullscreenFrame.setUndecorated(true); // Remove window decorations
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
        String username = (args.length > 0) ? args[0] : "Guest";
        SwingUtilities.invokeLater(() -> new UserCatalogueGUI(username));
    }
}
