package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.GradientPaint;

public class HelpFrame extends JFrame {
    private static volatile HelpFrame instance = null;

    private HelpFrame() {
        setTitle("Help");
        setSize(800, 550);
        setResizable(false);
        // Center the window when it opens
        setLocationRelativeTo(null);
        // Dispose the window on close
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel helpLabel = new JLabel("How to play Crazy Eights");
        Font titleFont = new Font("Chalkboard", Font.BOLD, 26);
        // Setting the font,colour and border of helpLabel
        helpLabel.setFont(titleFont);
        helpLabel.setForeground(Color.WHITE);
        helpLabel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        // Align helpLabel to the center of JFrame
        helpLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // creating 'How to Play' textBox
        JTextArea textBox = new JTextArea();
        // Make the text box read-only and remove unnecessary focus line in textBox
        textBox.setEditable(false);
        textBox.setFocusable(false);
        // Make the text box transparent and set the font colour white
        textBox.setOpaque(false);
        textBox.setForeground(Color.WHITE);
        // Enable line wrapping
        textBox.setLineWrap(true);
        textBox.setWrapStyleWord(true);

        // Add the instructions of the game to the text box
        textBox.setText("""
                Objective:
                The goal of the game is to clear your hand!

                How to Play:
                Each player takes turns playing 1 card per turn.
                You can play any card that matches the SUIT or VALUE of the latest played card.
                HOWEVER! An 8 card has a special effect that can be played anytime. It lets you set the current game's suit to any suit you want!
                A maximum of 5 cards can be drawn per turn if you do not have any playable cards.
                If 2 players have the same points after the game ends, the higher ranked player is determined by the one who has lesser cards in his hand.

                How to Win:
                The game ends when a player empties his/her hand. The loser is determined by the number of points he/she has at the end of the game.
                Aces are 1 point, Jacks, Queens and Kings are 10 points each. Any 8 cards are worth 50 POINTS. So make sure you use it fast!

                Have fun playing!
                """);

        // set the textBox's font
        Font textFont = new Font("Chalkboard", Font.PLAIN,18);
        textBox.setFont(textFont);

        // Add a white border to textBox to create a visible outline
        textBox.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        // Use GridBagLayout for flexible component positioning
        JPanel panel = new JPanel(new GridBagLayout());

        // Add an empty border with a small gap at the top
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 15, 16));

        // Add the helpLabel and textBox to the panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        // Expand horizontally
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        // Align to the top of the panel
        gbc.anchor = GridBagConstraints.PAGE_START;
        // adding helpLabel to the panel
        panel.add(helpLabel, gbc);

        // Next row
        gbc.gridy = 1;
        // Expand vertically
        gbc.weighty = 1.0;
        // Fill both horizontal and vertical
        gbc.fill = GridBagConstraints.BOTH;
        //adding textBox to the panel
        panel.add(textBox, gbc);

        // Set the background color gradient
        // Make the panel transparent so that the gradient background can be shown
        panel.setOpaque(false);

        // Override the paintComponent method to draw the gradient background
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Define the gradient colors
                Color color1 = new Color(0x033D15);
                Color color2 = new Color(3, 40, 21);

                // Create the gradient paint
                GradientPaint gradientPaint = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);

                // Set rendering hints for smoother gradient
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Paint the gradient background
                g2d.setPaint(gradientPaint);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        gradientPanel.setLayout(new BorderLayout());
        gradientPanel.add(panel, BorderLayout.CENTER);
        setContentPane(gradientPanel);
    }

    public static HelpFrame getInstance() {
        if (instance == null) {
            synchronized (HelpFrame.class) {
                if (instance == null) {
                    instance = new HelpFrame();
                }
            }
        }
        return instance;
    }
}
