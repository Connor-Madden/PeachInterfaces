import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AdminCatalogueGUI {
    private JFrame frame;
    private JPanel itemPanel;
    private JScrollPane scrollPane;
    private JTextField searchField;
    private JButton searchButton;
    private JComboBox<String> filterDropdown;
    private JLabel slidingTextLabel;
    private Timer slideOutTimer;
    private Timer slideInTimer;
    private int slidingTextWidth = 0;
    private final int SLIDING_TEXT_MAX_WIDTH = 150;
    private Icon clothingIcon;
    private JPanel selectedItemPanel = null;
    private Color defaultPanelBorderColor = new Color(224, 224, 224);
    private Color hoverPanelBorderColor = new Color(60, 179, 113);
    private Color selectedPanelBorderColor = new Color(0, 0, 255);
    private Map<String, Object> selectedItem = null;
    private final Font modernFont = new Font("Segoe UI", Font.PLAIN, 14);

    private static final Map<Integer, String> ITEM_IMAGES = new HashMap<>();
    static {
        ITEM_IMAGES.put(1, "src/main/images/Dress.png");
        ITEM_IMAGES.put(2, "src/main/images/Roots.png");
        ITEM_IMAGES.put(3, "src/main/images/Merrell.png");
        ITEM_IMAGES.put(4, "src/main/images/Jeans.png");
        ITEM_IMAGES.put(5, "src/main/images/Bracelet.png");
        ITEM_IMAGES.put(6, "src/main/images/Puma.png");
        ITEM_IMAGES.put(7, "src/main/images/Wallet.png");
    }

    public AdminCatalogueGUI(String username) {
        frame = new JFrame("Admin - Fashion and Clothing Catalogue");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        ImageIcon originalIcon = new ImageIcon("src/main/java/ClothingIcon.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        clothingIcon = new ImageIcon(scaledImage);

        // Top Panel
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(255, 235, 205));

        // Logo Panel
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(255, 235, 205));
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.X_AXIS));

        String logoPath = "src/main/images/Logo.png";
        ImageIcon logoIcon = new ImageIcon(new ImageIcon(logoPath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JLabel logoLabel = new JLabel(logoIcon);
        logoPanel.add(logoLabel);

        JPanel slidingTextPanel = new JPanel();
        slidingTextPanel.setBackground(new Color(255, 235, 205));
        slidingTextPanel.setLayout(new BoxLayout(slidingTextPanel, BoxLayout.X_AXIS));
        slidingTextPanel.setPreferredSize(new Dimension(0, 50));

        slidingTextLabel = new JLabel("Peach Interfaces");
        slidingTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        slidingTextLabel.setForeground(new Color(60, 179, 113));
        slidingTextPanel.add(slidingTextLabel);
        logoPanel.add(slidingTextPanel);

        GridBagConstraints gbcLogo = new GridBagConstraints();
        gbcLogo.gridx = 0;
        gbcLogo.gridy = 0;
        gbcLogo.weightx = 0;
        gbcLogo.anchor = GridBagConstraints.WEST;
        gbcLogo.insets = new Insets(0, 10, 0, 10);
        topPanel.add(logoPanel, gbcLogo);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        searchPanel.setBackground(new Color(255, 235, 205));

        searchField = new JTextField(20);
        searchField.setFont(modernFont);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        searchButton = new AdminCatalogueGUI.RoundedButton("Search", new Color(60, 179, 113), Color.WHITE);

        filterDropdown = new JComboBox<>(new String[]{"All", "Mens Clothing", "Womens Clothing", "Jewelry", "Accessories", "Kids Clothing"});
        filterDropdown.setFont(new Font("Arial", Font.BOLD, 14));
        filterDropdown.setBackground(new Color(60, 179, 113));
        filterDropdown.setForeground(Color.WHITE);

        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel("Filter: "));
        searchPanel.add(filterDropdown);

        GridBagConstraints gbcSearch = new GridBagConstraints();
        gbcSearch.gridx = 1;
        gbcSearch.gridy = 0;
        gbcSearch.weightx = 1;
        gbcSearch.fill = GridBagConstraints.HORIZONTAL;
        gbcSearch.anchor = GridBagConstraints.CENTER;
        topPanel.add(searchPanel, gbcSearch);

        frame.add(topPanel, BorderLayout.NORTH);

        // Item Grid
        itemPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        itemPanel.setBackground(Color.WHITE);
        scrollPane = new JScrollPane(itemPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(255, 235, 205));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel bottomTextLabel = new JLabel("© 2025 Peach Interfaces. Freshly Picked Clothing For You");
        bottomTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bottomTextLabel.setForeground(new Color(60, 179, 113));
        bottomTextLabel.setHorizontalAlignment(JLabel.LEFT);

        bottomTextLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bottomTextLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 16));
                bottomTextLabel.setText("<html><u>" + bottomTextLabel.getText() + "</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                bottomTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
                bottomTextLabel.setText(bottomTextLabel.getText().replaceAll("<[^>]*>", ""));
            }
        });

        bottomPanel.add(bottomTextLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(new Color(255, 235, 205));

        JButton addButton = new JButton("Add Item");
        JButton editButton = new JButton("Edit Item");
        JButton removeButton = new JButton("Remove Item");
        JButton exitButton = new JButton("Log Out");

        styleButton(addButton, new Color(70, 130, 180));
        styleButton(editButton, new Color(210, 105, 30));
        styleButton(removeButton, new Color(178, 34, 34));
        styleButton(exitButton, new Color(255, 0, 0));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(exitButton);

        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        loadClothingItems(ParseDatabase.getClothingItems());

        addButton.addActionListener(e -> addClothingItem());
        editButton.addActionListener(e -> editClothingItem());
        removeButton.addActionListener(e -> removeClothingItem());
        exitButton.addActionListener(e -> exitApplication());
        searchButton.addActionListener(e -> performSearch());
        filterDropdown.addActionListener(e -> performFilter());
        searchField.addActionListener(e -> performSearch());

        slideOutTimer = new Timer(10, e -> {
            if (slidingTextWidth < SLIDING_TEXT_MAX_WIDTH) {
                slidingTextWidth += 5;
                slidingTextPanel.setPreferredSize(new Dimension(slidingTextWidth, 50));
                slidingTextPanel.revalidate();
            } else {
                slideOutTimer.stop();
            }
        });

        slideInTimer = new Timer(10, e -> {
            if (slidingTextWidth > 0) {
                slidingTextWidth -= 5;
                slidingTextPanel.setPreferredSize(new Dimension(slidingTextWidth, 50));
                slidingTextPanel.revalidate();
            } else {
                slideInTimer.stop();
            }
        });

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

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(darkenColor(bgColor, 0.8f));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }



    private Color darkenColor(Color color, float factor) {
        return new Color(
                Math.max((int)(color.getRed() * factor), 0),
                Math.max((int)(color.getGreen() * factor), 0),
                Math.max((int)(color.getBlue() * factor), 0)
        );
    }

    private void loadClothingItems(List<Map<String, Object>> items) {
        itemPanel.removeAll();

        for (Map<String, Object> item : items) {
            JPanel panel = new JPanel();
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224), 1));
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            // Load image using item ID
            int itemId = (int) item.get("id");
            String imagePath = ITEM_IMAGES.getOrDefault(itemId, "src/main/images/Logo.png");
            ImageIcon icon = loadImage(imagePath);

            JLabel imageLabel = new JLabel(icon);
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Item name with ID integrated (small gray text after the name)
            String itemName = (String) item.get("name");
            JLabel nameLabel = new JLabel(
                    "<html><div style='text-align: center;'>" +
                            "<span style='font-size: small; font-weight: bold; color: #666; margin-right: 6px;'>#" + itemId + "</span>" +
                            "<span style='font-size: medium; font-weight: bold; color: #333;'>  " + itemName + "</span>" +
                            "</div></html>",
                    JLabel.CENTER
            );

            // Item details with simplified formatting
            JLabel detailsLabel = new JLabel(
                    "<html><div style='text-align: center; font-size: small; color: #555;'>" +
                            "<p style='margin: 2px 0;'><b>Color:</b> " + item.get("colour") + "</p>" +
                            "<p style='margin: 2px 0;'><b>Type:</b> " + item.get("itemType") + "</p>" +
                            "<p style='margin: 2px 0;'><b>Size:</b> " + item.get("size") + "</p>" +
                            "<p style='margin: 4px 0; font-style: italic; color: #777;'>" + item.get("description") + "</p>" +
                            "</div></html>",
                    JLabel.CENTER
            );

            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(imageLabel);
            panel.add(Box.createVerticalStrut(5)); // Add spacing between image and text
            panel.add(nameLabel);
            panel.add(detailsLabel);

            // --- Hover/Selection Effects ---
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selectedItemPanel != null) {
                        selectedItemPanel.setBorder(BorderFactory.createLineBorder(defaultPanelBorderColor, 1));
                    }
                    selectedItemPanel = panel;
                    selectedItem = item;
                    panel.setBorder(BorderFactory.createLineBorder(selectedPanelBorderColor, 2));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (panel != selectedItemPanel) {
                        panel.setBorder(BorderFactory.createLineBorder(hoverPanelBorderColor, 2));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (panel != selectedItemPanel) {
                        panel.setBorder(BorderFactory.createLineBorder(defaultPanelBorderColor, 1));
                    }
                }
            });

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
        }

        if (icon == null || icon.getIconWidth() == -1) {
            icon = new ImageIcon(new ImageIcon("src/main/images/Logo.png")
                    .getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH));
        }

        return icon;
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        List<Map<String, Object>> results;

        if (searchText.matches("\\d+")) {
            int id = Integer.parseInt(searchText);
            results = ParseDatabase.searchItemsById(id);
        } else {
            results = ParseDatabase.searchItems(searchText);
        }

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

    private void addClothingItem() {
        JTextField nameField = new JTextField();
        JTextField colorField = new JTextField();
        String[] itemTypes = {"Mens Clothing", "Womens Clothing", "Jewelry", "Accessories", "Kids Clothing"};
        JComboBox<String> typeField = new JComboBox<>(itemTypes);
        JTextField sizeField = new JTextField();
        JTextField descriptionField = new JTextField();

        // Add a text area for long description
        JTextArea longDescriptionArea = new JTextArea(5, 30);
        longDescriptionArea.setLineWrap(true);
        longDescriptionArea.setWrapStyleWord(true);
        JScrollPane longDescriptionScroll = new JScrollPane(longDescriptionArea);

        // Image selection components with better formatting
        JPanel imagePreviewPanel = new JPanel(new BorderLayout());
        imagePreviewPanel.setBorder(BorderFactory.createTitledBorder("Item Image"));
        imagePreviewPanel.setPreferredSize(new Dimension(250, 250));

        JLabel imagePreviewLabel = new JLabel("No image selected", JLabel.CENTER);
        imagePreviewLabel.setVerticalTextPosition(JLabel.BOTTOM);
        imagePreviewLabel.setHorizontalTextPosition(JLabel.CENTER);
        imagePreviewLabel.setForeground(Color.GRAY);
        imagePreviewLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        // Container panel to maintain aspect ratio
        JPanel imageContainer = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagePreviewLabel.getIcon() == null) {
                    g.setColor(new Color(240, 240, 240));
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(10, 10, getWidth() - 20, getHeight() - 20);
                }
            }
        };
        imageContainer.setBackground(Color.WHITE);
        imageContainer.setPreferredSize(new Dimension(200, 200));
        imageContainer.add(imagePreviewLabel);

        JButton selectImageButton = new JButton("Select Image");
        selectImageButton.setPreferredSize(new Dimension(120, 25));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.add(selectImageButton);

        imagePreviewPanel.add(imageContainer, BorderLayout.CENTER);
        imagePreviewPanel.add(buttonPanel, BorderLayout.SOUTH);

        selectImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Item Image");
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    String name = f.getName().toLowerCase();
                    return f.isDirectory() || name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg");
                }

                public String getDescription() {
                    return "Image Files (*.png, *.jpg, *.jpeg)";
                }
            });

            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
                    Image image = icon.getImage();

                    // Calculate scaled dimensions while maintaining aspect ratio
                    int containerWidth = imageContainer.getWidth() - 20;
                    int containerHeight = imageContainer.getHeight() - 20;

                    double widthRatio = (double) containerWidth / image.getWidth(null);
                    double heightRatio = (double) containerHeight / image.getHeight(null);
                    double ratio = Math.min(widthRatio, heightRatio);

                    int scaledWidth = (int) (image.getWidth(null) * ratio);
                    int scaledHeight = (int) (image.getHeight(null) * ratio);

                    Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                    imagePreviewLabel.setIcon(new ImageIcon(scaledImage));
                    imagePreviewLabel.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error loading image: " + ex.getMessage(),
                            "Image Error", JOptionPane.ERROR_MESSAGE);
                    imagePreviewLabel.setIcon(null);
                    imagePreviewLabel.setText("Invalid image");
                }
            }
        });



        // Main panel layout
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Form panel with better spacing
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Item Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Color:"), gbc);
        gbc.gridx = 1;
        formPanel.add(colorField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(typeField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Size:"), gbc);
        gbc.gridx = 1;
        formPanel.add(sizeField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(descriptionField, gbc);

        // Add to form panel
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Long Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(longDescriptionScroll, gbc);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(imagePreviewPanel, BorderLayout.EAST);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Item",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, clothingIcon);

        if (result == JOptionPane.OK_OPTION) {
            // Create a new item map with all the entered data
            Map<String, String> newItem = new HashMap<>();
            newItem.put("name", nameField.getText());
            newItem.put("colour", colorField.getText());
            newItem.put("itemType", (String) typeField.getSelectedItem());
            newItem.put("size", sizeField.getText());
            newItem.put("description", descriptionField.getText());

            // Handle the image
            int newId = ITEM_IMAGES.keySet().stream().max(Integer::compare).orElse(0) + 1;
            if (imagePreviewLabel.getIcon() != null) {
                try {
                    String destPath = "src/main/images/item_" + newId + ".png";
                    ImageIcon icon = (ImageIcon) imagePreviewLabel.getIcon();
                    BufferedImage bufferedImage = new BufferedImage(
                            icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = bufferedImage.createGraphics();
                    icon.paintIcon(null, g2, 0, 0);
                    g2.dispose();
                    ImageIO.write(bufferedImage, "png", new File(destPath));
                    ITEM_IMAGES.put(newId, destPath);
                    newItem.put("imagePath", destPath);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving image: " + ex.getMessage(),
                            "Image Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                newItem.put("imagePath", "src/main/images/Logo.png");
            }

            // Save long description to file
            try {
                String descriptionFileName = "src/main/descriptions/item_" + newId + "_desc.txt";
                Files.createDirectories(Paths.get("src/main/descriptions"));
                Files.write(Paths.get(descriptionFileName),
                        longDescriptionArea.getText().getBytes());
                newItem.put("descriptionFile", descriptionFileName);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving description file: " + ex.getMessage(),
                        "File Error", JOptionPane.ERROR_MESSAGE);
            }

            newItem.put("id", String.valueOf(newId));

            // SINGLE database call to add the item
            ParseDatabase.addClothingItem(newItem);
            loadClothingItems(ParseDatabase.getClothingItems());
        }
    }

    private void editClothingItem() {
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(frame, "Please select an item to edit first.",
                    "Edit Item", JOptionPane.WARNING_MESSAGE, clothingIcon);
            return;
        }

        // Create input fields with current values
        JTextField nameField = new JTextField((String) selectedItem.get("name"));
        JTextField colorField = new JTextField((String) selectedItem.get("colour"));
        String[] itemTypes = {"Mens Clothing", "Womens Clothing", "Jewelry", "Accessories", "Kids Clothing"};
        JComboBox<String> typeField = new JComboBox<>(itemTypes);
        typeField.setSelectedItem(selectedItem.get("itemType"));
        JTextField sizeField = new JTextField((String) selectedItem.get("size"));
        JTextField descriptionField = new JTextField((String) selectedItem.get("description"));

        // Long description field
        JTextArea longDescriptionArea = new JTextArea(5, 30);
        longDescriptionArea.setLineWrap(true);
        longDescriptionArea.setWrapStyleWord(true);
        JScrollPane longDescriptionScroll = new JScrollPane(longDescriptionArea);

        // Load existing long description from file
        int itemId = (int) selectedItem.get("id");
        String descFilePath = "src/main/descriptions/item_" + itemId + "_desc.txt";
        try {
            if (Files.exists(Paths.get(descFilePath))) {
                String content = new String(Files.readAllBytes(Paths.get(descFilePath)));
                longDescriptionArea.setText(content);
            }
        } catch (IOException ex) {
            System.out.println("Error loading description file: " + ex.getMessage());
        }

        // Image selection components with better formatting
        JPanel imagePreviewPanel = new JPanel(new BorderLayout());
        imagePreviewPanel.setBorder(BorderFactory.createTitledBorder("Item Image"));
        imagePreviewPanel.setPreferredSize(new Dimension(250, 250));

        // Load current image or show default
        JLabel imagePreviewLabel = new JLabel("", JLabel.CENTER);
        imagePreviewLabel.setVerticalTextPosition(JLabel.BOTTOM);
        imagePreviewLabel.setHorizontalTextPosition(JLabel.CENTER);
        imagePreviewLabel.setForeground(Color.GRAY);
        imagePreviewLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        try {
            String imagePath = ITEM_IMAGES.getOrDefault(itemId, "src/main/images/Logo.png");
            ImageIcon icon = new ImageIcon(imagePath);
            if (icon.getImage() != null) {
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                imagePreviewLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                imagePreviewLabel.setText("No image selected");
            }
        } catch (Exception e) {
            imagePreviewLabel.setText("No image selected");
        }

        // Container panel to maintain aspect ratio
        JPanel imageContainer = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagePreviewLabel.getIcon() == null) {
                    g.setColor(new Color(240, 240, 240));
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(10, 10, getWidth() - 20, getHeight() - 20);
                }
            }
        };
        imageContainer.setBackground(Color.WHITE);
        imageContainer.setPreferredSize(new Dimension(200, 200));
        imageContainer.add(imagePreviewLabel);

        JButton selectImageButton = new JButton("Change Image");
        selectImageButton.setPreferredSize(new Dimension(120, 25));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.add(selectImageButton);

        imagePreviewPanel.add(imageContainer, BorderLayout.CENTER);
        imagePreviewPanel.add(buttonPanel, BorderLayout.SOUTH);

        selectImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Item Image");
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    String name = f.getName().toLowerCase();
                    return f.isDirectory() || name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg");
                }

                public String getDescription() {
                    return "Image Files (*.png, *.jpg, *.jpeg)";
                }
            });

            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
                    Image image = icon.getImage();

                    // Calculate scaled dimensions while maintaining aspect ratio
                    int containerWidth = imageContainer.getWidth() - 20;
                    int containerHeight = imageContainer.getHeight() - 20;

                    double widthRatio = (double) containerWidth / image.getWidth(null);
                    double heightRatio = (double) containerHeight / image.getHeight(null);
                    double ratio = Math.min(widthRatio, heightRatio);

                    int scaledWidth = (int) (image.getWidth(null) * ratio);
                    int scaledHeight = (int) (image.getHeight(null) * ratio);

                    Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                    imagePreviewLabel.setIcon(new ImageIcon(scaledImage));
                    imagePreviewLabel.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error loading image: " + ex.getMessage(),
                            "Image Error", JOptionPane.ERROR_MESSAGE);
                    imagePreviewLabel.setIcon(null);
                    imagePreviewLabel.setText("Invalid image");
                }
            }
        });

        // Main panel layout
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Form panel with better spacing
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Item Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Color:"), gbc);
        gbc.gridx = 1;
        formPanel.add(colorField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(typeField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Size:"), gbc);
        gbc.gridx = 1;
        formPanel.add(sizeField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(descriptionField, gbc);

        // Add the new long description field
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Long Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(longDescriptionScroll, gbc);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(imagePreviewPanel, BorderLayout.EAST);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Edit Item",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, clothingIcon);

        if (result == JOptionPane.OK_OPTION) {
            // Update the item with new values
            Map<String, String> updatedItem = new HashMap<>();
            updatedItem.put("id", selectedItem.get("id").toString());
            updatedItem.put("name", nameField.getText());
            updatedItem.put("colour", colorField.getText());
            updatedItem.put("itemType", typeField.getSelectedItem().toString());
            updatedItem.put("size", sizeField.getText());
            updatedItem.put("description", descriptionField.getText());

            // Save the long description to file
            try {
                Files.createDirectories(Paths.get("src/main/descriptions"));
                Files.write(Paths.get(descFilePath), longDescriptionArea.getText().getBytes());
                updatedItem.put("descriptionFile", descFilePath);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving description file: " + ex.getMessage(),
                        "File Error", JOptionPane.ERROR_MESSAGE);
            }

            // Handle image update if changed
            if (imagePreviewLabel.getIcon() != null) {
                try {
                    String destPath = "src/main/images/item_" + itemId + ".png";
                    ImageIcon icon = (ImageIcon) imagePreviewLabel.getIcon();
                    BufferedImage bufferedImage = new BufferedImage(
                            icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = bufferedImage.createGraphics();
                    icon.paintIcon(null, g2, 0, 0);
                    g2.dispose();
                    ImageIO.write(bufferedImage, "png", new File(destPath));
                    ITEM_IMAGES.put(itemId, destPath);
                    updatedItem.put("imagePath", destPath);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving image: " + ex.getMessage(),
                            "Image Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Update the item in the database
            ParseDatabase.editClothingItem(itemId, updatedItem);
            loadClothingItems(ParseDatabase.getClothingItems());
        }
    }

    private void removeClothingItem() {
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(frame, "Please select an item to remove first.", "Remove Item", JOptionPane.WARNING_MESSAGE, clothingIcon);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to remove this item?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                clothingIcon);

        if (confirm == JOptionPane.YES_OPTION) {
            ParseDatabase.removeClothingItem((int) selectedItem.get("id"));
            loadClothingItems(ParseDatabase.getClothingItems());
        }
    }

    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, clothingIcon);
        if (confirm == JOptionPane.YES_OPTION) {
            frame.dispose();
            new LogInPanel();
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
        SwingUtilities.invokeLater(() -> new AdminCatalogueGUI("admin"));
        ParseDatabase.initializeDatabase();
        ParseDatabase.addGucciDress();
    }
}