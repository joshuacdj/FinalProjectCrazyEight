package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.GradientPaint;

public class Help extends JFrame {

    private Font titleFont = new Font("Chalkboard", Font.BOLD, 26); // Elegant font

    public Help() {

        setTitle("Help");
        setSize(800, 500);
        setMinimumSize(new Dimension(850,600));
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose the window on close

        JLabel helpLabel = new JLabel("How to play Crazy Eight");
        helpLabel.setFont(titleFont); // Set the font of helpLabel
        helpLabel.setForeground(Color.WHITE);
        helpLabel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        helpLabel.setHorizontalAlignment(SwingConstants.CENTER); // Align label text to the center

        JTextArea textBox = new JTextArea();
        textBox.setEditable(false); // Make the text box read-only
        textBox.setFocusable(false);
        textBox.setOpaque(false); // Make the text box transparent
        textBox.setForeground(Color.WHITE); // Set text color to white
        textBox.setLineWrap(true); // Enable line wrapping
        textBox.setWrapStyleWord(true);

        // Add some sample text to the text box
        textBox.setText("Objective:\nThe goal of the game is to have the least number of points when someone clears their hand!\n\n" +
                "How to Play:\nEach player takes turns playing 1 card per turn.\n" +
                "You can play any card that matches the SUIT or VALUE of the latest played card.\n" +
                "HOWEVER! An 8 card has a special effect that can be played anytime. It lets you set the current game's suit to any suit you want!\n" +
                "A maximum of 5 cards can be drawn per turn if you do not have any playable cards.\n\n" +
                "How to Win:\nThe game ends when a player empties his/her hand. The loser is determined by the number of points he/she has at the end of the game.\n" +
                "Aces are 1 point, Jacks, Queens and Kings are 10 points each. Any 8 cards are worth 50 POINTS. So make sure you use it fast!\n\n" +
                "Have fun playing!\n");


        // Set the background color of the text box to match the JFrame's background
        textBox.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        Font textFont = new Font("Chalkboard", Font.PLAIN,18);
        textBox.setFont(textFont);

        // Add a border to the text box to create a visible outline
        textBox.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        JPanel panel = new JPanel(new GridBagLayout()); // Use GridBagLayout for flexible component positioning

        // Add an empty border with a small gap at the top
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 15, 16));

        // Add the helpLabel and textBox to the panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Expand horizontally
        gbc.weighty = 0.0; // Do not expand vertically
        gbc.anchor = GridBagConstraints.PAGE_START; // Align to the top
        panel.add(helpLabel, gbc);

        gbc.gridy = 1; // Next row
        gbc.weighty = 1.0; // Expand vertically
        gbc.fill = GridBagConstraints.BOTH; // Fill both horizontal and vertical
        panel.add(textBox, gbc);

        // Set the background color gradient
        panel.setOpaque(false); // Make the panel transparent to show the gradient background

        // Override the paintComponent method to draw the gradient background
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Define the gradient colors
                Color color1 = new Color(0x033D15);
                Color color2 = new Color(3, 61, 21);

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
}
