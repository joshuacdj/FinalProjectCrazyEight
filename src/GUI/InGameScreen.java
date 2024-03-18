package GUI;

import javax.swing.*;
import java.awt.*;

public class InGameScreen extends JPanel {

    public InGameScreen() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Common settings for the player panels
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Add the north player panel
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(createPlayerPanel(), gbc);

        // Add the south player panel
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(createPlayerPanel(), gbc);

        // Add the east player panel
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(createPlayerPanel(), gbc);

        // Add the west player panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(createPlayerPanel(), gbc);

        // Add the center panel
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1; // Gives the center panel more weight to expand
        gbc.weighty = 1;
        add(createCenterPanel(), gbc);
    }

    private JPanel createPlayerPanel() {
        JPanel playerPanel = new JPanel();
        playerPanel.setBackground(new Color(240, 240, 240)); // Just to visualize the panel, remove later
        playerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Remove later

        // Your player panel content code here...

        return playerPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 4, 0, 0)); // 1 row, 2 columns with a gap of 10px
        centerPanel.setOpaque(false); // If you want the background to show through

        // Create buttons with image icons
        ImageIcon icon1 = new ImageIcon(new ImageIcon("/Users/jeremaine/Documents/GitHub/CS102ProjectCrazyEightsSwing/cards/8_of_diamonds.png").getImage().getScaledInstance(-1, 200, Image.SCALE_SMOOTH));
        ImageIcon icon2 = new ImageIcon(new ImageIcon("/Users/jeremaine/Documents/GitHub/CS102ProjectCrazyEightsSwing/cards/2_of_diamonds.png").getImage().getScaledInstance(-1, 200, Image.SCALE_SMOOTH));

        JButton button1 = new JButton(icon1);
        JButton button2 = new JButton(icon2);

        // Make buttons transparent
        button1.setBorder(BorderFactory.createEmptyBorder());
        button1.setContentAreaFilled(false);
        button2.setBorder(BorderFactory.createEmptyBorder());
        button2.setContentAreaFilled(false);

        // Add buttons to the center panel
        centerPanel.add(button1);
        centerPanel.add(button2);

        return centerPanel;
    }


    // Main method to test the InGameScreen layout
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("In-Game Screen");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new InGameScreen());
            frame.setPreferredSize(new Dimension(800, 600)); // Preferred initial size
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
