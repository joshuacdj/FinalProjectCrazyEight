package app;

import java.util.*;
import javax.swing.*;
import static gui.Sound.*;
import gui.*;
import logic.*;

public class Controller implements DrawActionListener{
    private WelcomeScreen welcomeScreen = new WelcomeScreen();
    private Round currentRound = new Round();
    private InGameScreen inGameScreen;

    public Controller() {
        // Play the background music
        backGroundMusic();
        welcomeSound();

        // Initialise the game state
        currentRound.roundStart();

        // Implement the draw action listener
        for (Player player : currentRound.getListOfPlayers()) {
            if (player instanceof Computer) {
                ((Computer) player).setDrawActionListener(this);
            }
        }

        // Instantiate the inGameScreen with the current game state
        inGameScreen = new InGameScreen(currentRound, this);

        // Display the welcomeScreen
        welcomeScreen.setVisible(true);

        // Attach listeners to the "Play" and "Exit" button
        welcomeScreen.getPlayButton().addActionListener(e -> startGame());
        welcomeScreen.getExitButton().addActionListener(e -> {
            welcomeClickSound();
            // Display a confirmation dialog
            int confirm = JOptionPane.showConfirmDialog(
                    welcomeScreen,
                    "Are you sure you want to quit the game?",
                    "Quit Game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            // Check if the user confirmed
            if (confirm == JOptionPane.YES_OPTION) {
                // User clicked "YES", so exit the game
                System.exit(0);
            }
            // If "NO" or closed dialog, do nothing and return to the game
        });
    }

    public void compPlay() {
        // Initialise a new thread to add a delay to computer actions
        new Thread(() -> {
            // Loop through the players in this round
            ArrayList<Player> playerList = currentRound.getListOfPlayers();
            for (Player p : playerList) {
                // At the start of every turn, check if the drawpile needs to be restocked
                inGameScreen.restockDrawPile();
                if (p instanceof Computer c) {
                    try {
                        // Log computer action
                        inGameScreen.highlightPlayerTurn(inGameScreen.determineOrientation(c));
                        // Wait for 2 seconds before proceeding to the next iteration
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                    // Get the card and suit played by the computer
                    // The suit may differ from the card played if the computer plays an 8
                    // ArrayList will be null if the computer turn is skipped
                    ArrayList<Object> cardNSuit = c.action(currentRound.getDiscardPile().getTopCard(), currentRound.getDrawPile());

                    SwingUtilities.invokeLater(() -> {
                        if (cardNSuit != null) {
                            dealCardSound();
                            // Get the card and suit played
                            Card cardy = (Card) cardNSuit.get(0);
                            Suit s = (Suit) cardNSuit.get(1);
                            // Add the card played to the discard pile
                            currentRound.getDiscardPile().addCard(cardy);
                            // Set the top card accordingly if card played is not an 8
                            if (cardy.getValue() != 8) {
                                currentRound.getDiscardPile().setTopCard(cardy);
                            } else {
                                // If the top card was an 8, indicate the appropriate suit that was set
                                currentRound.getDiscardPile().setTopCard(new Card(0, s));
                            }
                            // Update GUI after the Computer's turn
                            inGameScreen.refreshPlayerPanel(inGameScreen.determineOrientation(p));
                            inGameScreen.updateDiscardPileImage();
                        }

                        // Once Comp 3 has played, update the ability to draw and/or play cards by human
                        if(c.getName().equals("Comp 3")){
                            inGameScreen.updateDrawPileButton();
                            inGameScreen.setCardAlreadyPlayedByHumanToFalse();
                        }
                    });

                    // If the computer's hand size is empty, end the game
                    if(c.getHand().isEmpty()){
                        endGame();
                        break;
                    }
                }
            }
            // At the end of the third computer playing, highlight the player panel to indicate it is the human's turn
            inGameScreen.highlightPlayerTurn("South");
        }).start();
    }
    public void endGame(){
        // Someone has won the game. End the game and display the leaderboard of the players
        inGameScreen.displayWinPanel();
    }

    private void startGame() {
        // Attach the sound for the "Play" button
        welcomeClickSound();

        // Stop the welcomeScreen's attached sound
        stopSound("welcomeSound");

        // Switch to inGameScreen
        showScreen(inGameScreen);
    }
    
    private void showScreen(JPanel panel) {
        // Switch the screen display to the specified panel
        welcomeScreen.getContentPane().removeAll();
        welcomeScreen.revalidate();
        welcomeScreen.repaint();
        welcomeScreen.setContentPane(panel);
    }

    public void onCardDrawn(Player player) {
        SwingUtilities.invokeLater(() -> {
            if (player instanceof Computer) {
                // Update the computer's playerPanel
                String orientation = inGameScreen.determineOrientation(player);
                inGameScreen.refreshPlayerPanel(orientation);
            }
        });
    }

    public void startNewGame() {
        // Reinitialise a new game
        currentRound = new Round();
        currentRound.roundStart();
        for (Player player : currentRound.getListOfPlayers()) {
            if (player instanceof Computer) {
                ((Computer) player).setDrawActionListener(this);
            }
        }

        // Setup InGameScreen for a new game
        if (inGameScreen != null) {
            inGameScreen.setVisible(false);
            // Ensure the old instance is discarded
            inGameScreen = null;
        }
        inGameScreen = new InGameScreen(currentRound, this);
        showScreen(inGameScreen); // Method to switch the view to InGameScreen, if needed
        inGameScreen.setVisible(true);
    }
}

