/**
 * A simple Java Swing GUI application for a Fashion and Clothing Catalogue.
 * It displays a JFrame with a button and a label. When the button is clicked,
 * the label text changes.
 */
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CatalogueGUI {
    /**
     * The main method initializes the GUI components and sets up the JFrame.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        // Create a new JFrame with a title
        JFrame frame = new JFrame("Fashion and Clothing Catalogue");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation
        frame.setSize(700, 500); // Set the frame size

        // Create a JPanel to hold components
        JPanel panel = new JPanel();

        // Create a JLabel with initial text
        JLabel label = new JLabel("Hello, Swing!");

        // Create a JButton with label "Click Me"
        JButton button = new JButton("Click Me");

        /**
         * Adds an ActionListener to the button to change the label text when clicked.
         */
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("Button Clicked!");
            }
        });

        // Add components to the panel
        panel.add(label);
        panel.add(button);

        // Add panel to the frame
        frame.add(panel);

        // Make the frame visible
        frame.setVisible(true);
    }
}