package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.text.AttributedString;

public class WelcomeScreen extends JFrame {

    private Color darkGreen= new Color(0x00512C); // Light green
    private Color lightGreen = new Color(0, 153, 76); // Dark green for contrast
    private Color orange = new Color(0xFF4C29);
    private Font titleFont = new Font("Segoe Script", Font.BOLD, 60); // Elegant font
    private Font buttonFont = new Font("Segoe UI Symbol", Font.BOLD, 22); // Modern, readable font
    private Font symbolFont = new Font("Segoe UI Symbol", Font.PLAIN, 100); // For card symbols

    private JButton playButton;
    private JButton helpButton;
    private JButton exitButton;


    private class TitlePanel extends JPanel {
        private Font buttonFont = new Font("Segoe UI Symbol", Font.BOLD, 22); // Modern, readable font
        private Font symbolFont = new Font("Segoe UI Symbol", Font.PLAIN, 100); // For card symbols
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
        ImageIcon icon1 = new ImageIcon(new ImageIcon("src/main/resources/images/8_of_clubs.png").getImage().getScaledInstance(-1, 120 , Image.SCALE_SMOOTH));
        JLabel cardLabel1 = new JLabel(icon1);
        gbc.gridx = 0;
        gbc.gridy = 0;
        backgroundPanel.add(cardLabel1, gbc);

        ImageIcon icon2 = new ImageIcon(new ImageIcon("src/main/resources/images/8_of_spades.png").getImage().getScaledInstance(-1, 120 , Image.SCALE_SMOOTH));
//        Test cardLabel2 = new Test(icon2, 45);
        JLabel cardLabel2 = new JLabel(icon2);
        gbc.gridx = 2;
        gbc.gridy = 2;
        backgroundPanel.add(cardLabel2, gbc);

        ImageIcon icon3 = new ImageIcon(new ImageIcon("src/main/resources/images/8_of_hearts.png").getImage().getScaledInstance(-1, 120 , Image.SCALE_SMOOTH));
        JLabel cardLabel3 = new JLabel(icon3);
        gbc.gridx = 0;
        gbc.gridy = 2;
        backgroundPanel.add(cardLabel3, gbc);

        ImageIcon icon4 = new ImageIcon(new ImageIcon("src/main/resources/images/8_of_diamonds.png").getImage().getScaledInstance(-1, 120 , Image.SCALE_SMOOTH));
        JLabel cardLabel4 = new JLabel(icon4);
        gbc.gridx = 2;
        gbc.gridy = 0;
        backgroundPanel.add(cardLabel4, gbc);

        // action buttons
        gbc.gridx = 1;
        gbc.gridy = 1;
        playButton = new JButton("playplayplayplayplay");
        playButton.addMouseListener(new MouseAdapter() {
                                        public void mouseClicked(MouseEvent e) {
//                                            if (text.equals("Play")) {
//                                                // Assuming this code is inside an ActionListener in your WelcomeScreen
//                                                getContentPane().removeAll();
//                                                add(new GUI.InGameScreen());
//                                                revalidate();
//                                                repaint();
//                                            }
                                        }
                                    });
        backgroundPanel.add(playButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        backgroundPanel.add(createButton("Help", 300, 60), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        backgroundPanel.add(createButton("Exit", 300, 60), gbc);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createButton(String text, int width, int height) {
        JPanel buttonPanel = new CustomButton(new Color(255, 255, 255, 200), new Color(0, 0, 0, 50));
        buttonPanel.setPreferredSize(new Dimension(width, height));
        buttonPanel.setLayout(new BorderLayout());
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Color.DARK_GRAY);
        label.setFont(buttonFont);
        buttonPanel.add(label, BorderLayout.CENTER);

        buttonPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                buttonPanel.setBackground(new Color(255, 255, 255, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonPanel.setBackground(new Color(255, 255, 255, 200));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                buttonPanel.setBackground(new Color(200, 200, 200, 200));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                buttonPanel.setBackground(new Color(255, 255, 255, 255));
            }

            public void mouseClicked(MouseEvent e) {
                if (text.equals("Play")) {
                    // Assuming this code is inside an ActionListener in your WelcomeScreen
                      getContentPane().removeAll();
                      add(new GUI.InGameScreen());
                      revalidate();
                      repaint();
                }
            }
        });
        return buttonPanel;
    }

    public JButton getPlayButton(){
        return playButton;
    }




    private static class CustomButton extends JPanel {
        private Color backgroundColor;
        private Color borderColor;

        public CustomButton(Color backgroundColor, Color borderColor) {
            this.backgroundColor = backgroundColor;
            this.borderColor = borderColor;
            setOpaque(false); // Indicate that we'll paint our own background
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Background
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            // Border
            g2d.setColor(borderColor);
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            g2d.dispose();
        }

        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            backgroundColor = bg;
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeScreen().setVisible(true));
    }
}
