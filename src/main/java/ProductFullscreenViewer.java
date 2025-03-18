import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ProductFullscreenViewer {
    public static void openProductFullscreen(Map<String, Object> item, String imagePath) {
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
                + "<p><i>" + item.get("description") + "</i></p></html>");
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
}
