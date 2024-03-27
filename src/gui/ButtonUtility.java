package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ButtonUtility {
    public static JButton createCustomButton(String text, int width, int height) {
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
}
