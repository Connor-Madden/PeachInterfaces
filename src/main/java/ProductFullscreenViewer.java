import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Map;

public class ProductFullscreenViewer {
    public static void openProductFullscreen(Map<String, Object> item, String imagePath, String descriptionFilePath) {
        JFrame fullscreenFrame = new JFrame();
        fullscreenFrame.setUndecorated(true);
        fullscreenFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        fullscreenFrame.getContentPane().setBackground(Color.WHITE);
        fullscreenFrame.setLayout(new BorderLayout());

        // Load Image
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            originalImage = null;
        }

        if (originalImage == null) {
            try {
                originalImage = ImageIO.read(new File("src/main/images/Logo.png")); // Fallback image
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // Scale Image (Preserving Aspect Ratio)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxWidth = (int) (screenSize.getWidth() * 0.8);
        int maxHeight = (int) (screenSize.getHeight() * 0.8);

        double scaleFactor = Math.min(
                (double) maxWidth / originalImage.getWidth(),
                (double) maxHeight / originalImage.getHeight()
        );

        int scaledWidth = (int) (originalImage.getWidth() * scaleFactor);
        int scaledHeight = (int) (originalImage.getHeight() * scaleFactor);

        Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        BufferedImage scaledBufferedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledBufferedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        // No sharpening applied, keeping it softer
        JLabel imageLabel = new JLabel(new ImageIcon(scaledBufferedImage));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        // Create Tabbed Pane with Hover Effects
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 16));

        // Custom UI for Hover Effects
        tabbedPane.setUI(new BasicTabbedPaneUI() {
            private int hoverIndex = -1;

            @Override
            protected void installListeners() {
                super.installListeners();
                tabbedPane.addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        int newHoverIndex = tabForCoordinate(tabbedPane, e.getX(), e.getY());
                        if (newHoverIndex != hoverIndex) {
                            hoverIndex = newHoverIndex;
                            tabbedPane.repaint();
                        }
                    }
                });

                tabbedPane.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hoverIndex = -1;
                        tabbedPane.repaint();
                    }
                });
            }

            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement,
                                              int tabIndex, int x, int y,
                                              int w, int h, boolean isSelected) {
                if (isSelected) {
                    g.setColor(new Color(220, 230, 255)); // Selected Tab Color
                } else if (tabIndex == hoverIndex) {
                    g.setColor(new Color(210, 225, 255)); // Hover Tab Color
                } else {
                    g.setColor(new Color(245, 245, 245)); // Normal Tab Color
                }
                g.fillRect(x, y, w, h);
            }

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement,
                                          int tabIndex, int x, int y,
                                          int w, int h, boolean isSelected) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(x, y, w, h);
            }
        });

        // Basic Details Tab
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        StringBuilder detailsHtml = new StringBuilder("<html><div style='text-align: center;'>");
        detailsHtml.append("<h1 style='color: #333; margin-bottom: 20px;'>").append(item.get("name")).append("</h1>");
        detailsHtml.append("<p style='font-size: large; margin: 8px 0;'><b>Color:</b> ").append(item.get("colour")).append("</p>");
        detailsHtml.append("<p style='font-size: large; margin: 8px 0;'><b>Type:</b> ").append(item.get("itemType")).append("</p>");
        detailsHtml.append("<p style='font-size: large; margin: 8px 0;'><b>Size:</b> ").append(item.get("size")).append("</p>");
        detailsHtml.append("<p style='font-size: large; margin: 20px 0; font-style: italic; color: #555;'>")
                .append(item.get("description")).append("</p>");
        detailsHtml.append("</div></html>");

        JLabel detailsLabel = new JLabel(detailsHtml.toString(), JLabel.CENTER);
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        detailsPanel.add(detailsLabel, BorderLayout.CENTER);
        tabbedPane.addTab("Product Details", detailsPanel);

        // Long Description Tab
        JPanel longDescPanel = new JPanel(new BorderLayout());
        JTextArea longDescArea = new JTextArea();
        longDescArea.setEditable(false);
        longDescArea.setLineWrap(true);
        longDescArea.setWrapStyleWord(true);
        longDescArea.setFont(new Font("Arial", Font.PLAIN, 18));
        longDescArea.setMargin(new Insets(20, 20, 20, 20));

        try {
            String longDesc = new String(Files.readAllBytes(Paths.get(descriptionFilePath)));
            longDescArea.setText(longDesc);
        } catch (IOException e) {
            longDescArea.setText("No detailed description available for this product.");
        }

        JScrollPane scrollPane = new JScrollPane(longDescArea);
        longDescPanel.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Full Description", longDescPanel);

        // Close button (existing code remains the same)
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setBackground(new Color(220, 50, 50));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(new Color(180, 30, 30));
                closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(new Color(220, 50, 50));
                closeButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        closeButton.addActionListener(e -> fullscreenFrame.dispose());

        closeButton.getModel().addChangeListener(e -> {
            ButtonModel model = (ButtonModel) e.getSource();
            if (model.isPressed()) {
                closeButton.setBackground(new Color(150, 20, 20));
            } else if (model.isRollover()) {
                closeButton.setBackground(new Color(180, 30, 30));
            } else {
                closeButton.setBackground(new Color(220, 50, 50));
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeButton);

        fullscreenFrame.add(imageLabel, BorderLayout.CENTER);
        fullscreenFrame.add(tabbedPane, BorderLayout.SOUTH);
        fullscreenFrame.add(buttonPanel, BorderLayout.NORTH);

        fullscreenFrame.setVisible(true);
    }
}
