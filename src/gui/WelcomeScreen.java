package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.io.*;

import static gui.Sound.welcomeClickSound;

public class WelcomeScreen extends JFrame {

    private final static int TITLE_BUTTON_WIDTH = 300;
    private final static int TITLE_BUTTON_HEIGHT = 60;
    private final static Dimension DECORATIVE_CARD_DIMENSION = new Dimension(110, 160);
    private final static Color DARK_GREEN = new Color(0x00512C); // Light green
    private final static Color LIGHT_GREEN = new Color(0, 153, 76); // Dark green for contrast
    private static JButton playButton;
    private static JButton helpButton;
    private static JButton exitButton;

    public WelcomeScreen() {

        // Set dock icon
        try {
            Image iconImage = ImageIO.read(new File("images/CrazyEightIcon.png"));
            setIconImage(iconImage);

            // For macOS, set the dock icon
            if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
                // Java 9 and newer
                if (Taskbar.isTaskbarSupported()) {
                    Taskbar.getTaskbar().setIconImage(iconImage);
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to find the app icon! " + e.getMessage());
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Crazy Eights - Card Game");

        // Start the window maximized
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        getContentPane().setLayout(new BorderLayout());

        // Create a new panel for the welcome screen background
        JPanel backgroundPanel = new JPanel(new GridBagLayout()) {
            // Set the background of the welcome screen to gradient green
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradientPaint = new GradientPaint(0, 0, DARK_GREEN, 0, getHeight(), LIGHT_GREEN);
                g2d.setPaint(gradientPaint);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Add the green background to the window
        getContentPane().add(backgroundPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 0, 10, 0);

        // Replace the old titleLabel with the new custom TitlePanel
        TitlePanel titlePanel = new TitlePanel("Crazy Eights", new Font("Serif", Font.BOLD | Font.ITALIC, 60), Color.BLACK, 2);

        gbc.gridx = 1;
        gbc.gridy = 0;
        backgroundPanel.add(titlePanel, gbc);

        // Card images to decorate welcome screen

        // Adding each 8 card to the different sides of the screen
        ImageIcon icon1 = ImageUtility.loadAndScaleCardImage("images/8_of_clubs.png", DECORATIVE_CARD_DIMENSION.width, DECORATIVE_CARD_DIMENSION.height, false);
        JLabel cardLabel1 = new JLabel(icon1);
        gbc.gridx = 0;
        gbc.gridy = 0;
        backgroundPanel.add(cardLabel1, gbc);
        ImageIcon icon2 = ImageUtility.loadAndScaleCardImage("images/8_of_spades.png", DECORATIVE_CARD_DIMENSION.width, DECORATIVE_CARD_DIMENSION.height, false);
        JLabel cardLabel2 = new JLabel(icon2);
        gbc.gridx = 2;
        gbc.gridy = 2;
        backgroundPanel.add(cardLabel2, gbc);

        ImageIcon icon3 = ImageUtility.loadAndScaleCardImage("images/8_of_hearts.png", DECORATIVE_CARD_DIMENSION.width, DECORATIVE_CARD_DIMENSION.height, false);
        JLabel cardLabel3 = new JLabel(icon3);
        gbc.gridx = 0;
        gbc.gridy = 2;
        backgroundPanel.add(cardLabel3, gbc);

        ImageIcon icon4 = ImageUtility.loadAndScaleCardImage("images/8_of_diamonds.png", DECORATIVE_CARD_DIMENSION.width, DECORATIVE_CARD_DIMENSION.height, false);
        JLabel cardLabel4 = new JLabel(icon4);
        gbc.gridx = 2;
        gbc.gridy = 0;
        backgroundPanel.add(cardLabel4, gbc);

        // Create play button
        gbc.gridx = 1;
        gbc.gridy = 1;
        playButton = ButtonUtility.createCustomButton("Play", TITLE_BUTTON_WIDTH, TITLE_BUTTON_HEIGHT);

        // Adding play button to welcome screen
        backgroundPanel.add(playButton, gbc);

        // Create help button
        gbc.gridx = 1;
        gbc.gridy = 2;
        helpButton = ButtonUtility.createCustomButton("Help", TITLE_BUTTON_WIDTH, TITLE_BUTTON_HEIGHT);

        // Action listener for help button
        helpButton.addActionListener(e -> {
                welcomeClickSound();
                HelpFrame helpWindow = HelpFrame.getInstance();
                helpWindow.setVisible(true);
        });

        // Adding help button to welcome screen
        backgroundPanel.add(helpButton, gbc);

        // Create quit button
        gbc.gridx = 1;
        gbc.gridy = 3;
        exitButton = ButtonUtility.createCustomButton("Exit", TITLE_BUTTON_WIDTH, TITLE_BUTTON_HEIGHT);

        // Adding quit button to welcome screen
        backgroundPanel.add(exitButton, gbc);

        // Resizes the window to fit the components - buttons and images
        pack();

        // Centers the frame on the screen
        setLocationRelativeTo(null);
    }
    
    public JButton getPlayButton(){
        return playButton;
    }

    public JButton getExitButton(){
        return exitButton;
    }
}
