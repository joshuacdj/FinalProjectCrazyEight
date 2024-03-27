package gui;

import logic.Human;
import logic.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static gui.Sound.*;

public class WinPanel {
    public static JPanel winPanel(int width, int height, List<Player> sortedPlayers) {
        JPanel winPanel = getjPanel(width, height);

        // Step 2: Create and style the title
        JLabel titleLabel = new JLabel("Game Over - Leaderboard");
        titleLabel.setForeground(new Color(255, 215, 0)); // Gold color
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        winPanel.add(titleLabel);

        int iconWidth = 30; // Example width
        int iconHeight = 30; // Example height

        // Step 3: Populate the panel with sorted scores and player rankings
        boolean winSoundPlayed = false;

        for (int i = 0; i < sortedPlayers.size(); i++) {
            Player player = sortedPlayers.get(i);
            String rankText = (i + 1) + getPositionSuffix(i + 1) + " - " + player.getName() + ": " + player.calculatePoints();
            JLabel playerScoreLabel = new JLabel(rankText);
            playerScoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            playerScoreLabel.setForeground(Color.WHITE);
            playerScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            playerScoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            if (i == 0) {
                playerScoreLabel.setIcon(scaleIcon("images/gold_medal.png", iconWidth, iconHeight));
            } else if (i == 1) {
                playerScoreLabel.setIcon(scaleIcon("images/silver_medal.png", iconWidth, iconHeight));
            } else if (i == 2) {
                playerScoreLabel.setIcon(scaleIcon("images/bronze_medal.png", iconWidth, iconHeight));
            }

            // For the first player only, check if its a Human to decide the sound
            if (i == 0 && player instanceof Human) {

                youWinSound(); // Play the win sound for Human first place
                winSoundPlayed = true; // Set the flag since the win sound is played

            } else if (!winSoundPlayed) { // Only play the lose sound if the win sound hasnt been played
                youLoseSound(); // This ensures the lose sound is played only if the win sound hasnt been
            }
            winPanel.add(playerScoreLabel);
        }
        return winPanel;
    }

    private static JPanel getjPanel(int width, int height) {
        JPanel winPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color color1 = new Color(36, 11, 54);
                Color color2 = new Color(91, 76, 121);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        winPanel.setLayout(new BoxLayout(winPanel, BoxLayout.Y_AXIS));
        winPanel.setSize(width, height);
        winPanel.setOpaque(false);
        winPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return winPanel;
    }

    private static String getPositionSuffix(int position) {
        return switch (position) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    private static ImageIcon scaleIcon(String path, int width, int height) {
        // Load the original image
        ImageIcon originalIcon = new ImageIcon(path);
        Image originalImage = originalIcon.getImage();

        // Scale it to fit the UI
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // Return the new ImageIcon
        return new ImageIcon(scaledImage);
    }
}
