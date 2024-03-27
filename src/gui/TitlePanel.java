package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

class TitlePanel extends JPanel {
    private String titleText;
    private Font titleFont;
    private Color shadowColor;
    private int shadowOffset;
    private final Color ORANGE = new Color(0xFF4C29);
    private static final Dimension TITLE_PANEL_DIMENSION = new Dimension(800, 100);

    public TitlePanel(String text, Font font, Color shadowColor, int shadowOffset) {
        this.titleText = text;
        this.titleFont = font;
        this.shadowColor = shadowColor;
        this.shadowOffset = shadowOffset;
        setOpaque(false);
        this.setPreferredSize(TITLE_PANEL_DIMENSION);
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
        attributedString.addAttribute(TextAttribute.FOREGROUND, ORANGE, 0, titleText.length());
        g2d.drawString(attributedString.getIterator(), x, y);

        g2d.dispose();
    }
}
