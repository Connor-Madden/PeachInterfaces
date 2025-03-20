import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogInPanel {
    private JFrame loginFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckbox;
    private Icon clothingIcon;

    public LogInPanel() {
        // Create the login frame
        loginFrame = new JFrame("Login/Logout - Fashion and Clothing Catalogue");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(450, 300);
        loginFrame.setLayout(new BorderLayout());
        loginFrame.setResizable(false); // Prohibit full screen

        // Load and resize clothing icon
        ImageIcon originalIcon = new ImageIcon("src/main/java/ClothingIcon.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        clothingIcon = new ImageIcon(scaledImage);

        // Create the top panel for the logo and text
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ImageIcon logoIcon = new ImageIcon(new ImageIcon("src/main/images/Logo.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JLabel logoLabel = new JLabel(logoIcon);
        JLabel titleLabel = new JLabel("Welcome to the Peach Interfaces Fashion Catalogue!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        topPanel.add(logoLabel);
        topPanel.add(titleLabel);

        // Create the panel for the login form
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username field
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        loginPanel.add(usernameField, gbc);

        // Password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        loginPanel.add(passwordField, gbc);

        // Show password checkbox
        gbc.gridx = 1;
        gbc.gridy = 2;
        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.addActionListener(e ->
                passwordField.setEchoChar(showPasswordCheckbox.isSelected() ? '\0' : '*')
        );
        loginPanel.add(showPasswordCheckbox, gbc);

        // Login and Exit buttons panel
        gbc.gridx = 1;
        gbc.gridy = 3;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(60, 179, 113));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if ("admin".equals(username) && "adminpass".equals(password)) {
                    showLoginDialog("You are logged in as admin", username, true);
                } else if ("user".equals(username) && "userpass".equals(password)) {
                    showLoginDialog("You are logged in as user", username, false);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(255, 69, 0));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setFont(new Font("Arial", Font.BOLD, 12));
        exitButton.addActionListener(e -> {
            // Show a confirmation dialog with "Thank you for browsing"
            int confirm = JOptionPane.showConfirmDialog(
                    loginFrame,
                    "Thank you for stopping by! Are you sure you want to exit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    clothingIcon
            );

            // If the user confirms, exit the application
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);

        loginPanel.add(buttonPanel, gbc);

        // Add components to the frame
        loginFrame.add(topPanel, BorderLayout.NORTH);
        loginFrame.add(loginPanel, BorderLayout.CENTER);

        // Center the frame on the screen
        loginFrame.setLocationRelativeTo(null);

        // Make the frame visible
        loginFrame.setVisible(true);
    }

    private void showLoginDialog(String message, String username, boolean isAdmin) {
        JDialog dialog = new JDialog(loginFrame, "Login Successful", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(loginFrame);

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dialog.add(messageLabel, BorderLayout.CENTER);

        JButton continueButton = new JButton("Continue");
        continueButton.setBackground(new Color(60, 179, 113));
        continueButton.setForeground(Color.WHITE);
        continueButton.setFont(new Font("Arial", Font.BOLD, 12));
        continueButton.setFocusPainted(false);
        continueButton.setBorderPainted(false);

        continueButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(46, 139, 87));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(60, 179, 113));
            }
        });

        continueButton.addActionListener(e -> {
            dialog.dispose();
            loginFrame.dispose();
            if (isAdmin) {
                new AdminCatalogueGUI(username);
            } else {
                new UserCatalogueGUI(username);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(continueButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LogInPanel::new);
    }
}