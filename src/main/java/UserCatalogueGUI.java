import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * A graphical user interface for browsing and interacting with a fashion and clothing catalogue.
 * Provides functionality for both regular users and guests including browsing items with images,
 * searching and filtering, managing favorites (for registered users), viewing items in fullscreen,
 * and responsive design with quality-adjusted image loading.
 *
 * The interface features modern UI elements with hover effects, smooth animations, and adaptive
 * image quality based on the number of displayed items.
 *
 * @version 3.0
 * @see LogInPanel
 * @see AdminCatalogueGUI
 * @since Java 23
 */
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
    private final Font modernFont = new Font("Segoe UI", Font.PLAIN, 14);
    private String username;
    private Set<Integer> favorites = new HashSet<>();
    private RoundedButton favoritesButton;
    private static final String FAVORITES_DIR = "user_favorites/";
    private static final String IMAGE_MAP_FILE = "src/main/data/item_images.dat";
    // Hardcoded mapping of item IDs to image paths
    private static final Map<Integer, String> ITEM_IMAGES = new HashMap<>();

    /**
     * Static initializer block that loads the item image mappings from file.
     * Initializes the default image paths for known items.
     */
    static {
        ITEM_IMAGES.put(1, "src/main/images/item_1.png");
        ITEM_IMAGES.put(2, "src/main/images/item_2.png");
        ITEM_IMAGES.put(3, "src/main/images/item_3.png");
        ITEM_IMAGES.put(4, "src/main/images/item_4.png");
        ITEM_IMAGES.put(5, "src/main/images/item_5.png");
        ITEM_IMAGES.put(6, "src/main/images/item_6.png");
        ITEM_IMAGES.put(7, "src/main/images/item_7.png");
        ITEM_IMAGES.put(8, "src/main/images/item_8.png");

        // Load any additional saved images
        loadImageMap();
    }

    /**
     * Loads image mappings from persistent storage.
     * Merges any saved mappings with the default ones.
     */
    @SuppressWarnings("unchecked")
    private static void loadImageMap() {
        File file = new File(IMAGE_MAP_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(file))) {
                Map<Integer, String> loadedMap = (Map<Integer, String>) ois.readObject();
                ITEM_IMAGES.putAll(loadedMap);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Failed to load image map: " + e.getMessage());
            }
        }
    }

    /**
     * Constructs a new UserCatalogueGUI for the specified username.
     * Initializes all UI components and loads the clothing items.
     *
     * @param username the username of the logged-in user (or "Guest" for guest access)
     */
    public UserCatalogueGUI(String username) {
        this.username = username;
        frame = new JFrame("User - Fashion and Clothing Catalogue");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        // Set the frame to full screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Open in full screen mode

        // Load favorites if not guest
        if (!"Guest".equals(username)) {
            loadFavorites();
        }

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

        // --------------- FAVORITES BUTTON (Right Side) -------------------
        if (!"Guest".equals(username)) {
            favoritesButton = new RoundedButton("Favourites", new Color(255, 105, 180), Color.WHITE);
            favoritesButton.setFont(modernFont);
            favoritesButton.addActionListener(e -> showFavorites());

            GridBagConstraints gbcFavorites = new GridBagConstraints();
            gbcFavorites.gridx = 2;
            gbcFavorites.gridy = 0;
            gbcFavorites.anchor = GridBagConstraints.EAST;
            gbcFavorites.insets = new Insets(0, 10, 0, 20);
            topPanel.add(favoritesButton, gbcFavorites);
        }

        frame.add(topPanel, BorderLayout.NORTH);

        // --------------- SEARCH & FILTER PANEL (Center) -------------------
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        searchPanel.setBackground(new Color(255, 235, 205));

        searchField = new JTextField(20);
        searchField.setFont(modernFont);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

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
        scrollPane.getHorizontalScrollBar().setUnitIncrement(20);
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

        searchField.setFont(modernFont);
        searchButton.setFont(modernFont);
        filterDropdown.setFont(modernFont);

        frame.setVisible(true);
    }

    /**
     * Loads and displays clothing items in the catalogue view.
     * Automatically adjusts image quality based on the number of items displayed.
     *
     * @param items the list of clothing items to display (as maps of properties)
     */
    private void loadClothingItems(List<Map<String, Object>> items) {
        itemPanel.removeAll();

        int displayedItemCount = items.size();
        boolean enhancedQualityMode = displayedItemCount <= 2;

        for (Map<String, Object> item : items) {
            JPanel panel = new JPanel();
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224), 1));
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            int itemId = (int) item.get("id");
            String imagePath = ITEM_IMAGES.getOrDefault(itemId, "src/main/images/Logo.png");

            // Load image with different quality settings
            ImageIcon icon;
            if (enhancedQualityMode) {
                // Enhanced quality for 2 or fewer items
                icon = loadEnhancedQualityImage(imagePath, 400, 500);
            } else {
                // Normal mode with slightly larger images
                icon = loadImage(imagePath, 200, 300); // Increased from 300x400
            }

            // Image panel (same size regardless of quality mode)
            JPanel imagePanel = new JPanel(new BorderLayout());
            imagePanel.setBackground(Color.WHITE);
            imagePanel.setPreferredSize(new Dimension(250, 350)); // Consistent panel size

            JLabel imageLabel = new JLabel(icon);
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setVerticalAlignment(JLabel.CENTER);
            imagePanel.add(imageLabel, BorderLayout.CENTER);

            // Only add the favorite button if the user is NOT "Guest"
            if (!"Guest".equals(username)) {
                JButton favoriteButton = new JButton();
                favoriteButton.setOpaque(false);
                favoriteButton.setContentAreaFilled(false);
                favoriteButton.setBorderPainted(false);
                favoriteButton.setFocusPainted(false);
                favoriteButton.setIcon(new ImageIcon(createHeartIcon(favorites.contains(itemId))));
                favoriteButton.addActionListener(e -> toggleFavorite(itemId, favoriteButton));

                // Position the heart in the top-right
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                buttonPanel.setOpaque(false);
                buttonPanel.add(favoriteButton);
                imagePanel.add(buttonPanel, BorderLayout.NORTH);
            }

            // Product name (centered)
            String itemName = (String) item.get("name");
            JLabel nameLabel = new JLabel(
                    "<html><div style='text-align: center;'>" +
                            "<span style='font-size: " + (enhancedQualityMode ? "large" : "medium") +
                            "; font-weight: bold; color: #333;'>" + itemName + "</span>" +
                            "</div></html>",
                    JLabel.CENTER
            );

            // Product details (centered)
            JLabel detailsLabel = new JLabel(
                    "<html><div style='text-align: center; font-size: " +
                            (enhancedQualityMode ? "medium" : "small") + "; color: #555;'>" +
                            "<p style='margin: 2px 0;'><b>Color:</b> " + item.get("colour") + "</p>" +
                            "<p style='margin: 2px 0;'><b>Type:</b> " + item.get("itemType") + "</p>" +
                            "<p style='margin: 2px 0;'><b>Size:</b> " + item.get("size") + "</p>" +
                            "<p style='margin: 4px 0; font-style: italic; color: #777;'>" +
                            item.get("description") + "</p>" +
                            "</div></html>",
                    JLabel.CENTER
            );

            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Add components to the panel
            panel.add(imagePanel);
            panel.add(Box.createVerticalStrut(5)); // Spacing
            panel.add(nameLabel);
            panel.add(detailsLabel);

            // Mouse hover effects
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    panel.setBorder(BorderFactory.createLineBorder(new Color(60, 179, 113), 2));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    panel.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224), 1));
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    String descriptionFilePath = "src/main/descriptions/item_" + item.get("id") + "_desc.txt";
                    ProductFullscreenViewer.openProductFullscreen(item, imagePath, descriptionFilePath);
                }
            });

            itemPanel.add(panel);
        }

        itemPanel.revalidate();
        itemPanel.repaint();
    }

    /**
     * Loads an image with enhanced quality settings for detailed viewing.
     * Uses high-quality scaling algorithms and maintains aspect ratio.
     *
     * @param path the path to the image file
     * @param width the target width for scaling
     * @param height the target height for scaling
     * @return an ImageIcon with the enhanced quality image
     */
    private ImageIcon loadEnhancedQualityImage(String path, int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(path));
            if (originalImage == null) {
                originalImage = ImageIO.read(new File("src/main/images/Logo.png"));
            }

            double scaleFactor = Math.min(
                    (double) width / originalImage.getWidth(),
                    (double) height / originalImage.getHeight()
            );

            int scaledWidth = (int) (originalImage.getWidth() * scaleFactor);
            int scaledHeight = (int) (originalImage.getHeight() * scaleFactor);

            BufferedImage resizedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImage.createGraphics();

            // Enhanced quality settings
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

            g2d.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
            g2d.dispose();

            return new ImageIcon(resizedImage);
        } catch (IOException e) {
            System.err.println("Error loading enhanced quality image: " + e.getMessage());
            return new ImageIcon("src/main/images/Logo.png");
        }
    }

    /**
     * Creates a heart icon for the favorites system.
     * The icon appears filled if the item is a favorite, outline otherwise.
     *
     * @param filled whether the heart should appear filled
     * @return a BufferedImage containing the heart icon
     */
    private Image createHeartIcon(boolean filled) {
        if ("Guest".equals(username)) {
            // Return a transparent image for guests
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }

        int width = 35;
        int height = 20;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create a more natural heart shape
        if (filled) {
            g2.setColor(new Color(255, 105, 180)); // Hot pink
        } else {
            g2.setColor(new Color(200, 200, 200)); // Light gray
        }

        // Draw a properly proportioned heart
        GeneralPath heart = new GeneralPath();
        heart.moveTo(width/2, height/4);
        heart.curveTo(width/2 + width/4, -height/4, width, height/3, width/2, height);
        heart.curveTo(0, height/3, width/2 - width/4, -height/4, width/2, height/4);

        g2.fill(heart);

        // Add outline for better visibility
        g2.setStroke(new BasicStroke(1.2f));
        g2.setColor(filled ? new Color(200, 0, 100) : new Color(150, 150, 150));
        g2.draw(heart);

        g2.dispose();
        return image;
    }

    /**
     * Toggles an item's favorite status and updates the UI.
     * Handles special cases for guest users and updates persistent storage.
     *
     * @param itemId the ID of the item to toggle
     * @param favoriteButton the button associated with this item
     */
    private void toggleFavorite(int itemId, JButton favoriteButton) {
        if ("Guest".equals(username)) return;

        boolean wasFavorite = favorites.contains(itemId);

        if (wasFavorite) {
            favorites.remove(itemId);
            favoriteButton.setIcon(new ImageIcon(createHeartIcon(false)));
            saveFavorites();

            if (isViewingFavorites()) {
                // First check if this was the last favorite
                if (favorites.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "You haven't added any items to favorites yet!",
                            "Favorites",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadClothingItems(ParseDatabase.getClothingItems());
                    updateFavoritesButtonToNormal();
                } else {
                    // Only refresh if there are still favorites left
                    showFavorites(false); // Pass false to suppress duplicate dialog
                }
            }
        } else {
            favorites.add(itemId);
            favoriteButton.setIcon(new ImageIcon(createHeartIcon(true)));
            saveFavorites();
        }
    }

    /**
     * Checks if the current view is showing favorites.
     *
     * @return true if currently viewing favorites, false otherwise
     */
    private boolean isViewingFavorites() {
        return favoritesButton != null && favoritesButton.getText().equals("Go Back");
    }

    /**
     * Shows the user's favorite items (always shows empty dialog if applicable).
     */
    private void showFavorites() {
        showFavorites(true); // Default behavior shows dialog
    }

    /**
     * Shows the user's favorite items or a message if none exist.
     *
     * @param showEmptyDialog whether to show a dialog when no favorites exist
     */
    private void showFavorites(boolean showEmptyDialog) {
        List<Map<String, Object>> currentFavorites = getCurrentFavorites();

        if (currentFavorites.isEmpty()) {
            if (showEmptyDialog) {
                JOptionPane.showMessageDialog(frame,
                        "You haven't added any items to favorites yet!",
                        "Favorites",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            loadClothingItems(ParseDatabase.getClothingItems());
            updateFavoritesButtonToNormal();
        } else {
            loadClothingItems(currentFavorites);
            updateFavoritesButtonToGoBack();
        }
    }

    /**
     * Gets the current list of favorite items with complete details.
     *
     * @return a list of maps containing favorite item data
     */
    private List<Map<String, Object>> getCurrentFavorites() {
        List<Map<String, Object>> allItems = ParseDatabase.getClothingItems();
        List<Map<String, Object>> favoriteItems = new ArrayList<>();

        for (Map<String, Object> item : allItems) {
            int itemId = (int) item.get("id");
            if (favorites.contains(itemId)) {
                favoriteItems.add(item);
            }
        }
        return favoriteItems;
    }

    /**
     * Updates the favorites button to "Go Back" state.
     */
    private void updateFavoritesButtonToGoBack() {
        favoritesButton.setText("Go Back");
        // Clear existing listeners
        for (ActionListener al : favoritesButton.getActionListeners()) {
            favoritesButton.removeActionListener(al);
        }
        favoritesButton.addActionListener(e -> {
            loadClothingItems(ParseDatabase.getClothingItems());
            updateFavoritesButtonToNormal();
        });
    }

    /**
     * Updates the favorites button to normal "Favorites" state.
     */
    private void updateFavoritesButtonToNormal() {
        favoritesButton.setText("Favourites");
        // Clear existing listeners
        for (ActionListener al : favoritesButton.getActionListeners()) {
            favoritesButton.removeActionListener(al);
        }
        favoritesButton.addActionListener(e -> showFavorites());
    }

    /**
     * Loads the user's favorite items from persistent storage.
     */
    @SuppressWarnings("unchecked")
    private void loadFavorites() {
        File file = new File(FAVORITES_DIR + username + ".dat");
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            favorites = (Set<Integer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading favorites: " + e.getMessage());
            favorites = new HashSet<>();
        }
    }

    /**
     * Saves the user's favorite items to persistent storage.
     * Creates the favorites directory if it doesn't exist.
     */
    private void saveFavorites() {
        // Create directory if it doesn't exist
        File dir = new File(FAVORITES_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(FAVORITES_DIR + username + ".dat");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(favorites);
        } catch (IOException e) {
            System.out.println("Error saving favorites: " + e.getMessage());
        }
    }

    /**
     * Loads and scales an image while maintaining aspect ratio.
     * Falls back to a default image if the specified image cannot be loaded.
     *
     * @param path the path to the image file
     * @param width the target width for scaling
     * @param height the target height for scaling
     * @return a scaled ImageIcon
     */
    private ImageIcon loadImage(String path, int width, int height) {
        try {
            Image originalImage = new ImageIcon(path).getImage();

            // Calculate scaled dimensions maintaining aspect ratio
            int originalWidth = originalImage.getWidth(null);
            int originalHeight = originalImage.getHeight(null);

            double widthRatio = (double) width / originalWidth;
            double heightRatio = (double) height / originalHeight;
            double ratio = Math.min(widthRatio, heightRatio);

            int scaledWidth = (int) (originalWidth * ratio);
            int scaledHeight = (int) (originalHeight * ratio);

            // Create scaled instance with better quality
            BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
            g2d.dispose();

            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.out.println("Image not found: " + path);
            Image defaultImage = new ImageIcon("src/main/images/Logo.png")
                    .getImage()
                    .getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(defaultImage);
        }
    }

    /**
     * Updates the item panel width when the window is resized.
     */
    private void updateItemWidth() {
        itemPanel.revalidate();
        itemPanel.repaint();
    }

    /**
     * Performs a search based on the current search field text.
     * Updates the displayed items with search results.
     */
    private void performSearch() {
        String searchText = searchField.getText().trim();
        List<Map<String, Object>> results = ParseDatabase.searchItems(searchText);
        loadClothingItems(results);
    }

    /**
     * Filters items based on the current dropdown selection.
     * Updates the displayed items with filtered results.
     */
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

    /**
     * A custom JButton implementation with rounded corners and hover effects.
     */
    private static class RoundedButton extends JButton {
        private final Color bgColor;
        private final Color fgColor;
        private Color currentColor;

        /**
         * Creates a new RoundedButton with specified colors.
         *
         * @param text the button text
         * @param bgColor the background color
         * @param fgColor the foreground (text) color
         */
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

        /**
         * Custom painting for the rounded button appearance.
         *
         * @param g the Graphics context
         */
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

    /**
     * The main entry point for the UserCatalogueGUI.
     *
     * @param args command line arguments (optional username as first argument)
     */
    public static void main(String[] args) {
        loadImageMap(); // Load saved images before starting
        String username = (args.length > 0) ? args[0] : "Guest";
        SwingUtilities.invokeLater(() -> new UserCatalogueGUI(username));
    }
}
