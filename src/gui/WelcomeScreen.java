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

    private final static int TITLEBUTTONWIDTH = 300;
    private final static int TITLEBUTTONHEIGHT = 60;

    private Color darkGreen= new Color(0x00512C); // Light green
    private Color lightGreen = new Color(0, 153, 76); // Dark green for contrast

    private JButton playButton;
    private JButton helpButton;
    private JButton exitButton;

    public WelcomeScreen() {

        // Set the apple dock icon
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

        // Set the minimum size of the window
        setMinimumSize(new Dimension(1000, 800));
        getContentPane().setLayout(new BorderLayout());

        // Create a new panel for the welcome screen background
        JPanel backgroundPanel = new JPanel(new GridBagLayout()) {
            // Set the background of the welcome screen to gradient green
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradientPaint = new GradientPaint(0, 0, darkGreen, 0, getHeight(), lightGreen);
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

        // Setting size of the title
        titlePanel.setPreferredSize(new Dimension(800, 100));
        gbc.gridx = 1;
        gbc.gridy = 0;
        backgroundPanel.add(titlePanel, gbc);

        // Card images to decorate welcome screen
        final int DISPLAYCARDWIDTH = -1;
        final int DISPLAYCARDHEIGHT = 120;

        // Adding each 8 card to the different sides of the screen
        ImageIcon icon1 = new ImageIcon(new ImageIcon("images/8_of_clubs.png").getImage().getScaledInstance(DISPLAYCARDWIDTH, DISPLAYCARDHEIGHT , Image.SCALE_SMOOTH));
        JLabel cardLabel1 = new JLabel(icon1);
        gbc.gridx = 0;
        gbc.gridy = 0;
        backgroundPanel.add(cardLabel1, gbc);

        ImageIcon icon2 = new ImageIcon(new ImageIcon("images/8_of_spades.png").getImage().getScaledInstance(DISPLAYCARDWIDTH, DISPLAYCARDHEIGHT , Image.SCALE_SMOOTH));
        JLabel cardLabel2 = new JLabel(icon2);
        gbc.gridx = 2;
        gbc.gridy = 2;
        backgroundPanel.add(cardLabel2, gbc);

        ImageIcon icon3 = new ImageIcon(new ImageIcon("images/8_of_hearts.png").getImage().getScaledInstance(DISPLAYCARDWIDTH, DISPLAYCARDHEIGHT , Image.SCALE_SMOOTH));
        JLabel cardLabel3 = new JLabel(icon3);
        gbc.gridx = 0;
        gbc.gridy = 2;
        backgroundPanel.add(cardLabel3, gbc);

        ImageIcon icon4 = new ImageIcon(new ImageIcon("images/8_of_diamonds.png").getImage().getScaledInstance(DISPLAYCARDWIDTH, DISPLAYCARDHEIGHT , Image.SCALE_SMOOTH));
        JLabel cardLabel4 = new JLabel(icon4);
        gbc.gridx = 2;
        gbc.gridy = 0;
        backgroundPanel.add(cardLabel4, gbc);

        // Create play button
        gbc.gridx = 1;
        gbc.gridy = 1;
        playButton = ButtonUtility.createCustomButton("Play", TITLEBUTTONWIDTH, TITLEBUTTONHEIGHT);

        // Adding play button to welcome screen
        backgroundPanel.add(playButton, gbc);

        // Create help button
        gbc.gridx = 1;
        gbc.gridy = 2;
        helpButton = ButtonUtility.createCustomButton("Help", TITLEBUTTONWIDTH, TITLEBUTTONHEIGHT);

        // Action listener for help button
        helpButton.addActionListener(e -> {
                welcomeClickSound();
                Help helpWindow = gui.Help.getInstance();
                helpWindow.setVisible(true);
        });

        // Adding help button to welcome screen
        backgroundPanel.add(helpButton, gbc);

        // Create quit button
        gbc.gridx = 1;
        gbc.gridy = 3;
        exitButton = ButtonUtility.createCustomButton("Exit", TITLEBUTTONWIDTH, TITLEBUTTONHEIGHT);

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
