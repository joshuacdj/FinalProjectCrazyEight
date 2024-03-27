package gui;

import logic.Human;
import logic.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static gui.Sound.*;

public class WinPanel {
    // Defining constants
    private static Color TITLE_COLOUR = new Color(255, 215, 0);
    private static Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);

    public static JPanel winPanel(int width, int height, List<Player> sortedPlayers) {
        JPanel winPanel = getjPanel(width, height);

        // Create and style the title
        JLabel titleLabel = new JLabel("Game Over - Leaderboard");
        titleLabel.setForeground(TITLE_COLOUR);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        // Add title to the end game screen
        winPanel.add(titleLabel);

        // Setting width and height
        int iconWidth = 30;
        int iconHeight = 30;

        // Populate the panel with sorted scores and player rankings
        boolean winSoundPlayed = false;

        // Setting the win panel
        for (int i = 0; i < sortedPlayers.size(); i++) {
            Player player = sortedPlayers.get(i);

            // Stores the position, player name and the number of points that player has in a string
            String rankText = (i + 1) + getPositionSuffix(i + 1) + " - " + player.getName() + ": " + player.calculatePoints();

            // Puts the string into a label so that it can be placed on the win panel
            JLabel playerScoreLabel = new JLabel(rankText);

            // Set font, font size, colour and alignment of the text
            playerScoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            playerScoreLabel.setForeground(Color.WHITE);
            playerScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            playerScoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

            // Sets images for 1st, 2nd and 3rd placed players
            if (i == 0) {
                playerScoreLabel.setIcon(ImageUtility.loadAndScaleCardImage("images/gold_medal.png", iconWidth, iconHeight, false));
            } else if (i == 1) {
                playerScoreLabel.setIcon(ImageUtility.loadAndScaleCardImage("images/silver_medal.png", iconWidth, iconHeight, false));
            } else if (i == 2) {
                playerScoreLabel.setIcon(ImageUtility.loadAndScaleCardImage("images/bronze_medal.png", iconWidth, iconHeight, false));
            }

            // For the 1st placed player only, check if it's a Human to decide the sound
            if (i == 0 && player instanceof Human) {
                // Play the win sound for Human first place
                youWinSound();

                // Set the flag since the win sound is played
                winSoundPlayed = true;
            }
            // Only play the losing sound if the win sound hasn't been played - Human lost the game
            else if (!winSoundPlayed) {
                // This ensures the losing sound is played only if the win sound hasn't been played
                youLoseSound();
            }

            // Adding the leaderboard and rankings to the win panel
            winPanel.add(playerScoreLabel);
        }
        return winPanel;
    }

    private static JPanel getjPanel(int width, int height) {
        // Setting the various dimensions of the win panel
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

    // Creation of the suffixes for the different rankings
    private static String getPositionSuffix(int position) {
        return switch (position) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }
}
