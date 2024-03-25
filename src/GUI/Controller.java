package GUI;

import java.lang.reflect.Array;
import java.util.*;
import main.java.*;// Adjust the package path based on your actual structure

import javax.swing.*;

import static GUI.Sound.*;

public class Controller implements DrawActionListener{
    private WelcomeScreen welcomeScreen = new WelcomeScreen();
    private Round currentRound = new Round();
    private InGameScreen inGameScreen;

    private Help help = new Help();

    public Controller() {
        backGroundMusic();
        currentRound.roundStart();
        // After creating Computer instances
        for (Player player : currentRound.getListOfPlayers()) {
            if (player instanceof Computer) {
                ((Computer) player).setDrawActionListener(this);
            }
        }
        inGameScreen = new InGameScreen(currentRound, this);
        // Initialize the main JFrame to hold different screens (panels)
        welcomeScreen.setVisible(true);
        welcomeScreen.getPlayButton().addActionListener(e -> startGame());
        welcomeScreen.getExitButton().addActionListener(e -> {
            // Display a confirmation dialog
            int confirm = JOptionPane.showConfirmDialog(
                    welcomeScreen, // Assuming 'inGameScreen' is the component you want to anchor the dialog to
                    "Are you sure you want to quit the game?", // The message to display
                    "Quit Game", // The title of the dialog window
                    JOptionPane.YES_NO_OPTION, // Option type (Yes/No)
                    JOptionPane.QUESTION_MESSAGE // Message type
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
                if (p instanceof Computer) {
                    Computer c = (Computer) p;
                    try {
                        // Log computer action
                        System.out.println("This is computer " + c.getName() + ":" + c.getHand());
                        inGameScreen.highlightPlayerTurn(inGameScreen.determineOrientation(c));
                        // Wait for 3 seconds before proceeding to the next iteration
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
                            // Updating GUI components must be done on the EDT

                            // Add the card played to the discard pile
                            currentRound.getDiscardPile().addCard(cardy);
                            // Set the top card accordingly if card played is not an 8
                            if (cardy.getValue() != 8) {
                                currentRound.getDiscardPile().setTopCard(cardy);

                            // If the top card was an 8, indicate the appropriate suit that was set
                            } else {
                                currentRound.getDiscardPile().setTopCard(new Card(0, s));
                            }
                            // Update GUI here
                            inGameScreen.refreshPlayerPanel(inGameScreen.determineOrientation(p));
                            inGameScreen.updateDiscardPileImage();
                        } //NOTE BOTTOM 3 LINES SHOULD BE UNNECESSARY
//                        else if (c.getName().equals("Comp 3")) {
//                                inGameScreen.setCardPlayedByHumanToFalse();
//                        }

                        // Once Comp 3 has played, update the ability to draw and/or play cards by human
                        if(c.getName().equals("Comp 3")){
                            inGameScreen.updateDrawPileButton();
                            inGameScreen.setCardPlayedByHumanToFalse();
                        }
                    });

                    // If the computer's hand size is 0, end the game
                    if(c.getHand().size() == 0){
                        inGameScreen.setGameEnd(true);
                        endGame();
                        break;
                    }
                }
            }
            // At the end of the third computer playing, highlight the player panel
            // to indicate it is the human's turn
            inGameScreen.highlightPlayerTurn("South");
        }).start();
    }
    public void endGame(){
        inGameScreen.displayWinPanel();
    }

    private void startGame() {
        showScreen(inGameScreen);
    }
    
    private void showScreen(JPanel panel) {
        // Switching the content pane to display the specified screen
        welcomeScreen.getContentPane().removeAll();
        welcomeScreen.revalidate();
        welcomeScreen.repaint();
        welcomeScreen.setContentPane(panel);
    }

    public void onCardDrawn(Player player) {
        SwingUtilities.invokeLater(() -> {
            if (player instanceof Computer) {
                // Assuming you have a way to get the correct orientation for this computer player
                String orientation = inGameScreen.determineOrientation(player);
                inGameScreen.refreshPlayerPanel(orientation);
            }
        });
    }

    public void startNewGame() {
        // Initialize or reinitialize game state
        currentRound = new Round();
        currentRound.roundStart();
        for (Player player : currentRound.getListOfPlayers()) {
            if (player instanceof Computer) {
                ((Computer) player).setDrawActionListener(this);
            }
        }

        // Setup InGameScreen for a new game
        if (inGameScreen != null) {
            inGameScreen.setVisible(false); // Hide or dispose of the existing InGameScreen
            inGameScreen = null; // Ensure the old instance is discarded
        }
        inGameScreen = new InGameScreen(currentRound, this);
        showScreen(inGameScreen); // Method to switch the view to InGameScreen, if needed
        inGameScreen.setVisible(true);
    }

    public static void main(String[] args) {
        Controller control = new Controller();
    }
}

