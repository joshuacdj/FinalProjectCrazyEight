package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.GradientPaint;
import java.io.*;

public class WelcomeScreen extends JFrame {

    private Color darkGreen= new Color(0x00512C); // Light green
    private Color lightGreen = new Color(0, 153, 76); // Dark green for contrast
    private Color orange = new Color(0xFF4C29);

    private JButton playButton;
    private JButton helpButton;
    private JButton exitButton;


    private class TitlePanel extends JPanel {
        private String titleText;
        private Font titleFont;
        private Color shadowColor;
        private int shadowOffset;

        public TitlePanel(String text, Font font, Color shadowColor, int shadowOffset) {
            this.titleText = text;
            this.titleFont = font;
            this.shadowColor = shadowColor;
            this.shadowOffset = shadowOffset;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Create a shadow effect
            g2d.setFont(titleFont);
            AttributedString attributedString = new AttributedString(titleText);
            attributedString.addAttribute(TextAttribute.FONT, titleFont);
            attributedString.addAttribute(TextAttribute.FOREGROUND, shadowColor, 0, titleText.length());

            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(titleText)) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

            // Draw the shadow
            g2d.drawString(attributedString.getIterator(), x + shadowOffset, y + shadowOffset);
            // Draw the actual text
            attributedString.addAttribute(TextAttribute.FOREGROUND, orange, 0, titleText.length());
            g2d.drawString(attributedString.getIterator(), x, y);

            g2d.dispose();
        }
    }
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
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Start maximized
        setMinimumSize(new Dimension(1000, 800)); // Minimum size of the window
        getContentPane().setLayout(new BorderLayout());

        JPanel backgroundPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradientPaint = new GradientPaint(0, 0, darkGreen, 0, getHeight(), lightGreen);
                g2d.setPaint(gradientPaint);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        getContentPane().add(backgroundPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridwidth = GridBagConstraints.REMAINDER; // commented this out and managed to place cards at the side
//        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Replace the old titleLabel with the new custom TitlePanel
        TitlePanel titlePanel = new TitlePanel("Crazy Eights", new Font("Serif", Font.BOLD | Font.ITALIC, 60), Color.BLACK, 2);
        titlePanel.setPreferredSize(new Dimension(800, 100)); // Set your preferred size
        gbc.gridx = 1;
        gbc.gridy = 0;
        backgroundPanel.add(titlePanel, gbc);

        // Card images
        ImageIcon icon1 = new ImageIcon(new ImageIcon("images/8_of_clubs.png").getImage().getScaledInstance(-1, 120 , Image.SCALE_SMOOTH));
        JLabel cardLabel1 = new JLabel(icon1);
        gbc.gridx = 0;
        gbc.gridy = 0;
        backgroundPanel.add(cardLabel1, gbc);

        ImageIcon icon2 = new ImageIcon(new ImageIcon("images/8_of_spades.png").getImage().getScaledInstance(-1, 120 , Image.SCALE_SMOOTH));
//        Test cardLabel2 = new Test(icon2, 45);
        JLabel cardLabel2 = new JLabel(icon2);
        gbc.gridx = 2;
        gbc.gridy = 2;
        backgroundPanel.add(cardLabel2, gbc);

        ImageIcon icon3 = new ImageIcon(new ImageIcon("images/8_of_hearts.png").getImage().getScaledInstance(-1, 120 , Image.SCALE_SMOOTH));
        JLabel cardLabel3 = new JLabel(icon3);
        gbc.gridx = 0;
        gbc.gridy = 2;
        backgroundPanel.add(cardLabel3, gbc);

        ImageIcon icon4 = new ImageIcon(new ImageIcon("images/8_of_diamonds.png").getImage().getScaledInstance(-1, 120 , Image.SCALE_SMOOTH));
        JLabel cardLabel4 = new JLabel(icon4);
        gbc.gridx = 2;
        gbc.gridy = 0;
        backgroundPanel.add(cardLabel4, gbc);

        // action buttons
        gbc.gridx = 1;
        gbc.gridy = 1;
        playButton = createCustomButton("Play", 300, 60);
        
        backgroundPanel.add(playButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        helpButton = createCustomButton("Help", 300, 60);

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Help helpWindow = new Help();
                helpWindow.setVisible(true);
            }
        });

        backgroundPanel.add(helpButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        exitButton = createCustomButton("Exit", 300, 60);
        backgroundPanel.add(exitButton, gbc);

        pack();
        setLocationRelativeTo(null);
    }

    public JButton createCustomButton(String text, int width, int height) {
        JButton button = new JButton(text) {
            // Custom painting is handled here
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Set gradients or solid colors here
                if (getModel().isPressed()) {
                    // Gradient paint for pressed button
                    g2.setPaint(new GradientPaint(0, 0, new Color(180, 180, 180, 200),
                            0, getHeight(), new Color(150, 150, 150, 200)));
                } else if (getModel().isRollover()) {
                    // Gradient paint for hover button
                    g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 255),
                            0, getHeight(), new Color(220, 220, 220, 255)));
                } else {
                    // Default gradient paint
                    g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 200),
                            0, getHeight(), new Color(230, 230, 230, 200)));
                }

                // Draw the rounded rectangle button background
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();

                super.paintComponent(g);
            }

            // Method to paint border if needed
            @Override
            protected void paintBorder(Graphics g) {
                // Optional: Implement custom border painting if required
            }
        };

        button.setPreferredSize(new Dimension(width, height));
        button.setForeground(Color.DARK_GRAY); // Text color
        button.setFocusPainted(false); // Remove the focus border
        button.setContentAreaFilled(false); // Tell Swing to not fill the content area
        button.setOpaque(false); // Make the button non-opaque


        return button;
    }

    public JButton getPlayButton(){
        return playButton;
    }

    public JButton getExitButton(){
        return exitButton;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new WelcomeScreen().setVisible(true));
    }
}
