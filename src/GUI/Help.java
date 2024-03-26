package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.text.AttributedString;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.GradientPaint;

import java.io.*;

public class Help extends JFrame {

    private Font titleFont = new Font("Segoe Script", Font.BOLD, 18); // Elegant font

    public Help() {

        setTitle("Help");
        setSize(1000, 600);
        setMinimumSize(new Dimension(800,500));
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
        textBox.setText("Objective: \nThe goal of the game is to have the least number of points when someone clears their hand!\n\n\n" +
                "How to Play: \nEach player takes turns playing a maximum of 1 card per turn.\n" +
                "You can play any card that matches the SUIT or VALUE of the top card of the discard pile.\n" +
                "BUT an 8 can be played anytime and upon playing an 8, you can set the current suit to any suit you want!\n" +
                "If you don't have any playable cards, you can draw up to 5 cards. If you still don't have any cards to play, your turn will be skipped!\n\n\n" +
                "How to Win: \nThe game ends when someone empties their entire hand and the loser is determined by the number of points he/she has at the end of the game.\n" +
                "Aces are 1 point, Kings are 13 points. Any 8 cards are worth 50 POINTS!\n\n\n" +
                "Have fun playing!\n");


        // Set the background color of the text box to match the JFrame's background
        textBox.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        Font textFont = textBox.getFont();
        textBox.setFont(new Font(textFont.getName(), textFont.getStyle(), 15));

        // Add a border to the text box to create a visible outline
        textBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel panel = new JPanel(new GridBagLayout()); // Use GridBagLayout for flexible component positioning

        // Add an empty border with a small gap at the top
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 15, 15));

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
                Color color1 = new Color(0xA49A7F);
                Color color2 = new Color(164, 154, 127);

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
