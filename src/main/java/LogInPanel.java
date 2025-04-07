import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A graphical user interface for user authentication in a fashion and clothing catalogue application.
 * This class provides a login panel with various functionalities including:
 * User login with username and password
 * Account creation for new users
 * Account removal for existing users
 * Password recovery
 * Guest access
 * Admin and regular user differentiation
 *
 * The class maintains user credentials in a file and provides appropriate visual feedback
 * for all user interactions. It features modern UI elements with hover effects and
 * responsive design.
 *
 * After successful authentication, users are directed to either the admin or regular
 * user interface based on their credentials.
 *
 * @version 3.0
 * @see AdminCatalogueGUI
 * @see UserCatalogueGUI
 * @since Java 23
 */
public class LogInPanel {
    JFrame loginFrame;
    JTextField usernameField;
    JPasswordField passwordField;
    private JCheckBox showPasswordCheckbox;
    private Icon clothingIcon;
    private Map<String, String> userCredentials;
    private static final String CREDENTIALS_FILE = "user_credentials.dat";
    private final Font modernFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Component rootPanel;

    /**
     * Constructs a new LogInPanel with all necessary UI components and functionality.
     * Initializes the user credentials database, creates the login frame with styled components,
     * and sets up all action listeners for user interactions.
     *
     * The constructor performs the following operations:
     * Loads existing user credentials from file
     * Creates the main login frame with modern styling
     * Sets up username and password fields with validation
     * Configures all action buttons (login, create account, etc.)
     * Implements password visibility toggle
     */
    public LogInPanel() {
        // Load user credentials from file
        userCredentials = loadCredentials();

        // Add default admin credentials if not present
        if (!userCredentials.containsKey("admin")) {
            userCredentials.put("admin", "adminpass");
        }

        // Create the login frame
        loginFrame = new JFrame("Login/Logout - Fashion and Clothing Catalogue");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(450, 400);
        loginFrame.setLayout(new BorderLayout());
        loginFrame.setResizable(false);

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

        // Username field with enhanced styling
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.setFont(modernFont);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        loginPanel.add(usernameField, gbc);

        // Password field with enhanced styling
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setFont(modernFont);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        loginPanel.add(passwordField, gbc);

        /// Enhanced show password checkbox with modern styling
        gbc.gridx = 1;
        gbc.gridy = 2;
        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
        showPasswordCheckbox.setFocusPainted(false);
        showPasswordCheckbox.setOpaque(false);  // Make the background transparent
        showPasswordCheckbox.setBorderPainted(false);
        showPasswordCheckbox.setContentAreaFilled(false);  // Remove the default checkbox background
        showPasswordCheckbox.setForeground(new Color(70, 70, 70));  // Dark gray text


// Create a custom checkbox icon
        UIManager.put("CheckBox.icon", UIManager.getIcon("CheckBox.icon"));
        showPasswordCheckbox.setIcon(new CheckBoxIcon(false));
        showPasswordCheckbox.setSelectedIcon(new CheckBoxIcon(true));

// Add hover effect
        showPasswordCheckbox.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                showPasswordCheckbox.setForeground(new Color(30, 30, 30));
                showPasswordCheckbox.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent evt) {
                showPasswordCheckbox.setForeground(new Color(70, 70, 70));
                showPasswordCheckbox.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        showPasswordCheckbox.addItemListener(e -> {
            if (showPasswordCheckbox.isSelected()) {
                passwordField.setEchoChar((char) 0); // Show password
            } else {
                passwordField.setEchoChar('•'); // Use bullet character
            }
        });
        loginPanel.add(showPasswordCheckbox, gbc);

        // Login, Create Account, Remove Account, Forgot Password, Guest, and Exit buttons panel
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Login button with hover effect
        JButton loginButton = createStyledButton("Login", new Color(60, 179, 113));
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Please enter both username and password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("admin".equals(username) && "adminpass".equals(password)) {
                showLoginDialog("You are logged in as an admin", username, true);
            } else if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
                showLoginDialog("You are logged in as " + username, username, false);
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Create Account button with hover effect
        JButton createAccountButton = createStyledButton("Create Account", new Color(70, 130, 180));
        createAccountButton.addActionListener(e -> showCreateAccountDialog());

        // Remove Account button with hover effect
        JButton removeAccountButton = createStyledButton("Remove Account", new Color(220, 20, 60));
        removeAccountButton.addActionListener(e -> showRemoveAccountDialog());

        // Forgot Password button with hover effect
        JButton forgotPasswordButton = createStyledButton("Forgot Password", new Color(218, 165, 32));
        forgotPasswordButton.addActionListener(e -> showForgotPasswordDialog());

        // Guest button with hover effect
        JButton guestButton = createStyledButton("Continue as Guest", new Color(100, 100, 100));
        guestButton.addActionListener(e -> {
            loginFrame.dispose();
            showGuestLoginDialog();
        });

        // Exit button with hover effect
        JButton exitButton = createStyledButton("Exit", new Color(255, 69, 0));
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    loginFrame,
                    "Thank you for stopping by! Are you sure you want to exit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    clothingIcon
            );

            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Add buttons to panel
        buttonPanel.add(loginButton);
        buttonPanel.add(createAccountButton);
        buttonPanel.add(removeAccountButton);
        buttonPanel.add(forgotPasswordButton);
        buttonPanel.add(guestButton);
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

