import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Crazy Eights");
        setSize(1000, 800); // Set the size based on your background image

        // Set layout manager to null for absolute positioning of components
        setLayout(null);

        // Background label
        JLabel backgroundLabel = new JLabel();
//        ImageIcon backgroundImage = new ImageIcon(this.getClass().getResource("/Users/jeremaine/Downloads/DALL·E 2024-02-29 20.31.46 - Create a 1000x800 PNG image designed as a background for a welcome screen of a Baccarat game. The image should be elegant and suitable for overlaying .jpg"));
//        backgroundLabel.setIcon(backgroundImage);

        backgroundLabel.setBounds(0, 0, 1000, 800); // Set the bounds to fill the frame
        add(backgroundLabel);

        // Title
        JLabel titleLabel = new JLabel("CRAZY EIGHTS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48)); // Adjust font size as needed
        titleLabel.setForeground(Color.BLACK); // Text color
        titleLabel.setBounds(250, 100, 500, 100); // Adjust location and size as needed
        backgroundLabel.add(titleLabel); // Add the title label to the background label

        // Play Button
        JButton playButton = createButton("Play", 350, 300, 300, 50);
        backgroundLabel.add(playButton);

        // Help Button
        JButton helpButton = createButton("Help", 350, 400, 300, 50);
        backgroundLabel.add(helpButton);

        // Exit Button
        JButton exitButton = createButton("Exit", 350, 500, 300, 50);
        backgroundLabel.add(exitButton);

        // Set the location of the window to the center of the screen
        setLocationRelativeTo(null);
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("Comic Sans", Font.PLAIN, 20)); // Set the font to match your design
        button.setBounds(x, y, width, height); // Set the position and size
        button.addActionListener((ActionEvent e) -> {
            System.out.println("hello");
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeScreen().setVisible(true));
    }
}
