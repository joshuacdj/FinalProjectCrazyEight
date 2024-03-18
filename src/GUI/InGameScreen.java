import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import game.*;

public class InGameScreen extends JFrame {
    // Labels for displaying the cards and scores
    private JLabel playerCard1Label, playerCard2Label, bankerCard1Label, bankerCard2Label;
    private JLabel playerScoreLabel, bankerScoreLabel;
    private JButton dealButton, exitButton;
    private BaccaratGameLogic gameLogic; // Assuming you have a GameLogic class

    public InGameScreen() {
        setTitle("Baccarat Game");
        setSize(600, 400); // Adjust size as needed
        setLayout(new GridLayout(3, 1)); // Simple layout for demonstration
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        gameLogic = new BaccaratGameLogic(); // Initialize your game logic here

        initUI();
    }

    private void initUI() {
        // Panel for player's cards
        JPanel playerPanel = new JPanel();
        playerPanel.add(new JLabel("Player:"));
        playerCard1Label = new JLabel();
        playerCard2Label = new JLabel();
        playerPanel.add(playerCard1Label);
        playerPanel.add(playerCard2Label);
        playerScoreLabel = new JLabel("Score: 0");
        playerPanel.add(playerScoreLabel);

        // Panel for banker's cards
        JPanel bankerPanel = new JPanel();
        bankerPanel.add(new JLabel("Banker:"));
        bankerCard1Label = new JLabel();
        bankerCard2Label = new JLabel();
        bankerPanel.add(bankerCard1Label);
        bankerPanel.add(bankerCard2Label);
        bankerScoreLabel = new JLabel("Score: 0");
        bankerPanel.add(bankerScoreLabel);

        // Panel for actions
        JPanel actionPanel = new JPanel();
        dealButton = new JButton("Deal");
        dealButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Here, you would call your game logic to deal cards and then update the UI accordingly
//                BaccaratGameLogicGameLogic.newRound();
                updateUI();
            }
        });
        actionPanel.add(dealButton);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        actionPanel.add(exitButton);

        // Add panels to the frame
        add(playerPanel);
        add(bankerPanel);
        add(actionPanel);
    }

    private void updateUI() {
        // This method should update card labels and scores based on the current game state
        // Example: playerCard1Label.setIcon(new ImageIcon(getClass().getResource(gameLogic.getPlayerHand().get(0).getImagePath())));
        // Update scores: playerScoreLabel.setText("Score: " + gameLogic.getPlayerScore());
    }
}

