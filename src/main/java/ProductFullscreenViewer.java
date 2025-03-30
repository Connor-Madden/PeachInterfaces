import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.Map;

public class ProductFullscreenViewer {
    public static void openProductFullscreen(Map<String, Object> item, String imagePath, String descriptionFilePath) {
        JFrame fullscreenFrame = new JFrame();
        fullscreenFrame.setUndecorated(false);
        fullscreenFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        fullscreenFrame.getContentPane().setBackground(Color.WHITE);
        fullscreenFrame.setLayout(new BorderLayout());

        // Load image
        ImageIcon fullImage = new ImageIcon(imagePath);
        if (fullImage.getIconWidth() == -1) {
            fullImage = new ImageIcon("src/main/images/Logo.png"); // Default image
        }

        // Resize the image to a fixed size (500x500)
        int fixedWidth = 500;
        int fixedHeight = 500;
        Image scaledImage = fullImage.getImage().getScaledInstance(fixedWidth, fixedHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        // Read description from file
        String description = loadDescriptionFromFile(descriptionFilePath);

        // Product details
        JLabel detailsLabel = new JLabel("<html><h1>" + item.get("name") + "</h1>"
                + "<p>Color: " + item.get("colour") + "</p>"
                + "<p>Type: " + item.get("itemType") + "</p>"
                + "<p>Size: " + item.get("size") + "</p>"
                + "<p><i>" + description + "</i></p></html>");
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        // Panel for layout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        contentPanel.setBackground(Color.WHITE);

        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(imageLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        contentPanel.add(detailsLabel, gbc);

        // Close fullscreen on click
        fullscreenFrame.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fullscreenFrame.dispose();
            }
        });

        fullscreenFrame.add(contentPanel, BorderLayout.CENTER);
        fullscreenFrame.setVisible(true);
    }

    private static String loadDescriptionFromFile(String descriptionFilePath) {
        // Read description from file
        String description = "No description available.";
        try {
            description = new String(Files.readAllBytes(Paths.get(descriptionFilePath)));
        } catch (IOException e) {
            System.err.println("Error reading description file: " + e.getMessage());
        }
        System.out.println("Description: " + description);
        return description;
    }
}
