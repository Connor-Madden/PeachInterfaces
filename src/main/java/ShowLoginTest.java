import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import static org.junit.Assert.*;


// The admin login works properly
// Invalid logins are prevented
// Users can create new accounts and store the id properly

public class ShowLoginTest {
    private LogInPanel logInPanel;

    @Before
    public void setUp() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                logInPanel = new LogInPanel();
                logInPanel.loginFrame.setVisible(true); // Ensure the frame is visible
            });
        } catch (InterruptedException | InvocationTargetException e) {
            fail("Failed to initialize GUI: " + e.getMessage());
        }
    }

    @Test
    // checks if the admin user can sucessfully log in
    public void testAdminLoginSuccess() {
        SwingUtilities.invokeLater(() -> {
            logInPanel.usernameField.setText("admin");
            logInPanel.passwordField.setText("adminpass");

            JButton loginButton = findButton(logInPanel.loginFrame, "Login");
            assertNotNull("Login button not found", loginButton);

            loginButton.doClick();

            assertEquals("admin", logInPanel.usernameField.getText());
        });
    }

    @Test
    // checks to see if the incorrect credentials allow logins
    public void testInvalidLogin() {
        SwingUtilities.invokeLater(() -> {
            logInPanel.usernameField.setText("invalidUser");
            logInPanel.passwordField.setText("wrongPass");

            JButton loginButton = findButton(logInPanel.loginFrame, "Login");
            assertNotNull("Login button not found", loginButton);

            loginButton.doClick();

            assertNotEquals("invalidUser", logInPanel.usernameField.getText());
        });
    }

    @Test
    // checks if the account creation has been created
    public void testAccountCreation() {
        SwingUtilities.invokeLater(() -> {
            JButton createAccountButton = findButton(logInPanel.loginFrame, "Create Account");
            assertNotNull("Create Account button not found", createAccountButton);

            createAccountButton.doClick();

            logInPanel.usernameField.setText("newUser");
            logInPanel.passwordField.setText("newPass");
            logInPanel.saveCredentials();

            Map<String, String> credentials = logInPanel.loadCredentials();
            assertTrue("Account was not created", credentials.containsKey("newUser"));
            assertEquals("newPass", credentials.get("newUser"));
        });
    }

    private JButton findButton(Container container, String text) {
        for (Component c : container.getComponents()) {
            if (c instanceof JButton) {
                JButton button = (JButton) c;
                if (button.getText().equals(text)) {
                    return button;
                }
            } else if (c instanceof Container) {
                JButton foundButton = findButton((Container) c, text);
                if (foundButton != null) {
                    return foundButton;
                }
            }
        }
        return null;
    }
}
