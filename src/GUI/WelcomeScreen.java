import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WelcomeScreen extends JFrame {

    private Color lightGreen = new Color(119, 221, 119); // Light green
    private Color darkGreen = new Color(0, 153, 76); // Dark green for contrast
    private Font titleFont = new Font("Segoe Script", Font.BOLD, 60); // Elegant font
    private Font buttonFont = new Font("Segoe UI Symbol", Font.BOLD, 22); // Modern, readable font
    private Font symbolFont = new Font("Segoe UI Symbol", Font.PLAIN, 100); // For card symbols

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
                GradientPaint gradientPaint = new GradientPaint(0, 0, lightGreen, 0, getHeight(), darkGreen);
                g2d.setPaint(gradientPaint);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        getContentPane().add(backgroundPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel titleLabel = new JLabel("Crazy Eights", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        backgroundPanel.add(titleLabel, gbc);

//        JLabel symbolsLabel = new JLabel("♠ ♥ ♣ ♦", SwingConstants.CENTER);
//        symbolsLabel.setFont(symbolFont);
//        symbolsLabel.setForeground(Color.WHITE);
//        backgroundPanel.add(symbolsLabel, gbc);
        // Card images
        JLabel cardLabel1 = new JLabel(new ImageIcon("/Users/jeremaine/Documents/GitHub/CS102ProjectCrazyEightsSwing/cards/8_of_diamonds.png"));
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        backgroundPanel.add(cardLabel1, gbc);

//        JLabel cardLabel2 = new JLabel(new ImageIcon("path/to/8_hearts_image.jpg"));
//        gbc.gridx = 1;
//        gbc.gridy = 1;
//        add(cardLabel2, gbc);
//
//        JLabel cardLabel3 = new JLabel(new ImageIcon("path/to/8_diamonds_image.jpg"));
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        add(cardLabel3, gbc);
//
//        JLabel cardLabel4 = new JLabel(new ImageIcon("path/to/8_spades_image.jpg"));
//        gbc.gridx = 1;
//        gbc.gridy = 2;
//        add(cardLabel4, gbc);

        backgroundPanel.add(createButton("Play", 300, 60), gbc);
        backgroundPanel.add(createButton("Help", 300, 60), gbc);
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
        });

        return buttonPanel;
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