    /**
     * Creates a darker version of the given color by the specified factor.
     *
     * @param color the original color to darken
     * @param factor the darkening factor (0.0 to 1.0)
     * @return a new darkened color
     */
    private Color darkenColor(Color color, double factor) {
        int r = (int) (color.getRed() * (1 - factor));
        int g = (int) (color.getGreen() * (1 - factor));
        int b = (int) (color.getBlue() * (1 - factor));
        return new Color(
                Math.max(r, 0),
                Math.max(g, 0),
                Math.max(b, 0),
                color.getAlpha()
        );
    }

    /**
     * Displays a dialog for removing an existing user account.
     * The dialog prompts for username and password verification before removal.
     * Admin accounts cannot be removed through this dialog.
     */
    private void showRemoveAccountDialog() {
        JDialog removeAccountDialog = new JDialog(loginFrame, "Remove Account", true);
        removeAccountDialog.setLayout(new GridBagLayout());
        removeAccountDialog.setSize(350, 200);
        removeAccountDialog.setLocationRelativeTo(loginFrame);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        removeAccountDialog.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        removeAccountDialog.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        removeAccountDialog.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        removeAccountDialog.add(passwordField, gbc);

        JButton removeButton = new JButton("Remove Account");
        removeButton.setBackground(new Color(220, 20, 60));
        removeButton.setForeground(Color.WHITE);
        removeButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(removeAccountDialog, "Please enter both username and password", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("admin".equals(username)) {
                JOptionPane.showMessageDialog(removeAccountDialog, "Cannot remove admin account", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!userCredentials.containsKey(username)) {
                JOptionPane.showMessageDialog(removeAccountDialog, "Account not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!userCredentials.get(username).equals(password)) {
                JOptionPane.showMessageDialog(removeAccountDialog, "Incorrect password", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    removeAccountDialog,
                    "Are you sure you want to permanently remove your account?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                userCredentials.remove(username);
                saveCredentials();
                JOptionPane.showMessageDialog(removeAccountDialog, "Account removed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                removeAccountDialog.dispose();
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 2;
        removeAccountDialog.add(removeButton, gbc);

        removeAccountDialog.setVisible(true);
    }

    public Component getRootPanel() {
        return rootPanel;
    }


    /**
     * Custom Icon implementation for styled checkbox appearance.
     */
    private static class CheckBoxIcon implements Icon {
        private final boolean selected;

        /**
         * Creates a new CheckBoxIcon with the specified selection state.
         *
         * @param selected whether the checkbox should appear selected
         */
        public CheckBoxIcon(boolean selected) {
            this.selected = selected;
        }

         /** Paints the checkbox icon with custom styling.
         *
         * @param c the component being painted
         * @param g the graphics context
         * @param x the x coordinate of the icon
         * @param y the y coordinate of the icon
         */
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw the checkbox border
            g2.setColor(new Color(80, 80, 80));
            g2.drawRoundRect(x, y, 14, 14, 4, 4);

            if (selected) {
                // Draw the checkmark
                g2.setColor(new Color(128, 0, 128));
                g2.fillRoundRect(x, y, 14, 14, 4, 4);

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(x + 3, y + 7, x + 6, y + 10);
                g2.drawLine(x + 6, y + 10, x + 11, y + 3);
            }

            g2.dispose();
        }

        /**
         * Gets the width of the icon.
         *
         * @return the icon width in pixels
         */
        @Override
        public int getIconWidth() {
            return 16;
        }

        /**
         * Gets the height of the icon.
         *
         * @return the icon height in pixels
         */
        @Override
        public int getIconHeight() {
            return 16;
        }
    }

    /**
     * Displays a dialog for retrieving a forgotten password.
     * The dialog prompts for username and displays the associated password if found.
     * Admin password retrieval is handled differently for security.
     */
    private void showForgotPasswordDialog() {
        JDialog forgotPasswordDialog = new JDialog(loginFrame, "Forgot Password", true);
        forgotPasswordDialog.setLayout(new GridBagLayout());
        forgotPasswordDialog.setSize(350, 150);
        forgotPasswordDialog.setLocationRelativeTo(loginFrame);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField usernameField = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        forgotPasswordDialog.add(new JLabel("Enter your username:"), gbc);

        gbc.gridx = 1;
        forgotPasswordDialog.add(usernameField, gbc);

        JButton retrieveButton = new JButton("Retrieve Password");
        retrieveButton.setBackground(new Color(218, 165, 32));
        retrieveButton.setForeground(Color.WHITE);
        retrieveButton.addActionListener(e -> {
            String username = usernameField.getText();

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(forgotPasswordDialog, "Please enter a username", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("admin".equals(username)) {
                JOptionPane.showMessageDialog(forgotPasswordDialog, "Please contact system administrator for admin password", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (!userCredentials.containsKey(username)) {
                JOptionPane.showMessageDialog(forgotPasswordDialog, "Account not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(forgotPasswordDialog,
                    "Your password is: " + userCredentials.get(username),
                    "Password Retrieved",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        gbc.gridx = 1;
        gbc.gridy = 1;
        forgotPasswordDialog.add(retrieveButton, gbc);

        forgotPasswordDialog.setVisible(true);
    }

    /**
     * Creates a styled button with consistent appearance and hover effects.
     *
     * @param text the button text
     * @param bgColor the background color for the button
     * @return a configured JButton with hover effects
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(darkenColor(bgColor, 0.2));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return button;
    }

    /**
     * Displays a dialog for creating a new user account.
     * The dialog validates username availability and password matching before creation.
     */
    private void showCreateAccountDialog() {
        JDialog createAccountDialog = new JDialog(loginFrame, "Create New Account", true);
        createAccountDialog.setLayout(new GridBagLayout());
        createAccountDialog.setSize(350, 200);
        createAccountDialog.setLocationRelativeTo(loginFrame);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField newUsernameField = new JTextField(15);
        JPasswordField newPasswordField = new JPasswordField(15);
        JPasswordField confirmPasswordField = new JPasswordField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        createAccountDialog.add(new JLabel("New Username:"), gbc);

        gbc.gridx = 1;
        createAccountDialog.add(newUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        createAccountDialog.add(new JLabel("New Password:"), gbc);

        gbc.gridx = 1;
        createAccountDialog.add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        createAccountDialog.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        createAccountDialog.add(confirmPasswordField, gbc);

        JButton createButton = new JButton("Create Account");
        createButton.setBackground(new Color(70, 130, 180));
        createButton.setForeground(Color.WHITE);
        createButton.addActionListener(e -> {
            String username = newUsernameField.getText();
            String password = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(createAccountDialog, "Username and password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userCredentials.containsKey(username)) {
                JOptionPane.showMessageDialog(createAccountDialog, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(createAccountDialog, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.length() < 4) {
                JOptionPane.showMessageDialog(createAccountDialog, "Password must be at least 4 characters", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            userCredentials.put(username, password);
            saveCredentials();
            JOptionPane.showMessageDialog(createAccountDialog, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            createAccountDialog.dispose();
        });

        gbc.gridx = 1;
        gbc.gridy = 3;
        createAccountDialog.add(createButton, gbc);

        createAccountDialog.setVisible(true);
    }

    /**
     * Loads user credentials from the persistent storage file.
     *
     * @return a Map containing username-password pairs
     * @throws RuntimeException if there's an error reading the credentials file
     */
    @SuppressWarnings("unchecked")
    Map<String, String> loadCredentials() {
        File file = new File(CREDENTIALS_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(loginFrame, "Error loading user credentials. Using empty database.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new HashMap<>();
        }
    }

    /**
     * Saves the current user credentials to persistent storage.
     * Displays an error message if the operation fails.
     */
    void saveCredentials() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CREDENTIALS_FILE))) {
            oos.writeObject(userCredentials);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(loginFrame, "Error saving user credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays a success dialog after successful login and transitions to the appropriate catalogue interface.
     *
     * @param message the success message to display
     * @param username the username of the logged-in user
     * @param isAdmin flag indicating if the user has admin privileges
     */
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

        continueButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                continueButton.setBackground(new Color(46, 139, 87));
            }
            public void mouseExited(MouseEvent evt) {
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

    /**
     * Displays a dialog for guest users and transitions to the user catalogue interface.
     * Guest users have limited functionality compared to registered users.
     */
    private void showGuestLoginDialog() {
        JDialog dialog = new JDialog(loginFrame, "Guest Access", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(loginFrame);

        JLabel messageLabel = new JLabel("<html><center>You are browsing as a guest.<br>Favourite features are disabled for guest users.</center></html>",
                SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dialog.add(messageLabel, BorderLayout.CENTER);

        JButton continueButton = new JButton("Continue as Guest");
        continueButton.setBackground(new Color(100, 100, 100));
        continueButton.setForeground(Color.WHITE);
        continueButton.setFont(new Font("Arial", Font.BOLD, 12));
        continueButton.setFocusPainted(false);
        continueButton.setBorderPainted(false);

        continueButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                continueButton.setBackground(new Color(70, 70, 70));
            }
            public void mouseExited(MouseEvent evt) {
                continueButton.setBackground(new Color(100, 100, 100));
            }
        });

        continueButton.addActionListener(e -> {
            dialog.dispose();
            loginFrame.dispose();
            new UserCatalogueGUI("Guest");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(continueButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    /**
     * The entry point for the login application. Creates and shows the login panel
     * on the Event Dispatch Thread to ensure thread safety.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LogInPanel::new);
    }
}