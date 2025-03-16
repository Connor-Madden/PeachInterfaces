import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogInPanel {
    private JFrame loginFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LogInPanel() {
        // Create the login frame
        loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 200);
        loginFrame.setLayout(new BorderLayout());

        // Create the panel for the login form
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Username field
        loginPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        loginPanel.add(usernameField);

        // Password field
        loginPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        loginPanel.add(passwordField);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Check credentials
                if ("admin".equals(username) && "adminpass".equals(password)) {
                    // If credentials are correct, close the login frame and open the AdminCatalogueGUI
                    loginFrame.dispose();
                    new AdminCatalogueGUI(username);
                } else {
                    // If credentials are incorrect, show an error message
                    JOptionPane.showMessageDialog(loginFrame, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        loginPanel.add(loginButton);

        // Add the login panel to the frame
        loginFrame.add(loginPanel, BorderLayout.CENTER);

        // Center the frame on the screen
        loginFrame.setLocationRelativeTo(null);

        // Make the frame visible
        loginFrame.setVisible(true);
    }

    public static void main(String[] args) {
        // Run the login panel
        SwingUtilities.invokeLater(LogInPanel::new);
    }
}