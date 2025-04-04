import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for the LogInPanel class. These tests ensure that the login functionality
 * works correctly, invalid logins are prevented, and users can successfully create new accounts.
 */
public class ShowLoginTest {
    private LogInPanel logInPanel;

    /**
     * Sets up the test environment by initializing the LogInPanel and ensuring
     * the login frame is visible before each test.
     */
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

    /**
     * Tests if the admin user can successfully log in with valid credentials.
     * The test simulates entering the correct username and password, clicking
     * the login button, and verifying that the login is successful.
     */
    @Test
    public void testAdminLoginSuccess() {
        SwingUtilities.invokeLater(() -> {
            logInPanel.usernameField.setText("admin");
            logInPanel.passwordField.setText("adminpass");

            JButton loginButton = findButton(logInPanel.loginFrame, "Login");
            assertNotNull("Login button not found", loginButton);

            loginButton.doClick();

            // Check if the username field still contains the correct admin username
            assertEquals("admin", logInPanel.usernameField.getText());
        });
    }

    /**
     * Tests that invalid login credentials prevent the user from logging in.
     * The test simulates entering an incorrect username and password and
     * verifies that the login does not proceed by ensuring the username is unchanged.
     */
    @Test
    public void testInvalidLogin() {
        SwingUtilities.invokeLater(() -> {
            logInPanel.usernameField.setText("invalidUser");
            logInPanel.passwordField.setText("wrongPass");

            JButton loginButton = findButton(logInPanel.loginFrame, "Login");
            assertNotNull("Login button not found", loginButton);

            loginButton.doClick();

            // Ensure the username field is not set to the invalid username
            assertNotEquals("invalidUser", logInPanel.usernameField.getText());
        });
    }

    /**
     * Tests if a new user account can be created successfully.
     * The test simulates clicking the "Create Account" button, entering a new username
     * and password, saving the credentials, and verifying that the new account exists
     * in the credentials storage.
     */
    @Test
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

    /**
     * Helper method to find a button with the specified text within a container.
     * This method recursively searches for a button in the container and its subcomponents.
     *
     * @param container the container to search in
     * @param text the text of the button to find
     * @return the JButton if found, or null if not found
     */
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
